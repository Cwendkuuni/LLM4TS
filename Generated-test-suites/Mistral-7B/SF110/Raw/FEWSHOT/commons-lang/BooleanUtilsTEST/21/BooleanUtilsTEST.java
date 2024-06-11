package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import static org.junit.Assert.*;

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
    }

    @Test
    public void testToBooleanWithInteger() {
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
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithInteger() {
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
    public void testToBooleanObjectWithBoolean() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject((Boolean) 1, null, null, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject((Boolean) 0, null, null, null));
        assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
    }

    @Test
    public void testToBooleanWithBoolean() {
        assertTrue(BooleanUtils.toBoolean((Boolean) 1, null, null, null));
        assertFalse(BooleanUtils.toBoolean((Boolean) 0, null, null, null));
        assertNull(BooleanUtils.toBoolean(null, null, null, null));
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
        assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertEquals(true, BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertEquals(false, BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertEquals(false, BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithNullValue() {
        assertFalse(BooleanUtils.toBoolean(null));
        assertEquals(false, BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        boolean[] expected = {true, false, true};
        Boolean[] actual = BooleanUtils.toBooleanArray(new boolean[] {true, false, true});
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBooleanWithBooleanArrayList() {
        List<Boolean> expected = Arrays.asList(true, false, true);
        List<Boolean> actual = BooleanUtils.toBooleanList(Arrays.asList(new Boolean[]{true, false, true}));
        assertEquals(expected, actual);
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true, true));
        assertFalse(BooleanUtils.and(true, false, true, true));
        assertFalse(BooleanUtils.and(false, true, true, true));
        assertFalse(BooleanUtils.and(false, false, true, true));
        assertFalse(BooleanUtils.and(false, false, false, false));
        assertFalse(BooleanUtils.and(null, true, true, true));
        try {
            BooleanUtils.and(null, null, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new boolean[]{null, true, true, true});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true, true));
        assertTrue(BooleanUtils.or(true, true, true, false));
        assertTrue(BooleanUtils.or(true, false, true, true));
        assertTrue(BooleanUtils.or(false, true, true, true));
        assertFalse(BooleanUtils.or(false, false, true, true));
        assertFalse(BooleanUtils.or(null, true, true, true));
        try {
            BooleanUtils.or(null, null, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new boolean[]{null, true, true, true});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true, true, true));
        assertFalse(BooleanUtils.xor(true, true, true, false));
        assertFalse(BooleanUtils.xor(true, false, true, true));
        assertFalse(BooleanUtils.xor(false, true, true, true));
        assertTrue(BooleanUtils.xor(false, false, true, true));
        assertFalse(BooleanUtils.xor(null, true, true, true));
        try {
            BooleanUtils.xor(null, null, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new boolean[]{null, true, true, true});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }
}