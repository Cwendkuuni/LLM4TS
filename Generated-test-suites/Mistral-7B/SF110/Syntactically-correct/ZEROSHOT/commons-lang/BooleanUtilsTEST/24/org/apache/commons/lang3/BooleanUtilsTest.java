package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @org.junit.Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean expected = Boolean.FALSE;
        Boolean result = BooleanUtils.negate(bool);
        assertEquals(expected, result);

        bool = Boolean.FALSE;
        expected = Boolean.TRUE;
        result = BooleanUtils.negate(bool);
        assertEquals(expected, result);

        bool = null;
        assertNull(BooleanUtils.negate(bool));
    }

    @org.junit.Test
    public void testIsTrue() {
        Boolean bool = Boolean.TRUE;
        boolean expected = true;
        boolean result = BooleanUtils.isTrue(bool);
        assertEquals(expected, result);

        bool = Boolean.FALSE;
        expected = false;
        result = BooleanUtils.isTrue(bool);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.isTrue(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsNotTrue() {
        Boolean bool = Boolean.TRUE;
        boolean expected = false;
        boolean result = BooleanUtils.isNotTrue(bool);
        assertEquals(expected, result);

        bool = Boolean.FALSE;
        expected = true;
        result = BooleanUtils.isNotTrue(bool);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.isNotTrue(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsFalse() {
        Boolean bool = Boolean.FALSE;
        boolean expected = true;
        boolean result = BooleanUtils.isFalse(bool);
        assertEquals(expected, result);

        bool = Boolean.TRUE;
        expected = false;
        result = BooleanUtils.isFalse(bool);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.isFalse(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsNotFalse() {
        Boolean bool = Boolean.FALSE;
        boolean expected = false;
        boolean result = BooleanUtils.isNotFalse(bool);
        assertEquals(expected, result);

        bool = Boolean.TRUE;
        expected = true;
        result = BooleanUtils.isNotFalse(bool);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.isNotFalse(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToBoolean() {
        Boolean expected = Boolean.TRUE;
        boolean result = BooleanUtils.toBoolean(expected);
        assertEquals(expected, result);

        int value = 1;
        expected = Boolean.TRUE;
        result = BooleanUtils.toBoolean(value);
        assertEquals(expected, result);

        value = 0;
        expected = Boolean.FALSE;
        result = BooleanUtils.toBoolean(value);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool = Boolean.TRUE;
        boolean expected = true;
        boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToBooleanWithBoolean() {
        Boolean bool = Boolean.TRUE;
        boolean expected = true;
        boolean result = BooleanUtils.toBoolean(bool);
        assertEquals(expected, result);

        bool = Boolean.FALSE;
        expected = false;
        result = BooleanUtils.toBoolean(bool);
        assertEquals(expected, result);

        bool = null;
        expected = false;
        result = BooleanUtils.toBoolean(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToBooleanWithInt() {
        boolean expected = true;
        boolean result = BooleanUtils.toBoolean(1);
        assertEquals(expected, result);

        expected = false;
        result = BooleanUtils.toBoolean(0);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToBooleanObject() {
        int value = 1;
        Boolean expected = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(value);
        assertEquals(expected, result);

        value = 0;
        expected = Boolean.FALSE;
        result = BooleanUtils.toBooleanObject(value);
        assertEquals(expected, result);

        value = -1;
        assertNull(BooleanUtils.toBooleanObject(value));
    }

    @org.junit.Test
    public void testToBooleanObjectWithInt() {
        int value = 1;
        Boolean expected = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(value, 1, 0);
        assertEquals(expected, result);

        value = 0;
        expected = Boolean.FALSE;
        result = BooleanUtils.toBooleanObject(value, 1, 0);
        assertEquals(expected, result);

        value = -1;
        assertNull(BooleanUtils.toBooleanObject(value, 1, 0));
    }

    @org.junit.Test
    public void testToBooleanObjectWithNullableInt() {
        Integer integer = Integer.valueOf(1);
        Boolean expected = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(integer);
        assertEquals(expected, result);

        integer = null;
        assertNull(BooleanUtils.toBooleanObject(integer));
    }

    @org.junit.Test
    public void testToBooleanObjectWithNullableInteger() {
        Integer integer = Integer.valueOf(1);
        Boolean expected = Boolean.TRUE;
        Boolean result = BooleanUtils.toBooleanObject(integer, null, null, null);
        assertEquals(expected, result);

        integer = null;
        assertNull(BooleanUtils.toBooleanObject(integer, null, null, null));
    }

    @org.junit.Test
    public void testToInteger() {
        boolean bool = true;
        int expected = 1;
        int result = BooleanUtils.toInteger(bool);
        assertEquals(expected, result);

        bool = false;
        expected = 0;
        result = BooleanUtils.toInteger(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToIntegerObjectWithBoolean() {
        boolean bool = true;
        Integer expected = NumberUtils.INTEGER_ONE;
        Integer result = BooleanUtils.toIntegerObject(bool);
        assertEquals(expected, result);

        bool = false;
        expected = NumberUtils.INTEGER_ZERO;
        result = BooleanUtils.toIntegerObject(bool);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testToIntegerObjectWithBooleanArray() {
        boolean[] array = {true, false};
        boolean expected = true;
        boolean result = BooleanUtils.and(array);
        assertEquals(expected, result);

        array = new boolean[0];
        assertFalse(BooleanUtils.and(array));

        try {
            Boolean[] booleanArray = null;
            Boolean result = BooleanUtils.and(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray() {
        boolean[] array = {true, false};
        boolean expected = true;
        boolean result = BooleanUtils.and(array).booleanValue();
        assertEquals(expected, result);

        array = new boolean[0];
        assertFalse(BooleanUtils.and(array).booleanValue());

        try {
            Boolean[] booleanArray = null;
            Boolean result = BooleanUtils.and(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray_2() {
        boolean[] array = {true, false};
        boolean expected = true;
        boolean result = BooleanUtils.and(array, true, false, false).booleanValue();
        assertEquals(expected, result);

        array = new boolean[0];
        assertFalse(BooleanUtils.and(array, true, false, false).booleanValue());

        try {
            Boolean[] booleanArray = null;
            Boolean result = BooleanUtils.and(booleanArray, true, false, false);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray_3() {
        boolean[] array = {true, false};
        boolean expected = false;
        boolean result = BooleanUtils.or(array).booleanValue();
        assertEquals(expected, result);

        array = new boolean[0];
        assertFalse(BooleanUtils.or(array).booleanValue());

        try {
            Boolean[] booleanArray = null;
            Boolean result = BooleanUtils.or(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray_4() {
        boolean[] array = {true, false};
        boolean expected = false;
        boolean result = BooleanUtils.xor(array).booleanValue();
        assertEquals(expected, result);

        array = new boolean[0];
        assertFalse(BooleanUtils.xor(array).booleanValue());

        try {
            Boolean[] booleanArray = null;
            Boolean result = BooleanUtils.xor(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }
}