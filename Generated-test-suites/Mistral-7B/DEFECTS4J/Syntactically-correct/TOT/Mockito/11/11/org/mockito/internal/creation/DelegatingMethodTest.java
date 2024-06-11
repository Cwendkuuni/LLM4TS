package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse(delegatingMethod1.isVarArgs());

        Method method2 = new MockitoMethod(int[].class.getMethod("new", int.class));
        DelegingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        Method method2 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method differentMethod = new MockitoMethod(int.class.getMethod("valueOf", int.class));
        DelegatingMethod differentDelegatingMethod = new DelegatingMethod(differentMethod);
        assertFalse(delegatingMethod1.equals(differentDelegatingMethod));
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertEquals("valueOf", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }
}