package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Assert.assertEquals(17, builder.hashCode());
    }

    @Test
    public void testConstructorWithInitialValueAndMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(11, 31);
        builder.append(true);
        builder.append(1);
        Assert.assertEquals(11 * 31 + 1 + 1, builder.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialValue() {
        new HashCodeBuilder(0, 31);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMultiplier() {
        new HashCodeBuilder(1, 0);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        Assert.assertEquals(37 * builder.hashCode() + 1, builder.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] array = new boolean[]{true, false, true};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length) + Arrays.hashCode(array);
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        Assert.assertEquals(37 * builder.hashCode() + 1, builder.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] array = new byte[]{1, 2, 3};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length) + Arrays.hashCode(array);
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        Assert.assertEquals(37 * builder.hashCode() + 'a', builder.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] array = new char[]{'a', 'b', 'c'};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length) + Arrays.hashCode(array);
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        long doubleBits = Double.doubleToLongBits(1.0);
        Assert.assertEquals(37 * builder.hashCode() + Double.hashCode(1.0), builder.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] array = new double[]{1.0, 2.0, 3.0};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (double d : array) {
            long doubleBits = Double.doubleToLongBits(d);
            expectedHashCode += 37 * (37 * expectedHashCode + Double.hashCode(d));
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float) 1.0);
        int floatBits = Float.floatToIntBits(1.0f);
        Assert.assertEquals(37 * builder.hashCode() + floatBits, builder.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] array = new float[]{1.0f, 2.0f, 3.0f};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (float f : array) {
            int floatBits = Float.floatToIntBits(f);
            expectedHashCode += 37 * (37 * expectedHashCode + floatBits);
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        Assert.assertEquals(37 * builder.hashCode() + 1, builder.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] array = new int[]{1, 2, 3};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (int i : array) {
            expectedHashCode += 37 * (37 * expectedHashCode + i);
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        Assert.assertEquals(37 * builder.hashCode() + (int)(1L ^ (1L >> 32)), builder.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] array = new long[]{1, 2, 3};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (long l : array) {
            expectedHashCode += 37 * (37 * expectedHashCode + (int)(l ^ (l >> 32)));
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String obj = "test";
        builder.append(obj);
        Assert.assertEquals(37 * builder.hashCode() + obj.hashCode(), builder.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] array = new Object[]{"test1", "test2"};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (Object o : array) {
            expectedHashCode += 37 * (37 * expectedHashCode + o.hashCode());
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        Assert.assertEquals(37 * builder.hashCode() + 1, builder.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] array = new short[]{1, 2, 3};
        builder.append(array);
        int expectedHashCode = 37 * (37 * builder.hashCode() + array.length);
        for (short s : array) {
            expectedHashCode += 37 * (37 * expectedHashCode + s);
        }
        Assert.assertEquals(expectedHashCode, builder.hashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(12345);
        Assert.assertEquals(37 * builder.hashCode() + 12345, builder.hashCode());
    }

    @Test
    public void testReflexionHashCode() {
        Object object = new Object(){};
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, false);
        Assert.assertEquals(expectedHashCode, HashCodeBuilder.reflectionHashCode(object, false));
    }

    @Test
    public void testReflexionHashCodeWithExcludeFields() {
        Object object = new Object(){
            int excludedField = 1;
        };
        int[] excludedFields = new int[]{3};
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, ReflectionToStringBuilder.class, false, excludedFields);
        Assert.assertEquals(expectedHashCode, HashCodeBuilder.reflectionHashCode(object, ReflectionToStringBuilder.class, false, excludedFields));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflexionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflexionHashCodeWithExcludeClass() {
        Object object = new Object(){};
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, String.class, false, new String[0]);
        Assert.assertEquals(expectedHashCode, HashCodeBuilder.reflectionHashCode(object, String.class, false, new String[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflexionHashCodeWithNullExcludeFields() {
        Object object = new Object(){};
        HashCodeBuilder.reflectionHashCode(object, ReflectionToStringBuilder.class, false, null);
    }

    @Test(expected = InternalError.class)
    public void testReflexionHashCodeWithIllegalAccessException() {
        Object object = new Object(){
            private int privateField = 1;
        };
        HashCodeBuilder.reflectionHashCode(object, Object.class, new HashCodeBuilder(17, 37), new String[0]);
    }
}

class ReflectionToStringBuilder {
    public static String[] toNoNullStringArray(Collection<?> collection) {
        return collection == null ? null : collection.toArray(new String[collection.size()]);
    }
}

class IDKey {
    private final Object value;

    public IDKey(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IDKey && ((IDKey) obj).value.equals(this.value);
    }
}