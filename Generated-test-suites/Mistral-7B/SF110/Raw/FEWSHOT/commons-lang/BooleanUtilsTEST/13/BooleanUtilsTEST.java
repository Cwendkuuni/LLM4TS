package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtilsTest {

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
        
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
        try {
            BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObject() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, -1));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, -1));
        assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
        try {
            BooleanUtils.toBooleanObject(2, 1, 0, -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }

        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true"));
        assertFalse(BooleanUtils.toBoolean("false"));
        assertFalse(BooleanUtils.toBoolean(""));
        assertFalse(BooleanUtils.toBoolean("  "));
        assertFalse(BooleanUtils.toBoolean("null"));
        assertFalse(BooleanUtils.toBoolean("nullString"));

        try {
            BooleanUtils.toBoolean("2");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The String did not match either specified value", ex.getMessage());
        }

        assertTrue(BooleanUtils.toBoolean("true", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("false", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("  ", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("null", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("nullString", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("2", "yes", "on", "1"));
        assertFalse(BooleanUtils.toBoolean("notTrue", "yes", "on", "1"));
    }

    @Test
    public void testToBooleanObjectWithString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "yes", "on", "1"));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("  ", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("null", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("nullString", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("2", "yes", "on", "1"));
        assertNull(BooleanUtils.toBooleanObject("notTrue", "yes", "on", "1"));

        try {
            BooleanUtils.toBooleanObject("notBoolean", "yes", "on", "1");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The String did not match any specified value", ex.getMessage());
        }
    }

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
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanDefaultIfNull(null, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanDefaultIfNull(null, Boolean.FALSE));
    }

    @Test
    public void testToBooleanArray() {
        boolean[] expected = {true, false, null, true, false};
        boolean[] result = BooleanUtils.toBooleanArray(new Object[]{Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToBooleanArrayWithPrimitive() {
        boolean[] expected = {true, false, false, true, false};
        boolean[] result = BooleanUtils.toBooleanArray(new boolean[]{true, false, (byte)0, true, false});
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true));
        assertTrue(BooleanUtils.and(true, false));
        assertFalse(BooleanUtils.and(false, true));
        assertFalse(BooleanUtils.and(false, false));
        assertNull(BooleanUtils.and(null, null));
        assertNull(BooleanUtils.and(null, Boolean.TRUE));
        assertNull(BooleanUtils.and(Boolean.TRUE, null));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.and(new Boolean[]{}));
        assertNull(BooleanUtils.and(new Boolean[]{null}));

        try {
            BooleanUtils.and(new Boolean[]{null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true));
        assertTrue(BooleanUtils.or(true, false));
        assertTrue(BooleanUtils.or(false, true));
        assertFalse(BooleanUtils.or(false, false));
        assertNull(BooleanUtils.or(null, null));
        assertNull(BooleanUtils.or(null, Boolean.TRUE));
        assertNull(BooleanUtils.or(Boolean.TRUE, null));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.or(new Boolean[]{}));
        assertNull(BooleanUtils.or(new Boolean[]{null}));

        try {
            BooleanUtils.or(new Boolean[]{null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, false));
        assertFalse(BooleanUtils.xor(false, true));
        assertFalse(BooleanUtils.xor(true, true));
        assertFalse(BooleanUtils.xor(false, false));
        assertNull(BooleanUtils.xor(null, null));
        assertNull(BooleanUtils.xor(null, Boolean.TRUE));
        assertNull(BooleanUtils.xor(Boolean.TRUE, null));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.xor(new Boolean[]{}));
        assertNull(BooleanUtils.xor(new Boolean[]{null}));

        try {
            BooleanUtils.xor(new Boolean[]{null, Boolean.TRUE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}