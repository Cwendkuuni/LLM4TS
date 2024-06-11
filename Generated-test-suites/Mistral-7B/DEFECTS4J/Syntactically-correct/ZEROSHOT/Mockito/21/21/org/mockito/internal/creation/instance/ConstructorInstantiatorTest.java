package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.InlineJavaReflectionUtils;
import org.mockito.internal.util.java.ClassUtils;
import org.mockito.internal.util.java.dependency.InjectingConstructor;
import org.mockito.internal.util.java.dependency.InjectingConstructorParameters;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    private Instantiator instantiator;
    private Object outerClassInstance;

    @Before
    public void setup() {
        outerClassInstance = mock(Object.class);
        instantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterInstance() {
        Class<String> stringClass = String.class;
        assertNotNull(instantiator.newInstance(stringClass));
    }

    @Test
    public void testNewInstanceWithNullOuterInstanceAndNoArgConstructor() {
        Class<String> stringClass = String.class;
        String result = (String) instantiator.newInstance(stringClass);
        assertEquals("", result);
    }

    @Test
    public void testNewInstanceWithNullOuterInstanceAndParametrizedConstructor() {
        Class<?> arrayClass = new ArrayType(String.class, 3).getRawType();
        Class<?> constructorArgType = new ParameterizedType() {
            @Override
            public Type getRawType() {
                return arrayClass;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Type[] constructorArgTypes = new Type[]{outerClassInstance.getClass(), constructorArgType};
        Class<ConstructorInstantiatorTestArray> clazz = new ClassType<ConstructorInstantiatorTestArray>() {}.getDeclaredClass();

        Constructor<ConstructorInstantiatorTestArray> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor1 -> constructor1.getParameterTypes().equals(constructorArgTypes))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());

        assertThrows(InstantiationException.class, () -> instantiator.newInstance(clazz));
    }

    @Test
    public void testNewInstanceWithOuterInstance() {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        StringBuilder result = (StringBuilder) instantiator.newInstance(stringBuilderClass);
        assertEquals("", result);
    }

    @Test
    public void testNewInstanceWithOuterInstanceAndNoArgConstructor() {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        StringBuilder result = (StringBuilder) instantiator.newInstance(stringBuilderClass);
        assertEquals("", result.toString());
    }

    @Test
    public void testNewInstanceWithOuterInstanceAndParametrizedConstructor() {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        Class<String> stringClass = String.class;

        Type[] constructorArgTypes = new Type[]{outerClassInstance.getClass(), stringClass};
        Class<ConstructorInstantiatorTestStringBuilder> clazz = new ClassType<ConstructorInstantiatorTestStringBuilder>() {}.getDeclaredClass();

        Constructor<ConstructorInstantiatorTestStringBuilder> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor1 -> constructor1.getParameterTypes().equals(constructorArgTypes))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());

        StringBuilder result = (StringBuilder) instantiator.newInstance(clazz);
        assertEquals(outerClassInstance.toString(), result.toString());
        assertEquals("outer", result.append("instance").toString());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterInstanceAndNoSuchConstructor() {
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        assertFalse(stringBuilderClass.getDeclaredConstructors().length > 0);
        instantiator.newInstance(stringBuilderClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterInstanceAndThrowsException() {
        Class<Throwable> throwableClass = Throwable.class;
        assertThrows(InstantiationException.class, () -> instantiator.newInstance(throwableClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterInstanceAndNoArgConstructorButThrowsException() {
        Class<Throwable> throwableClass = Throwable.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(throwableClass));
        assertEquals("Unable to create mock instance of 'Throwable'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testParamsException() {
        Class<String> stringClass = String.class;
        try {
            instantiator.paramsException(stringClass, new RuntimeException());
            fail();
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        String result = (String) ConstructorInstantiator.noArgConstructor(stringClass);
        assertEquals("", result);
    }
}

class ClassType<T> {
    private Class<T> clazz;

    T getDeclaredClass() {
        if (clazz == null) {
            try {
                clazz = (Class<T>) ClassUtils.getClassLoader().loadClass(getTypeName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return clazz.getDeclaredClasses()[0];
    }

    private String getTypeName() {
        return getClass().getName().substring(0, getClass().getName().lastIndexOf('.')) + "." + getSimpleName();
    }
}

class ArrayType<T> {
    private Class<T> componentType;

    public static Type getRawType(Class<?> componentType) {
        if (componentType.isArray()) {
            return getRawType(componentType.getComponentType());
        }
        return componentType;
    }

    Type getDeclaredClass() {
        return getRawType(componentType);
    }
}

class InjectingConstructor<T> {
    private Class<T> clazz;
    private Constructor<T> constructor;

    T getInstance(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    InjectingConstructor(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = InlineJavaReflectionUtils.findConstructorByReflection(clazz);
    }
}

class InjectingConstructorParameters<T> {
    private InjectingConstructor<T> constructor;
    private Object[] args;

    T getInstance() {
        return constructor.getInstance(args);
    }

    InjectingConstructorParameters(Class<T> clazz, Object... args) {
        this.constructor = new InjectingConstructor<>(clazz);
        this.args = args;
    }
}