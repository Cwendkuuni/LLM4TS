package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsDefaultReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object defaultReturnValue = new Object();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(returnsSmartNulls.answer(invocation)).thenReturn(defaultReturnValue);

        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswerReturnsNullForNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(String[].class);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerReturnsSmartNullForMockableType() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(String.class);

        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result.toString());

        verify(returnsSmartNulls).new ThrowingInterceptor(invocation);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerThrowsExceptionForToStringMethod() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("toString");

        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        throwingInterceptor.intercept(null, method, new Object[0], mock(MethodProxy.class));
    }
}