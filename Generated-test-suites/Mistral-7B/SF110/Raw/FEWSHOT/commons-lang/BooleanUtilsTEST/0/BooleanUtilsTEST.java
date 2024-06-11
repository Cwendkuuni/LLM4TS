package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testToBoolean() {
        assertFalse(BooleanUtils.toBoolean(false));
        assertTrue(BooleanUtils.toBoolean(true));
        assertNull(BooleanUtils.toBoolean(null));
        try {
            BooleanUtils.toBoolean("abc");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The given object is not a valid Boolean.", ex.getMessage());
        }
    }

    @Test
    public void testToBoolean_booleanArray() {
        boolean[] input = {false, true, false, true};
        assertTrue(BooleanUtils.toBoolean(input));

        input = new boolean[0];
        assertFalse(BooleanUtils.toBoolean(input));

        input = null;
        assertNull(BooleanUtils.toBoolean(input));
    }

    @Test
    public void testToBoolean_BooleanArray() {
        Boolean[] input = {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        assertTrue(BooleanUtils.toBoolean(input));

        input = new Boolean[0];
        assertFalse(BooleanUtils.toBoolean(input));

        input = null;
        assertNull(BooleanUtils.toBoolean(input));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testNegate() {
        Boolean boolTrue = Boolean.TRUE;
        Boolean boolFalse = Boolean.FALSE;
        Boolean negatedTrue = BooleanUtils.negate(boolTrue);
        Boolean negatedFalse = BooleanUtils.negate(boolFalse);
        assertEquals(Boolean.FALSE, negatedTrue);
        assertEquals(Boolean.TRUE, negatedFalse);
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsNotTrue() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testToBooleanObject() {
        Boolean boolTrue = Boolean.TRUE;
        Boolean boolFalse = Boolean.FALSE;
        Boolean boolNull = null;
        assertEquals(boolTrue, BooleanUtils.toBooleanObject(Boolean.TRUE));
        assertEquals(boolFalse, BooleanUtils.toBooleanObject(Boolean.FALSE));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean boolTrue = Boolean.TRUE;
        Boolean boolFalse = Boolean.FALSE;
        Boolean boolNull = null;
        boolean defaultValue = true;
        assertEquals(boolTrue, BooleanUtils.toBooleanDefaultIfNull(boolTrue, defaultValue));
        assertEquals(boolFalse, BooleanUtils.toBooleanDefaultIfNull(boolFalse, defaultValue));
        assertEquals(defaultValue, BooleanUtils.toBooleanDefaultIfNull(null, defaultValue));
    }

    @Test
    public void testToBoolean_int() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertNull(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObject_int() {
        Integer intTrue = 1;
        Integer intFalse = 0;
        Integer intNull = null;
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(intTrue));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(intFalse));
        assertNull(BooleanUtils.toBooleanObject(intNull));
    }

    @Test
    public void testToBoolean_int_int_int() {
        int valueTrue = 1;
        int valueFalse = 0;
        int nullValue = -1;
        assertTrue(BooleanUtils.toBoolean(valueTrue, valueTrue, valueFalse));
        assertFalse(BooleanUtils.toBoolean(valueFalse, valueTrue, valueFalse));
        assertFalse(BooleanUtils.toBoolean(nullValue, valueTrue, valueFalse));
    }

    @Test
    public void testToBoolean_int_Boolean_int_int() {
        int valueTrue = 1;
        int valueFalse = 0;
        int nullValue = -1;
        Boolean boolTrue = Boolean.TRUE;
        Boolean boolFalse = Boolean.FALSE;
        assertTrue(BooleanUtils.toBoolean(valueTrue, boolTrue, valueFalse, valueFalse));
        assertFalse(BooleanUtils.toBoolean(valueFalse, boolTrue, valueFalse, valueFalse));
        assertFalse(BooleanUtils.toBoolean(nullValue, boolTrue, valueFalse, valueFalse));
    }

    @Test
    public void testToBooleanObject_int_int_int_int() {
        int valueTrue = 1;
        int valueFalse = 0;
        int nullValue = -1;
        int trueValue = 1;
        int falseValue = 0;
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(valueTrue, trueValue, falseValue, nullValue));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(valueFalse, trueValue, falseValue, nullValue));
        assertNull(BooleanUtils.toBooleanObject(nullValue, trueValue, falseValue, nullValue));
    }

    @Test
    public void testToBooleanObject_int_Boolean_int_int_int() {
        int valueTrue = 1;
        int valueFalse = 0;
        int nullValue = -1;
        Boolean boolTrue = Boolean.TRUE;
        Boolean boolFalse = Boolean.FALSE;
        assertEquals(boolTrue, BooleanUtils.toBooleanObject(valueTrue, boolTrue, falseValue, nullValue));
        assertEquals(boolFalse, BooleanUtils.toBooleanObject(valueFalse, boolFalse, nullValue, nullValue));
        assertNull(BooleanUtils.toBooleanObject(nullValue, boolTrue, falseValue, nullValue));
    }

    @Test
    public void testToInteger() {
        assertTrue(BooleanUtils.toInteger(Boolean.TRUE) == 1);
        assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 1);
        assertNull(BooleanUtils.toInteger(null));
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
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "on", "off"));
        assertEquals("on", BooleanUtils.toString(Boolean.TRUE, "on", "off"));
        assertEquals("yes", BooleanUtils.toString(Boolean.TRUE, "yes", "no"));
        assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "yes", "no"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true));
        assertFalse(BooleanUtils.and(true, false));
        assertFalse(BooleanUtils.and(false, true));
        assertFalse(BooleanUtils.and(false, false));
        assertFalse(BooleanUtils.and(null, true, false));
        assertFalse(BooleanUtils.and(true, null, false));
        assertFalse(BooleanUtils.and(true, true, null));
        try {
            BooleanUtils.and(true, true, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements.", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true));
        assertTrue(BooleanUtils.or(true, false));
        assertTrue(BooleanUtils.or(false, true));
        assertFalse(BooleanUtils.or(false, false));
        assertFalse(BooleanUtils.or(null, true, false));
        assertFalse(BooleanUtils.or(true, null, false));
        assertFalse(BooleanUtils.or(true, true, null));
        try {
            BooleanUtils.or(true, true, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements.", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, false));
        assertFalse(BooleanUtils.xor(true, true));
        assertFalse(BooleanUtils.xor(false, false));
        assertFalse(BooleanUtils.xor(null, true, false));
        assertFalse(BooleanUtils.xor(true, null, false));
        assertFalse(BooleanUtils.xor(true, true, null));
        try {
            BooleanUtils.xor(true, true, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements.", ex.getMessage());
        }
    }
}