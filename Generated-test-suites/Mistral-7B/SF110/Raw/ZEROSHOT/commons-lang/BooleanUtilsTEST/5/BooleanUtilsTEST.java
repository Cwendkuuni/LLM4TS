package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertNull(BooleanUtils.negate(null));
 assertTrue(BooleanUtils.negate(bool).equals(Boolean.FALSE));
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.negate(bool).equals(Boolean.TRUE));
 }

 @Test
 public void testIsTrue() {
 assertFalse(BooleanUtils.isTrue(null));
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 }

 @Test
 public void testIsNotTrue() {
 assertFalse(BooleanUtils.isNotTrue(null));
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 }

 @Test
 public void testIsFalse() {
 assertFalse(BooleanUtils.isFalse(null));
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 }

 @Test
 public void testIsNotFalse() {
 assertFalse(BooleanUtils.isNotFalse(null));
 assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
 }

 @Test
 public void testToBoolean() {
 assertFalse(BooleanUtils.toBoolean(null));
 assertFalse(BooleanUtils.toBoolean(Integer.MIN_VALUE));
 assertTrue(BooleanUtils.toBoolean(Integer.MAX_VALUE));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertEquals(false, BooleanUtils.toBooleanDefaultIfNull(null, false));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanWithInt() {
 assertFalse(BooleanUtils.toBoolean(-1));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(-1));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 }

 @Test
 public void testToBooleanWithTwoInts() {
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 }

 @Test
 public void testToBooleanWithTwoIntsThrowsException() {
 try {
 BooleanUtils.toBoolean(2, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithTwoIntegers() {
 assertFalse(BooleanUtils.toBoolean(null, Integer.valueOf(1), Integer.valueOf(0)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(2), null, Integer.valueOf(0)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(2), null));

 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(1)));
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndANullValue() {
 assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndANullValueThrowsException() {
 try {
 BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndDifferentNullValues() {
 assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1)));
 assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), null, Integer.valueOf(0), Integer.valueOf(1)));
 assertNull(BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndDifferentNullValuesThrowsException() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), null, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndEqualNullValues() {
 assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanObjectWithTwoIntsAndEqualNullValuesThrowsException() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), null, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToInteger() {
 assertEquals(0, BooleanUtils.toInteger(false));
 assertEquals(1, BooleanUtils.toInteger(true));
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
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "on", "off"));
 assertEquals("off", BooleanUtils.toString(Boolean.FALSE, "on", "off"));
 assertEquals("yes", BooleanUtils.toString(Boolean.TRUE, "yes", "no"));
 assertEquals("no", BooleanUtils.toString(Boolean.FALSE, "yes", "no"));
 }

 @Test
 public void testAnd() {
 assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAndThrowsException() {
 BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE);
 }

 @Test
 public void testAndBooleanArray() {
 assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE}));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAndBooleanArrayThrowsException() {
 BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE});
 }

 @Test
 public void testOr() {
 assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 assertFalse(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOrThrowsException() {
 BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE);
 }

 @Test
 public void testOrBooleanArray() {
 assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testOrBooleanArrayThrowsException() {
 BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE});
 }

 @Test
 public void testXor() {
 assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testXorThrowsException() {
 BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE);
 }

 @Test
 public void testXorBooleanArray() {
 assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
 assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testXorBooleanArrayThrowsException() {
 BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE});
 }
}