package org.apache.commons.lang3.builder;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.ArrayUtils.toString;
import static org.apache.commons.lang3.reflect.FieldUtils.getFields;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilderConstructorWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(11, 31);
        Assert.assertEquals(11, hashCodeBuilder.iTotal);
        Assert.assertEquals(31, hashCodeBuilder.iConstant);
    }

    @Test
    public void testHashCodeBuilderAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        Assert.assertEquals(32, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        hashCodeBuilder.append(booleanArray);
        int expectedTotal = 81; // (37 * 3) + (0 * 1) + (37 * 3)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 42);
        Assert.assertEquals(53, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = {42, -12, 0};
        hashCodeBuilder.append(byteArray);
        int expectedTotal = 251; // (37 * 3) + 42 + (-65) + (37 * 2)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('A');
        Assert.assertEquals(33, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = {'A', 'B', 'C'};
        hashCodeBuilder.append(charArray);
        int expectedTotal = 397; // (37 * 3) + 65 + 66 + 67
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(Math.PI);
        long doubleBits = Double.doubleToLongBits(Math.PI);
        int expectedLowBits = (int) (doubleBits & 0xFFFFFFFF);
        int expectedHighBits = (int) ((doubleBits >>> 32) & 0xFFFFFFFF);
        int expectedTotal = (37 * 3) + expectedLowBits + expectedHighBits;
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = {Math.PI, Math.E, 0.0};
        hashCodeBuilder.append(doubleArray);
        int expectedTotal = 1329; // (37 * 3) + 1625 + 0
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(3.14f);
        int floatCode = Float.floatToIntBits(3.14f);
        int expectedTotal = (37 * 3) + floatCode;
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = {3.14f, 1.618f, 0.0f};
        hashCodeBuilder.append(floatArray);
        int expectedTotal = 1321; // (37 * 3) + 2146382152 + 16777216 + 0
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(42);
        Assert.assertEquals(53, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = {42, -12, 0};
        hashCodeBuilder.append(intArray);
        int expectedTotal = 251; // (37 * 3) + 42 + (-65) + (37 * 2)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(42L);
        long longBits = Double.doubleToLongBits(42L);
        int expectedLowBits = (int) (longBits & 0xFFFFFFFF);
        int expectedHighBits = (int) ((longBits >>> 32) & 0xFFFFFFFF);
        int expectedTotal = (37 * 3) + expectedLowBits + expectedHighBits;
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = {42L, -12L, 0L};
        hashCodeBuilder.append(longArray);
        int expectedTotal = 1353; // (37 * 3) + 2145483648 + (-8517453632) + (37 * 2)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String string = "HelloWorld";
        hashCodeBuilder.append(string);
        int expectedTotal = 33039; // (37 * 3) + 1101161478 // 1101161478 = "HelloWorld".hashCode()
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        Object[] objectArray = {"HelloWorld1", "HelloWorld2", null};
        hashCodeBuilder.append(objectArray);
        int expectedTotal = 47293; // (37 * 3) + 1101161478 + 1101161478 + (-842150461)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 42);
        Assert.assertEquals(53, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = {42, -12, 0};
        hashCodeBuilder.append(shortArray);
        int expectedTotal = 251; // (37 * 3) + 42 + (-65) + (37 * 2)
        Assert.assertEquals(expectedTotal, hashCodeBuilder.iTotal);
    }

    @Test
    public void testHashCodeBuilderAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int superHashCode = 42;
        hashCodeBuilder.appendSuper(superHashCode);
        Assert.assertEquals(53 + superHashCode, hashCodeBuilder.iTotal);
    }

    @Test
    public void testReflectionHashCodeWithInitialOddNumberAndMultiplierOddNumber() {
        int actualHashCode = HashCodeBuilder.reflectionHashCode(11, 31, new Object());
        int expectedHashCode = 11;
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithInvalidInitialOddNumber() {
        HashCodeBuilder.reflectionHashCode(0, 31, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithInvalidMultiplierOddNumber() {
        HashCodeBuilder.reflectionHashCode(11, 0, new Object());
    }

    @Test
    public void testReflectionHashCodeWithObject() {
        Object testObject = new Object() {
            private int field1 = 1;
            private int field2 = 2;
            private String field3 = "test";
        };
        int actualHashCode = HashCodeBuilder.reflectionHashCode(17, 37, testObject);
        int expectedHashCode = 23709817; // 17 * 3 + 37 + 1 + (1 * 37) + (2 * 37) + "test".hashCode()
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(17, 37, null);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object testObject = new Object() {
            private int field1 = 1;
            private int field2 = 2;
            private String field3 = "test";
            private transient int transientField = 3;
        };
        int actualHashCode = HashCodeBuilder.reflectionHashCode(17, 37, testObject, true, Object.class, new String[]{"field3"});
        int expectedHashCode = 23643221; // 17 * 3 + 3 + 1 + (1 * 37) + (2 * 37) + 17
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCodeWithSuperClass() {
        Object testObject = new ChildObject();
        int actualHashCode = HashCodeBuilder.reflectionHashCode(17, 37, testObject, false, Object.class, null);
        int expectedHashCode = 2167210363; // (17 * 3) + 37 + 1 + 77565
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    private static class IDKey {
        private final Object value;

        public IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(value);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || (o != null && this.value.equals(o));
        }
    }

    private static class ChildObject extends Object {
        @Override
        public int hashCode() {
            return 7;
        }
    }
}