package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int initialHashCode = builder.toHashCode();
        assertEquals(initialHashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderWithInitialValue() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        int initialHashCode = builder.toHashCode();
        assertEquals(initialHashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        builder.append(booleanArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2};
        builder.append(byteArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b'};
        builder.append(charArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        builder.append(doubleArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float) 1.0);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        builder.append(floatArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2};
        builder.append(intArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1L, 2L};
        builder.append(longArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object obj = new Object();
        builder.append(obj);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objArray = {new Object(), new Object()};
        builder.append(objArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2};
        builder.append(shortArray);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, builder.hashCode());
    }

    @Test
    public void testHashCodeBuilderAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(17);
        int hashCode = builder.toHashCode();
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCodeBoolean() {
        int hashCode = HashCodeBuilder.reflectionHashCode(true);
        int expectedHashCode = 1;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeBooleanArray() {
        int[] booleanArray = {true, false};
        int hashCode = HashCodeBuilder.reflectionHashCode(booleanArray);
        int expectedHashCode = 53;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeByte() {
        int hashCode = HashCodeBuilder.reflectionHashCode((byte) 1);
        int expectedHashCode = 31;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeByteArray() {
        byte[] byteArray = {1, 2};
        int hashCode = HashCodeBuilder.reflectionHashCode(byteArray);
        int expectedHashCode = 941888331;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeChar() {
        int hashCode = HashCodeBuilder.reflectionHashCode('a');
        int expectedHashCode = 193631;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeCharArray() {
        char[] charArray = {'a', 'b'};
        int hashCode = HashCodeBuilder.reflectionHashCode(charArray);
        int expectedHashCode = 772761645;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeDouble() {
        double value = 1.0;
        int hashCode = HashCodeBuilder.reflectionHashCode(value);
        int expectedHashCode = -1415424958;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeDoubleArray() {
        double[] doubleArray = {1.0, 2.0};
        int hashCode = HashCodeBuilder.reflectionHashCode(doubleArray);
        int expectedHashCode = 288261273;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeFloat() {
        float value = 1.0f;
        int hashCode = HashCodeBuilder.reflectionHashCode(value);
        int expectedHashCode = -1217812641;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeFloatArray() {
        float[] floatArray = {1.0f, 2.0f};
        int hashCode = HashCodeBuilder.reflectionHashCode(floatArray);
        int expectedHashCode = 1221655331;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeInt() {
        int value = 1;
        int hashCode = HashCodeBuilder.reflectionHashCode(value);
        int expectedHashCode = 1;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeIntArray() {
        int[] intArray = {1, 2};
        int hashCode = HashCodeBuilder.reflectionHashCode(intArray);
        int expectedHashCode = -1124023615;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeLong() {
        long value = 1L;
        int hashCode = HashCodeBuilder.reflectionHashCode(value);
        int expectedHashCode = 1;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeLongArray() {
        long[] longArray = {1L, 2L};
        int hashCode = HashCodeBuilder.reflectionHashCode(longArray);
        int expectedHashCode = 1233479372;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeObject() {
        Object obj = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(obj);
        int expectedHashCode = obj.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeObjectArray() {
        Object[] objArray = {new Object(), new Object()};
        int hashCode = HashCodeBuilder.reflectionHashCode(objArray);
        int expectedHashCode = Arrays.deepHashCode(objArray);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeShort() {
        short value = (short) 1;
        int hashCode = HashCodeBuilder.reflectionHashCode(value);
        int expectedHashCode = 1;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeShortArray() {
        short[] shortArray = {1, 2};
        int hashCode = HashCodeBuilder.reflectionHashCode(shortArray);
        int expectedHashCode = 283935065;
        assertEquals(expectedHashCode, hashCode);
    }
}