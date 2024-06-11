package org.junit.tests; // Replace with your package name if necessary

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NumberUtils;

public class BooleanUtilsTest {

    

    @org.junit.Test
    public void testNegate() {
        testNegate(Boolean.TRUE, Boolean.FALSE);
        testNegate(Boolean.FALSE, Boolean.TRUE);
        testNegate(null, null);
    }

    private void testNegate(Boolean expected, Boolean input) {
        assertEquals(expected, BooleanUtils.negate(input));
    }

    @org.junit.Test
    public void testIsTrue() {
        testIsTrue(Boolean.TRUE, true);
        testIsTrue(Boolean.FALSE, false);
        testIsTrue(null, false);
    }

    private void testIsTrue(Boolean input, boolean expected) {
        assertEquals(expected, BooleanUtils.isTrue(input));
    }

    @org.junit.Test
    public void testIsNotTrue() {
        testIsNotTrue(Boolean.TRUE, false);
        testIsNotTrue(Boolean.FALSE, true);
        testIsNotTrue(null, false);
    }

    @org.junit.Test
    public void testIsFalse() {
        testIsFalse(Boolean.TRUE, false);
        testIsFalse(Boolean.FALSE, true);
        testIsFalse(null, false);
    }

    @org.junit.Test
    public void testIsNotFalse() {
        testIsNotFalse(Boolean.TRUE, true);
        testIsNotFalse(Boolean.FALSE, false);
        testIsNotFalse(null, false);
    }

    @org.junit.Test
    public void testToBoolean() {
        testToBoolean(Boolean.TRUE, true);
        testToBoolean(Boolean.FALSE, false);
        testToBoolean(null, false);
        testToBoolean(-1, false);
        testToBoolean(0, false);
    }

    @org.junit.Test
    public void testToBooleanDefaultIfNull() {
        testToBooleanDefaultIfNull(Boolean.TRUE, true, true);
        testToBooleanDefaultIfNull(Boolean.FALSE, false, true);
        testToBooleanDefaultIfNull(null, true, true);
        testToBooleanDefaultIfNull(Boolean.TRUE, false, false);
        testToBooleanDefaultIfNull(Boolean.FALSE, true, false);
        testToBooleanDefaultIfNull(null, false, false);
    }

    @org.junit.Test
    public void testToBooleanWithInt() {
        testToBoolean(1, true);
        testToBoolean(0, false);
        testToBoolean(-1, false);
    }

    @org.junit.Test
    public void testToBooleanObjectWithInt() {
        testToBooleanObject(1, NumberUtils.INTEGER_ONE, Boolean.TRUE);
        testToBooleanObject(0, NumberUtils.INTEGER_ZERO, Boolean.FALSE);
        testToBooleanObject(-1, null, null);
    }

    @org.junit.Test
    public void testToBooleanWithInts() {
        testToBoolean(1, 1, 0, true);
        testToBoolean(0, 1, 0, false);
        testToBoolean(1, 0, 0, false);
        testToBoolean(0, 1, 1, false);
        testToBoolean(1, 1, 1, true);
        testToBoolean(1, 0, 1, false);
        testToBoolean(0, 1, 1, false);
        testToBoolean(1, 1, 0, false);
    }

    @org.junit.Test
    public void testToBooleanObjectWithInts() {
        testToBooleanObject(1, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, Boolean.TRUE);
        testToBooleanObject(0, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ONE, Boolean.FALSE);
        testToBooleanObject(1, null, NumberUtils.INTEGER_ONE, null);
        testToBooleanObject(1, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, Boolean.FALSE);
        testToBooleanObject(1, NumberUtils.INTEGER_ONE, null, null);
        testToBooleanObject(1, null, null, null);
    }

    @org.junit.Test
    public void testToInteger() {
        testToInteger(Boolean.TRUE, 1);
        testToInteger(Boolean.FALSE, 0);
    }

    @org.junit.Test
    public void testToIntegerObjectWithBoolean() {
        testToIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE);
        testToIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ZERO);
    }

    @org.junit.Test
    public void testToIntegerObjectWithNullableBoolean() {
        testToIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE);
        testToIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ZERO);
        testToIntegerObject(null, null);
    }

    @org.junit.Test
    public void testToIntegerWithBooleanAndInts() {
        testToInteger(Boolean.TRUE, 1, 1, true);
        testToInteger(Boolean.FALSE, 0, 1, false);
        testToInteger(Boolean.TRUE, 1, 0, false);
        testToInteger(Boolean.FALSE, 0, 0, false);
    }

    @org.junit.Test
    public void testToIntegerObjectWithNullableBooleanAndInts() {
        testToIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ONE, Boolean.TRUE);
        testToIntegerObject(Boolean.FALSE, null, NumberUtils.INTEGER_ONE, Boolean.FALSE);
        testToIntegerObject(null, null, null, null);
    }

    @org.junit.Test
    public void testToBooleanObjectWithString() {
        testToBooleanObject("true", Boolean.TRUE);
        testToBooleanObject("false", Boolean.FALSE);
        testToBooleanObject("null", null);
        testToBooleanObject("", null);
        testToBooleanObject("yes", null);
        testToBooleanObject("YES", null);
        testToBooleanObject("no", null);
        testToBooleanObject("NO", null);
        testToBooleanObject("on", null);
        testToBooleanObject("ON", null);
        testToBooleanObject("off", null);
        testToBooleanObject("OFF", null);
    }

    @org.junit.Test
    public void testToBooleanObjectWithStrings() {
        testToBooleanObject("true", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        testToBooleanObject("false", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        testToBooleanObject("null", null, null, null);
        testToBooleanObject("yes", null, null, null);
        testToBooleanObject("YES", null, null, null);
        testToBooleanObject("no", null, null, null);
        testToBooleanObject("NO", null, null, null);
        testToBooleanObject("on", null, null, null);
        testToBooleanObject("ON", null, null, null);
        testToBooleanObject("off", null, null, null);
        testToBooleanObject("OFF", null, null, null);
    }

    @org.junit.Test
    public void testToStringTrueFalse() {
        testToStringTrueFalse("true", Boolean.TRUE);
        testToStringTrueFalse("false", Boolean.FALSE);
        testToStringTrueFalse(null, null);
    }

    @org.junit.Test
    public void testToStringOnOff() {
        testToStringOnOff("on", Boolean.TRUE);
        testToStringOnOff("off", Boolean.FALSE);
        testToStringOnOff(null, null);
    }

    @org.junit.Test
    public void testToStringYesNo() {
        testToStringYesNo("yes", Boolean.TRUE);
        testToStringYesNo("no", Boolean.FALSE);
        testToStringYesNo(null, null);
    }

    @org.junit.Test
    public void testToStringWithBoolean() {
        testToString("true", Boolean.TRUE);
        testToString("false", Boolean.FALSE);
        testToString(null, null);
    }

    @org.junit.Test
    public void testToStringWithString() {
        testToString("true", Boolean.TRUE, "true", "true");
        testToString("false", Boolean.FALSE, "false", "false");
        testToString("null", null, null, null);
    }

    @org.junit.Test
    public void testAnd() {
        testAnd(new boolean[] { true, true, true }, true);
        testAnd(new boolean[] { true, true, false }, false);
        testAnd(new boolean[] { false, false, false }, false);
        testAnd(null, false);
        testAnd(new boolean[0], false);
    }

    @org.junit.Test
    public void testAndWithBooleans() {
        testAnd(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }), Boolean.TRUE);
        testAnd(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }), Boolean.FALSE);
        testAnd(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }), Boolean.FALSE);
        testAnd(null, false);
        testAnd(new Boolean[0][], false);
    }

    @org.junit.Test
    public void testOr() {
        testOr(new boolean[] { true, true, true }, true);
        testOr(new boolean[] { true, true, false }, true);
        testOr(new boolean[] { false, false, false }, false);
        testOr(null, false);
        testOr(new boolean[0], false);
    }

    @org.junit.Test
    public void testOrWithBooleans() {
        testOr(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }), Boolean.TRUE);
        testOr(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }), Boolean.TRUE);
        testOr(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }), Boolean.FALSE);
        testOr(null, false);
        testOr(new Boolean[0][], false);
    }

    @org.junit.Test
    public void testXor() {
        testXor(new boolean[] { true, true, true }, false);
        testXor(new boolean[] { true, true, false }, true);
        testXor(new boolean[] { false, false, false }, false);
        testXor(null, false);
        testXor(new boolean[0], false);
    }

    @org.junit.Test
    public void testXorWithBooleans() {
        testXor(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }), false);
        testXor(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }), true);
        testXor(BooleanUtils.toBooleanArray(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }), false);
        testXor(null, false);
        testXor(new Boolean[0][], false);
    }

    private void testAnd(boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.and(input) == expected);
    }

    private void testAnd(Boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.and(input) == expected);
    }

    private void testOr(boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.or(input) == expected);
    }

    private void testOr(Boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.or(input) == expected);
    }

    private void testXor(boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.xor(input) == expected);
    }

    private void testXor(Boolean[] input, boolean expected) {
        assertTrue(BooleanUtils.xor(input) == expected);
    }

    private void testToString(String input, Boolean expected) {
        assertEquals(expected.toString(), BooleanUtils.toString(input));
    }

    private void testToString(String input, Boolean expected, String trueString, String falseString) {
        assertEquals(expected.toString(), BooleanUtils.toString(input, trueString, falseString));
    }

    
}