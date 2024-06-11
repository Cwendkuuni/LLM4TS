package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationSpy;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterClassInstance() {
        Constructor<ConstructorInstantiator> constructor = ConstructorInstantiator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConstructorInstantiator constructorInstantiator = null;
        try {
            constructorInstantiator = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assertNull(constructorInstantiator);

        ConstructorInstantiator constructorInstantiatorInstance = new ConstructorInstantiator(null);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiatorInstance.newInstance(Mockito.spy(InvocationSpy.class)));
        assertNotNull(exception.getCause());
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        Object outerClassInstance = Mockito.spy(InvocationSpy.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(Mockito.spy(InvocationSpy.class).getClass()));
        assertNull(exception.getCause());
        assertNotNull(exception.getMessage());

        Constructor<InvocationSpy> constructor = InvocationSpy.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationSpy instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass() {
        Object outerClassInstance = Mockito.spy(InvocationSpy.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(ConstructorInstantiator.class));
        assertNull(exception.getCause());
        assertNotNull(exception.getMessage());

        Constructor<ConstructorInstantiator> constructor = ConstructorInstantiator.class.getDeclaredConstructor(outerClassInstance.getClass());
        constructor.setAccessible(true);
        ConstructorInstantiator instance = constructor.newInstance(outerClassInstance);
        assertNotNull(instance);
        assertNotNull(instance.outerClassInstance);
        assertSame(outerClassInstance, instance.outerClassInstance);
    }

    @Test
    public void testExceptionOnNoArgConstructor() {
        Object outerClassInstance = Mockito.spy(InvocationSpy.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(String.class));
        assertNull(exception.getCause());
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testExceptionOnConstructorWithParams() {
        Object outerClassInstance = Mockito.spy(InvocationSpy.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        InstantiationException exception = assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(ConstructorWithParams.class));
        assertNotNull(exception.getCause());
        assertNotNull(exception.getMessage());
    }

    private static class ConstructorWithParams {
        public ConstructorWithParams(Object outerClassInstance) {
        }
    }
}