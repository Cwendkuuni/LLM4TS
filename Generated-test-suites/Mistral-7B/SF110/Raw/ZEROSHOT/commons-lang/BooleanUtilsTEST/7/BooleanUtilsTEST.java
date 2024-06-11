package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.FALSE));
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
        Assert.assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
        Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
    }

    @Test
    public void testToBooleanWithString() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithString() {
        Assert.assertTrue(BooleanUtils.toBooleanObject("true"));
        Assert.assertFalse(BooleanUtils.toBooleanObject("false"));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithArray() {
        Assert.assertFalse(BooleanUtils.toBoolean(new boolean[0]));
        Assert.assertTrue(BooleanUtils.toBoolean(new boolean[]{true}));
        Assert.assertFalse(BooleanUtils.toBoolean(new boolean[]{false}));
        Assert.assertFalse(BooleanUtils.toBoolean(new boolean[]{null}));
    }

    @Test
    public void testToBooleanObjectWithArray() {
        Assert.assertNull(BooleanUtils.toBooleanObject(new Boolean[0]));
        Assert.assertTrue(BooleanUtils.toBooleanObject(new Boolean[]{Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.toBooleanObject(new Boolean[]{Boolean.FALSE}));
        Assert.assertNull(BooleanUtils.toBooleanObject(new Boolean[]{null}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntegerThrowsException() {
        BooleanUtils.toBoolean(2);
    }

    @Test
    public void testToBooleanWithIntegerTrueValue() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
    }

    @Test
    public void testToBooleanWithIntegerFalseValue() {
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntegerNullValueThrowsException() {
        BooleanUtils.toBoolean(null, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntegerThrowsException() {
        BooleanUtils.toBooleanObject(1, 1, 0, -1);
    }

    @Test
    public void testToBooleanObjectWithIntegerTrueValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
    }

    @Test
    public void testToBooleanObjectWithIntegerFalseValue() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectWithIntegerNullValueThrowsException() {
        BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null);
    }

    @Test
    public void testToBooleanWithStringTrueValue() {
        Assert.assertTrue(BooleanUtils.toBoolean("true"));
    }

    @Test
    public void testToBooleanWithStringFalseValue() {
        Assert.assertFalse(BooleanUtils.toBoolean("false"));
    }

    @Test
    public void testToBooleanWithStringNullValue() {
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithStringTrueValue() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
    }

    @Test
    public void testToBooleanObjectWithStringFalseValue() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
    }

    @Test
    public void testToBooleanObjectWithStringNullValue() {
        assertNull(BooleanUtils.toBooleanObject(null));
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
    public void testToStringTrueFalseWithTrueValue() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(true));
    }

    @Test
    public void testToStringTrueFalseWithFalseValue() {
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(false));
    }

    @Test
    public void testToStringOnOffWithTrueValue() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(true));
    }

    @Test
    public void testToStringOnOffWithFalseValue() {
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(false));
    }

    @Test
    public void testToStringYesNoWithTrueValue() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(true));
    }

    @Test
    public void testToStringYesNoWithFalseValue() {
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(false));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, false}));
        Assert.assertFalse(BooleanUtils.and(null));
        Assert.assertFalse(BooleanUtils.and(new boolean[0]));
    }

    @Test
    public void testAndWithBooleanValues() {
        Assert.assertTrue(BooleanUtils.and(true, true));
        Assert.assertFalse(BooleanUtils.and(true, false));
        Assert.assertFalse(BooleanUtils.and(false, true));
        Assert.assertFalse(BooleanUtils.and(false, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullValueThrowsException() {
        BooleanUtils.and(new boolean[]{null});
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, false}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{false, true}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false}));
        Assert.assertFalse(BooleanUtils.or(null));
        Assert.assertFalse(BooleanUtils.or(new boolean[0]));
    }

    @Test
    public void testOrWithBooleanValues() {
        Assert.assertTrue(BooleanUtils.or(true, true));
        Assert.assertTrue(BooleanUtils.or(true, false));
        Assert.assertTrue(BooleanUtils.or(false, true));
        Assert.assertFalse(BooleanUtils.or(false, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithNullValueThrowsException() {
        BooleanUtils.or(new boolean[]{null});
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, true}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, false}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{false, true}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false}));
        Assert.assertFalse(BooleanUtils.xor(null));
        Assert.assertFalse(BooleanUtils.xor(new boolean[0]));
    }

    @Test
    public void testXorWithBooleanValues() {
        Assert.assertFalse(BooleanUtils.xor(true, true));
        Assert.assertTrue(BooleanUtils.xor(true, false));
        Assert.assertTrue(BooleanUtils.xor(false, true));
        Assert.assertFalse(BooleanUtils.xor(false, false));
    }
}