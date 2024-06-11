
package org.junit.platform.commons.util;

import static java.lang.reflect.Modifier.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    // Existing methods

    public static <T> T getFieldValue(Object source, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        // Existing implementation
    }

    public static <T> void setFieldValue(Object source, String fieldName, T newValue) throws IllegalAccessException, NoSuchFieldException {
        // Existing implementation
    }

    // Add new method to get a private static final field value
    public static int getPrivateStaticFinalIntField(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        int value = (int) field.get(null);
        return value;
    }

    // Add new method to invoke a private static final method
    public static <T> T invokePrivateStaticFinalMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] arguments)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return (T) method.invoke(null, arguments);
    }

    // Add new method to get all private fields of a given class
    public static List<Field> getAllPrivateFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .collect(Collectors.toList());
    }
}
