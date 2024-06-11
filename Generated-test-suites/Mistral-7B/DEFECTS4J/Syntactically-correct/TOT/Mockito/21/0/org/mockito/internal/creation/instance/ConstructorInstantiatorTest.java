package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    @Test
    public void testWithNullOuterClassInstance() {
        Class<MyClass> cls = MyClass.class;
        ConstructorInstantiator instantiator = null; // intentionally left null

        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            assertInstanceOf(InstantiationException.class, e.getCause());
            assertInstanceOf(NoArgsConstructorException.class, e.getCause().getCause());
        }
    }

    @Test
    public <T> void testWithOuterClassInstance() {
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClass.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        MyClass result = instantiator.newInstance(cls);

        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithNoArgConstructorFailure() {
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClassWithNoArgConstructor.class;

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithDeclaredConstructorFailure() {
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClassWithWrongTypeConstructor.class;

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithExceptionInConstructor() {
        Object outerClassInstance = mock(OuterClass.class);
        Class<MyClass> cls = MyClassWithExceptionInConstructor.class;

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(cls);
    }

    private static class MyClass {
    }

    private static class MyClassWithNoArgConstructor {
    }

    private static class MyClassWithWrongTypeConstructor {
        public MyClassWithWrongTypeConstructor(String arg) {
        }
    }

    private static class MyClassWithExceptionInConstructor {
        public MyClassWithExceptionInConstructor(Exception arg) throws Exception {
            throw arg;
        }
    }

    private static class OuterClass {
    }

    private static class NoArgsConstructorException extends InstantiationException {
        public NoArgsConstructorException(String message) {
            super(message);
        }
    }
}