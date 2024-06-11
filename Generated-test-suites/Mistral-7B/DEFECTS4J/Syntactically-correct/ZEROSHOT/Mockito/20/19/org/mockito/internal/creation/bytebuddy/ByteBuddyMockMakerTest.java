package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
        classInstantiator = mockMaker.classInstantiator;
        cachingMockBytecodeGenerator = mockMaker.cachingMockBytecodeGenerator;
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .inPackage("org.mockito.internal.creation.bytebuddy")
                .typeToMock(String.class)
                .serializableMode(SerializableMode.SERIALIZABLE())
                .build();

        MockHandler handler = mock(MockHandler.class);

        String mockedProxyType = mockMaker.createMock(settings, handler).getClass();

        assertThat(mockedProxyType.isInterface(), is(true));
        assertThat(mockedProxyType.isAssignableFrom(String.class), is(true));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .inPackage("org.mockito.internal.creation.bytebuddy")
                .typeToMock(String.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .inPackage("org.mockito.internal.creation.bytebuddy")
                .typeToMock(String.class)
                .serializableMode(SerializableMode.SERIALIZABLE())
                .build();

        MockHandler handler = mock(MockHandler.class);

        Object mockInstance = mockMaker.createMock(settings, handler);

        Class<?> mockedProxyType = cachingMockBytecodeGenerator.get(
                String.class,
                new Class[0]
        );

        throw new AssertionError("Expected ClassCastException but got instance of class " + mockedProxyType.getName());
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .inPackage("org.mockito.internal.creation.bytebuddy")
                .typeToMock(String.class)
                .serializableMode(SerializableMode.SERIALIZABLE())
                .build();

        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        String mockedString = mock(String.class);

        MockHandler handler = mock(MockHandler.class);

        byte[] mockByteCode = new byte[0];
        Class<? extends String> mockedProxyType = Proxy.getProxyClass(String.class.getClassLoader(), new Class[] { String.class });

        String mockInstance = (String) Proxy.newProxyInstance(
                String.class.getClassLoader(),
                new Class[] { String.class },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        handler.handleCall(method, args);
                        return null;
                    }
                }
        );

        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mockInstance;

        MockHandler returnedHandler = mockMaker.getHandler(mockAccess);

        assertThat(returnedHandler, is(handler));
    }

    @Test
    public void testResetMock() {
        String mockedString = mock(String.class);

        MockHandler handler = mock(MockHandler.class);

        mockMaker.resetMock(mockedString, handler, new MockCreationSettings.Builder<String>().build());

        MockHandler resetHandler = mockMaker.getHandler(mockedString);

        assertThat(resetHandler, is(handler));
    }

    @Test
    public void testInitializeClassInstantiator() {
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);

        ClassInstantiator classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(true);

        assertThat(classInstantiator, isInstanceOf(ByteBuddyMockMaker.ClassInstantiator.class));
    }
}