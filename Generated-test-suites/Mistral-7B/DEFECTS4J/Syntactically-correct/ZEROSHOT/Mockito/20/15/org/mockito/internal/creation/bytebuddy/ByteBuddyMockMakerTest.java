package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRunner;
import org.mockito.plugins.MockMakerUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ByteBuddyMockMaker.class, GlobalConfiguration.class, ClassInstantiator.class, CachingMockBytecodeGenerator.class})
public class ByteBuddyMockMakerTest {

    private static final Class<ByteBuddyMockMaker> BYTE_BUDDY_MOCK_MAKER_CLASS = ByteBuddyMockMaker.class;

    @Test(expected = MockitoException.class)
    public void testCreateMock_serializableModeAcrossClassLoaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(Object.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_validMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(String.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        assertEquals("", (String) mock);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_classCastException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(String.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler1);
        String stringMock = (String) mock;
        assertEquals("", stringMock);
        verify(handler1).handle(eq(stringMock), any());
        Object wrongTypeMock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotSame(mock, wrongTypeMock);
        assertFalse(wrongTypeMock instanceof String);
        verify(handler1, never()).handle(eq(wrongTypeMock), any());
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_instantiationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(Object.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_unexpectedClassLoadingException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(Object.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            ClassInstantiator initializeClassInstantiator() {
                throw new RuntimeException("Unexpected class loading exception");
            }
        };
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler_validMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(String.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler1);
        assertEquals(handler, handler1);
    }

    @Test
    public void testResetMock_validMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .forType(String.class)
                .build();
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        MockHandler newHandler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler1);
        assertEquals(newHandler, handler1);
    }

    @Test
    public void testDescribeClass() {
        Class<String> stringClass = String.class;
        Object stringMock = Mockito.mock(String.class);
        assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2",
                ByteBuddyMockMaker.describeClass(stringClass));
        assertEquals("class org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest$MockType$1, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2",
                ByteBuddyMockMaker.describeClass(getClass()));
        assertEquals("class java.lang.Object, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2",
                ByteBuddyMockMaker.describeClass(getClass().getSuperclass()));
        assertEquals("null", ByteBuddyMockMaker.describeClass(null));
        assertNull(ByteBuddyMockMaker.describeClass(null));
        assertEquals("class org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest$1, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2",
                ByteBuddyMockMaker.describeClass(PowerMockito.mock(Object.class)));
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertNotNull(ByteBuddyMockMaker.initializeClassInstantiator());
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiator_missingObjenesis() {
        try {
            ByteBuddyMockMaker.initializeClassInstantiator();
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertNotNull(e.getMessage());
            assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.", e.getMessage());
        }
    }
}