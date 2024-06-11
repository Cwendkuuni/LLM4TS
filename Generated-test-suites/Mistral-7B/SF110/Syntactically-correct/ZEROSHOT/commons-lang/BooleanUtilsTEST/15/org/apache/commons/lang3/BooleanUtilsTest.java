package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        Assert.assertFalse(BooleanUtils.toBoolean((Object) 0));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithInt_IllegalArgumentException() {
        BooleanUtils.toBoolean(2);
    }

    @Test
    public void testToBooleanWithIntValues() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        Assert.assertNull(BooleanUtils.toBoolean(null, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntValues_IllegalArgumentException() {
        BooleanUtils.toBoolean(1, 2, 0);
    }

    @Test
    public void testToBooleanObjectWithIntValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntValues_IllegalArgumentException() {
        BooleanUtils.toBooleanObject(1, 1, null, 0);
    }

    @Test
    public void testToBooleanObjectWithNullableIntValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, null, null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, null, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, null, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        Assert.assertEquals(0, BooleanUtils.toInteger(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBoolean() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(null, 1, 0));
    }

    @Test
    public void testToIntegerWithValues() {
        Assert.assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(null, 1, 0));
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
    public void testToStringWithValues() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanAndString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "false"));
        Assert.assertNull(BooleanUtils.toString(null, "true"));
    }

    @Test
    public void testToStringWithBooleanAndStrings() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
        Assert.assertNull(BooleanUtils.toString(null, "true", "false", null));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true, true, true, true));
        assertFalse(BooleanUtils.and(true, true, false, true, true, true));
        assertFalse(BooleanUtils.and(true, true, true, false, true, true));
        assertFalse(BooleanUtils.and(true, true, true, true, false, true));
        assertFalse(BooleanUtils.and(true, true, true, true, true, false));
        assertFalse(BooleanUtils.and(true, true, true, true, false, false));
        assertFalse(BooleanUtils.and(true, true, false, false, false, false));
        assertFalse(BooleanUtils.and(false, false, false, false, false, false));
        try {
            BooleanUtils.and(null, true, true, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.and(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true, true, true, true));
        assertTrue(BooleanUtils.or(true, true, false, true, true, true));
        assertTrue(BooleanUtils.or(true, true, true, false, true, true));
        assertTrue(BooleanUtils.or(true, true, true, true, false, true));
        assertTrue(BooleanUtils.or(true, true, true, true, true, false));
        assertTrue(BooleanUtils.or(true, true, true, true, false, false));
        assertTrue(BooleanUtils.or(true, false, false, false, false, false));
        assertTrue(BooleanUtils.or(false, false, false, false, false, false));
        try {
            BooleanUtils.or(null, true, true, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.or(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true, true, true, true, true));
        assertFalse(BooleanUtils.xor(true, true, false, true, true, true));
        assertFalse(BooleanUtils.xor(true, true, true, false, true, true));
        assertFalse(BooleanUtils.xor(true, true, true, true, false, true));
        assertFalse(BooleanUtils.xor(true, true, true, true, true, false));
        assertFalse(BooleanUtils.xor(true, true, true, true, false, false));
        assertFalse(BooleanUtils.xor(true, false, false, false, false, false));
        assertFalse(BooleanUtils.xor(false, false, false, false, false, false));
        try {
            BooleanUtils.xor(null, true, true, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.xor(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}