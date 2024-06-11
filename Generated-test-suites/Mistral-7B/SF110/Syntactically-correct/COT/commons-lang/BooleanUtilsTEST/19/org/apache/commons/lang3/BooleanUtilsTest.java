package org.junit.tests; // replace with your package name if necessary

import static org.apache.commons.lang3.BooleanUtils.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    // Test for negate method
    @Test
    public void testNegate() {
        assertNull(negate(null));
        assertTrue(negate(Boolean.TRUE) == Boolean.FALSE);
        assertFalse(negate(Boolean.FALSE) == Boolean.TRUE);
    }

    // Test for isTrue method
    @Test
    public void testIsTrue() {
        assertTrue(isTrue(Boolean.TRUE));
        assertFalse(isTrue(Boolean.FALSE));
    }

    // Test for isNotTrue method
    @Test
    public void testIsNotTrue() {
        assertFalse(isNotTrue(Boolean.TRUE));
        assertTrue(isNotTrue(Boolean.FALSE));
    }

    // Test for isFalse method
    @Test
    public void testIsFalse() {
        assertFalse(isFalse(Boolean.TRUE));
        assertTrue(isFalse(Boolean.FALSE));
    }

    // Test for isNotFalse method
    @Test
    public void testIsNotFalse() {
        assertTrue(isNotFalse(Boolean.TRUE));
        assertFalse(isNotFalse(Boolean.FALSE));
    }

    // Test for toBoolean method
    @Test
    public void testToBoolean() {
        assertTrue(toBoolean(Boolean.TRUE) == Boolean.TRUE);
        assertFalse(toBoolean(Boolean.FALSE) == Boolean.FALSE);
        assertNull(toBoolean(null));
    }

    // Test for toBooleanDefaultIfNull method
    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, true) == Boolean.TRUE);
        assertTrue(toBooleanDefaultIfNull(Boolean.FALSE, true) == true);
        assertTrue(toBooleanDefaultIfNull(null, true) == true);
        assertFalse(toBooleanDefaultIfNull(Boolean.TRUE, false) == false);
        assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, false) == false);
        assertFalse(toBooleanDefaultIfNull(null, false) == false);
    }

    // Test for toBoolean method with int value
    @Test
    public void testToBooleanWithIntValue() {
        assertTrue(toBoolean(1) == Boolean.TRUE);
        assertFalse(toBoolean(0) == Boolean.FALSE);
        assertFalse(toBoolean(-1) == null);
    }

    // Test for toBooleanObject method with int value
    @Test
    public void testToBooleanObjectWithIntValue() {
        assertEquals(Boolean.TRUE, toBooleanObject(1));
        assertEquals(Boolean.FALSE, toBooleanObject(0));
        assertNull(toBooleanObject(-1));
    }

    // Test for toBoolean method with int values and specified true/false values
    @Test
    public void testToBooleanWithIntValuesAndSpecifiedTrueFalseValues() {
        assertTrue(toBoolean(1, 1, 0) == true);
        assertFalse(toBoolean(0, 1, 0) == false);
        try {
            toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for toBooleanObject method with Integer object
    @Test
    public void testToBooleanObjectWithIntegerObject() {
        assertEquals(Boolean.TRUE, toBooleanObject(Integer.valueOf(1)));
        assertEquals(Boolean.FALSE, toBooleanObject(Integer.valueOf(0)));
        assertNull(toBooleanObject(null));
    }

    // Test for toBooleanObject method with Integer objects and specified true/false values
    @Test
    public void testToBooleanObjectWithIntegerObjectsAndSpecifiedTrueFalseValues() {
        assertEquals(Boolean.TRUE, toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), null, null));
        assertEquals(Boolean.FALSE, toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), null, null));
        assertNull(toBooleanObject(null, null, null, null));
        try {
            toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for toInteger method
    @Test
    public void testToInteger() {
        assertEquals(1, toInteger(true));
        assertEquals(0, toInteger(false));
    }

    // Test for toIntegerObject method with Boolean object
    @Test
    public void testToIntegerObjectWithBooleanObject() {
        assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(true));
        assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(false));
        assertNull(toIntegerObject(null));
    }

    // Test for toIntegerObject method with Boolean object and specified true/false values
    @Test
    public void testToIntegerObjectWithBooleanObjectAndSpecifiedTrueFalseValues() {
        assertEquals(Integer.valueOf(1), toIntegerObject(true, 1, 0));
        assertEquals(Integer.valueOf(0), toIntegerObject(false, 1, 0));
    }

    // Test for toBooleanObject method with String value
    @Test
    public void testToBooleanObjectWithStringValue() {
        assertEquals(Boolean.TRUE, toBooleanObject("true"));
        assertNull(toBooleanObject(null));
        try {
            toBooleanObject("false");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for toBooleanObject method with String values and specified true/false/null strings
    @Test
    public void testToBooleanObjectWithStringValuesAndSpecifiedTrueFalseNullStrings() {
        assertEquals(Boolean.TRUE, toBooleanObject("true", "true", null, null));
        assertNull(toBooleanObject(null, "true", null, null));
        try {
            toBooleanObject("false", "true", null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for toBoolean method with String value
    @Test
    public void testToBooleanWithStringValue() {
        assertTrue(toBoolean("true"));
        assertFalse(toBoolean("false"));
    }

    // Test for toBoolean method with String values and specified true/false strings
    @Test
    public void testToBooleanWithStringValuesAndSpecifiedTrueFalseStrings() {
        assertTrue(toBoolean("true", "true"));
        assertFalse(toBoolean("false", "false"));
        try {
            toBoolean("true", "false");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for and method with boolean array
    @Test
    public void testAnd() {
        assertTrue(and(new boolean[] {true, true, true}));
        assertFalse(and(new boolean[] {false, false, false}));
        assertFalse(and(new boolean[] {true, false, true}));
        assertFalse(and(new boolean[] {null}));
        try {
            and(new boolean[] {});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for and method with Boolean array
    @Test
    public void testAndWithBooleanArray() {
        assertTrue(and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(and(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(and(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertNull(and(new Boolean[] {}));
        try {
            and(new Boolean[] {Boolean.TRUE, null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for or method with boolean array
    @Test
    public void testOr() {
        assertTrue(or(new boolean[] {true, true, true}));
        assertFalse(or(new boolean[] {false, false, false}));
        assertTrue(or(new boolean[] {true, false, true}));
        assertFalse(or(new boolean[] {null}));
        try {
            or(new boolean[] {});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for or method with Boolean array
    @Test
    public void testOrWithBooleanArray() {
        assertTrue(or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(or(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertTrue(or(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertNull(or(new Boolean[] {}));
        try {
            or(new Boolean[] {Boolean.TRUE, null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for xor method with boolean array
    @Test
    public void testXor() {
        assertTrue(xor(new boolean[] {true, true, false}));
        assertFalse(xor(new boolean[] {false, false, false}));
        assertFalse(xor(new boolean[] {true, false, true}));
        assertFalse(xor(new boolean[] {null}));
        try {
            xor(new boolean[] {});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test for xor method with Boolean array
    @Test
    public void testXorWithBooleanArray() {
        assertTrue(xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertNull(xor(new Boolean[] {}));
        try {
            xor(new Boolean[] {Boolean.TRUE, null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}