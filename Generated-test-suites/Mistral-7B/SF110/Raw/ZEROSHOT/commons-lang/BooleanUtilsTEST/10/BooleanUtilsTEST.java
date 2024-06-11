package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;

import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @org.junit.Test
    public void testNegate() {
        Boolean actual = BooleanUtils.negate(Boolean.TRUE);
        Assert.assertEquals(Boolean.FALSE, actual);

        actual = BooleanUtils.negate(Boolean.FALSE);
        Assert.assertEquals(Boolean.TRUE, actual);

        actual = BooleanUtils.negate(null);
        Assert.assertNull(actual);
    }

    @org.junit.Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @org.junit.Test
    public void testIsNotTrue() {
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @org.junit.Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @org.junit.Test
    public void testIsNotFalse() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @org.junit.Test
    public void testToBooleanWithInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        Assert.assertFalse(BooleanUtils.toBoolean(-1));
    }

    @org.junit.Test
    public void testToBooleanObjectWithInt() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        Assert.assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @org.junit.Test
    public void testToBooleanWithIntTrueValueFalseValue() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Integer did not match either specified value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithIntTrueValueFalseValueNullValue() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 0));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Integer did not match any specified value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @org.junit.Test
    public void testToIntegerObjectWithNullableBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @org.junit.Test
    public void testToIntegerWithBoolean() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @org.junit.Test
    public void testToIntegerWithBooleanTrueValueFalseValue() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
        try {
            BooleanUtils.toInteger(null, 1, 0);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToBooleanObjectString() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @org.junit.Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToStringWithStringTrueStringFalseString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @org.junit.Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[] {true, true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[] {true, true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[] {false, false, false}));
        try {
            BooleanUtils.and(new boolean[] {true, true, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.and(new boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testAndWithBooleanArray() {
        Assert.assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.and(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[] {true, true, true, true}));
        Assert.assertTrue(BooleanUtils.or(new boolean[] {true, true, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[] {false, false, false}));
        try {
            BooleanUtils.or(new boolean[] {true, true, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.or(new boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testOrWithBooleanArray() {
        Assert.assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.or(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[] {true, true, true, true}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[] {true, true, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[] {false, false, false}));
        try {
            BooleanUtils.xor(new boolean[] {true, true, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.xor(new boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }

    @org.junit.Test
    public void testXorWithBooleanArray() {
        Assert.assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertTrue(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
        try {
            BooleanUtils.xor(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Array is empty", e.getMessage());
        }
    }
}