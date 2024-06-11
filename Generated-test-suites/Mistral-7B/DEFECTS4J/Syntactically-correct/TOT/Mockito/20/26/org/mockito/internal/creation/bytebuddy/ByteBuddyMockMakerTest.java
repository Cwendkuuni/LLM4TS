import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockFactory;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockMakerMockFactory mockMakerMockFactory;
    private MockHandler mockHandler;
    private InternalMockHandler internalMockHandler;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockMakerMockFactory = Mockito.mock(MockMakerMockFactory.class);
        mockHandler = mock(MockHandler.class);
        internalMockHandler = new InternalMockHandler(mockMakerMockFactory, null);
        Mockito.when(mockMakerMockFactory.createMockMaker()).thenReturn(byteBuddyMockMaker);
    }

    @Test
    public void testCreateMock() {
        // Test creating a mock with default settings
        Type typeToMock = String[].class;
        MockCreationSettings<String[]> settings = new MockCreationSettings(typeToMock, null, null, SerializableMode.NONE, Strictness.LENIENT);
        String[] mockedArray = byteBuddyMockMaker.createMock(settings, internalMockHandler);
        assertNotNull(mockedArray);
        assertEquals(mockedArray.length, 0);

        // Test throwing exception when serializableMode is ACROSS_CLASSLOADERS
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        try {
            byteBuddyMockMaker.createMock(settings, internalMockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }

        // Test exception handling when instantiation fails
        doAnswer((Answer<Object>) invocation -> {
            throw new InstantiationException("Unable to create mock instance");
        }).when(classInstantiator).instantiate(any(Class.class));
        try {
            byteBuddyMockMaker.createMock(settings, internalMockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'java.lang.Object'", e.getMessage());
        }

        // Test setting the mock handler
        ArgumentCaptor<MockMethodInterceptor.MockAccess> mockAccessCaptor = ArgumentCaptor.forClass(MockMethodInterceptor.MockAccess.class);
        byteBuddyMockMaker.createMock(settings, internalMockHandler);
        verify(mockAccessCaptor.getValue()).setMockitoInterceptor(any(MockMethodInterceptor.class));

        // Test getting the mock handler
        Object mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assertEquals(internalMockHandler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        // Test resetting the mock handler
        String[] mockedArray = (String[]) byteBuddyMockMaker.createMock(new MockCreationSettings<>(String[].class, null, null, SerializableMode.NONE, Strictness.LENIENT), internalMockHandler);
        byteBuddyMockMaker.resetMock(mockedArray, mockHandler, new MockCreationSettings<>(String[].class, null, null, SerializableMode.NONE, Strictness.LENIENT));
        MockHandler resetHandler = byteBuddyMockMaker.getHandler(mockedArray);
        assertNotNull(resetHandler);
        assertEquals(mockHandler, resetHandler);

        // Test that resetting the mock does not affect the original handler
        ArgumentCaptor<MockMethodInterceptor.MockAccess> mockAccessCaptor = ArgumentCaptor.forClass(MockMethodInterceptor.MockAccess.class);
        verify(mockAccessCaptor.getValue(), Mockito.atLeastOnce()).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verify(mockAccessCaptor.getValue(), never()).setMockitoInterceptor(eq(internalMockHandler));
    }
}