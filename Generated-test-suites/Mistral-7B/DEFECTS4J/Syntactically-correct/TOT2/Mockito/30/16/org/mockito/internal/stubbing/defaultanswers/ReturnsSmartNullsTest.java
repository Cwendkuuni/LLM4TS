package org.example.junit.test.cases.ReturnsSmartNullsTest;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.SmartNullPointerException;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsSmartNull() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object returnValue = answer.answer(invocationMock);
        verify(invocationMock).answer(any());
        assertEquals("SmartNull returned by unstubbed method", returnValue.toString());
    }

    @Test(expected = SmartNullPointerException.class)
    public void testAnswer_ThrowsSmartNullPointerException_WhenUnstubbedMethodWithPrintableName_ReturnTypeIsNotMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")));
        answer.answer(invocationMock);
    }

    @Test
    public void testAnswer_ReturnsNull_WhenDelegateAnswerReturnsNotNull_AndReturnTypeIsMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(answer.delegate.answer(invocationMock)).thenReturn("Test String");
        Object returnValue = answer.answer(invocationMock);
        assertNotNull(returnValue);
        assertEquals("Test String", returnValue);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_ThrowsNullPointerException_WhenDelegateAnswerReturnsNull_AndReturnTypeIsNotMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object returnValue = answer.answer(invocationMock);
        assertNull(returnValue);
        verify(invocationMock).answer(ArgumentMatchers.any());
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenUnstubbedMethodWithPrintableName_ReturnTypeIsMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")));
        Object returnValue = answer.answer(invocationMock);
        assertEquals("SmartNull returned by unstubbed toString method on mock", returnValue.toString());
    }

    @Test(expected = SmartNullPointerException.class)
    public void testAnswer_ThrowsSmartNullPointerException_WhenUnstubbedMethodWithPrintableName_ReturnTypeIsNotMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("value")));
        answer.answer(invocationMock);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenUnstubbedMethodWithEmptyArguments_AndReturnTypeIsMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object[] emptyArguments = new Object[0];
        when(invocationMock.getArguments()).thenReturn(emptyArguments);
        Object returnValue = answer.answer(invocationMock);
        assertEquals("SmartNull returned by unstubbed method with empty arguments", returnValue.toString());
    }

    @Test(expected = SmartNullPointerException.class)
    public void testAnswer_ThrowsSmartNullPointerException_WhenUnstubbedMethodWithEmptyArguments_AndReturnTypeIsNotMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object[] emptyArguments = new Object[0];
        when(invocationMock.getArguments()).thenReturn(emptyArguments);
        when(invocationMock.getMethod()).thenReturn(new Method(Integer.TYPE.getDeclaredMethod("intValue")));
        answer.answer(invocationMock);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_WhenUnstubbedMethodWithNonEmptyArguments_AndReturnTypeIsMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object[] nonEmptyArguments = new Object[]{1, 2, 3};
        when(invocationMock.getArguments()).thenReturn(nonEmptyArguments);
        Object returnValue = answer.answer(invocationMock);
        assertEquals("SmartNull returned by unstubbed method with non-empty arguments", returnValue.toString());
    }

    @Test(expected = SmartNullPointerException.class)
    public void testAnswer_ThrowsSmartNullPointerException_WhenUnstubbedMethodWithNonEmptyArguments_AndReturnTypeIsNotMockable() throws Exception {
        Answer<Object> answer = new ReturnsSmartNulls();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        Object[] nonEmptyArguments = new Object[]{1, 2, 3};
        when(invocationMock.getArguments()).thenReturn(nonEmptyArguments);
        when(invocationMock.getMethod()).thenReturn(new Method(int[].class.getDeclaredMethod("length")));
        answer.answer(invocationMock);
    }
}