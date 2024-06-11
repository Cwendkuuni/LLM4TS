import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;
    private ClassInstantiator classInstantiatorMock;
    private GlobalConfiguration globalConfigurationMock;
    private MockMakerHandler mockMakerHandlerMock;

    @Before
    public void setup() {
        cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
        classInstantiatorMock = mock(ClassInstantiator.class);
        globalConfigurationMock = mock(GlobalConfiguration.class);
        mockMakerHandlerMock = mock(MockMakerHandler.class);

        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            {
                this.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorMock;
                this.classInstantiator = classInstantiatorMock;
                this.globalConfiguration = globalConfigurationMock;
                this.mockMakerHandler = mockMakerHandlerMock;
            }
        };
    }

    @Test
    public void testCreateMock_ValidSettings_ReturnsMockInstance() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.DEFAULT);
        }};
        MockHandler handler = mock(MockHandler.class);

        when(cachingMockBytecodeGeneratorMock.get(eq(String.class), any()))
                .thenReturn(String.class);

        when(classInstantiatorMock.instantiate(eq(String.class)))
                .thenReturn(new Object());

        Object mockInstance = byteBuddyMockMaker.createMock(settings, handler);

        assertNotNull(mockInstance);
        verify(handler).handle(any(), any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InvalidSerializableMode_ThrowsMockitoException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        }};

        byteBuddyMockMaker.createMock(settings, mock(MockHandler.class));

        // MockitoException is expected to be thrown but not caught here
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_ClassCastException_ThrowsMockitoException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.DEFAULT);
        }};
        MockHandler handler = mock(MockHandler.class);

        when(cachingMockBytecodeGeneratorMock.get(eq(String.class), any()))
                .thenReturn(String[].class);

        byteBuddyMockMaker.createMock(settings, handler);

        // ClassCastException is expected to be thrown but not caught here
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_InstantiationException_ThrowsMockitoException() {
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.DEFAULT);
        }};
        MockHandler handler = mock(MockHandler.class);

        when(cachingMockBytecodeGeneratorMock.get(eq(String.class), any()))
                .thenThrow(new InstantiationException());

        byteBuddyMockMaker.createMock(settings, handler);

        // InstantiationException is expected to be thrown but not caught here
    }

    @Test
    public void testGetHandler_ReturnsMockHandler() {
        Object mock = mock(String.class);

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        assertNotNull(handler);
    }

    @Test   
    public void testResetMock_ResetsMockHandler() {
        Object mock = mock(String.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(String.class);
            setSerializableMode(SerializableMode.DEFAULT);
        }};

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        ArgumentCaptor<MockHandler> handlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
        verify(mock).setMockitoInterceptor(eq(new MockMethodInterceptor(any(), eq(settings))));
        verify(mock).setMockitoInterceptor(handlerCaptor.capture());
        assertEquals(newHandler, handlerCaptor.getValue());
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisAvailable_ReturnsClassInstantiator() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (Throwable throwable) {
            fail("Expected no exception to be thrown");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisNotAvailable_ThrowsIllegalStateException() {
        try {
            doThrow(new ClassNotFoundException("Objenesis not found"))
                    .when(globalConfigurationMock)
                    .getMockComponentFactory(eq(String.class));
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (IllegalStateException e) {
            assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.", e.getMessage());
        }
    }
}