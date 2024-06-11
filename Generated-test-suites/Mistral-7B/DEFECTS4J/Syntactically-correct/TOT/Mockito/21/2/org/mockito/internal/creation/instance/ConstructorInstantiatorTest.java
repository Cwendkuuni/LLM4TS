import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.exceptions.base.InstantiationException;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Integer intInstance = instantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructedClassWithParameters() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(String[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(Boolean.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            String errorMessage = e.getMessage();
            assertTrue(errorMessage.contains("Please ensure it has parameter-less constructor."));
        }
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Integer intInstance = instantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndConstructedClassWithParameters() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.newInstance(String[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }

        try {
            String[] stringArrayInstance = instantiator.newInstance(String[].class);
            Assert.assertNotNull(stringArrayInstance);
            String firstElement = stringArrayInstance[0];
            String secondElement = stringArrayInstance[1];
            Assert.assertNotNull(firstElement);
            Assert.assertNotNull(secondElement);
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndNoArgConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Integer intInstance = instantiator.newInstance(Integer.class);
        Assert.assertNotNull(intInstance);
        Assert.assertEquals(0, intInstance.intValue());

        Boolean boolInstance = instantiator.newInstance(Boolean.class);
        Assert.assertNotNull(boolInstance);
        Assert.assertEquals(false, boolInstance.booleanValue());
    }

    @Test
    public void testNewInstanceWithOuterClassInstanceAndThrowsExceptionInConstructor() {
        Object outerClassInstance = new Object();
        Constructor<RuntimeException> constructor = RuntimeException.class.getDeclaredConstructor();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.newInstance(RuntimeException.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            InstantiationException exception = (InstantiationException) e.getCause();
            Throwable cause = exception.getCause();
            Assert.assertSame(constructor, cause.getConstructor());
            Assert.assertEquals("Unable to create mock instance of 'java.lang.RuntimeException'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
}