package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        // Test with null input
        assertNull(BooleanUtils.negate(null));

        // Test with true input
        assertTrue(BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));

        // Test with false input
        assertFalse(BooleanUtils.negate(Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        // Test with null input
        assertFalse(BooleanUtils.isTrue(null));

        // Test with true input
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));

        // Test with false input
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        // Test with null input
        assertFalse(BooleanUtils.isNotTrue(null));

        // Test with true input
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));

        // Test with false input
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        // Test with null input
        assertFalse(BooleanUtils.isFalse(null));

        // Test with true input
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));

        // Test with false input
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalse() {
        // Test with null input
        assertFalse(BooleanUtils.isNotFalse(null));

        // Test with true input
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));

        // Test with false input
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testToBoolean() {
        // Test with null input
        assertFalse(BooleanUtils.toBoolean(null));

        // Test with boolean input
        assertNotNull(BooleanUtils.toBoolean(Boolean.TRUE));
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));

        assertNotNull(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));

        // Test with integer input
        assertNotNull(BooleanUtils.toBoolean(1));
        assertTrue(BooleanUtils.toBoolean(1));

        assertNull(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(0));

        // Test with int input
        assertNotNull(BooleanUtils.toBoolean(1, 1, 0));
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));

        assertNull(BooleanUtils.toBoolean(0, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));

        try {
            BooleanUtils.toBoolean(-1, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        // Test with null input and true default value
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));

        // Test with null input and false default value
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));

        // Test with boolean input
        assertNotNull(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));

        assertNotNull(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));

        // Test with integer input
        assertNotNull(BooleanUtils.toBooleanDefaultIfNull(1, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, false));

        assertNull(BooleanUtils.toBooleanDefaultIfNull(0, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
    }

    @Test
    public void testToBooleanObject() {
        // Test with null input
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 
                                                -1));

        // Test with boolean input
        assertNotNull(BooleanUtils.toBooleanObject(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));

        assertNotNull(BooleanUtils.toBooleanObject(Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));

        // Test with integer input
        assertNull(BooleanUtils.toBooleanObject(1, 1, 0, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(1, 1, 0, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(0, 1, 0, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(0, 1, 0, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, 
                                                null));

        try {
            BooleanUtils.toBooleanObject(2, 1, 0, 
                                         -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithNullableBoolean() {
        // Test with null boolean input
        assertNull(BooleanUtils.toBooleanObject(null, null));

        // Test with boolean input
        assertNotNull(BooleanUtils.toBooleanObject(Boolean.TRUE, null));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, null));

        assertNotNull(BooleanUtils.toBooleanObject(Boolean.FALSE, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, null));

        // Test with integer input
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(null, 0, 1, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(null, 0, 1, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 
                                                null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, 
                                                -1));

        // Test with integer input and nullBoolean argument
        assertNull(BooleanUtils.toBooleanObject(1, null, 0, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(1, null, 0, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(0, null, 1, 
                                                -1));
        assertNull(BooleanUtils.toBooleanObject(0, null, 1, 
                                                null));

        assertNull(BooleanUtils.toBooleanObject(1, null, 0, 
                                                null));
        assertNull(BooleanUtils.toBooleanObject(1, null, 0, 
                                                -1));

        try {
            BooleanUtils.toBooleanObject(2, null, 1, 
                                         -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        // Test with null input
        assertNull(BooleanUtils.toInteger(null));

        // Test with boolean input
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));

        // Test with integer input
        assertEquals(1, BooleanUtils.toInteger(1));
        assertEquals(0, BooleanUtils.toInteger(0));

        try {
            BooleanUtils.toInteger(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer must be either 0 or 1", ex.getMessage());
        }
    }

    @Test
    public void testToIntegerObject() {
        // Test with null input
        assertNull(BooleanUtils.toIntegerObject(null));

        // Test with boolean input
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));

        // Test with integer input
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(1));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(0));

        try {
            BooleanUtils.toIntegerObject(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer must be either 0 or 1", ex.getMessage());
        }
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        // Test with null boolean input
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(null));

        // Test with boolean input
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));

        // Test with integer input
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(1));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(0));
    }

    @Test
    public void testToIntegerWithValue() {
        // Test with null input
        assertNull(BooleanUtils.toInteger(null, 1, 0));

        // Test with boolean input
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));

        // Test with integer input
        assertEquals(1, BooleanUtils.toInteger(1, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(0, 1, 0));

        try {
            BooleanUtils.toInteger(-1, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer must be either 1 or 0", ex.getMessage());
        }
    }

    @Test
    public void testToIntegerWithBooleanArray() {
        boolean[] bools = {true, false, true, false};
        boolean[] expectedBools = {false, true, false, true};

        boolean[] primitives = BooleanUtils.toPrimitive(bools);

        for (int i = 0; i < bools.length; i++) {
            assertEquals(expectedBools[i], primitives[i]);
        }

        Boolean[] boolObjects = BooleanUtils.toBooleanArray(primitives);

        for (int i = 0; i < bools.length; i++) {
            assertEquals(bools[i], boolObjects[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullArray() {
        BooleanUtils.and(null);
    }

    @Test
    public void testAnd() {
        // Test with empty array
        assertFalse(BooleanUtils.and());

        // Test with single boolean
        assertTrue(BooleanUtils.and(true));

        // Test with array of booleans
        boolean[] bools = {true, true, true, false, true};
        assertTrue(BooleanUtils.and(bools));

        bools = new boolean[] {false, false, false, false, false};
        assertFalse(BooleanUtils.and(bools));

        try {
            BooleanUtils.and(bools = new boolean[] {false, null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        // Test with empty array
        assertFalse(BooleanUtils.or());

        // Test with single boolean
        assertTrue(BooleanUtils.or(true));

        // Test with array of booleans
        boolean[] bools = {true, true, true, false, true};
        assertTrue(BooleanUtils.or(bools));

        bools = new boolean[] {false, false, false, false, false};
        assertFalse(BooleanUtils.or(bools));

        try {
            BooleanUtils.or(bools = new boolean[] {false, null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        // Test with empty array
        assertFalse(BooleanUtils.xor());

        // Test with single boolean
        assertFalse(BooleanUtils.xor(true));

        // Test with array of booleans
        boolean[] bools = {true, true, true, false, true};
        assertFalse(BooleanUtils.xor(bools));

        bools = new boolean[] {true, true, true, true, true};
        assertFalse(BooleanUtils.xor(bools));

        bools = new boolean[] {true, false, true, false, true};
        assertTrue(BooleanUtils.xor(bools));

        bools = new boolean[] {false, true, true, false, true};
        assertTrue(BooleanUtils.xor(bools));

        bools = new boolean[] {false, false, false, false, false};
        assertFalse(BooleanUtils.xor(bools));

        try {
            BooleanUtils.xor(bools = new boolean[] {false, null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}