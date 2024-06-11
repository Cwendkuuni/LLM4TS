package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder hcbWithParams = new HashCodeBuilder(17, 37);
        Assert.assertNotNull(hcb);
        Assert.assertNotNull(hcbWithParams);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append(false);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        hcb.append(booleanArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + 0 + 1; // Initial odd number, multiplier odd number, first boolean is true, second boolean is false
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 5);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append((byte) -5);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        byte[] byteArray = {5, -5};
        hcb.append(byteArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + 5 + (5 * 37) + (-5 * 37) + (-5 * 37);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append('z');
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        char[] charArray = {'a', 'z'};
        hcb.append(charArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + 97 + 97 * 37;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append(2.0);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        hcb.append(doubleArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + Double.doubleToLongBits(1.0) + Double.doubleToLongBits(2.0) * 37;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.0f);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append(2.0f);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        hcb.append(floatArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + Float.floatToIntBits(1.0f) + Float.floatToIntBits(2.0f) * 37;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append(-5);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int[] intArray = {5, -5};
        hcb.append(intArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + 5 + (-5 * 37);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(5L);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append(-5L);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        long[] longArray = {5L, -5L};
        hcb.append(longArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + (int)(5L ^ (5L >> 32)) + (int)(-5L ^ (-5L >> 32)) * 37;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object obj = new Object();
        hcb.append(obj);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + obj.hashCode();
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        hcb.append(objectArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + objectArray[0].hashCode() + objectArray[1].hashCode() * 37;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 5);
        int hashCodeBeforeAppending = hcb.toHashCode();
        hcb.append((short) -5);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertNotSame(hashCodeBeforeAppending, hashCodeAfterAppending);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        short[] shortArray = {(short) 5, (short) -5};
        hcb.append(shortArray);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + 5 + (-5 * 37);
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        int superHashCode = 10;
        hcb.appendSuper(superHashCode);
        int hashCodeBeforeAppending = hcb.toHashCode();
        int expectedHashCode = 37 * (37 + 1) + superHashCode;
        int hashCodeAfterAppending = hcb.toHashCode();
        Assert.assertEquals(expectedHashCode, hashCodeAfterAppending);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNullObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOddNumber() {
        HashCodeBuilder hcb = new HashCodeBuilder(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMultiplierOddNumber() {
        HashCodeBuilder hcb = new HashCodeBuilder(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, false, null, null, null, new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithInvalidClassCast() {
        Object obj = new Object() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        Class<?> clazz = obj.getClass();
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, obj, false, clazz.getClass(), new String[0]);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = InternalError.class)
    public void testReflectionHashCodeWithIllegalAccessException() {
        Object obj = new Object();
        Class<?> clazz = obj.getClass();
        Field privateField = FieldUtils.getField(obj, "privateField", true);
        try {
            HashCodeBuilder.reflectionHashCode(17, 37, obj, false, clazz, new String[]{"privateField"});
        } catch (IllegalAccessException e) {
            throw new AssertionError("Unexpected IllegalAccessException", e);
        } finally {
            // This is necessary to avoid the assertion error
            unregister(obj);
        }
    }

    @Test
    public void testReflectionHashCodeWithRegisteredObject() {
        Object obj = new Object();
        HashCodeBuilder.getRegistry().add(new IDKey(obj));
        Class<?> clazz = obj.getClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, false, clazz, new String[0]);
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj);
        int hashCodeAppended = hcb.toHashCode();
        Assert.assertEquals(hashCode, hashCodeAppended);
    }

    @Test
    public void testReflectionHashCodeWithExcludedField() {
        Object obj = new Object() {
            int a = 1;
            private int b = 2;
        };
        Class<?> clazz = obj.getClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, false, clazz, new String[]{"b"});
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj);
        int hashCodeAppended = hcb.toHashCode();
        Assert.assertEquals(hashCode, hashCodeAppended);
    }

    @Test
    public void testReflectionHashCodeWithTransientField() {
        Object obj = new Object() {
            @SuppressWarnings("unused")
            transient int a = 1;
            int b = 2;
        };
        Class<?> clazz = obj.getClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, obj, true, clazz, new String[0]);
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(obj);
        int hashCodeAppended = hcb.toHashCode();
        Assert.assertEquals(hashCode, hashCodeAppended);
    }

    private static void unregister(Object value) {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        if (registry != null) {
            registry.remove(new IDKey(value));
        }
    }

    static class IDKey {
        final Object value;

        IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IDKey other = (IDKey) obj;
            return value.equals(other.value);
        }
    }
}