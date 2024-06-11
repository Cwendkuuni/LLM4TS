import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NormalCase() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        assertEquals(Object.class, instantiator.newInstance(Object.class).getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(Object.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_DeclaredConstructorException() {
        Class<MyClassWithOuterClass> clazz = MyClassWithOuterClass.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(MyOuterClass.class));
        try {
            instantiator.newInstance(clazz);
        } catch (InstantiationException e) {
            throw e;
        }
    }

    @Test
    public void testWithOuterClass_NormalCase() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        Object outerClassInstance = mock(Object.class);
        Class<MyClass> clazz = MyClass.class;
        MyClass instance = instantiator.withOuterClass(clazz);
        assertEquals(outerClassInstance, instance.getOuterClass());
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_NoSuchConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        Class<MyClassWithWrongOuterClass> clazz = MyClassWithWrongOuterClass.class;
        try {
            instantiator.withOuterClass(clazz);
        } catch (InstantiationException e) {
            throw e;
        }
    }

    @Test
    public void testParamsException_NoArgConstructorException() {
        Class<MyClassNoArgConstructor> clazz = MyClassNoArgConstructor.class;
        try {
            new ConstructorInstantiator(null).newInstance(clazz);
        } catch (Exception e) {
            InstantiationException exception = new ConstructorInstantiator(null).paramsException(clazz, e);
            assertEquals("Unable to create mock instance of 'MyClassNoArgConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
        }
    }

    @Test
    public void testParamsException_DeclaredConstructorException() {
        Class<MyClassWithOuterClass> clazz = MyClassWithOuterClass.class;
        try {
            new ConstructorInstantiator(mock(MyOuterClass.class)).newInstance(clazz);
        } catch (Exception e) {
            InstantiationException exception = new ConstructorInstantiator(mock(MyOuterClass.class)).paramsException(clazz, e);
            assertEquals("Unable to create mock instance of 'MyClassWithOuterClass'.\nPlease ensure the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor_NormalCase() {
        Object instance = new ConstructorInstantiator(null).noArgConstructor(MyClass.class);
        assertEquals(MyClass.class, instance.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_NoSuchConstructorException() {
        try {
            new ConstructorInstantiator(null).noArgConstructor(MyClassWithNoSuchConstructor.class);
        } catch (Exception e) {
            InstantiationException exception = new ConstructorInstantiator(null).paramsException(MyClassWithNoSuchConstructor.class, e);
            assertEquals("Unable to create mock instance of 'MyClassWithNoSuchConstructor'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
        }
    }
}

class MyClass {
    public MyClass() {}
}

class MyClassWithOuterClass {
    MyOuterClass outerClass;

    MyClassWithOuterClass(MyOuterClass outerClass) {
        this.outerClass = outerClass;
    }
}

class MyClassWithWrongOuterClass {
    MyMismatchedClass outerClass;

    MyClassWithWrongOuterClass(MyMismatchedClass outerClass) {
        this.outerClass = outerClass;
    }
}

class MyMismatchedClass {}

class MyClassNoArgConstructor {}

class MyClassWithNoSuchConstructor {}