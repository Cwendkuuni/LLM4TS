package com.example.junit; // replace with your package name

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Object invocation = Mockito.mock(Object.class);
        Object result = returnsSmartNulls.answer(invocation);
        verify(returnsSmartNulls).answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNonNullReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Object invocation = Mockito.mock(Object.class);
        when(returnsSmartNulls.answer(invocation)).thenReturn("Test String");
        assertEquals("Test String", returnsSmartNulls.answer(invocation));
        verify(returnsSmartNulls).answer(invocation);
    }

    @Test
    public void testAnswerMethodWithMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Object mockReturnType = Mockito.mock(Object.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenReturn(mockReturnType);
        assertEquals(mockReturnType, returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)));
        verify(returnsSmartNulls).answer(ArgumentMatchers.any(InvocationOnMock.class));
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAnswerMethodWithNonMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        final Class<String> stringClass = String.class;
        when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenReturn(new String("Test String"));
        returnsSmartNulls.answer(Mockito.mock(Object.class));
    }

    @Test
    public void testAnswerMethodWithToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Object invocation = Mockito.mock(Object.class);
        when(invocation.toString()).thenReturn("Test Object");
        when(returnsSmartNulls.answer(ArgumentMatchers.eq(invocation))).thenReturn("SmartNull returned by unstubbed " +
                invocation.toString() + " method on mock");
        assertEquals("SmartNull returned by unstubbed Test Object method on mock", returnsSmartNulls.answer(invocation));
    }

    @Test(expected = org.mockito.exceptions.smart.SmartNullPointerException.class)
    public void testAnswerMethodWithException() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        Object invocation = Mockito.mock(Object.class);
        when(returnsSmartNulls.answer(ArgumentMatchers.eq(invocation))).then(new ThrowingInterceptor(invocation));
        returnsSmartNulls.answer(invocation);
    }
}