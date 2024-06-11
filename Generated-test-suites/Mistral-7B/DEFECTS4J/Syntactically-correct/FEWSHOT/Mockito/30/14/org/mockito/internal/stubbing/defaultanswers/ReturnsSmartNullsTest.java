package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertEquals(null, returnsSmartNulls.answer(invocation));
        verify(returnsSmartNulls).answer(invocation);
    }

    @Test
    public void testAnswerNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(int[].class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(null, returnsSmartNulls.answer(invocation));
        verify(returnsSmartNulls).answer(invocation);
    }

    @Test
    public void testAnswerWithToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(String.class);
        when(method.getName()).thenReturn("toString");
        when(invocation.getMethod()).thenReturn(method);
        String result = (String) returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test
    public void testAnswerWithThrowingInterceptor() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(method.getReturnType()).thenReturn(String.class);
        when(invocation.getMethod()).thenReturn(method);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(imposterizer);
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
        when(imposterizer.imposterise(throwingInterceptor, String.class)).thenReturn("SmartNull");
        String result = (String) returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull", result);
    }

    @Test
    public void testAnswerWithDelegate() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(returnsSmartNulls.delegate).thenReturn(delegate);
        Object delegateResult = new Object();
        when(delegate.answer(invocation)).thenReturn(delegateResult);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(delegateResult, result);
    }

    @Test
    public void testAnswerWithDelegateNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(Answer.class);
        when(returnsSmartNulls.delegate).thenReturn(delegate);
        when(delegate.answer(invocation)).thenReturn(null);
        assertNull(returnsSmartNulls.answer(invocation));
        verify(returnsSmartNulls).answer(invocation);
    }

    @Test
    public void testToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertEquals("ReturnsSmartNulls", returnsSmartNulls.toString());
    }
}