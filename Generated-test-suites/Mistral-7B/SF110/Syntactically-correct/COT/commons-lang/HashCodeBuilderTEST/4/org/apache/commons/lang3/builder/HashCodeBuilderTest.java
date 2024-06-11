package org.junit.tests;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderConstructor() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Assert.assertEquals(17, hashCodeBuilder.iTotal);
        Assert.assertEquals(37, hashCodeBuilder.iConstant);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(11, 13);
        Assert.assertEquals(11, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
        Assert.assertEquals(13, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        Assert.assertEquals(32, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(5, 11);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(true);
        Assert.assertEquals(3653, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hashCodeBuilder.append(booleanArray);
        Assert.assertEquals(369343695, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(7, 19);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(booleanArray);
        Assert.assertEquals(-1355443175, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 5);
        Assert.assertEquals(85, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(15, 31);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append((byte) 5);
        Assert.assertEquals(316670773, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        Assert.assertEquals(57228755, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(3, 13);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(byteArray);
        Assert.assertEquals(253458451, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        Assert.assertEquals(31891, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(9, 17);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append('a');
        Assert.assertEquals(129761, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        Assert.assertEquals(183084071, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(11, 19);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(charArray);
        Assert.assertEquals(-1014362277, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.0);
        Assert.assertEquals(1415057145, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(5, 13);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(1.0);
        Assert.assertEquals(121828363, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0, 3.0};
        hashCodeBuilder.append(doubleArray);
        Assert.assertEquals(-1556191965, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(7, 17);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(doubleArray);
        Assert.assertEquals(-470672026, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.0f);
        Assert.assertEquals(1351085393, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(3, 11);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(1.0f);
        Assert.assertEquals(-1353672147, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f, 3.0f};
        hashCodeBuilder.append(floatArray);
        Assert.assertEquals(1005237857, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(11, 19);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(floatArray);
        Assert.assertEquals(1308015291, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(10);
        Assert.assertEquals(33861, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(15, 31);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(10);
        Assert.assertEquals(1114681317, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        Assert.assertEquals(-1032602221, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(7, 13);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(intArray);
        Assert.assertEquals(1504136883, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(10L);
        Assert.assertEquals(332627395, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(15, 31);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(10L);
        Assert.assertEquals(1009070519, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);
        Assert.assertEquals(134315527, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(7, 13);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(longArray);
        Assert.assertEquals(1325838119, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object obj = new Object();
        hashCodeBuilder.append(obj);
        Assert.assertTrue(hashCodeBuilder.iTotal > 0);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(5, 11);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(obj);
        Assert.assertTrue(hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal > 0);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object(), new Object()};
        hashCodeBuilder.append(objectArray);
        Assert.assertTrue(hashCodeBuilder.iTotal > 0);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(3, 11);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(objectArray);
        Assert.assertTrue(hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal > 0);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 10);
        Assert.assertEquals(30645, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(15, 31);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append((short) 10);
        Assert.assertEquals(780789217, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        Assert.assertEquals(-1314753827, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(7, 13);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.append(shortArray);
        Assert.assertEquals(1533957963, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendSuperInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(10);
        Assert.assertEquals(33657, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber = new HashCodeBuilder(5, 11);
        hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.appendSuper(10);
        Assert.assertEquals(245855711, hashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber.iTotal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNull() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                HashCodeBuilder.reflectionHashCode(null, false, null, null, null, new String[0])
        );
    }
}