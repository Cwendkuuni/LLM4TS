package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.BooleanUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertEquals(expectedFalse, BooleanUtils.negate(inputTrue));
 assertEquals(expectedTrue, BooleanUtils.negate(inputFalse));
 }

 @Test
 public void testIsTrue() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertTrue(BooleanUtils.isTrue(inputTrue));
 assertFalse(BooleanUtils.isTrue(inputFalse));
 }

 @Test
 public void testIsNotTrue() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertFalse(BooleanUtils.isNotTrue(inputTrue));
 assertTrue(BooleanUtils.isNotTrue(inputFalse));
 }

 @Test
 public void testIsFalse() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertFalse(BooleanUtils.isFalse(inputTrue));
 assertTrue(BooleanUtils.isFalse(inputFalse));
 }

 @Test
 public void testIsNotFalse() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertTrue(BooleanUtils.isNotFalse(inputTrue));
 assertFalse(BooleanUtils.isNotFalse(inputFalse));
 }

 @Test
 public void testToBoolean() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean inputFalse = Boolean.FALSE;
 final Integer inputIntegerTrue = 1;
 final Integer inputIntegerFalse = 0;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalse));
 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputIntegerTrue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputIntegerFalse));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean inputFalse = Boolean.FALSE;
 final Boolean nullInput = null;
 final boolean valueIfNull = true;

 assertEquals(expectedTrue, BooleanUtils.toBooleanDefaultIfNull(inputTrue, valueIfNull));
 assertEquals(valueIfNull, BooleanUtils.toBooleanDefaultIfNull(nullInput, valueIfNull));
 assertEquals(expectedFalse, BooleanUtils.toBooleanDefaultIfNull(inputFalse, valueIfNull));
 }

 @Test
 public void testToBooleanWithInteger() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Integer inputTrue = 1;
 final Integer inputFalse = 0;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalse));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Integer inputTrue = 1;
 final Integer inputFalse = 0;

 assertEquals(expectedTrue, BooleanUtils.toBooleanObject(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toBooleanObject(inputFalse));
 }

 @Test
 public void testToBooleanWithBooleanValue() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final boolean inputTrue = true;
 final boolean inputFalse = false;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalse));
 }

 @Test
 public void testToBooleanWithNullableBoolean() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final Boolean nullInput = null;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalse));
 assertEquals(null, BooleanUtils.toBoolean(nullInput));
 }

 @Test
 public void testToBooleanWithIntegerValue() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final int inputTrue = 1;
 final int inputFalse = 0;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrue, 1, 0));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalse, 1, 0));
 }

 @Test
 public void testToBooleanWithIntValues() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final int trueValue = 1;
 final int falseValue = 0;
 final int input = 1;

 assertEquals(expectedTrue, BooleanUtils.toBoolean(input, trueValue, falseValue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(input, falseValue, trueValue));
 }

 @Test
 public void testToBooleanWithIntegerObjects() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Integer trueValue = Integer.valueOf(1);
 final Integer falseValue = Integer.valueOf(0);
 final Integer input = Integer.valueOf(1);

 assertEquals(expectedTrue, BooleanUtils.toBoolean(input, trueValue, falseValue));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(input, falseValue, trueValue));
 }

 @Test
 public void testToBooleanObjectWithIntegerValues() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final int trueValue = 1;
 final int falseValue = 0;
 final int nullValue = 0;

 assertEquals(expectedTrue, BooleanUtils.toBooleanObject(trueValue, trueValue, falseValue, nullValue));
 assertEquals(expectedFalse, BooleanUtils.toBooleanObject(falseValue, trueValue, falseValue, nullValue));
 assertEquals(null, BooleanUtils.toBooleanObject(nullValue, trueValue, falseValue, nullValue));
 }

 @Test
 public void testToBooleanObjectWithIntegerObjects() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Integer trueValue = Integer.valueOf(1);
 final Integer falseValue = Integer.valueOf(0);
 final Integer nullValue = null;

 assertEquals(expectedTrue, BooleanUtils.toBooleanObject(trueValue, trueValue, falseValue, nullValue));
 assertEquals(expectedFalse, BooleanUtils.toBooleanObject(falseValue, trueValue, falseValue, nullValue));
 assertEquals(null, BooleanUtils.toBooleanObject(nullValue, trueValue, falseValue, nullValue));
 }

 @Test
 public void testToBooleanWithString() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final String inputTrueString = "true";
 final String inputFalseString = "false";

 assertEquals(expectedTrue, BooleanUtils.toBoolean(inputTrueString));
 assertEquals(expectedFalse, BooleanUtils.toBoolean(inputFalseString));
 }

 @Test
 public void testToBooleanObjectWithString() {
 final Boolean expectedTrue = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.FALSE;
 final String inputTrueString = "true";
 final String inputFalseString = "false";

 assertEquals(expectedTrue, BooleanUtils.toBooleanObject(inputTrueString));
 assertEquals(expectedFalse, BooleanUtils.toBooleanObject(inputFalseString));
 }

 @Test
 public void testToStringTrueFalse() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String expectedTrue = "true";
 final String expectedFalse = "false";

 assertEquals(expectedTrue, BooleanUtils.toStringTrueFalse(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toStringTrueFalse(inputFalse));
 }

 @Test
 public void testToStringOnOff() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String expectedOn = "on";
 final String expectedOff = "off";

 assertEquals(expectedOn, BooleanUtils.toStringOnOff(inputTrue));
 assertEquals(expectedOff, BooleanUtils.toStringOnOff(inputFalse));
 }

 @Test
 public void testToStringYesNo() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String expectedYes = "yes";
 final String expectedNo = "no";

 assertEquals(expectedYes, BooleanUtils.toStringYesNo(inputTrue));
 assertEquals(expectedNo, BooleanUtils.toStringYesNo(inputFalse));
 }

 @Test
 public void testToStringWithBoolean() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String expectedTrue = "true";
 final String expectedFalse = "false";

 assertEquals(expectedTrue, BooleanUtils.toString(inputTrue, "true", "false"));
 assertEquals(expectedFalse, BooleanUtils.toString(inputFalse, "true", "false"));
 }

 @Test
 public void testToStringWithBooleanTrueFalse() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String expectedTrue = "true";
 final String expectedFalse = "false";

 assertEquals(expectedTrue, BooleanUtils.toString(inputTrue));
 assertEquals(expectedFalse, BooleanUtils.toString(inputFalse));
 }

 @Test
 public void testToStringWithBooleanValues() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String trueString = "true";
 final String falseString = "false";

 assertEquals(trueString, BooleanUtils.toString(inputTrue, trueString, falseString));
 assertEquals(falseString, BooleanUtils.toString(inputFalse, trueString, falseString));
 }

 @Test
 public void testToStringWithStringValues() {
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;
 final String trueString = "true";
 final String falseString = "false";

 assertEquals(trueString, BooleanUtils.toString(inputTrue, trueString, falseString));
 assertEquals(falseString, BooleanUtils.toString(inputFalse, trueString, falseString));
 }

 @Test
 public void testAnd() {
 final boolean[] inputArray = new boolean[] { true, true, true };
 final boolean expected = true;

 assertTrue(BooleanUtils.and(inputArray));
 }

 @Test
 public void testAndWithBoolean() {
 final boolean expected = true;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputTrue2 = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertEquals(expected, BooleanUtils.and(inputTrue, inputTrue2));
 assertFalse(BooleanUtils.and(inputTrue, inputFalse));
 }

 @Test
 public void testAndWithBooleanArray() {
 final boolean expected = true;
 final boolean[] inputArray = new boolean[] { true, true, true };

 assertTrue(BooleanUtils.and(inputArray));
 }

 @Test
 public void testAndWithNullArray() {
 assertFalse(BooleanUtils.and((Boolean[]) null));
 }

 @Test
 public void testOr() {
 final boolean[] inputArray = new boolean[] { true, true, true };
 final boolean expected = true;

 assertTrue(BooleanUtils.or(inputArray));
 }

 @Test
 public void testOrWithBoolean() {
 final boolean expected = true;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputTrue2 = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertEquals(expected, BooleanUtils.or(inputTrue, inputTrue2));
 assertTrue(BooleanUtils.or(inputTrue, inputFalse));
 }

 @Test
 public void testOrWithBooleanArray() {
 final boolean expected = true;
 final boolean[] inputArray = new boolean[] { true, true, true };

 assertTrue(BooleanUtils.or(inputArray));
 }

 @Test
 public void testOrWithNullArray() {
 assertFalse(BooleanUtils.or((Boolean[]) null));
 }

 @Test
 public void testXor() {
 final boolean[] inputArray = new boolean[] { true, true, false };
 final boolean expected = false;

 assertEquals(expected, BooleanUtils.xor(inputArray));
 }

 @Test
 public void testXorWithBoolean() {
 final boolean expected = false;
 final Boolean inputTrue = Boolean.TRUE;
 final Boolean inputFalse = Boolean.FALSE;

 assertEquals(expected, BooleanUtils.xor(inputTrue, inputFalse));
 }

 @Test
 public void testXorWithBooleanArray() {
 final boolean expected = false;
 final boolean[] inputArray = new boolean[] { true, true, false };

 assertEquals(expected, BooleanUtils.xor(inputArray));
 }

 @Test
 public void testXorWithNullArray() {
 assertFalse(BooleanUtils.xor((Boolean[]) null));
 }
}