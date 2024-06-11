package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expected = 1;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = {true, false, true};
        builder.append(array);
        int expected = 1 * 37 + 0 * 37 + 1 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int expected = 1;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = {1, 2, 3};
        builder.append(array);
        int expected = 1 * 37 + 2 * 37 + 3 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int expected = 'a' * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = {'a', 'b', 'c'};
        builder.append(array);
        int expected = 'a' * 37 + 'b' * 37 + 'c' * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14;
        builder.append(value);
        long doubleValue = Double.doubleToLongBits(value);
        int expected = (int) (doubleValue ^ doubleValue >> 32);
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = {1.1, 2.2, 3.3};
        builder.append(array);
        int expected = ((int) (Double.doubleToLongBits(array[0]) ^ Double.doubleToLongBits(array[0]) >> 32)) * 37 +
                       ((int) (Double.doubleToLongBits(array[1]) ^ Double.doubleToLongBits(array[1]) >> 32)) * 37 +
                       ((int) (Double.doubleToLongBits(array[2]) ^ Double.doubleToLongBits(array[2]) >> 32));
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 3.14f;
        builder.append(value);
        int expected = Float.floatToIntBits(value);
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = {1.1f, 2.2f, 3.3f};
        builder.append(array);
        int expected = Float.floatToIntBits(array[0]) * 37 +
                       Float.floatToIntBits(array[1]) * 37 +
                       Float.floatToIntBits(array[2]) * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int value = 1;
        builder.append(value);
        int expected = 1 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = {1, 2, 3};
        builder.append(array);
        int expected = 1 * 37 + 2 * 37 + 3 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 1;
        builder.append(value);
        int expected = (int) (value ^ value >> 32);
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = {1, 2, 3};
        builder.append(array);
        int expected = (int) array[0] ^ (int) (array[0] >>> 32) * 37 +
                       (int) array[1] ^ (int) (array[1] >>> 32) * 37 +
                       (int) array[2] ^ (int) (array[2] >>> 32) * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object obj = new Object();
        builder.append(obj);
        int expected = obj.hashCode();
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = {new Object(), new Object(), new Object()};
        builder.append(array);
        int expected = Arrays.hashCode(array);
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short value = 1;
        builder.append(value);
        int expected = value * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = {1, 2, 3};
        builder.append(array);
        int expected = 1 * 37 + 2 * 37 + 3 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 123;
        builder.appendSuper(superHashCode);
        int expected = 123 * 37;
        int actual = builder.toHashCode();
        assertEquals(expected, actual);
    }
}