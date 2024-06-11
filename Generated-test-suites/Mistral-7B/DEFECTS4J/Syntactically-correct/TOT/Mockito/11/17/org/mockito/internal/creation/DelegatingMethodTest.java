package org.mockito.internal.creation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Class<?> clazz = MockitoMethod.class;
        Method method = MockitoMethod.class.getMethod("getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        Assertions.assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testGetName() {
        Class<?> clazz = MockitoMethod.class;
        Method method = MockitoMethod.class.getMethod("getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "getName";
        String actualName = delegatingMethod.getName();

        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsAbstract() {
        Class<?> clazz = Object.class;
        Method method = clazz.getMethod("toString");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expectedIsAbstract = false;
        boolean actualIsAbstract = delegatingMethod.isAbstract();

        Assertions.assertEquals(expectedIsAbstract, actualIsAbstract);

        clazz = AbstractClass.class;
        method = clazz.getMethod("toString");
        delegatingMethod = new DelegatingMethod(method);

        expectedIsAbstract = true;
        actualIsAbstract = delegatingMethod.isAbstract();

        Assertions.assertEquals(expectedIsAbstract, actualIsAbstract);
    }

    static class AbstractClass {
        // Abstract class with an abstract method
        abstract void abstractMethod();
    }
}