package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderInitialValue() {
        HashCodeBuilder builder = new HashCodeBuilder(11, 13);
        assertEquals(11, builder.hashCode() & 0xFFFFFF); // mask off higher bits
        assertEquals(13, (builder.hashCode() >> 32) & 0xFFFFFFFF); // mask off lower bits
    }

    @Test
    public void testHashCodeBuilderWithInitialValueAndMultiplier() {
        HashCodeBuilder builder = new HashCodeBuilder(19, 23);
        Object obj = new Object();
        builder.append(obj);
        assertEquals(19, builder.hashCode() & 0xFFFFFF);
        assertEquals(23, (builder.hashCode() >> 32) & 0xFFFFFFFF);
        assertEquals(obj.hashCode(), builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithObject() {
        Object obj = new Object();
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(obj);
        assertEquals(obj.hashCode(), builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithBoolean() {
        boolean bool = true;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(bool);
        assertEquals(bool ? 0 : 1, builder.hashCode() % 2);
    }

    @Test
    public void testHashCodeBuilderWithBooleanArray() {
        boolean[] bools = {true, false, true};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(bools);
        int expected = 1;
        for (boolean b : bools) {
            expected *= (b ? 0 : 1) * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithByte() {
        byte byteValue = 3;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(byteValue);
        assertEquals(byteValue, builder.hashCode() & 0xFF);
    }

    @Test
    public void testHashCodeBuilderWithByteArray() {
        byte[] bytes = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(bytes);
        int expected = 1;
        for (byte b : bytes) {
            expected *= (b + 1) * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithChar() {
        char charValue = 'a';
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(charValue);
        assertEquals((int)charValue, builder.hashCode() & 0xFFFF);
    }

    @Test
    public void testHashCodeBuilderWithCharArray() {
        char[] chars = {'a', 'b', 'c'};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(chars);
        int expected = 1;
        for (char c : chars) {
            expected *= (int)c * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithDouble() {
        double doubleValue = 3.14;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(doubleValue);
        long doubleBits = Double.doubleToLongBits(doubleValue);
        assertEquals(doubleBits, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithDoubleArray() {
        double[] doubles = {1.1, 2.2, 3.3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(doubles);
        long expected = 1;
        for (double d : doubles) {
            long dLongBits = Double.doubleToLongBits(d);
            expected *= dLongBits;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithFloat() {
        float floatValue = 3.14f;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(floatValue);
        assertEquals((int)Float.floatToIntBits(floatValue), builder.hashCode() & 0xFFFFFFFF);
    }

    @Test
    public void testHashCodeBuilderWithFloatArray() {
        float[] floats = {1.1f, 2.2f, 3.3f};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(floats);
        int expected = 1;
        for (float f : floats) {
            expected *= Float.floatToIntBits(f);
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithInt() {
        int intValue = 42;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(intValue);
        assertEquals(intValue, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithIntArray() {
        int[] ints = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(ints);
        int expected = 1;
        for (int i : ints) {
            expected *= (i + 1) * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithLong() {
        long longValue = 42L;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(longValue);
        assertEquals(longValue, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithLongArray() {
        long[] longs = {1L, 2L, 3L};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(longs);
        long expected = 1;
        for (long l : longs) {
            expected *= l * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithShort() {
        short shortValue = 42;
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(shortValue);
        assertEquals(shortValue, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithShortArray() {
        short[] shorts = {1, 2, 3};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(shorts);
        int expected = 1;
        for (short s : shorts) {
            expected *= (s + 1) * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithCustomClass() throws NoSuchFieldException, IllegalAccessException {
        class CustomClass {
            int value;

            public CustomClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        CustomClass customObject = new CustomClass(123);
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(customObject);
        Field valueField = CustomClass.class.getDeclaredField("value");
        valueField.setAccessible(true);
        int expected = 1;
        assertEquals(customObject.getValue(), valueField.getInt(customObject));
        expected *= (customObject.getValue() + 1) * 31;
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithArrayOfCustomClass() throws NoSuchFieldException, IllegalAccessException {
        class CustomClass {
            int value;

            public CustomClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        CustomClass[] customObjects = new CustomClass[3];
        for (int i = 0; i < 3; i++) {
            customObjects[i] = new CustomClass(i + 1);
        }
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(customObjects);
        long expected = 1;
        for (CustomClass customObject : customObjects) {
            expected *= customObject.getValue() * 31;
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithRegisteredClasses() throws NoSuchFieldException, IllegalAccessException {
        class CustomClass1 {
            int value;

            public CustomClass1(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        class CustomClass2 {
            int value;

            public CustomClass2(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        HashCodeBuilder builder = new HashCodeBuilder();
        Class<?> clazz1 = CustomClass1.class;
        Class<?> clazz2 = CustomClass2.class;
        builder.append(new CustomClass1(1));
        builder.append(new CustomClass2(2));
        builder.register();
        builder.append(new CustomClass1(3));
        builder.append(new CustomClass2(4));
        long expected = 1;
        for (int i = 1; i <= 4; i++) {
            expected *= HashCodeBuilder.getRegistry().get(new IDKey(i)).getValue().hashCode();
        }
        assertEquals(expected, builder.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithNullObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
    }

    @Test
    public void testHashCodeBuilderWithNullArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((Object[])null);
        assertEquals(0, builder.hashCode());
    }

    static class IDKey {
        final Object value;

        public IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}