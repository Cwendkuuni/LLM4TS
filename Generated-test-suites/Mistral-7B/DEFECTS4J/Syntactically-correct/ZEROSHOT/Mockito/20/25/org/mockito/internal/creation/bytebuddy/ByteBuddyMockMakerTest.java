package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.StringJoiner.join;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.util.MockCreationSettings;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;
import org.powermock.modules.testng.PowerMockRule;

import java.lang.reflect.Constructor;
import java.lang.reflect.UndeclaredThrowableException;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    private ClassInstantiator classInstantiator;

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        classInstantiator = mock(ClassInstantiator.class);
        when(byteBuddyMockMaker.cachingMockBytecodeGenerator).thenReturn(cachingMockBytecodeGenerator);
        when(byteBuddyMockMaker.classInstantiator).thenReturn(classInstantiator);
    }

    @Test
    public void testCreateMock_ValidSettingsAndHandler() {
        MockCreationSettings settings = new MockCreationSettings(String.class, null);
        MockHandler handler = mock(MockHandler.class);
        when(classInstantiator.instantiate(MockitoMockType.class)).thenReturn(new Object());

        byteBuddyMockMaker.createMock(settings, handler);

        verify(cachingMockBytecodeGenerator).get(eq(String.class), isNull());
        verify(classInstantiator).instantiate(eq(MockitoMockType.class));
        verify(handler).handle(any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassloaders() {
        MockCreationSettings settings = new MockCreationSettings(String.class, null);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = mock(MockHandler.class);

        byteBuddyMockMaker.createMock(settings, handler);

        verifyZeroInteractions(cachingMockBytecodeGenerator);
        verifyZeroInteractions(classInstantiator);
        verifyZeroInteractions(handler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_ClassCastException() {
        MockCreationSettings settings = new MockCreationSettings(String.class, null);
        MockHandler handler = mock(MockHandler.class);
        when(classInstantiator.instantiate(MockitoMockType.class)).thenReturn(new Object()[0]);

        byteBuddyMockMaker.createMock(settings, handler);

        verifyZeroInteractions(cachingMockBytecodeGenerator);
        verifyZeroInteractions(classInstantiator);
        verifyZeroInteractions(handler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_InstantiationException() {
        MockCreationSettings settings = new MockCreationSettings(String.class, null);
        MockHandler handler = mock(MockHandler.class);
        when(classInstantiator.instantiate(MockitoMockType.class)).throwing(new InstantiationException());

        byteBuddyMockMaker.createMock(settings, handler);

        verifyZeroInteractions(cachingMockBytecodeGenerator);
        verifyZeroInteractions(classInstantiator);
        verifyZeroInteractions(handler);
    }

    @Test
    public void testGetHandler() {
        Object mock = spy(new Object());

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        Object mock = spy(new Object());
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings(String.class, null);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        verify(mock).setMockitoInterceptor(any());
        verifyZeroInteractions(cachingMockBytecodeGenerator);
        verifyZeroInteractions(classInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_MissingObjenesis() {
        byteBuddyMockMaker.initializeClassInstantiator();

        verifyZeroInteractions(cachingMockBytecodeGenerator);
        verifyZeroInteractions(classInstantiator);
    }

    private static class MockitoMockType {}
}