package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean negatedBool = BooleanUtils.negate(bool);
        assertFalse(negatedBool.booleanValue());

        bool = null;
        negatedBool = BooleanUtils.negate(bool);
        assertNull(negatedBool);
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
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
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
    public void testToBooleanWithBooleanAndValues() {
        assertTrue(BooleanUtils.toBoolean(true, 1, 0));
        assertFalse(BooleanUtils.toBoolean(false, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntegerAndValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        try {
            BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, 1, 0, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        assertNull(BooleanUtils.toInteger(null));
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
    public void testToIntegerWithBooleanAndValues() {
        assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanAndValuesAndNullValues() {
        assertEquals(1, BooleanUtils.toInteger(true, 1, null, null));
        assertEquals(0, BooleanUtils.toInteger(false, 1, null, null));
        assertEquals(0, BooleanUtils.toInteger(null, 1, null, null));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertNull(BooleanUtils.toBooleanObject(null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
    }

    @Test
    public void testToBooleanObjectWithStringAndValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true"));
        assertFalse(BooleanUtils.toBoolean("false"));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithStringAndValues() {
        assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
        assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
        assertFalse(BooleanUtils.toBoolean(null, "true", "false"));
    }

    @Test
    public void testToStringTrueFalseWithBoolean() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringTrueFalseWithBooleanAndValues() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toStringTrueFalse(null, "true", "false"));
    }

    @Test
    public void testToStringOnOffWithBoolean() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertNull(BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringOnOffWithBooleanAndValues() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE, "on", "off"));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE, "on", "off"));
        assertNull(BooleanUtils.toStringOnOff(null, "on", "off"));
    }

    @Test
    public void testToStringYesNoWithBoolean() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertNull(BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringYesNoWithBooleanAndValues() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE, "yes", "no"));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE, "yes", "no"));
        assertNull(BooleanUtils.toStringYesNo(null, "yes", "no"));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(BooleanUtils.toBooleanArray(true, true, true, true)));
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanArray(true, true, true, false)));
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanArray(null, true, true, true)));
    }

    @Test
    public void testAndWithBooleanObjects() {
        assertTrue(BooleanUtils.and(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)));
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanObjectArray(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanArray(true, true, true, true)));
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanArray(true, true, true, false)));
        assertFalse(BooleanUtils.or(BooleanUtils.toBooleanArray(null, true, true, true)));
    }

    @Test
    public void testOrWithBooleanObjects() {
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)));
        assertFalse(BooleanUtils.or(BooleanUtils.toBooleanObjectArray(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor(BooleanUtils.toBooleanArray(true, true, true, false)));
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanArray(true, true, true, true)));
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanArray(null, true, true, true)));
    }

    @Test
    public void testXorWithBooleanObjects() {
        assertTrue(BooleanUtils.xor(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)));
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanObjectArray(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanObjectArray(null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
    }
}