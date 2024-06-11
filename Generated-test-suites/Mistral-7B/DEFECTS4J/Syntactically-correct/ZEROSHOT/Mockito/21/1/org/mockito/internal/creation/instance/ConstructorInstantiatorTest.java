package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.utils.reflection.Whitebox.getInternalState;
import static org.mockito.internal.utils.reflection.Whitebox.setInternalState;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.outerClassInstance);

        Class<?> intClass = int.class;
        assertNull(instantiator.newInstance(intClass));
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantiator.outerClassInstance);

        Class<?> intClass = int.class;
        int instance = (int) instantiator.newInstance(intClass);
        assertNotNull(instance);
        assertInstanceOf(Integer.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        instantiator.newInstance(null);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithoutNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Class<?> customClass = new CustomClass();
        instantiator.newInstance(customClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithExceptionThrownInNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Class<?> customClass = new CustomExceptionClass();
        instantiator.newInstance(customClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithExceptionThrownInConstructorWithArguments() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Class<?> customClass = new CustomArgExceptionClass();
        instantiator.newInstance(customClass);
    }

    private static class CustomClass {
        // empty constructor
    }

    private static class CustomExceptionClass {
        CustomExceptionClass() throws IllegalAccessException, InstantiationException {
            throw new IllegalAccessException("Should not be invoked");
        }
    }

    private static class CustomArgExceptionClass {
        CustomArgExceptionClass(int arg) throws InstantiationException {
            throw new InstantiationException("Should not be invoked");
        }
    }

    @Test
    public void testWithOuterClass_NoArgConstructor() throws Exception {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> intClass = int.class;

        Constructor<Integer> constructor = intClass.getDeclaredConstructor(outerClassInstance.getClass());
        constructor.setAccessible(true);

        Integer intInstance = instantiator.withOuterClass(intClass);
        assertNotNull(intInstance);
        assertInstanceOf(Integer.class, intInstance);

        setInternalState(instantiator, "outerClassInstance", null);
        try {
            instantiator.withOuterClass(intClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testWithOuterClass_ConstructorWithArguments() throws Exception {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<?> customClass = new CustomClassWithArguments();

        Constructor<CustomClassWithArguments> constructor = customClass.getDeclaredConstructor(outerClassInstance.getClass(), String.class);
        constructor.setAccessible(true);

        CustomClassWithArguments customInstance = instantiator.withOuterClass(customClass);
        assertNotNull(customInstance);
        assertInstanceOf(CustomClassWithArguments.class, customInstance);

        setInternalState(instantiator, "outerClassInstance", null);
        try {
            instantiator.withOuterClass(customClass);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    private static class CustomClassWithArguments {
        CustomClassWithArguments(Object outerClassInstance, String arg) {
            // constructor implementation
        }
    }
}