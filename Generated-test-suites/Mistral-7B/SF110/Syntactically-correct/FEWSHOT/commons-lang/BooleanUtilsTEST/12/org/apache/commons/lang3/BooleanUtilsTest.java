package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

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
        assertFalse(BooleanUtils.toBoolean(null));
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanObject() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithBoolean() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanObjectWithValueAndTrueFalse() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0));
        assertNull(BooleanUtils.toBooleanObject(0, 1, 1));
    }

    @Test
    public void testToBooleanObjectWithValueAndTrueFalseNull() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
    }

    @Test
    public void testToBooleanObjectWithValueAndTrueFalseNullValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, null, null, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, null, null, null));
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
    }

    @Test
    public void testToInteger() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObject() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerWithValueAndTrueFalse() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithValueAndTrueFalseNull() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0, null));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0, null));
        assertNull(BooleanUtils.toInteger(null, 1, 0, null));
    }

    @Test
    public void testToIntegerWithValueAndTrueFalseNullValues() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, null, null, null));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, null, null, null));
        assertNull(BooleanUtils.toInteger(null, null, null, null));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    @Test
    public void testToString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithTrueFalse() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "on", "off"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "on", "off"));
    }

    @Test
    public void testToStringWithTrueFalseNull() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "on", "off", null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "on", "off", null));
        assertNull(BooleanUtils.toString(null, "on", "off", null));
    }

    @Test
    public void testToStringWithTrueFalseNullValues() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "on", null, null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "on", null, null));
        assertNull(BooleanUtils.toString(null, "on", null, null));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.and(new Boolean[]{Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.TRUE})));
        assertFalse(BooleanUtils.and((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.FALSE})));
        assertFalse(BooleanUtils.and((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.TRUE})));
        assertFalse(BooleanUtils.and((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.FALSE})));
        try {
            BooleanUtils.and((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, null}));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanPrimitive() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.and(new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.or(new Boolean[]{Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.TRUE})));
        assertTrue(BooleanUtils.or((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.FALSE})));
        assertTrue(BooleanUtils.or((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.TRUE})));
        assertFalse(BooleanUtils.or((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.FALSE})));
        try {
            BooleanUtils.or((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, null}));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanPrimitive() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.or(new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.TRUE})));
        assertFalse(BooleanUtils.xor((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, Boolean.FALSE})));
        assertFalse(BooleanUtils.xor((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.TRUE})));
        assertTrue(BooleanUtils.xor((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.FALSE, Boolean.FALSE})));
        try {
            BooleanUtils.xor((Boolean[]) ArrayUtils.toObject(new Object[][]{Boolean.TRUE, null}));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanPrimitive() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        try {
            BooleanUtils.xor(new boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}