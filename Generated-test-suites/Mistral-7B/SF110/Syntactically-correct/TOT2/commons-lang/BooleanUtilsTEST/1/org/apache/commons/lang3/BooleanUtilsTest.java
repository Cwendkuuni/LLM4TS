package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
  Boolean input = Boolean.TRUE;
  Boolean expectedOutput = Boolean.FALSE;
  Boolean output = BooleanUtils.negate(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.negate(input);
  assertEquals(expectedOutput, output);
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
  Boolean input = Boolean.TRUE;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.isTrue(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.isTrue(input);
  assertEquals(expectedOutput, output);
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
  Boolean input = Boolean.TRUE;
  boolean expectedOutput = false;
  boolean output = BooleanUtils.isNotTrue(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.isNotTrue(input);
  assertEquals(expectedOutput, output);
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
  Boolean input = Boolean.FALSE;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.isFalse(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.isFalse(input);
  assertEquals(expectedOutput, output);
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
  Boolean input = Boolean.FALSE;
  boolean expectedOutput = false;
  boolean output = BooleanUtils.isNotFalse(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = true;
  output = BooleanUtils.isNotFalse(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toBoolean method with Boolean input
 @Test
 public void testToBooleanWithBoolean() {
  Boolean input = Boolean.TRUE;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.toBoolean(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.toBoolean(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toBoolean method with integer input
 @Test
 public void testToBooleanWithInteger() {
  int input = 1;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.toBoolean(input);
  assertEquals(expectedOutput, output);

  input = 0;
  expectedOutput = false;
  output = BooleanUtils.toBoolean(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toBooleanDefaultIfNull method with Boolean input and default value as true
 @Test
 public void testToBooleanDefaultIfNullWithBooleanTrue() {
  Boolean input = Boolean.TRUE;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.toBooleanDefaultIfNull(input, true);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = true;
  output = BooleanUtils.toBooleanDefaultIfNull(input, true);
  assertEquals(expectedOutput, output);
 }

 // Test for toBooleanDefaultIfNull method with Boolean input and default value as false
 @Test
 public void testToBooleanDefaultIfNullWithBooleanFalse() {
  Boolean input = Boolean.FALSE;
  boolean expectedOutput = false;
  boolean output = BooleanUtils.toBooleanDefaultIfNull(input, false);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.toBooleanDefaultIfNull(input, false);
  assertEquals(expectedOutput, output);
 }

 // Test for toBoolean method with int input and trueValue and falseValue set
 @Test
 public void testToBooleanWithIntegerTrueFalse() {
  int input = 1;
  boolean expectedOutput = true;
  boolean output = BooleanUtils.toBoolean(input, 1, 0);
  assertEquals(expectedOutput, output);

  input = 0;
  expectedOutput = false;
  output = BooleanUtils.toBoolean(input, 1, 0);
  assertEquals(expectedOutput, output);

  try {
   input = 2;
   output = BooleanUtils.toBoolean(input, 1, 0);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Integer did not match either specified value", e.getMessage());
  }
 }

 // Test for toBoolean method with Integer input and trueValue and falseValue set
 @Test
 public void testToBooleanWithIntegerObjectTrueFalse() {
  Integer input = Integer.valueOf(1);
  boolean expectedOutput = true;
  boolean output = BooleanUtils.toBoolean(input, Integer.valueOf(1), Integer.valueOf(0));
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = false;
  output = BooleanUtils.toBoolean(input, Integer.valueOf(1), Integer.valueOf(0));
  assertEquals(expectedOutput, output);

  try {
   input = Integer.valueOf(2);
   output = BooleanUtils.toBoolean(input, Integer.valueOf(1), Integer.valueOf(0));
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Integer did not match either specified value", e.getMessage());
  }
 }

 // Test for toBooleanObject method with integer input and trueValue and falseValue set
 @Test
 public void testToBooleanObjectWithIntegerTrueFalse() {
  int input = 1;
  Boolean expectedOutput = Boolean.TRUE;
  Boolean output = BooleanUtils.toBooleanObject(input, 1, 0, 0);
  assertEquals(expectedOutput, output);

  input = 0;
  expectedOutput = Boolean.FALSE;
  output = BooleanUtils.toBooleanObject(input, 1, 0, 0);
  assertEquals(expectedOutput, output);

  try {
   input = 2;
   output = BooleanUtils.toBooleanObject(input, 1, 0, 0);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Integer did not match any specified value", e.getMessage());
  }
 }

 // Test for toBooleanObject method with Integer input and trueValue and falseValue set
 @Test
 public void testToBooleanObjectWithIntegerObjectTrueFalse() {
  Integer input = Integer.valueOf(1);
  Boolean expectedOutput = Boolean.TRUE;
  Boolean output = BooleanUtils.toBooleanObject(input, Integer.valueOf(1), Integer.valueOf(0), null);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.toBooleanObject(input, Integer.valueOf(1), Integer.valueOf(0), null);
  assertEquals(expectedOutput, output);

  try {
   input = Integer.valueOf(2);
   output = BooleanUtils.toBooleanObject(input, Integer.valueOf(1), Integer.valueOf(0), null);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Integer did not match any specified value", e.getMessage());
  }
 }

 // Test for toStringTrueFalse method
 @Test
 public void testToStringTrueFalse() {
  Boolean input = Boolean.TRUE;
  String expectedOutput = "true";
  String output = BooleanUtils.toStringTrueFalse(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.toStringTrueFalse(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toStringOnOff method
 @Test
 public void testToStringOnOff() {
  Boolean input = Boolean.TRUE;
  String expectedOutput = "on";
  String output = BooleanUtils.toStringOnOff(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.toStringOnOff(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toStringYesNo method
 @Test
 public void testToStringYesNo() {
  Boolean input = Boolean.TRUE;
  String expectedOutput = "yes";
  String output = BooleanUtils.toStringYesNo(input);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.toStringYesNo(input);
  assertEquals(expectedOutput, output);
 }

 // Test for toString method with Boolean input and trueString, falseString, nullString set
 @Test
 public void testToStringWithBooleanTrueFalseNullString() {
  Boolean input = Boolean.TRUE;
  String expectedOutput = "true";
  String output = BooleanUtils.toString(input, "true", "false", null);
  assertEquals(expectedOutput, output);

  input = Boolean.FALSE;
  expectedOutput = "false";
  output = BooleanUtils.toString(input, "true", "false", null);
  assertEquals(expectedOutput, output);

  input = null;
  expectedOutput = null;
  output = BooleanUtils.toString(input, "true", "false", null);
  assertEquals(expectedOutput, output);
 }

 // Test for and method with boolean array
 @Test
 public void testAndWithBooleanArray() {
  boolean[] input = new boolean[] { true, true, true };
  boolean expectedOutput = true;
  boolean output = BooleanUtils.and(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { false, true, true };
  expectedOutput = false;
  output = BooleanUtils.and(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { null };
  try {
   output = BooleanUtils.and(input);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }

 // Test for and method with Boolean array
 @Test
 public void testAndWithBooleanArrayBoolean() {
  Boolean[] input = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
  boolean expectedOutput = true;
  boolean output = BooleanUtils.and(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE };
  expectedOutput = false;
  output = BooleanUtils.and(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { null };
  try {
   output = BooleanUtils.and(input).booleanValue();
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }

 // Test for or method with boolean array
 @Test
 public void testOrWithBooleanArray() {
  boolean[] input = new boolean[] { true, true, true };
  boolean expectedOutput = true;
  boolean output = BooleanUtils.or(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { false, true, true };
  expectedOutput = true;
  output = BooleanUtils.or(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { null };
  try {
   output = BooleanUtils.or(input);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }

 // Test for or method with Boolean array
 @Test
 public void testOrWithBooleanArrayBoolean() {
  Boolean[] input = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
  boolean expectedOutput = true;
  boolean output = BooleanUtils.or(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE };
  expectedOutput = true;
  output = BooleanUtils.or(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { null };
  try {
   output = BooleanUtils.or(input).booleanValue();
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }

 // Test for xor method with boolean array
 @Test
 public void testXorWithBooleanArray() {
  boolean[] input = new boolean[] { true, true, true };
  boolean expectedOutput = false;
  boolean output = BooleanUtils.xor(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { false, true, true };
  expectedOutput = true;
  output = BooleanUtils.xor(input);
  assertEquals(expectedOutput, output);

  input = new boolean[] { null };
  try {
   output = BooleanUtils.xor(input);
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }

 // Test for xor method with Boolean array
 @Test
 public void testXorWithBooleanArrayBoolean() {
  Boolean[] input = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
  boolean expectedOutput = false;
  boolean output = BooleanUtils.xor(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.TRUE };
  expectedOutput = true;
  output = BooleanUtils.xor(input).booleanValue();
  assertEquals(expectedOutput, output);

  input = new Boolean[] { null };
  try {
   output = BooleanUtils.xor(input).booleanValue();
   fail("Expected IllegalArgumentException to be thrown");
  } catch (IllegalArgumentException e) {
   assertEquals("The Array must not be null", e.getMessage());
  }
 }
}