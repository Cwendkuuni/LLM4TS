package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockHandler;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.testng.PowerMockRule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockMakerMockHandler handler;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setUp() {
        GlobalConfiguration configuration = new GlobalConfiguration();
        configuration.enableClassCache();
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        handler = new MockMakerMockHandler();
    }

    @Test
    public void testCreateMock() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);

        MockHandler handlerSpy = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handlerSpy);

        assertEquals(typeToMock, Whitebox.getInternalState(handlerSpy.getMock()));
        verify(handlerSpy).handle(ArgumentMatchers.any(), ArgumentMatchers.eq(settings));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);

        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);
        T mock = byteBuddyMockMaker.createMock(settings, handler);

        MockHandler handlerFromByteBuddyMockMaker = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handlerSpy, handlerFromByteBuddyMockMaker);
    }

    @Test
    public void testResetMock() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);
        T mock = byteBuddyMockMaker.createMock(settings, handler);

        byteBuddyMockMaker.resetMock(mock, handlerSpy, settings);

        MockHandler handlerFromByteBuddyMockMaker = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handlerSpy, handlerFromByteBuddyMockMaker);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithClassCastException() throws Exception {
        Class<Number> numberClass = Number.class;
        MockCreationSettings<Number> settings = new MockCreationSettings<>(numberClass, handler);

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);

        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            public Class<? extends T> instantiate(Class<T> aClass) throws InstantiationException {
                throw new InstantiationException();
            }
        };

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithMockitoException() throws Exception {
        Class<String> typeToMock = String.class;
        MockCreationSettings<String> settings = new MockCreationSettings<>(typeToMock, handler);

        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            public Class<? extends T> get(Class<T> aClass, Class<?>... aClass1) throws MockitoException {
                throw new MockitoException("Exception thrown for testing purposes");
            }
        };

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorException() throws Exception {
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            public ClassInstantiator initializeClassInstantiator() {
                throw new IllegalStateException();
            }
        };
    }
}