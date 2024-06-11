package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.HashSet;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Assert.assertEquals(17, builder.toHashCode());
    }

    @Test
    public void testConstructorWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 5);
        Assert.assertEquals(13, builder.iConstant);
        Assert.assertEquals(5, builder.iTotal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForOddInitialValue() {
        new HashCodeBuilder(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForOddMultiplier() {
        new HashCodeBuilder(1, 0);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        Assert.assertEquals(32, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        int expected = 133; // (32 * 3 + 1) * 3 + 1
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        Assert.assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);
        int expected = 32 * 3 + 1 + 512; // (33 * 3 + 1) + (256 * byteArray.length)
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        Assert.assertEquals(33 * 3 + 97, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);
        int expected = 3 * (3 * 3 + 97) + 3 * (3 * 3 + 98) + 3 * (3 * 3 + 99);
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14;
        builder.append(value);
        long doubleInBits = Double.doubleToLongBits(value);
        Assert.assertEquals(3 * 3 + Double.hashCode(value), builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);
        int expected = 3 * (3 * 3 + Double.hashCode(doubleArray[0]))
                + 3 * (3 * 3 + Double.hashCode(doubleArray[1]))
                + 3 * (3 * 3 + Double.hashCode(doubleArray[2]));
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 3.14f;
        builder.append(value);
        int floatInBits = Float.floatToIntBits(value);
        Assert.assertEquals(3 * 3 + Float.hashCode(value), builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);
        int expected = 3 * (3 * 3 + Float.hashCode(floatArray[0]))
                + 3 * (3 * 3 + Float.hashCode(floatArray[1]))
                + 3 * (3 * 3 + Float.hashCode(floatArray[2]));
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        Assert.assertEquals(34, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        int expected = 3 * (3 * 3 + 1) + 3 * (3 * 3 + 2) + 3 * (3 * 3 + 3);
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        Assert.assertEquals(35, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);
        int expected = 3 * (3 * 3 + 1) + 3 * (3 * 3 + 2) + 3 * (3 * 3 + 3);
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String object = "test";
        builder.append(object);
        Assert.assertEquals(3 * 3 + object.hashCode(), builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {"test1", "test2", "test3"};
        builder.append(objectArray);
        int expected = 3 * (3 * 3 + "test1".hashCode())
                + 3 * (3 * 3 + "test2".hashCode())
                + 3 * (3 * 3 + "test3".hashCode());
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        Assert.assertEquals(35, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);
        int expected = 3 * (3 * 3 + 1) + 3 * (3 * 3 + 2) + 3 * (3 * 3 + 3);
        Assert.assertEquals(expected, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 42;
        builder.appendSuper(superHashCode);
        Assert.assertEquals(3 * 3 + superHashCode, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNullObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
    }

    @Test
    public void testReflectionHashCodeWithObject() {
        Object object = new Object() {
            int field1 = 1;
            int field2 = 2;
        };
        int result = HashCodeBuilder.reflectionHashCode(object);
        int expected = 3 * (3 * 3 + 1) + 3 * (3 * 3 + 2) + 3 * 3;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReflectionHashCodeWithCollectionExcludeFields() {
        List<String> list = Arrays.asList("test1", "test2", "test3");
        int result = HashCodeBuilder.reflectionHashCode(list, "field1");
        int expected = 3 * 3 + 3 * hashCode("test1") + 3 * hashCode("test2") + 3 * hashCode("test3");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReflectionHashCodeWithStringArrayExcludeFields() {
        String[] strings = {"test1", "test2", "test3"};
        int result = HashCodeBuilder.reflectionHashCode(strings, "field1");
        int expected = 3 * 3 + 3 * hashCode("test1") + 3 * hashCode("test2") + 3 * hashCode("test3");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNullExcludeFields() {
        HashCodeBuilder.reflectionHashCode(new Object(), null);
    }

    private static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }
}