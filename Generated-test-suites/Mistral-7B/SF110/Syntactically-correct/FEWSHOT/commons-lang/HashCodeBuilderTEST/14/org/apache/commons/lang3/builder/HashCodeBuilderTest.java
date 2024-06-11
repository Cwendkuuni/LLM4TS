package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(13, 17);

        assertEquals(17, builder.toHashCode());
        assertEquals(13, builderWithInitialValue.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenInitialValue() {
        new HashCodeBuilder(2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenMultiplier() {
        new HashCodeBuilder(1, 2);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expectedHashCode = 32 * 37 + 1;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);

        int expectedHashCode = 37;
        for (boolean bool : booleanArray) {
            expectedHashCode *= 37;
            if (bool) {
                expectedHashCode += 1;
            }
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int expectedHashCode = 37 * 37 + 5;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);

        int expectedHashCode = 37;
        for (byte b : byteArray) {
            expectedHashCode *= 37;
            expectedHashCode += b;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int expectedHashCode = 37 * 37 + 'a';
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);

        int expectedHashCode = 37;
        for (char c : charArray) {
            expectedHashCode *= 37;
            expectedHashCode += c;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14;
        builder.append(value);
        long doubleBits = Double.doubleToLongBits(value);
        int expectedHashCode = 37 * 37 + (int) (doubleBits ^ doubleBits >> 32);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);

        int expectedHashCode = 37;
        for (double d : doubleArray) {
            expectedHashCode *= 37;
            long doubleBits = Double.doubleToLongBits(d);
            expectedHashCode += (int) (doubleBits ^ doubleBits >> 32);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 3.14f;
        builder.append(value);
        int expectedHashCode = 37 * 37
                + Float.floatToIntBits(value);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);

        int expectedHashCode = 37;
        for (float f : floatArray) {
            expectedHashCode *= 37;
            expectedHashCode += Float.floatToIntBits(f);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int value = 5;
        builder.append(value);
        int expectedHashCode = 37 * 37 + value;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);

        int expectedHashCode = 37;
        for (int i : intArray) {
            expectedHashCode *= 37;
            expectedHashCode += i;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 5L;
        builder.append(value);
        int expectedHashCode = 37 * 37 + (int) (value ^ value >> 32);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1L, 2L, 3L};
        builder.append(longArray);

        int expectedHashCode = 37;
        for (long l : longArray) {
            expectedHashCode *= 37;
            expectedHashCode += (int) (l ^ l >> 32);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String object = "Test";
        builder.append(object);

        int expectedHashCode = 37 * 37 + object.hashCode();
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {"Test1", "Test2", "Test3"};
        builder.append(objectArray);

        int expectedHashCode = 1;
        for (Object o : objectArray) {
            expectedHashCode *= 37;
            expectedHashCode += o.hashCode();
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short value = 5;
        builder.append(value);
        int expectedHashCode = 37 * 37 + value;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);

        int expectedHashCode = 37;
        for (short s : shortArray) {
            expectedHashCode *= 37;
            expectedHashCode += s;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 123;
        builder.appendSuper(superHashCode);
        int expectedHashCode = 37 * 37 + superHashCode;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAppendNullObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] nullObjectArray = {null, null, null};
        builder.append(nullObjectArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendArrayOfDifferentType() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        char[] charArray = {'a', 'b', 'c'};
        builder.append(intArray);
        builder.append(charArray);
    }
}