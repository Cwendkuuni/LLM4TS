package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertNotNull(hcb);

        HashCodeBuilder hcbWithInitialOddNumber = new HashCodeBuilder(11);
        assertNotNull(hcbWithInitialOddNumber);
        assertEquals(11, hcbWithInitialOddNumber.iTotal);

        HashCodeBuilder hcbWithMultiplierOddNumber = new HashCodeBuilder(13, 17);
        assertNotNull(hcbWithMultiplierOddNumber);
        assertEquals(17, hcbWithMultiplierOddNumber.iConstant);
        assertEquals(13, hcbWithMultiplierOddNumber.iTotal);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        assertEquals(32, hcb.iTotal);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);
        assertEquals(64, hcb.iTotal);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 10);
        assertEquals(38, hcb.iTotal);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hcb.append(byteArray);
        assertEquals(60, hcb.iTotal);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        assertEquals(43, hcb.iTotal);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hcb.append(charArray);
        assertEquals(82, hcb.iTotal);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double value = 1.2;
        hcb.append(value);
        long hashCode = Double.doubleToLongBits(value);
        assertEquals(hashCode, hcb.iTotal);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.2, 2.3, 3.4};
        hcb.append(doubleArray);
        long totalHashCode = 1;
        for (double value : doubleArray) {
            long hashCode = Double.doubleToLongBits(value);
            totalHashCode *= hcb.iConstant;
            totalHashCode += hashCode;
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float value = 1.2f;
        hcb.append(value);
        int hashCode = Float.floatToIntBits(value);
        assertEquals(hashCode, hcb.iTotal);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.2f, 2.3f, 3.4f};
        hcb.append(floatArray);
        int totalHashCode = 1;
        for (float value : floatArray) {
            totalHashCode *= hcb.iConstant;
            totalHashCode += Float.floatToIntBits(value);
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5);
        assertEquals(53, hcb.iTotal);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {5, 6, 7};
        hcb.append(intArray);
        int totalHashCode = 1;
        for (int value : intArray) {
            totalHashCode *= hcb.iConstant;
            totalHashCode += value;
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(10L);
        assertEquals(53, hcb.iTotal);
        hcb.append(11L);
        assertEquals(1125970005520531534L, hcb.iTotal);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hcb.append(longArray);
        long totalHashCode = 1;
        for (long value : longArray) {
            totalHashCode *= hcb.iConstant;
            totalHashCode += value;
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object();
        hcb.append(obj);
        assertEquals(obj.hashCode(), hcb.iTotal);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);
        int totalHashCode = 1;
        for (Object object : objectArray) {
            totalHashCode *= hcb.iConstant;
            totalHashCode += object.hashCode();
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 10);
        assertEquals(44, hcb.iTotal);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hcb.append(shortArray);
        int totalHashCode = 1;
        for (short value : shortArray) {
            totalHashCode *= hcb.iConstant;
            totalHashCode += value;
        }
        assertEquals(totalHashCode, hcb.iTotal);
    }

    @Test
    public void testAppendSuperInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 10;
        hcb.appendSuper(superHashCode);
        assertEquals(superHashCode, hcb.iTotal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialOddNumber() {
        new HashCodeBuilder(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMultiplierOddNumber() {
        new HashCodeBuilder(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(null);
        hcb.iTotal *= hcb.iConstant;
    }
}