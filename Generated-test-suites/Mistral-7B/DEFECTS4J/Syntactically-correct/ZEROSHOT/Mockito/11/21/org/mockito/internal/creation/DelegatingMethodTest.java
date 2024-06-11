package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MethodType;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    private static final Class<?> RETURN_TYPE = String.class;
    private static final Class<?>[] PARAMETER_TYPES = new Class[]{Integer.class, Double.class};
    private static final String METHOD_NAME = "testMethod";

    @Test
    public void testConstructor() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[0], delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(METHOD_NAME, delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(PARAMETER_TYPES, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(RETURN_TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{Integer.class, Double.class, Integer[].class}, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        Method method2 = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());

        Method abstractMethod = new Method("abstractMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod abstractDelegatingMethod = new DelegatingMethod(abstractMethod);
        assertTrue(abstractDelegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = new Method("testMethod1", PARAMETER_TYPES, RETURN_TYPE);
        Method method2 = new Method("testMethod2", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method methodSame = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethodSame = new DelegatingMethod(methodSame);

        assertTrue(delegatingMethod1.equals(delegatingMethodSame));
        assertTrue(delegatingMethodSame.equals(delegatingMethod1));

        assertTrue(method1.equals(delegatingMethod1.getJavaMethod()));
        assertTrue(delegatingMethod1.getJavaMethod().equals(method1));
        assertTrue(delegatingMethod1.equals(new DelegatingMethod(method1)));
        assertTrue(new DelegatingMethod(method1).equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method1 = new Method("testMethod1", PARAMETER_TYPES, RETURN_TYPE);
        Method method2 = new Method("testMethod2", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertFalse(hashCode1 == hashCode2);
    }

    @Test
    public void testInvoke() throws Exception {
        Object target = new Object() {
            @SuppressWarnings("unchecked")
            public String testMethod(int arg1, double arg2) {
                return "result";
            }
        };

        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object result = delegatingMethod.invoke(target, new Object[]{1, 2.0});
        assertEquals("result", result);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInvokeWithException() throws Exception {
        Object target = new Object() {
            @SuppressWarnings("unchecked")
            public String testMethod(int arg1, double arg2) throws Exception {
                throw new Exception("Exception message");
            }
        };

        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(target, new Object[]{1, 2.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeWithNullTarget() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(null, new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeWithNullArguments() {
        Object target = new Object();
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(target, (Object[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeWithIncorrectNumberOfArguments() {
        Object target = new Object();
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(target, new Object[1]);
    }

    @Test(expected = IllegalAccessException.class)
    public void testInvokeWithAccessDenied() throws Exception {
        Object target = new Object();
        Method method = new Method("privateMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(target, new Object[0]);
    }

    @Test
    public void testToString() {
        Method method = new Method("testMethod", PARAMETER_TYPES, RETURN_TYPE);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals("DelegatingMethod [method=" + method.toString() + "]", delegatingMethod.toString());
    }
}