package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.TRUE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanFromInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObjectFromInt() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
    }

    @Test
    public void testToBooleanFromInteger() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test
    public void testToBooleanFromIntegerWithNullValues() {
        Assert.assertNull(BooleanUtils.toBoolean(null, 1, 0, null));
        Assert.assertNull(BooleanUtils.toBoolean(1, null, 0, null));
        Assert.assertNull(BooleanUtils.toBoolean(1, 1, null, null));
        Assert.assertNull(BooleanUtils.toBoolean(1, 1, 0, null));
    }

    @Test
    public void testToBooleanObjectFromInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
        Assert.assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), null, Integer.valueOf(0), null));
        Assert.assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), null, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(true));
        Assert.assertEquals(0, BooleanUtils.toInteger(false));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithValue() {
        Assert.assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
    }

    @Test
    public void testToIntegerWithStrings() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
        Assert.assertNull(BooleanUtils.toBoolean(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean("invalid"));

        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
        Assert.assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanObject("invalid"));

        Assert.assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean(null, "true", "false"));
        Assert.assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean("true", null, "false"));

        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
        Assert.assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanObject("true", null, "false", null));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.and(true, false, true, true, true));
        Assert.assertFalse(BooleanUtils.and(false, true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(false, false, true, true, true));
        Assert.assertFalse(BooleanUtils.and(false, false, false, true, true));
        Assert.assertFalse(BooleanUtils.and(false, false, false, false, true));
        Assert.assertFalse(BooleanUtils.and(false, false, false, false, false));
    }

    @Test
    public void testAndWithBooleans() {
        Assert.assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testAndWithArrays() {
        boolean[] booleanArray = {true, true, true, true, true};
        Assert.assertTrue(BooleanUtils.and(booleanArray));

        boolean[] booleanArrayFalse = {true, true, true, true, false};
        Assert.assertFalse(BooleanUtils.and(booleanArrayFalse));

        boolean[] booleanArrayMixed = {true, false, true, true, true};
        Assert.assertFalse(BooleanUtils.and(booleanArrayMixed));

        boolean[] booleanArrayAllFalse = {false, false, false, false, false};
        Assert.assertFalse(BooleanUtils.and(booleanArrayAllFalse));
    }

    @Test
    public void testAndWithBooleanArrays() {
        Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Assert.assertTrue(BooleanUtils.and(booleanArray));

        Boolean[] booleanArrayFalse = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Assert.assertFalse(BooleanUtils.and(booleanArrayFalse));

        Boolean[] booleanArrayMixed = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Assert.assertFalse(BooleanUtils.and(booleanArrayMixed));

        Boolean[] booleanArrayAllFalse = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        Assert.assertFalse(BooleanUtils.and(booleanArrayAllFalse));
    }
}