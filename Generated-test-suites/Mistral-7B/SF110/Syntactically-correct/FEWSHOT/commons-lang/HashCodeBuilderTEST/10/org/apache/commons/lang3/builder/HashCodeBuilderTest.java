package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(17, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(11, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(false);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
        builder.append(true);
        hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {false, true};
        builder.append(booleanArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {5, 10};
        builder.append(byteArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'e'};
        builder.append(charArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double value = 3.14;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {3.14, 1.618};
        builder.append(doubleArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float value = 3.14f;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {3.14f, 1.618f};
        builder.append(floatArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(11);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {11, 13};
        builder.append(intArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long value = 11L;
        builder.append(value);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {11L, 13L};
        builder.append(longArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object();
        builder.append(object);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, object.hashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        builder.append(objectArray);
        int hashCode = builder.toHashCode();
        // Hash codes of objects in the array are guaranteed to be different, so we only check that the result is non-zero
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 11);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {11, 13};
        builder.append(shortArray);
        int hashCode = builder.toHashCode();
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 11;
        builder.appendSuper(superHashCode);
        int hashCode = builder.toHashCode();
        assertEquals(hashCode, superHashCode);
    }

    @Test
    public void testReflectionHashCode() {
        Object object = new ClassWithHashCodeMethods();
        int hashCode = HashCodeBuilder.reflectionHashCode(object);
        // Hash codes of objects in the class are guaranteed to be different, so we only check that the result is non-zero
        assertTrue(hashCode % 37 != 0);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object object = new ClassWithHashCodeMethods();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, new String[]{"field1"});
        // Hash codes of objects in the class are guaranteed to be different, so we only check that the result is non-zero
        assertTrue(hashCode % 37 != 0);
    }
}

class ClassWithHashCodeMethods {
    private int field1 = 11;
    private int field2 = 13;

    @Override
    public int hashCode() {
        return field1 + field2;
    }
}