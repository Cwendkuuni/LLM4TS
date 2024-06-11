package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        assertEquals(17, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(382, hashCode);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int hashCode = hcb.toHashCode();
        assertEquals(33, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(true);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1277, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hcb.append(booleanArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-2009958685, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(booleanArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(942156168, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        int hashCode = hcb.toHashCode();
        assertEquals(53, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append((byte) 5);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(131825, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        hcb.append(byteArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1071590123, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(byteArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(-1610612735, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        int hashCode = hcb.toHashCode();
        assertEquals(34189, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append('a');
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1586296303, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        hcb.append(charArray);
        int hashCode = hcb.toHashCode();
        assertEquals(1144178299, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(charArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(209715187, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.23);
        int hashCode = hcb.toHashCode();
        assertEquals(813346235, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(1.23);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1247639693, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.34, 3.45};
        hcb.append(doubleArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-198531033, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(doubleArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(-304453217, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float) 1.23);
        int hashCode = hcb.toHashCode();
        assertEquals(152339164, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append((float) 1.23);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1625020043, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.34f, 3.45f};
        hcb.append(floatArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1159444914, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(floatArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1248403589, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        int hashCode = hcb.toHashCode();
        assertEquals(33, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(1);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(3417, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        hcb.append(intArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1463567681, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(intArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(-2083826187, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        int hashCode = hcb.toHashCode();
        assertEquals(33, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(1L);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(3417, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1L, 2L, 3L};
        hcb.append(longArray);
        int hashCode = hcb.toHashCode();
        assertEquals(199169443, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(longArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1528031489, hashCode);
    }

    @Test
    public void testAppendObject() {
        String string = "test";
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(string);
        int hashCode = hcb.toHashCode();
        assertEquals(1131735676, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(string);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(1131735676, hashCode);

        Object obj = new Object();
        hcb = new HashCodeBuilder();
        hcb.append(obj);
        hashCode = hcb.toHashCode();
        assertEquals(149510358, hashCode);

        hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(obj);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(149510358, hashCode);
    }

    @Test
    public void testAppendObjectArray() {
        String[] stringArray = {"test1", "test2", "test3"};
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(stringArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-2122156536, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(stringArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(-2122156536, hashCode);

        Object[] objArray = {new Object(), new Object()};
        hcb = new HashCodeBuilder();
        hcb.append(objArray);
        hashCode = hcb.toHashCode();
        assertEquals(2104906913, hashCode);

        hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(objArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(2104906913, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        int hashCode = hcb.toHashCode();
        assertEquals(33, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append((short) 1);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(3417, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        hcb.append(shortArray);
        int hashCode = hcb.toHashCode();
        assertEquals(-1445559071, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.append(shortArray);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(-1883307873, hashCode);
    }

    @Test
    public void testAppendSuperHashCode() {
        int superHashCode = 42;
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(superHashCode);
        int hashCode = hcb.toHashCode();
        assertEquals(33281, hashCode);

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(11, 31);
        hcbWithInitialValue.appendSuper(superHashCode);
        hashCode = hcbWithInitialValue.toHashCode();
        assertEquals(33958, hashCode);
    }
}