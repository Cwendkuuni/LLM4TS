package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.FALSE;
 assertEquals(Boolean.TRUE, BooleanUtils.negate(bool));
 bool = Boolean.TRUE;
 assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 assertNull(BooleanUtils.negate(null));
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isTrue(null));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotTrue(null));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isFalse(null));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotFalse(null));
 }

 // Test for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 }

 // Test for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 // Test for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 }

 // Test for toBoolean method with Integer argument
 @Test
 public void testToBooleanWithInteger() {
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
 }

 // Test for toBooleanObject method with Integer argument
 @Test
 public void testToBooleanObjectWithInteger() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
 }

 // Test for toBoolean method with int and int arguments
 @Test
 public void testToBooleanWithIntAndInt() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 }

 // Test for toBoolean method with Integer and int arguments
 @Test
 public void testToBooleanWithIntegerAndInt() {
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), 1, 0));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), 1, 0));
 }

 // Test for toBoolean method with Integer and Integer arguments
 @Test
 public void testToBooleanWithIntegerAndInteger() {
 try {
 BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBoolean method with Integer and Integer and Integer arguments
 @Test
 public void testToBooleanWithIntegerAndIntegerAndInteger() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBooleanObject method with int and int and int and int arguments
 @Test
 public void testToBooleanObjectWithIntAndIntAndIntAndInt() {
 try {
 BooleanUtils.toBooleanObject(1, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBooleanObject method with Integer and int and int and int arguments
 @Test
 public void testToBooleanObjectWithIntegerAndIntAndIntAndInt() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBooleanObject method with Integer and Integer and int and int arguments
 @Test
 public void testToBooleanObjectWithIntegerAndIntegerAndIntAndInt() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBooleanObject method with Integer and Integer and Integer and int arguments
 @Test
 public void testToBooleanObjectWithIntegerAndIntegerAndIntegerAndInt() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toBooleanObject method with Integer and Integer and Integer and Integer arguments
 @Test
 public void testToBooleanObjectWithIntegerAndIntegerAndIntegerAndInteger() {
 try {
 BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toInteger method
 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 // Test for toIntegerObject method with Boolean argument
 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 }

 // Test for toIntegerObject method with int argument
 @Test
 public void testToIntegerObjectWithInt() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(1));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(0));
 }

 // Test for toIntegerObject method with Integer argument
 @Test
 public void testToIntegerObjectWithInteger() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Integer.valueOf(1)));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Integer.valueOf(0)));
 }

 // Test for toInteger method with Boolean argument
 @Test
 public void testToIntegerWithBoolean() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 // Test for toInteger method with int and int arguments
 @Test
 public void testToIntegerWithIntAndInt() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 // Test for toInteger method with Boolean and int arguments
 @Test
 public void testToIntegerWithBooleanAndInt() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1));
 }

 // Test for toInteger method with Boolean and int and int arguments
 @Test
 public void testToIntegerWithBooleanAndIntAndInt() {
 try {
 BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for toStringTrueFalse method
 @Test
 public void testToStringTrueFalse() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 // Test for toStringOnOff method
 @Test
 public void testToStringOnOff() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 // Test for toStringYesNo method
 @Test
 public void testToStringYesNo() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 // Test for and method with boolean array
 @Test
 public void testAndWithBooleanArray() {
 assertTrue(BooleanUtils.and(new boolean[] { true, true, true }));
 assertFalse(BooleanUtils.and(new boolean[] { true, true, false }));
 }

 // Test for and method with Boolean array
 @Test
 public void testAndWithBooleanArrayBoolean() {
 try {
 BooleanUtils.and(new Boolean[] { Boolean.TRUE, null, Boolean.TRUE });
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for or method with boolean array
 @Test
 public void testOrWithBooleanArray() {
 assertTrue(BooleanUtils.or(new boolean[] { true, true, true }));
 assertFalse(BooleanUtils.or(new boolean[] { true, true, false }));
 }

 // Test for or method with Boolean array
 @Test
 public void testOrWithBooleanArrayBoolean() {
 try {
 BooleanUtils.or(new Boolean[] { Boolean.TRUE, null, Boolean.TRUE });
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for xor method with boolean array
 @Test
 public void testXorWithBooleanArray() {
 boolean[] arr = { false, true, false, true };
 boolean result = BooleanUtils.xor(arr);
 assertEquals(true, result);

 arr = new boolean[] { true, true, true, true };
 result = BooleanUtils.xor(arr);
 assertEquals(false, result);
 }

 // Test for xor method with Boolean array
 @Test
 public void testXorWithBooleanArrayBoolean() {
 try {
 BooleanUtils.xor(new Boolean[] { Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE });
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}