package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationMockingDetails;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testNullConstructorClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        assertThrows(NullPointerException.class, () -> instantiator.newInstance(null));
    }

    @Test
    public void testNullOuterClassInstanceNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
    }

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class, Strictness.LENIENT));
        String mockedInstance = instantiator.newInstance(String.class);

        assertNotNull(mockedInstance);
        assertEquals("", mockedInstance);
    }

    @Test
    public void testArgConstructorWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(String[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            //expected
        }
    }

    @Test
    public void testArgConstructorWithOuterClassInstance() {
        Object outerClassInstance = mock(Object.class, Strictness.LENIENT);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        String[] args = new String[]{"test"};
        String mockedInstance = instantiator.newInstance(String[].class, args);

        assertNotNull(mockedInstance);
        assertEquals("test", mockedInstance[0]);
    }

    @Test
    public void testArgConstructorWithOuterClassInstanceThrowsException() {
        Object outerClassInstance = mock(Object.class, Strictness.LENIENT);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<?> cls = String[].class;
        try {
            instantiator.newInstance(cls);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            //expected
        }

        try {
            Constructor<String[]> constructor = cls.getDeclaredConstructor(outerClassInstance.getClass());
            InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.withOuterClass(cls));
            assertEquals("Unable to create mock instance of '[" + String.class.getSimpleName() + "].'.\nPlease ensure that the outer instance has correct type and that the target class has a constructor taking parameter(s) of type(s) [" + outerClassInstance.getClass().getSimpleName() + "]", exception.getMessage());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testArgConstructorWithOuterClassInstanceAndValidConstructor() {
        Object outerClassInstance = mock(Object.class, Strictness.LENIENT);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        String[] args = new String[]{"test"};
        String[] mockedInstance = instantiator.withOuterClass(String[].class, args);

        assertNotNull(mockedInstance);
        assertEquals(args[0], mockedInstance[0]);
    }
}