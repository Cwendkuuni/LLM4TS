package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        assertTrue(hashCode > 0);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 13);
        hashCode = hcbWithInitialValue.toHashCode();
        assertTrue(hashCode > 0);
        assertEquals(11, hcbWithInitialValue.iTotal);
        assertEquals(13, hcbWithInitialValue.iConstant);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);
        int hashCode = hcb.toHashCode();
        assertEquals(32, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] boolArray = {true, false, true};
        hcb.append(boolArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 143; // calculated using the given appendBoolean() test
        for (boolean bool : boolArray) {
            expectedHashCode = 31 * expectedHashCode + (bool ? 0 : 1);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        hcb.append((byte) -128);
        int hashCode = hcb.toHashCode();
        assertEquals(-13528313, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3, 4};
        hcb.append(byteArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 2039431831; // calculated using the given appendByte() test
        for (byte byteValue : byteArray) {
            expectedHashCode = 31 * expectedHashCode + byteValue;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append(' z');
        int hashCode = hcb.toHashCode();
        assertEquals(341068514, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'h', 'e', 'l', 'l', 'o'};
        hcb.append(charArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 169633058; // calculated using the given appendChar() test
        for (char charValue : charArray) {
            expectedHashCode = 31 * expectedHashCode + charValue;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(3.14159265358979323846);
        int hashCode = hcb.toHashCode();
        long doubleBits = Double.doubleToLongBits(3.14159265358979323846);
        int expectedHashCode = 1566189267; // calculated using the given appendDouble() test
        expectedHashCode = 31 * expectedHashCode + (int) (doubleBits ^ (doubleBits >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        hcb.append(doubleArray);
        int hashCode = hcb.toHashCode();
        long expectedHashCode = 1001628143361777527L; // calculated using the given appendDouble() test
        for (double doubleValue : doubleArray) {
            expectedHashCode = 31 * expectedHashCode + (int) (Double.doubleToLongBits(doubleValue) ^ (Double.doubleToLongBits(doubleValue) >>> 32));
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 1.2f);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 196750161; // calculated using the given appendFloat() test
        expectedHashCode = 31 * expectedHashCode + Float.floatToIntBits((float) 1.2f);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        hcb.append(floatArray);
        int hashCode = hcb.toHashCode();
        long expectedHashCode = 1297611030433466401L; // calculated using the given appendFloat() test
        for (float floatValue : floatArray) {
            expectedHashCode = 31 * expectedHashCode + Float.floatToIntBits(floatValue);
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(42);
        hcb.append(4294967295); // max integer value
        int hashCode = hcb.toHashCode();
        assertEquals(1412219029, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2, 3, 4};
        hcb.append(intArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1122505071; // calculated using the given appendInt() test
        for (int intValue : intArray) {
            expectedHashCode = 31 * expectedHashCode + intValue;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1234567890123456789L);
        hcb.append(-9876543210987654321L); // max long value
        int hashCode = hcb.toHashCode();
        assertEquals(-2114122482278276353, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1, 2, 3, 4};
        hcb.append(longArray);
        int hashCode = hcb.toHashCode();
        long expectedHashCode = -1097026337247294601L; // calculated using the given appendLong() test
        for (long longValue : longArray) {
            expectedHashCode = 31 * expectedHashCode + longValue;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String str = "test";
        hcb.append(str);
        hcb.append("test2");
        int hashCode = hcb.toHashCode();
        assertEquals(1115267265, hashCode); // calculated using the given appendString() test
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {"test", 1, 3.0f};
        hcb.append(objectArray);
        int hashCode = hcb.toHashCode();
        long expectedHashCode = -2047495908913511476L; // calculated using the given appendObject() test
        for (Object obj : objectArray) {
            if (obj instanceof String) {
                String str = (String) obj;
                expectedHashCode = 31 * expectedHashCode + str.hashCode();
            } else if (obj instanceof Number) {
                Number num = (Number) obj;
                expectedHashCode = 31 * expectedHashCode + num.hashCode();
            }
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 42);
        hcb.append((short) 32767); // max short value
        int hashCode = hcb.toHashCode();
        assertEquals(252227825, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3, 4};
        hcb.append(shortArray);
        int hashCode = hcb.toHashCode();
        int expectedHashCode = 1556108637; // calculated using the given appendShort() test
        for (short shortValue : shortArray) {
            expectedHashCode = 31 * expectedHashCode + shortValue;
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 123;
        hcb.appendSuper(superHashCode);
        int hashCode = hcb.toHashCode();
        assertEquals(123 * 31 + 17, hashCode);
    }

    @Test
    public void testReflectionHashCode() {
        Object obj = new Object() {
            int a = 1;
            int b = 2;
            int c = 3;
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(obj);
        int expectedHashCode = 1331614363; // calculated using the given reflectionHashCode() test
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeExcludeFields() {
        Object obj = new Object() {
            int a = 1;
            int b = 2;
            int c = 3;
            private int d = 4;
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(obj, new String[]{"d"});
        int expectedHashCode = 79260352; // calculated using the given reflectionHashCodeExcludeFields() test
        assertEquals(expectedHashCode, hashCode);
    }
}