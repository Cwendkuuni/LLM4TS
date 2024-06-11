import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @Before
    public void setup() {
        outerClassInstance = Mockito.mock(String.class);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertEquals(ConstructorInstantiator.class, constructorInstantiator.getClass());
        assertNull(constructorInstantiator.outerClassInstance);

        String cls = String.class;
        try {
            constructorInstantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testNewInstanceWithClassThatHasNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = String.class;
        String instance = constructorInstantiator.newInstance(cls);
        assertEquals("", instance);
    }

    @Test
    public void testNewInstanceWithClassThatHasArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = AnotherClass.class;
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testNewInstanceWithClassThatHasOuterClassConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = OuterClassWithConstructor.class;
        OuterClassWithConstructor outerClassWithConstructor = Mockito.spy(cls, Mockito.CALLS_REAL_METHODS);
        Mockito.doReturn(outerClassInstance).when(outerClassWithConstructor).getOuterClass();

        assertTrue(cls.isInstance(constructorInstantiator.newInstance(cls)));
        Mockito.verify(outerClassWithConstructor).newInstance();
    }

    @Test
    public void testNewInstanceWithClassThatThrowsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = ExceptionThrowingClass.class;
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testNewInstanceWithClassThatThrowsIllegalAccessException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = IllegalAccessClass.class;
        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(cls));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatThrowsRuntimeException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        String cls = RuntimeExceptionThrowingClass.class;
        constructorInstantiator.newInstance(cls);
    }

    @Test
    public void testParamsException() {
        Class<String> cls = String.class;
        Exception e = new RuntimeException();
        InstantiationException exception = ConstructorInstantiator.paramsException(cls, e);

        assertEquals("Unable to create mock instance of '" + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
        assertEquals(RuntimeException.class, exception.getCause().getClass());
    }

    static class AnotherClass {
        AnotherClass() {
            // constructor with arguments
        }
    }

    static class OuterClassWithConstructor {
        private Object outerClass;

        OuterClassWithConstructor() {
            this.outerClass = new Object();
        }

        Object getOuterClass() {
            return outerClass;
        }

        static class InnerClass {
            InnerClass() {
            }
        }
    }

    static class IllegalAccessClass {
        private int i;

        IllegalAccessClass() {
            this(1);
        }

        IllegalAccessClass(int i) {
            this.i = i;
        }
    }

    static class RuntimeExceptionThrowingClass {
        RuntimeExceptionThrowingClass() {
            throw new RuntimeException();
        }
    }
}