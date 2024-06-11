import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Object> objectClass = Object.class;
        try {
            Object instance = instantiator.newInstance(objectClass);
            Assert.assertNotNull(instance);
        } catch (InstantiationException e) {
            fail("Expected no exception when instantiating a parameterless class with null outer class instance.");
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameter() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Foo> fooClass = Foo.class;
        try {
            instantiator.newInstance(fooClass);
            fail("Expected InstantiationException when instantiating a class with constructor parameter and null outer class instance.");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Bar> barClass = Bar.class;
        try {
            Bar instance = instantiator.newInstance(barClass);
            Assert.assertNotNull(instance);
            Assert.assertEquals(outerClassInstance, Whitebox.getInternalState(instance, "outerInstance"));
        } catch (InstantiationException e) {
            fail("Expected instance to be created when outer class instance is valid and class is parameterless.");
        }
    }

    @Test
    public void testNewInstanceWithValidOuterClassInstanceAndConstructorWithParameter() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Foo> fooClass = Foo.class;
        try {
            Foo instance = instantiator.newInstance(fooClass);
            Assert.assertNotNull(instance);
            Assert.assertEquals(outerClassInstance, Whitebox.getInternalState(instance, "outerInstance"));
            assertNotNull(Whitebox.getInternalState(instance, "constructorArgument"));
        } catch (InstantiationException e) {
            fail("Expected instance to be created when outer class instance is valid and class has a constructor.");
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithInvalidClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<FooBar> fooBarClass = FooBar.class;
        instantiator.newInstance(fooBarClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithExceptionInConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Baz> bazClass = Baz.class;
        try {
            instantiator.newInstance(bazClass);
            fail("Expected InstantiationException when constructor throws an exception.");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    private static class Foo {
        Foo() {}

        Object outerInstance;

        private Foo(Object outerInstance) {
            this.outerInstance = outerInstance;
        }
    }

    private static class Bar {
        Bar() {}
    }

    private static class Baz {
        Baz(Object outerInstance) {}
    }
}