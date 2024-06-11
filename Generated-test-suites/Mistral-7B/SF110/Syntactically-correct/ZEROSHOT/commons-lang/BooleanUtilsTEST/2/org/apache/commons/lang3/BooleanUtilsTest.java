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
 Integer intValue = 1;
 Assert.assertTrue(BooleanUtils.toBoolean(intValue));
 Assert.assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));
 }

 @Test
 public void testToBooleanWithBoolean() {
 Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 }

 @Test
 public void testToBooleanWithInteger() {
 Assert.assertTrue(BooleanUtils.toBoolean(1));
 Assert.assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanWithIntAndBooleanValue() {
 Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithIntegerThrowsIllegalArgumentException() {
 BooleanUtils.toBoolean(2, 1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithIntegerWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toBoolean(null, 1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithBooleanThrowsIllegalArgumentException() {
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithInteger() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntegerThrowsIllegalArgumentException() {
 BooleanUtils.toBooleanObject(2, 1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntegerWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toBooleanObject(null, 1, 0);
 }

 @Test
 public void testToBooleanObjectWithBooleanAndBooleanValue() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithBooleanAndBooleanValueThrowsIllegalArgumentException() {
 BooleanUtils.toBooleanObject(Boolean.TRUE, null, Boolean.FALSE);
 }

 @Test
 public void testToBooleanObjectWithBooleanAndBooleanValueWithNullValue() {
 Assert.assertNull(BooleanUtils.toBooleanObject(Boolean.TRUE, Boolean.TRUE, null));
 }

 @Test
 public void testToBooleanObjectWithBooleanAndIntegerValue() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, 1, 0));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, 1, 0));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, 1, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithBooleanAndIntegerValueThrowsIllegalArgumentException() {
 BooleanUtils.toBooleanObject(Boolean.TRUE, 2, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithBooleanAndIntegerValueWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toBooleanObject(Boolean.TRUE, null, 0);
 }

 @Test
 public void testToInteger() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToStringTrueFalse() {
 Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test
 public void testToStringOnOff() {
 Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test
 public void testToStringYesNo() {
 Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test
 public void testToStringTrueFalseWithBoolean() {
 Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringTrueFalseWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toStringTrueFalse(null);
 }

 @Test
 public void testToStringOnOffWithBoolean() {
 Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringOnOffWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toStringOnOff(null);
 }

 @Test
 public void testToStringYesNoWithBoolean() {
 Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringYesNoWithNullValueThrowsIllegalArgumentException() {
 BooleanUtils.toStringYesNo(null);
 }

 @Test
 public void testAnd() {
 Assert.assertTrue(BooleanUtils.and(true, true, true, true));
 Assert.assertFalse(BooleanUtils.and(true, true, true, false));
 Assert.assertFalse(BooleanUtils.and(true, false, true, false));
 Assert.assertFalse(BooleanUtils.and(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithBooleanArrayThrowsIllegalArgumentException() {
 Boolean[] booleanArray = new Boolean[]{null};
 BooleanUtils.and(booleanArray);
 }

 @Test
 public void testAndWithBooleanArrayWithNullElement() {
 Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE};
 Assert.assertFalse(BooleanUtils.and(booleanArray));
 }

 @Test
 public void testAndWithBooleanArrayWithEmptyArray() {
 Boolean[] booleanArray = new Boolean[]{};
 Assert.assertFalse(BooleanUtils.and(booleanArray));
 }

 @Test
 public void testAndWithBooleanPrimitives() {
 Assert.assertTrue(BooleanUtils.and(true, true, true, true));
 Assert.assertFalse(BooleanUtils.and(true, true, true, false));
 Assert.assertFalse(BooleanUtils.and(true, false, true, false));
 Assert.assertFalse(BooleanUtils.and(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithBooleanPrimitivesThrowsIllegalArgumentException() {
 BooleanUtils.and(new boolean[]{true, true, true, null});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithBooleanPrimitivesWithNullElementThrowsIllegalArgumentException() {
 BooleanUtils.and(new boolean[]{true, true, true, null, false});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndWithBooleanPrimitivesWithEmptyArrayThrowsIllegalArgumentException() {
 BooleanUtils.and(new boolean[]{});
 }

 @Test
 public void testOr() {
 Assert.assertTrue(BooleanUtils.or(true, true, true, true));
 Assert.assertTrue(BooleanUtils.or(true, true, true, false));
 Assert.assertTrue(BooleanUtils.or(true, false, true, false));
 Assert.assertTrue(BooleanUtils.or(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithBooleanArrayThrowsIllegalArgumentException() {
 Boolean[] booleanArray = new Boolean[]{null};
 BooleanUtils.or(booleanArray);
 }

 @Test
 public void testOrWithBooleanArrayWithNullElement() {
 Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE};
 Assert.assertTrue(BooleanUtils.or(booleanArray));
 }

 @Test
 public void testOrWithBooleanArrayWithEmptyArray() {
 Boolean[] booleanArray = new Boolean[]{};
 Assert.assertTrue(BooleanUtils.or(booleanArray));
 }

 @Test
 public void testOrWithBooleanPrimitives() {
 Assert.assertTrue(BooleanUtils.or(true, true, true, true));
 Assert.assertTrue(BooleanUtils.or(true, true, true, false));
 Assert.assertTrue(BooleanUtils.or(true, false, true, false));
 Assert.assertTrue(BooleanUtils.or(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithBooleanPrimitivesThrowsIllegalArgumentException() {
 BooleanUtils.or(new boolean[]{true, true, true, null});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithBooleanPrimitivesWithNullElementThrowsIllegalArgumentException() {
 BooleanUtils.or(new boolean[]{true, true, true, null, false});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrWithBooleanPrimitivesWithEmptyArrayThrowsIllegalArgumentException() {
 BooleanUtils.or(new boolean[]{});
 }

 @Test
 public void testXor() {
 Assert.assertFalse(BooleanUtils.xor(true, true, true, true));
 Assert.assertTrue(BooleanUtils.xor(true, true, true, false));
 Assert.assertTrue(BooleanUtils.xor(true, false, true, false));
 Assert.assertTrue(BooleanUtils.xor(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithBooleanArrayThrowsIllegalArgumentException() {
 Boolean[] booleanArray = new Boolean[]{null};
 BooleanUtils.xor(booleanArray);
 }

 @Test
 public void testXorWithBooleanArrayWithNullElement() {
 Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE};
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 }

 @Test
 public void testXorWithBooleanArrayWithEmptyArray() {
 Boolean[] booleanArray = new Boolean[]{};
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 }

 @Test
 public void testXorWithBooleanPrimitives() {
 Assert.assertFalse(BooleanUtils.xor(true, true, true, true));
 Assert.assertTrue(BooleanUtils.xor(true, true, true, false));
 Assert.assertTrue(BooleanUtils.xor(true, false, true, false));
 Assert.assertTrue(BooleanUtils.xor(false, false, false, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithBooleanPrimitivesThrowsIllegalArgumentException() {
 BooleanUtils.xor(new boolean[]{true, true, true, null});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithBooleanPrimitivesWithNullElementThrowsIllegalArgumentException() {
 BooleanUtils.xor(new boolean[]{true, true, true, null, false});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorWithBooleanPrimitivesWithEmptyArrayThrowsIllegalArgumentException() {
 BooleanUtils.xor(new boolean[]{});
 }
}