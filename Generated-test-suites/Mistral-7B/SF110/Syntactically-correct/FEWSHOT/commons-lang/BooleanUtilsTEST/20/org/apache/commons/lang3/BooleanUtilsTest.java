package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
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
        assertNull(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertNull(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertNull(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertEquals(false, BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertNull(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @Test
    public void testToBooleanWithIntAndDefault() {
        assertTrue(BooleanUtils.toBoolean(1, true, false));
        assertFalse(BooleanUtils.toBoolean(0, true, false));
        assertEquals(true, BooleanUtils.toBoolean(-1, true, false));
    }

    @Test
    public void testToBooleanObjectWithIntAndDefault() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(-1, Boolean.TRUE, Boolean.FALSE));
    }

    @Test
    public void testToBooleanWithBooleanAndDefault() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE, true, false));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE, true, false));
        assertEquals(true, BooleanUtils.toBoolean(null, true, false));
    }

    @Test
    public void testToBooleanObjectWithBooleanAndDefault() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE));
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
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
        assertNull(BooleanUtils.toInteger(null, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanAndDefault() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 0));
        assertEquals(0, BooleanUtils.toInteger(null, 1, 0, 0));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
        assertNull(BooleanUtils.toBooleanObject(null));
        assertFalse(BooleanUtils.toBooleanObject("false"));
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true"));
        assertFalse(BooleanUtils.toBoolean("false"));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithStringAndDefault() {
        assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
        assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
        assertNull(BooleanUtils.toBoolean(null, "true", "false"));
    }

    @Test
    public void testToBooleanObjectWithStringAndDefault() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", Boolean.TRUE, Boolean.FALSE, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", Boolean.TRUE, Boolean.FALSE, null));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
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
    public void testToStringWithBoolean() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanAndDefault() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", "default"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", "default"));
        assertEquals("default", BooleanUtils.toString(null, "true", "false", "default"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
        assertNull(BooleanUtils.and(null));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.and(null));
    }

    @Test
    public void testAndWithBooleanArrayAndDefault() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}, Boolean.TRUE));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, Boolean.TRUE));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}, Boolean.TRUE));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}, Boolean.TRUE));
        assertNull(BooleanUtils.and(new Boolean[]{null}, Boolean.TRUE));
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
        assertNull(BooleanUtils.or(null));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.or(null));
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
        assertNull(BooleanUtils.xor(null));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.xor(null));
    }
}