package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsDefaultReturnValue() {
        Object returnValue = new ReturnsSmartNulls().answer(mock(Object.class));
        assertEquals(null, returnValue);
    }

    @Test
    public void testAnswer_ReturnsSmartNullForToStringMethod() {
        Object mock = mock(String.class, new ReturnsSmartNulls());
        String smartNull = (String) new ReturnsSmartNulls().answer(mock.toString());
        assertEquals("SmartNull returned by unstubbed toString method on mock", smartNull);
    }

    @Test
    public void testAnswer_ReturnsSmartNullForNonMockableReturnType() {
        final int result = 42;
        Object mock = mock(ReturnsSmartNulls.class);
        Answer<Integer> answer = Mockito.spy(mock);
        InvocationHandler handler = Mockito.storeArguments(answer, new Object[0]);
        Mockito.doReturn(result).when(answer).answer(ArgumentMatchers.any());

        Integer smartNull = (Integer) new ReturnsSmartNulls().answer(answer);
        assertNull(smartNull);

        verify(answer, Mockito.times(1)).answer(ArgumentMatchers.any());
        verifyZeroInteractions(answer);
    }

    @Test
    public void testAnswer_ReturnsSmartNullForMockableReturnType() {
        Object mock = mock(String.class, new ReturnsSmartNulls());
        assertEquals("SmartNull returned by unstubbed method on mock", new ReturnsSmartNulls().answer(mock.hashCode()));
    }

    @Test
    public void testAnswer_ReturnsDelegateReturnValue() {
        Object mock = mock(String.class, new ReturnsSmartNulls());
        when(mock.length()).thenReturn(5);

        int result = new ReturnsSmartNulls().answer(mock.length());
        assertEquals(5, result);
    }

    @Test
    public void testThrowingInterceptor_ToStringMethod() {
        Object mock = mock(String.class, new ReturnsSmartNulls());
        try {
            new ReturnsSmartNulls().answer(mock.toString());
            fail("Expected smart null pointer exception.");
        } catch (Throwable e) {
            String message = e.getMessage();
            assertTrue(message.contains("SmartNull returned by unstubbed toString method on mock"));
        }
    }

    @Test
    public void testThrowingInterceptor_NonMockableReturnType() {
        ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
        Class<Long> longClass = Long.class;

        try {
            imposterizer.imposterise(new ThrowingInterceptor(null), longClass);
            fail("Expected IllegalStateException.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testThrowingInterceptor_MockableReturnType() {
        Class<String> stringClass = String.class;
        Method method = String.class.getMethod("hashCode");
        ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);

        ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
        Object imposterizedInterceptor = imposterizer.imposterise(interceptor, stringClass);

        Method proxyMethod = null;
        try {
            proxyMethod = imposterizedInterceptor.getClass().getMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
        } catch (NoSuchMethodException e) {
            fail("Expected method not found.");
        }

        Object proxy = Mockito.mock(MethodProxy.class);
        Object result = null;
        try {
            result = proxyMethod.invoke(imposterizedInterceptor, Mockito.mock(Object.class), null, new Object[0], proxy);
        } catch (Throwable e) {
            fail("Expected smart null pointer exception.", e);
        }

        assertEquals("SmartNull returned by unstubbed hashCode method on mock", result.toString());
    }
}