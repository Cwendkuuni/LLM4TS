package org.mockito.internal.creation.bytebuddy.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.ByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        classInstantiator = spy(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.classInstantiator = classInstantiator;
        byteBuddyMockMaker.cachingMockBytecodeGenerator = cachingMockBytecodeGenerator;
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.forClass(Object.class);
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidSerializableMode() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockWithClassCastException() {
        MockHandler mockHandler = mock(MockHandler.class);
        Class<Object> mockedProxyType = Object.class;
        Object mockInstance = new Object() {};
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(mockedProxyType, null), mockHandler);
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(new MockCreationSettings<>(mockedProxyType, null), mockInstance);
    }

    @Test
    public void testGetHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(Object.class, null), mockHandler);
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(Object.class, null), mockHandler);
        byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(Object.class, null));
        assertNull(byteBuddyMockMaker.getHandler(mock));
        byteBuddyMockMaker.setHandler(mock, mockHandler);
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertNull(byteBuddyMockMaker.classInstantiator);
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> objenesisClassLoader = null;
        try {
            objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        } catch (ClassNotFoundException e) {
            byteBuddyMockMaker.initializeClassInstantiator();
        }
        assertNotNull(byteBuddyMockMaker.classInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenObjenesisIsMissing() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}