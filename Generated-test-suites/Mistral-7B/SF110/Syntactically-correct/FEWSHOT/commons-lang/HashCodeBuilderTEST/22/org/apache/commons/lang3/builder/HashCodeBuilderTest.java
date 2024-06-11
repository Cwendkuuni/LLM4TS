package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb1 = new HashCodeBuilder();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        HashCodeBuilder hcb3 = new HashCodeBuilder(13, 31);
        assertEquals(hcb1.getClass(), hcb2.getClass());
        assertEquals(hcb1.getClass(), hcb3.getClass());
        assertNotSame(hcb1.hashCode(), hcb2.hashCode());
        assertNotSame(hcb1.hashCode(), hcb3.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(false);
        hcb.append(true);
        assertEquals(hcb.toHashCode(), 13 * 31 + Integer.SIGNUM);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] arr = {false, true, false};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) 127);
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + 1 + 127);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] arr = {1, 127};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('z');
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + Character.hashCode('a') + Character.hashCode('z'));
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] arr = {'a', 'z'};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23);
        assertEquals(hcb.toHashCode(), 13 * 31 + Double.doubleToLongBits(1.23));
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] arr = {1.23, 2.7182818284590451};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 1.23);
        hcb.append((float) 2.7182818f);
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + Float.floatToIntBits(1.23f) + Float.floatToIntBits(2.7182818f));
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] arr = {1.23f, 2.7182818f};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(123);
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + 1 + 123);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] arr = {1, 123};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(123L);
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + (int)(1L ^ 1L >> 32) + (int)123L);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] arr = {1L, 123L};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String str = "test";
        hcb.append(str);
        assertEquals(hcb.toHashCode(), 13 * 31 + str.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] arr = {1, "test", new Object()};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        hcb.append((short) 127);
        assertEquals(hcb.toHashCode(), 13 * 31 * 2 + 1 + 127);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] arr = {1, 127};
        hcb.append(arr);
        assertEquals(hcb.toHashCode(), 13 * 31 * arr.length + Arrays.hashCode(arr));
    }

    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(123);
        assertEquals(hcb.toHashCode(), 13 * 31 + 123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((Object[]) null);
    }
}