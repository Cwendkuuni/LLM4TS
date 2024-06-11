package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Assert.assertEquals(17, builder.toHashCode());
    }

    @Test
    public void testConstructorWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(11, 23);
        Assert.assertEquals(2907, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new HashCodeBuilder(0, 37);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(false);
        Assert.assertEquals(19, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        boolean[] booleanArray = {false, true};
        builder2.append(booleanArray);
        Assert.assertEquals(-1892529433, builder2.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        Assert.assertEquals(38, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        byte[] byteArray = {1, 2};
        builder2.append(byteArray);
        Assert.assertEquals(-1391113683, builder2.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        Assert.assertEquals(1066613969, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        char[] charArray = {'a', 'b'};
        builder2.append(charArray);
        Assert.assertEquals(-1578133821, builder2.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        Assert.assertEquals(1125871623235433, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        builder2.append(doubleArray);
        Assert.assertEquals(-1288292152333305376L, builder2.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        Assert.assertEquals(151165938, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        builder2.append(floatArray);
        Assert.assertEquals(-1755656227, builder2.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        Assert.assertEquals(39, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        int[] intArray = {1, 2};
        builder2.append(intArray);
        Assert.assertEquals(-1250234422, builder2.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        Assert.assertEquals(39, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        long[] longArray = {1L, 2L};
        builder2.append(longArray);
        Assert.assertEquals(-1331952533325761349L, builder2.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object());
        Assert.assertEquals(1812857882, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        builder2.append(objectArray);
        Assert.assertEquals(753120334, builder2.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new Object[0]);
        Assert.assertEquals(1, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        builder2.append(objectArray);
        Assert.assertEquals(753120334, builder2.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        Assert.assertEquals(41, builder.toHashCode());

        HashCodeBuilder builder2 = new HashCodeBuilder();
        short[] shortArray = {1, 2};
        builder2.append(shortArray);
        Assert.assertEquals(-1784806013, builder2.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Assert.assertEquals(17, builder.toHashCode());
        builder.appendSuper(5);
        Assert.assertEquals(19137, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectAppendNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(null, false, null, null, null, new String[0]);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The object to build a hash code for must not be null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testReflectAppendNonArrayObject() {
        Object object = new Object();
        Assert.assertEquals(1812857882, HashCodeBuilder.reflectionHashCode(17, 37, object, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayObject() {
        Object[] objectArray = {new Object(), new Object()};
        Assert.assertEquals(753120334, HashCodeBuilder.reflectionHashCode(17, 37, objectArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayLong() {
        long[] longArray = {1L, 2L};
        Assert.assertEquals(-1331952533325761349L, HashCodeBuilder.reflectionHashCode(17, 37, longArray, false, null, new String[0]).longValue());
    }

    @Test
    public void testReflectAppendArrayInt() {
        int[] intArray = {1, 2};
        Assert.assertEquals(-1250234422, HashCodeBuilder.reflectionHashCode(17, 37, intArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayShort() {
        short[] shortArray = {1, 2};
        Assert.assertEquals(-1784806013, HashCodeBuilder.reflectionHashCode(17, 37, shortArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayByte() {
        byte[] byteArray = {1, 2};
        Assert.assertEquals(-1391113683, HashCodeBuilder.reflectionHashCode(17, 37, byteArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayChar() {
        char[] charArray = {'a', 'b'};
        Assert.assertEquals(-1578133821, HashCodeBuilder.reflectionHashCode(17, 37, charArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayFloat() {
        float[] floatArray = {1.0f, 2.0f};
        Assert.assertEquals(-1755656227, HashCodeBuilder.reflectionHashCode(17, 37, floatArray, false, null, new String[0]).intValue());
    }

    @Test
    public void testReflectAppendArrayDouble() {
        double[] doubleArray = {1.0, 2.0};
        Assert.assertEquals(-1288292152333305376L, HashCodeBuilder.reflectionHashCode(17, 37, doubleArray, false, null, new String[0]).longValue());
    }

    @Test
    public void testReflectAppendArrayBoolean() {
        boolean[] booleanArray = {true, false};
        Assert.assertEquals(-1892529433, HashCodeBuilder.reflectionHashCode(17, 37, booleanArray, false, null, new String[0]).intValue());
    }
}