package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertTrue(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
    }

    @Test
    public void testIsTrue() {
        assertFalse(BooleanUtils.isTrue(null));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(null));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
        assertNull(BooleanUtils.toBoolean(null));
        try {
            BooleanUtils.toBoolean(Integer.valueOf(2));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithNullableBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObject() {
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
        assertNull(BooleanUtils.toBooleanObject(null));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithNullableBoolean() {
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null, null));
        assertSame(Boolean.TRUE, BooleanUtils.toBooleanObject(1, Boolean.TRUE, Boolean.FALSE, null, null));
        assertSame(Boolean.FALSE, BooleanUtils.toBooleanObject(0, Boolean.TRUE, Boolean.FALSE, null, null));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null, null));
    }

    @Test
    public void testToInteger() {
        assertTrue(BooleanUtils.toInteger(Boolean.TRUE) == 1);
        assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 1);
        assertFalse(BooleanUtils.toInteger(Boolean.TRUE) == 0);
        assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 0);
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        assertNull(BooleanUtils.toIntegerObject(null));
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerWithValue() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
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
    public void testAnd() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{null}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, null}));
        assertFalse(BooleanUtils.and(new Boolean[]{null, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{null, null}));
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(new Boolean[]{null}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.TRUE, null}));
        assertFalse(BooleanUtils.or(new Boolean[]{null, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{null, null}));
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{null}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, null}));
        assertFalse(BooleanUtils.xor(new Boolean[]{null, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{null, null}));
    }
}