package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMethod()).thenReturn(new Method(String.class.getName(), "testMethod", new Class[0]));

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerSmartNull() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMethod()).thenReturn(new Method(String.class.getName(), "toString", new Class[0]));

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
    }

    @Test
    public void testAnswerDefaultInstance() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMethod()).thenReturn(new Method("java.util.Collections", "emptyList", new Class[0]));

        Object result = returnsSmartNulls.answer(invocation);
        assertNotSame(Arrays.asList(new Object[0]).getClass(), result.getClass());
    }

    @Test
    public void testAnswerInstance() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        Object instance = mock(List.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{instance});
        when(invocation.getMethod()).thenReturn(new Method("java.util.List", "add", new Class[]{Object.class}));

        ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
        doAnswer(ArgumentMatchers.isInstance(instance.getClass()))
                .when(spyReturnsSmartNulls)
                .answer(ArgumentMatchers.any(InvocationOnMock.class)).thenReturn(instance);

        Object result = spyReturnsSmartNulls.answer(invocation);
        assertEquals(instance, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerNonMockableType() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMethod()).thenReturn(new Method("java.lang.Math", "sqrt", new Class[0]));

        ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
        doAnswer(ArgumentMatchers.any(InvocationOnMock.class))
                .when(spyReturnsSmartNulls)
                .answer(ArgumentMatchers.any(InvocationOnMock.class)).thenCallRealMethod();

        spyReturnsSmartNulls.answer(invocation);
    }

    @Test
    public void testAnswerThrowingInterceptor() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getMethod()).thenReturn(new Method(String.class.getName(), "toString", new Class[0]));

        Object result = returnsSmartNulls.answer(invocation);
        verify(invocation).getMethod().invoke(mock, invocation.getArguments());
        verifyNoMoreInteractions(mock);
    }
}