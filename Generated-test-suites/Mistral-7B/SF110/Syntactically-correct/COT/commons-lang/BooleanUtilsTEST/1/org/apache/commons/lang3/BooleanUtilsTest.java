package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NullPointerException;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertFalse(BooleanUtils.negate(Boolean.TRUE));
        assertTrue(BooleanUtils.negate(Boolean.FALSE));
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
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @Test
    public void testToBooleanWithTwoIntegers() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test
    public void testToBooleanWithThreeIntegers() {
        try {
            BooleanUtils.toBoolean(1, 1, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }

        try {
            BooleanUtils.toBoolean(0, 1, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithFourIntegers() {
        try {
            BooleanUtils.toBoolean(1, 1, 1, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }

        try {
            BooleanUtils.toBoolean(1, 1, 2, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }

        try {
            BooleanUtils.toBoolean(null, 1, 1, 1);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testToBooleanWithFourIntegerObjects() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));

        try {
            BooleanUtils.toBooleanObject(1, 1, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.toBooleanObject(1, null, 0, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.toBooleanObject(null, 1, 0, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }

        try {
            BooleanUtils.toBooleanObject(null, null, 0, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
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
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithTwoStrings() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", "true"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false", "true"));
    }

    @Test
    public void testAnd() {
        assertFalse(BooleanUtils.and(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.and(new boolean[] { true, true, true }));
        assertFalse(BooleanUtils.and(new boolean[0]));
        assertFalse(BooleanUtils.and(new boolean[] { null }));
        assertFalse(BooleanUtils.and(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.and(new boolean[] { null, true, null }));
        assertFalse(BooleanUtils.and(new boolean[] { null, null, true }));
        assertFalse(BooleanUtils.and(new boolean[] { null, null, null, null }));
    }

    @Test
    public void testAndWithBooleanArrays() {
        assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { null }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, null, null }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, null, null, null }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, null, null, Boolean.FALSE }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, Boolean.FALSE, null }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, null, Boolean.TRUE }));
    }

    @Test
    public void testAndWithBooleanPrimitives() {
        assertFalse(BooleanUtils.and(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.and(new boolean[] { true, true, true }));
        assertFalse(BooleanUtils.and(new boolean[0]));
        assertFalse(BooleanUtils.and(new boolean[] { null }));
        assertFalse(BooleanUtils.and(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.and(new boolean[] { null, null, null, null }));
        try {
            BooleanUtils.and(new boolean[] { null, null, null, null, null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testOr() {
        assertFalse(BooleanUtils.or(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.or(new boolean[] { true, true, true }));
        assertFalse(BooleanUtils.or(new boolean[0]));
        assertFalse(BooleanUtils.or(new boolean[] { null }));
        assertFalse(BooleanUtils.or(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.or(new boolean[] { null, null, null, null }));
    }

    @Test
    public void testOrWithBooleanArrays() {
        assertFalse(BooleanUtils.or(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.or(new Boolean[0]));
        assertFalse(BooleanUtils.or(new Boolean[] { null }));
        assertFalse(BooleanUtils.or(new Boolean[] { null, null, null }));
        assertFalse(BooleanUtils.or(new Boolean[] { null, null, null, null }));
    }

    @Test
    public void testOrWithBooleanPrimitives() {
        assertFalse(BooleanUtils.or(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.or(new boolean[] { true, true, true }));
        assertFalse(BooleanUtils.or(new boolean[0]));
        assertFalse(BooleanUtils.or(new boolean[] { null }));
        assertFalse(BooleanUtils.or(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.or(new boolean[] { null, null, null, null }));
        try {
            BooleanUtils.or(new boolean[] { null, null, null, null, null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.xor(new boolean[] { true, true, false }));
        assertFalse(BooleanUtils.xor(new boolean[0]));
        assertFalse(BooleanUtils.xor(new boolean[] { null }));
        assertFalse(BooleanUtils.xor(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.xor(new boolean[] { null, null, null, null }));
    }

    @Test
    public void testXorWithBooleanArrays() {
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE }));
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }));
        assertFalse(BooleanUtils.xor(new Boolean[0]));
        assertFalse(BooleanUtils.xor(new Boolean[] { null }));
        assertFalse(BooleanUtils.xor(new Boolean[] { null, null, null }));
        assertFalse(BooleanUtils.xor(new Boolean[] { null, null, null, null }));
    }

    @Test
    public void testXorWithBooleanPrimitives() {
        assertFalse(BooleanUtils.xor(new boolean[] { false, true, false }));
        assertTrue(BooleanUtils.xor(new boolean[] { true, true, false }));
        assertFalse(BooleanUtils.xor(new boolean[0]));
        assertFalse(BooleanUtils.xor(new boolean[] { null }));
        assertFalse(BooleanUtils.xor(new boolean[] { null, null, null }));
        assertFalse(BooleanUtils.xor(new boolean[] { null, null, null, null }));
        try {
            BooleanUtils.xor(new boolean[] { null, null, null, null, null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected
        }
    }
}