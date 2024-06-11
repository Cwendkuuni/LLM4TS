package org.mockito.internal.creation.instance;

import org.junit.*;
import static org.junit.Assert.*;;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertSame(outerClassInstance, constructorInstantiator.outerClassInstance);
    }

    @Test
    public <T> void testNewInstance_ValidClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        T instance = constructorInstantiator.newInstance(TestClass.class);
        assertTrue(instance instanceof TestClass);
    }

    @Test
    public <T> void testNewInstance_NoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        T instance = constructorInstantiator.newInstance(TestClass.class);
        assertTrue(instance instanceof TestClass);
    }

    @Test
    public <T> void testWithOuterClass() {
        TestOuterClass outerClass = new TestOuterClass();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
        T instance = constructorInstantiator.newInstance(TestClass.class);
        assertTrue(instance instanceof TestClass);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClass_NoSuchConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(TestClassWithNoConstructor.class);
    }

    @Test
    public void testParamsException_NoArgConstructor() {
        try {
            new ConstructorInstantiator(null).newInstance(Object.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Please ensure it has parameter-less constructor."));
        }
    }

    @Test
    public void testParamsException_ConstructorWithArgs() {
        try {
            new ConstructorInstantiator(null).newInstance(TestClassWithArgs.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Please ensure that the outer instance has correct type and that the target class has parameter-less constructor."));
        }
    }
}