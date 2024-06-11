package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
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
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        Assert.assertTrue(BooleanUtils.toBoolean(0));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.MAX_VALUE));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    @Test
    public void testToBooleanWithBoolean() {
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
    }

    @Test
    public void testToBooleanWithInteger() {
        Assert.assertFalse(BooleanUtils.toBoolean(-1));
        Assert.assertTrue(BooleanUtils.toBoolean(0));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.MAX_VALUE));
    }

    @Test
    public void testToBooleanObject() {
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
        Assert.assertFalse(BooleanUtils.toBooleanObject(Boolean.FALSE));
        Assert.assertTrue(BooleanUtils.toBooleanObject(Boolean.TRUE));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertFalse(BooleanUtils.toBooleanObject(Integer.MIN_VALUE));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
        Assert.assertTrue(BooleanUtils.toBooleanObject(Integer.ZERO));
        Assert.assertTrue(BooleanUtils.toBooleanObject(Integer.ONE));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        final boolean[] input = {true, false, null};
        Assert.assertTrue(BooleanUtils.and(input));
        final Boolean[] expected = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertArrayEquals(expected, BooleanUtils.and(BooleanUtils.toBooleanArray(input)));
    }

    @Test
    public void testToBooleanWithBooleanArray2() {
        final Boolean[] input = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertTrue(BooleanUtils.and(input));
        final boolean[] expected = {true, false, null};
        Assert.assertArrayEquals(expected, BooleanUtils.toBooleanArray(input));
    }

    @Test
    public void testToBooleanWithBooleanArray3() {
        final boolean[] input = {true, false, null};
        Assert.assertFalse(BooleanUtils.or(input));
        final Boolean[] expected = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertArrayEquals(expected, BooleanUtils.or(BooleanUtils.toBooleanArray(input)));
    }

    @Test
    public void testToBooleanWithBooleanArray4() {
        final Boolean[] input = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertFalse(BooleanUtils.or(input));
        final boolean[] expected = {true, false, null};
        Assert.assertArrayEquals(expected, BooleanUtils.toBooleanArray(input));
    }

    @Test
    public void testToBooleanWithBooleanArray5() {
        final boolean[] input = {true, false, null};
        Assert.assertTrue(BooleanUtils.xor(input));
        final Boolean[] expected = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertArrayEquals(expected, BooleanUtils.xor(BooleanUtils.toBooleanArray(input)));
    }

    @Test
    public void testToBooleanWithBooleanArray6() {
        final Boolean[] input = {Boolean.TRUE, Boolean.FALSE, null};
        Assert.assertFalse(BooleanUtils.xor(input));
        final boolean[] expected = {true, false, null};
        Assert.assertArrayEquals(expected, BooleanUtils.toBooleanArray(input));
    }
}