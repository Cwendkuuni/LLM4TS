package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.MathUtils;
import org.apache.commons.lang3.NullPointerException;
import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.isTrue(bool), bool != null && bool.booleanValue());
        }
    }

    @Test
    public void testIsNotTrue() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.isNotTrue(bool), !BooleanUtils.isTrue(bool));
        }
    }

    @Test
    public void testIsFalse() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.isFalse(bool), !bool != null && !bool.booleanValue());
        }
    }

    @Test
    public void testIsNotFalse() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.isNotFalse(bool), !BooleanUtils.isFalse(bool));
        }
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        Assert.assertTrue(BooleanUtils.toBoolean(42));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
    }

    @Test
    public void testToBooleanWithBoolean() {
        for (int i = 0; i < 16; i++) {
            Boolean bool = Boolean.valueOf((i & 1) == 1 ? "true" : "false");
            Assert.assertEquals(BooleanUtils.toBoolean(bool), bool.booleanValue());
        }
    }

    @Test
    public void testToBooleanWithInteger() {
        try {
            BooleanUtils.toBoolean(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObject() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
        try {
            BooleanUtils.toBooleanObject("-1");
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1));
    }

    @Test
    public void testToBooleanObjectWithIntegerNullValue() {
        try {
            BooleanUtils.toBooleanObject(1, 1, 0, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 1, null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 1, null));
    }

    @Test
    public void testToInteger() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.toInteger(bool), BooleanUtils.toBoolean(bool) ? 1 : 0);
        }
    }

    @Test
    public void testToIntegerObject() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBoolean() {
        for (Boolean bool : new Boolean[]{null, Boolean.TRUE, Boolean.FALSE}) {
            Assert.assertEquals(BooleanUtils.toInteger(bool), BooleanUtils.toBoolean(bool) ? 1 : 0);
        }
    }

    @Test
    public void testToIntegerWithBooleanAndDefaultValues() {
        for (int i = 0; i < 16; i++) {
            Boolean bool = Boolean.valueOf((i & 1) == 1 ? "true" : "false");
            Assert.assertEquals(BooleanUtils.toInteger(bool, 1, 0), (i & 1) == 1 ? 1 : 0);
        }
    }

    @Test
    public void testToIntegerWithBooleanAndNullDefaultValue() {
        Assert.assertEquals(0, BooleanUtils.toInteger(null, null, null));
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, null, null));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, null, null));
    }

    @Test
    public void testToIntegerWithBooleanArray() {
        boolean[] b = {true, false, true, false};
        int[] expected = {1, 0, 1, 0};
        int[] result = BooleanUtils.toInteger(b);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToBooleanArray() {
        int[] ints = {1, 0, 1, 0};
        boolean[] expected = {true, false, true, false};
        boolean[] result = BooleanUtils.toBooleanArray(ints);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToBooleanWithByte() {
        for (byte b : new byte[]{-1, 0, 1}) {
            Assert.assertEquals(BooleanUtils.toBoolean(Byte.valueOf(b)), ByteUtils.toBooleanObject(b) != null);
        }
    }

    @Test
    public void testToBooleanWithShort() {
        for (short s : new short[]{-1, 0, 1}) {
            Assert.assertEquals(BooleanUtils.toBoolean(Short.valueOf(s)), ShortUtils.toBooleanObject(s) != null);
        }
    }

    @Test
    public void testToBooleanWithCharacter() {
        for (char c : new char[]{'\u0000', '\u0001', '\uffff'}) {
            Assert.assertEquals(BooleanUtils.toBoolean(Character.valueOf(c)), Character.isDefined(c) ? Character.isDefined(c) : false);
        }
    }

    @Test
    public void testToBooleanWithByteArray() {
        byte[] byteArray = new byte[]{-1, 0, 1};
        Boolean[] expectedBooleanArray = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        Boolean[] resultBooleanArray = BooleanUtils.toBooleanArray(byteArray);
        Assert.assertArrayEquals(expectedBooleanArray, resultBooleanArray);
    }

    @Test
    public void testToBooleanWithShortArray() {
        short[] shortArray = new short[]{-1, 0, 1};
        Boolean[] expectedBooleanArray = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        Boolean[] resultBooleanArray = BooleanUtils.toBooleanArray(shortArray);
        Assert.assertArrayEquals(expectedBooleanArray, resultBooleanArray);
    }

    @Test
    public void testToBooleanWithCharacterArray() {
        char[] charArray = new char[]{'a', '\u0000', '\u0001'};
        Boolean[] expectedBooleanArray = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean[] resultBooleanArray = BooleanUtils.toBooleanArray(charArray);
        Assert.assertArrayEquals(expectedBooleanArray, resultBooleanArray);
    }

    @Test
    public void testToBooleanWithByteBuffer() {
        byte[] byteArray = MathUtils.byteArrayFromLong(0x1234567890ABCDEF);
        Boolean[] expectedBooleanArray = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean[] resultBooleanArray = BooleanUtils.toBooleanArray(ByteBuffer.wrap(byteArray));
        Assert.assertArrayEquals(expectedBooleanArray, resultBooleanArray);
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, true, false, true, true));
        try {
            BooleanUtils.and(true, true, null, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.and(new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAndWithBooleanArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.and(b));
        try {
            BooleanUtils.and(new boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAndWithBooleanPrimitiveArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.and(ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(b))));
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.or(true, true, false, true, true));
        try {
            BooleanUtils.or(true, true, null, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.or(new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testOrWithBooleanArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.or(b));
        try {
            BooleanUtils.or(new boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOrWithBooleanPrimitiveArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.or(ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(b))));
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(true, true));
        Assert.assertTrue(BooleanUtils.xor(true, false));
        Assert.assertFalse(BooleanUtils.xor(true, true));
        try {
            BooleanUtils.xor(null, true, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.xor(new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testXorWithBooleanArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.xor(b) ^ Boolean.TRUE.booleanValue());
    }

    @Test
    public void testXorWithBooleanPrimitiveArray() {
        boolean[] b = {true, false, true, false, true, false, true, false};
        Assert.assertTrue(BooleanUtils.xor(ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(b))) ^ Boolean.TRUE.booleanValue());
    }
}