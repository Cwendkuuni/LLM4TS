package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean expected = null;
        Boolean actual = BooleanUtils.negate(null);
        assertEquals(expected, actual);

        expected = Boolean.TRUE;
        actual = BooleanUtils.negate(expected);
        assertEquals(Boolean.FALSE, actual);

        expected = Boolean.FALSE;
        actual = BooleanUtils.negate(expected);
        assertEquals(Boolean.TRUE, actual);
    }

    @Test
    public void testIsTrue() {
        Boolean bool1 = Boolean.TRUE;
        assertTrue(BooleanUtils.isTrue(bool1));

        Boolean bool2 = Boolean.FALSE;
        assertFalse(BooleanUtils.isTrue(bool2));

        Boolean bool3 = null;
        assertFalse(BooleanUtils.isTrue(bool3));
    }

    @Test
    public void testIsNotTrue() {
        Boolean bool1 = Boolean.TRUE;
        assertFalse(BooleanUtils.isNotTrue(bool1));

        Boolean bool2 = Boolean.FALSE;
        assertTrue(BooleanUtils.isNotTrue(bool2));

        Boolean bool3 = null;
        assertFalse(BooleanUtils.isNotTrue(bool3));
    }

    @Test
    public void testIsFalse() {
        Boolean bool1 = Boolean.TRUE;
        assertFalse(BooleanUtils.isFalse(bool1));

        Boolean bool2 = Boolean.FALSE;
        assertTrue(BooleanUtils.isFalse(bool2));

        Boolean bool3 = null;
        assertFalse(BooleanUtils.isFalse(bool3));
    }

    @Test
    public void testIsNotFalse() {
        Boolean bool1 = Boolean.TRUE;
        assertTrue(BooleanUtils.isNotFalse(bool1));

        Boolean bool2 = Boolean.FALSE;
        assertFalse(BooleanUtils.isNotFalse(bool2));

        Boolean bool3 = null;
        assertFalse(BooleanUtils.isNotFalse(bool3));
    }

    @Test
    public void testToBoolean() {
        Boolean bool1 = Boolean.TRUE;
        assertTrue(BooleanUtils.toBoolean(bool1));

        Boolean bool2 = null;
        assertFalse(BooleanUtils.toBoolean(bool2));

        int intValue1 = 1;
        assertTrue(BooleanUtils.toBoolean(intValue1));

        int intValue2 = 0;
        assertFalse(BooleanUtils.toBoolean(intValue2));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool1 = Boolean.TRUE;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool1, false));

        Boolean bool2 = null;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool2, false));

        Boolean bool3 = Boolean.TRUE;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool3, true));

        Boolean bool4 = Boolean.FALSE;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool4, true));
    }

    @Test
    public void testToBooleanObject() {
        int intValue1 = 1;
        Boolean expected = Boolean.TRUE;
        Boolean actual = BooleanUtils.toBooleanObject(intValue1);
        assertEquals(expected, actual);

        int intValue2 = 0;
        Boolean actual2 = BooleanUtils.toBooleanObject(intValue2);
        assertNull(actual2);

        Integer intWrapper1 = Integer.valueOf(1);
        Boolean expected2 = Boolean.TRUE;
        Boolean actual3 = BooleanUtils.toBooleanObject(intWrapper1);
        assertEquals(expected2, actual3);

        Integer intWrapper2 = null;
        Boolean actual4 = BooleanUtils.toBooleanObject(intWrapper2);
        assertNull(actual4);
    }

    @Test
    public void testToBooleanObjectWithValues() {
        int trueValue = 1;
        int falseValue = 0;
        int nullValue = -1;
        Integer intValue = 1;

        Boolean expected = Boolean.TRUE;
        Boolean actual = BooleanUtils.toBooleanObject(intValue, trueValue, falseValue, nullValue);
        assertEquals(expected, actual);

        actual = BooleanUtils.toBooleanObject(null, trueValue, falseValue, nullValue);
        assertNull(actual);

        actual = BooleanUtils.toBooleanObject(intValue, null, falseValue, nullValue);
        assertNull(actual);

        actual = BooleanUtils.toBooleanObject(null, null, falseValue, nullValue);
        assertNull(actual);

        try {
            BooleanUtils.toBooleanObject(intValue, trueValue, falseValue, nullValue);
        } catch (IllegalArgumentException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testToBooleanObjectWithWrappers() {
        int trueValue = 1;
        int falseValue = 0;
        int nullValue = -1;
        Integer intValue1 = Integer.valueOf(1);
        Integer intValue2 = null;
        
        Boolean expected = Boolean.TRUE;
        Boolean actual = BooleanUtils.toBooleanObject(intValue1, trueValue, falseValue, nullValue);
        assertEquals(expected, actual);

        try {
            BooleanUtils.toBooleanObject(intValue1, null, falseValue, nullValue);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }

        try {
            BooleanUtils.toBooleanObject(intValue2, trueValue, falseValue, nullValue);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        Boolean bool1 = Boolean.TRUE;
        int expected = 1;
        int actual = BooleanUtils.toInteger(bool1);
        assertEquals(expected, actual);

        Boolean bool2 = Boolean.FALSE;
        int actual2 = BooleanUtils.toInteger(bool2);
        assertEquals(0, actual2);

        Boolean bool3 = null;
        int actual3 = BooleanUtils.toInteger(bool3);
        assertEquals(0, actual3);
    }

    @Test
    public void testToStringTrueFalse() {
        Boolean bool1 = Boolean.TRUE;
        String expected = "true";
        String actual = BooleanUtils.toStringTrueFalse(bool1);
        assertEquals(expected, actual);

        Boolean bool2 = Boolean.FALSE;
        String expected2 = "false";
        String actual2 = BooleanUtils.toStringTrueFalse(bool2);
        assertEquals(expected2, actual2);

        Boolean bool3 = null;
        String actual3 = BooleanUtils.toStringTrueFalse(bool3);
        assertNull(actual3);
    }

    @Test
    public void testToStringOnOff() {
        Boolean bool1 = Boolean.TRUE;
        String expected = "on";
        String actual = BooleanUtils.toStringOnOff(bool1);
        assertEquals(expected, actual);

        Boolean bool2 = Boolean.FALSE;
        String expected2 = "off";
        String actual2 = BooleanUtils.toStringOnOff(bool2);
        assertEquals(expected2, actual2);

        Boolean bool3 = null;
        String actual3 = BooleanUtils.toStringOnOff(bool3);
        assertNull(actual3);
    }

    @Test
    public void testToStringYesNo() {
        Boolean bool1 = Boolean.TRUE;
        String expected = "yes";
        String actual = BooleanUtils.toStringYesNo(bool1);
        assertEquals(expected, actual);

        Boolean bool2 = Boolean.FALSE;
        String expected2 = "no";
        String actual2 = BooleanUtils.toStringYesNo(bool2);
        assertEquals(expected2, actual2);

        Boolean bool3 = null;
        String actual3 = BooleanUtils.toStringYesNo(bool3);
        assertNull(actual3);
    }

    @Test
    public void testToString() {
        Boolean bool1 = Boolean.TRUE;
        String expected = "true";
        String actual = BooleanUtils.toString(bool1, "true", "false");
        assertEquals(expected, actual);

        Boolean bool2 = Boolean.FALSE;
        String expected2 = "false";
        String actual2 = BooleanUtils.toString(bool2, "true", "false");
        assertEquals(expected2, actual2);

        Boolean bool3 = null;
        String actual3 = BooleanUtils.toString(bool3, "true", "false");
        assertNull(actual3);
    }

    @Test
    public void testAnd() {
        boolean[] input = {true, false, true, false};
        boolean expected = true;
        boolean actual = BooleanUtils.and(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {true, null};
            Boolean actualBoolean = BooleanUtils.and(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndBoolean() {
        Boolean[] input = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
        Boolean expected = Boolean.TRUE;
        Boolean actual = BooleanUtils.and(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {null, Boolean.FALSE};
            Boolean actualBoolean = BooleanUtils.and(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        boolean[] input = {true, false, true, false};
        boolean expected = false;
        boolean actual = BooleanUtils.or(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {true, null};
            Boolean actualBoolean = BooleanUtils.or(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrBoolean() {
        Boolean[] input = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
        Boolean expected = Boolean.TRUE;
        Boolean actual = BooleanUtils.or(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {null, Boolean.FALSE};
            Boolean actualBoolean = BooleanUtils.or(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        boolean[] input = {true, false, true, false};
        boolean expected = false;
        boolean actual = BooleanUtils.xor(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {true, null};
            Boolean actualBoolean = BooleanUtils.xor(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorBoolean() {
        Boolean[] input = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
        Boolean expected = Boolean.FALSE;
        Boolean actual = BooleanUtils.xor(input);
        assertEquals(expected, actual);

        try {
            Boolean[] inputBoolean = {null, Boolean.FALSE};
            Boolean actualBoolean = BooleanUtils.xor(inputBoolean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}