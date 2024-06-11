package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testConstructor() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertNotNull(builder);

        HashCodeBuilder builderWithParams = new HashCodeBuilder(17, 31);
        assertNotNull(builderWithParams);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expectedHashCode = 32;
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(true);
        expectedHashCode = 31 * 17 + 1;
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);

        int expectedHashCode = 32 * 3 + 1; // 1 for true, 0 for false
        for (boolean bool : booleanArray) {
            expectedHashCode += bool ? 0 : 1;
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 5);
        int expectedHashCode = 32 + 5;
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append((byte) 5);
        expectedHashCode = 31 * 17 + 5;
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);

        int expectedHashCode = 32 * 3;
        for (byte byteValue : byteArray) {
            expectedHashCode += byteValue;
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int expectedHashCode = 32 + ('a' << 6); // Unicode value of 'a' is 97
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append('a');
        expectedHashCode = 31 * 17 + ('a' << 6);
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);

        int expectedHashCode = 32 * 3;
        for (char charValue : charArray) {
            expectedHashCode += charValue;
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double doubleValue = 123.45;
        builder.append(doubleValue);

        long doubleValueBits = Double.doubleToLongBits(doubleValue);
        int expectedHashCode = 32 * 37; // 37 is the multiplier in HashCodeBuilder
        expectedHashCode += (int) (doubleValueBits >>> 32);
        expectedHashCode += (int) (doubleValueBits & 0xFFFFFFFFL);

        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(doubleValue);
        expectedHashCode = 31 * 17 + 32 * 37 + (int) (doubleValueBits >>> 32) + (int) (doubleValueBits & 0xFFFFFFFFL);
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);

        int expectedHashCode = 32 * 3;
        for (double doubleValue : doubleArray) {
            expectedHashCode += Double.doubleToIntBits(doubleValue);
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float floatValue = 123.45f;
        builder.append(floatValue);

        int expectedHashCode = 32 * 37; // 37 is the multiplier in HashCodeBuilder
        expectedHashCode += Float.floatToIntBits(floatValue);

        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(floatValue);
        expectedHashCode = 31 * 17 + 32 * 37 + Float.floatToIntBits(floatValue);
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);

        int expectedHashCode = 32 * 3;
        for (float floatValue : floatArray) {
            expectedHashCode += Float.floatToIntBits(floatValue);
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int intValue = 123;
        builder.append(intValue);
        int expectedHashCode = 32 * 37 + intValue;
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(intValue);
        expectedHashCode = 31 * 17 + 32 * 37 + intValue;
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);

        int expectedHashCode = 32 * 3;
        for (int intValue : intArray) {
            expectedHashCode += intValue;
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long longValue = 1234567890123456789L;
        builder.append(longValue);

        int expectedHashCode = 32 * 37; // 37 is the multiplier in HashCodeBuilder
        expectedHashCode += (int) (longValue >>> 32);
        expectedHashCode += (int) (longValue & 0xFFFFFFFFL);

        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(longValue);
        expectedHashCode = 31 * 17 + 32 * 37 + (int) (longValue >>> 32) + (int) (longValue & 0xFFFFFFFFL);
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);

        int expectedHashCode = 32 * 3;
        for (long longValue : longArray) {
            expectedHashCode += (int) (longValue >>> 32);
            expectedHashCode += (int) (longValue & 0xFFFFFFFFL);
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String stringObject = "testString";
        builder.append(stringObject);

        int expectedHashCode = 32 * 37 + stringObject.hashCode();
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(stringObject);
        expectedHashCode = 31 * 17 + 32 * 37 + stringObject.hashCode();
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String[] stringArray = {"testString1", "testString2", "testString3"};
        builder.append(stringArray);

        int expectedHashCode = 32 * 3;
        for (String stringValue : stringArray) {
            expectedHashCode += stringValue.hashCode();
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short shortValue = (short) 123;
        builder.append(shortValue);
        int expectedHashCode = 32 * 37 + shortValue;
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.append(shortValue);
        expectedHashCode = 31 * 17 + 32 * 37 + shortValue;
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);

        int expectedHashCode = 32 * 3;
        for (short shortValue : shortArray) {
            expectedHashCode += shortValue;
        }

        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 789;
        builder.appendSuper(superHashCode);

        int expectedHashCode = 32 * 37 + superHashCode;
        assertEquals(expectedHashCode, builder.toHashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(17, 31);
        builderWithInitialValue.appendSuper(superHashCode);
        expectedHashCode = 31 * 17 + 32 * 37 + superHashCode;
        assertEquals(expectedHashCode, builderWithInitialValue.toHashCode());
    }

    @Test
    public void testReflectionHashCodeWithInitialNonZeroOddNumberAndMultiplierNonZeroOddNumber() {
        Object objectWithHashCode = new Object() {
            @Override
            public int hashCode() {
                return 123456789;
            }
        };

        int expectedHashCode = 17 * 31 + 32 * 37 + objectWithHashCode.hashCode();
        int resultHashCode = HashCodeBuilder.reflectionHashCode(objectWithHashCode, false, null, false, null, new String[0]);
        assertEquals(expectedHashCode, resultHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, false, null, false, null, new String[0]);
    }

    @Test
    public void testReflectionHashCodeWithTestTransientsFalse() {
        Object objectWithTransientField = new Object() {
            @Transient
            int transientField;

            int nonTransientField = 123;
        };

        int expectedHashCode = 17 * 31 * 2;
        int resultHashCode = HashCodeBuilder.reflectionHashCode(objectWithTransientField, false, null, false, null, new String[0]);
        assertEquals(expectedHashCode, resultHashCode);
    }

    @Test
    public void testReflectionHashCodeWithTestTransientsTrue() {
        Object objectWithTransientField = new Object() {
            @Transient
            int transientField;

            int nonTransientField = 123;
        };

        int expectedHashCode = 17 * 31 * 3;
        int resultHashCode = HashCodeBuilder.reflectionHashCode(objectWithTransientField, true, null, false, null, new String[0]);
        assertEquals(expectedHashCode, resultHashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludeFields() {
        Object objectWithExcludedFields = new Object() {
            int field1 = 1;
            @SuppressWarnings("unused")
            int field2 = 2;
            transient int field3 = 3;
            int field4 = 4;
            private int field5 = 5;

            void method() {}
        };

        int expectedHashCode = 17 * 31 * 3;
        int resultHashCode = HashCodeBuilder.reflectionHashCode(objectWithExcludedFields, false, null, false, new String[]{"field2"}, new String[0]);
        assertEquals(expectedHashCode, resultHashCode);
    }

    @Test
    public void testIsRegistered() {
        Object objectToRegister = new Object();
        assertFalse(HashCodeBuilder.isRegistered(objectToRegister));

        HashCodeBuilder.reflectionAppend(objectToRegister, objectToRegister.getClass(), new HashCodeBuilder(), false, new String[0]);
        assertTrue(HashCodeBuilder.isRegistered(objectToRegister));

        HashCodeBuilder.unregister(objectToRegister);
        assertFalse(HashCodeBuilder.isRegistered(objectToRegister));
    }

    @Test
    public void testGetRegistryIsEmpty() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertTrue(registry.isEmpty());
    }

    @Test
    public void testGetRegistryIsNotEmpty() {
        Object objectToRegister = new Object();
        HashCodeBuilder.reflectionAppend(objectToRegister, objectToRegister.getClass(), new HashCodeBuilder(), false, new String[0]);

        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertFalse(registry.isEmpty());
        assertTrue(registry.contains(new IDKey(objectToRegister)));
    }

}