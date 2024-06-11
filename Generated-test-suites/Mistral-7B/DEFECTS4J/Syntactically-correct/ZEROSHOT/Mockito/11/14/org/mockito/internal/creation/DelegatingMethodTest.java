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
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    private static final String CLASS_NAME = DelegatingMethod.class.getName();

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMethod() {
        new DelegatingMethod((Method) null);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod() {
            @Override
            public Method getJavaMethod() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getName() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<?>[] getParameterTypes() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<?> getReturnType() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isVarArgs() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isAbstract() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean equals(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int hashCode() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {IllegalArgumentException.class};
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class<?>[]{IllegalArgumentException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = new Method("testMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);

        Method method = delegatingMethod.getJavaMethod();

        assertEquals(javaMethod, method);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", new Class[]{Integer.TYPE, String.class}, Integer.TYPE);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(new Class[] {Integer.TYPE, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(Integer.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method methodVariable = new Method("testMethod", new Class[]{}, Integer[].class);
        Method methodFixed = new Method("testMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethodVariable = new DelegatingMethod(methodVariable);
        DelegatingMethod delegatingMethodFixed = new DelegatingMethod(methodFixed);

        assertTrue(delegatingMethodVariable.isVarArgs());
        assertFalse(delegatingMethodFixed.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = new Method("testMethod", new Class[]{}, void.class);
        Method concreteMethod = new Method("testMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(abstractMethod);
        DelegatingMethod delegatingMethodConcrete = new DelegatingMethod(concreteMethod);

        assertTrue(delegatingMethodAbstract.isAbstract());
        assertFalse(delegatingMethodConcrete.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method("testMethod", new Class[]{}, Integer.TYPE);
        Method method2 = new Method("testMethod", new Class[]{}, Integer.TYPE);
        Method method3 = new Method("testOtherMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method("testMethod", new Class[]{}, Integer.TYPE);
        Method method2 = new Method("testMethod", new Class[]{}, Integer.TYPE);
        Method method3 = new Method("testOtherMethod", new Class[]{}, Integer.TYPE);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertEquals(delegatingMethod1.hashCode(), delegatingMethod1.hashCode());
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
        assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod3.hashCode());
    }

    @Test(expected = InvocationTargetException.class)
    public void testCallMethodThrowsException() throws Exception {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object target = new Object();
        Object[] arguments = new Object[]{42};

        delegatingMethod.invoke(target, arguments, MethodType.NORMAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallMethodWithNullTarget() throws Exception {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(null, new Object[]{42}, MethodType.NORMAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallMethodWithNullArguments() throws Exception {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(new Object(), null, MethodType.NORMAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallMethodWithNullMethodType() throws Exception {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        delegatingMethod.invoke(new Object(), new Object[]{42}, null);
    }

    @Test
    public void testCallMethod() throws Exception {
        Method method = new Method("testMethod", new Class[]{}, Integer.TYPE);
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Object target = new Object();
        Object[] arguments = new Object[]{42};

        Object result = delegatingMethod.invoke(target, arguments, MethodType.NORMAL);

        assertEquals(Integer.valueOf(42), result);
    }
}