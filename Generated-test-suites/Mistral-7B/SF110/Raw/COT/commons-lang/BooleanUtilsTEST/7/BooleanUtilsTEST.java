package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.MathUtils;
import org.apache.commons.lang3.NullPointerException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    @Test
    public void testIsFalse() {
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        try {
            BooleanUtils.toBoolean("abc");
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanArray() {
        boolean[] testArray = new boolean[]{true, false, true};
        Boolean[] expectedArray = new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
        Assert.assertArrayEquals(expectedArray, BooleanUtils.toBooleanArray(testArray));
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertEquals(null, BooleanUtils.toString(null, "true", "false"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithNullTrueValue() {
        BooleanUtils.toBooleanObject(Boolean.TRUE, null, Boolean.FALSE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithNullFalseValue() {
        BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.FALSE, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithNullNullValue() {
        BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithNullInput() {
        try {
            BooleanUtils.toBooleanObject((String)null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testToBooleanObject() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("1", "false", "true", null));
        Assert.assertEquals(null, BooleanUtils.toBooleanObject("null", "true", "false", null));
        Assert.assertEquals(null, BooleanUtils.toBooleanObject("", "true", "false", null));
    }

    @Test
    public void testToBooleanWithString() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
        Assert.assertTrue(BooleanUtils.toBoolean("TRUE"));
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
        Assert.assertFalse(BooleanUtils.toBoolean("FALSE"));
        Assert.assertFalse(BooleanUtils.toBoolean(""));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean("null"));
    }

    @Test
    public void testToBooleanWithStringAndValues() {
        Assert.assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
        Assert.assertTrue(BooleanUtils.toBoolean("TRUE", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean("FALSE", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean("", "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean(null, "true", "false"));
        Assert.assertFalse(BooleanUtils.toBoolean("null", "true", "false"));
        try {
            BooleanUtils.toBoolean("123", "true", "false");
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
        try {
            BooleanUtils.toBoolean("abc", "true", "false");
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(true, true));
        Assert.assertTrue(BooleanUtils.and(true, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, false));
        Assert.assertFalse(BooleanUtils.and(true, false, true, true));
        Assert.assertFalse(BooleanUtils.and(false, true));
        Assert.assertFalse(BooleanUtils.and(false, true, true, true));
        Assert.assertFalse(BooleanUtils.and(true, true, false, false));
        Assert.assertFalse(BooleanUtils.and(true, true, false, false, true, true));
        try {
            BooleanUtils.and(true, true, null, null);
            fail();
        } catch (NullPointerException e) {
            //expected
        }
        try {
            BooleanUtils.and(new boolean[]{true, true, true, true, null});
            fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(true, true));
        Assert.assertTrue(BooleanUtils.or(true, true, true, true));
        Assert.assertFalse(BooleanUtils.or(true, false));
        Assert.assertTrue(BooleanUtils.or(true, false, true, true));
        Assert.assertFalse(BooleanUtils.or(false, true));
        Assert.assertTrue(BooleanUtils.or(false, true, true, true));
        Assert.assertTrue(BooleanUtils.or(true, true, false, false));
        Assert.assertTrue(BooleanUtils.or(true, true, false, false, true, true));
        try {
            BooleanUtils.or(true, true, null, null);
            fail();
        } catch (NullPointerException e) {
            //expected
        }
        try {
            BooleanUtils.or(new boolean[]{true, true, true, true, null});
            fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(true, true));
        Assert.assertTrue(BooleanUtils.xor(true, false));
        Assert.assertFalse(BooleanUtils.xor(false, true));
        Assert.assertTrue(BooleanUtils.xor(false, false));
        Assert.assertTrue(BooleanUtils.xor(true, true, false, false));
        Assert.assertFalse(BooleanUtils.xor(true, true, true, true));
        Assert.assertFalse(BooleanUtils.xor(true, true, true, false));
        Assert.assertTrue(BooleanUtils.xor(true, true, true, false, true, false));
        try {
            BooleanUtils.xor(true, true, null, null);
            fail();
        } catch (NullPointerException e) {
            //expected
        }
        try {
            BooleanUtils.xor(new boolean[]{true, true, true, true, null});
            fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

}