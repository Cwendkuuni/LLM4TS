package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        Class<DummyClass> dummyClass = DummyClass.class;
        DummyClass instance = instantiator.newInstance(dummyClass);
        assertNotNull(instance);
        assertEquals(0, instance.getCount());
    }

    @Test
    public void testConstructorWithOuterInstance() {
        Object outerInstance = mock(OuterClass.class, InstantiationStrategy.CGLIB);
        Instantiator instantiator = new ConstructorInstantiator(outerInstance);
        Class<InnerClass> innerClass = InnerClass.class;
        InnerClass instance = instantiator.newInstance(innerClass);
        assertNotNull(instance);
        assertEquals(outerInstance, instance.getOuterClassInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testWithNullOuterClassInstance() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        Class<DummyClass> dummyClass = DummyClass.class;
        instantiator.newInstance(dummyClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithNoParameterLessConstructor() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        Class<NoArgConstructorException> noArgConstructorExceptionClass = NoArgConstructorException.class;
        instantiator.newInstance(noArgConstructorExceptionClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithNonDeclaredConstructor() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        Class<NonDeclaredConstructorClass> nonDeclaredConstructorClass = NonDeclaredConstructorClass.class;
        instantiator.newInstance(nonDeclaredConstructorClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithNoAccessibleConstructor() {
        Instantiator instantiator = new ConstructorInstantiator(null);
        Class<NoAccessibleConstructorClass> noAccessibleConstructorClass = NoAccessibleConstructorClass.class;
        instantiator.newInstance(noAccessibleConstructorClass);
    }

    private static class DummyClass {
        private int count = 0;

        public DummyClass() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    private static class InnerClass {
        private Object outerClassInstance;

        public InnerClass(Object outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public Object getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private static class OuterClass {}

    private static class NoArgConstructorException {
        public NoArgConstructorException() {
            throw new RuntimeException();
        }
    }

    private static class NonDeclaredConstructorClass {
        private NonDeclaredConstructorClass(int i) {}
    }

    private static class NoAccessibleConstructorClass {
        private NoAccessibleConstructorClass() {
            Constructor<NoAccessibleConstructorClass> constructor = NoAccessibleConstructorClass.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }
    }
}