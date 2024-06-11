package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.IDKey;
import org.apache.commons.lang3.Reflections;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(0, hcb.hashCode());

        HashCodeBuilder hcb1 = new HashCodeBuilder(1, 3);
        assertEquals(1, hcb1.hashCode());

        HashCodeBuilder hcb2 = new HashCodeBuilder(17, 37);
        assertEquals(17, hcb2.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int expectedResult = 0;
        expectedResult = expectedResult * 37 + 1;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);

        int expectedResult = 0;
        for (boolean value : booleanArray) {
            expectedResult = expectedResult * 37 + (value ? 0 : 1);
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 0);
        int expectedResult = 0;
        expectedResult = expectedResult * 37;
        assertEquals(expectedResult, hcb.hashCode());

        hcb.append((byte) 5);
        expectedResult = expectedResult * 37 + 5;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {5, 0};
        hcb.append(byteArray);

        int expectedResult = 0;
        for (byte value : byteArray) {
            expectedResult = expectedResult * 37 + value;
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('A');
        int expectedResult = 0;
        expectedResult = expectedResult * 37 + 'A' & 0xFF;
        assertEquals(expectedResult, hcb.hashCode());

        hcb.append('Z');
        expectedResult = expectedResult * 37 + 'Z' & 0xFF;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'A', 'Z'};
        hcb.append(charArray);

        int expectedResult = 0;
        for (char value : charArray) {
            expectedResult = expectedResult * 37 + value;
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0);
        long doubleValue = Double.doubleToLongBits(1.0);
        int expectedResult = 1;
        expectedResult = expectedResult * 37 + (int) (doubleValue ^ doubleValue >> 32);
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        hcb.append(doubleArray);

        long expectedResult = 0;
        for (double value : doubleArray) {
            long doubleValue = Double.doubleToLongBits(value);
            expectedResult = expectedResult * 37 + (int) (doubleValue ^ doubleValue >> 32);
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0f);
        int expectedResult = 1;
        expectedResult = expectedResult * 37 + Float.floatToIntBits(1.0f);
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        hcb.append(floatArray);

        int expectedResult = 0;
        for (float value : floatArray) {
            expectedResult = expectedResult * 37 + Float.floatToIntBits(value);
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        int expectedResult = 1;
        expectedResult = expectedResult * 37 + 1;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2};
        hcb.append(intArray);

        int expectedResult = 0;
        for (int value : intArray) {
            expectedResult = expectedResult * 37 + value;
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        int expectedResult = 1;
        expectedResult = expectedResult * 37 + (int)(1L ^ 1L >> 32);
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1L, 2L};
        hcb.append(longArray);

        int expectedResult = 0;
        for (long value : longArray) {
            expectedResult = expectedResult * 37 + (int)(value ^ value >> 32);
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object object = new Object();
        hcb.append(object);

        if (object == null) {
            expectedResult = 0;
        } else {
            expectedResult = expectedResult * 37 + object.hashCode();
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);

        int expectedResult = 0;
        for (Object value : objectArray) {
            if (value == null) {
                expectedResult *= 37;
            } else {
                expectedResult = expectedResult * 37 + value.hashCode();
            }
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        int expectedResult = 1;
        expectedResult = expectedResult * 37 + 1;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2};
        hcb.append(shortArray);

        int expectedResult = 0;
        for (short value : shortArray) {
            expectedResult = expectedResult * 37 + value;
        }
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 5;
        hcb.appendSuper(superHashCode);
        int expectedResult = hcb.hashCode();
        expectedResult = expectedResult * 37 + superHashCode;
        assertEquals(expectedResult, hcb.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object testObject = new TestClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        TestClass testClass = Reflections.getDeclaredSubclasses(TestClass.class, TestClass.class);
        int expectedResult = 0;

        for (Field field : Reflections.getDeclaredFields(TestClass.class)) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(testObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            expectedResult = expectedResult * 37 + (fieldValue == null ? 0 : fieldValue.hashCode());
        }

        for (Class<?> superclass = testClass.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            for (Field field : Reflections.getDeclaredFields(superclass)) {
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(testObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                expectedResult = expectedResult * 37 + (fieldValue == null ? 0 : fieldValue.hashCode());
            }
        }

        assertEquals(expectedResult, hashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayWithNullValue() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), null};
        hcb.append(objectArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNegativeInitialOddNumber() {
        HashCodeBuilder hcb = new HashCodeBuilder(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNegativeMultiplierOddNumber() {
        HashCodeBuilder hcb = new HashCodeBuilder(1, -3);
    }

    static class TestClass {
        private int field1;
        private short field2;
        private char field3;
        private boolean field4;
        private byte field5;
        private double field6;
        private float field7;
        private long field8;

        public TestClass() {
            this.field1 = 1;
            this.field2 = 2;
            this.field3 = 'A';
            this.field4 = true;
            this.field5 = 5;
            this.field6 = 1.0;
            this.field7 = 1.0f;
            this.field8 = 1L;
        }
    }
}