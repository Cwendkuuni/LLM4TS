package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 bool = Boolean.FALSE;
 assertEquals(Boolean.TRUE, BooleanUtils.negate(bool));
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
 assertTrue(BooleanUtils.isNotFalse(bool));
 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotFalse(bool));
 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean((byte)0));
 assertTrue(BooleanUtils.toBoolean((byte)1));
 assertFalse(BooleanUtils.toBoolean((short)0));
 assertTrue(BooleanUtils.toBoolean((short)1));
 assertFalse(BooleanUtils.toBoolean((char)0));
 assertTrue(BooleanUtils.toBoolean((char)1));
 assertFalse(BooleanUtils.toBoolean(0));
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean((int)0));
 assertTrue(BooleanUtils.toBoolean((int)1));
 assertFalse(BooleanUtils.toBoolean((long)0));
 assertTrue(BooleanUtils.toBoolean((long)1));
 assertFalse(BooleanUtils.toBoolean((float)0.0));
 assertTrue(BooleanUtils.toBoolean((float)1.0));
 assertFalse(BooleanUtils.toBoolean((double)0.0));
 assertTrue(BooleanUtils.toBoolean((double)1.0));
 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 }

 @Test
 public void testToBooleanWithValue() {
 assertTrue(BooleanUtils.toBoolean(true, true, false));
 assertFalse(BooleanUtils.toBoolean(false, true, false));
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertFalse(BooleanUtils.toBoolean(1, 1, 0));
 throw new IllegalArgumentException("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testToBooleanObject() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithValue() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithValueAndDefault() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Boolean.FALSE));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Boolean.FALSE));
 assertNull(BooleanUtils.toBooleanObject(null, Boolean.FALSE));
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(true));
 assertEquals(0, BooleanUtils.toInteger(false));
 assertEquals(1, BooleanUtils.toInteger(1));
 assertEquals(0, BooleanUtils.toInteger(0));
 }

 @Test
 public void testToIntegerObject() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE).intValue());
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(1, BooleanUtils.toIntegerObject(true).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(false).intValue());
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 assertEquals(1, BooleanUtils.toIntegerObject(Boolean.TRUE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(Boolean.FALSE).intValue());
 assertEquals(0, BooleanUtils.toIntegerObject(null).intValue());
 }

 @Test
 public void testToIntegerWithValue() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 @Test
 public void testToIntegerWithValueAndDefault() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(1, BooleanUtils.toInteger(false, 0, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithInvalidValues() {
 BooleanUtils.toBoolean(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithInvalidValues() {
 BooleanUtils.toBooleanObject(null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithValueAndInvalidValues() {
 BooleanUtils.toBoolean(true, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithValueAndInvalidValues() {
 BooleanUtils.toBooleanObject(Integer.valueOf(1), null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithNullBoolean() {
 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithNullBoolean() {
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithValueAndNullValue() {
 assertFalse(BooleanUtils.toBoolean(true, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithValueAndNullValue() {
 assertNull(BooleanUtils.toBooleanObject(Boolean.TRUE, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithValueAndNullDefault() {
 assertFalse(BooleanUtils.toBoolean(true, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithValueAndNullDefault() {
 assertNull(BooleanUtils.toBooleanObject(Boolean.TRUE, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithValueAndInvalidDefault() {
 assertFalse(BooleanUtils.toBoolean(true, 1.5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithValueAndInvalidDefault() {
 assertNull(BooleanUtils.toBooleanObject(Boolean.TRUE, 1.5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerWithValueAndInvalidValues() {
 assertFalse(BooleanUtils.toBoolean(true, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerObjectWithValueAndInvalidValues() {
 assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), null, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerWithNullBoolean() {
 assertEquals(0, BooleanUtils.toInteger(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerObjectWithNullBoolean() {
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithValueAndNullValues() {
 assertFalse(BooleanUtils.toBoolean(true, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithValueAndNullValues() {
 assertNull(BooleanUtils.toBooleanObject(Boolean.TRUE, null, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerWithValueAndInvalidValues() {
 assertEquals(0, BooleanUtils.toInteger(true, 1.5, 0.5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToIntegerObjectWithValueAndInvalidValues() {
 assertNull(BooleanUtils.toIntegerObject(Integer.valueOf(1), 1.5, 0.5));
 }
}