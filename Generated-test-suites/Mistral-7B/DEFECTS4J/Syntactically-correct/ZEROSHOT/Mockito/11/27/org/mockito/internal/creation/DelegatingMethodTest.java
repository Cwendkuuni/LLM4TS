package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MethodType;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testGetName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testGetParameterTypes", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(int.class, parameterTypes[0]);
    }

    @Test
    public void testGetReturnType() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(void.class, returnType);
    }

    @Test
    public void testIsVarArgs() throws Exception {
        Method method1 = DelegatingMethodTest.class.getMethod("testIsVarArgs1", int[].class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.isVarArgs());

        Method method2 = DelegatingMethodTest.class.getMethod("testIsVarArgs2");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testIsAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());

        Method abstractMethod = DelegatingMethodTest.class.getMethod("testIsAbstractAbstract");
        DelegatingMethod abstractDelegatingMethod = new DelegatingMethod(abstractMethod);

        assertTrue(abstractDelegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws Exception {
        Method method1 = DelegatingMethodTest.class.getMethod("testEquals1", Object.class);
        Method method2 = DelegatingMethodTest.class.getMethod("testEquals2", Object.class);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Object equalsObject1 = method1.getDeclaringClass().newInstance();
        Object equalsObject2 = method2.getDeclaringClass().newInstance();

        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Object sameMethodObject = method1.getDeclaringClass().newInstance();
        Method sameMethod = sameMethodObject.getClass().getMethod("testEquals1", Object.class);

        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(sameMethod);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("testHashCode");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMethod() {
        new DelegatingMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObject() {
        Method method = DelegatingMethodTest.class.getMethod("testNullObject");
        new DelegatingMethod(method, null);
    }

    public static void testGetExceptionTypes() {
    }

    public static void testGetJavaMethod() {
    }

    public static void testGetName() {
    }

    public static void testGetParameterTypes(int i) {
    }

    public static void testGetReturnType() {
    }

    public static void testIsVarArgs1(int[] ints) {
    }

    public static void testIsVarArgs2() {
    }

    public static void testIsAbstractAbstract() {
        abstract class AbstractClass {
            abstract void abstractMethod();
        }

        AbstractClass abstractClass = new AbstractClass() {
            @Override
            public void abstractMethod() {
            }
        };
    }

    public static void testIsAbstract() {
    }

    public static void testEquals1(Object obj) {
    }

    public static void testEquals2(Object obj) {
    }
}