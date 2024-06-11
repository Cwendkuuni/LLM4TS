package org.junit.jupiter.api;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.lang3.BooleanUtils;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 final Boolean result = BooleanUtils.negate(bool);
 assertFalse(result.booleanValue());
 final Boolean nullInput = null;
 final Boolean resultNullInput = BooleanUtils.negate(nullInput);
 assertNull(resultNullInput);
 }

 @Test
 public void testIsTrue() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isTrue(boolTrue));
 assertFalse(BooleanUtils.isTrue(boolFalse));
 }

 @Test
 public void testIsNotTrue() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(boolFalse));
 assertFalse(BooleanUtils.isNotFalse(boolTrue));
 assertFalse(BooleanUtils.isNotTrue(boolTrue));
 assertTrue(BooleanUtils.isNotTrue(boolFalse));
 }

 @Test
 public void testIsFalse() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertFalse(BooleanUtils.isFalse(boolTrue));
 assertTrue(BooleanUtils.isFalse(boolFalse));
 }

 @Test
 public void testIsNotFalse() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotFalse(boolTrue));
 assertFalse(BooleanUtils.isNotFalse(boolFalse));
 }

 @Test
 public void testToBoolean() {
 final Boolean bool = Boolean.FALSE;
 final boolean result = BooleanUtils.toBoolean(bool);
 assertFalse(result);
 final Integer intValue = Integer.valueOf(0);
 final boolean resultIntValue = BooleanUtils.toBoolean(intValue);
 assertTrue(resultIntValue);
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 final Boolean bool = Boolean.FALSE;
 final boolean defaultValue = true;
 final boolean result = BooleanUtils.toBooleanDefaultIfNull(bool, defaultValue);
 assertFalse(result);
 final Boolean nullInput = null;
 final boolean defaultValueInput = true;
 final boolean resultNullInput = BooleanUtils.toBooleanDefaultIfNull(nullInput, defaultValueInput);
 assertTrue(resultNullInput);
 }

 @Test
 public void testToBooleanWithInt() {
 final int intValue = 1;
 final boolean result = BooleanUtils.toBoolean(intValue);
 assertTrue(result);
 final int intFalseValue = 0;
 final boolean resultIntFalseValue = BooleanUtils.toBoolean(intFalseValue);
 assertFalse(resultIntFalseValue);
 }

 @Test
 public void testToBooleanObjectWithInt() {
 final int intValue = 1;
 final Integer intWrapper = Integer.valueOf(intValue);
 final Boolean result = BooleanUtils.toBooleanObject(intValue);
 assertTrue(result.booleanValue());
 final Boolean resultIntWrapper = BooleanUtils.toBooleanObject(intWrapper);
 assertTrue(resultIntWrapper.booleanValue());
 }

 @Test
 public void testToBooleanWithInts() {
 final int intTrueValue = 1;
 final int intFalseValue = 0;
 final int input = intTrueValue;
 final boolean result = BooleanUtils.toBoolean(input, intTrueValue, intFalseValue);
 assertTrue(result);
 final int inputFalse = intFalseValue;
 final boolean resultFalse = BooleanUtils.toBoolean(inputFalse, intTrueValue, intFalseValue);
 assertFalse(resultFalse);
 }

 @Test
 public void testToBooleanWithIntegerObjects() {
 final int intTrueValue = 1;
 final int intFalseValue = 0;
 final Integer intTrue = Integer.valueOf(intTrueValue);
 final Integer intFalse = Integer.valueOf(intFalseValue);
 final boolean result = BooleanUtils.toBoolean(intTrue, intTrue, intFalse);
 assertTrue(result);
 final Integer inputFalse = intFalse;
 final boolean resultFalse = BooleanUtils.toBoolean(inputFalse, intTrue, intFalse);
 assertFalse(resultFalse);
 }

 @Test
 public void testToBooleanObjectWithIntsAndNullValue() {
 final int intTrueValue = 1;
 final int intFalseValue = 0;
 final int nullValue = -1;
 final boolean result = BooleanUtils.toBooleanObject(intTrueValue, intTrueValue, intFalseValue, nullValue).booleanValue();
 assertTrue(result);
 final int inputFalse = intFalseValue;
 final boolean resultFalse = BooleanUtils.toBooleanObject(inputFalse, intTrueValue, intFalseValue, nullValue).booleanValue();
 assertFalse(resultFalse);
 final Integer nullInput = null;
 final boolean resultNullInput = BooleanUtils.toBooleanObject(nullInput, intTrueValue, intFalseValue, nullValue).booleanValue();
 assertFalse(resultNullInput);
 }

 @Test
 public void testToBooleanObjectWithIntegerObjectsAndNullValue() {
 final int intTrueValue = 1;
 final int intFalseValue = 0;
 final Integer nullValue = null;
 final boolean result = BooleanUtils.toBooleanObject(Integer.valueOf(intTrueValue), Integer.valueOf(intTrueValue), Integer.valueOf(intFalseValue), nullValue).booleanValue();
 assertTrue(result);
 final Integer inputFalse = Integer.valueOf(intFalseValue);
 final boolean resultFalse = BooleanUtils.toBooleanObject(inputFalse, Integer.valueOf(intTrueValue), Integer.valueOf(intFalseValue), nullValue).booleanValue();
 assertFalse(resultFalse);
 final Integer nullInput = null;
 final boolean resultNullInput = BooleanUtils.toBooleanObject(nullInput, Integer.valueOf(intTrueValue), Integer.valueOf(intFalseValue), nullValue).booleanValue();
 assertFalse(resultNullInput);
 }

 @Test
 public void testToBooleanWithString() {
 final String stringTrue = "true";
 final String stringFalse = "false";
 final boolean result = BooleanUtils.toBoolean(stringTrue);
 assertTrue(result);
 final boolean resultFalse = BooleanUtils.toBoolean(stringFalse);
 assertFalse(resultFalse);
 }

 @Test
 public void testToBooleanObjectWithString() {
 final String stringTrue = "true";
 final String stringFalse = "false";
 final String stringNull = null;
 final Boolean resultTrue = BooleanUtils.toBooleanObject(stringTrue);
 assertTrue(resultTrue.booleanValue());
 final Boolean resultFalse = BooleanUtils.toBooleanObject(stringFalse);
 assertFalse(resultFalse.booleanValue());
 final Boolean resultNull = BooleanUtils.toBooleanObject(stringNull);
 assertNull(resultNull);
 }

 @Test
 public void testToBooleanWithStringAndDefaultValues() {
 final String stringTrue = "true";
 final String stringFalse = "false";
 final String stringNull = null;
 final boolean defaultValueTrue = true;
 final boolean defaultValueFalse = false;
 final boolean resultTrue = BooleanUtils.toBoolean(stringTrue, defaultValueTrue, defaultValueFalse);
 assertTrue(resultTrue);
 final boolean resultFalse = BooleanUtils.toBoolean(stringFalse, defaultValueTrue, defaultValueFalse);
 assertFalse(resultFalse);
 final boolean resultNull = BooleanUtils.toBoolean(stringNull, defaultValueTrue, defaultValueFalse);
 assertTrue(defaultValueTrue);
 }

 @Test
 public void testToStringTrueFalse() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final String resultTrue = BooleanUtils.toStringTrueFalse(boolTrue);
 assertEquals("true", resultTrue);
 final String resultFalse = BooleanUtils.toStringTrueFalse(boolFalse);
 assertEquals("false", resultFalse);
 }

 @Test
 public void testToStringOnOff() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final String resultTrue = BooleanUtils.toStringOnOff(boolTrue);
 assertEquals("on", resultTrue);
 final String resultFalse = BooleanUtils.toStringOnOff(boolFalse);
 assertEquals("off", resultFalse);
 }

 @Test
 public void testToStringYesNo() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final String resultTrue = BooleanUtils.toStringYesNo(boolTrue);
 assertEquals("yes", resultTrue);
 final String resultFalse = BooleanUtils.toStringYesNo(boolFalse);
 assertEquals("no", resultFalse);
 }

 @Test
 public void testToStringWithDefaultValues() {
 final Boolean boolTrue = Boolean.TRUE;
 final Boolean boolFalse = Boolean.FALSE;
 final String trueString = "trueString";
 final String falseString = "falseString";
 final String nullString = null;
 final String resultTrue = BooleanUtils.toString(boolTrue, trueString, falseString);
 assertEquals(trueString, resultTrue);
 final String resultFalse = BooleanUtils.toString(boolFalse, trueString, falseString);
 assertEquals(falseString, resultFalse);
 final String resultNull = BooleanUtils.toString(null, trueString, falseString);
 assertEquals(nullString, resultNull);
 }

 @Test
 public void testAnd() {
 final boolean[] input = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
 final boolean result = BooleanUtils.and(input);
 assertTrue(result);
 final boolean[] inputFalse = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
 final boolean resultFalse = BooleanUtils.and(inputFalse);
 assertFalse(resultFalse);
 final boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(nullInput));
 }

 @Test
 public void testAndWithBooleans() {
 final Boolean[] input = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 final Boolean result = BooleanUtils.and(input);
 assertTrue(result.booleanValue());
 final Boolean[] inputFalse = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 final Boolean resultFalse = BooleanUtils.and(inputFalse);
 assertFalse(resultFalse.booleanValue());
 final Boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(nullInput));
 }

 @Test
 public void testOr() {
 final boolean[] input = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
 final boolean result = BooleanUtils.or(input);
 assertTrue(result);
 final boolean[] inputFalse = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
 final boolean resultFalse = BooleanUtils.or(inputFalse);
 assertFalse(resultFalse);
 final boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(nullInput));
 }

 @Test
 public void testOrWithBooleans() {
 final Boolean[] input = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 final Boolean result = BooleanUtils.or(input);
 assertTrue(result.booleanValue());
 final Boolean[] inputFalse = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 final Boolean resultFalse = BooleanUtils.or(inputFalse);
 assertFalse(resultFalse.booleanValue());
 final Boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(nullInput));
 }

 @Test
 public void testXor() {
 final boolean[] input = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
 final boolean result = BooleanUtils.xor(input);
 assertTrue(result);
 final boolean[] inputFalse = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
 final boolean resultFalse = BooleanUtils.xor(inputFalse);
 assertFalse(resultFalse);
 final boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(nullInput));
 }

 @Test
 public void testXorWithBooleans() {
 final Boolean[] input = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 final Boolean result = BooleanUtils.xor(input);
 assertTrue(result.booleanValue());
 final Boolean[] inputFalse = {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
 final Boolean resultFalse = BooleanUtils.xor(inputFalse);
 assertFalse(resultFalse.booleanValue());
 final Boolean[] nullInput = null;
 assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(nullInput));
 }
}