package org.junit.tests; // Replace with your test package if needed

import static org.apache.commons.lang3.BooleanUtils.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(negate(Boolean.TRUE) == Boolean.FALSE);
        assertNull(negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(isTrue(Boolean.TRUE));
        assertFalse(isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(isNotTrue(Boolean.TRUE));
        assertTrue(isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        assertFalse(isFalse(Boolean.TRUE));
        assertTrue(isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(isNotFalse(Boolean.TRUE));
        assertFalse(isNotFalse(Boolean.FALSE));
    }

    @Test
    public void testToBoolean() {
        assertTrue(toBoolean(Boolean.TRUE));
        assertFalse(toBoolean(Boolean.FALSE));
        assertFalse(toBoolean(null));
        assertFalse(toBoolean(Integer.MIN_VALUE));
        assertFalse(toBoolean(new Integer(-1)));
        assertFalse(toBoolean(0));
        assertTrue(toBoolean(1));
        assertTrue(toBoolean(NumberUtils.INTEGER_ONE));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertTrue(toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(toBooleanDefaultIfNull(null, true));
        assertFalse(toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithBoolean() {
        assertTrue(toBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE) == Boolean.TRUE);
        assertFalse(toBoolean(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE) == Boolean.TRUE);
        assertNull(toBoolean(null, Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(toBoolean(1, 1, 0) == Boolean.TRUE);
        assertFalse(toBoolean(0, 1, 0) == Boolean.TRUE);
        assertFalse(toBoolean(-1, 1, 0));
        assertFalse(toBoolean(Integer.MIN_VALUE, 1, 0));
        assertFalse(toBoolean(0, 0, 1));
        assertFalse(toBoolean(1, 0, 1));
        assertTrue(toBoolean(1, 1, 1));
        assertTrue(toBoolean(Integer.MAX_VALUE, 1, 0));
        assertFalse(toBoolean(MathUtils.MIN_VALUE, 1, 0));
    }

    @Test
    public void testToBooleanObject() {
        assertEquals(Boolean.TRUE, toBooleanObject(Boolean.TRUE));
        assertEquals(Boolean.FALSE, toBooleanObject(Boolean.FALSE));
        assertNull(toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithBoolean() {
        assertEquals(Boolean.TRUE, toBooleanObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, toBooleanObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertNull(toBooleanObject(null, Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, toBooleanObject(Integer.valueOf(1), Boolean.TRUE, Boolean.FALSE, null));
        assertEquals(Boolean.FALSE, toBooleanObject(Integer.valueOf(0), Boolean.TRUE, Boolean.FALSE, null));
        assertNull(toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, toInteger(true));
        assertEquals(0, toInteger(false));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(true));
        assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(false));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(Boolean.FALSE));
        assertNull(toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, toInteger(true, 1, 0));
        assertEquals(0, toInteger(false, 1, 0));
        assertEquals(-1, toInteger(false, 0, 1));
        assertEquals(1, toInteger(true, 0, 1));
        assertEquals(1, toInteger(true, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanEdgeCasesWithInt() {
        toBoolean(-2, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanEdgeCasesWithInteger() {
        toBooleanObject(-2, 1, 0, null);
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, toBooleanObject("true"));
        assertEquals(Boolean.FALSE, toBooleanObject("false"));
        assertNull(toBooleanObject(null));
        assertNull(toBooleanObject(""));
        assertNull(toBooleanObject("yes"));
        assertNull(toBooleanObject("no"));
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(toBoolean("true"));
        assertFalse(toBoolean("false"));
        assertFalse(toBoolean(null));
        assertFalse(toBoolean(""));
        assertFalse(toBoolean("yes"));
        assertFalse(toBoolean("no"));
        assertFalse(toBoolean("on"));
        assertFalse(toBoolean("off"));
        assertFalse(toBoolean("1"));
        assertFalse(toBoolean("0"));
        assertFalse(toBoolean(""));
        assertFalse(toBoolean("12345"));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", toStringTrueFalse(Boolean.FALSE));
        assertNull(toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", toStringOnOff(Boolean.TRUE));
        assertEquals("off", toStringOnOff(Boolean.FALSE));
        assertNull(toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", toStringYesNo(Boolean.TRUE));
        assertEquals("no", toStringYesNo(Boolean.FALSE));
        assertNull(toStringYesNo(null));
    }

    @Test
    public void testToStringWithBoolean() {
        assertEquals("true", toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", toString(Boolean.FALSE, "true", "false"));
        assertNull(toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanAndValues() {
        assertEquals("true", toString(Boolean.TRUE, "true", "false", "on"));
        assertEquals("false", toString(Boolean.FALSE, "true", "false", "on"));
        assertNull(toString(null, "true", "false", "on"));
    }

    @Test
    public void testAnd() {
        assertTrue(and(true, true));
        assertFalse(and(true, false));
        assertFalse(and(false, true));
        assertFalse(and(false, false));
        assertFalse(and(null, true, false));
        assertFalse(and(new boolean[] { true, false, true }));
        assertFalse(and(new boolean[] { false, true, false }));
        assertFalse(and(new boolean[] { false, false, false }));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(and(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(and(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(and(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(and(null, Boolean.TRUE, Boolean.FALSE));
        assertFalse(and(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
        assertFalse(and(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE }));
        assertFalse(and(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }

    @Test
    public void testOr() {
        assertTrue(or(true, true));
        assertTrue(or(true, false));
        assertTrue(or(false, true));
        assertFalse(or(false, false));
        assertFalse(or(null, true, false));
        assertFalse(or(new boolean[] { true, false, true }));
        assertFalse(or(new boolean[] { false, true, false }));
        assertFalse(or(new boolean[] { false, false, false }));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(or(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(or(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(or(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(or(null, Boolean.TRUE, Boolean.FALSE));
        assertFalse(or(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
        assertFalse(or(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE }));
        assertFalse(or(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }

    @Test
    public void testXor() {
        assertTrue(xor(true, true));
        assertTrue(xor(true, false));
        assertFalse(xor(false, true));
        assertTrue(xor(false, false));
        assertFalse(xor(null, true, false));
        assertFalse(xor(new boolean[] { true, false, true }));
        assertFalse(xor(new boolean[] { false, true, false }));
        assertFalse(xor(new boolean[] { false, false, false }));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(xor(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(xor(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(xor(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(xor(null, Boolean.TRUE, Boolean.FALSE));
        assertFalse(xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
        assertFalse(xor(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE }));
        assertFalse(xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }
}