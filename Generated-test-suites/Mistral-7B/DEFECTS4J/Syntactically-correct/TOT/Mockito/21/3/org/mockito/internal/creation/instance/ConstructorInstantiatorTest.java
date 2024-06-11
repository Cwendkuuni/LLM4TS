import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'java.lang.String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndClassWithConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(Number[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of '[Ljava.lang.Number;'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = instantiator.newInstance(String.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Number[] numbers = instantiator.newInstance(Number[].class);
        assertNotNull(numbers);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndClassWithNoConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(InvalidClass.class);
    }

    @Test
    public void testParamsExceptionMessage() {
        try {
            ConstructorInstantiator.paramsException(String[].class, new InstantiationException(""));
            fail("Expected String to be appended to exception message");
        } catch (InstantiationException e) {
            assertTrue(e.getMessage().contains("['Ljava.lang.String;'"));
        }
    }

    private static class InvalidClass {
        private InvalidClass() {
        }

        private InvalidClass(int i) {
        }
    }
}