package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testConstructor() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        assertNotNull(answer);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInvocation() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        answer.answer(null);
    }

    @Test
    public void testAnswerWithNonMockableType() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(new int[0].getClass());
        assertNull(answer.answer(invocation));
    }

    @Test
    public void testAnswerWithMockableType() {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(String.class);
        String smartNull = answer.answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", smartNull);
    }

    @Test
    public void testThrowingInterceptor() throws Exception {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(new Throwable().getClass());
        assertThrows(Throwable.class, () -> answer.answer(invocation));
    }
}