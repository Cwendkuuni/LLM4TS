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
    public void testAnswer_ReturnsDefaultReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[0]));

        Object defaultReturnValue = new Object();
        when(mock.testMethod()).thenReturn(defaultReturnValue);

        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("toString", new Class[0]));

        String result = (String) returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForNonMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[] { int[].class }));

        InvocationOnMock spyInvocation = spy(invocation);
        verify(spyInvocation).throwException(new UnsupportedOperationException("Operation unsupported."));

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ForNonMockableFinalClass() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[] {}));

        Class<?> finalClass = Integer.TYPE;
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_ReturnsSmartNull_ThrowsException() {
        ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[0]));

        try {
            returnsSmartNulls.answer(invocation);
            fail();
        } catch (NullPointerException e) {
            // expected
            verify(returnsSmartNulls).getDefaultInterceptor().answer(invocation);
            verify(returnsSmartNulls.getDefaultInterceptor()).throwException(new NullPointerException());
        }
    }
}