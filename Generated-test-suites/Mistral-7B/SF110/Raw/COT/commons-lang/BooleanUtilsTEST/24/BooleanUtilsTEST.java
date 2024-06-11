package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.MathUtils;
import org.apache.commons.lang3.NullPointerException;
import org.junit.Test;

public class BooleanUtilsTest {

 // Test methods for negate
 @Test
 public void testNegateNullInput() {
 assertNull(BooleanUtils.negate(null));
 }

 @Test
 public void testNegateTrueInput() {
 assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
 }

 @Test
 public void testNegateFalseInput() {
 assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
 }

 // Test methods for isTrue
 @Test
 public void testIsTrueNullInput() {
 assertFalse(BooleanUtils.isTrue(null));
 }

 @Test
 public void testIsTrueFalseInput() {
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 }

 @Test
 public void testIsTrueTrueInput() {
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 }

 // Test methods for isNotTrue
 @Test
 public void testIsNotTrueNullInput() {
 assertFalse(BooleanUtils.isNotTrue(null));
 }

 @Test
 public void testIsNotTrueFalseInput() {
 assertTrue(BooleanUtils.isNotTrue(Boolean.TRUE));
 }

 @Test
 public void testIsNotTrueTrueInput() {
 assertFalse(BooleanUtils.isNotTrue(Boolean.FALSE));
 }

 // Test methods for isFalse
 @Test
 public void testIsFalseNullInput() {
 assertFalse(BooleanUtils.isFalse(null));
 }

 @Test
 public void testIsFalseTrueInput() {
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 }

 @Test
 public void testIsFalseFalseInput() {
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 }

 // Test methods for isNotFalse
 @Test
 public void testIsNotFalseNullInput() {
 assertFalse(BooleanUtils.isNotFalse(null));
 }

 @Test
 public void testIsNotFalseTrueInput() {
 assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 }

 @Test
 public void testIsNotFalseFalseInput() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 }

 // Test methods for toBoolean
 @Test
 public void testToBooleanNullInput() {
 assertFalse(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanFalseInput() {
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 }

 @Test
 public void testToBooleanTrueInput() {
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 }

 @Test
 public void testToBooleanIntInputFalse() {
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanIntInputTrue() {
 assertTrue(BooleanUtils.toBoolean(1));
 }

 @Test
 public void testToBooleanIntInputWithTrueValue() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 }

 @Test
 public void testToBooleanIntInputWithFalseValue() {
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanIntInputWithInvalidValue() {
 BooleanUtils.toBoolean(2, 1, 0);
 }

 // Test methods for toBooleanDefaultIfNull
 @Test
 public void testToBooleanDefaultIfNullNullInputTrueValue() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 @Test
 public void testToBooleanDefaultIfNullNullInputFalseValue() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanInputTrue() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanInputFalse() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanInputFalseValue() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanInputNullValue() {
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 // Test methods for toBooleanObject
 @Test
 public void testToBooleanObjectNullInput() {
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectFalseInput() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
 }

 @Test
 public void testToBooleanObjectTrueInput() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
 }

 @Test
 public void testToBooleanObjectIntInputFalse() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 }

 @Test
 public void testToBooleanObjectIntInputTrue() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 }

 @Test
 public void testToBooleanObjectIntInputWithTrueValue() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
 }

 @Test
 public void testToBooleanObjectIntInputWithFalseValue() {
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
 }

 @Test
 public void testToBooleanObjectIntInputWithNullValue() {
 assertNull(BooleanUtils.toBooleanObject(0, 1, 0, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectIntInputWithInvalidValue() {
 BooleanUtils.toBooleanObject(2, 1, 0, 0);
 }

 // Test methods for toInteger
 @Test
 public void testToIntegerNullInput() {
 assertThrows(NullPointerException.class, () -> BooleanUtils.toInteger(null));
 }

 @Test
 public void testToIntegerFalseInput() {
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerTrueInput() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 }

 @Test
 public void testToIntegerWithValueTrue() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 }

 @Test
 public void testToIntegerWithValueFalse() {
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 @Test
 public void testToIntegerWithValueWithTrueValue() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 1, 0));
 }

 @Test
 public void testToIntegerWithValueWithFalseValue() {
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 1, 0));
 }

 // Test methods for toString
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

 // Test methods for and
 @Test
 public void testAndNullArray() {
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and((Boolean[]) null));
 }

 @Test
 public void testAndEmptyArray() {
 assertFalse(BooleanUtils.and());
 }

 @Test
 public void testAndArrayWithSingleFalseElement() {
 assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testAndArrayWithSingleTrueElement() {
 assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testAndArrayWithMultipleFalseElements() {
 assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testAndArrayWithMultipleTrueElements() {
 assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testAndArrayWithNullElement() {
 assertThrows(NullPointerException.class, () -> BooleanUtils.and(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE }));
 }

 // Test methods for or
 @Test
 public void testOrNullArray() {
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or((Boolean[]) null));
 }

 @Test
 public void testOrEmptyArray() {
 assertFalse(BooleanUtils.or());
 }

 @Test
 public void testOrArrayWithSingleTrueElement() {
 assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testOrArrayWithSingleFalseElement() {
 assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testOrArrayWithMultipleFalseElements() {
 assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testOrArrayWithMultipleTrueElements() {
 assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testOrArrayWithNullElement() {
 assertThrows(NullPointerException.class, () -> BooleanUtils.or(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE }));
 }

 // Test methods for xor
 @Test
 public void testXorNullArray() {
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor((Boolean[]) null));
 }

 @Test
 public void testXorEmptyArray() {
 assertFalse(BooleanUtils.xor());
 }

 @Test
 public void testXorArrayWithSingleTrueElement() {
 assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testXorArrayWithSingleFalseElement() {
 assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testXorArrayWithMultipleFalseElements() {
 assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
 }

 @Test
 public void testXorArrayWithMultipleTrueElements() {
 assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
 }

 @Test
 public void testXorArrayWithNullElement() {
 assertThrows(NullPointerException.class, () -> BooleanUtils.xor(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE }));
 }
}