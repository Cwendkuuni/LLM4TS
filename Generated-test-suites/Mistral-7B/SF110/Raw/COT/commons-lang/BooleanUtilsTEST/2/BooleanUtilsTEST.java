package org.junit.tests; // replace with your test package name

import static org.apache.commons.lang3.BooleanUtils.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NumberUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(negate(Boolean.TRUE) == Boolean.FALSE);
        assertTrue(negate(Boolean.FALSE) == Boolean.TRUE);
        assertNull(negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(isTrue(Boolean.TRUE));
        assertFalse(isTrue(Boolean.FALSE));
        assertFalse(isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(isNotTrue(Boolean.TRUE));
        assertTrue(isNotTrue(Boolean.FALSE));
        assertFalse(isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(isFalse(Boolean.TRUE));
        assertTrue(isFalse(Boolean.FALSE));
        assertFalse(isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(isNotFalse(Boolean.TRUE));
        assertFalse(isNotFalse(Boolean.FALSE));
        assertFalse(isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(toBoolean(Boolean.TRUE) == Boolean.TRUE);
        assertFalse(toBoolean(Boolean.FALSE) == Boolean.FALSE);
        assertFalse(toBoolean(null) == Boolean.FALSE);
        assertTrue(toBoolean(1) == Boolean.TRUE);
        assertFalse(toBoolean(0) == Boolean.FALSE);
        assertFalse(toBoolean(-1) == Boolean.FALSE);
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, false) == Boolean.TRUE);
        assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, false) == Boolean.FALSE);
        assertFalse(toBooleanDefaultIfNull(null, false) == false);
        assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, true) == Boolean.TRUE);
        assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, true) == Boolean.FALSE);
        assertTrue(toBooleanDefaultIfNull(null, true) == true);
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(toBoolean(1) == Boolean.TRUE);
        assertFalse(toBoolean(0) == Boolean.FALSE);
        assertFalse(toBoolean(-1) == Boolean.FALSE);
    }

    @Test
    public void testToBooleanObject() {
        assertEquals(Boolean.TRUE, toBooleanObject(Integer.valueOf(1)));
        assertEquals(Boolean.FALSE, toBooleanObject(Integer.valueOf(0)));
        assertNull(toBooleanObject(null));
        assertNull(toBooleanObject(Integer.valueOf(-1)));
    }

    @Test
    public void testToBooleanObjectWithIntegerAndValues() {
        assertEquals(Boolean.TRUE, toBooleanObject(1, 1, 0, null));
        assertEquals(Boolean.FALSE, toBooleanObject(0, 1, 0, null));
        assertNull(toBooleanObject(-1, 1, 0, null));
        assertNull(toBooleanObject(null, 1, 0, null));
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
    public void testToIntegerWithValue() {
        assertEquals(1, toInteger(true, 1, 0));
        assertEquals(0, toInteger(false, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanArray() {
        assertTrue(and(new boolean[]{true, true, true}));
        assertFalse(and(new boolean[]{false, true, true}));
        assertFalse(and(new boolean[]{true, false, true}));
        assertFalse(and(new boolean[]{false, false, false}));
        assertFalse(and(null));

        assertTrue(or(new boolean[]{true, true, true}));
        assertTrue(or(new boolean[]{false, true, true}));
        assertTrue(or(new boolean[]{true, false, true}));
        assertFalse(or(new boolean[]{false, false, false}));
        assertFalse(or(null));

        assertTrue(xor(new boolean[]{true, true, true}));
        assertFalse(xor(new boolean[]{false, true, true}));
        assertTrue(xor(new boolean[]{true, false, true}));
        assertTrue(xor(new boolean[]{false, false, false}));
        assertFalse(xor(null));

        try {
            and(new Boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }

        try {
            or(new Boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }

        try {
            xor(new Boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}