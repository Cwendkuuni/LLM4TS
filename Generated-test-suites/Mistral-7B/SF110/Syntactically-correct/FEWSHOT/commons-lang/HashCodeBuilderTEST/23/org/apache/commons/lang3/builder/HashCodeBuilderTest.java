package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        assertEquals(17, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        assertEquals(11, hashCodeBuilderWithInitialValue.toHashCode());
        assertEquals(235405951, hashCodeBuilderWithInitialValue.hashCode()); // 11 * 13 + 17
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        assertEquals(34, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(true);
        assertEquals(235405985, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + (1 + 0)
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hashCodeBuilder.append(booleanArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(booleanArray);

        int expectedHashCode = 235405985; // 11 * 13 + 1
        for (boolean booleanValue : booleanArray) {
            expectedHashCode = expectedHashCode * 13 + (booleanValue ? 0 : 1);
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 1);
        assertEquals(35, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append((byte) 1);
        assertEquals(235406017, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + 1 + 1
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(byteArray);

        int expectedHashCode = 235406017; // 11 * 13 + 1
        for (byte byteValue : byteArray) {
            expectedHashCode = expectedHashCode * 13 + byteValue;
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        assertEquals(37, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append('a');
        assertEquals(235406043, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + 'a'.hashCode()
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(charArray);

        int expectedHashCode = 235406043; // 11 * 13 + 'a'.hashCode()
        for (char charValue : charArray) {
            expectedHashCode = expectedHashCode * 13 + charValue;
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.2);
        assertEquals(513123115, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(1.2);
        assertEquals(235406259, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + Double.doubleToLongBits(1.2)
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {1.2, 2.3, 3.4};
        hashCodeBuilder.append(doubleArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(doubleArray);

        int expectedHashCode = 235406259; // 11 * 13 + Double.doubleToLongBits(1.2)
        for (double doubleValue : doubleArray) {
            expectedHashCode = expectedHashCode * 13 + Double.doubleToLongBits(doubleValue);
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.2f);
        assertEquals(513123115, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(1.2f);
        assertEquals(235406259, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + Float.floatToIntBits(1.2f)
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {1.2f, 2.3f, 3.4f};
        hashCodeBuilder.append(floatArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(floatArray);

        int expectedHashCode = 235406259; // 11 * 13 + Float.floatToIntBits(1.2f)
        for (float floatValue : floatArray) {
            expectedHashCode = expectedHashCode * 13 + Float.floatToIntBits(floatValue);
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        assertEquals(39, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(1);
        assertEquals(235406077, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + 1
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(intArray);

        int expectedHashCode = 235406077; // 11 * 13 + 1
        for (int intValue : intArray) {
            expectedHashCode = expectedHashCode * 13 + intValue;
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        assertEquals(41, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(1L);
        assertEquals(235406111, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + 1
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(longArray);

        int expectedHashCode = 235406111; // 11 * 13 + 1
        for (long longValue : longArray) {
            expectedHashCode = expectedHashCode * 13 + longValue;
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String stringObject = "test";
        hashCodeBuilder.append(stringObject);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(stringObject);

        int expectedHashCode = 235406111; // 11 * 13 + stringObject.hashCode()
        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {"test1", "test2", "test3"};
        hashCodeBuilder.append(objectArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(objectArray);

        int expectedHashCode = 235406111; // 11 * 13 + "test1".hashCode()
        for (Object object : objectArray) {
            expectedHashCode = expectedHashCode * 13 + object.hashCode();
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 1);
        assertEquals(43, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append((short) 1);
        assertEquals(235406121, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + (short) 1
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.append(shortArray);

        int expectedHashCode = 235406121; // 11 * 13 + (short) 1
        for (short shortValue : shortArray) {
            expectedHashCode = expectedHashCode * 13 + shortValue;
        }

        assertEquals(expectedHashCode, hashCodeBuilderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(357);
        assertEquals(390352291, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilderWithInitialValue = new HashCodeBuilder(11, 13);
        hashCodeBuilderWithInitialValue.appendSuper(357);
        assertEquals(235406153, hashCodeBuilderWithInitialValue.toHashCode()); // 11 * 13 + 357
    }
}