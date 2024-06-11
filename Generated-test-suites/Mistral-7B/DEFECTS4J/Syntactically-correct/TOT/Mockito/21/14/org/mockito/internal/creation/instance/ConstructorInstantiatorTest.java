import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        try {
            String instance = instantiator.newInstance(stringClass);
            Assert.assertNotNull(instance);
            Assert.assertEquals("", instance);
        } catch (InstantiationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithOuterClass() {
        Object outerClassInstance = new Object() {
            // some fields and methods
        };
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<TestClassWithConstructor> testClass = TestClassWithConstructor.class;
        try {
            TestClassWithConstructor instance = instantiator.newInstance(testClass);
            Assert.assertNotNull(instance);
            // test the behavior of the class with the outer instance
            instance.outerMethod(outerClassInstance);
        } catch (InstantiationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithNoParamelessConstructor() {
        Class<TestClassWithNoParamelessConstructor> testClass = TestClassWithNoParamelessConstructor.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(testClass);
            Assert.fail("Expected InstantiationException but got none.");
        } catch (InstantiationException e) {
            // expected exception
        }
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithException() {
        Class<TestClassWithException> testClass = TestClassWithException.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(testClass);
            Assert.fail("Expected InstantiationException but got none.");
        } catch (InstantiationException e) {
            Assert.assertEquals("Unable to create mock instance of 'TestClassWithException'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testParamsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<TestClassWithTwoParams> testClass = TestClassWithTwoParams.class;
        try {
            instantiator.newInstance(testClass);
            Assert.fail("Expected InstantiationException but got none.");
        } catch (InstantiationException e) {
            Assert.assertThat(e.getCause(), instanceOf(InvocationTargetException.class));
            Assert.assertThat(((InvocationTargetException) e.getCause()).getCause(), instanceOf(UndeclaredThrowableException.class));
            Assert.assertEquals("java.lang.IllegalArgumentException: wrong number of arguments\n", ((UndeclaredThrowableException) e.getCause()).getCause().getMessage());
        }
    }

    private static class TestClassWithConstructor {
        // some methods and fields

        public void outerMethod(Object outerClassInstance) {
            // some behavior
        }
    }

    private static class TestClassWithNoParamelessConstructor {
        // no constructor
    }

    private static class TestClassWithException {
        public TestClassWithException(Object obj) throws Exception {
            throw new Exception();
        }
    }

    private static class TestClassWithTwoParams {
        public TestClassWithTwoParams(Object obj1, Object obj2) {
            throw new IllegalArgumentException("wrong number of arguments");
        }
    }
}