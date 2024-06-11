package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean expected = Boolean.FALSE;
        assertEquals(expected, BooleanUtils.negate(bool));

        bool = null;
        assertNull(BooleanUtils.negate(bool));
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
    public void testIsFalse() {
        Boolean bool = Boolean.FALSE;
        assertTrue(BooleanUtils.isFalse(bool));

        bool = Boolean.TRUE;
        assertFalse(BooleanUtils.isFalse(bool));

        bool = null;
        assertFalse(BooleanUtils.isFalse(bool));
    }

    @Test
    public void testIsNotTrue() {
        Boolean bool = Boolean.TRUE;
        assertFalse(BooleanUtils.isNotTrue(bool));

        bool = Boolean.FALSE;
        assertTrue(BooleanUtils.isNotTrue(bool));

        bool = null;
        assertFalse(BooleanUtils.isNotTrue(bool));
    }

    @Test
    public void testIsNotFalse() {
        Boolean bool = Boolean.FALSE;
        assertFalse(BooleanUtils.isNotFalse(bool));

        bool = Boolean.TRUE;
        assertTrue(BooleanUtils.isNotFalse(bool));

        bool = null;
        assertFalse(BooleanUtils.isNotFalse(bool));
    }

    @Test
    public void testToBoolean() {
        Boolean bool = Boolean.TRUE;
        assertTrue(BooleanUtils.toBoolean(bool));

        bool = null;
        assertFalse(BooleanUtils.toBoolean(bool));

        int intValue = 1;
        assertTrue(BooleanUtils.toBoolean(intValue));

        intValue = 0;
        assertFalse(BooleanUtils.toBoolean(intValue));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool = Boolean.TRUE;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));

        bool = null;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));

        bool = Boolean.FALSE;
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));

        bool = null;
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));
    }

    @Test
    public void testToBooleanObject() {
        Boolean expected = Boolean.TRUE;
        int intValue = 1;
        assertEquals(expected, BooleanUtils.toBooleanObject(intValue));

        intValue = 0;
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(intValue));

        expected = null;
        intValue = -1;
        assertNull(BooleanUtils.toBooleanObject(intValue));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Boolean expected = Boolean.TRUE;
        Integer intValue = Integer.valueOf(1);
        assertEquals(expected, BooleanUtils.toBooleanObject(intValue));

        intValue = Integer.valueOf(0);
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(intValue));

        intValue = null;
        assertNull(BooleanUtils.toBooleanObject(intValue));
    }

    @Test
    public void testToBooleanObjectWithNullableInteger() {
        Integer intValue = Integer.valueOf(1);
        Boolean expected = Boolean.TRUE;
        assertEquals(expected, BooleanUtils.toBooleanObject(intValue, 1, 0, null));

        intValue = Integer.valueOf(0);
        expected = Boolean.FALSE;
        assertEquals(expected, BooleanUtils.toBooleanObject(intValue, 1, 0, null));

        intValue = null;
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
    }

    @Test
    public void testToInteger() {
        boolean bool = true;
        int expected = 1;
        assertEquals(expected, BooleanUtils.toInteger(bool));

        bool = false;
        expected = 0;
        assertEquals(expected, BooleanUtils.toInteger(bool));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        boolean bool = true;
        Integer expected = Integer.valueOf(1);
        assertEquals(expected, BooleanUtils.toIntegerObject(bool));

        bool = false;
        expected = Integer.valueOf(0);
        assertEquals(expected, BooleanUtils.toIntegerObject(bool));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Boolean bool = Boolean.TRUE;
        Integer expected = Integer.valueOf(1);
        assertEquals(expected, BooleanUtils.toIntegerObject(bool));

        bool = Boolean.FALSE;
        expected = Integer.valueOf(0);
        assertEquals(expected, BooleanUtils.toIntegerObject(bool));

        bool = null;
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithValue() {
        boolean bool = true;
        int expected = 1;
        assertEquals(expected, BooleanUtils.toInteger(bool, 1, 0));

        bool = false;
        expected = 0;
        assertEquals(expected, BooleanUtils.toInteger(bool, 1, 0));
    }

    @Test
    public void testToIntegerWithNullableBoolean() {
        Boolean bool = Boolean.TRUE;
        int expected = 1;
        assertEquals(expected, BooleanUtils.toInteger(bool, 1, 0, null));

        bool = Boolean.FALSE;
        expected = 0;
        assertEquals(expected, BooleanUtils.toInteger(bool, 1, 0, null));

        bool = null;
        assertEquals(0, BooleanUtils.toInteger(null, 1, 0, null));
    }

    @Test
    public void testToStringTrueFalse() {
        Boolean bool = Boolean.TRUE;
        String expected = "true";
        assertEquals(expected, BooleanUtils.toStringTrueFalse(bool));

        bool = Boolean.FALSE;
        expected = "false";
        assertEquals(expected, BooleanUtils.toStringTrueFalse(bool));

        bool = null;
        assertNull(BooleanUtils.toStringTrueFalse(bool));
    }

    @Test
    public void testToStringOnOff() {
        Boolean bool = Boolean.TRUE;
        String expected = "on";
        assertEquals(expected, BooleanUtils.toStringOnOff(bool));

        bool = Boolean.FALSE;
        expected = "off";
        assertEquals(expected, BooleanUtils.toStringOnOff(bool));

        bool = null;
        assertNull(BooleanUtils.toStringOnOff(bool));
    }

    @Test
    public void testToStringYesNo() {
        Boolean bool = Boolean.TRUE;
        String expected = "yes";
        assertEquals(expected, BooleanUtils.toStringYesNo(bool));

        bool = Boolean.FALSE;
        expected = "no";
        assertEquals(expected, BooleanUtils.toStringYesNo(bool));

        bool = null;
        assertNull(BooleanUtils.toStringYesNo(bool));
    }

    @Test
    public void testToString() {
        Boolean bool = Boolean.TRUE;
        String expected = "true";
        assertEquals(expected, BooleanUtils.toString(bool, "true", "false"));

        bool = Boolean.FALSE;
        expected = "false";
        assertEquals(expected, BooleanUtils.toString(bool, "true", "false"));

        bool = null;
        assertNull(BooleanUtils.toString(bool, "true", "false"));
    }

    @Test
    public void testAnd() {
        boolean[] array = {true, true, true};
        assertTrue(BooleanUtils.and(array));

        array = new boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new boolean[]{false, true, true};
        assertFalse(BooleanUtils.and(array));
    }

    @Test
    public void testAndWithBooleanArray() {
        Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        assertTrue(BooleanUtils.and(array));

        array = new Boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
        assertFalse(BooleanUtils.and(array));
    }

    @Test
    public void testOr() {
        boolean[] array = {true, true, true};
        assertTrue(BooleanUtils.or(array));

        array = new boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new boolean[]{false, false, false};
        assertFalse(BooleanUtils.or(array));
    }

    @Test
    public void testOrWithBooleanArray() {
        Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        assertTrue(BooleanUtils.or(array));

        array = new Boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
        assertFalse(BooleanUtils.or(array));
    }

    @Test
    public void testXor() {
        boolean[] array = {true, true, true};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new boolean[]{true, false, false};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[]{false, true, true};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[]{true, true, false};
        assertTrue(BooleanUtils.xor(array));
    }

    @Test
    public void testXorWithBooleanArray() {
        boolean[] array = {true, true, true};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[0];
        throw new IllegalArgumentException("Array is empty");

        array = new boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
        assertFalse(BooleanUtils.xor(array));

        array = new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
        assertTrue(BooleanUtils.xor(array));
    }
}