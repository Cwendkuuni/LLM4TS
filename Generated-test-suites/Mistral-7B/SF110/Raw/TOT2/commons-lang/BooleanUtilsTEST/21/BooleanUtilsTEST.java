package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test fornegatemethod
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.negate(bool);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = Boolean.TRUE;
 actual = BooleanUtils.negate(bool);
 assertEquals(expected, actual);

 bool = null;
 assertNull(BooleanUtils.negate(bool));
 }

 // Test forisTruemethod
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 // Test forisNotTruemethod
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));

 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 // Test forisFalsemethod
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));

 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 // Test forisNotFalsemethod
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));

 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 // Test fortoBooleanmethod
 @Test
 public void testToBoolean() {
 assertTrue(BooleanUtils.toBoolean(true));
 assertFalse(BooleanUtils.toBoolean(false));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithNull() {
 BooleanUtils.toBoolean(null);
 }

 // Test fortoBooleanDefaultIfNullmethod
 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));

 bool = null;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, true));
 }

 // Test fortoBoolean method with integer
 @Test
 public void testToBooleanWithInteger() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithNegativeInteger() {
 BooleanUtils.toBoolean(-1);
 }

 // Test fortoBooleanObject method
 @Test
 public void testToBooleanObject() {
 assertTrue(BooleanUtils.toBooleanObject(Boolean.TRUE));
 assertFalse(BooleanUtils.toBooleanObject(Boolean.FALSE));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test fortoBooleanObject methods with integer
 @Test
 public void testToBooleanObjectWithInteger() {
 assertTrue(BooleanUtils.toBooleanObject(1));
 assertFalse(BooleanUtils.toBooleanObject(0));
 assertNull(BooleanUtils.toBooleanObject(-1));
 }

 // Test fortoBoolean method with integer and specified values
 @Test
 public void testToBooleanWithIntegerSpecifiedValues() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertFalse(BooleanUtils.toBoolean(2, 1, 0));
 }

 // Test fortoBooleanObject method with integer and specified values
 @Test
 public void testToBooleanObjectWithIntegerSpecifiedValues() {
 assertTrue(BooleanUtils.toBooleanObject(1, 1, 0, 0));
 assertFalse(BooleanUtils.toBooleanObject(0, 1, 0, 0));
 assertNull(BooleanUtils.toBooleanObject(2, 1, 0, 0));
 }

 // Test fortoInteger method
 @Test
 public void testToInteger() {
 assertEquals(1, BooleanUtils.toInteger(true));
 assertEquals(0, BooleanUtils.toInteger(false));
 }

 // Test fortoIntegerObject method
 @Test
 public void testToIntegerObject() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 // Test fortoIntegerObject method with Boolean
 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 // Test fortoInteger method with boolean and specified values
 @Test
 public void testToIntegerWithBooleanSpecifiedValues() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 // Test fortoIntegerObject method with Boolean and specified values
 @Test
 public void testToIntegerObjectWithBooleanSpecifiedValues() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
 }

 // Test fortoBooleanObject method with String
 @Test
 public void testToBooleanObjectWithString() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test fortoBooleanObject method with String and specified values
 @Test
 public void testToBooleanObjectWithStringSpecifiedValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
 assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
 }

 // Test fortoBoolean method with String
 @Test
 public void testToBooleanWithString() {
 assertTrue(BooleanUtils.toBoolean("true"));
 assertFalse(BooleanUtils.toBoolean("false"));
 assertFalse(BooleanUtils.toBoolean(""));
 }

 // Test fortoBoolean method with String and specified values
 @Test
 public void testToBooleanWithStringSpecifiedValues() {
 assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
 assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
 assertFalse(BooleanUtils.toBoolean("", "true", "false"));
 }

 // Test fortoStringTrueFalse method
 @Test
 public void testToStringTrueFalse() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 // Test fortoStringOnOff method
 @Test
 public void testToStringOnOff() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 // Test fortoStringYesNo method
 @Test
 public void testToStringYesNo() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 // Test fortoString method with Boolean
 @Test
 public void testToStringWithBoolean() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE));
 }

 // Test fortoString method with Boolean and specified values
 @Test
 public void testToStringWithBooleanSpecifiedValues() {
 assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 // Test forand method with boolean array
 @Test
 public void testAndWithBooleanArray() {
 boolean[] booleanArray = { true, true, true, true };
 assertTrue(BooleanUtils.and(booleanArray));

 booleanArray = new boolean[] { false, true, true, true };
 assertFalse(BooleanUtils.and(booleanArray));

 booleanArray = new boolean[] { true, false, true, true };
 assertFalse(BooleanUtils.and(booleanArray));
 }

 // Test forand method with Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullBooleanArray() {
 BooleanUtils.and((Boolean[]) null);
 }

 // Test forand method with boolean array and specified values
 @Test
 public void testAndWithBooleanArraySpecifiedValues() {
 boolean[] booleanArray = { true, true, true, true };
 assertTrue(BooleanUtils.and(booleanArray, new boolean[] { true, true, true, true }));
 assertFalse(BooleanUtils.and(booleanArray, new boolean[] { false, true, true, true }));
 }

 // Test foror method with boolean array
 @Test
 public void testOrWithBooleanArray() {
 boolean[] booleanArray = { true, true, true, true };
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray = new boolean[] { false, true, true, true };
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray = new boolean[] { true, false, true, true };
 assertTrue(BooleanUtils.or(booleanArray));

 booleanArray = new boolean[] { false, false, false, false };
 assertFalse(BooleanUtils.or(booleanArray));
 }

 // Test foror method with Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testOrWithNullBooleanArray() {
 BooleanUtils.or((Boolean[]) null);
 }

 // Test foror method with boolean array and specified values
 @Test
 public void testOrWithBooleanArraySpecifiedValues() {
 boolean[] booleanArray = { true, true, true, true };
 assertTrue(BooleanUtils.or(booleanArray, new boolean[] { true, true, true, true }));
 assertTrue(BooleanUtils.or(booleanArray, new boolean[] { false, true, true, true }));
 }

 // Test forxor method with boolean array
 @Test
 public void testXorWithBooleanArray() {
 boolean[] booleanArray = { true, true, true, true };
 assertEquals(false, BooleanUtils.xor(booleanArray));

 booleanArray = new boolean[] { false, true, true, true };
 assertEquals(true, BooleanUtils.xor(booleanArray));

 booleanArray = new boolean[] { true, false, true, true };
 assertEquals(true, BooleanUtils.xor(booleanArray));
 }

 // Test forxor method with Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testXorWithNullBooleanArray() {
 BooleanUtils.xor((Boolean[]) null);
 }

 // Test forxor method with boolean array and specified values
 @Test
 public void testXorWithBooleanArraySpecifiedValues() {
 boolean[] booleanArray = { true, true, true, true };
 assertEquals(false, BooleanUtils.xor(booleanArray, new boolean[] { true, true, true, true }));
 assertEquals(true, BooleanUtils.xor(booleanArray, new boolean[] { false, true, true, true }));
 }
}