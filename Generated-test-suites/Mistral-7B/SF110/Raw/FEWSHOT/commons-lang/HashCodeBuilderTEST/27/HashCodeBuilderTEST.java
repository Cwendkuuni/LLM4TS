package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int initialHashCode = hcb.toHashCode();
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        assertEquals(initialHashCode, hcb2.toHashCode());

        hcb.append(1);
        hcb.append(1.0f);
        hcb.append("hello");
        hcb.append(new Object(){});

        int finalHashCode = hcb.toHashCode();
        HashCodeBuilder hcb3 = new HashCodeBuilder();
        hcb3.append(1);
        hcb3.append(1.0f);
        hcb3.append("hello");
        hcb3.append(new Object(){});

        assertEquals(finalHashCode, hcb3.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(false);
        hcb.append(true);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(false);
        hcb2.append(true);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte)1);
        hcb.append((byte)2);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((byte)1);
        hcb2.append((byte)2);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('b');
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append('a');
        hcb2.append('b');

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0);
        hcb.append(2.0);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1.0);
        hcb2.append(2.0);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((float)1.0);
        hcb.append((float)2.0);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((float)1.0);
        hcb2.append((float)2.0);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(2);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1);
        hcb2.append(2);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(2L);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(1L);
        hcb2.append(2L);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short)1);
        hcb.append((short)2);
        int finalHashCode = hcb.toHashCode();

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append((short)1);
        hcb2.append((short)2);

        assertEquals(finalHashCode, hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        boolean[] booleanArray2 = {true, false};
        hcb2.append(booleanArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {(byte)1, (byte)2};
        hcb.append(byteArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        byte[] byteArray2 = {(byte)1, (byte)2};
        hcb2.append(byteArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'b'};
        hcb.append(charArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        char[] charArray2 = {'a', 'b'};
        hcb2.append(charArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        hcb.append(doubleArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        double[] doubleArray2 = {1.0, 2.0};
        hcb2.append(doubleArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        hcb.append(floatArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        float[] floatArray2 = {1.0f, 2.0f};
        hcb2.append(floatArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {1, 2};
        hcb.append(intArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        int[] intArray2 = {1, 2};
        hcb2.append(intArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {1L, 2L};
        hcb.append(longArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        long[] longArray2 = {1L, 2L};
        hcb2.append(longArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {(short)1, (short)2};
        hcb.append(shortArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        short[] shortArray2 = {(short)1, (short)2};
        hcb2.append(shortArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendArrayObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        Object[] objectArray2 = {new Object(), new Object()};
        hcb2.append(objectArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object object = new Object();
        hcb.append(object);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        Object object2 = new Object();
        hcb2.append(object2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        Object[] objectArray2 = {new Object(), new Object()};
        hcb2.append(objectArray2);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }

    @Test
    public void testHashCodeBuilderAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 123;
        hcb.appendSuper(superHashCode);

        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.appendSuper(superHashCode);

        assertEquals(hcb.toHashCode(), hcb2.toHashCode());
    }
}