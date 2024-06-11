package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceof;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Integer intInstance = constructorInstantiator.newInstance(Integer.class);
        assertNotNull(intInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndNonParameterlessClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            constructorInstantiator.newInstance(String.class);
            assert false : "Should have thrown InstantiationException";
        } catch (InstantiationException e) {
            assertInstanceof(InstantiationException.class, e.getCause());
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Integer intInstance = constructorInstantiator.newInstance(Integer.class);
        assertNotNull(intInstance);
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndNonParameterlessClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            constructorInstantiator.newInstance(String.class);
            String stringInstance = (String) constructorInstantiator.newInstance(String.class);
            assertNotNull(stringInstance);
        } catch (InstantiationException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) e.getCause();
                assertInstanceof(UndeclaredThrowableException.class, ite.getTargetException());
                assertInstanceof(IllegalArgumentException.class, ite.getTargetException().getCause());
            }
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndNonSupportedClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(MyNotSupportingClass.class);
    }

    private static class MyNotSupportingClass {
        MyNotSupportingClass() {
            throw new RuntimeException("Not supported");
        }
    }
}