package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.negate(null));
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
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithIntValues() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        Assert.assertNull(BooleanUtils.toBoolean(null, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Integer did not match either specified value", e.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 0));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, 0);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Integer did not match any specified value", e.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toInteger(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBooleanValue() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanValues() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, Integer.valueOf(1), Integer.valueOf(0)));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, Integer.valueOf(1), Integer.valueOf(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithInvalidString() {
        BooleanUtils.toBooleanObject("abc");
    }

    @Test
    public void testToBooleanObjectWithString() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithStringValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
        try {
            BooleanUtils.toBooleanObject("invalid", "true", "false", null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The String did not match any specified value", e.getMessage());
        }
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringWithBooleanValue() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanValues() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
        Assert.assertNull(BooleanUtils.toString(null, "true", "false", null));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(true, true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.and(false, true, true, true, false));
        Assert.assertFalse(BooleanUtils.and(false, false, false, false, false));
        try {
            BooleanUtils.and(null, true, true, true, false);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.and(new boolean[] {true, true, true, true, false, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArrays() {
        Assert.assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.and(new Boolean[] {null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(true, true, true, true, true));
        Assert.assertTrue(BooleanUtils.or(true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.or(false, true, true, true, false));
        Assert.assertFalse(BooleanUtils.or(false, false, false, false, false));
        try {
            BooleanUtils.or(null, true, true, true, false);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.or(new boolean[] {true, true, true, true, false, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArrays() {
        Assert.assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.or(new Boolean[] {null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(true, true, true, true, true));
        Assert.assertTrue(BooleanUtils.xor(true, true, true, true, false));
        Assert.assertFalse(BooleanUtils.xor(false, true, true, true, false));
        Assert.assertFalse(BooleanUtils.xor(false, false, false, false, false));
        try {
            BooleanUtils.xor(null, true, true, true, false);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.xor(new boolean[] {true, true, true, true, false, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArrays() {
        Assert.assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertTrue(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.xor(new Boolean[] {null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not be null", e.getMessage());
        }
        try {
            BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The array must not contain any null elements", e.getMessage());
        }
    }
}