package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.collections.EmptyMap;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;

    @Before
    public void setUp() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstance_NullOuterClassInstance_ReturnsNewInstanceWithoutOuterClass() {
        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNull(outerClassInstance);
        assertEquals("", instance);
    }

    @Test
    public void testNewInstance_ValidOuterClassInstance_ReturnsNewInstanceWithOuterClass() {
        Class<MockedClass> mockedClass = MockedClass.class;
        MockedClass instance = constructorInstantiator.newInstance(mockedClass);
        assertEquals(outerClassInstance, instance.getOuterInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullClass_ThrowsException() {
        constructorInstantiator.newInstance(null);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithoutNoArgConstructor_ThrowsException() {
        Class<ArgConstructorClass> argConstructorClass = ArgConstructorClass.class;
        constructorInstantiator.newInstance(argConstructorClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithThrowingNoArgConstructor_ThrowsException() {
        Class<ThrowingConstructorClass> throwingConstructorClass = ThrowingConstructorClass.class;
        constructorInstantiator.newInstance(throwingConstructorClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_ClassWithDeclaredConstructor_ThrowsException() {
        Class<ConstructorClass> constructorClass = ConstructorClass.class;
        constructorInstantiator.newInstance(constructorClass);
    }

    @Test
    public void testWithOuterClass_ValidClass_ReturnsNewInstance() {
        Class<MockedClass> mockedClass = MockedClass.class;
        MockedClass instance = constructorInstantiator.withOuterClass(mockedClass);
        assertEquals(outerClassInstance, instance.getOuterInstance());
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithoutNoArgConstructor_ThrowsException() {
        Class<ArgConstructorClass> argConstructorClass = ArgConstructorClass.class;
        constructorInstantiator.withOuterClass(argConstructorClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithThrowingNoArgConstructor_ThrowsException() {
        Class<ThrowingConstructorClass> throwingConstructorClass = ThrowingConstructorClass.class;
        constructorInstantiator.withOuterClass(throwingConstructorClass);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_ClassWithDeclaredConstructor_ThrowsException() {
        Class<ConstructorClass> constructorClass = ConstructorClass.class;
        constructorInstantiator.withOuterClass(constructorClass);
    }

    private static class MockedClass {
        private final Object outerInstance;

        MockedClass() {
            this(outerClassInstance);
        }

        MockedClass(Object outerInstance) {
            this.outerInstance = outerInstance;
        }

        Object getOuterInstance() {
            return outerInstance;
        }
    }

    private static class ArgConstructorClass {
        ArgConstructorClass() {}
    }

    private static class ThrowingConstructorClass {
        ThrowingConstructorClass(int i) {}
    }

    private static class ConstructorClass {
        ConstructorClass() throws Exception {
            throw new Exception();
        }
    }

    private static class SpyClass {
        private Object outerInstance;

        public SpyClass(Object outerInstance) {
            this.outerInstance = outerInstance;
        }

        public Object getOuterInstance() {
            return outerInstance;
        }

        public void setOuterInstance(Object outerInstance) {
            this.outerInstance = outerInstance;
        }
    }

    @Test
    public void testParamsException_ValidClass_ReturnsExpectedMessage() {
        Class<SpyClass> spyClass = SpyClass.class;
        InstantiationException exception = paramsException(spyClass, new RuntimeException());
        assertEquals("Unable to create mock instance of 'SpyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor_ValidClass_ReturnsNewInstance() {
        Class<String> stringClass = String.class;
        String instance = ConstructorInstantiator.noArgConstructor(stringClass);
        assertNotNull(instance);
        assertEquals("", instance);
    }
}