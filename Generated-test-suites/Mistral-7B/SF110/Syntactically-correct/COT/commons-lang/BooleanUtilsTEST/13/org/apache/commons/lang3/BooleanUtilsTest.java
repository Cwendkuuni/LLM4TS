package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        testBooleanNegate(true, false);
        testBooleanNegate(false, true);
        testBooleanNegate(null, null);
    }

    private void testBooleanNegate(Boolean bool, Boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.negate(bool));
    }

    @Test
    public void testIsTrue() {
        testIsTrueFalse(true, true);
        testIsTrueFalse(false, false);
        testIsTrueFalse(null, null);
    }

    @Test
    public void testIsNotTrue() {
        testIsTrueFalse(true, false);
        testIsTrueFalse(false, true);
        testIsTrueFalse(null, null);
    }

    @Test
    public void testIsFalse() {
        testIsTrueFalse(true, false);
        testIsTrueFalse(false, true);
        testIsTrueFalse(null, null);
    }

    @Test
    public void testIsNotFalse() {
        testIsTrueFalse(true, true);
        testIsTrueFalse(false, false);
        testIsTrueFalse(null, null);
    }

    @Test
    public void testToBoolean() {
        testToBooleanWithBoolean(true);
        testToBooleanWithBoolean(false);
        testToBooleanWithBoolean(null);
        testToBooleanWithInteger(1);
        testToBooleanWithInteger(0);
        testToBooleanWithInteger(-1);
        testToBooleanWithPrimitive(1);
        testToBooleanWithPrimitive(0);
        testToBooleanWithPrimitive(-1);
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        testToBooleanDefaultIfNullWithBoolean(true, true);
        testToBooleanDefaultIfNullWithBoolean(false, false);
        testToBooleanDefaultIfNullWithBoolean(null, true);
        testToBooleanDefaultIfNullWithBooleanAndValue(true, true, 1);
        testToBooleanDefaultIfNullWithBooleanAndValue(false, false, 0);
        testToBooleanDefaultIfNullWithBooleanAndValue(null, true, 1);
        testToBooleanDefaultIfNullWithBooleanAndValue(null, false, 0);
    }

    @Test
    public void testToBooleanWithString() {
        testToBooleanWithString("true");
        testToBooleanWithString("false");
        testToBooleanWithString("null");
        testToBooleanWithString("yes");
        testToBooleanWithString("no");
        testToBooleanWithString("on");
        testToBooleanWithString("off");
    }

    @Test
    public void testToStringTrueFalse() {
        testToStringTrueFalse(Boolean.TRUE);
        testToStringTrueFalse(Boolean.FALSE);
        testToStringTrueFalse(null);
    }

    @Test
    public void testToStringOnOff() {
        testToStringOnOff(Boolean.TRUE);
        testToStringOnOff(Boolean.FALSE);
        testToStringOnOff(null);
    }

    @Test
    public void testToStringYesNo() {
        testToStringYesNo(Boolean.TRUE);
        testToStringYesNo(Boolean.FALSE);
        testToStringYesNo(null);
    }

    @Test
    public void testToString() {
        testToString(Boolean.TRUE, "true");
        testToString(Boolean.FALSE, "false");
        testToString(null, null);
    }

    @Test
    public void testToStringWithValues() {
        testToStringWithValues(Boolean.TRUE, "true", "trueString");
        testToStringWithValues(Boolean.FALSE, "false", "falseString");
        testToStringWithValues(null, "nullString", null);
    }

    @Test
    public void testAnd() {
        testAndWithBoolean(true, true, true);
        testAndWithBoolean(true, true, false);
        testAndWithBoolean(true, false, false);
        testAndWithBoolean(false, true, false);
        testAndWithBoolean(false, false, false);
        testAndWithBooleanArray(new boolean[]{true, true, true});
        testAndWithBooleanArray(new boolean[]{true, true, false});
        testAndWithBooleanArray(new boolean[]{false, false, false});
        testAndWithBooleanArray(new boolean[]{true, false});
        testAndWithBooleanArray(new boolean[]{false, true});
        testAndWithBooleanArray(new boolean[]{});
        testAndWithBooleanArrayNull();
        testAndWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        testAndWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        testAndWithBooleanAndBoolean(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        testAndWithBooleanAndBoolean(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        testAndWithBooleanAndBoolean(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        testAndWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
        testAndWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        testAndWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE});
        testAndWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.FALSE});
        testAndWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.TRUE});
        testAndWithBooleanAndBooleanArray(new Boolean[]{});
        testAndWithBooleanAndBooleanArrayNull();
    }

    @Test
    public void testOr() {
        testOrWithBoolean(true, true, true);
        testOrWithBoolean(true, true, false);
        testOrWithBoolean(true, false, true);
        testOrWithBoolean(false, true, true);
        testOrWithBoolean(false, false, false);
        testOrWithBooleanArray(new boolean[]{true, true, true});
        testOrWithBooleanArray(new boolean[]{true, true, false});
        testOrWithBooleanArray(new boolean[]{false, false, false});
        testOrWithBooleanArray(new boolean[]{true, false});
        testOrWithBooleanArray(new boolean[]{false, true});
        testOrWithBooleanArray(new boolean[]{});
        testOrWithBooleanArrayNull();
        testOrWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        testOrWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        testOrWithBooleanAndBoolean(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        testOrWithBooleanAndBoolean(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
        testOrWithBooleanAndBoolean(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        testOrWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
        testOrWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        testOrWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE});
        testOrWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.FALSE});
        testOrWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.TRUE});
        testOrWithBooleanAndBooleanArray(new Boolean[]{});
        testOrWithBooleanAndBooleanArrayNull();
    }

    @Test
    public void testXor() {
        testXorWithBoolean(true, true, false);
        testXorWithBoolean(true, true, true);
        testXorWithBoolean(true, false, true);
        testXorWithBoolean(false, true, true);
        testXorWithBoolean(false, false, false);
        testXorWithBooleanArray(new boolean[]{true, true, true});
        testXorWithBooleanArray(new boolean[]{true, true, false});
        testXorWithBooleanArray(new boolean[]{false, false, false});
        testXorWithBooleanArray(new boolean[]{true, false});
        testXorWithBooleanArray(new boolean[]{false, true});
        testXorWithBooleanArray(new boolean[]{});
        testXorWithBooleanArrayNull();
        testXorWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        testXorWithBooleanAndBoolean(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        testXorWithBooleanAndBoolean(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        testXorWithBooleanAndBoolean(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
        testXorWithBooleanAndBoolean(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        testXorWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
        testXorWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
        testXorWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE});
        testXorWithBooleanAndBooleanArray(new Boolean[]{Boolean.TRUE, Boolean.FALSE});
        testXorWithBooleanAndBooleanArray(new Boolean[]{Boolean.FALSE, Boolean.TRUE});
        testXorWithBooleanAndBooleanArray(new Boolean[]{});
        testXorWithBooleanAndBooleanArrayNull();
    }

    private void testIsTrueFalse(boolean bool, boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.isTrue(bool));
    }

    private void testIsTrueFalse(Boolean bool, boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.isTrue(bool));
    }

    private void testIsTrueFalse(Boolean bool, boolean expected, String message) {
        try {
            Assert.assertEquals(expected, BooleanUtils.isTrue(bool), message);
        } catch (AssertionError e) {
            throw new RuntimeException(message, e);
        }
    }

    private void testIsTrueFalse(boolean bool, boolean expected, String trueString, String falseString) {
        Assert.assertEquals(expected, BooleanUtils.toBoolean(bool, trueString, falseString));
    }

    private void testIsTrueFalse(Boolean bool, boolean expected, String trueString, String falseString) {
        Assert.assertEquals(expected, BooleanUtils.toBoolean(bool, trueString, falseString));
    }

    private void testIsTrueFalse(Boolean bool, boolean expected, String trueString, String falseString, String nullString) {
        Assert.assertEquals(expected, BooleanUtils.toBooleanObject(bool, trueString, falseString, nullString));
    }

    private void testToBooleanWithBoolean(Boolean bool) {
        Assert.assertEquals(bool, BooleanUtils.toBooleanObject(BooleanUtils.toBoolean(bool)));
    }

    private void testToBooleanWithBoolean(boolean bool) {
        Assert.assertEquals(BooleanUtils.toBoolean(bool), BooleanUtils.toBooleanObject(BooleanUtils.toBoolean(bool)));
    }

    private void testToBooleanWithBoolean(boolean bool, boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.toBoolean(bool));
    }

    private void testToBooleanWithBoolean(Boolean bool, boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.toBooleanObject(bool).booleanValue());
    }

    private void testToBooleanWithInteger(int value) {
        Assert.assertEquals(BooleanUtils.toBoolean(value), BooleanUtils.toBooleanObject(value));
    }

    private void testToBooleanWithPrimitive(int value) {
        Assert.assertEquals(BooleanUtils.toBoolean(value), BooleanUtils.toBooleanObject(value).booleanValue());
    }

    private void testToBooleanDefaultIfNullWithBooleanAndValue(boolean bool, boolean expectedValue, int value) {
        Assert.assertEquals(expectedValue, BooleanUtils.toBooleanDefaultIfNull(bool, value));
    }

    private void testToBooleanWithString(String str) {
        Assert.assertEquals(BooleanUtils.toBooleanObject(BooleanUtils.toBoolean(str)), BooleanUtils.toBooleanObject(str));
    }

    private void testToStringTrueFalse(Boolean bool) {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(bool));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    private void testToStringOnOff(Boolean bool) {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(bool));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    private void testToStringYesNo(Boolean bool) {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(bool));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    private void testToString(Boolean bool, String expected) {
        Assert.assertEquals(expected, BooleanUtils.toString(bool));
    }

    private void testToStringWithValues(Boolean bool, String expectedValue, String valueString) {
        Assert.assertEquals(expectedValue, BooleanUtils.toString(bool, valueString));
    }

    private void testAndWithBoolean(boolean... bools) {
        Assert.assertEquals(BooleanUtils.and(bools), Boolean.valueOf(MathUtils.multiply(bools.length, 0.5f) > 0.5f));
    }

    private void testAndWithBoolean(Boolean... bools) {
        Assert.assertEquals(BooleanUtils.and(bools), BooleanUtils.and(ArrayUtils.toPrimitive(bools)));
    }

    private void testAndWithBooleanArray(boolean[] bools) {
        Assert.assertEquals(BooleanUtils.and(bools), Boolean.valueOf(MathUtils.multiply(bools.length, 0.5f) > 0.5f));
    }

    private void testAndWithBooleanArrayNull() {
        try {
            BooleanUtils.and((boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    private void testAndWithBooleanAndBoolean(Boolean a, Boolean b, Boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.and(a, b));
    }

    private void testOrWithBoolean(boolean... bools) {
        Assert.assertEquals(BooleanUtils.or(bools), Boolean.valueOf(MathUtils.sum(bools.length, 0) > 0));
    }

    private void testOrWithBoolean(Boolean... bools) {
        Assert.assertEquals(BooleanUtils.or(bools), BooleanUtils.or(ArrayUtils.toPrimitive(bools)));
    }

    private void testOrWithBooleanArray(boolean[] bools) {
        Assert.assertEquals(BooleanUtils.or(bools), Boolean.valueOf(MathUtils.sum(bools.length, 0) > 0));
    }

    private void testOrWithBooleanArrayNull() {
        try {
            BooleanUtils.or((boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Array must not be null", e.getMessage());
        }
    }

    private void testOrWithBooleanAndBoolean(Boolean a, Boolean b, Boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.or(a, b));
    }

    private void testXorWithBoolean(boolean... bools) {
        Assert.assertEquals(BooleanUtils.xor(bools), BooleanUtils.xor(ArrayUtils.toPrimitive(bools)));
    }

    private void testXorWithBooleanAndBoolean(Boolean a, Boolean b, Boolean expected) {
        Assert.assertEquals(expected, BooleanUtils.xor(a, b));
    }

    private void testXorWithBooleanArray(boolean[] bools) {
        Assert.assertEquals(BooleanUtils.xor(bools), BooleanUtils.xor(ArrayUtils.toPrimitive(bools)));
    }
}