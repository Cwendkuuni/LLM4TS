package org.example.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

    @Test
    public void testConstructor() {
        new ReturnsSmartNulls();
    }

    @Test
    public void testAnswerReturnsNullWhenDelegateAnswerReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);

        assertNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testSmartNullIsReturnedWhenUnstubbedMethodIsCalledAndReturnTypeIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Mockito mock = mock(Object.class);
        when(mock.toString()).thenReturn("Mockito");

        InvocationOnMock invocation = createMockInvocation(toStringMethod(mock));

        String result = (String) returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionIsThrownWhenUnstubbedMethodIsCalledAndReturnTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object unmockableObject = new Object() {};

        InvocationOnMock invocation = createMockInvocation(noMethod(unmockableObject.getClass()));

        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testMethodCallMessageIsFormattedCorrectly() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);

        InvocationOnMock invocation = createMockInvocation(toStringMethod(mock));

        String result = (String) returnsSmartNulls.answer(invocation);

        assertEquals("toString(Object[]) called on mock", result);
    }

    private InvocationOnMock createMockInvocation(Method method) {
        Object mock = mock(method.getDeclaringClass());
        when(mock.getClass()).thenReturn(method.getDeclaringClass());
        when(mock.getMethod()).thenReturn(method);
        return new InvocationOnMock(method, new Object[0]);
    }

    private Method toStringMethod(Object mock) {
        try {
            return mock.getClass().getMethod("toString", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method noMethod(Class<?> clazz) {
        try {
            return clazz.getMethod("noSuchMethod", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}