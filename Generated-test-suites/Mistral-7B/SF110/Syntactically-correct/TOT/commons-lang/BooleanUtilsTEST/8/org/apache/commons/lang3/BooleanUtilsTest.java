package org.apache.commons.lang3;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.Boolean;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.util.booleanArrays;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.TRUE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithInteger() {
        Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIncorrectParamType() {
        BooleanUtils.toBoolean("hello");
    }

    @Test
    public void testToBooleanWithIntegerAndValues() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(null, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(2), 1, 0));
        Assert.assertEquals(IllegalArgumentException.class, getClass(), BooleanUtils.toBoolean(Integer.valueOf(3), 1, 0).getClass());
    }

    @Test
    public void testToBooleanObjectWithIntegerAndValues() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), 1, 0, null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), 1, 0, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(2), 1, 0, null));
        Assert.assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(3), 1, 0, null));
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
        Assert.assertEquals(0, BooleanUtils.toInteger(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithValue() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToIntegerWithIncorrectParamType() {
        BooleanUtils.toInteger("hello");
    }

    @Test
    public void testToIntegerWithValueAndValues() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(null, 1, 0));
        Assert.assertEquals(1, BooleanUtils.toInteger(Integer.valueOf(1), 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Integer.valueOf(0), 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(null, 1, 0));
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        Assert.assertNull(BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringWithValues() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertNull(BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanArray() {
        boolean[] booleanArr = {true, false, null};
        boolean[] expectedBooleanArr = {true, false, null};
        Assert.assertArrayEquals(expectedBooleanArr, BooleanUtils.toBooleanArray(BooleanUtils.toBooleanArray(booleanArr)));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, false, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, false, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{null, true, true}));
        try {
            BooleanUtils.and(new boolean[]{null, 5, 6});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testAndWithBooleanArrayAndValues() {
        Assert.assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[]{null, Boolean.TRUE, Boolean.TRUE}));
        try {
            BooleanUtils.and(new Boolean[]{null, Integer.valueOf(5), Integer.valueOf(6)});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, false, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{null, true, true}));
        try {
            BooleanUtils.or(new boolean[]{null, 5, 6});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArrayAndValues() {
        Assert.assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[]{null, Boolean.TRUE, Boolean.TRUE}));
        try {
            BooleanUtils.or(new Boolean[]{null, Integer.valueOf(5), Integer.valueOf(6)});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, false, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{null, true, true}));
        try {
            BooleanUtils.xor(new boolean[]{null, 5, 6});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArrayAndValues() {
        Assert.assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[]{null, Boolean.TRUE, Boolean.TRUE}));
        try {
            BooleanUtils.xor(new Boolean[]{null, Integer.valueOf(5), Integer.valueOf(6)});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
        }
    }

}