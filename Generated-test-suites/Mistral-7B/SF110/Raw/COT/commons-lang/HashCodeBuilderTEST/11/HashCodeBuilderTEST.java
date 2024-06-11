package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Assert.assertEquals(17, hcb.iTotal);
        Assert.assertEquals(37, hcb.iConstant);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        Assert.assertEquals(11, hcbWithInitialAndMultiplier.iTotal);
        Assert.assertEquals(31, hcbWithInitialAndMultiplier.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 37);
        hcbWithInitialAndMultiplier.append(true);
        Assert.assertEquals(13 * 37 + 1, hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hcb.append(booleanArray);

        int expectedHashCode = 51; // 37 * 2 + 1 + 37 * 2 + 1 + 37 * 1 + 1
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 37);
        hcbWithInitialAndMultiplier.append(booleanArray);
        Assert.assertEquals(13 * 37 * booleanArray.length + 37 * (booleanArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append((byte) 5);
        Assert.assertEquals(11 * 31 + 5, hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {5, 10, 15};
        hcb.append(byteArray);

        int expectedHashCode = 138; // 37 * 3 + 10 + 37 * 2 + 15
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(byteArray);
        Assert.assertEquals(11 * 31 * byteArray.length + 37 * (byteArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 37);
        hcbWithInitialAndMultiplier.append('a');
        Assert.assertEquals(13 * 37 + (int) 'a', hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hcb.append(charArray);

        int expectedHashCode = 312; // 37 * 3 + 'a' + 37 * 2 + 'b' + 37 * 1 + 'c'
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(charArray);
        Assert.assertEquals(11 * 31 * charArray.length + 37 * (charArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double testDouble = 1.23;
        hcb.append(testDouble);
        long doubleBits = Double.doubleToLongBits(testDouble);
        Assert.assertEquals(37 * 2 + (int) (doubleBits >> 32) + (int) (doubleBits & 0xFFFFFFFF), hcb.iTotal);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        hcb.append(doubleArray);

        int expectedHashCode = 1259; // 37 * 3 + Double.doubleToLongBits(1.1) >> 32 + 37 * 2 + Double.doubleToLongBits(2.2) >> 32 + 37 * 1 + Double.doubleToLongBits(3.3) >> 32
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 31);
        hcbWithInitialAndMultiplier.append(doubleArray);
        Assert.assertEquals(13 * 31 * doubleArray.length + 37 * (doubleArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float testFloat = 1.23f;
        hcb.append(testFloat);
        Assert.assertEquals(37 * 2 + Float.floatToIntBits(testFloat), hcb.iTotal);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        hcb.append(floatArray);

        int expectedHashCode = 1241; // 37 * 3 + Float.floatToIntBits(1.1f) + 37 * 2 + Float.floatToIntBits(2.2f) + 37 * 1 + Float.floatToIntBits(3.3f)
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 31);
        hcbWithInitialAndMultiplier.append(floatArray);
        Assert.assertEquals(13 * 31 * floatArray.length + 37 * (floatArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(42);
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(42);
        Assert.assertEquals(11 * 31 + 42, hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {42, 43, 44};
        hcb.append(intArray);

        int expectedHashCode = 133; // 37 * 3 + 42 + 37 * 2 + 43 + 37 * 1 + 44
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(intArray);
        Assert.assertEquals(11 * 31 * intArray.length + 37 * (intArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long testLong = 1234567890123456789L;
        hcb.append(testLong);
        Assert.assertEquals(37 * 2 + (int)(testLong >> 32) + (int)(testLong & 0xFFFFFFFF), hcb.iTotal);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {123, 456, 789};
        hcb.append(longArray);

        int expectedHashCode = 1167; // 37 * 3 + (int)(123 >> 32) + (int)(123 & 0xFFFFFFFF) + 37 * 2 + (int)(456 >> 32) + (int)(456 & 0xFFFFFFFF) + 37 * 1 + (int)(789 >> 32) + (int)(789 & 0xFFFFFFFF)
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(13, 31);
        hcbWithInitialAndMultiplier.append(longArray);
        Assert.assertEquals(13 * 31 * longArray.length + 37 * (longArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String testString = "test";
        hcb.append(testString);
        Assert.assertEquals(37 * 2 + testString.hashCode(), hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(testString);
        Assert.assertEquals(11 * 31 + testString.hashCode(), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {"test1", "test2", "test3"};
        hcb.append(objectArray);

        int expectedHashCode = 1281; // 37 * 3 + "test1".hashCode() + 37 * 2 + "test2".hashCode() + 37 * 1 + "test3".hashCode()
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(objectArray);
        Assert.assertEquals(11 * 31 * objectArray.length + 37 * (objectArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 123);
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append((short) 123);
        Assert.assertEquals(11 * 31 + 123, hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {123, 124, 125};
        hcb.append(shortArray);

        int expectedHashCode = 131; // 37 * 3 + 123 + 37 * 2 + 124 + 37 * 1 + 125
        Assert.assertEquals(expectedHashCode, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.append(shortArray);
        Assert.assertEquals(11 * 31 * shortArray.length + 37 * (shortArray.length - 1), hcbWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(12345);
        Assert.assertEquals(38, hcb.iTotal);

        HashCodeBuilder hcbWithInitialAndMultiplier = new HashCodeBuilder(11, 31);
        hcbWithInitialAndMultiplier.appendSuper(12345);
        Assert.assertEquals(11 * 31 + 12345, hcbWithInitialAndMultiplier.iTotal);
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

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithZeroMultiplier() {
        new HashCodeBuilder(0, 31);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithZeroInitial() {
        new HashCodeBuilder(13, 0);
    }
}