package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    private static HashCodeBuilder hashCodeBuilder;
    private static Object testObject;

    @BeforeClass
    public static void setup() {
        testObject = new TestObject();
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void testAppendBoolean() {
        hashCodeBuilder.append(true);
        assertEquals(37, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] booleanArray = {true, false};
        hashCodeBuilder.append(booleanArray);
        int expectedHashCode = 37 * 37 + 1; // 1 for true
        expectedHashCode *= 37 + 0; // 0 for false
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendByte() {
        hashCodeBuilder.append((byte) 10);
        assertEquals(37 * 37 * 16 + 10, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        byte[] byteArray = {5, 1, 3};
        hashCodeBuilder.append(byteArray);
        int expectedHashCode = 37 * 37 * 37 * 3 + 5 * 37 + 1 * 37 + 3;
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendChar() {
        hashCodeBuilder.append('A');
        assertEquals(37 * 37 * 16 + 'A'.hashCode(), hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        char[] charArray = {'A', 'B', 'C'};
        hashCodeBuilder.append(charArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (char c : charArray) {
            expectedHashCode *= 37 + c.hashCode();
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendDouble() {
        hashCodeBuilder.append(1.1);
        long doubleHashCode = Double.doubleToLongBits(1.1);
        assertEquals(37 * 37 * 16 + (int) (doubleHashCode ^ doubleHashCode >> 32), hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        double[] doubleArray = {1.1, 2.2, 3.3};
        hashCodeBuilder.append(doubleArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (double d : doubleArray) {
            long doubleHashCode = Double.doubleToLongBits(d);
            expectedHashCode *= 37 + (int) (doubleHashCode ^ doubleHashCode >> 32);
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendFloat() {
        hashCodeBuilder.append((float) 1.5);
        int floatHashCode = Float.floatToIntBits((float) 1.5);
        assertEquals(37 * 37 * 16 + floatHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        float[] floatArray = {1.5f, 2.5f, 3.5f};
        hashCodeBuilder.append(floatArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (float f : floatArray) {
            int floatHashCode = Float.floatToIntBits(f);
            expectedHashCode *= 37 + floatHashCode;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppenInt() {
        hashCodeBuilder.append(10);
        assertEquals(37 * 37 * 16 + 10, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        int[] intArray = {10, 20, 30};
        hashCodeBuilder.append(intArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (int i : intArray) {
            expectedHashCode *= 37 + i;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendLong() {
        hashCodeBuilder.append(10L);
        long longHashCode = 10;
        assertEquals(37 * 37 * 16 + (int) (longHashCode ^ longHashCode >> 32), hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        long[] longArray = {10L, 20L, 30L};
        hashCodeBuilder.append(longArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (long l : longArray) {
            long longHashCode = l;
            expectedHashCode *= 37 + (int) (longHashCode ^ longHashCode >> 32);
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendObject() {
        hashCodeBuilder.append(testObject);
        int testObjectHashCode = testObject.hashCode();
        assertEquals(37 * 37 * 16 + testObjectHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        TestObject[] testObjectArray = {new TestObject(), new TestObject()};
        hashCodeBuilder.append(testObjectArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (TestObject to : testObjectArray) {
            int testObjectHashCode = to.hashCode();
            expectedHashCode *= 37 + testObjectHashCode;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendShort() {
        hashCodeBuilder.append((short) 10);
        assertEquals(37 * 37 * 16 + (short) 10, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        short[] shortArray = {10, 20, 30};
        hashCodeBuilder.append(shortArray);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (short s : shortArray) {
            expectedHashCode *= 37 + s;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        hashCodeBuilder.appendSuper(testObject.hashCode());
        int testObjectHashCode = testObject.hashCode();
        assertEquals(37 * 37 * 16 + testObjectHashCode, hashCodeBuilder.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (Field field : testObject.getClass().getDeclaredFields()) {
            try {
                Object fieldValue = field.get(testObject);
                int fieldHashCode;
                if (field.getType().isPrimitive()) {
                    fieldHashCode = field.getType().hashCode();
                } else {
                    fieldHashCode = fieldValue.hashCode();
                }
                expectedHashCode *= 37 + fieldHashCode;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unexpected IllegalAccessException", e);
            }
        }
        assertEquals(expectedHashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        String[] excludedFields = {"field1", "field2"};
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObject, excludedFields);
        int expectedHashCode = 37 * 37 * 37 * 3;
        for (Field field : testObject.getClass().getDeclaredFields()) {
            if (!ArrayUtils.contains(excludedFields, field.getName())) {
                try {
                    Object fieldValue = field.get(testObject);
                    int fieldHashCode;
                    if (field.getType().isPrimitive()) {
                        fieldHashCode = field.getType().hashCode();
                    } else {
                        fieldHashCode = fieldValue.hashCode();
                    }
                    expectedHashCode *= 37 + fieldHashCode;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unexpected IllegalAccessException", e);
                }
            }
        }
        assertEquals(expectedHashCode, reflectionHashCode);
    }
}

class TestObject {
    private int field1 = 1;
    private String field2 = "test";
}