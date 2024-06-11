package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Whitebox.getInnerClass;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testNewInstanceWithMethod() {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method method = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertNotNull(delegatingMethod);
        assertEquals(method, delegatingMethod.getJavaMethod());
        assertEquals(method.getName(), delegatingMethod.getName());
        assertEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
        assertEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
        assertEquals(method.getModifiers(), delegatingMethod.getModifiers());
        assertSame(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testNewInstanceWithInnerClass() {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Class<DelegatingMethod> delegatingMethodClass = DelegatingMethod.class;

        Method getMockitoMethod = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");
        Method getDelegatingMethod = MethodTestUtil.getTestMethod(delegatingMethodClass, "getJavaMethod");

        Class<?> innerClass = getInnerClass(mockitoMethodClass, "MockitoMethod$1");
        Method innerMethod = MethodTestUtil.getTestMethod(innerClass, "getJavaMethod");

        DelegatingMethod delegatingMethod = new DelegatingMethod((Method) getInnerClass(innerClass, "getJavaMethod").getDeclaredMethod("getJavaMethod"));

        assertNotNull(delegatingMethod);
        assertEquals(innerMethod, delegatingMethod.getJavaMethod());
        assertEquals(innerMethod.getName(), delegatingMethod.getName());
        assertEquals(innerMethod.getParameterTypes(), delegatingMethod.getParameterTypes());
        assertEquals(innerMethod.getReturnType(), delegatingMethod.getReturnType());
        assertEquals(innerMethod.isVarArgs(), delegatingMethod.isVarArgs());
        assertEquals(innerMethod.getExceptionTypes(), delegatingMethod.getExceptionTypes());
        assertEquals(innerMethod.getModifiers(), delegatingMethod.getModifiers());
        assertSame(innerMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testEquals() {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method method1 = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");
        Method method2 = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertTrue(delegatingMethod2.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(method1));
        assertTrue(delegatingMethod1.equals(method2));
        assertTrue(delegatingMethod2.equals(method1));
        assertTrue(delegatingMethod2.equals(method2));

        Object other = new Object();
        assertFalse(delegatingMethod1.equals(other));
        assertFalse(delegatingMethod2.equals(other));
    }

    @Test
    public void testHashCode() {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method method = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode2);
    }

    @Test(expected = NullPointerException.class)
    public void testNewInstanceWithNullMethod() {
        new DelegatingMethod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithAbstractMethod() {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method abstractMethod = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName", Object.class);

        new DelegatingMethod(abstractMethod);
    }

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method method = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testIsAbstract() throws NoSuchMethodException {
        Class<MockitoMethod> mockitoMethodClass = MockitoMethod.class;
        Method nonAbstractMethod = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName");
        Method abstractMethod = MethodTestUtil.getTestMethod(mockitoMethodClass, "getName", Object.class);

        DelegatingMethod delegatingNonAbstractMethod = new DelegatingMethod(nonAbstractMethod);
        assertFalse(delegatingNonAbstractMethod.isAbstract());

        DelegatingMethod delegatingAbstractMethod = new DelegatingMethod(abstractMethod);
        assertTrue(delegatingAbstractMethod.isAbstract());
    }
}

class MethodTestUtil {

    @SuppressWarnings("unchecked")
    public static <T> Method getTestMethod(Class<T> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Failed to find method %s() in class %s with parameters: %s", methodName, clazz.getName(), Arrays.toString(parameterTypes)));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Method getTestMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Failed to find method %s() in class %s", methodName, clazz.getName()));
        }
    }
}