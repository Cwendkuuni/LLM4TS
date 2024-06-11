package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        assertEquals(34, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder(11, 13);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(-2041895871, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder(19, 23);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(1365633829, hashCode2);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int hashCode = hcb.toHashCode();
        assertEquals(31, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append(false);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(-1, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(true).append(false);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(-1677721308, hashCode2);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hcb.append(booleanArray);
        int hashCode = hcb.toHashCode();
        assertEquals(1339878021, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        boolean[] booleanArray1 = null;
        hcb1.append(booleanArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        int hashCode = hcb.toHashCode();
        assertEquals(33, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append((byte) -128);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(-129, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((byte) 1).append((byte) -128);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(-1513293603, hashCode2);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {(byte) 1, (byte) -128};
        hcb.append(byteArray);
        int hashCode = hcb.toHashCode();
        assertEquals(1614321255, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        byte[] byteArray1 = null;
        hcb1.append(byteArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        int hashCode = hcb.toHashCode();
        assertEquals(35, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append('z');
        int hashCode1 = hcb1.toHashCode();
        assertEquals(-125, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append('a').append('z');
        int hashCode2 = hcb2.toHashCode();
        assertEquals(-1552768831, hashCode2);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'z'};
        hcb.append(charArray);
        int hashCode = hcb.toHashCode();
        assertEquals(721465645, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        char[] charArray1 = null;
        hcb1.append(charArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23);
        int hashCode = hcb.toHashCode();
        assertEquals(-1205645625, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append(Double.NaN);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(-1743543151, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1.23).append(Double.NaN);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(341985932, hashCode2);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23, Double.NaN};
        hcb.append(doubleArray);
        int hashCode = hcb.toHashCode();
        assertEquals(1378573211, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        double[] doubleArray1 = null;
        hcb1.append(doubleArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23f);
        int hashCode = hcb.toHashCode();
        assertEquals(-1086581139, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append(Float.NaN);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(1954845647, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1.23f).append(Float.NaN);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(1984416862, hashCode2);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.23f, Float.NaN};
        hcb.append(floatArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1331712330, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        float[] floatArray1 = null;
        hcb1.append(floatArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(123);
        int hashCode = hcb.toHashCode();
        assertEquals(1127380343, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append(-123);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(1212883515, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(123).append(-123);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(1312618583, hashCode2);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {123, -123};
        hcb.append(intArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1028186839, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        int[] intArray1 = null;
        hcb1.append(intArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(123L);
        int hashCode = hcb.toHashCode();
        assertEquals(1127380343, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append(-123L);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(1212883515, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(123L).append(-123L);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(1312618583, hashCode2);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {123L, -123L};
        hcb.append(longArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1028186839, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        long[] longArray1 = null;
        hcb1.append(longArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        String str = "test";
        hcb.append(str);
        int hashCode = hcb.toHashCode();
        assertEquals(1523431681, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        Object obj = new Object();
        hcb1.append(obj);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(str).append(obj);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(-2029637535, hashCode2);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), "test"};
        hcb.append(objectArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-725283811, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        Object[] objectArray1 = null;
        hcb1.append(objectArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 123);
        int hashCode = hcb.toHashCode();
        assertEquals(1127380343, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        hcb1.append((short) -123);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(1212883515, hashCode1);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((short) 123).append((short) -123);
        int hashCode2 = hcb2.toHashCode();
        assertEquals(1312618583, hashCode2);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {(short) 123, (short) -123};
        hcb.append(shortArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1028186839, hashCode);

        HashCodeBuilder hcb1 = new HashCodeBuilder();
        short[] shortArray1 = null;
        hcb1.append(shortArray1);
        int hashCode1 = hcb1.toHashCode();
        assertEquals(0, hashCode1);
    }

    @Test
    public void testAppendSuperInt() {
        class TestClass {
            int superHashCode = 42;
        }

        HashCodeBuilder hcb = new HashCodeBuilder();
        TestClass testClass = new TestClass();
        hcb.appendSuper(testClass.superHashCode);
        int hashCode = hcb.toHashCode();
        assertEquals(43, hashCode);
    }
}