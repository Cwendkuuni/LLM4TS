package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertTrue(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithIntValue() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithIntValue() {
        assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE) == Boolean.TRUE);
        assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO) == Boolean.TRUE);
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithIntValuesAndDefaultValue() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0) == true);
        assertFalse(BooleanUtils.toBoolean(0, 1, 0) == true);
        assertFalse(BooleanUtils.toBoolean(-1, 1, 0) == true);
    }

    @Test
    public void testToBooleanObjectWithIntValuesAndDefaultValue() {
        assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null) == Boolean.TRUE);
        assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null) == Boolean.TRUE);
        assertNull(BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertTrue(BooleanUtils.toBooleanObject("true") == Boolean.TRUE);
        assertFalse(BooleanUtils.toBooleanObject("false") == Boolean.TRUE);
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithStringsAndNullValue() {
        assertTrue(BooleanUtils.toBooleanObject("true", "true", "false", null) == Boolean.TRUE);
        assertFalse(BooleanUtils.toBooleanObject("false", "true", "false", null) == Boolean.TRUE);
        assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true") == true);
        assertFalse(BooleanUtils.toBoolean("false") == true);
        assertFalse(BooleanUtils.toBoolean(null) == true);
    }

    @Test
    public void testToBooleanWithStringsAndDefaultValue() {
        assertTrue(BooleanUtils.toBoolean("true", "true", "false") == true);
        assertFalse(BooleanUtils.toBoolean("false", "true", "false") == true);
        assertFalse(BooleanUtils.toBoolean(null, "true", "false") == true);
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertNull(BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertNull(BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithTrueString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true"));
        assertNull(BooleanUtils.toString(Boolean.FALSE, "true"));
        assertNull(BooleanUtils.toString(null, "true"));
    }

    @Test
    public void testToStringWithFalseString() {
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "false"));
        assertNull(BooleanUtils.toString(Boolean.TRUE, "false"));
        assertNull(BooleanUtils.toString(null, "false"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(new boolean[] {true, true, true}));
        assertFalse(BooleanUtils.and(new boolean[] {true, false, false}));
        assertFalse(BooleanUtils.and(new boolean[0]));
        try {
            BooleanUtils.and(new boolean[] {true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanObjects() {
        assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[0]));
        try {
            BooleanUtils.and(new Boolean[] {Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(new boolean[] {true, true, true}));
        assertFalse(BooleanUtils.or(new boolean[] {true, false, false}));
        assertFalse(BooleanUtils.or(new boolean[0]));
        try {
            BooleanUtils.or(new boolean[] {true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanObjects() {
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(new Boolean[0]));
        try {
            BooleanUtils.or(new Boolean[] {Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(new boolean[] {true, false, true}));
        assertFalse(BooleanUtils.xor(new boolean[] {true, true, true}));
        assertFalse(BooleanUtils.xor(new boolean[0]));
        try {
            BooleanUtils.xor(new boolean[] {true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanObjects() {
        assertTrue(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[0]));
        try {
            BooleanUtils.xor(new Boolean[] {Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}