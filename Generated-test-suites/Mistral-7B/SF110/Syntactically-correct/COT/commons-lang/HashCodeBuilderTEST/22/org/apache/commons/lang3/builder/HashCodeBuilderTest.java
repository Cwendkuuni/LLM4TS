package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(53, 29);
        assertEquals(53, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(3835328887, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(true);
        assertEquals(1238642427, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        int expectedHashCode = 1116126285;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(booleanArray);
        int expectedHashCodeWithInitialValue = 210247827;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 10);
        assertEquals(11107752, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append((byte) 10);
        assertEquals(2051788131, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);
        int expectedHashCode = 1966628828;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(byteArray);
        int expectedHashCodeWithInitialValue = 1227920195;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('A');
        assertEquals(501847553, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append('A');
        assertEquals(2113260751, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'A', 'B', 'C'};
        builder.append(charArray);
        int expectedHashCode = 1812741433;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(charArray);
        int expectedHashCodeWithInitialValue = 172181235;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14159265358979323846;
        builder.append(value);
        long doubleAsLongBits = Double.doubleToLongBits(value);
        int expectedHashCode = 1874486585;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(value);
        int expectedHashCodeWithInitialValue = 1898014229;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);
        int expectedHashCode = 301643480;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(doubleArray);
        int expectedHashCodeWithInitialValue = 2229409127;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 3.14159265f;
        builder.append(value);
        int expectedHashCode = 1360716445;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(value);
        int expectedHashCodeWithInitialValue = 2121345871;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);
        int expectedHashCode = 1101826650;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(floatArray);
        int expectedHashCodeWithInitialValue = 1592733821;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(10);
        assertEquals(1120403411, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(10);
        assertEquals(1534234639, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        int expectedHashCode = 1219124973;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(intArray);
        int expectedHashCodeWithInitialValue = 1314572241;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 10L;
        builder.append(value);
        int expectedHashCode = 1828731765;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(value);
        int expectedHashCodeWithInitialValue = 2063417181;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);
        int expectedHashCode = -1102524580;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(longArray);
        int expectedHashCodeWithInitialValue = -1092686865;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String object = "test";
        builder.append(object);
        int expectedHashCode = 74430993;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(object);
        int expectedHashCodeWithInitialValue = 1778090855;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {"test1", "test2", "test3"};
        builder.append(objectArray);
        int expectedHashCode = 1368768457;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(objectArray);
        int expectedHashCodeWithInitialValue = -1929381525;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 10);
        assertEquals(1929496407, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append((short) 10);
        assertEquals(2137733329, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);
        int expectedHashCode = 1824758536;
        assertEquals(expectedHashCode, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.append(shortArray);
        int expectedHashCodeWithInitialValue = 1910025833;
        assertEquals(expectedHashCodeWithInitialValue, builderWithInitialValue.hashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 10;
        builder.appendSuper(superHashCode);
        assertEquals(3835328887, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(31, 61);
        builderWithInitialValue.appendSuper(superHashCode);
        assertEquals(1954392281, builderWithInitialValue.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNull() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] nullArray = {null};
        builder.append(nullArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        try {
            HashCodeBuilder.reflectionHashCode(null, false);
        } catch (IllegalArgumentException e) {
            assertEquals("The object to build a hash code for must not be null", e.getMessage());
        }
    }

    @Test(expected = InternalError.class)
    public void testReflectionHashCodeWithIllegalAccessException() {
        try {
            Class<?> clazz = Object.class;
            HashCodeBuilder.reflectionHashCode(17, 37, new Object(){}, false, clazz, new String[]{"illegalField"});
        } catch (IllegalAccessException e) {
            // Expected
        } catch (Exception e) {
            throw new InternalError("Unexpected exception: " + e.getClass().getName());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionHashCodeWithNullClass() {
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, new Object(), false, null, new String[0]);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}