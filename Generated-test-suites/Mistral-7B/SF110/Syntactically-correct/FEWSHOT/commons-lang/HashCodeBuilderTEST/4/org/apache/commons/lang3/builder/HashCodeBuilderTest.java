package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        assertEquals(33, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append(false);
        assertEquals(31, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(11, 17);
        hashCodeBuilder2.append(true);
        assertEquals(1821, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(11, 17).append(false);
        assertEquals(1812, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hashCodeBuilder.append(booleanArray);
        assertEquals(-1759478119, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 0);
        assertEquals(1, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append((byte) 5);
        assertEquals(27, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(13, 31);
        hashCodeBuilder2.append((byte) 3);
        assertEquals(1181119581, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(13, 31).append((byte) -5);
        assertEquals(1181118633, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        assertEquals(1903926163, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        assertEquals(31917, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append('z');
        assertEquals(-1394145455, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(31, 13);
        hashCodeBuilder2.append(' ');
        assertEquals(-1227039613, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(31, 13).append('\u0001');
        assertEquals(1033682423, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        assertEquals(354032635, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1.23);
        assertEquals(1823539583, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append(Double.POSITIVE_INFINITY);
        assertEquals(7605557561660636355L, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(19, 11);
        hashCodeBuilder2.append(Double.NaN);
        assertEquals(1267650600921873425L, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(19, 11).append(Double.NEGATIVE_INFINITY);
        assertEquals(-1509217876327528941L, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.46, 3.7};
        hashCodeBuilder.append(doubleArray);
        assertEquals(-1618533465367373108L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((float) 1.23);
        assertEquals(1732071235, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append(Float.POSITIVE_INFINITY);
        assertEquals(1714118809265145664L, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(19, 37);
        hashCodeBuilder2.append(Float.NaN);
        assertEquals(11258999068425978832L, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(19, 37).append(Float.NEGATIVE_INFINITY);
        assertEquals(-1457172418321843317L, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.46f, 3.7f};
        hashCodeBuilder.append(floatArray);
        assertEquals(-1059028263417509445L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        assertEquals(31, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append(Integer.MIN_VALUE);
        assertEquals(-2147483648, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(13, 31);
        hashCodeBuilder2.append(42);
        assertEquals(-1467432027, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(13, 31).append(-42);
        assertEquals(1467432028, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        assertEquals(-1464310058166612245L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        assertEquals(1, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append(Long.MIN_VALUE);
        assertEquals(-9223372036854775808L, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(13, 31);
        hashCodeBuilder2.append(42L);
        assertEquals(1205837903234572535L, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(13, 31).append(-42L);
        assertEquals(-1205837903234572536L, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);
        assertEquals(-754691315360548814L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String string = "test";
        hashCodeBuilder.append(string);
        assertEquals(1026294595, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        Integer integer = 1;
        hashCodeBuilder1.append(integer);
        assertEquals(-1610612745, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(13, 31);
        hashCodeBuilder2.append("test string");
        assertEquals(1197124551780766903L, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(13, 31).append(new Object());
        assertEquals(1246129669, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hashCodeBuilder.append(objectArray);
        assertEquals(-1537636402228078562L, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 0);
        assertEquals(1, hashCodeBuilder.toHashCode());

        HashCodeBuilder hashCodeBuilder1 = new HashCodeBuilder();
        hashCodeBuilder1.append((short) 5);
        assertEquals(29, hashCodeBuilder1.toHashCode());

        HashCodeBuilder hashCodeBuilder2 = new HashCodeBuilder(13, 31);
        hashCodeBuilder2.append((short) 3);
        assertEquals(1201018889, hashCodeBuilder2.toHashCode());

        HashCodeBuilder hashCodeBuilder3 = new HashCodeBuilder(13, 31).append((short) -5);
        assertEquals(1201018743, hashCodeBuilder3.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        assertEquals(1466745848, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(42);
        assertEquals(43, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new Object() {
            int i = 42;
            String name = "test";
        };
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(object);
        assertEquals(1106111063, reflectionHashCode);

        Class<? extends Object> clazz = object.getClass();
        int reflectionHashCode1 = HashCodeBuilder.reflectionHashCode(object, clazz);
        assertEquals(1106111063, reflectionHashCode1);

        int reflectionHashCode2 = HashCodeBuilder.reflectionHashCode(object, clazz, true);
        assertEquals(457808661, reflectionHashCode2);

        int reflectionHashCode3 = HashCodeBuilder.reflectionHashCode(object, clazz, true, Object.class, new String[]{"i", "name"});
        assertEquals(146607676, reflectionHashCode3);
    }
}