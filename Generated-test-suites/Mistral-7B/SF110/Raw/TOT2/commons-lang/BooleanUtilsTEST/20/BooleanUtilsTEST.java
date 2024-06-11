package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test case for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.negate(bool);
 assertEquals("negation of true is false", expected, actual);

 bool = Boolean.FALSE;
 expected = Boolean.TRUE;
 actual = BooleanUtils.negate(bool);
 assertEquals("negation of false is true", expected, actual);

 bool = null;
 expected = null;
 actual = BooleanUtils.negate(bool);
 assertEquals("negation of null is null", expected, actual);
 }

 // Test case for isTrue method
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 boolean expected = true;
 boolean actual = BooleanUtils.isTrue(bool);
 assertEquals("isTrue of true is true", expected, actual);

 bool = Boolean.FALSE;
 expected = false;
 actual = BooleanUtils.isTrue(bool);
 assertEquals("isTrue of false is false", expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.isTrue(bool);
 assertEquals("isTrue of null is false", expected, actual);
 }

 // Test case for isNotTrue method
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 boolean expected = false;
 boolean actual = BooleanUtils.isNotTrue(bool);
 assertEquals("isNotTrue of true is false", expected, actual);

 bool = Boolean.FALSE;
 expected = true;
 actual = BooleanUtils.isNotTrue(bool);
 assertEquals("isNotTrue of false is true", expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.isNotTrue(bool);
 assertEquals("isNotTrue of null is false", expected, actual);
 }

 // Test case for isFalse method
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 boolean expected = true;
 boolean actual = BooleanUtils.isFalse(bool);
 assertEquals("isFalse of false is true", expected, actual);

 bool = Boolean.TRUE;
 expected = false;
 actual = BooleanUtils.isFalse(bool);
 assertEquals("isFalse of true is false", expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.isFalse(bool);
 assertEquals("isFalse of null is false", expected, actual);
 }

 // Test case for isNotFalse method
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 boolean expected = false;
 boolean actual = BooleanUtils.isNotFalse(bool);
 assertEquals("isNotFalse of false is false", expected, actual);

 bool = Boolean.TRUE;
 expected = true;
 actual = BooleanUtils.isNotFalse(bool);
 assertEquals("isNotFalse of true is true", expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.isNotFalse(bool);
 assertEquals("isNotFalse of null is false", expected, actual);
 }

 // Test case for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 Boolean bool = Boolean.TRUE;
 boolean expected = true;
 boolean actual = BooleanUtils.toBoolean(bool);
 assertEquals("toBoolean of true is true", expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.toBoolean(bool);
 assertEquals("toBoolean of null is false", expected, actual);
 }

 // Test case for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 int value = 1;
 boolean expected = true;
 boolean actual = BooleanUtils.toBoolean(value);
 assertEquals("toBoolean of non-zero int is true", expected, actual);

 value = 0;
 expected = false;
 actual = BooleanUtils.toBoolean(value);
 assertEquals("toBoolean of zero int is false", expected, actual);
 }

 // Test case for toBooleanObject method with Boolean argument
 @Test
 public void testToBooleanObjectWithBoolean() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(bool);
 assertEquals("toBooleanObject of true is true", expected, actual);

 bool = null;
 assertNull("toBooleanObject of null is null", actual);
 }

 // Test case for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 int value = 1;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(value);
 assertEquals("toBooleanObject of non-zero int is true", expected, actual);

 value = 0;
 assertNull("toBooleanObject of zero int is null", actual);
 }

 // Test case for toBoolean method with int and values
 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanWithIntAndValues() {
 int value = 2;
 Boolean trueValue = Boolean.TRUE;
 Boolean falseValue = Boolean.FALSE;
 try {
 BooleanUtils.toBoolean(value, trueValue, falseValue);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 // Test case for toBooleanObject method with int and values
 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectWithIntAndValues() {
 int value = 2;
 Boolean trueValue = Boolean.TRUE;
 Boolean falseValue = Boolean.FALSE;
 try {
 BooleanUtils.toBooleanObject(value, trueValue, falseValue, null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 // Test case for toInteger method with Boolean argument
 @Test
 public void testToIntegerWithBoolean() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toInteger(bool);
 assertEquals("toInteger of true is 1", expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toInteger(bool);
 assertEquals("toInteger of false is 0", expected, actual);
 }

 // Test case for toIntegerObject method with Boolean argument
 @Test
 public void testToIntegerObjectWithBoolean() {
 Boolean bool = Boolean.TRUE;
 Integer expected = NumberUtils.INTEGER_ONE;
 Integer actual = BooleanUtils.toIntegerObject(bool);
 assertEquals("toIntegerObject of true is NumberUtils.INTEGER_ONE", expected, actual);

 bool = Boolean.FALSE;
 expected = NumberUtils.INTEGER_ZERO;
 actual = BooleanUtils.toIntegerObject(bool);
 assertEquals("toIntegerObject of false is NumberUtils.INTEGER_ZERO", expected, actual);
 }

 // Test case for toStringTrueFalse method with Boolean argument
 @Test
 public void testToStringTrueFalseWithBoolean() {
 Boolean bool = Boolean.TRUE;
 String expected = "true";
 String actual = BooleanUtils.toStringTrueFalse(bool);
 assertEquals("toStringTrueFalse of true is 'true'", expected, actual);

 bool = Boolean.FALSE;
 expected = "false";
 actual = BooleanUtils.toStringTrueFalse(bool);
 assertEquals("toStringTrueFalse of false is 'false'", expected, actual);
 }

 // Test case for toStringOnOff method with Boolean argument
 @Test
 public void testToStringOnOffWithBoolean() {
 Boolean bool = Boolean.TRUE;
 String expected = "on";
 String actual = BooleanUtils.toStringOnOff(bool);
 assertEquals("toStringOnOff of true is 'on'", expected, actual);

 bool = Boolean.FALSE;
 expected = "off";
 actual = BooleanUtils.toStringOnOff(bool);
 assertEquals("toStringOnOff of false is 'off'", expected, actual);
 }

 // Test case for toStringYesNo method with Boolean argument
 @Test
 public void testToStringYesNoWithBoolean() {
 Boolean bool = Boolean.TRUE;
 String expected = "yes";
 String actual = BooleanUtils.toStringYesNo(bool);
 assertEquals("toStringYesNo of true is 'yes'", expected, actual);

 bool = Boolean.FALSE;
 expected = "no";
 actual = BooleanUtils.toStringYesNo(bool);
 assertEquals("toStringYesNo of false is 'no'", expected, actual);
 }

 // Test case for and method with Boolean array
 @Test
 public void testAndWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 boolean expected = true;
 boolean actual = BooleanUtils.and(array);
 assertEquals("and of [true, true, true] is true", expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
 expected = false;
 actual = BooleanUtils.and(array);
 assertEquals("and of [true, true, false] is false", expected, actual);
 }

 // Test case for and method with Boolean array and primitive conversion
 @Test(expected = IllegalArgumentException.class)
 public void testAndWithBooleanArrayAndPrimitiveConversion() {
 boolean[] primitive = {true, true, true};
 try {
 BooleanUtils.and(primitive);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test case for or method with Boolean array
 @Test
 public void testOrWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 boolean expected = true;
 boolean actual = BooleanUtils.or(array);
 assertEquals("or of [true, true, true] is true", expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
 expected = true;
 actual = BooleanUtils.or(array);
 assertEquals("or of [true, true, false] is true", expected, actual);

 array = new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 expected = false;
 actual = BooleanUtils.or(array);
 assertEquals("or of [false, false, false] is false", expected, actual);
 }

 // Test case for or method with Boolean array and primitive conversion
 @Test(expected = IllegalArgumentException.class)
 public void testOrWithBooleanArrayAndPrimitiveConversion() {
 boolean[] primitive = {true, true, true};
 try {
 BooleanUtils.or(primitive);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test case for xor method with Boolean array
 @Test
 public void testXorWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 boolean expected = false;
 boolean actual = BooleanUtils.xor(array);
 assertEquals("xor of [true, true, true] is false", expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 expected = true;
 actual = BooleanUtils.xor(array);
 assertEquals("xor of [true, false, true] is true", expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
 expected = true;
 actual = BooleanUtils.xor(array);
 assertEquals("xor of [true, true, false] is true", expected, actual);
 }

 // Test case for xor method with Boolean array and primitive conversion
 @Test(expected = IllegalArgumentException.class)
 public void testXorWithBooleanArrayAndPrimitiveConversion() {
 boolean[] primitive = {true, true, true};
 try {
 BooleanUtils.xor(primitive);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }
}