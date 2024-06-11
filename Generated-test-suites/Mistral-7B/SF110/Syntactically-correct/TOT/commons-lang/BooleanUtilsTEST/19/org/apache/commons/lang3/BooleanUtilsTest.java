package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertFalse(BooleanUtils.isFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Integer.MAX_VALUE));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @Test
    public void testToBooleanWithBooleanAndValues() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
    }

    @Test
    public void testToBooleanWithIntegerAndValues() {
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
    public void testToBooleanObjectWithIntegerAndValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, Integer.MIN_VALUE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, Integer.MIN_VALUE));
        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, Integer.MIN_VALUE));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithValue() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanAndValues() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 2));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 2));
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
    public void testToStringWithValue() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true, true));
        assertFalse(BooleanUtils.and(true, true, false, false));
        assertFalse(BooleanUtils.and(true, false, true, false));
        assertFalse(BooleanUtils.and(false, true, true, false));
        assertFalse(BooleanUtils.and(false, false, true, false));
        assertFalse(BooleanUtils.and(false, false, false, true));

        try {
            BooleanUtils.and(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.and(true, null, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.and(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testAndWithBooleanArrays() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE}));

        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.and(new Boolean[]{null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true, true));
        assertTrue(BooleanUtils.or(true, true, true, false));
        assertTrue(BooleanUtils.or(true, true, false, false));
        assertTrue(BooleanUtils.or(false, true, true, false));
        assertTrue(BooleanUtils.or(false, true, false, false));
        assertTrue(BooleanUtils.or(false, false, true, false));

        try {
            BooleanUtils.or(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.or(true, null, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.or(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testOrWithBooleanArrays() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE}));

        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.or(new Boolean[]{null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true, true, true));
        assertFalse(BooleanUtils.xor(true, true, true, false));
        assertFalse(BooleanUtils.xor(true, true, false, false));
        assertFalse(BooleanUtils.xor(true, false, true, false));
        assertFalse(BooleanUtils.xor(false, true, true, false));
        assertFalse(BooleanUtils.xor(false, false, true, false));
        assertTrue(BooleanUtils.xor(true, true, false, true));
        assertFalse(BooleanUtils.xor(true, false, false, true));
        assertFalse(BooleanUtils.xor(false, true, false, true));
        assertFalse(BooleanUtils.xor(false, false, true, true));
        assertFalse(BooleanUtils.xor(false, false, false, true));
        assertFalse(BooleanUtils.xor(false, false, false, false));

        try {
            BooleanUtils.xor(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.xor(true, null, true, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.xor(true, true, null, true);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testXorWithBooleanArrays() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE}));

        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.xor(new Boolean[]{null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }
}