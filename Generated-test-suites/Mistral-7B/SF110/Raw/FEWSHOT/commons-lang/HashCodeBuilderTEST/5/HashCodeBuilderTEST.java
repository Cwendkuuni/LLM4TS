package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(1, 2);
        assertEquals(17, hcb.toHashCode());
        assertEquals(1 * 37 + 1, hcb.hashCode());
        assertEquals(1 * 37 + 1 * 37, hcbWithInitialAndMultiplier.toHashCode());
        assertEquals(1 * 37 + 1 * 37, hcbWithInitialAndMultiplier.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 1) + 0;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * 37 * booleanArray.length;
        for (boolean bool : booleanArray) {
            expectedHashCode = 37 * expectedHashCode + (bool ? 0 : 1);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 5);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hcb.append(byteArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 + 3;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 'a');
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hcb.append(charArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * charArray.length + 3 * 37;
        for (char c : charArray) {
            expectedHashCode = 37 * expectedHashCode + c;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23);
        int hashCode = hcb.toHashCode();
        long doubleToLongBits = Double.doubleToLongBits(1.23);
        int expectedHashCode = 37 * (37 * 1 + (int) (doubleToLongBits ^ doubleToLongBits >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.34, 3.45};
        hcb.append(doubleArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * doubleArray.length + 3 * 37 * 37;
        for (double d : doubleArray) {
            long doubleToLongBits = Double.doubleToLongBits(d);
            expectedHashCode = 37 * expectedHashCode + (int) (doubleToLongBits ^ doubleToLongBits >>> 32);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23f);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + Float.floatToIntBits(1.23f));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.34f, 3.45f};
        hcb.append(floatArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * floatArray.length + 3 * 37 * 37;
        for (float f : floatArray) {
            expectedHashCode = 37 * expectedHashCode + Float.floatToIntBits(f);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 1);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hcb.append(intArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * intArray.length + 3 * 37 * 37;
        for (int i : intArray) {
            expectedHashCode = 37 * expectedHashCode + i;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + (int) (1L ^ 1L >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1L, 2L, 3L};
        hcb.append(longArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * longArray.length + 3 * 37 * 37;
        for (long l : longArray) {
            expectedHashCode = 37 * expectedHashCode + (int) (l ^ l >>> 32);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object();
        hcb.append(obj);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + obj.hashCode());
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * objectArray.length + 3 * 37 * 37;
        for (Object obj : objectArray) {
            expectedHashCode = 37 * expectedHashCode + obj.hashCode();
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 5);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 5);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hcb.append(shortArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1 * 37 * 37 + 2 * 37 * shortArray.length + 3 * 37 * 37;
        for (short s : shortArray) {
            expectedHashCode = 37 * expectedHashCode + s;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendSuperInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(1);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 37 * (37 * 1 + 1);
        assertEquals(expectedHashCode, hashCode);
    }
}