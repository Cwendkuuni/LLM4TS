package org.junit.jupiter.api;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.BooleanUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertFalse(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
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
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean((Object) 0));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithInts() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithInts() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toInteger(null));
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
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithInts() {
        assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(true));
        assertEquals("false", BooleanUtils.toStringTrueFalse(false));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(true));
        assertEquals("off", BooleanUtils.toStringOnOff(false));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(true));
        assertEquals("no", BooleanUtils.toStringYesNo(false));
    }

    @Test
    public void testToStringWithBoolean() {
        assertEquals("true", BooleanUtils.toString(true, "true", "false"));
        assertEquals("false", BooleanUtils.toString(false, "true", "false"));
    }

    @Test
    public void testToStringWithStrings() {
        assertEquals("true", BooleanUtils.toString(true, "yes", "no"));
        assertEquals("false", BooleanUtils.toString(false, "yes", "no"));
    }

    @Test
    public void testAndWithBooleans() {
        assertTrue(BooleanUtils.and(true, true, true, true, true));
        assertFalse(BooleanUtils.and(true, false, true, true, true));
        assertFalse(BooleanUtils.and(false, true, true, true, true));
        assertFalse(BooleanUtils.and(false, false, true, true, true));
        assertFalse(BooleanUtils.and(false, false, false, true, true));
        assertFalse(BooleanUtils.and(false, false, false, false, true));
        assertFalse(BooleanUtils.and(false, false, false, false, false));
    }

    @Test
    public void testAndWithBooleansArray() {
        assertTrue(BooleanUtils.and((Boolean[]) new Object[] { true, true, true, true, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { true, false, true, true, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { false, true, true, true, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { false, false, true, true, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { false, false, false, true, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { false, false, false, false, true }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { false, false, false, false, false }));

        try {
            BooleanUtils.and((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleansObject() {
        assertTrue(BooleanUtils.and((Boolean[]) new Object[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));

        try {
            BooleanUtils.and((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleans() {
        assertTrue(BooleanUtils.or(true, true, true, true, true));
        assertTrue(BooleanUtils.or(true, false, true, true, true));
        assertTrue(BooleanUtils.or(false, true, true, true, true));
        assertTrue(BooleanUtils.or(false, false, true, true, true));
        assertTrue(BooleanUtils.or(false, false, false, true, true));
        assertTrue(BooleanUtils.or(false, false, false, false, true));
        assertTrue(BooleanUtils.or(false, false, false, false, false));
    }

    @Test
    public void testOrWithBooleansArray() {
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { true, true, true, true, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { true, false, true, true, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { false, true, true, true, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { false, false, true, true, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { false, false, false, true, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { false, false, false, false, true }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { false, false, false, false, false }));

        try {
            BooleanUtils.or((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleansObject() {
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));

        try {
            BooleanUtils.or((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleans() {
        assertFalse(BooleanUtils.xor(true, true, true, true, true));
        assertTrue(BooleanUtils.xor(true, false, true, true, true));
        assertTrue(BooleanUtils.xor(false, true, true, true, true));
        assertTrue(BooleanUtils.xor(false, false, true, true, true));
        assertTrue(BooleanUtils.xor(false, false, false, true, true));
        assertTrue(BooleanUtils.xor(false, false, false, false, true));
        assertTrue(BooleanUtils.xor(false, false, false, false, false));
    }

    @Test
    public void testXorWithBooleansArray() {
        assertFalse(BooleanUtils.xor((Boolean[]) new Object[] { true, true, true, true, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { true, false, true, true, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { false, true, true, true, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { false, false, true, true, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { false, false, false, true, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { false, false, false, false, true }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { false, false, false, false, false }));

        try {
            BooleanUtils.xor((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleansObject() {
        assertFalse(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE }));
        assertTrue(BooleanUtils.xor((Boolean[]) new Object[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));

        try {
            BooleanUtils.xor((Boolean[]) null);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}