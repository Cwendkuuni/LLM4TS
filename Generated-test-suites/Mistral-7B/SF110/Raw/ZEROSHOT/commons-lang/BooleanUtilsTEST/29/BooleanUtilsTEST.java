package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertNull(BooleanUtils.negate(null));
 assertFalse(BooleanUtils.negate(bool));
 assertTrue(BooleanUtils.negate(Boolean.FALSE));
 }

 @Test
 public void testIsTrue() {
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isTrue(null));
 }

 @Test
 public void testIsNotTrue() {
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotTrue(null));
 }

 @Test
 public void testIsFalse() {
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(null));
 }

 @Test
 public void testIsNotFalse() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotFalse(null));
 }

 @Test
 public void testToBoolean() {
 assertFalse(BooleanUtils.toBoolean(false));
 assertFalse(BooleanUtils.toBoolean(null));
 assertTrue(BooleanUtils.toBoolean(true));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(false, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(true, false));
 }

 @Test
 public void testToBooleanWithInt() {
 assertFalse(BooleanUtils.toBoolean(0));
 assertFalse(BooleanUtils.toBoolean(null));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
 assertNull(BooleanUtils.toBooleanObject(null));
 assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
 }

 @Test
 public void testToBooleanWithIntTrueValueFalseValue() {
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanWithIntTrueValueFalseValueNullValue() {
 Integer nullValue = Integer.valueOf(0);
 assertFalse(BooleanUtils.toBoolean(0, 1, 0, nullValue));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), nullValue));
 assertNull(BooleanUtils.toBoolean(null, nullValue, nullValue, nullValue));
 assertTrue(BooleanUtils.toBoolean(1, 1, 0, nullValue));
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), nullValue));
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(true));
 assertEquals(0, BooleanUtils.toInteger(false));
 }

 @Test
 public void testToStringTrueFalse() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test
 public void testToStringOnOff() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test
 public void testToStringYesNo() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test
 public void testToString() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 @Test
 public void testAnd() {
 boolean[] array = {true, true, true};
 assertTrue(BooleanUtils.and(array));
 array[1] = false;
 assertFalse(BooleanUtils.and(array));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAndWithNullArray() {
 BooleanUtils.and(null);
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(array));
 array[1] = Boolean.FALSE;
 assertFalse(BooleanUtils.and(array));
 }

 @Test
 public void testOr() {
 boolean[] array = {false, false, false};
 assertFalse(BooleanUtils.or(array));
 array[1] = true;
 assertTrue(BooleanUtils.or(array));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOrWithNullArray() {
 BooleanUtils.or(null);
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] array = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 assertFalse(BooleanUtils.or(array));
 array[1] = Boolean.TRUE;
 assertTrue(BooleanUtils.or(array));
 }

 @Test
 public void testXor() {
 boolean[] array = {false, true, false};
 assertFalse(BooleanUtils.xor(array));
 array[0] = true;
 assertTrue(BooleanUtils.xor(array));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testXorWithNullArray() {
 BooleanUtils.xor(null);
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] array = {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
 assertFalse(BooleanUtils.xor(array));
 array[0] = Boolean.TRUE;
 assertTrue(BooleanUtils.xor(array));
 }
}