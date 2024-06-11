package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

public class HashCodeBuilderTest {

    private static HashCodeBuilder hashCodeBuilder;
    private static Object testObject;

    @BeforeClass
    public static void setUpClass() {
        testObject = new TestClass();
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        assertNotNull(hashCodeBuilder);
    }

    @Test
    public void testHashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(19, 31);
        assertNotNull(hashCodeBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenInitialNumber() {
        new HashCodeBuilder(2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenMultiplier() {
        new HashCodeBuilder(1, 2);
    }

    @Test
    public void testAppendingBooleanValue() {
        hashCodeBuilder.append(true);
        assertEquals(37, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingBooleanArray() {
        hashCodeBuilder.append(new boolean[]{true, false, true});
        assertEquals(1043, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingByteValue() {
        hashCodeBuilder.append((byte) 5);
        assertEquals(37 * 2 + 5, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingByteArray() {
        hashCodeBuilder.append(new byte[]{1, 2, 3});
        assertEquals(37 * 3 + 1 + 2 + 3, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingCharValue() {
        hashCodeBuilder.append('a');
        assertEquals(37 * 2 + 'a', hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingCharArray() {
        hashCodeBuilder.append(new char[]{'a', 'b', 'c'});
        assertEquals(37 * 3 + 'a' + 'b' + 'c', hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingDoubleValue() {
        hashCodeBuilder.append(1.23);
        long doubleHashCode = Double.doubleToLongBits(1.23);
        assertEquals(37 * 2 + (int) (doubleHashCode ^ (doubleHashCode >> 32)), hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingDoubleArray() {
        double[] doubleArray = {1.23, 2.34, 3.45};
        hashCodeBuilder.append(doubleArray);
        for (double value : doubleArray) {
            long doubleHashCode = Double.doubleToLongBits(value);
            assertEquals(37 * 3 + (int) (doubleHashCode ^ (doubleHashCode >> 32)), hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendingFloatValue() {
        hashCodeBuilder.append(1.23f);
        int floatHashCode = Float.floatToIntBits(1.23f);
        assertEquals(37 * 2 + floatHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingFloatArray() {
        float[] floatArray = {1.23f, 2.34f, 3.45f};
        hashCodeBuilder.append(floatArray);
        for (float value : floatArray) {
            int floatHashCode = Float.floatToIntBits(value);
            assertEquals(37 * 3 + floatHashCode, hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendingIntValue() {
        hashCodeBuilder.append(10);
        assertEquals(37 * 2 + 10, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingIntArray() {
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        for (int value : intArray) {
            assertEquals(37 * 3 + value, hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendingLongValue() {
        hashCodeBuilder.append(10L);
        long longHashCode = 10L ^ (10L >> 32);
        assertEquals(37 * 2 + (int) longHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingLongArray() {
        long[] longArray = {1L, 2L, 3L};
        hashCodeBuilder.append(longArray);
        for (long value : longArray) {
            long longHashCode = value ^ (value >> 32);
            assertEquals(37 * 3 + (int) longHashCode, hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendingObjectValue() {
        hashCodeBuilder.append(testObject);
        int objectHashCode = testObject.hashCode();
        assertEquals(37 * 2 + objectHashCode, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingObjectArray() {
        Object[] objectArray = {new TestClass(), new TestClass(), new TestClass()};
        hashCodeBuilder.append(objectArray);
        for (Object value : objectArray) {
            int objectHashCode = value.hashCode();
            assertEquals(37 * 3 + objectHashCode, hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendingShortValue() {
        hashCodeBuilder.append((short) 5);
        assertEquals(37 * 2 + 5, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testAppendingShortArray() {
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        for (short value : shortArray) {
            assertEquals(37 * 3 + value, hashCodeBuilder.toHashCode());
        }
    }

    @Test
    public void testAppendSuperHashCode() {
        hashCodeBuilder.appendSuper(10);
        assertEquals(37 * 2 + 10, hashCodeBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeWithObject() {
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        int objectHashCode = testObject.hashCode();
        assertEquals(37 * 2 + objectHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithBoolean() {
        int hashCode = HashCodeBuilder.reflectionHashCode(true);
        assertEquals(37, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithBooleanArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new boolean[]{true, false, true});
        assertEquals(1043, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithByte() {
        int hashCode = HashCodeBuilder.reflectionHashCode((byte) 5);
        assertEquals(37 * 2 + 5, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithByteArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new byte[]{1, 2, 3});
        assertEquals(37 * 3 + 1 + 2 + 3, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithChar() {
        int hashCode = HashCodeBuilder.reflectionHashCode('a');
        assertEquals(37 * 2 + 'a', hashCode);
    }

    @Test
    public void testReflectionHashCodeWithCharArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new char[]{'a', 'b', 'c'});
        assertEquals(37 * 3 + 'a' + 'b' + 'c', hashCode);
    }

    @Test
    public void testReflectionHashCodeWithDouble() {
        int hashCode = HashCodeBuilder.reflectionHashCode(1.23);
        long doubleHashCode = Double.doubleToLongBits(1.23);
        assertEquals(37 * 2 + (int) (doubleHashCode ^ (doubleHashCode >> 32)), hashCode);
    }

    @Test
    public void testReflectionHashCodeWithDoubleArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new double[]{1.23, 2.34, 3.45});
        for (double value : new double[]{1.23, 2.34, 3.45}) {
            long doubleHashCode = Double.doubleToLongBits(value);
            assertEquals(37 * 3 + (int) (doubleHashCode ^ (doubleHashCode >> 32)), hashCode);
        }
    }

    @Test
    public void testReflectionHashCodeWithFloat() {
        int hashCode = HashCodeBuilder.reflectionHashCode(1.23f);
        int floatHashCode = Float.floatToIntBits(1.23f);
        assertEquals(37 * 2 + floatHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithFloatArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new float[]{1.23f, 2.34f, 3.45f});
        for (float value : new float[]{1.23f, 2.34f, 3.45f}) {
            int floatHashCode = Float.floatToIntBits(value);
            assertEquals(37 * 3 + floatHashCode, hashCode);
        }
    }

    @Test
    public void testReflectionHashCodeWithInt() {
        int hashCode = HashCodeBuilder.reflectionHashCode(10);
        assertEquals(37 * 2 + 10, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithIntArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new int[]{1, 2, 3});
        for (int value : new int[]{1, 2, 3}) {
            assertEquals(37 * 3 + value, hashCode);
        }
    }

    @Test
    public void testReflectionHashCodeWithLong() {
        int hashCode = HashCodeBuilder.reflectionHashCode(10L);
        long longHashCode = 10L ^ (10L >> 32);
        assertEquals(37 * 2 + (int) longHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithLongArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new long[]{1L, 2L, 3L});
        for (long value : new long[]{1L, 2L, 3L}) {
            long longHashCode = value ^ (value >> 32);
            assertEquals(37 * 3 + (int) longHashCode, hashCode);
        }
    }

    @Test
    public void testReflectionHashCodeWithObjectArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new Object[]{new TestClass(), new TestClass(), new TestClass()});
        for (Object value : new Object[]{new TestClass(), new TestClass(), new TestClass()}) {
            int objectHashCode = value.hashCode();
            assertEquals(37 * 3 + objectHashCode, hashCode);
        }
    }

    @Test
    public void testReflectionHashCodeWithShort() {
        int hashCode = HashCodeBuilder.reflectionHashCode((short) 5);
        assertEquals(37 * 2 + 5, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithShortArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(new short[]{1, 2, 3});
        for (short value : new short[]{1, 2, 3}) {
            assertEquals(37 * 3 + value, hashCode);
        }
    }

    @Test
    public void testIsRegistered() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        assertFalse(HashCodeBuilder.isRegistered(testObject));
        hashCodeBuilder.append(testObject);
        assertTrue(HashCodeBuilder.isRegistered(testObject));
    }

    @Test
    public void testRegisterAndUnmarregisterObjects() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        assertNull(HashCodeBuilder.getRegistry());
        hashCodeBuilder.append(testObject);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertNotNull(registry);
        assertEquals(1, registry.size());
        IDKey idKey = registry.iterator().next();
        assertEquals(testObject.hashCode(), idKey.getValue().hashCode());
        hashCodeBuilder.unregister(testObject);
        registry = HashCodeBuilder.getRegistry();
        assertNull(registry);
    }

    @Test
    public void testReflectionAppend() throws IllegalAccessException {
        Object testObject = new TestClass();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Class<?> clazz = testObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            if (field.getName().equals("privateField")) {
                field.setAccessible(true);
                Object fieldValue = field.get(testObject);
                hashCodeBuilder.append(fieldValue);
                break;
            }
        }
        int hashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = testObject.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    private static class TestClass {
        private int privateField = 123;
    }

    private static class IDKey {
        final Object value;

        IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}