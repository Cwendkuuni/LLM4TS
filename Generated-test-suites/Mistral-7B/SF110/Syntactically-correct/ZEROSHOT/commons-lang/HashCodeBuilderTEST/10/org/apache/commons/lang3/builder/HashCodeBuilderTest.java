package org.junit.jupiter.api;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.ArrayUtils.contains;

@SuppressWarnings("unchecked")
public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int initialHashCode = hashCodeBuilder.hashCode();

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(1, 3);
        int initialOddNumber = hashCodeBuilderWithInitialValue.hashCode();

        Assertions.assertThrows(IllegalArgumentException.class, () -> new HashCodeBuilder(0, 3));

        // Test append methods for boolean
        HashCodeBuilder hashCodeBuilderBoolean = hashCodeBuilder.append(true);
        int hashCodeBoolean = hashCodeBuilderBoolean.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeBoolean);
        Assertions.assertEquals(hashCodeBoolean, hashCodeBuilderBoolean.append(false).hashCode());

        // Test append methods for byte
        byte byteValue = 1;
        HashCodeBuilder hashCodeBuilderByte = hashCodeBuilder.append(byteValue);
        int hashCodeByte = hashCodeBuilderByte.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeByte);
        Assertions.assertEquals(hashCodeByte, hashCodeBuilderByte.append((byte) 2).hashCode());

        // Test append methods for char
        char charValue = 'A';
        HashCodeBuilder hashCodeBuilderChar = hashCodeBuilder.append(charValue);
        int hashCodeChar = hashCodeBuilderChar.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeChar);
        Assertions.assertEquals(hashCodeChar, hashCodeBuilderChar.append((char) 2).hashCode());

        // Test append methods for int
        int intValue = 1;
        HashCodeBuilder hashCodeBuilderInt = hashCodeBuilder.append(intValue);
        int hashCodeInt = hashCodeBuilderInt.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeInt);
        Assertions.assertEquals(hashCodeInt, hashCodeBuilderInt.append(2).hashCode());

        // Test append methods for long
        long longValue = 1L;
        HashCodeBuilder hashCodeBuilderLong = hashCodeBuilder.append(longValue);
        int hashCodeLong = hashCodeBuilderLong.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeLong);
        Assertions.assertEquals(hashCodeLong, hashCodeBuilderLong.append(2L).hashCode());

        // Test append methods for Object
        Object objectToAppend = new Object();
        HashCodeBuilder hashCodeBuilderObject = hashCodeBuilder.append(objectToAppend);
        int hashCodeObject = hashCodeBuilderObject.hashCode();
        Assertions.assertEquals(initialHashCode, hashCodeObject);
        Assertions.assertEquals(hashCodeObject, hashCodeBuilderObject.append(new Object()).hashCode());

        // Test reflectionHashCode methods
        Object reflectionTestObject = new ReflectionTestObject();
        int initialReflectionHashCode = HashCodeBuilder.reflectionHashCode(reflectionTestObject);
        Assertions.assertNotEquals(initialHashCode, initialReflectionHashCode);

        int hashCodeReflectionWithExcludedFields = HashCodeBuilder.reflectionHashCode(reflectionTestObject, Arrays.asList("excludedField1", "excludedField2"));
        Assertions.assertNotEquals(initialReflectionHashCode, hashCodeReflectionWithExcludedFields);

        int hashCodeReflectionWithSuperClass = HashCodeBuilder.reflectionHashCode(reflectionTestObject.getClass().getSuperclass());
        Assertions.assertNotEquals(initialReflectionHashCode, hashCodeReflectionWithSuperClass);

        // Test register and unregister methods
        HashCodeBuilder hashCodeBuilderToRegister = new HashCodeBuilder();
        HashCodeBuilder.register(hashCodeBuilderToRegister);
        boolean isRegistered = HashCodeBuilder.isRegistered(hashCodeBuilderToRegister);
        Assertions.assertTrue(isRegistered);

        HashCodeBuilder.unregister(hashCodeBuilderToRegister);
        isRegistered = HashCodeBuilder.isRegistered(hashCodeBuilderToRegister);
        Assertions.assertFalse(isRegistered);
    }

    private static class ReflectionTestObject {
        private int field1 = 1;
        private String field2 = "test";
        private long field3 = 1L;
        private boolean excludedField1 = false;
        private String excludedField2 = "test2";

        @Override
        public String toString() {
            return "ReflectionTestObject{" +
                    "field1=" + field1 +
                    ", field2='" + field2 + '\'' +
                    ", field3=" + field3 +
                    '}';
        }
    }

    private static class IDKey {
        private final Object value;

        public IDKey(Object value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IDKey idKey = (IDKey) o;
            return value.equals(idKey.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }
}

class TestUtil {
    public static void reflectAppend(Object object, Class<?> clazz, HashCodeBuilder builder, boolean useTransients, String[] excludeFields) throws IllegalAccessException {
        HashCodeBuilder.reflectionAppend(object, clazz, builder, useTransients, excludeFields);
    }
}