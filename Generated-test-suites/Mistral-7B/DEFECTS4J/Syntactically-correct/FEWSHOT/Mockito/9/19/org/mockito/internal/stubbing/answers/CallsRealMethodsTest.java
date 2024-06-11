package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMock().equals(realObject);

        Object result = answer.answer(invocation);

        assertEquals(result, realObject);
        verify(realObject).equals(realObject);
    }

    @Test
    public void testCallRealMethodWithParameter() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMock().equals(realObject);
        invocation.getArguments()[0] = "test";

        Object result = answer.answer(invocation);

        assertEquals(result, realObject);
        verify(realObject).equals(realObject);
        verify(realObject).equals("test");
    }

    @Test
    public void testCallRealMethodWithReturnValue() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMock().equals(realObject);
        invocation.getMethod().returnsFirstArgument();

        Object result = answer.answer(invocation);

        assertEquals(result, realObject);
        verify(realObject, Mockito.never()).equals(realObject);
        assertEquals(result, realObject);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCallRealMethodWithThrowable() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMock().equals(realObject);
        invocation.getMethod().throwsException(new RuntimeException("Test Exception"));

        answer.answer(invocation);
    }
}