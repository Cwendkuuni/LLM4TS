package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean negated = BooleanUtils.negate(bool);
 assertFalse(negated.booleanValue());

 bool = Boolean.FALSE;
 negated = BooleanUtils.negate(bool);
 assertTrue(negated.booleanValue());

 bool = null;
 negated = BooleanUtils.negate(bool);
 assertNull(negated);
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
 assertTrue(BooleanUtils.toBoolean(true));
 assertFalse(BooleanUtils.toBoolean(false));
 assertFalse(BooleanUtils.toBoolean(0));
 assertFalse(BooleanUtils.toBoolean(-1));
 assertFalse(BooleanUtils.toBoolean((Byte)Byte.MIN_VALUE));
 assertFalse(BooleanUtils.toBoolean((Short)Short.MIN_VALUE));
 assertFalse(BooleanUtils.toBoolean((Integer)Integer.MIN_VALUE));
 assertFalse(BooleanUtils.toBoolean((Long)Long.MIN_VALUE));
 assertFalse(BooleanUtils.toBoolean((Float)Float.NaN));
 assertFalse(BooleanUtils.toBoolean((Double)Double.NaN));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanDefaultIfNullWithDefaultFalse() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test(expected = NullPointerException.class)
 public void testToBooleanNullPointerException() {
 BooleanUtils.toBoolean(null);
 }

 @Test
 public void testToBooleanWithTrueValue() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanWithFalseValue() {
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithInvalidValue() {
 BooleanUtils.toBoolean(2, 1, 0);
 }

 @Test
 public void testToBooleanObject() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithTrueValue() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
 }

 @Test
 public void testToBooleanObjectWithFalseValue() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
 }

 @Test
 public void testToBooleanObjectWithNullValue() {
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithIntValueAndTrueValue() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, -1));
 }

 @Test
 public void testToBooleanObjectWithIntValueAndFalseValue() {
 assertFalse(BooleanUtils.toBooleanObject(0, 1, 0, -1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntValueAndInvalidValue() {
 BooleanUtils.toBooleanObject(2, 1, 0, -1);
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
 public void testToStringWithTrueValue() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 }

 @Test
 public void testToStringWithFalseValue() {
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithInvalidValue() {
 BooleanUtils.toString(null, "true", "false");
 }

 @Test
 public void testToStringWithTrueValueAndTrueString() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 }

 @Test
 public void testToStringWithTrueValueAndFalseString() {
 assertEquals("false", BooleanUtils.toString(Boolean.TRUE, "false", "false"));
 }

 @Test
 public void testToStringWithFalseValueAndTrueString() {
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 @Test
 public void testToStringWithFalseValueAndFalseString() {
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "false", "false"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringWithNullValue() {
 BooleanUtils.toString(null, "true", "false");
 }

 @Test
 public void testAnd() {
 assertTrue(BooleanUtils.and(true, true, true, true, true, true));
 assertFalse(BooleanUtils.and(true, true, false, false, true, true));
 }

 @Test
 public void testAndWithNullArray() {
 try {
 BooleanUtils.and((boolean[])null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testAndWithEmptyArray() {
 try {
 BooleanUtils.and(new boolean[0]);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("Array is empty", ex.getMessage());
 }
 }

 @Test
 public void testAnd() {
 boolean[] array = {true, true, true, true, true, true};
 assertTrue(BooleanUtils.and(array));

 array[2] = false;
 assertFalse(BooleanUtils.and(array));
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(booleanArray));

 booleanArray[2] = Boolean.FALSE;
 assertFalse(BooleanUtils.and(booleanArray));
 }

 @Test
 public void testOr() {
 assertTrue(BooleanUtils.or(true, true, true, true, true, true));
 assertFalse(BooleanUtils.or(true, true, false, false, true, true));
 }

 @Test
 public void testOrWithNullArray() {
 try {
 BooleanUtils.or((boolean[])null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testOrWithEmptyArray() {
 try {
 BooleanUtils.or(new boolean[0]);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("Array is empty", ex.getMessage());
 }
 }

 @Test
 public void testOr() {
 boolean[] array = {true, true, true, true, true, true};
 assertTrue(BooleanUtils.or(array));

 array[2] = false;
 assertFalse(BooleanUtils.or(array));
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray[2] = Boolean.FALSE;
 assertFalse(BooleanUtils.or(booleanArray));
 }

 @Test
 public void testXor() {
 assertTrue(BooleanUtils.xor(true, false, true, false, true, false));
 assertFalse(BooleanUtils.xor(true, false, true, false, true, true));
 }

 @Test
 public void testXorWithNullArray() {
 try {
 BooleanUtils.xor((boolean[])null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 @Test
 public void testXorWithEmptyArray() {
 try {
 BooleanUtils.xor(new boolean[0]);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("Array is empty", ex.getMessage());
 }
 }

 @Test
 public void testXor() {
 boolean[] array = {true, true, true, true, true, true};
 boolean result = BooleanUtils.xor(array);
 assertTrue(result == true || result == false);

 array[2] = false;
 result = BooleanUtils.xor(array);
 assertTrue(result == true || result == false);
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean result = BooleanUtils.xor(booleanArray);
 assertTrue(result == Boolean.TRUE || result == Boolean.FALSE);

 booleanArray[2] = Boolean.FALSE;
 result = BooleanUtils.xor(booleanArray);
 assertTrue(result == Boolean.TRUE || result == Boolean.FALSE);
 }
}