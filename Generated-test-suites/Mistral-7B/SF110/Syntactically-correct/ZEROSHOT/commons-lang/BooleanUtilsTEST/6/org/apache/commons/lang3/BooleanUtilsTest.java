package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean((Object) 0));
        Assert.assertTrue(BooleanUtils.toBoolean((Object) 1));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    @Test
    public void testToBooleanWithInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        Assert.assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithInts() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        Assert.assertNull(BooleanUtils.toBoolean(null, 1, 0));
    }

    @Test
    public void testToBooleanObjectWithIntegers() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        Assert.assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
    }

    @Test
    public void testToBooleanObjectWithIntsAndNullValue() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(1, 1, 0, 0));
        Assert.assertFalse(BooleanUtils.toBooleanObject(0, 1, 0, 0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 0));
    }

    @Test
    public void testToBooleanObjectWithIntegersAndNullValue() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
        Assert.assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
    }

    @Test
    public void testToBooleanWithString() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithString() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithStrings() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithStringsAndSpecifiedValues() {
        Assert.assertTrue(BooleanUtils.toBoolean("yes"));
        Assert.assertFalse(BooleanUtils.toBoolean("no"));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithStringsAndSpecifiedValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("yes"));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("no"));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(true));
        Assert.assertEquals(0, BooleanUtils.toInteger(false));
        Assert.assertNull(BooleanUtils.toInteger(null));
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(true));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(false));
        Assert.assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(true));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(false));
        Assert.assertNull(BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(true));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(false));
        Assert.assertNull(BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("true", BooleanUtils.toString(true, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(false, "true", "false"));
        Assert.assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithDefaultValues() {
        Assert.assertEquals("true", BooleanUtils.toString(true, "true", "false", "unknown"));
        Assert.assertEquals("false", BooleanUtils.toString(false, "true", "false", "unknown"));
        Assert.assertEquals("unknown", BooleanUtils.toString(null, "true", "false", "unknown"));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(true, true, true, true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, true, true, true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.and(BooleanUtils.toBooleanArray(new Boolean[]{true, false, null})));
        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAndWithBooleans() {
        Boolean[] booleans = new Boolean[]{true, true, true, true, true, true, true, true};
        Assert.assertTrue(BooleanUtils.and(BooleanUtils.toBooleanArray(booleans)));
        boolean[] primitives = ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(new Boolean[]{true, true, true, false, null}));
        Assert.assertFalse(BooleanUtils.and(primitives));
        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(true, true, true, true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.or(true, true, true, true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.or(BooleanUtils.toBooleanArray(new Boolean[]{false, false, null})));
        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOrWithBooleans() {
        Boolean[] booleans = new Boolean[]{true, true, true, true, true, true, true, true};
        Assert.assertTrue(BooleanUtils.or(BooleanUtils.toBooleanArray(booleans)));
        boolean[] primitives = ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(new Boolean[]{false, false, false, false, false, false, false, false}));
        Assert.assertFalse(BooleanUtils.or(primitives));
        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testXor() {
        Assert.assertTrue(BooleanUtils.xor(true, true, true, true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.xor(true, true, true, true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanArray(new Boolean[]{true, false, null})));
        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testXorWithBooleans() {
        Boolean[] booleans = new Boolean[]{true, true, true, true, true, true, true, true};
        Assert.assertTrue(BooleanUtils.xor(BooleanUtils.toBooleanArray(booleans)));
        boolean[] primitives = ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(new Boolean[]{true, false, true, false, true, false, true, false}));
        Assert.assertEquals(2, Integer.bitCount(Byte.toUnsignedInt(~ArrayUtils.toPrimitive(BooleanUtils.toBooleanArray(new Boolean[]{false, false, false, false, false, false, false, false})))));
        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}