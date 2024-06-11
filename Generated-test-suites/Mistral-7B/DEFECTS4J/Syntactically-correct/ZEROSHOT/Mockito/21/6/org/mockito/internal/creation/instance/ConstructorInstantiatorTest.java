package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.testng.PowerMockTestCase;
import org.powermock.reflect.construction.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(AnnotatedClassLoader.class)
public class ConstructorInstantiatorTest {

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstanceWithNullOuterClassInstance() {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        T instance = constructorInstantiator.newInstance(cls);

        verify(cls).newInstance();
        assertEquals(null, instance);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNewInstanceWithArgConstructor() throws Exception {
        Class<OuterClass> outerClass = mock(Class.class);
        Class<InnerClass> innerClass = mock(Class.class);
        Constructor<InnerClass> innerClassConstructor = mockConstructor(InnerClass.class);
        Object outerClassInstance = mock(OuterClass.class);

        when(outerClass.getDeclaredConstructor(outerClass.getType())).thenReturn(innerClassConstructor);

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = constructorInstantiator.newInstance(innerClass);

        verify(outerClassInstance).getClass();
        verify(innerClassConstructor).newInstance(outerClassInstance);
        verifyNoMoreInteractions(outerClass, innerClassConstructor);

        assertNotNull(instance);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstanceWithNoArgConstructorThrowsException() throws Exception {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        when(cls.getDeclaredConstructors()).thenReturn(new Constructor[0]);

        constructorInstantiator.newInstance(cls);

        verify(cls).newInstance();
        fail("Expected InstantiationException but it was not thrown.");
    }

    @SuppressWarnings("unchecked")
    @Test(expected = InstantiationException.class)
    public <T> void testNewInstanceWithArgConstructorThrowsException() throws Exception {
        Class<OuterClass> outerClass = mock(Class.class);
        Class<InnerClass> innerClass = mock(Class.class);
        Constructor<InnerClass> innerClassConstructor = mockConstructor(InnerClass.class);

        when(outerClass.getDeclaredConstructor(outerClass.getType())).thenThrow(new InstantiationException(""));

        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(innerClass);

        verify(outerClass).getDeclaredConstructor(outerClass.getType());
        fail("Expected InstantiationException but it was not thrown.");
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testParamsException() {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        when(cls.getSimpleName()).thenReturn("MockClass");

        try {
            constructorInstantiator.paramsException(cls, new RuntimeException(""));
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'MockClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
            String actualMessage = e.getMessage();

            assertEquals(expectedMessage, actualMessage);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testNoArgConstructor() throws Exception {
        Class<T> cls = mock(Class.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        when(cls.isInterface()).thenReturn(false);
        when(cls.getDeclaredConstructors()).thenReturn(new Constructor[0]);

        T instance = constructorInstantiator.newInstance(cls);

        verify(cls).newInstance();
        verifyNoMoreInteractions(cls);

        assertNotNull(instance);
    }

    private static Constructor<InnerClass> mockConstructor(Class<InnerClass> innerClass) {
        try {
            return innerClass.getDeclaredConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static class OuterClass {
    }

    @SuppressWarnings("unchecked")
    static class InnerClass {
    }

    @Before
    public void initMocks() {
        initMocks(this);
    }
}