package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Assert;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @org.junit.Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertTrue(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
        assertNull(BooleanUtils.negate(null));
    }

    @org.junit.Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @org.junit.Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @org.junit.Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @org.junit.Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @org.junit.Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @org.junit.Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @org.junit.Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(1)));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(0)));
    }

    @org.junit.Test
    public void testToBooleanWithIntTrueValueFalseValue() {
        assertTrue(BooleanUtils.toBoolean(true, 1, 0));
        assertFalse(BooleanUtils.toBoolean(false, 1, 0));
    }

    @org.junit.Test
    public void testToBooleanWithIntTrueValueFalseValueNullValue() {
        assertTrue(BooleanUtils.toBoolean(true, 1, 0, -1));
        assertFalse(BooleanUtils.toBoolean(false, 1, 0, -1));
        assertNull(BooleanUtils.toBoolean(-1, 1, 0, -1));
    }

    @org.junit.Test
    public void testToBooleanObjectWithIntTrueValueFalseValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(1), 1, 0, -1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(0), 1, 0, -1));
        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
    }

    @org.junit.Test
    public void testToBooleanObjectWithIntTrueValueFalseValueNullValueNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(1), null, null, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(MathUtils.toIntExact(0), null, null, null));
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
    }

    @org.junit.Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @org.junit.Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @org.junit.Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @org.junit.Test
    public void testToIntegerWithBooleans() {
        try {
            BooleanUtils.toInteger(Boolean.TRUE, Boolean.FALSE, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanArray() {
        boolean[] expected = new boolean[] {true, false, true, false, true, false};
        Boolean[] actual = BooleanUtils.toBooleanArray(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testToBooleanArrayNullInput() {
        try {
            BooleanUtils.toBooleanArray(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToBooleanArrayEmptyInput() {
        Boolean[] input = new Boolean[0];
        boolean[] expected = new boolean[0];
        boolean[] actual = BooleanUtils.toBooleanArray(input);
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testToBooleanArrayArrayWithNullElements() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE};
        try {
            BooleanUtils.toBooleanArray(input);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToBooleanArrayWithNullElementsRemoved() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE};
        boolean[] expected = new boolean[] {true, false, true, false, true};
        boolean[] actual = BooleanUtils.toBooleanArray(input);
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true, true));
        assertFalse(BooleanUtils.and(true, true, true, false));
        assertFalse(BooleanUtils.and(true, true, false, true));
        assertFalse(BooleanUtils.and(true, false, true, true));
        assertFalse(BooleanUtils.and(false, true, true, true));
        assertFalse(BooleanUtils.and(false, true, true, false));
        assertFalse(BooleanUtils.and(false, true, false, true));
        assertFalse(BooleanUtils.and(false, false, true, true));
        assertFalse(BooleanUtils.and(false, false, true, false));
        assertFalse(BooleanUtils.and(false, false, false, true));
        assertFalse(BooleanUtils.and(false, false, false, false));

        try {
            BooleanUtils.and(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testAndWithBooleans() {
        boolean[] expected = new boolean[] {true, false, false, false, true, false};
        boolean result = BooleanUtils.and(true, true, false, false, true, false);
        assertArrayEquals(expected, new boolean[] {result, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE});
    }

    @org.junit.Test
    public void testAndWithBooleansArrayWithNullElements() {
        try {
            BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testAndWithBooleansArrayWithNullElementsRemoved() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE};
        boolean[] expected = new boolean[] {true, false, true, false, true};
        boolean[] actual = BooleanUtils.toBooleanArray(BooleanUtils.and(input));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true, true));
        assertTrue(BooleanUtils.or(true, true, true, false));
        assertTrue(BooleanUtils.or(true, true, false, true));
        assertTrue(BooleanUtils.or(true, false, true, true));
        assertTrue(BooleanUtils.or(false, true, true, true));
        assertTrue(BooleanUtils.or(false, true, true, false));
        assertTrue(BooleanUtils.or(false, true, false, true));
        assertTrue(BooleanUtils.or(false, false, true, true));
        assertTrue(BooleanUtils.or(false, false, true, false));
        assertTrue(BooleanUtils.or(false, false, false, true));
        assertTrue(BooleanUtils.or(false, false, false, false));

        try {
            BooleanUtils.or(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testOrWithBooleans() {
        boolean[] expected = new boolean[] {true, true, true, true, true, true};
        boolean result = BooleanUtils.or(true, true, false, false, true, false);
        assertArrayEquals(expected, new boolean[] {result, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
    }

    @org.junit.Test
    public void testOrWithBooleansArrayWithNullElements() {
        try {
            BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testOrWithBooleansArrayWithNullElementsRemoved() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE};
        boolean[] expected = new boolean[] {true, true, true, true, true};
        boolean[] actual = BooleanUtils.toBooleanArray(BooleanUtils.or(input));
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true, true, true));
        assertFalse(BooleanUtils.xor(true, true, true, false));
        assertFalse(BooleanUtils.xor(true, true, false, true));
        assertFalse(BooleanUtils.xor(true, false, true, true));
        assertFalse(BooleanUtils.xor(false, true, true, true));
        assertFalse(BooleanUtils.xor(false, true, true, false));
        assertFalse(BooleanUtils.xor(false, true, false, true));
        assertFalse(BooleanUtils.xor(false, false, true, true));
        assertFalse(BooleanUtils.xor(false, false, true, false));
        assertFalse(BooleanUtils.xor(false, false, false, true));
        assertFalse(BooleanUtils.xor(false, false, false, false));
        assertTrue(BooleanUtils.xor(true, false, false, true));

        try {
            BooleanUtils.xor(null, true, true, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testXorWithBooleans() {
        boolean[] expected = new boolean[] {true, false, true, false, true, false, true, false};
        boolean result = BooleanUtils.xor(true, true, false, false, true, false, true, false);
        assertArrayEquals(expected, new boolean[] {result, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE});
    }

    @org.junit.Test
    public void testXorWithBooleansArrayWithNullElements() {
        try {
            BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, null});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testXorWithBooleansArrayWithNullElementsRemoved() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, null};
        boolean[] expected = new boolean[] {true, false, true, false, true, false, true, false};
        boolean[] actual = BooleanUtils.toBooleanArray(BooleanUtils.xor(input));
        assertArrayEquals(expected, actual);
    }
}