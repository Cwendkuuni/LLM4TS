package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Assert;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @org.junit.Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.FALSE));
    }

    @org.junit.Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @org.junit.Test
    public void testIsNotTrue() {
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotTrue(null));
    }

    @org.junit.Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @org.junit.Test
    public void testIsNotFalse() {
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
    }

    @org.junit.Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));

        assertTrue(BooleanUtils.toBoolean((byte) 1));
        assertFalse(BooleanUtils.toBoolean((byte) 0));

        assertTrue(BooleanUtils.toBoolean((short) 1));
        assertFalse(BooleanUtils.toBoolean((short) 0));

        assertTrue(BooleanUtils.toBoolean((char) '1'));
        assertFalse(BooleanUtils.toBoolean((char) '0'));

        assertTrue(BooleanUtils.toBoolean((int) 1));
        assertFalse(BooleanUtils.toBoolean((int) 0));

        assertTrue(BooleanUtils.toBoolean((long) 1));
        assertFalse(BooleanUtils.toBoolean((long) 0));

        assertTrue(BooleanUtils.toBoolean((float) 1.0f));
        assertFalse(BooleanUtils.toBoolean((float) 0.0f));

        assertTrue(BooleanUtils.toBoolean((double) 1.0));
        assertFalse(BooleanUtils.toBoolean((double) 0.0));

        assertTrue(BooleanUtils.toBoolean(MathUtils.TRUE));
        assertFalse(BooleanUtils.toBoolean(MathUtils.FALSE));
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @org.junit.Test
    public void testToBooleanWithValue() {
        assertTrue(BooleanUtils.toBoolean(true, true, false));
        assertFalse(BooleanUtils.toBoolean(false, true, false));
    }

    @org.junit.Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @org.junit.Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertNull(BooleanUtils.toStringOnOff(null));
    }

    @org.junit.Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertNull(BooleanUtils.toStringYesNo(null));
    }

    @org.junit.Test
    public void testToString() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
        assertNull(BooleanUtils.toString(null, "true", "false", null));

        assertEquals("on", BooleanUtils.toString(Boolean.TRUE, "on", "off", null));
        assertEquals("off", BooleanUtils.toString(Boolean.FALSE, "on", "off", null));
        assertNull(BooleanUtils.toString(null, "on", "off", null));

        assertEquals("yes", BooleanUtils.toString(Boolean.TRUE, "yes", "no", null));
        assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "yes", "no", null));
        assertNull(BooleanUtils.toString(null, "yes", "no", null));
    }

    @org.junit.Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        assertFalse(BooleanUtils.and(new boolean[]{true, false, true}));
        assertFalse(BooleanUtils.and(new boolean[]{false, true, true}));
        assertFalse(BooleanUtils.and(new boolean[0]));
        assertFalse(BooleanUtils.and(new boolean[]{null}));
        assertFalse(BooleanUtils.and(new boolean[]{null, null}));
        assertFalse(BooleanUtils.and(new boolean[]{null, true}));
        assertFalse(BooleanUtils.and(new boolean[]{true, null}));

        try {
            BooleanUtils.and(new boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.and(new boolean[]{true, true, true, 1});
            fail("Expected ArrayStoreException to be thrown");
        } catch (ArrayStoreException ex) {
            // Expected
        }
    }

    @org.junit.Test
    public void testAndWithBoolean() {
        assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.and(null, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, null, Boolean.TRUE));
        assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, null));

        try {
            BooleanUtils.and(Boolean.TRUE, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testOr() {
        assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
        assertTrue(BooleanUtils.or(new boolean[]{true, false, true}));
        assertTrue(BooleanUtils.or(new boolean[]{false, true, true}));
        assertFalse(BooleanUtils.or(new boolean[0]));
        assertFalse(BooleanUtils.or(new boolean[]{null}));
        assertFalse(BooleanUtils.or(new boolean[]{null, null}));
        assertFalse(BooleanUtils.or(new boolean[]{null, true}));
        assertFalse(BooleanUtils.or(new boolean[]{true, null}));

        try {
            BooleanUtils.or(new boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.or(new boolean[]{true, true, true, 1});
            fail("Expected ArrayStoreException to be thrown");
        } catch (ArrayStoreException ex) {
            // Expected
        }
    }

    @org.junit.Test
    public void testOrWithBoolean() {
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertTrue(BooleanUtils.or(null, Boolean.TRUE, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, null, Boolean.TRUE));
        assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, null));

        try {
            BooleanUtils.or(Boolean.TRUE, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @org.junit.Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(new boolean[]{true, true, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, false, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{false, true, true}));
        assertFalse(BooleanUtils.xor(new boolean[0]));
        assertFalse(BooleanUtils.xor(new boolean[]{null}));
        assertFalse(BooleanUtils.xor(new boolean[]{null, null}));
        assertFalse(BooleanUtils.xor(new boolean[]{null, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, null}));

        try {
            BooleanUtils.xor(new boolean[]{null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }

        try {
            BooleanUtils.xor(new boolean[]{true, true, true, 1});
            fail("Expected ArrayStoreException to be thrown");
        } catch (ArrayStoreException ex) {
            // Expected
        }
    }

    @org.junit.Test
    public void testXorWithBoolean() {
        assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
        assertFalse(BooleanUtils.xor(null, Boolean.TRUE, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, null, Boolean.TRUE));
        assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, null));

        try {
            BooleanUtils.xor(Boolean.TRUE, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }
}