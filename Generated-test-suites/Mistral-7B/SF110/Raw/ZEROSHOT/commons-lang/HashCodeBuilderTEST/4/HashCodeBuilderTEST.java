package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    private static HashCodeBuilder hashCodeBuilder;
    private static int initialHashCode;
    private static int multiplierHashCode;
    private static Object testObject;

    @BeforeClass
    public static void setUpBeforeClass() {
        hashCodeBuilder = new HashCodeBuilder(17, 37);
        initialHashCode = 17;
        multiplierHashCode = 37;
        testObject = new TestClass();
    }

    @Test
    public void testAppendBoolean() {
        hashCodeBuilder.append(true);
        assertEquals(initialHashCode * multiplierHashCode + 1, hashCodeBuilder.toHashCode());
        hashCodeBuilder.append(false);
        assertEquals(initialHashCode * multiplierHashCode + 1 + hashCodeBuilder.hashCode(), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] booleanArray = {true, false};
        hashCodeBuilder.append(booleanArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (boolean booleanValue : booleanArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + (booleanValue ? 0 : 1);
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        hashCodeBuilder.append((byte) 5);
        assertEquals(initialHashCode * multiplierHashCode + 5, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        byte[] byteArray = {5, 10};
        hashCodeBuilder.append(byteArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (byte byteValue : byteArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + byteValue;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        hashCodeBuilder.append('a');
        assertEquals(initialHashCode * multiplierHashCode + (int)'a', hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        char[] charArray = {'a', 'b'};
        hashCodeBuilder.append(charArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (char charValue : charArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + charValue;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        hashCodeBuilder.append(1.2);
        long doubleBits = Double.doubleToLongBits(1.2);
        assertEquals(initialHashCode * multiplierHashCode + Double.hashCode(1.2), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        double[] doubleArray = {1.2, 2.3};
        hashCodeBuilder.append(doubleArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (double doubleValue : doubleArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + Double.hashCode(doubleValue);
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        hashCodeBuilder.append((float) 1.2);
        int floatBits = Float.floatToIntBits((float) 1.2);
        assertEquals(initialHashCode * multiplierHashCode + Float.hashCode((float) 1.2), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        float[] floatArray = {1.2f, 2.3f};
        hashCodeBuilder.append(floatArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (float floatValue : floatArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + Float.hashCode(floatValue);
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        hashCodeBuilder.append(5);
        assertEquals(initialHashCode * multiplierHashCode + 5, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        int[] intArray = {5, 10};
        hashCodeBuilder.append(intArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (int intValue : intArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + intValue;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        hashCodeBuilder.append(5L);
        assertEquals(initialHashCode * multiplierHashCode + (int)(5L ^ (5L >>> 32)), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        long[] longArray = {5L, 10L};
        hashCodeBuilder.append(longArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (long longValue : longArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + (int)(longValue ^ (longValue >>> 32));
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        hashCodeBuilder.append(testObject);
        assertEquals(initialHashCode * multiplierHashCode + testObject.hashCode(), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        Object[] objectArray = {new TestClass(), new TestClass()};
        hashCodeBuilder.append(objectArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (Object object : objectArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + object.hashCode();
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        hashCodeBuilder.append((short) 5);
        assertEquals(initialHashCode * multiplierHashCode + 5, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        short[] shortArray = {5, 10};
        hashCodeBuilder.append(shortArray);
        int expectedHashCode = initialHashCode * multiplierHashCode;
        for (short shortValue : shortArray) {
            expectedHashCode = expectedHashCode * multiplierHashCode + shortValue;
        }
        assertEquals(expectedHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        hashCodeBuilder.appendSuper(initialHashCode);
        assertEquals(initialHashCode * multiplierHashCode + initialHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeBoolean() {
        assertEquals(hashCodeBuilder.reflectionHashCode(true), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeBooleanArray() {
        boolean[] booleanArray = {true, false};
        assertEquals(hashCodeBuilder.reflectionHashCode(booleanArray, false), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeClass() {
        assertEquals(hashCodeBuilder.reflectionHashCode(TestClass.class, true), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeClassSuperClass() {
        assertEquals(hashCodeBuilder.reflectionHashCode(TestClass.class, TestSuperClass.class, true), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeObject() {
        assertEquals(hashCodeBuilder.reflectionHashCode(testObject, false), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeObjectExcludeFields() {
        String[] excludeFields = {"field1", "field2"};
        assertEquals(hashCodeBuilder.reflectionHashCode(testObject, excludeFields), hashCodeBuilder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionNullObject() {
        hashCodeBuilder.reflectionHashCode(null, false);
    }

    private static class TestClass {
        private int field1;
        private String field2;

        public TestClass() {
            field1 = 5;
            field2 = "test";
        }

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }

    private static class TestSuperClass {
        private int superField;

        public TestSuperClass() {
            superField = 10;
        }

        public int getSuperField() {
            return superField;
        }
    }
}