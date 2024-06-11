package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerNull() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        invocation.setTarget(mock);

        assertNull(returnsSmartNulls.answer(invocation));

        verify(mock).toString(); // verify that toString() was called
    }

    @Test
    public void testAnswerString() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        String mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        invocation.setTarget(mock);

        String result = (String) returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", result);

        verify(mock).toString(); // verify that toString() was called
    }

    @Test
    public void testAnswerObject() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(Object.class);
        invocation.setTarget(mock);

        assertNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswerClassThatCanBeImposterised() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object mock = mock(ClassImposterizer.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(ClassImposterizer.class);
        invocation.setTarget(mock);

        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals(ClassImposterizer.class, result.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerNullType() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(null);
        invocation.setTarget(mock);

        returnsSmartNulls.answer(invocation); // should throw NPE
    }

    @Test
    public void testThrowingInterceptorToString() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        invocation.setTarget(mock);

        String result = (String) returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", result);

        verify(mock).toString(); // verify that toString() was called
    }

    @Test
    public void testThrowingInterceptorNonToString() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);

        MethodProxy proxy = mock(MethodProxy.class);
        Class<?> type = mock(Class.class);
        type.getDeclaredMethod("toString").setAccessible(true);
        MethodInterceptor interceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
        verify(mock).toString(); // verify that toString() was not called
    }
}