import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            Class<Object> cls = Object.class;
            Object instance = instantiator.newInstance(cls);
            Assert.assertNotNull(instance);
        } catch (InstantiationException e) {
            Assert.fail("Expected no argument constructor to work.");
        }
    }

    @Test
    public void testInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<SomeClass> cls = SomeClass.class;
        try {
            SomeClass instance = instantiator.newInstance(cls);
            Assert.assertNotNull(instance);
            Assert.assertEquals(outerClassInstance, Whitebox.getInnerStaticField(instance, "outerClassInstance"));
        } catch (InstantiationException e) {
            Assert.fail("Expected to instantiate class with outer class instance.");
        }
    }

    @Test(expected = InstantiationException.class)
    public void testNoOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<SomeClass> cls = SomeClass.class;
        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNoParameterlessConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<SomeClassWithConstructor> cls = SomeClassWithConstructor.class;
        instantiator.newInstance(cls);
    }

    @Test
    public void testParamsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<SomeClassWithWrongType> cls = SomeClassWithWrongType.class;
        try {
            instantiator.newInstance(cls);
            Assert.fail("Expected params exception.");
        } catch (InstantiationException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Unable to create mock instance of 'SomeClassWithWrongType'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor."));
        }
    }

    @Test
    public void testReflectionException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<SomeClassWithReflectionException> cls = SomeClassWithReflectionException.class;
        try {
            instantiator.newInstance(cls);
            Assert.fail("Expected reflection exception.");
        } catch (InstantiationException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof ReflectionException);
        }
    }

    static class SomeClass {}
    static class SomeClassWithConstructor {
        SomeClass outerClassInstance;

        SomeClassWithConstructor(SomeClass outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }
    }
    static class SomeClassWithWrongType {}
    static class SomeClassWithReflectionException {
        SomeClassWithReflectionException() throws Exception {
            throw new Exception();
        }
    }
}