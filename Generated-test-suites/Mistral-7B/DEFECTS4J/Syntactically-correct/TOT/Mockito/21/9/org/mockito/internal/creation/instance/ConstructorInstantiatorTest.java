import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Object> cls = Object.class;

        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndConstructorWithParameter() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Foo> cls = Foo.class;

        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'Foo'.\nPlease ensure it has parameter-less constructor.";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> cls = Object.class;

        Object result = instantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(outerClassInstance.getClass(), result.getClass().getSuperclass());
    }

    @Test
    public void testNewInstanceWithNotNullOuterClassInstanceAndConstructorWithParameter() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Foo> cls = Foo.class;

        Foo result = instantiator.newInstance(cls);
        assertNotNull(result);
        assertEquals(outerClassInstance, result.getOuterClassInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNotNullOuterClassInstanceAndNoSuchConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Bar> cls = Bar.class;

        instantiator.newInstance(cls);
    }

    private static class Foo {
        private Foo() {}

        private Object getOuterClassInstance() {
            return null;
        }
    }

    private static class Bar {
        private Bar(Object outerClassInstance) {}
    }
}