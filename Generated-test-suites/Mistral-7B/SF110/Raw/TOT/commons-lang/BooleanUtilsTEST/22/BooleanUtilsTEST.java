package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
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
 assertFalse(BooleanUtils.toBoolean(0));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 }

 @Test
 public void testToBoolean_int() {
 assertFalse(BooleanUtils.toBoolean(-1));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanObject_int() {
 assertFalse(BooleanUtils.toBooleanObject(-1));
 assertTrue(BooleanUtils.toBooleanObject(1));
 }

 @Test
 public void testToBoolean_int_int_int() {
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 }

 @Test
 public void testToBoolean_Integer_Integer_Integer() {
 Integer integer = null;
 assertFalse(BooleanUtils.toBoolean(integer, 1, 0));
 integer = Integer.valueOf(0);
 assertFalse(BooleanUtils.toBoolean(integer, 1, 0));
 integer = Integer.valueOf(1);
 assertTrue(BooleanUtils.toBoolean(integer, 1, 0));
 }

 @Test
 public void testToBooleanObject_int_int_int_int() {
 assertFalse(BooleanUtils.toBooleanObject(0, 1, 0, -1));
 assertTrue(BooleanUtils.toBooleanObject(1, 1, 0, -1));
 assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
 }

 @Test
 public void testToBooleanObject_Integer_Integer_Integer_Integer() {
 assertFalse(BooleanUtils.toBooleanObject(null, 1, 0, null));
 assertTrue(BooleanUtils.toBooleanObject(Integer.valueOf(1), 1, 0, null));
 assertNull(BooleanUtils.toBooleanObject(null, 1, 0, Integer.valueOf(1)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBoolean_int_int_int_int_NullPointerException() {
 BooleanUtils.toBoolean(null, 1, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObject_Integer_Integer_Integer_Integer_NullPointerException() {
 BooleanUtils.toBooleanObject(null, 1, 0, null);
 }

 @Test
 public void testToInteger() {
 assertEquals(0, BooleanUtils.toInteger(false));
 assertEquals(1, BooleanUtils.toInteger(true));
 }

 @Test
 public void testToIntegerObject_boolean() {
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false));
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true));
 }

 @Test
 public void testToIntegerObject_Boolean() {
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 }

 @Test
 public void testToInteger_boolean() {
 assertEquals(0, BooleanUtils.toInteger(false, 0, 1));
 assertEquals(1, BooleanUtils.toInteger(true, 0, 1));
 }

 @Test
 public void testToInteger_Boolean_int_int() {
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 0, 1));
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 0, 1));
 }

 @Test
 public void testToIntegerObject_boolean_Integer_Integer() {
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false, null, null));
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, null, null));
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, Integer.valueOf(1), null));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false, null, Integer.valueOf(1)));
 }

 @Test
 public void testToIntegerObject_Boolean_Integer_Integer_Integer() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, null, null, null));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false, null, null, null));
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, null, Integer.valueOf(1), null));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false, null, null, Integer.valueOf(1)));
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
 assertFalse(BooleanUtils.and(new boolean[] { false, true, false }));
 assertTrue(BooleanUtils.and(new boolean[] { true, true, true }));
 }

 @Test
 public void testAnd_Boolean() {
 assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
 assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
 }

 @Test
 public void testOr() {
 assertTrue(BooleanUtils.or(new boolean[] { true, false, true }));
 assertFalse(BooleanUtils.or(new boolean[] { false, false, false }));
 }

 @Test
 public void testOr_Boolean() {
 assertFalse(BooleanUtils.or(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
 assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
 }

 @Test
 public void testXor() {
 assertEquals(false, BooleanUtils.xor(new boolean[] { true, true, false }));
 assertEquals(true, BooleanUtils.xor(new boolean[] { true, false, true }));
 }

 @Test
 public void testXor_Boolean() {
 assertEquals(Boolean.FALSE, BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }));
 assertEquals(Boolean.TRUE, BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }));
 }
}