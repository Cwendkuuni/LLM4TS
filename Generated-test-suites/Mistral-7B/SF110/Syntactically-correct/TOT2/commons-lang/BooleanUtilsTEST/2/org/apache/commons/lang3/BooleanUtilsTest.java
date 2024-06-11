package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test all static methods in BooleanUtils class

 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 final Boolean expectedResult = Boolean.FALSE;
 final Boolean result = BooleanUtils.negate(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.isTrue(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsNotTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = false;
 final boolean result = BooleanUtils.isNotTrue(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.isFalse(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIsNotFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean expectedResult = false;
 final boolean result = BooleanUtils.isNotFalse(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBoolean() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithInt() {
 final int intValue = 1;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(intValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanObjectWithInt() {
 final int intValue = 1;
 final Boolean expectedResult = Boolean.TRUE;
 final Boolean result = BooleanUtils.toBooleanObject(intValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithInteger() {
 final Integer intValue = Integer.valueOf(1);
 final Boolean expectedResult = Boolean.TRUE;
 final Boolean result = BooleanUtils.toBooleanObject(intValue);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithIntAndValues() {
 final int intValue = 1;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(intValue, 1, 0);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithIntegerAndValues() {
 final Integer intValue = Integer.valueOf(1);
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(intValue, Integer.valueOf(1), Integer.valueOf(0));
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanObjectWithIntAndValues() {
 final int intValue = 1;
 final Boolean expectedResult = Boolean.TRUE;
 final Boolean result = BooleanUtils.toBooleanObject(intValue, 1, 0, 0);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanObjectWithIntegerAndValues() {
 final Integer intValue = Integer.valueOf(1);
 final Boolean expectedResult = Boolean.TRUE;
 final Boolean result = BooleanUtils.toBooleanObject(intValue, Integer.valueOf(1), Integer.valueOf(0), null);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithBoolean() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(bool);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithBooleanAndValues() {
 final Boolean bool = Boolean.TRUE;
 final boolean expectedResult = true;
 final boolean result = BooleanUtils.toBoolean(bool, true, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testToBooleanWithBooleanAndValuesException() {
 final Boolean bool = Boolean.TRUE;final boolean expectedResult = true; final boolean result = BooleanUtils.toBoolean(bool, null, null, null); assertNull(result); }

 @Test public void testToBooleanObjectWithBoolean() { final Boolean bool = Boolean.TRUE; final Boolean expectedResult = Boolean.TRUE; final Boolean result = BooleanUtils.toBooleanObject(bool); assertEquals(expectedResult, result); }

 @Test public void testToBooleanObjectWithBooleanAndValues() { final Boolean bool = Boolean.TRUE; final Boolean expectedResult = Boolean.TRUE; final Boolean result = BooleanUtils.toBooleanObject(bool, true, false); assertEquals(expectedResult, result); }

 @Test public void testToBooleanObjectWithBooleanAndValuesException() { final Boolean bool = Boolean.TRUE; final Boolean expectedResult = null; final Boolean result = BooleanUtils.toBooleanObject(bool, null, null, null); assertEquals(expectedResult, result); }

 @Test public void testToInteger() { final boolean bool = true; final int expectedResult = 1; final int result = BooleanUtils.toInteger(bool); assertEquals(expectedResult, result); }

 @Test public void testToIntegerObjectWithBoolean() { final boolean bool = true; final Integer expectedResult = Integer.valueOf(1); final Integer result = BooleanUtils.toIntegerObject(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalse() { final Boolean bool = Boolean.TRUE; final String expectedResult = "true"; final String result = BooleanUtils.toStringTrueFalse(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOff() { final Boolean bool = Boolean.TRUE; final String expectedResult = "on"; final String result = BooleanUtils.toStringOnOff(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNo() { final Boolean bool = Boolean.TRUE; final String expectedResult = "yes"; final String result = BooleanUtils.toStringYesNo(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalseWithBoolean() { final Boolean bool = Boolean.TRUE; final String expectedResult = "true"; final String result = BooleanUtils.toStringTrueFalse(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalseWithString() { final String str = "true"; final String expectedResult = "true"; final String result = BooleanUtils.toStringTrueFalse(str); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOffWithBoolean() { final Boolean bool = Boolean.TRUE; final String expectedResult = "on"; final String result = BooleanUtils.toStringOnOff(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOffWithString() { final String str = "on"; final String expectedResult = "on"; final String result = BooleanUtils.toStringOnOff(str); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNoWithBoolean() { final Boolean bool = Boolean.TRUE; final String expectedResult = "yes"; final String result = BooleanUtils.toStringYesNo(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNoWithString() { final String str = "yes"; final String expectedResult = "yes"; final String result = BooleanUtils.toStringYesNo(str); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalseWithNull() { final Boolean bool = null; final String expectedResult = null; final String result = BooleanUtils.toStringTrueFalse(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOffWithNull() { final Boolean bool = null; final String expectedResult = null; final String result = BooleanUtils.toStringOnOff(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNoWithNull() { final Boolean bool = null; final String expectedResult = null; final String result = BooleanUtils.toStringYesNo(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalseWithNullString() { final Boolean bool = Boolean.TRUE; final String expectedResult = null; final String result = BooleanUtils.toStringTrueFalse(bool, null, null, null); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOffWithNullString() { final Boolean bool = Boolean.TRUE; final String expectedResult = null; final String result = BooleanUtils.toStringOnOff(bool, null, null, null); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNoWithNullString() { final Boolean bool = Boolean.TRUE; final String expectedResult = null; final String result = BooleanUtils.toStringYesNo(bool, null, null, null); assertEquals(expectedResult, result); }

 @Test public void testToStringTrueFalseWithInvalidBoolean() { final Boolean bool = new Boolean("invalid"); final String expectedResult = null; final String result = BooleanUtils.toStringTrueFalse(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringOnOffWithInvalidBoolean() { final Boolean bool = new Boolean("invalid"); final String expectedResult = null; final String result = BooleanUtils.toStringOnOff(bool); assertEquals(expectedResult, result); }

 @Test public void testToStringYesNoWithInvalidBoolean() { final Boolean bool = new Boolean("invalid"); final String expectedResult = null; final String result = BooleanUtils.toStringYesNo(bool); assertEquals(expectedResult, result); }

 @Test public void testAndWithBooleanArrays() { final boolean[] booleanArray = { true, true, true, true }; final boolean expectedResult = true; final boolean result = BooleanUtils.and(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testAndWithBooleanArraysException() { final boolean[] booleanArray = { true, true, null, true }; final boolean expectedResult = false; final boolean result = BooleanUtils.and(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testAndWithBooleanArraysException2() { final boolean[] booleanArray = { null, true, true, true }; final boolean expectedResult = false; final boolean result = BooleanUtils.and(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testOrWithBooleanArrays() { final boolean[] booleanArray = { false, false, false, false }; final boolean expectedResult = false; final boolean result = BooleanUtils.or(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testOrWithBooleanArraysException1() { final boolean[] booleanArray = { false, true, false, true }; final boolean expectedResult = true; final boolean result = BooleanUtils.or(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testOrWithBooleanArraysException2() { final boolean[] booleanArray = { true, false, null, false }; final boolean expectedResult = false; final boolean result = BooleanUtils.or(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testXorWithBooleanArrays() { final boolean[] booleanArray = { true, true, true, true }; final boolean expectedResult = false; final boolean result = BooleanUtils.xor(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testXorWithBooleanArraysException1() { final boolean[] booleanArray = { true, true, true, null }; final boolean expectedResult = false; final boolean result = BooleanUtils.xor(booleanArray); assertEquals(expectedResult, result); }

 @Test public void testXorWithBooleanArraysException2() { final boolean[] booleanArray = { null, true, true, true }; final boolean expectedResult = false; final boolean result = BooleanUtils.xor(booleanArray); assertEquals(expectedResult, result); }

}