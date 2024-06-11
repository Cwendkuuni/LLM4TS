package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertFalse(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
    }

    @Test
    public void testIsTrue() {
        assertFalse(BooleanUtils.isTrue(null));
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(null));
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(null));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(null));
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testToBoolean() {
        assertFalse(BooleanUtils.toBoolean(0));
        assertTrue(BooleanUtils.toBoolean(1));

        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertFalse(BooleanUtils.toBoolean(0));
        assertTrue(BooleanUtils.toBoolean(1));

        assertNull(BooleanUtils.toBoolean(NumberUtils.MIN_VALUE));
        assertNull(BooleanUtils.toBoolean(NumberUtils.MAX_VALUE));
        assertNull(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        assertNull(BooleanUtils.toBoolean(Integer.MAX_VALUE));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertNull(BooleanUtils.toBooleanObject(null));
        assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
    }

    @Test
    public void testToBooleanWithValueAndDefault() {
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
    }

    @Test
    public void testToBooleanWithObjectAndDefault() {
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(true));
        assertEquals(0, BooleanUtils.toInteger(false));

        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false));

        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(null));
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(true));
        assertEquals(0, BooleanUtils.toInteger(false));

        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerWithValue() {
        assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(false, 1, 0));

        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
        assertNull(BooleanUtils.toBooleanObject(""));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true"));
        assertFalse(BooleanUtils.toBoolean("false"));
        assertFalse(BooleanUtils.toBoolean(""));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithStringAndDefault() {
        assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
        assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
        assertFalse(BooleanUtils.toBoolean(null, "true", "false"));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertEquals("", BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertEquals("", BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertEquals("", BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringWithDefaults() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertEquals("", BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithBoolean() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE));
        assertEquals("", BooleanUtils.toString(null));
    }

    @Test
    public void testAnd() {
        assertFalse(BooleanUtils.and(new boolean[] { false }));
        assertFalse(BooleanUtils.and(new boolean[] { false, false }));
        assertTrue(BooleanUtils.and(new boolean[] { true, true }));
        assertFalse(BooleanUtils.and(new boolean[] { true, false }));

        assertEquals(Boolean.FALSE, BooleanUtils.and(new Boolean[] { Boolean.FALSE }));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.FALSE, Boolean.FALSE }));
        assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
    }

    @Test
    public void testAndWithArrays() {
        assertFalse(BooleanUtils.and(new Boolean[] { null }));
        assertFalse(BooleanUtils.and(new Boolean[] {}));
        assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE }));
        assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));

        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new Boolean[] { null, Boolean.TRUE }));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new Boolean[] { Boolean.FALSE, null }));
    }

    @Test
    public void testOr() {
        assertFalse(BooleanUtils.or(new boolean[] { false }));
        assertTrue(BooleanUtils.or(new boolean[] { true, false }));
        assertTrue(BooleanUtils.or(new boolean[] { false, true }));
        assertTrue(BooleanUtils.or(new boolean[] { true, true }));

        assertEquals(Boolean.FALSE, BooleanUtils.or(new Boolean[] { Boolean.FALSE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.FALSE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));
    }

    @Test
    public void testOrWithArrays() {
        assertFalse(BooleanUtils.or(new Boolean[] { null }));
        assertFalse(BooleanUtils.or(new Boolean[] {}));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.FALSE, Boolean.TRUE }));
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));

        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new Boolean[] { null, Boolean.TRUE }));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new Boolean[] { Boolean.FALSE, null }));
    }

    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[] { false }));
        assertFalse(BooleanUtils.xor(new boolean[] { true, true }));
        assertFalse(BooleanUtils.xor(new boolean[] { false, false }));
        assertTrue(BooleanUtils.xor(new boolean[] { true, false }));

        assertEquals(Boolean.FALSE, BooleanUtils.xor(new Boolean[] { Boolean.FALSE }));
        assertEquals(Boolean.TRUE, BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));
        assertEquals(Boolean.FALSE, BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE }));
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
    }

    @Test
    public void testXorWithArrays() {
        assertFalse(BooleanUtils.xor(new Boolean[] { null }));
        assertFalse(BooleanUtils.xor(new Boolean[] {}));
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.TRUE }));
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE }));
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.TRUE }));
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE }));

        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new Boolean[] { null, Boolean.TRUE }));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new Boolean[] { Boolean.FALSE, null }));
    }
}