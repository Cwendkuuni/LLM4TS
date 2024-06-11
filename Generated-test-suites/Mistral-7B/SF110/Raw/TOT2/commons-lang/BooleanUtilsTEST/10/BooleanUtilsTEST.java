import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test case for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.negate(bool);
 assertEquals(expected, actual);

 bool = null;
 assertEquals(null, BooleanUtils.negate(bool));
 }

 // Test case for isTrue method
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 // Test case for isNotTrue method
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));

 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(bool));
 }

 // Test case for isFalse method
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));

 bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 // Test case for isNotFalse method
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));

 bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(bool));
 }

 // Test case for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBoolean(bool));

 bool = Boolean.FALSE;
 assertFalse(BooleanUtils.toBoolean(bool));

 bool = null;
 assertFalse(BooleanUtils.toBoolean(bool));
 }

 // Test case for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 int value = 1;
 assertTrue(BooleanUtils.toBoolean(value));

 value = 0;
 assertFalse(BooleanUtils.toBoolean(value));
 }

 // Test case for toBooleanDefaultIfNull method with Boolean argument
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(bool, false));

 bool = null;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(bool, false));
 }

 // Test case for toBooleanDefaultIfNull method with int argument
 @Test
 public void testToBooleanDefaultIfNullWithInt() {
 int value = 1;
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(value, false));

 value = 0;
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(value, false));
 }

 // Test case for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 int value = 1;
 assertTrue(BooleanUtils.toBooleanObject(value).booleanValue());

 value = 0;
 assertFalse(BooleanUtils.toBooleanObject(value).booleanValue());

 value = -1;
 assertNull(BooleanUtils.toBooleanObject(value));
 }

 // Test case for toBooleanObject method with Integer argument
 @Test
 public void testToBooleanObjectWithInteger() {
 Integer value = Integer.valueOf(1);
 assertTrue(BooleanUtils.toBooleanObject(value).booleanValue());

 value = Integer.valueOf(0);
 assertFalse(BooleanUtils.toBooleanObject(value).booleanValue());

 value = null;
 assertNull(BooleanUtils.toBooleanObject(value));
 }

 // Test case for toBoolean method with int and default values
 @Test
 public void testToBooleanWithIntAndDefaultValues() {
 int value = 1;
 assertTrue(BooleanUtils.toBoolean(value, true, false, 0));

 value = 0;
 assertFalse(BooleanUtils.toBoolean(value, true, false, 0));

 value = 2;
 assertFalse(BooleanUtils.toBoolean(value, true, false, 0));
 }

 // Test case for toBoolean method with Integer and default values
 @Test
 public void testToBooleanWithIntegerAndDefaultValues() {
 Integer value = Integer.valueOf(1);
 assertTrue(BooleanUtils.toBoolean(value, true, false, null));

 value = Integer.valueOf(0);
 assertFalse(BooleanUtils.toBoolean(value, true, false, null));

 value = null;
 assertFalse(BooleanUtils.toBoolean(value, true, false, null));
 }

 // Test case for toBooleanObject method with int, trueValue, falseValue, and nullValue
 @Test
 public void testToBooleanObjectWithIntAndValues() {
 int trueValue = 1;
 int falseValue = 0;
 int nullValue = -1;
 int value = trueValue;
 assertTrue(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue).booleanValue());

 value = falseValue;
 assertFalse(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue).booleanValue());

 value = nullValue;
 assertNull(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue));
 }

 // Test case for toBooleanObject method with Integer, trueValue, falseValue, and nullValue
 @Test
 public void testToBooleanObjectWithIntegerAndValues() {
 Integer trueValue = Integer.valueOf(1);
 Integer falseValue = Integer.valueOf(0);
 Integer nullValue = null;
 Integer value = trueValue;
 assertTrue(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue).booleanValue());

 value = falseValue;
 assertFalse(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue).booleanValue());

 value = null;
 assertNull(BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue));
 }

 // Test case for toInteger method with Boolean argument
 @Test
 public void testToIntegerWithBoolean() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toInteger(bool);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toInteger(bool);
 assertEquals(expected, actual);
 }

 // Test case for toIntegerObject method with Boolean argument
 @Test
 public void testToIntegerObjectWithBoolean() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toIntegerObject(bool).intValue();
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toIntegerObject(bool).intValue();
 assertEquals(expected, actual);
 }

 // Test case for toInteger method with Boolean and default values
 @Test
 public void testToIntegerWithBooleanAndDefaultValues() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toInteger(bool, 1, 0);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toInteger(bool, 1, 0);
 assertEquals(expected, actual);
 }

 // Test case for toInteger method with Boolean and nullable default values
 @Test
 public void testToIntegerWithBooleanAndNullableDefaultValues() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toInteger(bool, null, null);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toInteger(bool, null, null);
 assertEquals(expected, actual);
 }

 // Test case for toIntegerObject method with Boolean and default values
 @Test
 public void testToIntegerObjectWithBooleanAndDefaultValues() {
 Boolean bool = Boolean.TRUE;
 int expected = 1;
 int actual = BooleanUtils.toIntegerObject(bool, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO).intValue();
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = 0;
 actual = BooleanUtils.toIntegerObject(bool, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO).intValue();
 assertEquals(expected, actual);
 }

 // Test case for toIntegerObject method with Boolean and nullable default values
 @Test
 public void testToIntegerObjectWithBooleanAndNullableDefaultValues() {
 Boolean bool = Boolean.TRUE;
 Integer expected = NumberUtils.INTEGER_ONE;
 Integer actual = BooleanUtils.toIntegerObject(bool, null, null);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = NumberUtils.INTEGER_ZERO;
 actual = BooleanUtils.toIntegerObject(bool, null, null);
 assertEquals(expected, actual);
 }

 // Test case for and method with Boolean array
 @Test
 public void testAndWithBooleanArray() {
 Boolean[] arr = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(arr));

 arr[0] = Boolean.FALSE;
 assertFalse(BooleanUtils.and(arr));
 }

 // Test case for and method with null Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullBooleanArray() {
 Boolean[] arr = null;
 BooleanUtils.and(arr);
 }

 // Test case for and method with empty Boolean array
 @Test
 public void testAndWithEmptyBooleanArray() {
 Boolean[] arr = {};
 assertFalse(BooleanUtils.and(arr));
 }

 // Test case for or method with Boolean array
 @Test
 public void testOrWithBooleanArray() {
 Boolean[] arr = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(arr));

 arr[0] = Boolean.FALSE;
 assertTrue(BooleanUtils.or(arr));
 }

 // Test case for or method with null Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testOrWithNullBooleanArray() {
 Boolean[] arr = null;
 BooleanUtils.or(arr);
 }

 // Test case for or method with empty Boolean array
 @Test
 public void testOrWithEmptyBooleanArray() {
 Boolean[] arr = {};
 assertFalse(BooleanUtils.or(arr));
 }

 // Test case for xor method with Boolean array
 @Test
 public void testXorWithBooleanArray() {
 Boolean[] arr = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean result = Boolean.FALSE;
 BooleanUtils.xor(arr, result);
 assertFalse(result.equals(Boolean.TRUE));
 assertFalse(result.equals(Boolean.FALSE));

 result = Boolean.TRUE;
 BooleanUtils.xor(arr, result);
 assertTrue(result.equals(Boolean.TRUE));
 }

 // Test case for xor method with null Boolean array
 @Test(expected = IllegalArgumentException.class)
 public void testXorWithNullBooleanArray() {
 Boolean[] arr = null;
 BooleanUtils.xor(arr, null);
 }

 // Test case for xor method with empty Boolean array
 @Test
 public void testXorWithEmptyBooleanArray() {
 Boolean[] arr = {};
 Boolean result = Boolean.FALSE;
 BooleanUtils.xor(arr, result);
 assertFalse(result.equals(Boolean.TRUE));
 assertFalse(result.equals(Boolean.FALSE));
 }
}