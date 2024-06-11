package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertNotNull(hcb);
        
        HashCodeBuilder hcbWithInit = new HashCodeBuilder(11, 31);
        assertNotNull(hcbWithInit);
        assertEquals(11, hcbWithInit.iTotal);
        assertEquals(31, hcbWithInit.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        int expectedHashCode = 32 * 31 + 1; // 1 for true and 0 for false
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);
        int expectedHashCode = 32 * 31 * booleanArray.length;
        for (boolean value : booleanArray) {
            expectedHashCode += (value ? 0 : 1);
        }
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) -1);
        int expectedHashCode = 32 * 31 + 1; // 1 for positive byte and -1 for negative byte
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, -1};
        hcb.append(byteArray);
        int expectedHashCode = 32 * 31 * byteArray.length;
        for (byte value : byteArray) {
            expectedHashCode += value;
        }
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('z');
        int expectedHashCode = 32 * 31 * 2; // 'a' and 'z'
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'z'};
        hcb.append(charArray);
        int expectedHashCode = 32 * 31 * charArray.length;
        for (char value : charArray) {
            expectedHashCode += value;
        }
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double value = 1.23;
        hcb.append(value);
        long doubleBits = Double.doubleToLongBits(value);
        int expectedHashCode = 32 * 31 + (int) (doubleBits ^ doubleBits >> 32);
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.46};
        hcb.append(doubleArray);
        long totalBits = 0;
        for (double value : doubleArray) {
            totalBits += Double.doubleToLongBits(value);
        }
        int expectedHashCode = 32 * 31 * doubleArray.length;
        totalBits = totalBits ^ totalBits >> 32;
        assertEquals(expectedHashCode + totalBits, hcb.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float value = 1.23f;
        hcb.append(value);
        int expectedHashCode = 32 * 31 + Float.floatToIntBits(value);
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.46f};
        hcb.append(floatArray);
        int totalBits = 0;
        for (float value : floatArray) {
            totalBits += Float.floatToIntBits(value);
        }
        int expectedHashCode = 32 * 31 * floatArray.length;
        totalBits = totalBits ^ totalBits >> 32;
        assertEquals(expectedHashCode + totalBits, hcb.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(42);
        int expectedHashCode = 32 * 31 + 42;
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {42, 43};
        hcb.append(intArray);
        int expectedHashCode = 32 * 31 * intArray.length;
        for (int value : intArray) {
            expectedHashCode += value;
        }
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long value = 42L;
        hcb.append(value);
        int expectedHashCode = 32 * 31 + (int)(value ^ value >> 32);
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {42L, 43L};
        hcb.append(longArray);
        long totalBits = 0;
        for (long value : longArray) {
            totalBits += (int)(value ^ value >> 32);
        }
        int expectedHashCode = 32 * 31 * longArray.length;
        assertEquals(expectedHashCode + totalBits, hcb.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String object = "test";
        hcb.append(object);
        int expectedHashCode = 32 * 31 + object.hashCode();
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {"test1", "test2"};
        hcb.append(objectArray);
        long totalHashCode = 0;
        for (Object value : objectArray) {
            totalHashCode += value.hashCode();
        }
        int expectedHashCode = 32 * 31 * objectArray.length;
        assertEquals(expectedHashCode + totalHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 42);
        int expectedHashCode = 32 * 31 + 42;
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {42, 43};
        hcb.append(shortArray);
        int expectedHashCode = 32 * 31 * shortArray.length;
        for (short value : shortArray) {
            expectedHashCode += value;
        }
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 123;
        hcb.appendSuper(superHashCode);
        int expectedHashCode = 32 * 31 + superHashCode;
        assertEquals(expectedHashCode, hcb.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Object obj = new Object() {
            int i = 42;
            double d = 1.23;
            short s = 43;
            byte b = 1;
            char c = 'a';
            boolean bool = true;
            int[] intArray = {42, 43};
            short[] shortArray = {42, 43};
        };
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(obj, false);
        int hashCode = HashCodeBuilder.reflectionHashCode(obj);
        assertEquals(expectedHashCode, hashCode);
    }
}