package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MockitoMethod;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.internal.creation.DelegatingMethod.getExceptionTypes;
import static org.mockito.internal.creation.DelegatingMethod.getJavaMethod;
import static org.mockito.internal.creation.DelegatingMethod.getName;
import static org.mockito.internal.creation.DelegatingMethod.getParameterTypes;
import static org.mockito.internal.creation.DelegatingMethod.getReturnType;
import static org.mockito.internal.creation.DelegatingMethod.isAbstract;
import static org.mockito.internal.creation.DelegatingMethod.isVarArgs;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    private static final String TEST_CLASS_NAME = "org.mockito.internal.creation.DelegatingMethod";
    private static final String TEST_METHOD_NAME_1 = "getExceptionTypes";
    private static final String TEST_METHOD_NAME_2 = "getName";
    private static final String TEST_METHOD_NAME_3 = "getJavaMethod";
    private static final String TEST_METHOD_NAME_4 = "getParameterTypes";
    private static final String TEST_METHOD_NAME_5 = "getReturnType";
    private static final String TEST_METHOD_NAME_6 = "isAbstract";
    private static final String TEST_METHOD_NAME_7 = "isVarArgs";
    private static final String TEST_METHOD_NAME_8 = "equals";
    private static final String TEST_METHOD_NAME_9 = "hashCode";

    @Test
    public void testGetExceptionTypes() {
        Method method = getJavaMethod(reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_1));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = getExceptionTypes(delegatingMethod);
        Class<?>[] expectedExceptionTypes = method.getExceptionTypes();

        assertThat(exceptionTypes, is(expectedExceptionTypes));
    }

    @Test
    public void testGetJavaMethod() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_3);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = getJavaMethod(delegatingMethod);

        assertThat(javaMethod, is(method));
    }

    @Test
    public void testGetName() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_2);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = getName(delegatingMethod);

        assertThat(name, is(method.getName()));
    }

    @Test
    public void testGetParameterTypes() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_4);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = getParameterTypes(delegatingMethod);
        Class<?>[] expectedParameterTypes = method.getParameterTypes();

        assertThat(parameterTypes, is(expectedParameterTypes));
    }

    @Test
    public void testGetReturnType() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_5);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = getReturnType(delegatingMethod);
        Class<?> expectedReturnType = method.getReturnType();

        assertThat(returnType, is(expectedReturnType));
    }

    @Test
    public void testIsAbstract() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_6);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = isAbstract(delegatingMethod);

        boolean expectedIsAbstract = (method.getModifiers() & Modifier.ABSTRACT) != 0;

        assertThat(isAbstract, is(expectedIsAbstract));
    }

    @Test
    public void testIsVarArgs() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_7);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = isVarArgs(delegatingMethod);

        boolean expectedIsVarArgs = method.isVarArgs();

        assertThat(isVarArgs, is(expectedIsVarArgs));
    }

    @Test
    public void testEquals() {
        Method method1 = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_3);
        Method method2 = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_3);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        boolean expectedEquals = method1.equals(method2);

        assertThat(equals, is(expectedEquals));
    }

    @Test
    public void testHashCode() {
        Method method = reflectGetMethod(TEST_CLASS_NAME, TEST_METHOD_NAME_8);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();

        assertThat(hashCode, is(0)); // Hashcode should be overridden
    }

    private Method reflectGetMethod(String className, String methodName) {
        try {
            return Class.forName(className).getMethod(methodName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class: " + className, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find method: " + methodName + " in class: " + className, e);
        }
    }
}