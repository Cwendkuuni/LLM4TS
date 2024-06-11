package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCodeWithObject() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertEquals(hashCode, HashCodeBuilder.reflectionHashCode(object, false));
    }

    @Test
    public void testReflectionHashCodeWithCollectionExcludeFields() {
        String[] excludeFields = new String[]{"field1", "field2"};
        Object object = new MyClass("field1", "field2");
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false, MyClass.class, excludeFields);
        assertEquals(hashCode, HashCodeBuilder.reflectionHashCode(object, false, MyClass.class, excludeFields));
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        Object object = new MyClass(true, 1, "field1", new int[0]);
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true);
        assertEquals(hashCode, HashCodeBuilder.reflectionHashCode(object, true));
    }

    @Test
    public void testReflectionHashCodeWithSuperClass() {
        Object parentObject = new ParentClass();
        Object childObject = new ChildClass(parentObject);
        int hashCode = HashCodeBuilder.reflectionHashCode(childObject, ParentClass.class, false);
        assertEquals(hashCode, HashCodeBuilder.reflectionHashCode(childObject, ParentClass.class, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 0, 0, false, null, new String[0]);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(true);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[]{true, false};
        builder.append(booleanArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(booleanArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append((byte) 1);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = new byte[]{1, 2, 3};
        builder.append(byteArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(byteArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append('a');
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = new char[]{'a', 'b', 'c'};
        builder.append(charArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(charArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14159265358979323846;
        builder.append(value);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(value);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = new double[]{3.14159265358979323846, 2.7182818284590451027};
        builder.append(doubleArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(doubleArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 1.5f;
        builder.append(value);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(value);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = new float[]{1.5f, 2.5f};
        builder.append(floatArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(floatArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(1);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = new int[]{1, 2, 3};
        builder.append(intArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(intArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 1L;
        builder.append(value);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(value);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = new long[]{1L, 2L, 3L};
        builder.append(longArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(longArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        String objectString = "objectString";
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(objectString);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(objectString);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        String[] objectArray = new String[]{"string1", "string2"};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(objectArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(objectArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append((short) 1);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = new short[]{1, 2, 3};
        builder.append(shortArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.append(shortArray);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        int superHashCode = 1;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(superHashCode);
        int hashCode = builder.toHashCode();
        HashCodeBuilder newBuilder = new HashCodeBuilder();
        newBuilder.appendSuper(superHashCode);
        assertEquals(hashCode, newBuilder.toHashCode());
    }

    static class ParentClass {
    }

    static class ChildClass extends ParentClass {
        private int value;

        public ChildClass(ParentClass parentObject) {
            this.value = 1;
        }
    }

    static class MyClass {
        private boolean flag;
        private String field1;
        private int field2;

        public MyClass(boolean flag, String field1, int field2) {
            this.flag = flag;
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}