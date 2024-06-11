package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethodForNullArgument() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        invocation.getArgumentAt(0, Object.class, null);
        answer.answer(invocation);

        // No interaction with the real method was recorded
        verify(mockedObject, never()).someMethod(null);
    }

    @Test
    public void testCallsRealMethodForNonVoidReturnType() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);
        when(mockedObject.someMethod(anyInt())).thenReturn("Some value");

        Object returnValue = answer.answer(mock(InvocationOnMock.class));

        assertEquals("Some value", returnValue);
        verify(mockedObject).someMethod(anyInt());
    }

    @Test
    public void testCallsRealMethodForVoidReturnType() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);

        answer.answer(mock(InvocationOnMock.class));

        // No interaction with the real method was recorded
        verify(mockedObject, never()).someVoidMethod();
    }

    @Test
    public void testCallsRealMethodWithArguments() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);
        when(mockedObject.someMethod(anyInt(), anyString())).thenReturn(123);

        Object[] arguments = new Object[]{1, "test"};
        Object returnValue = answer.answer(mock(InvocationOnMock.class), arguments);

        assertEquals(123, returnValue);
        verify(mockedObject).someMethod(1, "test");
    }

    @Test
    public void testCallsRealMethodThrowsException() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);
        when(mockedObject.someMethod(anyInt())).thenThrow(new RuntimeException("Test exception"));

        try {
            answer.answer(mock(InvocationOnMock.class));
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }

        verify(mockedObject).someMethod(anyInt());
    }

    @Test
    public void testCallsRealMethodWithAnswer() {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockedObject = mock(Object.class);
        when(mockedObject.someMethod(anyInt())).thenReturn(123);

        Answer<Object> stubbedAnswer = mock(Answer.class);
        when(stubbedAnswer.answer(mock(InvocationOnMock.class))).thenReturn("Answer value");

        Object returnValue = answer.answer(mock(InvocationOnMock.class), stubbedAnswer);

        assertEquals("Answer value", returnValue);
        verify(mockedObject).someMethod(anyInt());
    }
}