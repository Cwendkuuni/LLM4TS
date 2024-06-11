package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 Assert.assertNull(BooleanUtils.negate(null));
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 Assert.assertTrue(BooleanUtils.isTrue(bool));
 Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isTrue(null));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 Assert.assertFalse(BooleanUtils.isNotTrue(bool));
 Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isNotTrue(null));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 Assert.assertTrue(BooleanUtils.isFalse(bool));
 Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isFalse(null));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 Assert.assertFalse(BooleanUtils.isNotFalse(bool));
 Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isNotFalse(null));
 }

 @Test
 public void testToBoolean() {
 Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanWithInt() {
 Assert.assertTrue(BooleanUtils.toBoolean(1));
 Assert.assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanWithInts() {
 Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 }

 @Test
 public void testToBooleanWithIntegers() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanObjectWithIntsAndNullValue() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, null, Integer.valueOf(0), null));
 }

 @Test
 public void testToInteger() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toInteger(null));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerWithValues() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 @Test
 public void testToIntegerWithBooleans() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
 }

 @Test
 public void testToIntegerWithBooleansAndNullValue() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null));
 Assert.assertNull(BooleanUtils.toIntegerObject(null, null, Boolean.FALSE, null));
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
 public void testToString() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 Assert.assertEquals("", BooleanUtils.toString(null, "true", "false"));
 }

 @Test
 public void testToStringWithValues() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
 Assert.assertEquals("", BooleanUtils.toString(null, "true", "false", null));
 }

 @Test
 public void testAnd() {
 boolean[] array = {true, true, true};
 assertTrue(BooleanUtils.and(array));
 boolean[] array2 = {true, false, true};
 assertFalse(BooleanUtils.and(array2));
 assertFalse(BooleanUtils.and(new boolean[0]));
 }

 @Test
 public void testAndWithBooleans() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(array));
 Boolean[] array3 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(array3));
 assertTrue(BooleanUtils.and(new Boolean[0]));
 }

 @Test
 public void testOr() {
 boolean[] array = {true, false, true};
 assertFalse(BooleanUtils.or(array));
 boolean[] array4 = {true, true, true};
 assertTrue(BooleanUtils.or(array4));
 assertFalse(BooleanUtils.or(new boolean[0]));
 }

 @Test
 public void testOrWithBooleans() {
 Boolean[] array = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertFalse(BooleanUtils.or(array));
 Boolean[] array5 = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(array5));
 assertTrue(BooleanUtils.or(new Boolean[0]));
 }

 @Test
 public void testXor() {
 boolean[] array = {true, true, true};
 assertFalse(BooleanUtils.xor(array));
 boolean[] array6 = {true, false, true};
 assertTrue(BooleanUtils.xor(array6));
 assertFalse(BooleanUtils.xor(new boolean[0]));
 }

 @Test
 public void testXorWithBooleans() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.xor(array));
 Boolean[] array7 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 assertTrue(BooleanUtils.xor(array7));
 assertFalse(BooleanUtils.xor(new Boolean[0]));
 }
}