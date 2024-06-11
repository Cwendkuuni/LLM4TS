package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(byteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        when(byteBuddyMockMaker.new CachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
    }

    @Test
    public void testCreateMockNoException() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT);
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertEquals(Object.class, mock.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(Object.class, SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(String.class, SerializableMode.DEFAULT);
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockInstantiationException() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT);
        when(classInstantiator.instantiate(eq(Object.class))).thenThrow(new InstantiationException());
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        when(byteBuddyMockMaker.getHandler(eq(mock))).thenReturn(handler);
        assertEquals(handler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(Object.class, SerializableMode.DEFAULT);
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        MockHandler oldHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotEquals(newHandler, oldHandler);
        assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorException() {
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis"))
                .thenThrow(new ClassNotFoundException());
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

}