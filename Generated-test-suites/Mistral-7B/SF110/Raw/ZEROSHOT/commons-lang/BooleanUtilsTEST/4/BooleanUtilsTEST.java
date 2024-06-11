package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 final Boolean nullBool = null;
 assertNull(BooleanUtils.negate(nullBool));
 }

 @Test
 public void testIsTrue() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(falseBool));
 final Boolean nullBool = null;
 assertFalse(BooleanUtils.isTrue(nullBool));
 }

 @Test
 public void testIsNotTrue() {
 final Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(falseBool));
 final Boolean nullBool = null;
 assertTrue(BooleanUtils.isNotTrue(nullBool));
 }

 @Test
 public void testIsFalse() {
 final Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(falseBool));
 final Boolean nullBool = null;
 assertFalse(BooleanUtils.isFalse(nullBool));
 }

 @Test
 public void testIsNotFalse() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(falseBool));
 final Boolean nullBool = null;
 assertFalse(BooleanUtils.isNotFalse(nullBool));
 }

 @Test
 public void testToBoolean() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(falseBool));
 final Integer intBool = Integer.valueOf(1);
 assertTrue(BooleanUtils.toBoolean(intBool));
 final Integer intFalse = Integer.valueOf(0);
 assertFalse(BooleanUtils.toBoolean(intFalse));
 final Integer nullInt = null;
 assertFalse(BooleanUtils.toBoolean(nullInt));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 final Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(falseBool, true));
 final Boolean nullBool = null;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(nullBool, true));
 }

 @Test
 public void testToBooleanWithValue() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool.booleanValue(), 1, 0));
 final Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(falseBool.booleanValue(), 1, 0));
 }

 @Test
 public void testToBooleanObject() {
 final Boolean bool = Boolean.TRUE;
 assertEquals(bool, BooleanUtils.toBooleanObject(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals(falseBool, BooleanUtils.toBooleanObject(falseBool));
 final Boolean nullBool = null;
 assertNull(BooleanUtils.toBooleanObject(nullBool));
 final Integer intBool = Integer.valueOf(1);
 assertNotEquals(intBool, BooleanUtils.toBooleanObject(intBool));
 final Integer intFalse = Integer.valueOf(0);
 assertNotEquals(intFalse, BooleanUtils.toBooleanObject(intFalse));
 }

 @Test
 public void testToBooleanObjectWithValue() {
 final Boolean bool = Boolean.TRUE;
 assertEquals(bool, BooleanUtils.toBooleanObject(bool.booleanValue(), Boolean.TRUE, Boolean.FALSE, null));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals(falseBool, BooleanUtils.toBooleanObject(falseBool.booleanValue(), Boolean.TRUE, Boolean.FALSE, null));
 final Boolean nullBool = null;
 assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
 final Integer intBool = Integer.valueOf(1);
 assertNotEquals(intBool, BooleanUtils.toBooleanObject(intBool.intValue(), Boolean.TRUE, Boolean.FALSE, null));
 }

 @Test
 public void testToInteger() {
 final Boolean bool = Boolean.TRUE;
 assertEquals(1, BooleanUtils.toInteger(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals(0, BooleanUtils.toInteger(falseBool));
 final Integer intBool = Integer.valueOf(1);
 assertEquals(1, BooleanUtils.toInteger(intBool));
 final Integer intFalse = Integer.valueOf(0);
 assertEquals(0, BooleanUtils.toInteger(intFalse));
 }

 @Test
 public void testToStringTrueFalse() {
 final Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toStringTrueFalse(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toStringTrueFalse(falseBool));
 final Boolean nullBool = null;
 assertEquals(null, BooleanUtils.toStringTrueFalse(nullBool));
 }

 @Test
 public void testToStringOnOff() {
 final Boolean bool = Boolean.TRUE;
 assertEquals("on", BooleanUtils.toStringOnOff(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals("off", BooleanUtils.toStringOnOff(falseBool));
 final Boolean nullBool = null;
 assertEquals(null, BooleanUtils.toStringOnOff(nullBool));
 }

 @Test
 public void testToStringYesNo() {
 final Boolean bool = Boolean.TRUE;
 assertEquals("yes", BooleanUtils.toStringYesNo(bool));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals("no", BooleanUtils.toStringYesNo(falseBool));
 final Boolean nullBool = null;
 assertEquals(null, BooleanUtils.toStringYesNo(nullBool));
 }

 @Test
 public void testToStringWithValues() {
 final Boolean bool = Boolean.TRUE;
 assertEquals("true", BooleanUtils.toString(bool, "true", "false"));
 final Boolean falseBool = Boolean.FALSE;
 assertEquals("false", BooleanUtils.toString(falseBool, "true", "false"));
 final Boolean nullBool = null;
 assertEquals(null, BooleanUtils.toString(nullBool, "true", "false"));
 }

 @Test
 public void testAnd() {
 final boolean[] array = { true, true, true };
 assertTrue(BooleanUtils.and(array));
 final boolean[] arrayFalse = { false, false, false };
 assertFalse(BooleanUtils.and(arrayFalse));
 final boolean[] arrayNull = null;
 assertFalse(BooleanUtils.and(arrayNull));
 final boolean[] arrayEmpty = {};
 assertFalse(BooleanUtils.and(arrayEmpty));
 }

 @Test
 public void testAndWithBooleanArray() {
 final Boolean[] booleanArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertEquals(Boolean.TRUE, BooleanUtils.and(booleanArray));
 final Boolean[] booleanArrayFalse = { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
 assertEquals(Boolean.FALSE, BooleanUtils.and(booleanArrayFalse));
 final Boolean[] booleanArrayNull = null;
 assertEquals(Boolean.FALSE, BooleanUtils.and(booleanArrayNull));
 final Boolean[] booleanArrayEmpty = {};
 assertEquals(Boolean.FALSE, BooleanUtils.and(booleanArrayEmpty));
 }

 @Test
 public void testOr() {
 final boolean[] array = { true, true, true };
 assertTrue(BooleanUtils.or(array));
 final boolean[] arrayFalse = { false, false, false };
 assertFalse(BooleanUtils.or(arrayFalse));
 final boolean[] arrayNull = null;
 assertFalse(BooleanUtils.or(arrayNull));
 final boolean[] arrayEmpty = {};
 assertFalse(BooleanUtils.or(arrayEmpty));
 }

 @Test
 public void testOrWithBooleanArray() {
 final Boolean[] booleanArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertEquals(Boolean.TRUE, BooleanUtils.or(booleanArray));
 final Boolean[] booleanArrayFalse = { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
 assertEquals(Boolean.FALSE, BooleanUtils.or(booleanArrayFalse));
 final Boolean[] booleanArrayNull = null;
 assertEquals(Boolean.FALSE, BooleanUtils.or(booleanArrayNull));
 final Boolean[] booleanArrayEmpty = {};
 assertEquals(Boolean.FALSE, BooleanUtils.or(booleanArrayEmpty));
 }

 @Test
 public void testXor() {
 final boolean[] array = { true, true, true };
 assertFalse(BooleanUtils.xor(array));
 final boolean[] arrayFalse = { false, false, false };
 assertFalse(BooleanUtils.xor(arrayFalse));
 final boolean[] arrayNull = null;
 assertFalse(BooleanUtils.xor(arrayNull));
 final boolean[] arrayEmpty = {};
 assertFalse(BooleanUtils.xor(arrayEmpty));
 final boolean[] arrayOne = { true, false, true };
 assertTrue(BooleanUtils.xor(arrayOne));
 }

 @Test
 public void testXorWithBooleanArray() {
 final Boolean[] booleanArray = { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
 assertEquals(Boolean.TRUE, BooleanUtils.xor(booleanArray));
 final Boolean[] booleanArrayFalse = { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE };
 assertEquals(Boolean.TRUE, BooleanUtils.xor(booleanArrayFalse));
 final Boolean[] booleanArrayNull = null;
 assertEquals(Boolean.FALSE, BooleanUtils.xor(booleanArrayNull));
 final Boolean[] booleanArrayEmpty = {};
 assertEquals(Boolean.FALSE, BooleanUtils.xor(booleanArrayEmpty));
 }
}