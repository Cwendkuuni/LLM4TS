import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.java.type.TypeUtils;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConstructorInstantiator.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testConstructorInstantiatorWithOuterClassInstance() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<String> stringClass = String.class;
        String expected = "outer";
        String result = instantiator.newInstance(stringClass);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testConstructorInstantiatorWithNoArgConstructor() {
        Class<Object> objectClass = Object.class;
        Object result = new ConstructorInstantiator(null).newInstance(objectClass);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorInstantiatorWithNoArgConstructorFailure() {
        Class<Object> objectClass = Object.class;
        PowerMockito.mockStatic(Object.class);
        PowerMockito.whenNew(Object.class).withNoArguments().thenThrow(new InstantiationException("Error message"));
        new ConstructorInstantiator(null).newInstance(objectClass);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorInstantiatorWithOuterClassInstanceAndWrongType() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Integer> integerClass = Integer.class;
        instantiator.newInstance(integerClass);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorInstantiatorWithOuterClassInstanceAndNoConstructor() {
        Object outerClassInstance = mock(Object.class);
        Constructor<Object> constructor = mock(Constructor.class);
        PowerMockito.whenNew(Object.class, outerClassInstance.getClass()).withArguments(outerClassInstance).thenReturn(constructor);
        PowerMockito.when(constructor.newInstance(outerClassInstance)).thenThrow(new InstantiationException("Error message"));

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(Object.class);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorInstantiatorWithOuterClassInstanceAndConstructorException() {
        Object outerClassInstance = mock(Object.class);
        Constructor<Object> constructor = mock(Constructor.class);
        PowerMockito.whenNew(Object.class, outerClassInstance.getClass()).withArguments(outerClassInstance).thenReturn(constructor);
        PowerMockito.when(constructor.newInstance(outerClassInstance)).thenThrow(new InvocationTargetException(new RuntimeException("Error message")));

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(Object.class);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorInstantiatorWithWrongOuterClassInstanceType() {
        Object outerClassInstance = mock(String.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Integer> integerClass = Integer.class;
        instantiator.newInstance(integerClass);
    }

    @Test
    public void testConstructorInstantiatorWithOuterClassInstanceAndClassWithConstructors() {
        Object outerClassInstance = spy(new Object() {});
        Class<MyClass> myClass = MyClass.class;

        Constructor<MyClass> constructor1 = mock(Constructor.class);
        Constructor<MyClass> constructor2 = mock(Constructor.class);

        PowerMockito.whenNew(MyClass.class, outerClassInstance.getClass(), TypeUtils.typeCapture(String[].class)).withArguments(outerClassInstance, (Object[]) Arrays.asList("arg1", "arg2").toArray()).thenReturn(constructor1);
        PowerMockito.whenNew(MyClass.class, outerClassInstance.getClass(), TypeUtils.typeCapture(int[].class)).withArguments(outerClassInstance, (Object[]) Arrays.asList(1, 2).toArray()).thenReturn(constructor2);

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        MyClass result1 = instantiator.newInstance(myClass);
        assertEquals(constructor1, result1.getClass().getConstructor(outerClassInstance.getClass(), String[].class).getDeclaringClass().getDeclaredConstructor(outerClassInstance.getClass(), String[].class));

        MyClass result2 = instantiator.newInstance(myClass);
        assertEquals(constructor2, result2.getClass().getConstructor(outerClassInstance.getClass(), int[].class).getDeclaringClass().getDeclaredConstructor(outerClassInstance.getClass(), int[].class));
    }
}