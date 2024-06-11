package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

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
    public void testIsAbstract() {
        Method abstractMethod = new MockitoMethod(new AbstractClass() {}.getClass().getMethod("abstractMethod", int.class));
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);
        boolean expectedResult = true;
        boolean actualResult = delegatingMethod.isAbstract();
        assertEquals(expectedResult, actualResult);

        Method concreteMethod = new MockitoMethod(String.class.getMethod("toLowerCase", String.class));
        delegatingMethod = new DelegatingMethod(concreteMethod);
        expectedResult = false;
        actualResult = delegatingMethod.isAbstract();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsWithDifferentMethods() {
        Method method1 = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockitoMethod(String.class.getMethod("toLowerCase", String.class));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testIsVarArgs() {
        Method methodWithVarArgs = new MockitoMethod(String[].class.getMethod("valueOf", String[].class));
        DelegatingMethod delegatingMethodWithVarArgs = new DelegatingMethod(methodWithVarArgs);
        boolean expectedResult = true;
        boolean actualResult = delegatingMethodWithVarArgs.isVarArgs();
        assertEquals(expectedResult, actualResult);

        Method methodWithoutVarArgs = new MockitoMethod(String.class.getMethod("valueOf", String.class));
        DelegatingMethod delegatingMethodWithoutVarArgs = new DelegatingMethod(methodWithoutVarArgs);
        expectedResult = false;
        actualResult = delegatingMethodWithoutVarArgs.isVarArgs();
        assertEquals(expectedResult, actualResult);
    }

    private static abstract class AbstractClass {
        abstract void abstractMethod(int i);
    }
}