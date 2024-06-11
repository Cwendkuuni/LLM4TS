package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

public class BooleanUtilsTest {

 // Test for negate method 
 @Test
 public void testNegate() {
 Boolean bool = Boolean.FALSE;
 Boolean negatedBool = BooleanUtils.negate(bool);
 assertTrue(negatedBool.equals(Boolean.TRUE));

 bool = Boolean.TRUE;
 negatedBool = BooleanUtils.negate(bool);
 assertTrue(negatedBool.equals(Boolean.FALSE));

 bool = null;
 assertEquals(null, BooleanUtils.negate(bool));
 }

 // Test for isTrue method 
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 // Test for isNotTrue method 
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));

 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 // Test for isFalse method 
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));

 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 // Test for isNotFalse method 
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));

 bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 // Test for toBoolean method with Boolean argument 
 @Test
 public void testToBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(bool));

 bool = null;
 assertFalse(BooleanUtils.toBoolean(bool));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument and default value 
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));

 bool = null;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 // Test for toBoolean method with int argument 
 @Test
 public void testToBooleanWithInt() {
 int intValue = 1;
 assertTrue(BooleanUtils.toBoolean(intValue));

 intValue = 0;
 assertFalse(BooleanUtils.toBoolean(intValue));
 }

 // Test for toBooleanObject method with int argument 
 @Test
 public void testToBooleanObjectWithInt() {
 int intValue = 1;
 assertTrue(BooleanUtils.toBooleanObject(intValue).equals(Boolean.TRUE));

 intValue = 0;
 assertTrue(BooleanUtils.toBooleanObject(intValue).equals(Boolean.FALSE));

 intValue = -1;
 assertNull(BooleanUtils.toBooleanObject(intValue));
 }

 // Test for toBoolean method with int argument and specified values 
 @Test
 public void testToBooleanWithIntSpecifiedValues() {
 int trueValue = 1;
 int falseValue = 0;
 int nullValue = -1;

 assertTrue(BooleanUtils.toBoolean(trueValue, trueValue, falseValue, nullValue));
 assertFalse(BooleanUtils.toBoolean(falseValue, trueValue, falseValue, nullValue));
 assertNull(BooleanUtils.toBoolean(nullValue, trueValue, falseValue, nullValue));
 }

 // Test for toBooleanObject method with int argument and specified values 
 @Test
 public void testToBooleanObjectWithIntSpecifiedValues() {
 int trueValue = 1;
 int falseValue = 0;
 int nullValue = -1;

 assertTrue(BooleanUtils.toBooleanObject(trueValue, trueValue, falseValue, nullValue).equals(Boolean.TRUE));
 assertFalse(BooleanUtils.toBooleanObject(falseValue, trueValue, falseValue, nullValue).equals(Boolean.FALSE));
 assertNull(BooleanUtils.toBooleanObject(nullValue, trueValue, falseValue, nullValue));
 }

 // Test for toInteger method 
 @Test
 public void testToInteger() {
 Boolean bool = Boolean.TRUE;
 assertEquals(1, BooleanUtils.toInteger(bool));

 bool = Boolean.FALSE;
 assertEquals(0, BooleanUtils.toInteger(bool));

 bool = null;
 assertNull(BooleanUtils.toInteger(bool));
 }

 // Test for toIntegerObject method with Boolean argument 
 @Test
 public void testToIntegerObjectWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(bool));

 bool = Boolean.FALSE;
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(bool));

 bool = null;
 assertNull(BooleanUtils.toIntegerObject(bool));
 }

 // Test for toIntegerObject method with int argument 
 @Test
 public void testToIntegerObjectWithInt() {
 int intValue = 1;
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(intValue));

 intValue = 0;
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(intValue));
 }

 // Test for toInteger method with Boolean argument and specified values 
 @Test
 public void testToIntegerWithBooleanSpecifiedValues() {
 int trueValue = 1;
 int falseValue = 0;

 assertEquals(trueValue, BooleanUtils.toInteger(Boolean.TRUE, trueValue, falseValue));
 assertEquals(falseValue, BooleanUtils.toInteger(Boolean.FALSE, trueValue, falseValue));
 }

 // Test for toInteger method with int argument and specified values 
 @Test
 public void testToIntegerWithIntSpecifiedValues() {
 int trueValue = 1;
 int falseValue = 0;

 assertEquals(trueValue, BooleanUtils.toInteger(trueValue, trueValue, falseValue));
 assertEquals(falseValue, BooleanUtils.toInteger(falseValue, trueValue, falseValue));
 }

 // Test for toBooleanObject method with String argument 
 @Test
 public void testToBooleanObjectWithString() {
 String str = "true";
 assertTrue(BooleanUtils.toBooleanObject(str).equals(Boolean.TRUE));

 str = "false";
 assertTrue(BooleanUtils.toBooleanObject(str).equals(Boolean.FALSE));

 str = null;
 assertNull(BooleanUtils.toBooleanObject(str));
 }

 // Test for toBooleanObject method with String argument and specified values 
 @Test
 public void testToBooleanObjectWithStringSpecifiedValues() {
 String trueString = "true";
 String falseString = "false";
 String nullString = null;

 assertTrue(BooleanUtils.toBooleanObject(trueString, trueString, nullString).equals(Boolean.TRUE));
 assertFalse(BooleanUtils.toBooleanObject(falseString, trueString, nullString).equals(Boolean.FALSE));
 assertNull(BooleanUtils.toBooleanObject(nullString, trueString, nullString));
 }

 // Test for toBoolean method with String argument 
 @Test
 public void testToBooleanWithString() {
 String str = "true";
 assertTrue(BooleanUtils.toBoolean(str));

 str = "false";
 assertFalse(BooleanUtils.toBoolean(str));

 str = null;
 assertFalse(BooleanUtils.toBoolean(str));
 }

 // Test for toBoolean method with String argument and specified values 
 @Test
 public void testToBooleanWithStringSpecifiedValues() {
 String trueString = "true";
 String falseString = "false";

 assertTrue(BooleanUtils.toBoolean(trueString, trueString, falseString));
 assertFalse(BooleanUtils.toBoolean(falseString, trueString, falseString));
 }

 // Test for toStringTrueFalse method 
 @Test
 public void testToStringTrueFalse() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool));

 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(bool));
 }

 // Test for toStringOnOff method 
 @Test
 public void testToStringOnOff() {
 Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool));

 bool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(bool));
 }

 // Test for toStringYesNo method 
 @Test
 public void testToStringYesNo() {
 Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool));

 bool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(bool));
 }

 // Test for toString method with Boolean argument 
 @Test
 public void testToStringWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toString(bool, "true", "false"));

 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toString(bool, "true", "false"));
 }

 // Test for and method with Boolean array 
 @Test
 public void testAndWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(booleanArray));

 booleanArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(booleanArray));

 booleanArray = new Boolean[] {null};
 throw new IllegalArgumentException("The Array must not contain any null elements");
 }

 // Test for and method with int array 
 @Test
 public void testAndWithIntArray() {
 int[] intArray = {1, 1, 1};
 assertTrue(BooleanUtils.and(intArray));

 intArray = new int[] {1, 0, 1};
 assertFalse(BooleanUtils.and(intArray));

 intArray = new int[] {0};
 throw new IllegalArgumentException("The Array must not be null");
 }

 // Test for or method with Boolean array 
 @Test
 public void testOrWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray = new Boolean[] {null};
 throw new IllegalArgumentException("The Array must not contain any null elements");
 }

 // Test for or method with int array 
 @Test
 public void testOrWithIntArray() {
 int[] intArray = {1, 1, 1};
 assertTrue(BooleanUtils.or(intArray));

 intArray = new int[] {1, 0, 1};
 assertTrue(BooleanUtils.or(intArray));

 intArray = new int[] {0};
 throw new IllegalArgumentException("The Array must not be null");
 }

 // Test for xor method with Boolean array 
 @Test
 public void testXorWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertEquals(Boolean.FALSE, BooleanUtils.xor(booleanArray));

 booleanArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertEquals(Boolean.TRUE, BooleanUtils.xor(booleanArray));

 booleanArray = new Boolean[] {null};
 throw new IllegalArgumentException("The Array must not contain any null elements");
 }

 // Test for xor method with int array 
 @Test
 public void testXorWithIntArray() {
 int[] intArray = {1, 1, 1};
 assertEquals(Boolean.FALSE, BooleanUtils.xor(intArray));

 intArray = new int[] {1, 0, 1};
 assertEquals(Boolean.TRUE, BooleanUtils.xor(intArray));

 intArray = new int[] {0};
 throw new IllegalArgumentException("The Array must not be null");
 }
}