package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

    @org.junit.Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean result = BooleanUtils.negate(bool);
        Assert.assertNull(result); // Test for null input
        result = BooleanUtils.negate(bool);
        Assert.assertEquals(Boolean.FALSE, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.negate(bool);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testIsTrue() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isTrue(bool);
        Assert.assertEquals(true, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isTrue(bool);
        Assert.assertEquals(false, result);

        result = BooleanUtils.isTrue(null);
        Assert.assertEquals(false, result);
    }

    @org.junit.Test
    public void testIsNotTrue() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isNotTrue(bool);
        Assert.assertEquals(false, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isNotTrue(bool);
        Assert.assertEquals(true, result);

        result = BooleanUtils.isNotTrue(null);
        Assert.assertEquals(true, result);
    }

    @org.junit.Test
    public void testIsFalse() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isFalse(bool);
        Assert.assertEquals(false, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isFalse(bool);
        Assert.assertEquals(true, result);

        result = BooleanUtils.isFalse(null);
        Assert.assertEquals(true, result);
    }

    @org.junit.Test
    public void testIsNotFalse() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.isNotFalse(bool);
        Assert.assertEquals(true, result);

        bool = Boolean.FALSE;
        result = BooleanUtils.isNotFalse(bool);
        Assert.assertEquals(false, result);

        result = BooleanUtils.isNotFalse(null);
        Assert.assertEquals(false, result);
    }

    @org.junit.Test
    public void testToBoolean() {
        Boolean result = BooleanUtils.toBoolean(Boolean.TRUE);
        Assert.assertEquals(Boolean.TRUE, result);

        int intValue = 1;
        result = BooleanUtils.toBoolean(intValue);
        Assert.assertEquals(Boolean.TRUE, result);

        intValue = 0;
        result = BooleanUtils.toBoolean(intValue);
        Assert.assertEquals(Boolean.FALSE, result);

        result = BooleanUtils.toBoolean(null);
        Assert.assertNull(result);

        try {
            result = BooleanUtils.toBoolean("hello");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        Boolean bool = Boolean.TRUE;
        boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        Assert.assertEquals(Boolean.TRUE, result);

        bool = null;
        result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
        Assert.assertEquals(false, result);
    }

    @org.junit.Test
    public void testToStringTrueFalse() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringTrueFalse(bool);
        Assert.assertEquals("true", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringTrueFalse(bool);
        Assert.assertEquals("false", result);

        result = BooleanUtils.toStringTrueFalse(null);
        Assert.assertNull(result);
    }

    @org.junit.Test
    public void testToStringOnOff() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringOnOff(bool);
        Assert.assertEquals("on", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringOnOff(bool);
        Assert.assertEquals("off", result);

        result = BooleanUtils.toStringOnOff(null);
        Assert.assertNull(result);
    }

    @org.junit.Test
    public void testToStringYesNo() {
        Boolean bool = Boolean.TRUE;
        String result = BooleanUtils.toStringYesNo(bool);
        Assert.assertEquals("yes", result);

        bool = Boolean.FALSE;
        result = BooleanUtils.toStringYesNo(bool);
        Assert.assertEquals("no", result);

        result = BooleanUtils.toStringYesNo(null);
        Assert.assertNull(result);
    }

    // Add more test cases for the rest of the methods

    @org.junit.Test
    public void testAnd() {
        boolean[] boolArray = {true, true, true};
        boolean result = BooleanUtils.and(boolArray);
        Assert.assertEquals(true, result);

        boolArray = new boolean[]{true, true, false};
        result = BooleanUtils.and(boolArray);
        Assert.assertEquals(false, result);

        boolArray = null;
        try {
            result = BooleanUtils.and(boolArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testOr() {
        boolean[] boolArray = {true, true, true};
        boolean result = BooleanUtils.or(boolArray);
        Assert.assertEquals(true, result);

        boolArray = new boolean[]{true, true, false};
        result = BooleanUtils.or(boolArray);
        Assert.assertEquals(true, result);

        boolArray = new boolean[]{false, false, false};
        result = BooleanUtils.or(boolArray);
        Assert.assertEquals(false, result);

        boolArray = null;
        try {
            result = BooleanUtils.or(boolArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testXor() {
        boolean[] boolArray = {true, false, true};
        boolean result = BooleanUtils.xor(boolArray);
        Assert.assertEquals(true, result);

        boolArray = new boolean[]{false, true, false};
        result = BooleanUtils.xor(boolArray);
        Assert.assertEquals(true, result);

        boolArray = new boolean[]{true, true, true};
        result = BooleanUtils.xor(boolArray);
        Assert.assertEquals(false, result);

        boolArray = null;
        try {
            result = BooleanUtils.xor(boolArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}