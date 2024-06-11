package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.apache.commons.lang3.BooleanUtils.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, negate(Boolean.FALSE));
        assertNull(negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(isTrue(Boolean.TRUE));
        assertFalse(isTrue(Boolean.FALSE));
        assertFalse(isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(isNotTrue(Boolean.TRUE));
        assertTrue(isNotTrue(Boolean.FALSE));
        assertFalse(isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(isFalse(Boolean.TRUE));
        assertTrue(isFalse(Boolean.FALSE));
        assertFalse(isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(isNotFalse(Boolean.TRUE));
        assertFalse(isNotFalse(Boolean.FALSE));
        assertFalse(isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(toBoolean(Boolean.TRUE));
        assertFalse(toBoolean(Boolean.FALSE));
        assertFalse(toBoolean(null));
        assertFalse(toBoolean((Object) 0));
        assertFalse(toBoolean((Object) -1));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(toBooleanDefaultIfNull(null, true));
        assertFalse(toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(toBoolean(1));
        assertFalse(toBoolean(0));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, toBooleanObject(1));
        assertEquals(Boolean.FALSE, toBooleanObject(0));
        assertNull(toBooleanObject(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntAndValues() {
        toBoolean(2, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntAndValues() {
        toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
    }

    @Test
    public void testToBooleanWithBooleanAndValues() {
        assertTrue(toBoolean(true, 1, 0));
        assertFalse(toBoolean(false, 1, 0));
    }

    @Test
    public void testToBooleanObjectWithBooleanAndValues() {
        assertEquals(Boolean.TRUE, toBooleanObject(Boolean.TRUE, 1, 0));
        assertEquals(Boolean.FALSE, toBooleanObject(Boolean.FALSE, 1, 0));
        assertNull(toBooleanObject(null, 1, 0));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", toStringTrueFalse(Boolean.FALSE));
        assertEquals("", toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", toStringOnOff(Boolean.TRUE));
        assertEquals("off", toStringOnOff(Boolean.FALSE));
        assertEquals("", toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", toStringYesNo(Boolean.TRUE));
        assertEquals("no", toStringYesNo(Boolean.FALSE));
        assertEquals("", toStringYesNo(null));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, toInteger(true));
        assertEquals(0, toInteger(false));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(true));
        assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(false));
        assertNull(toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(Boolean.FALSE));
        assertNull(toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBooleanAndValues() {
        assertEquals(1, toInteger(true, 1, 0));
        assertEquals(0, toInteger(false, 1, 0));
    }

    @Test
    public void testToIntegerWithNullableBooleanAndValues() {
        assertEquals(1, toInteger(true, 1, 0, null));
        assertEquals(0, toInteger(false, 1, 0, null));
        assertNull(toInteger(null, 1, 0, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntAndValuesThrowsException() {
        toBoolean(2, 1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntAndValuesThrowsException() {
        toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithBooleanAndValuesThrowsException() {
        toBoolean(true, 1, 0, 1);
    }

    // Test methods for and, or, and xor

    @Test
    public void testAnd() {
        assertTrue(and(true, true, true, true, true));
        assertFalse(and(true, true, true, true, false));
        assertFalse(and(true, true, false, true, false));
        assertFalse(and(true, false, true, true, false));
        assertFalse(and(false, true, true, true, false));
        assertFalse(and(false, false, true, true, false));
        assertFalse(and(false, false, false, true, false));
        assertFalse(and(false, false, false, false, false));
    }

    @Test
    public void testAndWithBooleanArrays() {
        assertTrue(and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullBooleanArray() {
        and(new Boolean[]{null});
    }

    @Test
    public void testOr() {
        assertTrue(or(true, true, true, true, true));
        assertFalse(or(true, true, true, true, false));
        assertTrue(or(true, true, false, true, false));
        assertTrue(or(true, false, true, true, false));
        assertTrue(or(false, true, true, true, false));
        assertTrue(or(false, false, true, true, false));
        assertTrue(or(false, false, false, true, false));
        assertTrue(or(false, false, false, false, false));
        assertTrue(or(true, true, true, true, null));
        assertTrue(or(true, true, true, true, new Boolean[0]));
        assertTrue(or(true, true, true, true, new Boolean[]{}));
        assertTrue(or(true, true, true, true, new Boolean[]{null}));
    }

    @Test
    public void testOrWithBooleanArrays() {
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[0]}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[]{}}));
        assertTrue(or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[]{null}}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithNullBooleanArray() {
        or(new Boolean[]{null});
    }

    @Test
    public void testXor() {
        assertTrue(xor(new boolean[]{true, true, true, true, true}));
        assertFalse(xor(new boolean[]{true, true, true, true, false}));
        assertFalse(xor(new boolean[]{true, true, false, true, false}));
        assertFalse(xor(new boolean[]{true, false, true, true, false}));
        assertFalse(xor(new boolean[]{false, true, true, true, false}));
        assertFalse(xor(new boolean[]{false, false, true, true, false}));
        assertFalse(xor(new boolean[]{false, false, false, true, false}));
        assertFalse(xor(new boolean[]{false, false, false, false, false}));
        assertFalse(xor(new boolean[]{true, true, true, true, null}));
        assertFalse(xor(new boolean[]{true, true, true, true, new boolean[0]}));
        assertFalse(xor(new boolean[]{true, true, true, true, new boolean[]{}}));
        assertFalse(xor(new boolean[]{true, true, true, true, new boolean[]{null}}));
    }

    @Test
    public void testXorWithBooleanArrays() {
        assertTrue(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[0]}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[]{}}));
        assertFalse(xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, new Boolean[]{null}}));
    }

}