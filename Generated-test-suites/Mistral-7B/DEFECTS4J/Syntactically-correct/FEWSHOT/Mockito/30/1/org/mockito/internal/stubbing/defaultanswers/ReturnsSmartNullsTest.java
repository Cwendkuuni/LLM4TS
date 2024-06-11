package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSameInstanceForSameInvocations() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        assertSame(returnsSmartNulls, returnsSmartNulls.answer(invocation1));
        assertSame(returnsSmartNulls, returnsSmartNulls.answer(invocation2));
    }

    @Test
    public void testAnswerReturnsNullIfTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Class<?> nonMockableType = int[].class;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "toString"));
        assertNull(returnsSmartNulls.answer(invocation));
        verify(invocation).getMethod();
    }

    @Test
    public void testAnswerReturnsSmartNullIfToStringMethodIsCalled() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "toString"));
        assertEquals("SmartNull returned by unstubbed toString() method on mock", returnsSmartNulls.answer(invocation));
        verify(invocation).getMethod();
    }

    @Test
    public void testAnswerReturnsDelegateReturnIfNotNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object delegateReturn = "some return value";
        Answer<Object> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(any(InvocationOnMock.class))).thenReturn(delegateReturn);
        ReturnsMoreEmptyValues moreEmptyValues = spy(new ReturnsMoreEmptyValues());
        when(returnsSmartNulls.delegate).thenReturn(mockAnswer);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertEquals(delegateReturn, returnsSmartNulls.answer(invocation));
        verify(mockAnswer).answer(invocation);
    }

    @Test
    public void testAnswerReturnsSmartNullIfDelegateReturnsNull() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object delegateNull = null;
        Answer<Object> mockAnswer = mock(Answer.class);
        when(mockAnswer.answer(any(InvocationOnMock.class))).thenReturn(delegateNull);
        ReturnsMoreEmptyValues moreEmptyValues = spy(new ReturnsMoreEmptyValues());
        when(returnsSmartNulls.delegate).thenReturn(mockAnswer);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNull(returnsSmartNulls.answer(invocation));
        verify(mockAnswer).answer(invocation);
    }
}