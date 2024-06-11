package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Assert.assertNull(BooleanUtils.negate(null));
 Assert.assertFalse(BooleanUtils.negate(bool));
 bool = Boolean.FALSE;
 Assert.assertTrue(BooleanUtils.negate(bool));
 }

 @Test
 public void testIsTrue() {
 Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isTrue(null));
 }

 @Test
 public void testIsNotTrue() {
 Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isNotTrue(null));
 }

 @Test
 public void testIsFalse() {
 Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isFalse(null));
 }

 @Test
 public void testIsNotFalse() {
 Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isNotFalse(null));
 }

 @Test
 public void testToBoolean() {
 Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.toBoolean(null));
 Assert.assertFalse(BooleanUtils.toBoolean((Object)0));
 Assert.assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanWithInteger() {
 Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
 Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
 Assert.assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 Assert.assertTrue(BooleanUtils.toBooleanObject(Integer.valueOf(1)).booleanValue());
 Assert.assertFalse(BooleanUtils.toBooleanObject(Integer.valueOf(0)).booleanValue());
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanWithBooleanAndValues() {
 Assert.assertTrue(BooleanUtils.toBoolean(true, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(false, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(null, 1, 0));
 }

 @Test
 public void testToBooleanWithIntegerAndValues() {
 Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(null, 1, 0));
 }

 @Test
 public void testToBooleanObjectWithIntegerAndValues() {
 Assert.assertTrue(BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null).booleanValue());
 Assert.assertFalse(BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null).booleanValue());
 Assert.assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
 }

 @Test
 public void testToBooleanObjectWithIntegerAndValuesThrowsException() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObjectWithNullableIntegerAndValues() {
 Assert.assertTrue(BooleanUtils.toBooleanObject(Integer.valueOf(1), null, Integer.valueOf(0), null).booleanValue());
 Assert.assertFalse(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null).booleanValue());
 Assert.assertNull(BooleanUtils.toBooleanObject(null, null, null, null));
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), null, Integer.valueOf(0), null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToInteger() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 Assert.assertEquals(0, BooleanUtils.toInteger(null));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 Assert.assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerWithBoolean() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 Assert.assertEquals(0, BooleanUtils.toInteger(null));
 }

 @Test
 public void testToIntegerWithBooleanAndValues() {
 Assert.assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 Assert.assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 Assert.assertEquals(0, BooleanUtils.toInteger(null, 1, 0));
 }

 @Test
 public void testToIntegerWithBooleanAndValuesThrowsException() {
 try {
 BooleanUtils.toInteger(true, 2, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test
 public void testToStringTrueFalse() {
 Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 Assert.assertEquals(null, BooleanUtils.toStringTrueFalse(null));
 }

 @Test
 public void testToStringOnOff() {
 Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 Assert.assertEquals(null, BooleanUtils.toStringOnOff(null));
 }

 @Test
 public void testToStringYesNo() {
 Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 Assert.assertEquals(null, BooleanUtils.toStringYesNo(null));
 }

 @Test
 public void testToStringWithBoolean() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 Assert.assertEquals(null, BooleanUtils.toString(null, "true", "false"));
 }

 @Test
 public void testToStringWithBooleanAndValues() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 Assert.assertEquals(null, BooleanUtils.toString(null, "true", "false"));
 }

 @Test
 public void testToStringWithBooleanAndValuesThrowsException() {
 try {
 BooleanUtils.toString(Boolean.FALSE, "true", "false", "invalid");
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The String did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testAnd() {
 boolean[] array = { true, true, true };
 assertTrue(BooleanUtils.and(array));
 array[1] = false;
 assertFalse(BooleanUtils.and(array));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullArray() {
 BooleanUtils.and((Boolean[])null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithEmptyArray() {
 BooleanUtils.and(new Boolean[0]);
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] array = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.and((Boolean[])ArrayUtils.add(array, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 assertTrue(BooleanUtils.and(array));
 array[1] = Boolean.FALSE;
 assertFalse(BooleanUtils.and(array));
 }

 @Test
 public void testAndWithBooleanArrayWithPrimitiveConversion() {
 boolean[] primitiveArray = { true, true, true };
 Boolean[] booleanArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.and((Boolean[])ArrayUtils.add(booleanArray, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 assertTrue(BooleanUtils.and(booleanArray));
 BooleanUtils.and(booleanArray, primitiveArray);
 assertTrue(BooleanUtils.and(primitiveArray));
 }

 @Test
 public void testOr() {
 boolean[] array = { true, true, true };
 assertTrue(BooleanUtils.or(array));
 array[1] = false;
 assertFalse(BooleanUtils.or(array));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithNullArray() {
 BooleanUtils.or((Boolean[])null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithEmptyArray() {
 BooleanUtils.or(new Boolean[0]);
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] array = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.or((Boolean[])ArrayUtils.add(array, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 assertTrue(BooleanUtils.or(array));
 array[1] = Boolean.FALSE;
 assertFalse(BooleanUtils.or(array));
 }

 @Test
 public void testOrWithBooleanArrayWithPrimitiveConversion() {
 boolean[] primitiveArray = { true, true, true };
 Boolean[] booleanArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.or((Boolean[])ArrayUtils.add(booleanArray, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 assertTrue(BooleanUtils.or(booleanArray));
 BooleanUtils.or(booleanArray, primitiveArray);
 assertTrue(BooleanUtils.or(primitiveArray));
 }

 @Test
 public void testXor() {
 boolean[] array = { true, true, true };
 boolean result = BooleanUtils.xor(array);
 Assert.assertEquals(true, result);
 array[1] = false;
 result = BooleanUtils.xor(array);
 Assert.assertEquals(true, result);
 array[0] = false;
 result = BooleanUtils.xor(array);
 Assert.assertEquals(true, result);
 array[2] = false;
 result = BooleanUtils.xor(array);
 Assert.assertEquals(false, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithNullArray() {
 BooleanUtils.xor((Boolean[])null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithEmptyArray() {
 BooleanUtils.xor(new Boolean[0]);
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] array = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.xor((Boolean[])ArrayUtils.add(array, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 BooleanUtils.xor(array);
 assertEquals(true, array[0].booleanValue());
 assertEquals(true, array[1].booleanValue());
 assertEquals(true, array[2].booleanValue());
 }

 @Test
 public void testXorWithBooleanArrayWithPrimitiveConversion() {
 boolean[] primitiveArray = { true, true, true };
 Boolean[] booleanArray = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
 try {
 BooleanUtils.xor((Boolean[])ArrayUtils.add(booleanArray, null));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // expected
 }
 BooleanUtils.xor(booleanArray, primitiveArray);
 assertEquals(true, booleanArray[0].booleanValue());
 assertEquals(true, booleanArray[1].booleanValue());
 assertEquals(true, booleanArray[2].booleanValue());
 }
}