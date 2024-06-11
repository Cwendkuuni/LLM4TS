package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expectedHashCode = 37 * 17 + 1;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);

        int expectedHashCode = 17 * 37 * 3 + 1 + 0 + 1;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);
        int expectedHashCode = 37 * 17 + 1;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {1, 2, 3};
        builder.append(byteArray);

        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('a');
        int expectedHashCode = 37 * 17 + 'a';
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'a', 'b', 'c'};
        builder.append(charArray);

        int expectedHashCode = 17 * 37 * 3 + 'a' + 'b' + 'c';
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double doubleValue = 3.14;
        builder.append(doubleValue);

        long doubleBits = Double.doubleToLongBits(doubleValue);
        int expectedHashCode = 37 * 17 + (int)(doubleBits ^ doubleBits >> 32);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.1, 2.2, 3.3};
        builder.append(doubleArray);

        for (double doubleValue : doubleArray) {
            long doubleBits = Double.doubleToLongBits(doubleValue);
            int hashCode = 37 * 17 + (int)(doubleBits ^ doubleBits >> 32);
            builder.iTotal *= 37;
            builder.iTotal += hashCode;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float floatValue = 3.14f;
        builder.append(floatValue);

        int expectedHashCode = 37 * 17 + Float.floatToIntBits(floatValue);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        builder.append(floatArray);

        for (float floatValue : floatArray) {
            int hashCode = 37 * 17 + Float.floatToIntBits(floatValue);
            builder.iTotal *= 37;
            builder.iTotal += hashCode;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int intValue = 123;
        builder.append(intValue);

        int expectedHashCode = 37 * 17 + intValue;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);

        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long longValue = 123L;
        builder.append(longValue);

        int expectedHashCode = 37 * 17 + (int)(longValue ^ longValue >> 32);
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);

        for (long longValue : longArray) {
            int hashCode = 37 * 17 + (int)(longValue ^ longValue >> 32);
            builder.iTotal *= 37;
            builder.iTotal += hashCode;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object objects[] = {new Object(), new Object()};

        for (Object object : objects) {
            int hashCode = (object == null) ? 0 : object.hashCode();
            builder.append(object);
        }

        int expectedHashCode = 17 * 37 * 2 + 0 + hashCodeOfObject1 + hashCodeOfObject2;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        Object objects[] = {new Object(), new Object()};
        builder.append(objects);

        for (Object object : objects) {
            int hashCode = (object == null) ? 0 : object.hashCode();
            builder.iTotal *= 37;
            builder.iTotal += hashCode;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short shortValue = 123;
        builder.append(shortValue);

        int expectedHashCode = 37 * 17 + shortValue;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);

        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int superHashCode = 123;
        builder.appendSuper(superHashCode);

        int expectedHashCode = 37 * 17 + superHashCode;
        assertEquals(expectedHashCode, builder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeBoolean() {
        boolean booleanValue = true;
        int hashCode = HashCodeBuilder.reflectionHashCode(booleanValue);
        assertEquals(37 * 17 + 1, hashCode);
    }

    @Test
    public void testReflectionHashCodeBooleanArray() {
        boolean[] booleanArray = {true, false, true};
        int hashCode = HashCodeBuilder.reflectionHashCode(booleanArray, false, null, new String[0]);
        int expectedHashCode = 17 * 37 * 3 + 1 + 0 + 1;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeByte() {
        byte byteValue = 1;
        int hashCode = HashCodeBuilder.reflectionHashCode(byteValue);
        assertEquals(37 * 17 + 1, hashCode);
    }

    @Test
    public void testReflectionHashCodeByteArray() {
        byte[] byteArray = {1, 2, 3};
        int hashCode = HashCodeBuilder.reflectionHashCode(byteArray, false, null, new String[0]);
        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeChar() {
        char charValue = 'a';
        int hashCode = HashCodeBuilder.reflectionHashCode(charValue);
        assertEquals(37 * 17 + charValue, hashCode);
    }

    @Test
    public void testReflectionHashCodeCharArray() {
        char[] charArray = {'a', 'b', 'c'};
        int hashCode = HashCodeBuilder.reflectionHashCode(charArray, false, null, new String[0]);
        int expectedHashCode = 17 * 37 * 3 + 'a' + 'b' + 'c';
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeDouble() {
        double doubleValue = 3.14;
        int hashCode = HashCodeBuilder.reflectionHashCode(doubleValue);
        long doubleBits = Double.doubleToLongBits(doubleValue);
        assertEquals(37 * 17 + (int)(doubleBits ^ doubleBits >> 32), hashCode);
    }

    @Test
    public void testReflectionHashCodeDoubleArray() {
        double[] doubleArray = {1.1, 2.2, 3.3};
        int hashCode = HashCodeBuilder.reflectionHashCode(doubleArray, false, null, new String[0]);
        for (double doubleValue : doubleArray) {
            long doubleBits = Double.doubleToLongBits(doubleValue);
            int hashCodeFromDouble = 37 * 17 + (int)(doubleBits ^ doubleBits >> 32);
            builder.iTotal *= 37;
            builder.iTotal += hashCodeFromDouble;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testReflectionHashCodeFloat() {
        float floatValue = 3.14f;
        int hashCode = HashCodeBuilder.reflectionHashCode(floatValue);
        assertEquals(37 * 17 + Float.floatToIntBits(floatValue), hashCode);
    }

    @Test
    public void testReflectionHashCodeFloatArray() {
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        int hashCode = HashCodeBuilder.reflectionHashCode(floatArray, false, null, new String[0]);
        for (float floatValue : floatArray) {
            int hashCodeFromFloat = 37 * 17 + Float.floatToIntBits(floatValue);
            builder.iTotal *= 37;
            builder.iTotal += hashCodeFromFloat;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testReflectionHashCodeInt() {
        int intValue = 123;
        int hashCode = HashCodeBuilder.reflectionHashCode(intValue);
        assertEquals(37 * 17 + intValue, hashCode);
    }

    @Test
    public void testReflectionHashCodeIntArray() {
        int[] intArray = {1, 2, 3};
        int hashCode = HashCodeBuilder.reflectionHashCode(intArray, false, null, new String[0]);
        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeLong() {
        long longValue = 123L;
        int hashCode = HashCodeBuilder.reflectionHashCode(longValue);
        assertEquals(37 * 17 + (int)(longValue ^ longValue >> 32), hashCode);
    }

    @Test
    public void testReflectionHashCodeLongArray() {
        long[] longArray = {1, 2, 3};
        int hashCode = HashCodeBuilder.reflectionHashCode(longArray, false, null, new String[0]);
        for (long longValue : longArray) {
            int hashCodeFromLong = 37 * 17 + (int)(longValue ^ longValue >> 32);
            builder.iTotal *= 37;
            builder.iTotal += hashCodeFromLong;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testReflectionHashCodeObject() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertEquals(object.hashCode(), hashCode);
    }

    @Test
    public void testReflectionHashCodeObjectArray() {
        Object[] objectArray = {new Object(), new Object()};
        int hashCode = HashCodeBuilder.reflectionHashCode(objectArray, false, null, new String[0]);
        for (Object object : objectArray) {
            int hashCodeFromObject = object.hashCode();
            builder.iTotal *= 37;
            builder.iTotal += hashCodeFromObject;
        }
        assertEquals(builder.toHashCode(), builder.iTotal);
    }

    @Test
    public void testReflectionHashCodeShort() {
        short shortValue = 123;
        int hashCode = HashCodeBuilder.reflectionHashCode(shortValue);
        assertEquals(37 * 17 + shortValue, hashCode);
    }

    @Test
    public void testReflectionHashCodeShortArray() {
        short[] shortArray = {1, 2, 3};
        int hashCode = HashCodeBuilder.reflectionHashCode(shortArray, false, null, new String[0]);
        int expectedHashCode = 17 * 37 * 3 + 1 + 2 + 3;
        assertEquals(expectedHashCode, hashCode);
    }

}