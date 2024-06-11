package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder_DefaultConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_CustomConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder(13, 7);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder(13, 7);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(true);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[5];
        hcb.append(booleanArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        boolean[] booleanArray2 = new boolean[5];
        hcb2.append(booleanArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((byte) 5);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = new byte[5];
        hcb.append(byteArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        byte[] byteArray2 = new byte[5];
        hcb2.append(byteArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append('a');
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = new char[5];
        hcb.append(charArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        char[] charArray2 = new char[5];
        hcb2.append(charArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(3.14);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(3.14);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = new double[5];
        hcb.append(doubleArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        double[] doubleArray2 = new double[5];
        hcb2.append(doubleArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 3.14);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((float) 3.14);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = new float[5];
        hcb.append(floatArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        float[] floatArray2 = new float[5];
        hcb2.append(floatArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(42);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(42);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = new int[5];
        hcb.append(intArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        int[] intArray2 = new int[5];
        hcb2.append(intArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1L);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = new long[5];
        hcb.append(longArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        long[] longArray2 = new long[5];
        hcb2.append(longArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendObject() {
        String stringObject = "test";
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(stringObject);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        String stringObject2 = "test";
        hcb2.append(stringObject2);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendObjectArray() {
        String[] stringArray = new String[5];
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(stringArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        String[] stringArray2 = new String[5];
        hcb2.append(stringArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 5);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((short) 5);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = new short[5];
        hcb.append(shortArray);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        short[] shortArray2 = new short[5];
        hcb2.append(shortArray2);
        assertNotEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilder_AppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(1234);
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.appendSuper(1234);
        assertEquals(initialHashCode, hcb2.toHashCode());
    }

    @Test
    public void testReflectionHashCode_Object() {
        Object testObject = new TestObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(testObject, true);
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testReflectionHashCode_ObjectWithExcludedFields() {
        Object testObject = new TestObject();
        String[] excludedFields = new String[]{"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, excludedFields);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(testObject, excludedFields, TestObject.class);
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testReflectionHashCode_ObjectWithSuperClass() {
        Object testSuperClassObject = new TestSuperClassObject();
        int hashCode = HashCodeBuilder.reflectionHashCode(testSuperClassObject, TestSuperClassObject.class, TestSubclassObject.class);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(testSuperClassObject, TestSuperClassObject.class, TestSubclassObject.class, true);
        assertEquals(hashCode, hashCode2);
    }

    private static class TestObject {
        private int field1 = 1;
        private String field2 = "test";

        @Override
        public String toString() {
            return "TestObject [field1=" + field1 + ", field2=" + field2 + "]";
        }
    }

    private static class TestSuperClassObject {
        private int superField = 2;

        @Override
        public String toString() {
            return "TestSuperClassObject [superField=" + superField + "]";
        }
    }

    private static class TestSubclassObject extends TestSuperClassObject {
        private int subField = 3;

        @Override
        public String toString() {
            return "TestSubclassObject [superField=" + super.superField + ", subField=" + subField + "]";
        }
    }
}