package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(17, hcb.toHashCode());

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(11, hcbWithInitialValue.toHashCode());
        assertEquals(31, hcbWithInitialValue.hashCode());

        HashCodeBuilder hcbWithInitialValueAndMultiplier = new HashCodeBuilder(5, 5);
        assertEquals(5, hcbWithInitialValueAndMultiplier.toHashCode());
        assertEquals(5, hcbWithInitialValueAndMultiplier.hashCode());

        HashCodeBuilder hcbWithInitialValueAndMultiplierNegative = new HashCodeBuilder(-5, -5);
        assertEquals(-5, hcbWithInitialValueAndMultiplierNegative.toHashCode());
        assertEquals(-5, hcbWithInitialValueAndMultiplierNegative.hashCode());

        HashCodeBuilder hcbWithInitialValueAndMultiplierZero = new HashCodeBuilder(0, 0);
        assertEquals(0, hcbWithInitialValueAndMultiplierZero.toHashCode());
        assertEquals(0, hcbWithInitialValueAndMultiplierZero.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(false);
        hcb.append(true);
        assertEquals(32, hcb.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] boolArray = {false, true, false};
        hcb.append(boolArray);
        assertEquals(64, hcb.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        assertEquals(38, hcb.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hcb.append(byteArray);
        assertEquals(111, hcb.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        assertEquals(39, hcb.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hcb.append(charArray);
        assertEquals(118, hcb.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double value = 1.23;
        hcb.append(value);
        long doubleBits = Double.doubleToLongBits(value);
        assertEquals(39 * 31 + Double.SIZE, hcb.toHashCode());
        assertEquals(doubleBits, hcb.toLong());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.46, 3.69};
        hcb.append(doubleArray);
        long totalDoubleBits = 0;
        for (double d : doubleArray) {
            totalDoubleBits += Double.doubleToLongBits(d);
        }
        assertEquals(3 * 31 * 3 + 3 * Double.SIZE, hcb.toHashCode());
        assertEquals(totalDoubleBits, hcb.toLong());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float value = (float) 1.23;
        hcb.append(value);
        int floatIntBits = Float.floatToIntBits(value);
        assertEquals(39 * 31 + Float.FLOAT_SIZE, hcb.toHashCode());
        assertEquals(floatIntBits, hcb.toInt());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.46f, 3.69f};
        hcb.append(floatArray);
        int totalFloatIntBits = 0;
        for (float f : floatArray) {
            totalFloatIntBits += Float.floatToIntBits(f);
        }
        assertEquals(3 * 31 * 3 + Float.FLOAT_SIZE * 3, hcb.toHashCode());
        assertEquals(totalFloatIntBits, hcb.toInt());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        assertEquals(41, hcb.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hcb.append(intArray);
        assertEquals(111, hcb.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        assertEquals(43, hcb.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        hcb.append(longArray);
        assertEquals(115, hcb.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object();
        hcb.append(obj);
        assertEquals(obj.hashCode(), hcb.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objArray = {new Object(), new Object(), new Object()};
        hcb.append(objArray);
        int totalObjectHashCode = 0;
        for (Object o : objArray) {
            totalObjectHashCode += o.hashCode();
        }
        assertEquals(3 * 31 * 3 + totalObjectHashCode, hcb.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        assertEquals(45, hcb.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hcb.append(shortArray);
        assertEquals(115, hcb.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 100;
        hcb.appendSuper(superHashCode);
        assertEquals(100 + 41, hcb.toHashCode());
    }

    @Test
    public void testReflectionHashCode() {
        Map<Object, Integer> objectToHashCode = new HashMap<>();
        objectToHashCode.put(new Object(), 100);
        objectToHashCode.put(new Object()[0], 200);
        objectToHashCode.put(new int[0], 300);
        objectToHashCode.put(new long[0], 400);
        objectToHashCode.put(new double[0], 500);
        objectToHashCode.put(new float[0], 600);
        objectToHashCode.put(new boolean[0], 700);
        objectToHashCode.put(new boolean[0][0], 800);
        objectToHashCode.put(new char[0], 900);
        objectToHashCode.put(new char[0][0], 1000);

        for (Map.Entry<Object, Integer> entry : objectToHashCode.entrySet()) {
            Object obj = entry.getKey();
            int hashCode = HashCodeBuilder.reflectionHashCode(obj, false);
            assertEquals(entry.getValue(), hashCode);
        }

        Map<Object, Integer> objectToHashCodeWithTransients = new HashMap<>();
        objectToHashCodeWithTransients.put(new Object() {
            private int transientField;

            public int getTransientField() {
                return transientField;
            }
        }, 1100);

        for (Map.Entry<Object, Integer> entry : objectToHashCodeWithTransients.entrySet()) {
            Object obj = entry.getKey();
            int hashCode = HashCodeBuilder.reflectionHashCode(obj, true);
            assertEquals(entry.getValue(), hashCode);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37, false, null, new String[0]);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        class TestObject {
            int excludedField = 1;
            int includedField = 2;

            @Override
            public String toString() {
                return "TestObject{" +
                        "excludedField=" + excludedField +
                        ", includedField=" + includedField +
                        '}';
            }
        }

        TestObject testObj = new TestObject();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(testObj, ReflectionToStringBuilder.toString(testObj).split(", "), false);
        assertEquals(17 * 31 + 2, reflectionHashCode);
    }
}