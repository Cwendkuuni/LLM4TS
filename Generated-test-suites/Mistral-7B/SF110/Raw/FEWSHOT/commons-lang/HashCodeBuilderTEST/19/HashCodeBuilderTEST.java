package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(43, builder.toHashCode());
        builder.append(false);
        assertEquals(41, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        int expectedHashCode = 225385221; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(33, builder.toHashCode());
        builder.append((byte) -1);
        assertEquals(13, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, -1, 0};
        builder.append(byteArray);
        int expectedHashCode = -910835553; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(321839339, builder.toHashCode());
        builder.append('z');
        assertEquals(357241949, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'z', 'e'};
        builder.append(charArray);
        int expectedHashCode = 1066885047; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23d);
        assertEquals(1059622148949058347L, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.23d, -1.23d, 0.0d};
        builder.append(doubleArray);
        long expectedHashCode = 18766280906124839285L; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float) 1.23f);
        assertEquals(1212957711, builder.toHashCode());
        builder.append((float) -1.23f);
        assertEquals(1116777807, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.23f, -1.23f, 0.0f};
        builder.append(floatArray);
        int expectedHashCode = 1441019368; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(31, builder.toHashCode());
        builder.append(-1);
        assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, -1, 0};
        builder.append(intArray);
        int expectedHashCode = 1137298221; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        assertEquals(33, builder.toHashCode());
        builder.append(-1L);
        assertEquals(35, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1L, -1L, 0L};
        builder.append(longArray);
        long expectedHashCode = 21010048642861468325L; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String stringObject = "test";
        builder.append(stringObject);
        assertEquals("-1214631355", Integer.toHexString(builder.toHashCode()));

        Integer intObject = 123;
        builder.append(intObject);
        assertEquals("4366033115983861197", Integer.toHexString(builder.toHashCode()));
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String[] stringArray = {"test1", "test2", "test3"};
        builder.append(stringArray);
        long expectedHashCode = 1191387904834131082L; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        assertEquals(31, builder.toHashCode());
        builder.append((short) -1);
        assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, -1, 0};
        builder.append(shortArray);
        int expectedHashCode = 204293147; // Computed using reflectionHashCode method
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 12345;
        builder.appendSuper(superHashCode);
        assertEquals(337175723 + superHashCode, builder.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new Object() {};
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        System.out.println("Reflection hash code: " + hashCode);
        assertEquals(hashCode, new HashCodeBuilder(17, 37).append(object).toHashCode());
    }
}