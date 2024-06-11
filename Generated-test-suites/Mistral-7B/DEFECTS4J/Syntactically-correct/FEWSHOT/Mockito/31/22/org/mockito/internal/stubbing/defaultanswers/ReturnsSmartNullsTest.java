package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithDefaultReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object target = mock(String.class);
        Method method = Object.class.getMethod("toString");

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[0]);
        Object defaultReturnValue = returnsSmartNulls.answer(invocation);
        assertNotNull(defaultReturnValue);
    }

    @Test
    public void testAnswerWithNullReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object target = mock(String.class);
        Method method = Object.class.getMethod("toString");

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[0]);
        Object defaultReturnValue = returnsSmartNulls.delegate.answer(invocation);
        assertNull(defaultReturnValue);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);

        verify(invocation).getMethod().getName().equals("toString");
    }

    @Test
    public void testAnswerWithSmartNullForStringType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object target = mock(String.class);
        Method method = Object.class.getMethod("toString");

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[0]);
        Object result = returnsSmartNulls.answer(invocation);
        String expectedMessage = "SmartNull returned by unstubbed toString method on mock";
        assertNotNull(result);
        assertEquals(expectedMessage, result.toString());

        verify(invocation).getMethod().getName().equals("toString");
    }

    @Test
    public void testAnswerWithSmartNullForNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object target = mock(Object.class);
        Method method = Integer.class.getMethod("valueOf", String.class);

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[]{"1"});
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullForNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object target = mock(Object.class);
        Method method = Integer.class.getMethod("valueOf", String.class);

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[]{null});
        Object result = returnsSmartNulls.answer(invocation);
        fail("Answer with ReturnsSmartNulls should throw NPE instead of returning null.");
    }

    @Test
    public void testThrowingInterceptor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object target = mock(String.class);
        Method method = Object.class.getMethod("toString");

        InvocationOnMock invocation = new InvocationOnMock(target, method, new Object[0]);
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Object result = throwingInterceptor.intercept(null, method, new Object[0], null);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
    }
}