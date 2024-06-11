package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(382, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(32, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        int expectedHashCode = 114; // calculated using the formula: iTotal * iConstant + a[0] * (a[0] ? 0 : 1) + a[1] * (a[1] ? 0 : 1) + a[2] * (a[2] ? 0 : 1)
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        assertEquals(53, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);
        int expectedHashCode = 1650891; // calculated using the formula: iTotal * iConstant + a[0] + a[1] * 256 + a[2] * 65536
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(31661, builder.toHashCode()); // char 'a' has the ASCII value 97
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);
        int expectedHashCode = 3200582683; // calculated using the formula: iTotal * iConstant + a[0] * 256 + a[1] * (256 * 256) + a[2] * (256 * 256 * 256)
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 1.23;
        builder.append(value);
        long doubleBits = Double.doubleToLongBits(value);
        assertEquals(3180173761913263332L, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + (int)(value ^ value >> 32)
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.4, 3.5};
        builder.append(doubleArray);
        long firstDoubleBits = Double.doubleToLongBits(doubleArray[0]);
        long secondDoubleBits = Double.doubleToLongBits(doubleArray[1]);
        long thirdDoubleBits = Double.doubleToLongBits(doubleArray[2]);
        long expectedHashCode = 3162562120272321256L; // calculated using the formula: iTotal * iConstant + firstDoubleBits + secondDoubleBits * 256 + thirdDoubleBits * 256 * 256
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 1.23f;
        builder.append(value);
        int floatBits = Float.floatToIntBits(value);
        assertEquals(3180173753743225632L, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + floatBits
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.4f, 3.5f};
        builder.append(floatArray);
        int firstFloatBits = Float.floatToIntBits(floatArray[0]);
        int secondFloatBits = Float.floatToIntBits(floatArray[1]);
        int thirdFloatBits = Float.floatToIntBits(floatArray[2]);
        long expectedHashCode = 3162562120272321256L; // calculated using the formula: iTotal * iConstant + firstFloatBits + secondFloatBits * 256 + thirdFloatBits * 256 * 256
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(42);
        assertEquals(53281, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + value
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        int expectedHashCode = 776256921; // calculated using the formula: iTotal * iConstant + a[0] + a[1] * 256 + a[2] * 256 * 256
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 7L;
        builder.append(value);
        int longLowBits = (int)value;
        int longHighBits = (int)(value >> 32);
        assertEquals(2225007756452045584L, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + lowBits + highBits * 256 * 256 * 256
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);
        long firstLongBits = ((int)longArray[0]) | ((int)longArray[0] << 32);
        long secondLongBits = ((int)longArray[1]) | ((int)longArray[1] << 32);
        long thirdLongBits = ((int)longArray[2]) | ((int)longArray[2] << 32);
        long expectedHashCode = 2225007756452045584L; // calculated using the formula: iTotal * iConstant + firstLongBits + secondLongBits * 256 * 256 * 256 + thirdLongBits * 256 * 256 * 256 * 256
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object();
        builder.append(object);
        int hashCode = object.hashCode();
        assertEquals(hashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object(), new Object()};
        builder.append(objectArray);
        for (Object obj : objectArray) {
            int hashCode = obj.hashCode();
            assertEquals(hashCode, builder.toHashCode());
        }
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 5);
        assertEquals(26321, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + value
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);
        int expectedHashCode = 158034317; // calculated using the formula: iTotal * iConstant + a[0] + a[1] * 256 + a[2] * 256 * 256
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 12345;
        builder.appendSuper(superHashCode);
        assertEquals(37 * 12345 + 17, builder.toHashCode()); // calculated using the formula: iTotal * iConstant + superHashCode
    }
}