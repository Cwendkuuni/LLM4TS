package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean expected = null;
 Boolean actual = BooleanUtils.negate(null);
 assertEquals(expected, actual);

 expected = Boolean.FALSE;
 actual = BooleanUtils.negate(Boolean.FALSE);
 assertEquals(Boolean.TRUE, actual);

 expected = Boolean.TRUE;
 actual = BooleanUtils.negate(Boolean.TRUE);
 assertEquals(Boolean.FALSE, actual);
 }

 @Test
 public void testIsTrue() {
 Boolean actual = BooleanUtils.isTrue(null);
 assertFalse(actual);

 actual = BooleanUtils.isTrue(Boolean.FALSE);
 assertFalse(actual);

 actual = BooleanUtils.isTrue(Boolean.TRUE);
 assertTrue(actual);
 }

 @Test
 public void testIsNotTrue() {
 Boolean actual = BooleanUtils.isNotTrue(null);
 assertTrue(actual);

 actual = BooleanUtils.isNotTrue(Boolean.FALSE);
 assertFalse(actual);

 actual = BooleanUtils.isNotTrue(Boolean.TRUE);
 assertFalse(actual);
 }

 @Test
 public void testIsFalse() {
 Boolean actual = BooleanUtils.isFalse(null);
 assertTrue(actual);

 actual = BooleanUtils.isFalse(Boolean.FALSE);
 assertTrue(actual);

 actual = BooleanUtils.isFalse(Boolean.TRUE);
 assertFalse(actual);
 }

 @Test
 public void testIsNotFalse() {
 Boolean actual = BooleanUtils.isNotFalse(null);
 assertFalse(actual);

 actual = BooleanUtils.isNotFalse(Boolean.FALSE);
 assertFalse(actual);

 actual = BooleanUtils.isNotFalse(Boolean.TRUE);
 assertTrue(actual);
 }

 @Test
 public void testToBoolean() {
 Integer input = 1;
 Boolean expected = true;
 Boolean actual = BooleanUtils.toBoolean(input);
 assertEquals(expected, actual);

 input = 0;
 expected = false;
 actual = BooleanUtils.toBoolean(input);
 assertEquals(expected, actual);

 input = -1;
 expected = null;
 actual = BooleanUtils.toBoolean(input);
 assertEquals(expected, actual);

 try {
 BooleanUtils.toBoolean(2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer must be null or not zero", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Integer input = 1;
 Boolean expected = true;
 Boolean actual = BooleanUtils.toBooleanDefaultIfNull(input, false);
 assertEquals(expected, actual);

 input = null;
 Boolean defaultValue = false;
 Boolean actual = BooleanUtils.toBooleanDefaultIfNull(input, defaultValue);
 assertEquals(defaultValue, actual);

 input = 0;
 expected = false;
 actual = BooleanUtils.toBooleanDefaultIfNull(input, defaultValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObject() {
 Integer input = 1;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(input);
 assertEquals(expected, actual);

 input = 0;
 expected = Boolean.FALSE;
 actual = BooleanUtils.toBooleanObject(input);
 assertEquals(expected, actual);

 input = null;
 assertNull(BooleanUtils.toBooleanObject(input));
 }

 @Test
 public void testToBooleanObjectWithTrueValue() {
 Integer input = 1;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 assertEquals(expected, actual);

 input = Integer.valueOf(1);
 expected = Boolean.TRUE;
 actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectWithFalseValue() {
 Integer input = 0;
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 assertEquals(expected, actual);

 input = Integer.valueOf(0);
 expected = Boolean.FALSE;
 actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectWithNullValue() {
 Integer input = null;
 Boolean expected = null;
 Boolean actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIllegalArgument() {
 Integer input = 2;
 Boolean expected = null;
 Boolean actual = BooleanUtils.toBooleanObject(input, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testToInteger() {
 Boolean input = true;
 int expected = 1;
 int actual = BooleanUtils.toInteger(input);
 assertEquals(expected, actual);

 input = false;
 expected = 0;
 actual = BooleanUtils.toInteger(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 Boolean input = true;
 int expected = 1;
 int actual = BooleanUtils.toIntegerObject(input);
 assertEquals(expected, actual);

 input = false;
 expected = 0;
 actual = BooleanUtils.toIntegerObject(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 Boolean input = Boolean.TRUE;
 Integer expected = Integer.valueOf(1);
 Integer actual = BooleanUtils.toIntegerObject(input);
 assertEquals(expected, actual);

 input = Boolean.FALSE;
 actual = BooleanUtils.toIntegerObject(input);
 assertNull(actual);

 input = null;
 actual = BooleanUtils.toIntegerObject(input);
 assertNull(actual);
 }

 @Test
 public void testToIntegerWithValue() {
 Boolean input = true;
 int expected = 1;
 int actual = BooleanUtils.toInteger(input, 1, 0);
 assertEquals(expected, actual);

 input = false;
 expected = 0;
 actual = BooleanUtils.toInteger(input, 1, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerWithIllegalArgument() {
 Boolean input = true;
 int expected = 0;
 int actual = BooleanUtils.toInteger(input, 2, 0);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testToStringTrueFalse() {
 Boolean input = Boolean.TRUE;
 String expected = "true";
 String actual = BooleanUtils.toStringTrueFalse(input);
 assertEquals(expected, actual);

 input = Boolean.FALSE;
 expected = "false";
 actual = BooleanUtils.toStringTrueFalse(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringOnOff() {
 Boolean input = Boolean.TRUE;
 String expected = "on";
 String actual = BooleanUtils.toStringOnOff(input);
 assertEquals(expected, actual);

 input = Boolean.FALSE;
 expected = "off";
 actual = BooleanUtils.toStringOnOff(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringYesNo() {
 Boolean input = Boolean.TRUE;
 String expected = "yes";
 String actual = BooleanUtils.toStringYesNo(input);
 assertEquals(expected, actual);

 input = Boolean.FALSE;
 expected = "no";
 actual = BooleanUtils.toStringYesNo(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithTrueValue() {
 Boolean input = Boolean.TRUE;
 String expected = "true";
 String actual = BooleanUtils.toString(input, "true", "false");
 assertEquals(expected, actual);

 input = Boolean.TRUE;
 expected = "TRUE";
 actual = BooleanUtils.toString(input, "True", "False");
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithFalseValue() {
 Boolean input = Boolean.FALSE;
 String expected = "false";
 String actual = BooleanUtils.toString(input, "true", "false");
 assertEquals(expected, actual);

 input = Boolean.FALSE;
 expected = "FALSE";
 actual = BooleanUtils.toString(input, "True", "False");
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithNullValue() {
 Boolean input = null;
 String expected = null;
 String actual = BooleanUtils.toString(input, "true", "false");
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringWithNullStringValue() {
 Boolean input = Boolean.TRUE;
 String expected = null;
 String actual = BooleanUtils.toString(input, null, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testAnd() {
 boolean[] input = new boolean[]{true, true};
 boolean expected = true;
 boolean actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new boolean[]{true, false};
 expected = false;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, false};
 expected = false;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, true};
 expected = false;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 try {
 boolean[] input2 = null;
 boolean actual2 = BooleanUtils.and(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] input = new Boolean[]{Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.FALSE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.and(input);
 assertEquals(expected, actual);

 try {
 Boolean[] input2 = null;
 Boolean actual2 = BooleanUtils.and(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testOr() {
 boolean[] input = new boolean[]{true, true};
 boolean expected = true;
 boolean actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new boolean[]{true, false};
 expected = true;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, false};
 expected = false;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, true};
 expected = true;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 try {
 boolean[] input2 = null;
 boolean actual2 = BooleanUtils.or(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] input = new Boolean[]{Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.FALSE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.or(input);
 assertEquals(expected, actual);

 try {
 Boolean[] input2 = null;
 Boolean actual2 = BooleanUtils.or(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testXor() {
 boolean[] input = new boolean[]{true, true};
 boolean expected = false;
 boolean actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new boolean[]{true, false};
 expected = true;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, true};
 expected = true;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new boolean[]{false, false};
 expected = false;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 try {
 boolean[] input2 = null;
 boolean actual2 = BooleanUtils.xor(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] input = new Boolean[]{Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.FALSE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 input = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.xor(input);
 assertEquals(expected, actual);

 try {
 Boolean[] input2 = null;
 Boolean actual2 = BooleanUtils.xor(input2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }
}