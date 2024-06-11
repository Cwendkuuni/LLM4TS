package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullForUnstubbedMethodWithStringReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("mockString");

        String result = (String) returnsSmartNulls.answer(new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{}), null));

        assertNull(result);
        verify(mock).toString();
    }

    @Test
    public void testReturnsSmartNullForUnstubbedMethodWithObjectReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(Object.class);

        Object result = returnsSmartNulls.answer(new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{}), null));

        assertNull(result);
    }

    @Test
    public void testReturnsDefaultReturnValueForStubbedMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("mockString");
        when(returnsSmartNulls).thenReturn(mock);

        String result = (String) returnsSmartNulls.answer(new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{}), null));

        assertNotNull(result);
        assertEquals("mockString", result);
    }

    @Test
    public void testThrowsNpeWhenCalledWithNullArgument() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);

        try {
            returnsSmartNulls.answer(null);
        } catch (NullPointerException e) {
            assertNull(e.getCause());
            return;
        }

        fail("Expected NullPointerException");
    }

    @Test
    public void testDoesNotInvokeToStringMethodWhenReturnTypeIsNotString() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(Number.class);

        InvocationOnMock invocation = new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{}), null);
        ArgumentCaptor<Throwable> throwingExceptionCaptor = ArgumentCaptor.forClass(Throwable.class);

        returnsSmartNulls.answer(invocation);

        verifyZeroInteractions(mock);
        verify(returnsSmartNulls).smartNullPointerException(any(Location.class));
        assertNotNull(invocation.getArguments()[0]);
        assertNotNull(invocation.getTarget());
        assertEquals("SmartNull returned by unstubbed answer() method on ReturnsSmartNulls", invocation.getMethod().getName());
        assertEquals(ThrowingInterceptor.class, invocation.getMethod().getInterceptorType());
    }

    @Test
    public void testReturnsMockInstanceWhenReturnTypeIsMockable() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);

        when(returnsSmartNulls).thenReturn(mock);

        Object result = returnsSmartNulls.answer(new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{}), null));

        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test
    public void testReturnsNullWhenReturnTypeIsFinal() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        final String FINAL_STRING = "finalString";

        Object result = returnsSmartNulls.answer(new InvocationOnMock(ReturnsSmartNulls.class, "answer", new Object[]{}, new Method(ReturnsSmartNulls.class, "answer", new Class[]{String.class}), null));

        assertNull(result);
    }
}