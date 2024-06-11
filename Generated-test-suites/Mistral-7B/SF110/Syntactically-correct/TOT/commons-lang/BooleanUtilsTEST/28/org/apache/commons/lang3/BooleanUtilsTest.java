package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertNull(BooleanUtils.negate(null));
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
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
        try {
            BooleanUtils.toBoolean(Integer.valueOf(-1));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        assertTrue(BooleanUtils.toBoolean(new boolean[]{true, true, true, true}));
        assertFalse(BooleanUtils.toBoolean(new boolean[]{false, false, false, false}));
        assertFalse(BooleanUtils.toBoolean(new boolean[]{null, true, false, true}));
        try {
            BooleanUtils.toBoolean(new boolean[]{2, 3, 4, 5});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any non-boolean values", ex.getMessage());
        }
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true, true));
        assertFalse(BooleanUtils.and(true, true, false, false));
        assertFalse(BooleanUtils.and(null, true, false, true));
        try {
            BooleanUtils.and(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            //expected
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true, true));
        assertTrue(BooleanUtils.or(true, true, true, false));
        assertFalse(BooleanUtils.or(false, false, false, false));
        try {
            BooleanUtils.or(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            //expected
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, false, false, false));
        assertFalse(BooleanUtils.xor(true, true, false, false));
        assertFalse(BooleanUtils.xor(true, false, true, false));
        try {
            BooleanUtils.xor(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            //expected
        }
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
    public void testToStringWithDefaults() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE));
        assertNull(BooleanUtils.toString(null));
    }

    @Test
    public void testToStringWithTrueFalse() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithTrueFalseAndNull() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", "null"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", "null"));
        assertEquals("null", BooleanUtils.toString(null, "true", "false", "null"));
    }
}