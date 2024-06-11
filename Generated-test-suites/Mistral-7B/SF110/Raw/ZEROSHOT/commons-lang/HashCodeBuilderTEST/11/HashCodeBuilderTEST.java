package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Builder;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        // Test default constructor
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.toHashCode());

        // Test constructor with initial odd number and multiplier odd number
        HashCodeBuilder hcb1 = new HashCodeBuilder(13, 5);
        assertEquals(13, hcb1.iConstant);
        assertEquals(5, hcb1.iTotal);

        // Test append methods for boolean, byte, char, double, float, int, long, Object, short
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(true);
        hcb2.append(false);
        hcb2.append((byte) 1);
        hcb2.append((byte) -1);
        hcb2.append((char) 'a');
        hcb2.append((char) 'z');
        hcb2.append(1.23d);
        hcb2.append(1.23f);
        hcb2.append(1);
        hcb2.append(123);
        hcb2.append(123L);
        hcb2.append("abc");
        hcb2.append(new Object());
        hcb2.append(new Object[0]);
        hcb2.append((short) 127);
        hcb2.append((short) -128);

        // Test append methods for arrays
        boolean[] booleanArray = {true, false};
        byte[] byteArray = {(byte) 1, (byte) -1};
        char[] charArray = {'a', 'z'};
        double[] doubleArray = {1.23, 1.23};
        float[] floatArray = {1.23f, 1.23f};
        int[] intArray = {1, 123};
        long[] longArray = {1, 123L};
        Object[] objectArray = {new Object(), new Object[0]};
        short[] shortArray = {(short) 127, (short) -128};
        hcb2.append(booleanArray);
        hcb2.append(byteArray);
        hcb2.append(charArray);
        hcb2.append(doubleArray);
        hcb2.append(floatArray);
        hcb2.append(intArray);
        hcb2.append(longArray);
        hcb2.append(objectArray);
        hcb2.append(shortArray);

        // Test append method for Object
        Object obj = new Object();
        HashCodeBuilder hcb3 = new HashCodeBuilder();
        hcb3.append(obj);
        hcb3.append(obj.getClass().getSimpleName());
        assertEquals(hcb2.toHashCode(), hcb3.toHashCode());

        // Test reflectionHashCode methods
        Object reflectionTestObj = new ReflectionTestClass();
        int reflectionHashCode1 = HashCodeBuilder.reflectionHashCode(17, 37, reflectionTestObj);
        int reflectionHashCode2 = HashCodeBuilder.reflectionHashCode(reflectionTestObj);
        int reflectionHashCode3 = HashCodeBuilder.reflectionHashCode(reflectionTestObj, true);
        int reflectionHashCode4 = HashCodeBuilder.reflectionHashCode(reflectionTestObj, Arrays.asList("field1", "field2"), null, new String[0]);
        int reflectionHashCode5 = HashCodeBuilder.reflectionHashCode(reflectionTestObj, Arrays.asList("field1"), ReflectionTestClass.class, new String[]{"field3"});
        assertEquals(reflectionHashCode1, reflectionHashCode2);
        assertEquals(reflectionHashCode2, reflectionHashCode3);
        assertEquals(reflectionHashCode3, reflectionHashCode4);
        assertEquals(reflectionHashCode4, reflectionHashCode5);
    }

    // Reflection test class
    static class ReflectionTestClass {
        private int field1 = 1;
        private String field2 = "test";
        private String field3 = "another test";

        public String getField3() {
            return field3;
        }
    }

    // Utility class for accessing private fields
    static class ReflectionUtils {
        public static Object getFieldValue(Object obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
            Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
            return field.get(obj);
        }

        public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
            Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
            field.set(obj, value);
        }
    }

    @Test
    public void testReflectAppend() throws IllegalAccessException, NoSuchFieldException {
        Object reflectionTestObj = new ReflectionTestClass();
        HashCodeBuilder hcb = new HashCodeBuilder();
        Field[] fields = ReflectionTestClass.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isSynthetic() && !field.getName().equals("class")) {
                Object fieldValue = ReflectionUtils.getFieldValue(reflectionTestObj, field.getName());
                hcb.append(fieldValue);
                ReflectionUtils.setFieldValue(reflectionTestObj, field.getName(), null);
            }
        }
        assertEquals(HashCodeBuilder.reflectionHashCode(17, 37, reflectionTestObj), hcb.toHashCode());
        ReflectionUtils.setFieldValue(reflectionTestObj, "field1", 1);
        ReflectionUtils.setFieldValue(reflectionTestObj, "field2", "test");
        assertEquals(HashCodeBuilder.reflectionHashCode(17, 37, reflectionTestObj), hcb.toHashCode());
    }

    // Test registry
    @Test
    public void testRegistry() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj1);
        hcb.append(obj2);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(2, registry.size());
        IDKey idKey1 = registry.stream().filter(id -> id.getValue().equals(obj1)).findFirst().get();
        IDKey idKey2 = registry.stream().filter(id -> id.getValue().equals(obj2)).findFirst().get();
        assertEquals(obj1, idKey1.getValue());
        assertEquals(obj2, idKey2.getValue());
        hcb.unregister(obj1);
        registry = HashCodeBuilder.getRegistry();
        assertEquals(1, registry.size());
        assertEquals(obj2, registry.stream().findFirst().get().getValue().getClass().isInstance(obj2) ? obj2 : null);
    }
}