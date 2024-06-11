package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int initialHashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        Assert.assertNotSame(initialHashCode, anotherBuilder.toHashCode());
    }

    @Test
    public void testHashCodeBuilderWithInitialOddNumberAndMultiplierOddNumber() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 17);
        int initialHashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder(13, 17);
        Assert.assertNotSame(initialHashCode, anotherBuilder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenInitialNumber() {
        new HashCodeBuilder(2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashCodeBuilderWithEvenMultiplierNumber() {
        new HashCodeBuilder(13, 2);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append(false);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false};
        builder.append(booleanArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(booleanArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append((byte) 2);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2};
        builder.append(byteArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(byteArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append('b');
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b'};
        builder.append(charArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(charArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append(2.0);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.0, 2.0};
        builder.append(doubleArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(doubleArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.0f);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append(2.0f);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.0f, 2.0f};
        builder.append(floatArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(floatArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append(2);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2};
        builder.append(intArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(intArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append(2L);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1L, 2L};
        builder.append(longArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(longArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object object = new Object();
        builder.append(object);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(object);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object[] objectArray = {new Object(), new Object()};
        builder.append(objectArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(objectArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        int hashCode = builder.toHashCode();
        builder = new HashCodeBuilder();
        builder.append((short) 2);
        int hashCode2 = builder.toHashCode();
        Assert.assertNotSame(hashCode, hashCode2);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {(short) 1, (short) 2};
        builder.append(shortArray);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.append(shortArray);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 123;
        builder.appendSuper(superHashCode);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.appendSuper(superHashCode);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        class TestClass {
            private int privateField = 1;
            private transient int transientField = 2;
        }

        HashCodeBuilder builder = new HashCodeBuilder();
        TestClass testClass = new TestClass();
        builder.appendSuper(testClass.hashCode());
        builder.append(testClass.getClass(), testClass, true, null, new String[0]);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.appendSuper(testClass.hashCode());
        anotherBuilder.append(testClass.getClass(), testClass, true, null, new String[0]);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        class TestClass {
            private int privateField = 1;
            private String excludedField = "excluded";
        }

        HashCodeBuilder builder = new HashCodeBuilder();
        TestClass testClass = new TestClass();
        builder.appendSuper(testClass.hashCode());
        builder.append(testClass.getClass(), testClass, false, null, new String[]{"excludedField"});
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.appendSuper(testClass.hashCode());
        anotherBuilder.append(testClass.getClass(), testClass, false, null, new String[]{"excludedField"});
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }

    @Test
    public void testReflectionHashCodeWithRegisteredObjects() {
        class TestClass {
            private int privateField = 1;
        }

        HashCodeBuilder builder = new HashCodeBuilder();
        TestClass testClass1 = new TestClass();
        TestClass testClass2 = new TestClass();
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        HashCodeBuilder.register(testClass1);
        HashCodeBuilder.register(testClass2);
        builder.appendSuper(0);
        builder.append(TestClass.class, testClass1, false, null, new String[0]);
        builder.append(TestClass.class, testClass2, false, null, new String[0]);
        int hashCode = builder.toHashCode();
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        anotherBuilder.appendSuper(0);
        anotherBuilder.append(TestClass.class, testClass1, false, null, new String[0]);
        anotherBuilder.append(TestClass.class, testClass2, false, null, new String[0]);
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
        HashCodeBuilder.unregister(testClass1);
        HashCodeBuilder.unregister(testClass2);
    }

    @Test(expected = InternalError.class)
    public void testReflectionHashCodeWithIllegalAccessException() {
        class TestClass {
            private int privateField = 1;

            private int getPrivateField() {
                return privateField;
            }
        }

        HashCodeBuilder builder = new HashCodeBuilder();
        TestClass testClass = new TestClass();
        builder.appendSuper(testClass.hashCode());
        builder.append(TestClass.class, testClass, false, null, new String[0]);
        try {
            FieldUtils.getField(TestClass.class, "privateField", true);
            builder.append(testClass, 0);
            int hashCode = builder.toHashCode();
        } catch (IllegalAccessException e) {
            throw new InternalError("Unexpected InternalError", e);
        }
        HashCodeBuilder anotherBuilder = new HashCodeBuilder();
        int hashCodeAnother = anotherBuilder.toHashCode();
        Assert.assertNotSame(hashCode, hashCodeAnother);
    }
}