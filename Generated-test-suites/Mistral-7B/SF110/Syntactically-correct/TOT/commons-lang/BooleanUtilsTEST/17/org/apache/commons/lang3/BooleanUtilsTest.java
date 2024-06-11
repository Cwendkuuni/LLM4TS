package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean result = BooleanUtils.negate(bool);
 assertFalse(result.equals(bool));
 bool = Boolean.FALSE;
 result = BooleanUtils.negate(bool);
 assertTrue(result.equals(bool));
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));
 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 }

 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 }

 @Test
 public void testToBooleanWithIntegerAndDefaults() {
 Integer intValue = 1;
 Boolean boolValue = BooleanUtils.toBoolean(intValue, Boolean.TRUE, Boolean.FALSE);
 assertEquals(Boolean.TRUE, boolValue);
 intValue = 0;
 boolValue = BooleanUtils.toBoolean(intValue, Boolean.TRUE, Boolean.FALSE);
 assertEquals(Boolean.FALSE, boolValue);
 intValue = null;
 boolValue = BooleanUtils.toBoolean(intValue, Boolean.TRUE, Boolean.FALSE);
 assertNull(boolValue);
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE).intValue());
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE).intValue());
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerWithBoolean() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerWithBooleanAndDefaults() {
 Boolean bool = Boolean.TRUE;
 int result = BooleanUtils.toInteger(bool, 1, 0);
 assertEquals(1, result);
 bool = Boolean.FALSE;
 result = BooleanUtils.toInteger(bool, 1, 0);
 assertEquals(0, result);
 bool = null;
 result = BooleanUtils.toInteger(bool, 1, 0);
 assertEquals(1, result); // default to 1
 }

 @Test
 public void testToBooleanObjectWithString() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithStringAndDefaults() {
 String str = "true";
 Boolean boolValue = BooleanUtils.toBooleanObject(str, Boolean.TRUE, Boolean.FALSE, null);
 assertEquals(Boolean.TRUE, boolValue);
 str = "false";
 boolValue = BooleanUtils.toBooleanObject(str, Boolean.TRUE, Boolean.FALSE, null);
 assertEquals(Boolean.FALSE, boolValue);
 str = null;
 boolValue = BooleanUtils.toBooleanObject(str, Boolean.TRUE, Boolean.FALSE, null);
 assertNull(boolValue);
 }

 @Test
 public void testToBooleanWithString() {
 assertTrue(BooleanUtils.toBoolean("true"));
 assertFalse(BooleanUtils.toBoolean("false"));
 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanWithStringAndDefaults() {
 String str = "true";
 Boolean boolValue = BooleanUtils.toBoolean(str, false, true);
 assertEquals(Boolean.TRUE, boolValue);
 str = "false";
 boolValue = BooleanUtils.toBoolean(str, false, true);
 assertEquals(Boolean.FALSE, boolValue);
 str = null;
 boolValue = BooleanUtils.toBoolean(str, false, true);
 assertEquals(false, boolValue); // default to false
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithIntOutOfRange() {
 Integer intValue = Integer.MIN_VALUE;
 try {
 BooleanUtils.toBoolean(intValue, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 intValue = Integer.MAX_VALUE;
 try {
 BooleanUtils.toBoolean(intValue, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntOutOfRange() {
 Integer intValue = Integer.MIN_VALUE;
 try {
 BooleanUtils.toBooleanObject(intValue, 1, 0, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 intValue = Integer.MAX_VALUE;
 try {
 BooleanUtils.toBooleanObject(intValue, 1, 0, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

}