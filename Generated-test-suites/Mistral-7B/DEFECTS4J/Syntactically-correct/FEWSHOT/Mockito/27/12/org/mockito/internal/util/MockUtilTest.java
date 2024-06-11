package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockitoConstants;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        Class<?>[] interfaces = settings.getExtraInterfaces();
        if (interfaces != null) {
            for (Class<?> interfaceClass : interfaces) {
                assertNotNull(Proxy.getProxyClass(new Class[]{classToMock, interfaceClass}));
            }
        }

        verifyNoInteractions(mock);
    }

    @Test
    public void testCreateMockSerializable() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        Class<?>[] interfaces = settings.getExtraInterfaces();
        if (interfaces != null) {
            for (Class<?> interfaceClass : interfaces) {
                assertNotNull(Proxy.getProxyClass(new Class[]{classToMock, interfaceClass, Serializable.class}));
            }
        }

        verifyNoInteractions(mock);
    }

    @Test
    public void testCreateMockSpiedInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<String> classToMock = String.class;
        Object spiedInstance = "test";
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        MethodInterceptorFilter methodInterceptorFilter = mockUtil.newMethodInterceptorFilter(settings);
        Whitebox.setInternalState(mock, "interceptor", methodInterceptorFilter);

        String result = (String) Whitebox.invokeMethod(mock, "toString");
        assertEquals("test", result);

        verify(spiedInstance).toString();
    }

    @Test
    public void testCreateMockInvalidType() {
        Class<Integer> classToMock = Integer.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testCreateMockExtraInterfaces() {
        Class<String> classToMock = String.class;
        Class<Serializable> serializableInterface = Serializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{serializableInterface});

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        Class<?>[] interfaces = settings.getExtraInterfaces();
        assertArrayEquals(new Class<?>[]{serializableInterface}, interfaces);

        verifyNoInteractions(mock);
    }

    @Test
    public void testCreateMockMockedType() {
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(mock(String.class));

        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testResetMock() {
        Object mock = mock(MockHandlerInterface.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        MockHandlerInterface newMockHandler = (MockHandlerInterface) getMockHandler(mock);
        NewMockHandler newNewMockHandler = new NewMockHandler(newMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newNewMockHandler, new MockSettingsImpl());

        ((Factory) mock).setCallback(0, newFilter);

        verify(mock).reset();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(MockHandlerInterface.class);
        MockUtil mockUtil = new MockUtil();
        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockNull() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMockitoMock(null));
    }

    @Test
    public void testIsMockitoMockNotMockitoMock() {
        Object nonMockitoMock = new Object();
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Factory.class);
        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter methodInterceptorFilter = new MethodInterceptorFilter(new MockHandler<>(null), new MockSettingsImpl());
        Whitebox.setInternalState(mock, "callback", methodInterceptorFilter);

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        assertEquals(methodInterceptorFilter, result);
    }

    @Test
    public void testGetInterceptorNull() {
        Object mock = mock(Factory.class);
        MockUtil mockUtil = new MockUtil();

        assertNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(MockHandlerInterface.class);
        MockUtil mockUtil = new MockUtil();

        String result = mockUtil.getMockName(mock);
        assertNotNull(result);
        assertEquals(MockitoConstants.MOCKITO_MOCK_NAME_PREFIX + mock.hashCode(), result);
    }

    private MockSettingsImpl createMockSettings() {
        return new MockSettingsImpl();
    }

    private <T> NewMockHandler<T> newMockHandler(MockHandler<T> mockHandler) {
        return new NewMockHandler<>(mockHandler);
    }

    private MockHandlerInterface<Object> getMockHandler(Object mock) {
        return (MockHandlerInterface<Object>) ReflectionUtils.getFieldValue(mock, "handler");
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (!expected.getClass().isAssignableFrom(actual.getClass())) {
            fail("Unexpected array types.");
        }
        assertArrayEquals((Object[]) expected, actual);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Arrays have different length.");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private static class NewMockHandler<T> implements MockHandler<T> {
        private final MockHandler<T> delegate;

        public NewMockHandler(MockHandler<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void handle(MethodInvocation invocation) throws Throwable {
            delegate.handle(invocation);
        }
    }
}