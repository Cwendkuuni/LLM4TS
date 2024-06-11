package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    // Test all methods in BooleanUtils class

    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
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
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertNull(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(-1));
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
    public void testToBooleanWithIntegers() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0)));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntsAndNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, -1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, -1));
        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntegersAndNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), null);
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
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
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
    public void testToIntegerWithBooleanAndInts() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
        try {
            BooleanUtils.toInteger(null, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Boolean must not be null", ex.getMessage());
        }
    }

    @Test
    public void testToIntegerWithBooleanAndIntegers() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        try {
            BooleanUtils.toIntegerObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Boolean must not be null", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        assertNull(BooleanUtils.toBooleanObject(null));
        try {
            BooleanUtils.toBooleanObject("asd");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid boolean representation: asd", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
        assertNull(BooleanUtils.toBooleanObject("asd", "true", "false", null));
        try {
            BooleanUtils.toBooleanObject("true", null, "false", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The String 'true' did not match the specified true value", ex.getMessage());
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
    public void testToStringWithValues() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        try {
            BooleanUtils.toString(null, "true", "false");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Boolean must not be null", ex.getMessage());
        }
    }

    @Test
    public void testToStringWithValuesAndNullValue() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
        assertNull(BooleanUtils.toString(null, "true", "false", null));
    }

    @Test
    public void testAndBooleanArray() {
        assertTrue(BooleanUtils.and(BooleanUtils.booleanArray(true, true, true, true)));
        assertFalse(BooleanUtils.and(BooleanUtils.booleanArray(true, true, false, false)));
        assertFalse(BooleanUtils.and(BooleanUtils.booleanArray(true, true, true, false)));
        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testAndBooleanArrayObjects() {
        assertTrue(BooleanUtils.and(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertFalse(BooleanUtils.and(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE)));
        assertFalse(BooleanUtils.and(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)));
        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new Boolean[0].clone());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testOrBooleanArray() {
        assertTrue(BooleanUtils.or(BooleanUtils.booleanArray(true, true, true, true)));
        assertTrue(BooleanUtils.or(BooleanUtils.booleanArray(true, true, true, false)));
        assertFalse(BooleanUtils.or(BooleanUtils.booleanArray(true, true, false, false)));
        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testOrBooleanArrayObjects() {
        assertTrue(BooleanUtils.or(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertTrue(BooleanUtils.or(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)));
        assertFalse(BooleanUtils.or(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE)));
        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new Boolean[0].clone());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testXorBooleanArray() {
        assertEquals(Boolean.FALSE, BooleanUtils.xor(BooleanUtils.booleanArray(true, true, true, true)));
        assertEquals(Boolean.TRUE, BooleanUtils.xor(BooleanUtils.booleanArray(true, false, true, false)));
        assertEquals(Boolean.TRUE, BooleanUtils.xor(BooleanUtils.booleanArray(true, true, false, false)));
        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new Boolean[0]);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testXorBooleanArrayObjects() {
        assertEquals(Boolean.FALSE, BooleanUtils.xor(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)));
        assertEquals(Boolean.TRUE, BooleanUtils.xor(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE)));
        assertEquals(Boolean.TRUE, BooleanUtils.xor(BooleanUtils.booleanArrayObjects(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE)));
        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new Boolean[0].clone());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }
}