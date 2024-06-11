package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.toHashCode());

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(11 * 31 + 17, hcbWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);

        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);

        assertEquals(37 * 2 + 1 + 0, hcb.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) -128);

        byte[] byteArray = {1, (byte) 128};
        hcb.append(byteArray);

        assertEquals(37 * 2 + 1 + (-128 + 128) + 1, hcb.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('z');

        char[] charArray = {'a', 'z'};
        hcb.append(charArray);

        assertEquals(37 * 2 + (int) 'a' + (int) 'z', hcb.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0);
        hcb.append(Math.PI);

        double[] doubleArray = {1.0, Math.PI};
        hcb.append(doubleArray);

        long doubleBits1 = Double.doubleToLongBits(1.0);
        long doubleBits2 = Double.doubleToLongBits(Math.PI);

        assertEquals(37 * 2 + (int) (doubleBits1 ^ doubleBits1 >> 32) + (int) (doubleBits2 ^ doubleBits2 >> 32), hcb.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 1.0);
        hcb.append((float) 2.5);

        float[] floatArray = {1.0f, 2.5f};
        hcb.append(floatArray);

        int floatToIntBits1 = Float.floatToIntBits(1.0f);
        int floatToIntBits2 = Float.floatToIntBits(2.5f);

        assertEquals(37 * 2 + (int) (floatToIntBits1 ^ floatToIntBits1 >> 32) + (int) (floatToIntBits2 ^ floatToIntBits2 >> 32), hcb.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(-1);

        int[] intArray = {1, -1};
        hcb.append(intArray);

        assertEquals(37 * 2 + 1 + (-1 + 1), hcb.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(-1L);

        long[] longArray = {1L, -1L};
        hcb.append(longArray);

        assertEquals(37 * 2 + (int) (1L ^ 1L >> 32) + (int) (-1L ^ -1L >> 32), hcb.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append("Test");

        Object[] objectArray = {"Test"};
        hcb.append(objectArray);

        assertEquals("Test".hashCode(), hcb.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(new Object[]{"String1", "String2"});

        String[] stringArray = {"String1", "String2"};
        hcb.append(stringArray);

        int hashCode = 0;
        for (String str : stringArray) {
            hashCode = 31 * hashCode + str.hashCode();
        }

        assertEquals(hashCode, hcb.toHashCode());
    }

    @Test
    public void testReflectionHashCodeBoolean() {
        Object obj = new Boolean(true);
        assertEquals(37 * 2 + 1, HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeByte() {
        Object obj = (byte) 1;
        assertEquals(37 * 2 + 1 + 1, HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeChar() {
        Object obj = 'a';
        assertEquals(37 * 2 + (int) 'a', HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeDouble() {
        Object obj = 1.0;
        long doubleBits = Double.doubleToLongBits(1.0);
        assertEquals(37 * 2 + (int) (doubleBits ^ doubleBits >> 32), HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeFloat() {
        Object obj = 1.0f;
        int floatToIntBits = Float.floatToIntBits(1.0f);
        assertEquals(37 * 2 + (int) (floatToIntBits ^ floatToIntBits >> 32), HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeInt() {
        Object obj = 1;
        assertEquals(37 * 2 + 1, HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeLong() {
        Object obj = 1L;
        long longBits = 1L ^ 1L >> 32;
        assertEquals(37 * 2 + (int) longBits, HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCodeObject() {
        Object obj = "Test";
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj));
    }
}