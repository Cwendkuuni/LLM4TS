import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito. ArgumentMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_NoArgsConstructor() {
        Class<Integer> intClass = Integer.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Integer intInstance = instantiator.newInstance(intClass);
        assertEquals(0, intInstance.intValue());
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_WithOuterClassConstructor() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object outerClassInstance = new Object() {};
        Class<Foo> fooClass = Foo.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Foo fooInstance = instantiator.newInstance(fooClass);
        assertEquals("expected", fooInstance.getValue());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullOuterClassInstance_WithOuterClassConstructorThrows() throws InstantiationException, NoSuchMethodException {
        Class<Foo> fooClass = Foo.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.withOuterClass(fooClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_OuterClassInstance_NoArgsConstructorThrows() throws InstantiationException, NoSuchMethodException {
        Object outerClassInstance = new Object() {};
        Class<Foo> fooClass = Foo.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.noArgConstructor(fooClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassHasNoConstructor() {
        Class<Foo> fooClass = Foo.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(fooClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassHasConstructorWithWrongType() {
        Object outerClassInstance = new Object() {};
        Class<Foo> fooClass = Foo.class;
        Constructor<Foo> constructor = fooClass.getDeclaredConstructor(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(fooClass);
    }

    @Test
    public void testParamsException_NoArgConstructor() {
        Class<Foo> fooClass = Foo.class;
        try {
            new ConstructorInstantiator(null).paramsException(fooClass, new Exception("Test exception"));
            fail();
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'Foo'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testParamsException_ConstructorThrows() throws InstantiationException {
        Class<Foo> fooClass = Foo.class;
        try {
            new ConstructorInstantiator(new Object() {}).paramsException(fooClass, new Exception("Test exception"));
            fail();
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'Foo'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    static class Foo {
        private String value;

        public Foo(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}