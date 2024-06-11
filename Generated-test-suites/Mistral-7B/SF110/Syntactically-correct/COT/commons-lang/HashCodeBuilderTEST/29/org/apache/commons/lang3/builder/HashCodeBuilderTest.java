package org.junit.tests;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.IllegalAccessException;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Assert.assertEquals(17, hashCodeBuilder.iTotal);
        Assert.assertEquals(37, hashCodeBuilder.iConstant);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        Assert.assertEquals(5, hashCodeBuilderWithInitialAndMultiplier.iTotal);
        Assert.assertEquals(7, hashCodeBuilderWithInitialAndMultiplier.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        Assert.assertEquals(38, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(true);
        Assert.assertEquals(5 * 37 + 1, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hashCodeBuilder.append(booleanArray);
        Assert.assertEquals(1 + 37 * booleanArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(booleanArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * booleanArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 1);
        Assert.assertEquals(39, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append((byte) 1);
        Assert.assertEquals(5 * 37 + (1 & 0xFF), hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        Assert.assertEquals(1 + 37 * byteArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(byteArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * byteArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        Assert.assertEquals(53, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append('a');
        Assert.assertEquals(5 * 37 + (int) 'a', hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        Assert.assertEquals(1 + 37 * charArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(charArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * charArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double doubleValue = 1.23;
        hashCodeBuilder.append(doubleValue);
        long doubleAsLong = Double.doubleToLongBits(doubleValue);
        Assert.assertEquals(37 * 8 + (int) (doubleAsLong ^ (doubleAsLong >> 32)), hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(doubleValue);
        Assert.assertEquals(5 * 37 * 8 + (int) (doubleAsLong ^ (doubleAsLong >> 32)), hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.34, 3.45};
        hashCodeBuilder.append(doubleArray);
        Assert.assertEquals(1 + 37 * doubleArray.length * 8, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(doubleArray);
        Assert.assertEquals(5 * 37 * 8 + 1 + 37 * doubleArray.length * 8, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float floatValue = 1.23f;
        hashCodeBuilder.append(floatValue);
        Assert.assertEquals(37 * 4 + Float.floatToIntBits(floatValue), hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(floatValue);
        Assert.assertEquals(5 * 37 * 4 + Float.floatToIntBits(floatValue), hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.34f, 3.45f};
        hashCodeBuilder.append(floatArray);
        Assert.assertEquals(1 + 37 * floatArray.length * 4, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(floatArray);
        Assert.assertEquals(5 * 37 * 4 + 1 + 37 * floatArray.length * 4, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(42);
        Assert.assertEquals(43, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(42);
        Assert.assertEquals(5 * 37 + 42, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {42, 43, 44};
        hashCodeBuilder.append(intArray);
        Assert.assertEquals(1 + 37 * intArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(intArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * intArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long longValue = 42L;
        hashCodeBuilder.append(longValue);
        Assert.assertEquals(45, hashCodeBuilder.iTotal);
        Assert.assertEquals(longValue, hashCodeBuilder.iTotal ^ (hashCodeBuilder.iTotal >> 32));

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(longValue);
        Assert.assertEquals(5 * 37 + 45, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {42L, 43L, 44L};
        hashCodeBuilder.append(longArray);
        Assert.assertEquals(1 + 37 * longArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(longArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * longArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object object = new Object();
        hashCodeBuilder.append(object);
        Assert.assertTrue(hashCodeBuilder.iTotal > 0);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(object);
        Assert.assertEquals(5 * 37 + object.hashCode(), hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object(), new Object()};
        hashCodeBuilder.append(objectArray);
        Assert.assertEquals(1 + 37 * objectArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(objectArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * objectArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 42);
        Assert.assertEquals(45, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append((short) 42);
        Assert.assertEquals(5 * 37 + 45, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {(short) 42, (short) 43, (short) 44};
        hashCodeBuilder.append(shortArray);
        Assert.assertEquals(1 + 37 * shortArray.length, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.append(shortArray);
        Assert.assertEquals(5 * 37 + 1 + 37 * shortArray.length, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testAppendSuper() {
        int superHashCode = 100;
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(superHashCode);
        Assert.assertEquals(superHashCode, hashCodeBuilder.iTotal);

        HashCodeBuilder hashCodeBuilderWithInitialAndMultiplier = new HashCodeBuilder(5, 7);
        hashCodeBuilderWithInitialAndMultiplier.appendSuper(superHashCode);
        Assert.assertEquals(5 * 37 + superHashCode, hashCodeBuilderWithInitialAndMultiplier.iTotal);
    }

    @Test
    public void testIsRegistered() {
        Object testObject = new Object();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Assert.assertFalse(HashCodeBuilder.isRegistered(testObject));

        hashCodeBuilder.append(testObject);
        Assert.assertTrue(HashCodeBuilder.isRegistered(testObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullExcludeFields() {
        HashCodeBuilder.reflectionHashCode(new Object(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullReflectUpToClass() {
        HashCodeBuilder.reflectionHashCode(new Object(), true, null, new String[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionHashCodeWithNullObjectToBuildHashCodeFor() {
        HashCodeBuilder.reflectionHashCode(null);
    }

    @Test
    public void testReflectionHashCode() {
        Object testObject = new Object();
        Class<?> testObjectClass = testObject.getClass();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(17, 37, testObject);
        Assert.assertEquals(reflectionHashCode, HashCodeBuilder.reflectionHashCode(17, 37, testObject, true, testObjectClass.getSuperclass(), new String[0]));
    }
}