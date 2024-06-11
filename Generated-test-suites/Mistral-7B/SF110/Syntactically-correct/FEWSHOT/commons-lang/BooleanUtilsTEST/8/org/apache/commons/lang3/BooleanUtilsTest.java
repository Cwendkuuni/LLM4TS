package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
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
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithIntegerTrueValueFalseValue() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test
    public void testToBooleanObjectWithIntegerTrueValueFalseValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        assertNull(BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
    }

    @Test
    public void testToBooleanObjectWithIntegerTrueValueFalseValueNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
        assertNull(BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithValue() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithValueTrueValueFalseValue() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    @Test
    public void testToStringTrueFalseWithStringTrueStringFalseStringNullString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
    }

    @Test
    public void testToStringOnOffWithStringTrueStringFalseStringNullString() {
        assertEquals("on", BooleanUtils.toString(Boolean.TRUE, "on", "off", null));
        assertEquals("off", BooleanUtils.toString(Boolean.FALSE, "on", "off", null));
    }

    @Test
    public void testToStringYesNoWithStringTrueStringFalseStringNullString() {
        assertEquals("yes", BooleanUtils.toString(Boolean.TRUE, "yes", "no", null));
        assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "yes", "no", null));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(new boolean[] {true, true}));
        assertFalse(BooleanUtils.and(new boolean[] {true, false}));
        assertArrayEquals(new boolean[] {false}, BooleanUtils.and(new boolean[] {false, true}));
        assertArrayEquals(new boolean[] {false}, BooleanUtils.and(new boolean[] {false, false}));
        assertArrayEquals(new boolean[] {true}, BooleanUtils.and(new boolean[] {true, true, true}));
        assertArrayEquals(new boolean[] {false}, BooleanUtils.and(new boolean[] {true, true, false}));
        assertArrayEquals(new boolean[] {false}, BooleanUtils.and(new boolean[] {true, false, false}));
        assertArrayEquals(new boolean[] {false}, BooleanUtils.and(new boolean[] {false, false, false}));
        try {
            BooleanUtils.and(new boolean[] {true, true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
        assertNull(BooleanUtils.and(new Boolean[] {null, Boolean.TRUE}));
        assertNull(BooleanUtils.and(new Boolean[] {Boolean.TRUE, null}));
        assertNull(BooleanUtils.and(new Boolean[] {null, null}));
        try {
            BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(new boolean[] {true, true}));
        assertTrue(BooleanUtils.or(new boolean[] {true, false}));
        assertTrue(BooleanUtils.or(new boolean[] {false, true}));
        assertTrue(BooleanUtils.or(new boolean[] {false, false}));
        assertTrue(BooleanUtils.or(new boolean[] {true, true, true}));
        assertFalse(BooleanUtils.or(new boolean[] {true, true, false}));
        assertFalse(BooleanUtils.or(new boolean[] {true, false, false}));
        assertFalse(BooleanUtils.or(new boolean[] {false, false, false}));
        try {
            BooleanUtils.or(new boolean[] {true, true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[] {true, true}));
        assertTrue(BooleanUtils.xor(new boolean[] {true, false}));
        assertFalse(BooleanUtils.xor(new boolean[] {false, true}));
        assertFalse(BooleanUtils.xor(new boolean[] {false, false}));
        assertFalse(BooleanUtils.xor(new boolean[] {true, true, true}));
        assertFalse(BooleanUtils.xor(new boolean[] {true, true, false}));
        assertFalse(BooleanUtils.xor(new boolean[] {true, false, false}));
        assertFalse(BooleanUtils.xor(new boolean[] {false, false, false}));
        try {
            BooleanUtils.xor(new boolean[] {true, true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray() {
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}