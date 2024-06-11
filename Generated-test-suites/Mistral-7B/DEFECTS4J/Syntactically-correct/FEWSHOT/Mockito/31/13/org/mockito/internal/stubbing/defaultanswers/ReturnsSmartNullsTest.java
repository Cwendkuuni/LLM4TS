package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_NullReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", null));
        assertNull(result);
    }

    @Test
    public void testAnswer_NotMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", int.class));
        assertNull(result);
    }

    @Test
    public void testAnswer_MockableReturnType() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("mockToString");

        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", String.class, mock));
        assertEquals("SmartNull returned by unstubbed testMethod() method on mock", result.toString());
        verify(mock).toString();
    }

    @Test
    public void testAnswer_MockableReturnTypeWithToString() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        String mockToString = "mockToString";
        String mock = spy(new String() {
            @Override
            public String toString() {
                return mockToString;
            }
        });

        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", String.class, mock));
        assertEquals(mockToString, result);
    }

    @Test
    public void testAnswer_MockableReturnTypeWithThrowingInterceptor() throws Exception {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        String mockToString = "mockToString";
        String mock = spy(new String() {
            @Override
            public String toString() {
                return mockToString;
            }
        });

        Method method = getMethod(ReturnsSmartNulls.class, "answer", InvocationOnMock.class);
        Object interceptor = new Object() {
            @SuppressWarnings("unchecked")
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                assertEquals(method.getName(), "answer");
                assertEquals(InvocationOnMock.class, method.getParameterTypes()[0]);
                assertEquals( ReturnsSmartNulls.class, obj.getClass());
                assertEquals(1, args.length);
                assertEquals(mock, args[0]);
                return mockToString;
            }
        };

        Object result = returnsSmartNulls.answer(createInvocationOnMock("testMethod", String.class, mock));
        assertEquals(mockToString, result);

        verify(mock).toString();
    }

    private InvocationOnMock createInvocationOnMock(String methodName, Class<?>... argumentTypes) {
        return new InvocationOnMock(new Method(methodName, argumentTypes), mock(Object.class));
    }

    private Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}