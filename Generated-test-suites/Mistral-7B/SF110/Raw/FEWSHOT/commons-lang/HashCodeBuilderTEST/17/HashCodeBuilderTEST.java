package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    // Test for default HashCodeBuilder
    @Test
    public void testDefaultHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        assertEquals(17, hashCode);
    }

    // Test for HashCodeBuilder with initial odd number and multiplier odd number
    @Test
    public void testHashCodeBuilderWithInitialAndMultiplierOddNumber() {
        HashCodeBuilder hcb = new HashCodeBuilder(31, 31);
        int hashCode = hcb.toHashCode();
        assertEquals(9601, hashCode);
    }

    // Test for append method with boolean
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int hashCode = hcb.toHashCode();
        assertEquals(32, hashCode);
    }

    // Test for append method with boolean array
    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 43 * 37 + 32 + 0;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with byte
    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 127);
        int hashCode = hcb.toHashCode();
        assertEquals(-268430409, hashCode);
    }

    // Test for append method with byte array
    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {(byte) 127, (byte) 126};
        hcb.append(byteArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 127 * 37 + 126 * 37 + 32;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with char
    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('A');
        int hashCode = hcb.toHashCode();
        assertEquals(65 * 37 + 32, hashCode);
    }

    // Test for append method with char array
    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'A', 'B'};
        hcb.append(charArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 65 * 37 + 66 * 37 + 32;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with double
    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double value = 1.23456789;
        hcb.append(value);
        int hashCode = hcb.toHashCode();
        long doubleBits = Double.doubleToLongBits(value);
        expectedHashCode = (int) (doubleBits ^ (doubleBits >>> 32)) * 37;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with double array
    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23456789, 2.34567890};
        hcb.append(doubleArray);
        int hashCode = hcb.toHashCode();
        for (double d : doubleArray) {
            long doubleBits = Double.doubleToLongBits(d);
            hashCode = hashCode * 37 + (int) (doubleBits ^ (doubleBits >>> 32));
        }
        assertEquals(hashCode, hcb.toHashCode());
    }

    // Test for append method with float
    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float value = 1.234567f;
        hcb.append(value);
        int hashCode = hcb.toHashCode();
        int floatBits = Float.floatToIntBits(value);
        expectedHashCode = (int) (floatBits * 37 + 32);
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with float array
    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.234567f, 2.345678f};
        hcb.append(floatArray);
        int hashCode = hcb.toHashCode();
        for (float f : floatArray) {
            int floatBits = Float.floatToIntBits(f);
            hashCode = hashCode * 37 + floatBits;
        }
        assertEquals(hashCode, hcb.toHashCode());
    }

    // Test for append method with int
    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(42);
        int hashCode = hcb.toHashCode();
        assertEquals(42 * 37 + 32, hashCode);
    }

    // Test for append method with int array
    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {42, 43};
        hcb.append(intArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 42 * 37 + 43 * 37 + 32;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with long
    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1234567890L);
        int hashCode = hcb.toHashCode();
        expectedHashCode = (int) (1234567890L ^ (1234567890L >>> 32)) * 37;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for append method with long array
    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1234567890L, 1234567891L};
        hcb.append(longArray);
        int hashCode = hcb.toHashCode();
        for (long l : longArray) {
            long longBits = Double.doubleToLongBits(l);
            hashCode = hashCode * 37 + (int) (longBits ^ (longBits >>> 32));
        }
        assertEquals(hashCode, hcb.toHashCode());
    }

    // Test for append method with Object
    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object object = new Object();
        hcb.append(object);
        int hashCode = hcb.toHashCode();
        assertEquals(object.hashCode(), hashCode);
    }

    // Test for append method with Object array
    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);
        int hashCode = hcb.toHashCode();
        for (Object o : objectArray) {
            hashCode = hashCode * 37 + o.hashCode();
        }
        assertEquals(hashCode, hcb.toHashCode());
    }

    // Test for append method with short
    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 32767);
        int hashCode = hcb.toHashCode();
        assertEquals(32767 * 37 + 32, hashCode);
    }

    // Test for append method with short array
    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {32767, 32768};
        hcb.append(shortArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 32767 * 37 + 32768 * 37 + 32;
        assertEquals(expectedHashCode, hashCode);
    }

    // Test for appendSuper method
    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 12345;
        hcb.appendSuper(superHashCode);
        int hashCode = hcb.toHashCode();
        assertEquals(superHashCode * 37 + hashCode, hcb.toHashCode());
    }
}