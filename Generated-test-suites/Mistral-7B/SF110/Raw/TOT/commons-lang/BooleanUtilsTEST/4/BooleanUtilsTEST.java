package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 final Boolean negated = BooleanUtils.negate(bool);
 assertFalse(negated.booleanValue());
 final Boolean negatedNull = BooleanUtils.negate(null);
 assertNull(negatedNull);
 }

 @Test
 public void testIsTrue() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isTrue(boolTrue));
 assertFalse(BooleanUtils.isTrue(boolFalse));
 }

 @Test
 public void testIsNotTrue() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(boolFalse));
 assertFalse(BooleanUtils.isNotFalse(boolTrue));
 assertFalse(BooleanUtils.isNotTrue(boolTrue));
 assertTrue(BooleanUtils.isNotTrue(boolFalse));
 }

 @Test
 public void testIsFalse() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertFalse(BooleanUtils.isFalse(boolTrue));
 assertTrue(BooleanUtils.isFalse(boolFalse));
 }

 @Test
 public void testIsNotFalse() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(boolTrue));
 assertFalse(BooleanUtils.isNotFalse(boolFalse));
 }

 @Test
 public void testToBoolean() {
 final Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool));
 final Integer intValue = 1;
 assertTrue(BooleanUtils.toBoolean(intValue));
 final int intValueNegative = -1;
 assertFalse(BooleanUtils.toBoolean(intValueNegative));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean bool = Boolean.TRUE;
 final Boolean boolNull = null;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(boolNull, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(boolNull, true));
 }

 @Test
 public void testToBooleanWithInteger() {
 final int intTrue = 1;
 final int intFalse = 0;
 assertTrue(BooleanUtils.toBoolean(intTrue));
 assertFalse(BooleanUtils.toBoolean(intFalse));
 }

 @Test
 public void testToBooleanObject() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final Integer intTrue = 1;
 final Integer intFalse = 0;
 assertEquals(boolTrue, BooleanUtils.toBooleanObject(boolTrue));
 assertEquals(boolFalse, BooleanUtils.toBooleanObject(boolFalse));
 assertEquals(intTrue, BooleanUtils.toBooleanObject(intTrue));
 assertEquals(intFalse, BooleanUtils.toBooleanObject(intFalse));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final Integer intTrue = 1;
 final Integer intFalse = 0;
 assertEquals(boolTrue, BooleanUtils.toBooleanObject(intTrue));
 assertEquals(boolFalse, BooleanUtils.toBooleanObject(intFalse));
 }

 @Test
 public void testToBooleanWithIntegerTrueValueFalseValue() {
 final int intTrue = 1;
 final int intFalse = 0;
 try {
 BooleanUtils.toBoolean(intTrue, 1, 0);
 assertTrue(true);
 } catch (IllegalArgumentException e) {
 fail("Expected to pass when true value is specified");
 }

 try {
 BooleanUtils.toBoolean(intFalse, 1, 0);
 assertTrue(true);
 } catch (IllegalArgumentException e) {
 fail("Expected to pass when false value is specified");
 }

 try {
 BooleanUtils.toBoolean(intTrue, 0, 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 }

 try {
 BooleanUtils.toBoolean(intFalse, 0, 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 }
}

 @Test
 public void testToBooleanWithIntegerTrueValueNullValue() {
 final int intTrue = 1;
 final int intNull = 0;
 try {
 BooleanUtils.toBoolean(intTrue, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(null));
 assertTrue(true);
 } catch (IllegalArgumentException e) {
 fail("Expected to pass when true value is specified");
 }

 try {
 BooleanUtils.toBoolean(intNull, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(null));
 assertNull(null);
 } catch (IllegalArgumentException e) {
 fail("Expected to pass when null value is specified");
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithInvalidInteger() {
 final int invalidValue = 2;
 BooleanUtils.toBoolean(invalidValue, 1, 0);
 fail("Expected IllegalArgumentException");
 }
}