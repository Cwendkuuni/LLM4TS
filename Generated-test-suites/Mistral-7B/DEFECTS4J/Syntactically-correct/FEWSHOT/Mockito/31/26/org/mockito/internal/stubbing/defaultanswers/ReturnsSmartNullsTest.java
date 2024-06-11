package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsDefaultEmptyValues() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        invokeMethodAndCheckReturnsDefaultValue(mock, new Object() {});
    }

    @Test
    public void testAnswerReturnsSmartNullForToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", returnsSmartNulls.answer(mockToStringInvocation()));
    }

    @Test
    public void testAnswerReturnsNullForNonMockableTypes() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);
        assertNull(returnsSmartNulls.answer(mockPrimitiveTypeInvocation()));
    }

    @Test
    public void testAnswerReturnsImposterizedObjectForMockableTypes() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);
        Object smartNullMock = mock(ClassImposterizer.class);
        ClassImposterizer.imposterise(new Object() {}, String.class);
        verify(smartNullMock).imposterise(eq(new ThrowingInterceptor(mockToStringInvocation())), eq(String.class));
        assertEquals("SmartNull returned by unstubbed toString() method on mock", returnsSmartNulls.answer(mockToStringInvocation()));
    }

    @Test
    public void testAnswerThrowsExceptionWhenInvokedOnNull() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertNull(returnsSmartNulls.answer(null));
    }

    private InvocationOnMock mockPrimitiveTypeInvocation() {
        return mockMethodCall(int.class);
    }

    private InvocationOnMock mockToStringInvocation() {
        return mockMethodCall(String.class, "toString");
    }

    private void invokeMethodAndCheckReturnsDefaultValue(Object mock, Object value) {
        assertEquals(value, mock.getClass().cast(mock).answer(mockMethodCall(Object.class, "getClass")).getClass().cast(Object.class));
    }

    private InvocationOnMock mockMethodCall(Class<?> clazz, String methodName) {
        return mockMethodCall(clazz, methodName, (Object[]) null);
    }

    private InvocationOnMock mockMethodCall(Class<?> clazz, String methodName, Object... arguments) {
        try {
            Object mock = mock(clazz, new Answer<Object>() {
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return invocation.callRealMethod();
                }
            });
            Method method = clazz.getMethod(methodName, arguments != null ? arguments.getClass() : new Class[0]);
            return new InvocationOnMock(method, mock, arguments);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationOnMock spyObjectWithThrowingInterceptor(Object obj) {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object spy = spy(obj);
        ((MethodInterceptor) returnsSmartNulls.new ThrowingInterceptor(null)).invocation = new InvocationOnMock(null, spy, new Object[0]);
        return ((MethodInterceptor) returnsSmartNulls.new ThrowingInterceptor(null)).invocation;
    }
}