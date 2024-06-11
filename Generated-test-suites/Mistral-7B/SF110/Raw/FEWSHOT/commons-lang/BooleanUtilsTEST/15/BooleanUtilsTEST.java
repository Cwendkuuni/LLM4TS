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
        assertEquals(Boolean.FALSE, negatedBool);

        bool = Boolean.FALSE;
        negatedBool = BooleanUtils.negate(bool);
        assertEquals(Boolean.TRUE, negatedBool);

        bool = null;
        negatedBool = BooleanUtils.negate(bool);
        assertNull(negatedBool);
    }

    @Test
    public void testIsTrue() {
        Boolean bool = Boolean.TRUE;
        assertTrue(BooleanUtils.isTrue(bool));

        bool = Boolean.FALSE;
        assertFalse(BooleanUtils.isTrue(bool));

        bool = null;
        assertFalse(BooleanUtils.isTrue(bool));
    }

    @Test
    public void testIsNotTrue() {
        Boolean bool = Boolean.TRUE;
        assertFalse(BooleanUtils.isNotTrue(bool));

        bool = Boolean.FALSE;
        assertTrue(BooleanUtils.isNotTrue(bool));

        bool = null;
        assertNull(BooleanUtils.isNotTrue(bool));
    }

    @Test
    public void testIsFalse() {
        Boolean bool = Boolean.FALSE;
        assertTrue(BooleanUtils.isFalse(bool));

        bool = Boolean.TRUE;
        assertFalse(BooleanUtils.isFalse(bool));

        bool = null;
        assertFalse(BooleanUtils.isFalse(bool));
    }

    @Test
    public void testIsNotFalse() {
        Boolean bool = Boolean.FALSE;
        assertFalse(BooleanUtils.isNotFalse(bool));

        bool = Boolean.TRUE;
        assertTrue(BooleanUtils.isNotFalse(bool));

        bool = null;
        assertNull(BooleanUtils.isNotFalse(bool));
    }

    @Test
    public void testToBoolean() {
        Boolean bool = Boolean.TRUE;
        assertTrue(BooleanUtils.toBoolean(bool));

        int intValue = 1;
        assertTrue(BooleanUtils.toBoolean(intValue));

        int intValueFalse = 0;
        assertFalse(BooleanUtils.toBoolean(intValueFalse));

        int intValueNull = -1;
        assertFalse(BooleanUtils.toBoolean(intValueNull));

        Integer integer = Integer.valueOf(1);
        assertTrue(BooleanUtils.toBoolean(integer));

        integer = Integer.valueOf(0);
        assertFalse(BooleanUtils.toBoolean(integer));

        integer = null;
        assertFalse(BooleanUtils.toBoolean(integer));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool = Boolean.TRUE;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));

        bool = null;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));

        bool = Boolean.FALSE;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, true));

        int intValue = 1;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(intValue, false));

        int intValueFalse = 0;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(intValueFalse, true));

        int intValueNull = -1;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(intValueNull, true));

        Integer integer = Integer.valueOf(1);
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(integer, false));

        integer = Integer.valueOf(0);
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(integer, true));

        integer = null;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(integer, true));
    }

    @Test
    public void testToBooleanObject() {
        Boolean bool = Boolean.TRUE;
        assertEquals(bool, BooleanUtils.toBooleanObject(true));

        int intValue = 1;
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(intValue));

        int intValueFalse = 0;
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(intValueFalse));

        int intValueNull = -1;
        assertNull(BooleanUtils.toBooleanObject(intValueNull));

        Integer integer = Integer.valueOf(1);
        assertEquals(integer, BooleanUtils.toBooleanObject(integer));

        integer = Integer.valueOf(0);
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(integer));

        integer = null;
        assertNull(BooleanUtils.toBooleanObject(integer));
    }

    @Test
    public void testToBooleanObjectWithValues() {
        Boolean bool = Boolean.TRUE;
        assertEquals(bool, BooleanUtils.toBooleanObject(true, true, false, false, null));

        int intValue = 1;
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(intValue, 1, 0, 0, null));

        int intValueFalse = 0;
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(intValueFalse, 1, 0, 0, null));

        int intValueNull = -1;
        assertNull(BooleanUtils.toBooleanObject(intValueNull, 1, 0, 0, null));

        Integer integer = Integer.valueOf(1);
        assertEquals(integer, BooleanUtils.toBooleanObject(integer, integer, null, null, null));

        integer = Integer.valueOf(0);
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(integer, integer, null, null, null));

        integer = null;
        assertNull(BooleanUtils.toBooleanObject(integer, integer, null, null, null));
    }

    @Test
    public void testToInteger() {
        Boolean bool = Boolean.TRUE;
        assertEquals(1, BooleanUtils.toInteger(bool));

        int intValue = 1;
        assertEquals(intValue, BooleanUtils.toInteger(BooleanUtils.toBooleanObject(intValue)));

        int intValueFalse = 0;
        assertEquals(intValueFalse, BooleanUtils.toInteger(BooleanUtils.toBooleanObject(intValueFalse)));

        int intValueNull = -1;
        assertEquals(intValueNull, BooleanUtils.toInteger(BooleanUtils.toBooleanObject(intValueNull)));

        Boolean boolNull = null;
        assertEquals(intValueNull, BooleanUtils.toInteger(boolNull));
    }

    @Test
    public void testToStringTrueFalse() {
        Boolean bool = Boolean.TRUE;
        assertEquals("true", BooleanUtils.toStringTrueFalse(bool));

        bool = Boolean.FALSE;
        assertEquals("false", BooleanUtils.toStringTrueFalse(bool));
    }

    @Test
    public void testToStringOnOff() {
        Boolean bool = Boolean.TRUE;
        assertEquals("on", BooleanUtils.toStringOnOff(bool));

        bool = Boolean.FALSE;
        assertEquals("off", BooleanUtils.toStringOnOff(bool));
    }

    @Test
    public void testToStringYesNo() {
        Boolean bool = Boolean.TRUE;
        assertEquals("yes", BooleanUtils.toStringYesNo(bool));

        bool = Boolean.FALSE;
        assertEquals("no", BooleanUtils.toStringYesNo(bool));
    }

    @Test
    public void testToStringWithValues() {
        Boolean bool = Boolean.TRUE;
        assertEquals("true", BooleanUtils.toString(bool, "true", "false"));

        bool = Boolean.FALSE;
        assertEquals("false", BooleanUtils.toString(bool, "true", "false"));

        int intValue = 1;
        assertEquals("true", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValue), "true", "false"));

        int intValueFalse = 0;
        assertEquals("false", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValueFalse), "true", "false"));

        int intValueNull = -1;
        assertEquals("", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValueNull), "true", "false"));

        Boolean boolNull = null;
        assertEquals("", BooleanUtils.toString(boolNull, "true", "false"));
    }

    @Test
    public void testToStringWithStrings() {
        Boolean bool = Boolean.TRUE;
        assertEquals("true", BooleanUtils.toString(bool, "yes", "no", ""));

        bool = Boolean.FALSE;
        assertEquals("false", BooleanUtils.toString(bool, "yes", "no", ""));

        int intValue = 1;
        assertEquals("true", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValue), "yes", "no", ""));

        int intValueFalse = 0;
        assertEquals("false", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValueFalse), "yes", "no", ""));

        int intValueNull = -1;
        assertEquals("", BooleanUtils.toString(BooleanUtils.toBooleanObject(intValueNull), "yes", "no", ""));

        Boolean boolNull = null;
        assertEquals("", BooleanUtils.toString(boolNull, "yes", "no", ""));
    }

    @Test
    public void testAnd() {
        boolean[] booleanArray = {true, true, true, true};
        assertTrue(BooleanUtils.and(booleanArray));

        boolean[] booleanArrayFalse = {true, false, false, true};
        assertFalse(BooleanUtils.and(booleanArrayFalse));

        boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.and(booleanArrayEmpty));

        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBoolean() {
        Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        assertTrue(BooleanUtils.and(booleanArray));

        Boolean[] booleanArrayFalse = {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        assertFalse(BooleanUtils.and(booleanArrayFalse));

        Boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.and(booleanArrayEmpty));

        try {
            BooleanUtils.and(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        boolean[] booleanArray = {true, true, true, true};
        assertTrue(BooleanUtils.or(booleanArray));

        boolean[] booleanArrayFalse = {true, false, false, true};
        assertFalse(BooleanUtils.or(booleanArrayFalse));

        boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.or(booleanArrayEmpty));

        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBoolean() {
        Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        assertTrue(BooleanUtils.or(booleanArray));

        Boolean[] booleanArrayFalse = {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        assertFalse(BooleanUtils.or(booleanArrayFalse));

        Boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.or(booleanArrayEmpty));

        try {
            BooleanUtils.or(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        boolean[] booleanArray = {true, true, true, true};
        assertFalse(BooleanUtils.xor(booleanArray));

        boolean[] booleanArrayTrue = {true, true, true, false};
        assertTrue(BooleanUtils.xor(booleanArrayTrue));

        boolean[] booleanArrayFalse = {true, false, false, true};
        assertFalse(BooleanUtils.xor(booleanArrayFalse));

        boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.xor(booleanArrayEmpty));

        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBoolean() {
        Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        assertFalse(BooleanUtils.xor(booleanArray));

        Boolean[] booleanArrayTrue = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        assertTrue(BooleanUtils.xor(booleanArrayTrue));

        Boolean[] booleanArrayFalse = {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
        assertFalse(BooleanUtils.xor(booleanArrayFalse));

        Boolean[] booleanArrayEmpty = {};
        assertFalse(BooleanUtils.xor(booleanArrayEmpty));

        try {
            BooleanUtils.xor(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}