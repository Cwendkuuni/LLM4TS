package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean negatedBool = BooleanUtils.negate(bool);
        assertTrue(negatedBool.equals(Boolean.FALSE));

        bool = Boolean.FALSE;
        negatedBool = BooleanUtils.negate(bool);
        assertTrue(negatedBool.equals(Boolean.TRUE));

        bool = null;
        negatedBool = BooleanUtils.negate(bool);
        assertNull(negatedBool);
    }

    @Test
    public void testIsTrue() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isTrue(bool);
        assertTrue(result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isTrue(bool);
        assertFalse(result);

        bool = null;
        result = BooleanUtils.isTrue(bool);
        assertFalse(result);
    }

    @Test
    public void testIsNotTrue() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isNotTrue(bool);
        assertFalse(result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isNotTrue(bool);
        assertTrue(result);

        bool = null;
        result = BooleanUtils.isNotTrue(bool);
        assertFalse(result);
    }

    @Test
    public void testIsFalse() {
        Boolean bool = Boolean.FALSE;
        boolean result = BooleanUtils.isFalse(bool);
        assertTrue(result);

        bool = Boolean.TRUE;
        result = BooleanUtils.isFalse(bool);
        assertFalse(result);

        bool = null;
        result = BooleanUtils.isFalse(bool);
        assertFalse(result);
    }

    @Test
    public void testIsNotFalse() {
        Boolean bool = Boolean.FALSE;
        boolean result = BooleanUtils.isNotFalse(bool);
        assertFalse(result);

        bool = Boolean.TRUE;
        result = BooleanUtils.isNotFalse(bool);
        assertTrue(result);

        bool = null;
        result = BooleanUtils.isNotFalse(bool);
        assertFalse(result);
    }

    @Test
    public void testToBoolean() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.toBoolean(bool);
        assertTrue(result);

        bool = null;
        result = BooleanUtils.toBoolean(bool);
        assertFalse(result);

        int intValue = 1;
        result = BooleanUtils.toBoolean(intValue);
        assertTrue(result);

        intValue = 0;
        result = BooleanUtils.toBoolean(intValue);
        assertFalse(result);
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        assertTrue(result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        assertTrue(result);

        bool = null;
        result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        assertFalse(result);

        result = BooleanUtils.toBooleanDefaultIfNull(bool, true);
        assertTrue(result);
    }

    @Test
    public void testToBooleanWithBoolean() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.toBoolean(bool, true, false);
        assertTrue(result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toBoolean(bool, true, false);
        assertFalse(result);
    }

    @Test
    public void testToBooleanObject() {
        Boolean bool = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(bool);
        assertTrue(result.equals(Boolean.TRUE));

        bool = Boolean.FALSE;
        result = BooleanUtils.toBooleanObject(bool);
        assertTrue(result.equals(Boolean.FALSE));

        bool = null;
        result = BooleanUtils.toBooleanObject(bool);
        assertNull(result);
    }

    @Test
    public void testToBooleanObjectWithBoolean() {
        Boolean bool = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(bool, true, false, null);
        assertTrue(result.equals(Boolean.TRUE));

        bool = Boolean.FALSE;
        result = BooleanUtils.toBooleanObject(bool, true, false, null);
        assertTrue(result.equals(Boolean.FALSE));

        bool = null;
        result = BooleanUtils.toBooleanObject(bool, true, false, null);
        assertNull(result);
    }

    @Test
    public void testToInteger() {
        boolean bool = true;
        int result = BooleanUtils.toInteger(bool);
        assertEquals(1, result);

        bool = false;
        result = BooleanUtils.toInteger(bool);
        assertEquals(0, result);
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Boolean bool = Boolean.TRUE;
        Integer result = BooleanUtils.toIntegerObject(bool);
        assertEquals(Integer.ONE, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toIntegerObject(bool);
        assertEquals(Integer.ZERO, result);
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Boolean bool = Boolean.TRUE;
        Integer result = BooleanUtils.toIntegerObject(bool);
        assertEquals(Integer.ONE, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toIntegerObject(bool);
        assertEquals(Integer.ZERO, result);

        bool = null;
        result = BooleanUtils.toIntegerObject(bool);
        assertNull(result);
    }

    @Test
    public void testToIntegerWithBoolean() {
        boolean bool = true;
        int result = BooleanUtils.toInteger(bool, 1, 0);
        assertEquals(1, result);

        bool = false;
        result = BooleanUtils.toInteger(bool, 1, 0);
        assertEquals(0, result);
    }

    @Test
    public void testToIntegerWithBooleanNullable() {
        boolean bool = true;
        int result = BooleanUtils.toInteger(bool, 1, 0, null);
        assertEquals(1, result);

        bool = false;
        result = BooleanUtils.toInteger(bool, 1, 0, null);
        assertEquals(0, result);

        bool = null;
        result = BooleanUtils.toInteger(bool, 1, 0, null);
        assertEquals(0, result);
    }

    @Test
    public void testToBooleanObjectWithString() {
        String str = "true";
        Boolean result = BooleanUtils.toBooleanObject(str);
        assertTrue(result.equals(Boolean.TRUE));

        str = "false";
        result = BooleanUtils.toBooleanObject(str);
        assertTrue(result.equals(Boolean.FALSE));

        str = null;
        result = BooleanUtils.toBooleanObject(str);
        assertNull(result);
    }

    @Test
    public void testToBooleanObjectWithNullableString() {
        String str = "true";
        Boolean result = BooleanUtils.toBooleanObject(str, "true", "false", null);
        assertTrue(result.equals(Boolean.TRUE));

        str = "false";
        result = BooleanUtils.toBooleanObject(str, "true", "false", null);
        assertTrue(result.equals(Boolean.FALSE));

        str = null;
        result = BooleanUtils.toBooleanObject(str, "true", "false", null);
        assertNull(result);
    }

    @Test
    public void testToBooleanWithString() {
        String str = "true";
        boolean result = BooleanUtils.toBoolean(str);
        assertTrue(result);

        str = "false";
        result = BooleanUtils.toBoolean(str);
        assertFalse(result);

        str = null;
        result = BooleanUtils.toBoolean(str);
        assertFalse(result);
    }

    @Test
    public void testToBooleanWithStringAndValues() {
        String str = "true";
        boolean result = BooleanUtils.toBoolean(str, "true", "false");
        assertTrue(result);

        str = "false";
        result = BooleanUtils.toBoolean(str, "true", "false");
        assertFalse(result);

        str = null;
        result = BooleanUtils.toBoolean(str, "true", "false");
        assertFalse(result);
    }

    @Test
    public void testToStringTrueFalse() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringTrueFalse(bool);
        assertEquals("true", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringTrueFalse(bool);
        assertEquals("false", result);

        bool = null;
        result = BooleanUtils.toStringTrueFalse(bool);
        assertEquals("", result);
    }

    @Test
    public void testToStringOnOff() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringOnOff(bool);
        assertEquals("on", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringOnOff(bool);
        assertEquals("off", result);

        bool = null;
        result = BooleanUtils.toStringOnOff(bool);
        assertEquals("", result);
    }

    @Test
    public void testToStringYesNo() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringYesNo(bool);
        assertEquals("yes", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringYesNo(bool);
        assertEquals("no", result);

        bool = null;
        result = BooleanUtils.toStringYesNo(bool);
        assertEquals("", result);
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toString(bool, "true", "false");
        assertEquals("true", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toString(bool, "true", "false");
        assertEquals("false", result);
    }

    @Test
    public void testToStringWithBooleanAndValues() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toString(bool, "on", "off");
        assertEquals("on", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toString(bool, "on", "off");
        assertEquals("off", result);
    }

    @Test
    public void testAnd() {
        boolean[] array = {true, true, true};
        boolean result = BooleanUtils.and(array);
        assertTrue(result);

        array = new boolean[] {true, true, false};
        result = BooleanUtils.and(array);
        assertFalse(result);

        array = new boolean[] {false, false, false};
        result = BooleanUtils.and(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.and(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArray() {
        Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.and(array);
        assertTrue(result.equals(Boolean.TRUE));

        array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        result = BooleanUtils.and(array);
        assertFalse(result);

        array = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        result = BooleanUtils.and(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.and(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        boolean[] array = {true, true, true};
        boolean result = BooleanUtils.or(array);
        assertTrue(result);

        array = new boolean[] {true, true, false};
        result = BooleanUtils.or(array);
        assertTrue(result);

        array = new boolean[] {false, false, false};
        result = BooleanUtils.or(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.or(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray() {
        Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.or(array);
        assertTrue(result.equals(Boolean.TRUE));

        array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        result = BooleanUtils.or(array);
        assertTrue(result.equals(Boolean.TRUE));

        array = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        result = BooleanUtils.or(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.or(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        boolean[] array = {true, true, true};
        boolean result = BooleanUtils.xor(array);
        assertFalse(result);

        array = new boolean[] {true, true, false};
        result = BooleanUtils.xor(array);
        assertTrue(result);

        array = new boolean[] {false, false, false};
        result = BooleanUtils.xor(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.xor(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray() {
        Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.xor(array);
        assertFalse(result.equals(Boolean.TRUE));

        array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        result = BooleanUtils.xor(array);
        assertTrue(result.equals(Boolean.TRUE));

        array = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        result = BooleanUtils.xor(array);
        assertFalse(result);

        array = null;
        try {
            BooleanUtils.xor(array);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}