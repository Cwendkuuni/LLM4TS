package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean negatedBool = BooleanUtils.negate(bool);
 assertFalse(negatedBool);

 bool = Boolean.FALSE;
 negatedBool = BooleanUtils.negate(bool);
 assertTrue(negatedBool);

 bool = null;
 negatedBool = BooleanUtils.negate(bool);
 assertNull(negatedBool);
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
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));

 int intValue = 1;
 assertTrue(BooleanUtils.toBoolean(intValue));

 intValue = 0;
 assertFalse(BooleanUtils.toBoolean(intValue));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));
 }

 @Test
 public void testToBooleanWithInt() {
 int intValue = 1;
 assertTrue(BooleanUtils.toBoolean(intValue));

 intValue = 0;
 assertFalse(BooleanUtils.toBoolean(intValue));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 Number number = NumberUtils.INTEGER_ONE;
 assertTrue(BooleanUtils.toBooleanObject(number).booleanValue());

 number = NumberUtils.INTEGER_ZERO;
 assertFalse(BooleanUtils.toBooleanObject(number).booleanValue());
 }

 @Test
 public void testToBooleanWithBooleanAndInts() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool, 1, 0));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE, 1, 0));
 try {
 BooleanUtils.toBoolean(null, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Boolean did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObjectWithIntegerAndInts() {
 Number number = NumberUtils.INTEGER_ONE;
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(number, 1, 0, -1));

 number = NumberUtils.INTEGER_ZERO;
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(number, 1, 0, -1));
 assertNull(BooleanUtils.toBooleanObject(null, 1, 0, -1));
 try {
 BooleanUtils.toBooleanObject(NumberUtils.INTEGER_TWO, 1, 0, -1);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToInteger() {
 assertTrue(BooleanUtils.toInteger(Boolean.TRUE) == 1);
 assertFalse(BooleanUtils.toInteger(Boolean.FALSE) == 1);
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
 public void testToIntegerWithValue() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 assertFalse(BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
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
 boolean[] boolArray = { true, true, true, true };
 assertTrue(BooleanUtils.and(boolArray));

 boolArray = new boolean[] { true, true, false, true };
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = new boolean[] { true, false, false, true };
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = new boolean[0];
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] boolArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertEquals(Boolean.TRUE, BooleanUtils.and(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE };
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = new Boolean[0];
 assertFalse(BooleanUtils.and(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }

 @Test
 public void testOr() {
 boolean[] boolArray = { true, true, true, true };
 assertTrue(BooleanUtils.or(boolArray));

 boolArray = new boolean[] { true, true, false, true };
 assertTrue(BooleanUtils.or(boolArray));

 boolArray = new boolean[] { true, false, false, true };
 assertTrue(BooleanUtils.or(boolArray));

 boolArray = new boolean[0];
 assertFalse(BooleanUtils.or(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] boolArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertEquals(Boolean.TRUE, BooleanUtils.or(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
 assertTrue(BooleanUtils.or(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE };
 assertTrue(BooleanUtils.or(boolArray));

 boolArray = new Boolean[0];
 assertFalse(BooleanUtils.or(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }

 @Test
 public void testXor() {
 boolean[] boolArray = { true, true, true, true };
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = new boolean[] { true, true, false, true };
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = new boolean[] { true, false, false, true };
 assertTrue(BooleanUtils.xor(boolArray));

 boolArray = new boolean[0];
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] boolArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE };
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE };
 assertTrue(BooleanUtils.xor(boolArray));

 boolArray = new Boolean[0];
 assertFalse(BooleanUtils.xor(boolArray));

 boolArray = null;
 throw new IllegalArgumentException("The Array must not be null");
 }
}