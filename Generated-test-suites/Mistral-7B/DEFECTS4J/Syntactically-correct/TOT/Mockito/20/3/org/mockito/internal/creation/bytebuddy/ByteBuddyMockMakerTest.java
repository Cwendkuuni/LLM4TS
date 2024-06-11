import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;

    @Before
    public void setup() {
        this.mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMockWithValidArguments() {
        Type mockedType = Mockito.class;
        Class<?> mockedInterface = Mockito.class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        MockCreationSettings<Mockito> settings = new MockCreationSettings.Builder()
                .typeToMock(mockedType)
                .extraInterfaces(mockedInterface)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        Mockito.verify(mockHandler).handle(any(), eq(settings));

        Object mock = mockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);
        assertEquals(mockedType, mock.getClass().getSuperclass());
        assertEquals(mockedInterface, mock.getClass().getInterfaces()[0]);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        Type mockedType = Mockito.class;
        Class<?> mockedInterface = Mockito.class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        MockCreationSettings<Mockito> settings = new MockCreationSettings.Builder()
                .typeToMock(mockedType)
                .extraInterfaces(mockedInterface)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        try {
            mockMaker.createMock(settings, mockHandler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        Type mockedType = Mockito.class;
        Object mock = createMockWithHandler(mockedType);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        Type mockedType = Integer.class;
        Class<?> mockedInterface = Number.class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mock = createMockWithHandler(mockedType);

        InternalMockHandler internalMockHandler = (InternalMockHandler) mockMaker.getHandler(mock);
        MockMethodInterceptor mockMethodInterceptor = internalMockHandler.getMockitoInterceptor();

        MockCreationSettings<Integer> settings = new MockCreationSettings.Builder()
                .typeToMock(mockedType)
                .extraInterfaces(mockedInterface)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        mockMaker.resetMock(mock, mockHandler, settings);

        InternalMockHandler newInternalMockHandler = (InternalMockHandler) mockMaker.getHandler(mock);
        MockMethodInterceptor newMockMethodInterceptor = newInternalMockHandler.getMockitoInterceptor();

        assertEquals(mockMethodInterceptor, newMockMethodInterceptor);
    }

    private Object createMockWithHandler(Type mockedType) {
        Class<?> mockedInterface = mockedType.getClass();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);

        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .typeToMock(mockedType)
                .extraInterfaces(mockedInterface)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        Object mock = mockMaker.createMock(settings, mockHandler);
        return mock;
    }
}