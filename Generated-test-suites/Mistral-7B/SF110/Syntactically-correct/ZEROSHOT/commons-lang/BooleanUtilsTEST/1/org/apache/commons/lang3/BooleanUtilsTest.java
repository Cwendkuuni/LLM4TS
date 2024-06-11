package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.negate(bool) == Boolean.FALSE);
 bool = null;
 assertNull(BooleanUtils.negate(bool));
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));
 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));
 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));
 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));
 bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 Integer intValue = 1;
 assertTrue(BooleanUtils.toBoolean(intValue));
 intValue = 0;
 assertFalse(BooleanUtils.toBoolean(intValue));
 intValue = null;
 assertFalse(BooleanUtils.toBoolean(intValue));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Integer intValue = 1;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(intValue, false));
 intValue = null;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(intValue, false));
 intValue = 0;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(intValue, false));
 }

 @Test
 public void testToBooleanWithValue() {
 int value = 1;
 assertTrue(BooleanUtils.toBoolean(value, 1, 0));
 value = 0;
 assertFalse(BooleanUtils.toBoolean(value, 1, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithInvalidValue() {
 int value = 2;
 BooleanUtils.toBoolean(value, 1, 0);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test
 public void testToBooleanObject() {
 Integer intValue = 1;
 assertTrue(BooleanUtils.toBooleanObject(intValue).equals(Boolean.TRUE));
 intValue = 0;
 assertTrue(BooleanUtils.toBooleanObject(intValue).equals(Boolean.FALSE));
 intValue = null;
 assertNull(BooleanUtils.toBooleanObject(intValue));
 }

 @Test
 public void testToBooleanObjectWithValue() {
 int value = 1;
 assertTrue(BooleanUtils.toBooleanObject(value, Boolean.TRUE, Boolean.FALSE, null).equals(Boolean.TRUE));
 value = 0;
 assertTrue(BooleanUtils.toBooleanObject(value, Boolean.TRUE, Boolean.FALSE, null).equals(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithInvalidValue() {
 Integer intValue = 2;
 BooleanUtils.toBooleanObject(intValue, Boolean.TRUE, Boolean.FALSE, null);
 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test
 public void testToBooleanObjectWithNullValue() {
 Integer intValue = null;
 assertNull(BooleanUtils.toBooleanObject(intValue, Boolean.TRUE, Boolean.FALSE, null));
 }

 @Test
 public void testToInteger() {
 boolean bool = true;
 assertEquals(1, BooleanUtils.toInteger(bool));
 bool = false;
 assertEquals(0, BooleanUtils.toInteger(bool));
 }

 @Test
 public void testToStringTrueFalse() {
 Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool));
 bool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(bool));
 }

 @Test
 public void testToStringOnOff() {
 Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool));
 bool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(bool));
 }

 @Test
 public void testToStringYesNo() {
 Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool));
 bool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(bool));
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullBoolean() {
 Boolean bool = null;
 BooleanUtils.toStringTrueFalse(bool);
 fail("Expected a NullPointerException to be thrown");
 }

 @Test
 public void testAnd() {
 boolean[] array = {true, true, true};
 assertTrue(BooleanUtils.and(array));
 array[1] = false;
 assertFalse(BooleanUtils.and(array));
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(booleanArray));
 booleanArray[1] = Boolean.FALSE;
 assertFalse(BooleanUtils.and(booleanArray));
 }

 @Test(expected = NullPointerException.class)
 public void testAndWithNullBooleanArray() {
 Boolean[] booleanArray = null;
 BooleanUtils.and(booleanArray);
 fail("Expected a NullPointerException to be thrown");
 }

 @Test
 public void testOr() {
 boolean[] array = {false, false, false};
 assertFalse(BooleanUtils.or(array));
 array[1] = true;
 assertTrue(BooleanUtils.or(array));
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 assertFalse(BooleanUtils.or(booleanArray));
 booleanArray[1] = Boolean.TRUE;
 assertTrue(BooleanUtils.or(booleanArray));
 }

 @Test(expected = NullPointerException.class)
 public void testOrWithNullBooleanArray() {
 Boolean[] booleanArray = null;
 BooleanUtils.or(booleanArray);
 fail("Expected a NullPointerException to be thrown");
 }

 @Test
 public void testXor() {
 boolean[] array = {true, false, true};
 boolean result = BooleanUtils.xor(array);
 assertEquals(true, result);
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 Boolean result = BooleanUtils.xor(booleanArray);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = NullPointerException.class)
 public void testXorWithNullBooleanArray() {
 Boolean[] booleanArray = null;
 BooleanUtils.xor(booleanArray);
 fail("Expected a NullPointerException to be thrown");
 }
}