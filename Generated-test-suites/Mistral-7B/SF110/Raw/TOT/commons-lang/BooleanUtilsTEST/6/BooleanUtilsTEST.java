package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 assertEquals(null, BooleanUtils.negate(null));
 }

 @Test
 public void testIsTrue() {
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isTrue(null));
 }

 @Test
 public void testIsNotTrue() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotFalse(null));
 }

 @Test
 public void testIsFalse() {
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
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
 assertTrue(BooleanUtils.toBoolean(true));
 assertFalse(BooleanUtils.toBoolean(false));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanWithInteger() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanWithIntAndBoolean() {
 assertTrue(BooleanUtils.toBoolean(1, true, false));
 assertFalse(BooleanUtils.toBoolean(0, true, false));
 assertFalse(BooleanUtils.toBoolean(null, true, false));
 }

 @Test
 public void testToBooleanObjectWithIntAndBoolean() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, true, false, null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, true, false, null));
 assertNull(BooleanUtils.toBooleanObject(null, true, false, null));
 }

 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerWithValue() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 @Test
 public void testToIntegerWithBooleanAndInt() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithNullBoolean() {
 // This method should throw an IllegalArgumentException with the message "The Boolean must not be null"
 // when called with a null argument.
 BooleanUtils.toBoolean((Boolean)null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithWrongTypes() {
 // This method should throw an IllegalArgumentException with the message "The argument is not a Boolean. Use toBoolean(int) instead."
 // when called with an int type argument.
 try {
 BooleanUtils.toBoolean(42);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("The argument is not a Boolean. Use toBoolean(int) instead.", e.getMessage());
 }
}

 @Test(expected = NullPointerException.class)
 public void testToBooleanObjectWithNullBooleanAndNonNullValues() {
 // This method should throw a NullPointerException with the message "The Boolean must not be null"
 // when called with a null argument and non-null values.
 try {
 BooleanUtils.toBooleanObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO);
 fail();
 } catch (NullPointerException e) {
 assertEquals("The Boolean must not be null", e.getMessage());
 }
}

 @Test(expected = NullPointerException.class)
 public void testToBooleanObjectWithNonNullBooleanAndNullValues() {
 // This method should throw a NullPointerException with the message "The Boolean must not be null"
 // when called with a non-null argument and null values.
 try {
 BooleanUtils.toBooleanObject(Boolean.TRUE, null, null);
 fail();
 } catch (NullPointerException e) {
 assertEquals("The Boolean must not be null", e.getMessage());
 }
}

 @Test(expected = NullPointerException.class)
 public void testToBooleanObjectWithNonNullBooleanAndPartialNullValues() {
 // This method should throw a NullPointerException with the message "The Boolean must not be null"
 // when called with a non-null argument and partially null values.
 try {
 BooleanUtils.toBooleanObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, null);
 fail();
 } catch (NullPointerException e) {
 assertEquals("The Boolean must not be null", e.getMessage());
 }
}
}