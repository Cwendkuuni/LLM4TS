package org.apache.commons.lang3;

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
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertNull(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
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
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntNullValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), null, null, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), null, null, null));
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
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
        assertNull(BooleanUtils.toIntegerObject(null));
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
    public void testAnd() {
        assertFalse(BooleanUtils.and(new boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertTrue(BooleanUtils.and(new boolean[]{Boolean.TRUE, Boolean.TRUE}));
        try {
            BooleanUtils.and(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new boolean[]{Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleans() {
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
        try {
            BooleanUtils.and(null, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertFalse(BooleanUtils.or(new boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new boolean[]{Boolean.TRUE, Boolean.TRUE}));
        try {
            BooleanUtils.or(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new boolean[]{Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleans() {
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
        try {
            BooleanUtils.or(null, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertTrue(BooleanUtils.xor(new boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new boolean[]{Boolean.TRUE, Boolean.FALSE}));
        try {
            BooleanUtils.xor(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new boolean[]{Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleans() {
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
        try {
            BooleanUtils.xor(null, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}