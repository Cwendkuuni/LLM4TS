package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @Test
    public void testToBooleanWithIntTrueValueFalseValueNullValue() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        assertNull(BooleanUtils.toBoolean(0, null, null));
    }

    @Test
    public void testToBooleanObjectWithIntTrueValueFalseValueNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanObjectWithIntTrueValueFalseValueNullValueNullValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        assertNull(BooleanUtils.toInteger(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
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
    public void testToIntegerWithValues() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 1, 0));
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 1));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toInteger(Boolean.TRUE, 0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toInteger(Boolean.FALSE, 1, 1, 1));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithStrings() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
        assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
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
    public void testToString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true));
        assertFalse(BooleanUtils.and(true, false));
        assertFalse(BooleanUtils.and(false, true));
        assertFalse(BooleanUtils.and(false, false));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(null, true));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(true, null));
    }

    @Test
    public void testAndWithBooleans() {
        assertTrue(BooleanUtils.and(new boolean[]{true, true}));
        assertFalse(BooleanUtils.and(new boolean[]{true, false}));
        assertFalse(BooleanUtils.and(new boolean[]{false, true}));
        assertFalse(BooleanUtils.and(new boolean[]{false, false}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new boolean[]{null, true}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new boolean[]{true, null}));
    }

    @Test
    public void testAndWithBooleansArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new Boolean[]{null, Boolean.TRUE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new Boolean[]{Boolean.TRUE, null}));
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true));
        assertTrue(BooleanUtils.or(true, false));
        assertTrue(BooleanUtils.or(false, true));
        assertFalse(BooleanUtils.or(false, false));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(null, true));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(true, null));
    }

    @Test
    public void testOrWithBooleans() {
        assertTrue(BooleanUtils.or(new boolean[]{true, true}));
        assertTrue(BooleanUtils.or(new boolean[]{true, false}));
        assertTrue(BooleanUtils.or(new boolean[]{false, true}));
        assertFalse(BooleanUtils.or(new boolean[]{false, false}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new boolean[]{null, true}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new boolean[]{true, null}));
    }

    @Test
    public void testOrWithBooleansArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new Boolean[]{null, Boolean.TRUE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new Boolean[]{Boolean.TRUE, null}));
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true));
        assertFalse(BooleanUtils.xor(true, false));
        assertFalse(BooleanUtils.xor(false, true));
        assertTrue(BooleanUtils.xor(false, false));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(null, true));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(true, null));
    }

    @Test
    public void testXorWithBooleans() {
        assertTrue(BooleanUtils.xor(new boolean[]{true, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, false}));
        assertFalse(BooleanUtils.xor(new boolean[]{false, true}));
        assertTrue(BooleanUtils.xor(new boolean[]{false, false}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new boolean[]{null, true}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new boolean[]{true, null}));
    }

    @Test
    public void testXorWithBooleansArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new Boolean[]{null, Boolean.TRUE}));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new Boolean[]{Boolean.TRUE, null}));
    }
}