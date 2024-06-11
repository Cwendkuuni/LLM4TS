package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyArray;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.arrayContaining;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.*;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    private static final Class<?> RETURN_TYPE = int.class;
    private static final Class<?>[] PARAMETER_TYPES = new Class<?>[]{int.class, String.class};
    private static final String METHOD_NAME = "testMethod";
    private static final Class<?>[] EXCEPTION_TYPES = new Class[0];

    @Test
    public void shouldCreateDelegatingMethodFromMethod() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(false);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(METHOD_NAME, delegatingMethod.getName());
        assertArrayEquals(PARAMETER_TYPES, delegatingMethod.getParameterTypes());
        assertArrayEquals(EXCEPTION_TYPES, delegatingMethod.getExceptionTypes());
        assertSame(RETURN_TYPE, delegatingMethod.getReturnType());
        assertFalse(delegatingMethod.isVarArgs());
        assertFalse(delegatingMethod.isAbstract());
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void shouldCreateDelegatingMethodWithVarArgs() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(new Class<?>[]{int[].class});
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(true);
        when(method.isAbstract()).thenReturn(false);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(METHOD_NAME, delegatingMethod.getName());
        assertArrayEquals(new Class<?>[]{int[].class}, delegatingMethod.getParameterTypes());
        assertArrayEquals(EXCEPTION_TYPES, delegatingMethod.getExceptionTypes());
        assertSame(RETURN_TYPE, delegatingMethod.getReturnType());
        assertFalse(delegatingMethod.isVarArgs());
        assertFalse(delegatingMethod.isAbstract());
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void shouldCreateDelegatingMethodWithAbstractMethod() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(METHOD_NAME, delegatingMethod.getName());
        assertArrayEquals(PARAMETER_TYPES, delegatingMethod.getParameterTypes());
        assertArrayEquals(EXCEPTION_TYPES, delegatingMethod.getExceptionTypes());
        assertSame(RETURN_TYPE, delegatingMethod.getReturnType());
        assertTrue(delegatingMethod.isAbstract());
        assertFalse(delegatingMethod.isVarArgs());
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void shouldCompareTwoEqualMethods() {
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn(METHOD_NAME);
        when(method1.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method1.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method1.getReturnType()).thenReturn(RETURN_TYPE);
        when(method1.isVarArgs()).thenReturn(false);
        when(method1.isAbstract()).thenReturn(false);

        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn(METHOD_NAME);
        when(method2.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method2.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method2.getReturnType()).thenReturn(RETURN_TYPE);
        when(method2.isVarArgs()).thenReturn(false);
        when(method2.isAbstract()).thenReturn(false);

        DelegatingMethod method1Delegating = new DelegatingMethod(method1);
        DelegatingMethod method2Delegating = new DelegatingMethod(method2);

        assertTrue(method1Delegating.equals(method2Delegating));
        assertTrue(method2Delegating.equals(method1Delegating));
        assertTrue(method1Delegating.equals(method1));
        assertTrue(method2Delegating.equals(method2));
    }

    @Test
    public void shouldCompareTwoDifferentMethods() {
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn(METHOD_NAME);
        when(method1.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method1.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method1.getReturnType()).thenReturn(RETURN_TYPE);
        when(method1.isVarArgs()).thenReturn(false);
        when(method1.isAbstract()).thenReturn(false);

        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("otherMethod");
        when(method2.getParameterTypes()).thenReturn(new Class<?>[]{int.class, String[].class});
        when(method2.getExceptionTypes()).thenReturn(new Class[0]);
        when(method2.getReturnType()).thenReturn(long.class);
        when(method2.isVarArgs()).thenReturn(false);
        when(method2.isAbstract()).thenReturn(false);

        DelegatingMethod method1Delegating = new DelegatingMethod(method1);
        DelegatingMethod method2Delegating = new DelegatingMethod(method2);

        assertFalse(method1Delegating.equals(method2Delegating));
        assertFalse(method2Delegating.equals(method1Delegating));
        assertFalse(method1Delegating.equals(method2));
        assertFalse(method2Delegating.equals(method1));
    }

    @Test
    public void shouldCompareMethodWithDelegatingMethod() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(false);

        DelegatingMethod methodDelegating = new DelegatingMethod(method);

        assertFalse(method.equals(methodDelegating));
        assertFalse(methodDelegating.equals(method));
        assertTrue(methodDelegating.equals(methodDelegating));
    }

    @Test
    public void shouldCreateDelegatingMethodWithException() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(new Class[]{RuntimeException.class});
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(false);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(METHOD_NAME, delegatingMethod.getName());
        assertArrayEquals(PARAMETER_TYPES, delegatingMethod.getParameterTypes());
        assertArrayEquals(new Class[]{RuntimeException.class}, delegatingMethod.getExceptionTypes());
        assertSame(RETURN_TYPE, delegatingMethod.getReturnType());
        assertFalse(delegatingMethod.isVarArgs());
        assertFalse(delegatingMethod.isAbstract());
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenNullMethodIsPassed() {
        new DelegatingMethod(null);
    }

    @Test
    public void shouldInvokeMethod() throws Exception {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(false);

        Object target = mock(Object.class);
        Answer<Integer> answer = arg -> 42;
        Stub stub = stub(method).toReturn(answer);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        MethodInterceptor interceptor = createMethodInterceptor(delegatingMethod, target);
        assertEquals(42, interceptor.invoke(target, 1, "test", new Object[0]));
        verify(target).invokeMethod(method, 1, "test", new Object[0]);
    }

    @Test(expected = InvocationTargetException.class)
    public void shouldThrowInvocationTargetExceptionWhenMethodThrowsException() throws Exception {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(new Class[]{RuntimeException.class});
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(false);

        Object target = mock(Object.class);
        Throwable exception = new RuntimeException("test exception");
        doThrow(exception).when(target).invokeMethod(method, anyInt(), anyObject(), anyArray(Object[].class));

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        try {
            MethodInterceptor interceptor = createMethodInterceptor(delegatingMethod, target);
            interceptor.invoke(target, 1, "test", new Object[0]);
            fail("Expected InvocationTargetException to be thrown");
        } catch (InvocationTargetException e) {
            assertEquals(exception.getCause(), e.getCause());
        }
    }

    @Test
    public void shouldTestIsVarArgs() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(new Class<?>[]{int[].class});
        when(method.isVarArgs()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());

        method.setParameterTypes(PARAMETER_TYPES);
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void shouldTestIsAbstract() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(METHOD_NAME);
        when(method.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method.getReturnType()).thenReturn(RETURN_TYPE);
        when(method.isVarArgs()).thenReturn(false);
        when(method.isAbstract()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());

        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void shouldTestHashCode() {
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn(METHOD_NAME);
        when(method1.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method1.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method1.getReturnType()).thenReturn(RETURN_TYPE);
        when(method1.isVarArgs()).thenReturn(false);
        when(method1.isAbstract()).thenReturn(false);

        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn(METHOD_NAME);
        when(method2.getParameterTypes()).thenReturn(PARAMETER_TYPES);
        when(method2.getExceptionTypes()).thenReturn(EXCEPTION_TYPES);
        when(method2.getReturnType()).thenReturn(RETURN_TYPE);
        when(method2.isVarArgs()).thenReturn(false);
        when(method2.isAbstract()).thenReturn(false);

        DelegatingMethod method1Delegating = new DelegatingMethod(method1);
        DelegatingMethod method2Delegating = new DelegatingMethod(method2);

        assertEquals(1, method1Delegating.hashCode());
        assertEquals(1, method2Delegating.hashCode());
    }
}