package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 final Boolean negatedBool = BooleanUtils.negate(bool);
 final Boolean expectedNegatedBool = Boolean.FALSE;
 assertEquals(expectedNegatedBool, negatedBool);
 final Boolean nullBool = null;
 final Boolean negatedNullBool = BooleanUtils.negate(nullBool);
 assertNull(negatedNullBool);
 }

 @Test
 public void testIsTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean result = BooleanUtils.isTrue(bool);
 final boolean expectedResult = true;
 assertTrue(result);
 final Boolean falseBool = Boolean.FALSE;
 final boolean resultFalse = BooleanUtils.isTrue(falseBool);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 }

 @Test
 public void testIsNotTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean result = BooleanUtils.isNotTrue(bool);
 final boolean expectedResult = false;
 assertFalse(result);
 final Boolean falseBool = Boolean.FALSE;
 final boolean resultFalse = BooleanUtils.isNotTrue(falseBool);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 final Boolean nullBool = null;
 final boolean resultNull = BooleanUtils.isNotTrue(nullBool);
 final boolean expectedResultNull = true;
 assertTrue(resultNull);
 }

 @Test
 public void testIsFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean result = BooleanUtils.isFalse(bool);
 final boolean expectedResult = true;
 assertTrue(result);
 final Boolean trueBool = Boolean.TRUE;
 final boolean resultTrue = BooleanUtils.isFalse(trueBool);
 final boolean expectedResultTrue = false;
 assertFalse(resultTrue);
 }

 @Test
 public void testIsNotFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean result = BooleanUtils.isNotFalse(bool);
 final boolean expectedResult = false;
 assertFalse(result);
 final Boolean trueBool = Boolean.TRUE;
 final boolean resultTrue = BooleanUtils.isNotFalse(trueBool);
 final boolean expectedResultTrue = true;
 assertTrue(resultTrue);
 }

 @Test
 public void testToBoolean() {
 final Boolean bool = Boolean.TRUE;
 final boolean result = BooleanUtils.toBoolean(bool);
 final boolean expectedResult = true;
 assertTrue(result);
 final Boolean falseBool = Boolean.FALSE;
 final boolean resultFalse = BooleanUtils.toBoolean(falseBool);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 final Integer intValueTrue = Integer.valueOf(1);
 final boolean resultIntValueTrue = BooleanUtils.toBoolean(intValueTrue);
 final boolean expectedResultIntValueTrue = true;
 assertTrue(resultIntValueTrue);
 final Integer intValueFalse = Integer.valueOf(0);
 final boolean resultIntValueFalse = BooleanUtils.toBoolean(intValueFalse);
 final boolean expectedResultIntValueFalse = false;
 assertFalse(resultIntValueFalse);
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
 assertTrue(result);
 final Boolean nullBool = null;
 final boolean expectedResultNull = true;
 final boolean resultNull = BooleanUtils.toBooleanDefaultIfNull(nullBool, false);
 assertTrue(resultNull);
 }

 @Test
 public void testToBooleanWithIllegalArgumentException() {
 final Boolean bool = Boolean.TRUE;
 final boolean[] expectedValues = {1, 2, 3};
 final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean(bool, expectedValues[0], expectedValues[1]));
 final String expectedMessage = "The Boolean did not match either specified value";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testToInteger() {
 final boolean bool = true;
 final int result = BooleanUtils.toInteger(bool);
 final int expectedResult = 1;
 assertEquals(expectedResult, result);
 final boolean falseBool = false;
 final int resultFalse = BooleanUtils.toInteger(falseBool);
 final int expectedResultFalse = 0;
 assertEquals(expectedResultFalse, resultFalse);
 }

 @Test
 public void testToIntegerObject() {
 final boolean bool = true;
 final Integer result = BooleanUtils.toIntegerObject(bool);
 final Integer expectedResult = Integer.valueOf(1);
 assertEquals(expectedResult, result);
 final boolean falseBool = false;
 final Integer resultFalse = BooleanUtils.toIntegerObject(falseBool);
 final Integer expectedResultFalse = Integer.valueOf(0);
 assertEquals(expectedResultFalse, resultFalse);
 final Boolean nullBool = null;
 final Integer resultNull = BooleanUtils.toIntegerObject(nullBool);
 assertNull(resultNull);
 }

 @Test
 public void testToIntegerWithIllegalArgumentException() {
 final boolean bool = true;
 final int[] expectedValues = {2, 3};
 final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toInteger(bool, expectedValues[0], expectedValues[1]));
 final String expectedMessage = "The Boolean did not match either specified value";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testToBooleanObject() {
 final String strTrue = "true";
 final Boolean resultStrTrue = BooleanUtils.toBooleanObject(strTrue);
 final Boolean expectedResultStrTrue = Boolean.TRUE;
 assertEquals(expectedResultStrTrue, resultStrTrue);
 final String strFalse = "false";
 final Boolean resultStrFalse = BooleanUtils.toBooleanObject(strFalse);
 final Boolean expectedResultStrFalse = Boolean.FALSE;
 assertEquals(expectedResultStrFalse, resultStrFalse);
 final String nullStr = null;
 final Boolean resultNullStr = BooleanUtils.toBooleanObject(nullStr);
 assertNull(resultNullStr);
 }

 @Test
 public void testToBooleanObjectWithIllegalArgumentException() {
 final String strTrue = "true";
 final String strFalse = "false";
 final String thirdString = "third";
 final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanObject(strTrue, strFalse, thirdString, null));
 final String expectedMessage = "The String did not match any specified value";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testToStringTrueFalse() {
 final Boolean bool = Boolean.TRUE;
 final String result = BooleanUtils.toStringTrueFalse(bool);
 final String expectedResult = "true";
 assertEquals(expectedResult, result);
 final Boolean falseBool = Boolean.FALSE;
 final String resultFalse = BooleanUtils.toStringTrueFalse(falseBool);
 final String expectedResultFalse = "false";
 assertEquals(expectedResultFalse, resultFalse);
 }

 @Test
 public void testToStringOnOff() {
 final Boolean bool = Boolean.TRUE;
 final String result = BooleanUtils.toStringOnOff(bool);
 final String expectedResult = "on";
 assertEquals(expectedResult, result);
 final Boolean falseBool = Boolean.FALSE;
 final String resultFalse = BooleanUtils.toStringOnOff(falseBool);
 final String expectedResultFalse = "off";
 assertEquals(expectedResultFalse, resultFalse);
 }

 @Test
 public void testToStringYesNo() {
 final Boolean bool = Boolean.TRUE;
 final String result = BooleanUtils.toStringYesNo(bool);
 final String expectedResult = "yes";
 assertEquals(expectedResult, result);
 final Boolean falseBool = Boolean.FALSE;
 final String resultFalse = BooleanUtils.toStringYesNo(falseBool);
 final String expectedResultFalse = "no";
 assertEquals(expectedResultFalse, resultFalse);
 }

 @Test
 public void testToStringWithIllegalArgumentException() {
 final Boolean bool = Boolean.TRUE;
 final String thirdString = "third";
 final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toString(bool, "true", "false", thirdString));
 final String expectedMessage = "The String did not match any specified value";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testToBooleanArray() {
 final boolean[] booleanArray = new boolean[] {true, false, true};
 final Boolean[] result = BooleanUtils.toBooleanArray(booleanArray);
 final Boolean[] expectedResult = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertArrayEquals(expectedResult, result);
 final Boolean[] booleanArrayNull = null;
 final Boolean[] resultNull = BooleanUtils.toBooleanArray(booleanArrayNull);
 assertNull(resultNull);
 }

 @Test
 public void testToBooleanArrayWithIllegalArgumentException() {
 final boolean[] booleanArray = new boolean[] {true, false, true};
 final Boolean[] expectedValues = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
 final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanArray(booleanArray, expectedValues[0], expectedValues[1]));
 final String expectedMessage = "The Array must not contain any null elements";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testAnd() {
 final boolean[] booleanArray = new boolean[] {true, true, true};
 final boolean result = BooleanUtils.and(booleanArray);
 final boolean expectedResult = true;
 assertTrue(result);
 final boolean[] booleanArrayFalse = new boolean[] {false, false, false};
 final boolean resultFalse = BooleanUtils.and(booleanArrayFalse);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 final boolean[] booleanArrayNull = null;
 final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> BooleanUtils.and(booleanArrayNull));
 final String expectedMessage = "The Array must not be null";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testAndWithBooleanArray() {
 final boolean[] booleanArray = new boolean[] {true, true, true};
 final boolean[] expectedBooleanArray = new boolean[] {true, true, true};
 final boolean result = BooleanUtils.and(BooleanUtils.toBooleanArray(booleanArray));
 assertEquals(expectedBooleanArray[0], result);
 final boolean result1 = BooleanUtils.and(BooleanUtils.toBooleanArray(booleanArray), BooleanUtils.toBooleanArray(new boolean[] {false, false, false}));
 final boolean[] expectedResult1 = new boolean[] {false, false, false};
 assertArrayEquals(expectedResult1, BooleanUtils.toBooleanArray(result));
 }

 @Test
 public void testOr() {
 final boolean[] booleanArray = new boolean[] {true, true, true};
 final boolean result = BooleanUtils.or(booleanArray);
 final boolean expectedResult = true;
 assertTrue(result);
 final boolean[] booleanArrayFalse = new boolean[] {false, false, false};
 final boolean resultFalse = BooleanUtils.or(booleanArrayFalse);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 final boolean[] booleanArrayNull = null;
 final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> BooleanUtils.or(booleanArrayNull));
 final String expectedMessage = "The Array must not be null";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testOrWithBooleanArray() {
 final boolean[] booleanArray = new boolean[] {true, true, true};
 final boolean[] expectedBooleanArray = new boolean[] {true, true, true};
 final boolean result = BooleanUtils.or(BooleanUtils.toBooleanArray(booleanArray));
 assertEquals(expectedBooleanArray[0], result);
 final boolean result1 = BooleanUtils.or(BooleanUtils.toBooleanArray(booleanArray), BooleanUtils.toBooleanArray(new boolean[] {false, false, false}));
 final boolean[] expectedResult1 = new boolean[] {true, true, true};
 assertArrayEquals(expectedResult1, BooleanUtils.toBooleanArray(result));
 }

 @Test
 public void testXor() {
 final boolean[] booleanArray = new boolean[] {true, true, false};
 final boolean result = BooleanUtils.xor(booleanArray);
 final boolean expectedResult = false;
 assertFalse(result);
 final boolean[] booleanArrayFalse = new boolean[] {false, false, false};
 final boolean resultFalse = BooleanUtils.xor(booleanArrayFalse);
 final boolean expectedResultFalse = false;
 assertFalse(resultFalse);
 final boolean[] booleanArrayNull = null;
 final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> BooleanUtils.xor(booleanArrayNull));
 final String expectedMessage = "The Array must not be null";
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testXorWithBooleanArray() {
 final boolean[] booleanArray = new boolean[] {true, true, false};
 final boolean[] expectedBooleanArray = new boolean[] {false, false, true};
 final boolean result = BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanArray));
 assertEquals(expectedBooleanArray[0], result);
 final boolean result1 = BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanArray), BooleanUtils.toBooleanArray(new boolean[] {true, true, false}));
 final boolean[] expectedResult1 = new boolean[] {false, false, true};
 assertArrayEquals(expectedResult1, BooleanUtils.toBooleanArray(result));
 }

}