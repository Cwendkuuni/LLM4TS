package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());
        assertEquals(17, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testHashCodeBuilderWithInitialValue() {
        HashCodeBuilder builder = new HashCodeBuilder(11, 31);
        assertEquals(11 * 31 + 17, builder.toHashCode());
        assertEquals(11 * 31 + 17, reflectionHashCode(builder.build(), 11, 31, false, null, new String[0]));
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(32, builder.toHashCode());
        assertEquals(32, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        int expectedHashCode = 37 * 3 + 1; // hashCode of boolean array
        for (boolean b : booleanArray) {
            expectedHashCode = expectedHashCode * 31 + (b ? 0 : 1);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        assertEquals(33, builder.toHashCode());
        assertEquals(33, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);
        int expectedHashCode = 37 * 3 + byteArray[0];
        for (int i = 1; i < byteArray.length; i++) {
            expectedHashCode = expectedHashCode * 31 + byteArray[i];
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        assertEquals(32 * 31 + 97, builder.toHashCode());
        assertEquals(32 * 31 + 97, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);
        int expectedHashCode = 37 * 3 + 97; // hashCode of char array
        for (char c : charArray) {
            expectedHashCode = expectedHashCode * 31 + c;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        long doubleToLongBits = Double.doubleToLongBits(1.0);
        assertEquals(37 * 31 + Double.hashCode(1.0), builder.toHashCode());
        assertEquals(37 * 31 + Double.hashCode(1.0), reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0, 3.0};
        builder.append(doubleArray);
        int expectedHashCode = 37 * 3;
        for (double d : doubleArray) {
            long doubleToLongBits = Double.doubleToLongBits(d);
            expectedHashCode = expectedHashCode * 31 + Double.hashCode(d);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        int floatToIntBits = Float.floatToIntBits(1.0f);
        assertEquals(37 * 31 + Float.hashCode(1.0f), builder.toHashCode());
        assertEquals(37 * 31 + Float.hashCode(1.0f), reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f, 3.0f};
        builder.append(floatArray);
        int expectedHashCode = 37 * 3;
        for (float f : floatArray) {
            expectedHashCode = expectedHashCode * 31 + Float.hashCode(f);
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(33, builder.toHashCode());
        assertEquals(33, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        int expectedHashCode = 37 * 3 + 1; // hashCode of int array
        for (int i : intArray) {
            expectedHashCode = expectedHashCode * 31 + i;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        assertEquals(33 * 31 + 1, builder.toHashCode());
        assertEquals(33 * 31 + 1, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);
        int expectedHashCode = 37 * 3 + 1; // hashCode of long array
        for (long l : longArray) {
            expectedHashCode = expectedHashCode * 31 + (int) (l ^ (l >> 32));
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendObject() {
        String stringToTest = "test";
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(stringToTest);
        int expectedHashCode = 17 * 31 + stringToTest.hashCode();
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendObjectArray() {
        String[] stringArrayToTest = {"test1", "test2", "test3"};
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(stringArrayToTest);
        int expectedHashCode = 37 * 3;
        for (String s : stringArrayToTest) {
            expectedHashCode = expectedHashCode * 31 + s.hashCode();
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        assertEquals(33, builder.toHashCode());
        assertEquals(33, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);
        int expectedHashCode = 37 * 3 + 1; // hashCode of short array
        for (short s : shortArray) {
            expectedHashCode = expectedHashCode * 31 + s;
        }
        assertEquals(expectedHashCode, builder.toHashCode());
        assertEquals(expectedHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 5;
        builder.appendSuper(superHashCode);
        assertEquals(37 * 31 + superHashCode, builder.toHashCode());
        assertEquals(37 * 31 + superHashCode, reflectionHashCode(builder.build(), 17, 37, false, null, new String[0]));
    }
}