package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertFalse(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
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
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean((short) 1));
        assertFalse(BooleanUtils.toBoolean((short) -1));
        assertFalse(BooleanUtils.toBoolean(Byte.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Byte.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean((char) 1));
        assertFalse(BooleanUtils.toBoolean((char) 0));
        assertFalse(BooleanUtils.toBoolean((byte) 1));
        assertFalse(BooleanUtils.toBoolean((byte) 0));
        assertFalse(BooleanUtils.toBoolean((long) 1));
        assertFalse(BooleanUtils.toBoolean((long) -1));
        assertFalse(BooleanUtils.toBoolean(Short.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Short.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean((int) 1));
        assertFalse(BooleanUtils.toBoolean((int) 0));
        assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Integer.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean((long) 1L));
        assertFalse(BooleanUtils.toBoolean((long) -1L));
        assertFalse(BooleanUtils.toBoolean(Long.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Long.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean((float) 1));
        assertFalse(BooleanUtils.toBoolean((float) 0));
        assertFalse(BooleanUtils.toBoolean(Float.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Float.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean(Double.MIN_VALUE));
        assertFalse(BooleanUtils.toBoolean(Double.MAX_VALUE));
        assertFalse(BooleanUtils.toBoolean(Double.NaN));
        assertFalse(BooleanUtils.toBoolean(Float.NaN));
        assertFalse(BooleanUtils.toBoolean(byte.class));
        assertFalse(BooleanUtils.toBoolean(short.class));
        assertFalse(BooleanUtils.toBoolean(int.class));
        assertFalse(BooleanUtils.toBoolean(long.class));
        assertFalse(BooleanUtils.toBoolean(char.class));
        assertFalse(BooleanUtils.toBoolean(float.class));
        assertFalse(BooleanUtils.toBoolean(double.class));
        assertFalse(BooleanUtils.toBoolean(Boolean[].class));
        assertFalse(BooleanUtils.toBoolean(boolean[].class));
        try {
            BooleanUtils.toBoolean("not a boolean");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument is not a boolean or a number", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        try {
            BooleanUtils.toBoolean(2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument is not a boolean or a number", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertNull(BooleanUtils.toBooleanObject(null));
        try {
            BooleanUtils.toBooleanObject(2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument is not a boolean or a number", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithIntegerTrueFalse() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test
    public void testToBooleanObjectWithIntegerTrueFalse() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 1));
    }

    @Test
    public void testToBooleanObjectWithIntegerNullValue() {
        assertNull(BooleanUtils.toBooleanObject(0, 1, 0, 0));
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
    public void testToStringWithValues() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithValuesTrueFalse() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithValuesTrueOther() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "other"));
        assertEquals("other", BooleanUtils.toString(Boolean.FALSE, "true", "other"));
    }

    @Test
    public void testToStringWithValuesFalseOther() {
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "other"));
        assertEquals("other", BooleanUtils.toString(Boolean.TRUE, "true", "other"));
    }

    @Test
    public void testToStringWithNullValues() {
        assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true));
        assertFalse(BooleanUtils.and(true, false));
        assertFalse(BooleanUtils.and(false, true));
        assertFalse(BooleanUtils.and(false, false));
    }

    @Test
    public void testAndWithBooleans() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testAndWithBooleanArrays() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullBooleanArray() {
        BooleanUtils.and((Boolean[]) null);
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true));
        assertTrue(BooleanUtils.or(true, false));
        assertTrue(BooleanUtils.or(false, true));
        assertFalse(BooleanUtils.or(false, false));
    }

    @Test
    public void testOrWithBooleans() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testOrWithBooleanArrays() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithNullBooleanArray() {
        BooleanUtils.or((Boolean[]) null);
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true));
        assertFalse(BooleanUtils.xor(true, false));
        assertFalse(BooleanUtils.xor(false, true));
        assertTrue(BooleanUtils.xor(false, false));
    }

    @Test
    public void testXorWithBooleans() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testXorWithBooleanArrays() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorWithNullBooleanArray() {
        BooleanUtils.xor((Boolean[]) null);
    }
}