package org.apache.commons.lang3.builder; //import statements go here

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.ParameterizedRunner;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@RunWith(ParameterizedRunner.class)
public class HashCodeBuilderTest {

    private static HashCodeBuilder hashCodeBuilder;
    private final Object object;
    private final int expectedHashCode;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new SimpleClass(), 42},
                new Object[]{new SimpleClassWithArrays(), 54},
                new Object[]{new SimpleClassWithTransient(), 45},
                new Object[]{new SimpleClassWithExcludedFields(), 39},
                new Object[]{null, Integer.MIN_VALUE}
        });
    }

    public HashCodeBuilderTest(Object object, int expectedHashCode) {
        this.object = object;
        this.expectedHashCode = expectedHashCode;
    }

    @BeforeClass
    public static void setUp() {
        hashCodeBuilder = new HashCodeBuilder();
    }

    @Test
    public void testReflectionHashCode() {
        int actualHashCode = HashCodeBuilder.reflectionHashCode(this.object, false);
        org.junit.Assert.assertEquals(this.expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37, false, null, new String[0]);
    }

    @Test
    public void testAppendBoolean() {
        hashCodeBuilder.append(true);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(1, actualHashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] booleanArray = {true, false, true};
        hashCodeBuilder.append(booleanArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 + ArrayUtils.hashCode(booleanArray);
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendByte() {
        hashCodeBuilder.append((byte) 1);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(1, actualHashCode);
    }

    @Test
    public void testAppendByteArray() {
        byte[] byteArray = {1, 2, 3};
        hashCodeBuilder.append(byteArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 + ArrayUtils.hashCode(byteArray);
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendChar() {
        hashCodeBuilder.append('a');
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(32 * 37 + 'a', actualHashCode);
    }

    @Test
    public void testAppendCharArray() {
        char[] charArray = {'a', 'b', 'c'};
        hashCodeBuilder.append(charArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 + ArrayUtils.hashCode(charArray);
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendDouble() {
        double value = 3.14159265;
        hashCodeBuilder.append(value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        long doubleBits = Double.doubleToLongBits(value);
        int expectedHashCode = 32 * 37 + (int) (doubleBits ^ doubleBits >> 32);
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        double[] doubleArray = {1.1, 2.2, 3.3};
        hashCodeBuilder.append(doubleArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * doubleArray.length;
        for (double d : doubleArray) {
            long doubleBits = Double.doubleToLongBits(d);
            expectedHashCode += 32 * 37 + (int) (doubleBits ^ doubleBits >> 32);
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendFloat() {
        float value = 3.14159265f;
        hashCodeBuilder.append(value);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int floatBits = Float.floatToIntBits(value);
        org.junit.Assert.assertEquals(32 * 37 + floatBits, actualHashCode);
    }

    @Test
    public void testAppendFloatArray() {
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        hashCodeBuilder.append(floatArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * floatArray.length;
        for (float f : floatArray) {
            int floatBits = Float.floatToIntBits(f);
            expectedHashCode += 32 * 37;
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendInt() {
        hashCodeBuilder.append(1);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(1, actualHashCode);
    }

    @Test
    public void testAppendIntArray() {
        int[] intArray = {1, 2, 3};
        hashCodeBuilder.append(intArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * intArray.length;
        for (int i : intArray) {
            expectedHashCode += 32 * 37 + i;
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendLong() {
        hashCodeBuilder.append(1L);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(1, actualHashCode);
    }

    @Test
    public void testAppendLongArray() {
        long[] longArray = {1, 2, 3};
        hashCodeBuilder.append(longArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * longArray.length;
        for (long l : longArray) {
            expectedHashCode += 32 * 37 + (int) (l ^ l >> 32);
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendObject() {
        String string = "test";
        hashCodeBuilder.append(string);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(string.hashCode(), actualHashCode);
    }

    @Test
    public void testAppendObjectArray() {
        String[] stringArray = {"test1", "test2", "test3"};
        hashCodeBuilder.append(stringArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * stringArray.length;
        for (String s : stringArray) {
            expectedHashCode += 32 * 37 + s.hashCode();
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendShort() {
        hashCodeBuilder.append((short) 1);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(1, actualHashCode);
    }

    @Test
    public void testAppendShortArray() {
        short[] shortArray = {1, 2, 3};
        hashCodeBuilder.append(shortArray);
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = 32 * 37 * shortArray.length;
        for (short s : shortArray) {
            expectedHashCode += 32 * 37 + s;
        }
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testAppendSuperHashCode() {
        hashCodeBuilder.appendSuper(12345);
        int actualHashCode = hashCodeBuilder.toHashCode();
        org.junit.Assert.assertEquals(12345, actualHashCode);
    }

    @Test
    public void testIsRegistered() {
        Object testObject = new SimpleClass();
        hashCodeBuilder.register(testObject);
        boolean isRegistered = HashCodeBuilder.isRegistered(testObject);
        org.junit.Assert.assertTrue(isRegistered);
    }

    @Test
    public void testIsRegisteredWithNullValue() {
        boolean isRegistered = HashCodeBuilder.isRegistered(null);
        org.junit.Assert.assertFalse(isRegistered);
    }

    @Test
    public void testReflectionAppend() throws IllegalAccessException {
        Object testObject = new SimpleClassWithFields();
        Class<?> clazz = testObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        hashCodeBuilder.register(testObject);
        for (Field field : fields) {
            Object fieldValue = field.get(testObject);
            hashCodeBuilder.append(fieldValue);
        }
        int actualHashCode = hashCodeBuilder.toHashCode();
        int expectedHashCode = this.expectedHashCode;
        org.junit.Assert.assertEquals(expectedHashCode, actualHashCode);
        hashCodeBuilder.unregister(testObject);
    }

    private static class SimpleClass {
    }

    private static class SimpleClassWithArrays {
        boolean[] boolArray;
        byte[] byteArray;
        char[] charArray;
        double[] doubleArray;
        float[] floatArray;
        int[] intArray;
        long[] longArray;
        short[] shortArray;
    }

    private static class SimpleClassWithTransient {
        @SuppressWarnings("unused")
        private transient int transientInt;
    }

    private static class SimpleClassWithExcludedFields {
        @SuppressWarnings("unused")
        private static final String EXCLUDED_FIELD = "EXCLUDED_FIELD";
        private int excludedField;
    }
}