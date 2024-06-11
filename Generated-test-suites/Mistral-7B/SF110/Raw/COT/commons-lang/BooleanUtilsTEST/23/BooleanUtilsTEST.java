package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean input = Boolean.TRUE;
        Boolean expectedOutput = Boolean.FALSE;
        Boolean output = BooleanUtils.negate(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertNull(BooleanUtils.negate(input));
    }

    @Test
    public void testIsTrue() {
        Boolean input = Boolean.TRUE;
        boolean expectedOutput = true;
        boolean output = BooleanUtils.isTrue(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertFalse(BooleanUtils.isTrue(input));
    }

    @Test
    public void testIsNotTrue() {
        Boolean input = Boolean.TRUE;
        boolean expectedOutput = false;
        boolean output = BooleanUtils.isNotTrue(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertTrue(BooleanUtils.isNotTrue(input));
    }

    @Test
    public void testIsFalse() {
        Boolean input = Boolean.FALSE;
        boolean expectedOutput = true;
        boolean output = BooleanUtils.isFalse(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertFalse(BooleanUtils.isFalse(input));
    }

    @Test
    public void testIsNotFalse() {
        Boolean input = Boolean.FALSE;
        boolean expectedOutput = false;
        boolean output = BooleanUtils.isNotFalse(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertTrue(BooleanUtils.isNotFalse(input));
    }

    @Test
    public void testToBoolean() {
        Boolean input = Boolean.TRUE;
        boolean expectedOutput = true;
        boolean output = BooleanUtils.toBoolean(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertFalse(BooleanUtils.toBoolean(input));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean input = Boolean.TRUE;
        boolean expectedOutput = true;
        boolean output = BooleanUtils.toBooleanDefaultIfNull(input, false);
        assertEquals(expectedOutput, output);

        input = null;
        boolean defaultValue = true;
        boolean output = BooleanUtils.toBooleanDefaultIfNull(input, defaultValue);
        assertEquals(defaultValue, output);
    }

    @Test
    public void testToBooleanWithInteger() {
        int input = 1;
        boolean expectedOutput = true;
        boolean output = BooleanUtils.toBoolean(input);
        assertEquals(expectedOutput, output);

        input = 0;
        boolean output = BooleanUtils.toBoolean(input);
        assertFalse(output);
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Integer input = Integer.valueOf(1);
        Boolean expectedOutput = Boolean.TRUE;
        Boolean output = BooleanUtils.toBooleanObject(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertNull(BooleanUtils.toBooleanObject(input));
    }

    @Test
    public void testToBooleanWithNullableInteger() {
        Integer input = Integer.valueOf(1);
        Boolean expectedOutput = Boolean.TRUE;
        Boolean output = BooleanUtils.toBooleanObject(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertNull(BooleanUtils.toBooleanObject(input));
    }

    @Test
    public void testToInteger() {
        boolean input = true;
        int expectedOutput = 1;
        int output = BooleanUtils.toInteger(input);
        assertEquals(expectedOutput, output);

        input = false;
        int output = BooleanUtils.toInteger(input);
        assertEquals(0, output);
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Boolean input = Boolean.TRUE;
        Integer expectedOutput = NumberUtils.INTEGER_ONE;
        Integer output = BooleanUtils.toIntegerObject(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertNull(BooleanUtils.toIntegerObject(input));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Boolean input = Boolean.TRUE;
        Integer expectedOutput = NumberUtils.INTEGER_ONE;
        Integer output = BooleanUtils.toIntegerObject(input);
        assertEquals(expectedOutput, output);

        input = null;
        assertNull(BooleanUtils.toIntegerObject(input));
    }

    @Test
    public void testToIntegerWithBoolean() {
        boolean input = true;
        int expectedOutput = 1;
        int output = BooleanUtils.toInteger(input, 1, 0);
        assertEquals(expectedOutput, output);

        input = false;
        int output = BooleanUtils.toInteger(input, 1, 0);
        assertEquals(0, output);
    }

    @Test
    public void testToIntegerWithBooleanAndDefaultValues() {
        boolean input = true;
        int trueValue = 1;
        int falseValue = 2;
        int nullValue = 3;
        int expectedOutput = 1;
        int output = BooleanUtils.toInteger(input, trueValue, falseValue, nullValue);
        assertEquals(expectedOutput, output);

        input = false;
        int output = BooleanUtils.toInteger(input, trueValue, falseValue, nullValue);
        assertEquals(2, output);

        input = null;
        int output = BooleanUtils.toInteger(input, trueValue, falseValue, nullValue);
        assertEquals(3, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntegerAndInvalidValues() {
        int input = 3;
        Boolean expectedOutput = null;
        try {
            Boolean output = BooleanUtils.toBoolean(input, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntegerAndInvalidValues() {
        int input = 3;
        try {
            Boolean output = BooleanUtils.toBooleanObject(input, 1, 0, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithNullableIntegerAndNullValues() {
        Integer input = null;
        try {
            Boolean output = BooleanUtils.toBooleanObject(input, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithBooleanArrayAndNullValues() {
        boolean[] input = new boolean[] {true, null};
        try {
            Boolean output = BooleanUtils.and(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAnd() {
        boolean[] input = new boolean[] {true, true, true};
        boolean expectedOutput = true;
        boolean output = BooleanUtils.and(input);
        assertEquals(expectedOutput, output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {false, false, false};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {false, false, true};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {false, true, true};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {true, false, true};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new boolean[] {null, true, true};
        try {
            Boolean output = BooleanUtils.and(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArray() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean expectedOutput = Boolean.TRUE;
        Boolean output = BooleanUtils.and(input);
        assertEquals(expectedOutput, output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.and(input);
        assertFalse(output);

        input = new Boolean[] {null, Boolean.TRUE, Boolean.TRUE};
        try {
            Boolean output = BooleanUtils.and(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        boolean[] input = new boolean[] {true, true, true};
        boolean expectedOutput = true;
        boolean output = BooleanUtils.or(input);
        assertEquals(expectedOutput, output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new boolean[] {false, false, false};
        boolean output = BooleanUtils.or(input);
        assertFalse(output);

        input = new boolean[] {false, false, true};
        boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new boolean[] {false, true, true};
        boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new boolean[] {true, false, true};
        boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new boolean[] {null, true, true};
        try {
            Boolean output = BooleanUtils.or(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean expectedOutput = Boolean.TRUE;
        Boolean output = BooleanUtils.or(input);
        assertEquals(expectedOutput, output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        Boolean output = BooleanUtils.or(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
        Boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.or(input);
        assertTrue(output);

        input = new Boolean[] {null, Boolean.TRUE, Boolean.TRUE};
        try {
            Boolean output = BooleanUtils.or(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        boolean[] input = new boolean[] {true, true, true};
        boolean expectedOutput = false;
        boolean output = BooleanUtils.xor(input);
        assertEquals(expectedOutput, output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new boolean[] {false, false, false};
        boolean output = BooleanUtils.xor(input);
        assertFalse(output);

        input = new boolean[] {false, false, true};
        boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new boolean[] {false, true, true};
        boolean output = BooleanUtils.xor(input);
        assertFalse(output);

        input = new boolean[] {true, false, true};
        boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new boolean[] {true, true, false};
        boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new boolean[] {null, true, true};
        try {
            Boolean output = BooleanUtils.xor(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray() {
        Boolean[] input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean expectedOutput = Boolean.FALSE;
        Boolean output = BooleanUtils.xor(input);
        assertEquals(expectedOutput, output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        Boolean output = BooleanUtils.xor(input);
        assertFalse(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
        Boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        Boolean output = BooleanUtils.xor(input);
        assertTrue(output);

        input = new Boolean[] {null, Boolean.TRUE, Boolean.TRUE};
        try {
            Boolean output = BooleanUtils.xor(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}