package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Assert;

import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;

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
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
    }

    @org.junit.Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        try {
            BooleanUtils.toBoolean((Object) (Object) new String("not a boolean"));
            fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    @org.junit.Test
    public void testToBooleanObject() {
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
    }

    @org.junit.Test
    public void testToBooleanObjectWithInt() {
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        Assert.assertNull(BooleanUtils.toBooleanObject(-1));
    }

    @org.junit.Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @org.junit.Test
    public void testToBooleanWithInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, Boolean.FALSE, Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(0, Boolean.FALSE, Boolean.TRUE));
        try {
            BooleanUtils.toBoolean(2, Boolean.FALSE, Boolean.TRUE);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Integer did not match either specified value", e.getMessage());
        }
    }

    @org.junit.Test
    public void testToBooleanWithIntegerArray() {
        boolean[] expected = new boolean[]{true, false, false};
        Boolean[] actual = BooleanUtils.toBoolean(new int[]{1, 0, 0}, Boolean.FALSE, Boolean.TRUE);
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray() {
        boolean[] expected = new boolean[]{true, false, false};
        Boolean[] actual = BooleanUtils.toBoolean(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null}, Boolean.FALSE, Boolean.TRUE);
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringTrueFalse(null));
    }

    @org.junit.Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringOnOff(null));
    }

    @org.junit.Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringYesNo(null));
    }

    @org.junit.Test
    public void testToString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertEquals(null, BooleanUtils.toString(null, "true", "false"));
    }

    @org.junit.Test
    public void testToStringWithTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "false", "false"));
        Assert.assertEquals(null, BooleanUtils.toString(null, "true", "false"));
    }

    @org.junit.Test
    public void testToStringWithOnOff() {
        Assert.assertEquals("on", BooleanUtils.toString(Boolean.TRUE, "on", "off"));
        Assert.assertEquals("off", BooleanUtils.toString(Boolean.FALSE, "on", "off"));
        Assert.assertEquals("off", BooleanUtils.toString(Boolean.FALSE, "off", "off"));
        Assert.assertEquals(null, BooleanUtils.toString(null, "on", "off"));
    }

    @org.junit.Test
    public void testToStringWithYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toString(Boolean.TRUE, "yes", "no"));
        Assert.assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "yes", "no"));
        Assert.assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "no", "no"));
        Assert.assertEquals(null, BooleanUtils.toString(null, "yes", "no"));
    }

    @org.junit.Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, false, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{null, true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, null, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, true, null}));
        try {
            BooleanUtils.and(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testAndWithBooleanPrimitiveArray() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, false, false}));
        try {
            BooleanUtils.and(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testAndWithBooleanArray() {
        Boolean[] expected = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};
        Boolean[] actual = BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{null, true, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{true, null, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{true, true, null}));
        try {
            BooleanUtils.or(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testOrWithBooleanPrimitiveArray() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
        try {
            BooleanUtils.or(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testOrWithBooleanArray() {
        Boolean[] expected = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean[] actual = BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, true, true}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{null, true, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, null, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, true, null}));
        try {
            BooleanUtils.xor(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testXorWithBooleanPrimitiveArray() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, true, true}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, true, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
        try {
            BooleanUtils.xor(new boolean[]{null, null, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    @org.junit.Test
    public void testXorWithBooleanArray() {
        Boolean[] expected = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE};
        Boolean[] actual = BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, actual);
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArray() {
        boolean[] expected = new boolean[]{true, false, false};
        Boolean[] actual = BooleanUtils.toBoolean(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null});
        assertArrayEquals(expected, ArrayUtils.toPrimitive(actual));
    }

    @org.junit.Test
    public void testToBooleanWithBooleanArrayWithDefaultValue() {
        boolean[] expected = new boolean[]{true, false, false};
        Boolean[] actual = BooleanUtils.toBoolean(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null}, false);
        assertArrayEquals(expected, ArrayUtils.toPrimitive(actual));
    }

    @org.junit.Test
    public void testAndWithBooleanArray() {
        boolean[] expected = new boolean[]{true, false, false};
        Boolean[] actual = BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null});
        assertArrayEquals(expected, ArrayUtils.toPrimitive(actual));
    }

    @org.junit.Test
    public void testOrWithBooleanArray() {
        boolean[] expected = new boolean[]{true, true, true};
        Boolean[] actual = BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, ArrayUtils.toPrimitive(actual));
    }

    @org.junit.Test
    public void testXorWithBooleanArray() {
        boolean[] expected = new boolean[]{false, true, false};
        Boolean[] actual = BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        assertArrayEquals(expected, ArrayUtils.toPrimitive(actual));
    }
}