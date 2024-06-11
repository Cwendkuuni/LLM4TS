import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMockException;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.invocation.MockMethodInterceptor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRegistry;
import org.mockito.plugins.MockMakerType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiatorSpy;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;

    @Before
    public void setup() {
        cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
        classInstantiatorSpy = spy(new ClassInstantiator() {
            @Override
            public Object instantiate(Class<?> aClass) throws InstantiationException {
                return null;
            }
        });
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
                return null;
            }

            @Override
            protected ClassInstantiator getClassInstantiator() {
                return classInstantiatorSpy;
            }

            @Override
            protected CachingMockBytecodeGenerator getCachingMockBytecodeGenerator() {
                return cachingMockBytecodeGeneratorMock;
            }
        };
        MockMakerRegistry.instance().registerMockMaker(MockMakerType.JUNIT, byteBuddyMockMaker);
    }

    @Test
    public void testCreateMockWithValidArguments() {
        MockHandler mockHandler = mock(MockMethodInterceptor.class);
        byteBuddyMockMaker.createMock(MockCreationSettings.forClass(String.class), mockHandler);
        assertNotNull(mockHandler.getMockitoInterceptor().getMock());
        verify(cachingMockBytecodeGeneratorMock).get(eq(String.class), any());
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockHandler mockHandler = mock(MockMethodInterceptor.class);
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(
                MockCreationSettings.forClass(String.class).serializableMode(SerializableMode.ACROSS_CLASSLOADERS), mockHandler));
    }

    @Test
    public void testCreateMockWithThrowableInstantiationException() {
        doThrow(new InstantiationException()).when(classInstantiatorSpy).instantiate(MockCreationSettings.forClass(String.class).getMockedType());
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(MockCreationSettings.forClass(String.class), mock(MockHandler.class)));
    }

    @Test
    public void testCreateMockWithClassCastException() {
        Class<?> mockedProxyType = mock(Class.class);
        MockHandler mockHandler = mock(MockMethodInterceptor.class);
        ArgumentCaptor<Object> mockCaptor = ArgumentCaptor.forClass(MockMethodInterceptor.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(String.class, mockedProxyType, null), mockHandler);
        verify(classInstantiatorSpy).instantiate(mockedProxyType);
        byteBuddyMockMaker.resetMock(mockedProxyType, mockHandler, new MockCreationSettings<>(String.class, mockedProxyType, null));
        assertThrows(ClassCastException.class, () -> byteBuddyMockMaker.getHandler(mockedProxyType));
    }

    @Test
    public void testCreateMockWithThrowableMockMakerException() {
        try {
            Class<?> mockType = Class.forName("NonExistentClass");
            byteBuddyMockMaker.createMock(MockCreationSettings.forClass(mockType), mock(MockHandler.class));
        } catch (ClassNotFoundException e) {
            throw new MockitoException("Unable to create mock instance of type 'NonExistentClass'", e);
        }
    }

    @Test
    public void testGetHandlerWithValidMock() {
        MockHandler mockHandler = mock(MockMethodInterceptor.class);
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.forClass(String.class), mockHandler);
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        assertNull(byteBuddyMockMaker.getHandler(new Object(){}));
    }

    @Test
    public void testResetMockWithValidMock() {
        MockHandler mockHandler = mock(MockMethodInterceptor.class);
        Object mock = byteBuddyMockMaker.createMock(MockCreationSettings.forClass(String.class), mockHandler);
        byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(String.class, mock.getClass(), null));
        assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMockWithInvalidMock() {
        Object mock = new Object();
        assertThrows(NullPointerException.class, () -> byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), new MockCreationSettings<>(String.class, mock.getClass(), null)));
    }

    @Test
    public void testInitializeClassInstantiatorWithObjenesis() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockMakerRegistry.instance().unregisterMockMaker(byteBuddyMockMaker.getClass());
        Class<?> objenesisClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> constructor = objenesisClass.getDeclaredConstructor(boolean.class);
        Object objenesisInstance = constructor.newInstance(true);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            protected ClassInstantiator getClassInstantiator() {
                ClassInstantiator classInstantiator = (ClassInstantiator) objenesisInstance;
                return classInstantiator;
            }
        };
        MockMakerRegistry.instance().registerMockMaker(MockMakerType.JUNIT, byteBuddyMockMaker);
    }

    @Test
    public void testInitializeClassInstantiatorWithInvalidObjenesis() {
        Class<?> objenesisClass = Class.forName("NonExistentClassForObjenesis");
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            protected ClassInstantiator getClassInstantiator() {
                return (ClassInstantiator) Class.forName(objenesisClass.getName()).newInstance();
            }
        };
        assertThrows(ClassNotFoundException.class, () -> {
            MockMakerRegistry.instance().registerMockMaker(MockMakerType.JUNIT, byteBuddyMockMaker);
        });
    }
}