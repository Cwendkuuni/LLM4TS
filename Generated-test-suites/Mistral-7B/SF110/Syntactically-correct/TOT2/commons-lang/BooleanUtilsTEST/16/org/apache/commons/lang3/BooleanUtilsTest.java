import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.negate(bool);
 assertEquals(expected, actual);

 bool = null;
 assertNull(BooleanUtils.negate(bool));
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 boolean expected = true;
 boolean actual = BooleanUtils.isTrue(bool);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = false;
 actual = BooleanUtils.isTrue(bool);
 assertEquals(expected, actual);

 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 boolean expected = false;
 boolean actual = BooleanUtils.isNotTrue(bool);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = true;
 actual = BooleanUtils.isNotTrue(bool);
 assertEquals(expected, actual);

 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 boolean expected = true;
 boolean actual = BooleanUtils.isFalse(bool);
 assertEquals(expected, actual);

 bool = Boolean.TRUE;
 expected = false;
 actual = BooleanUtils.isFalse(bool);
 assertEquals(expected, actual);

 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 boolean expected = false;
 boolean actual = BooleanUtils.isNotFalse(bool);
 assertEquals(expected, actual);

 bool = Boolean.TRUE;
 expected = true;
 actual = BooleanUtils.isNotFalse(bool);
 assertEquals(expected, actual);

 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 // Test for toBoolean method
 @Test
 public void testToBoolean() {
 Boolean bool = Boolean.TRUE;
 boolean expected = true;
 boolean actual = BooleanUtils.toBoolean(bool);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = false;
 actual = BooleanUtils.toBoolean(bool);
 assertEquals(expected, actual);

 bool = null;
 assertFalse(BooleanUtils.toBoolean(bool));
 }

 // Test for toBooleanDefaultIfNull method
 @Test
 public void testToBooleanDefaultIfNull() {
 Boolean bool = Boolean.TRUE;
 boolean expected = true;
 boolean actual = BooleanUtils.toBooleanDefaultIfNull(bool, false);
 assertEquals(expected, actual);

 bool = Boolean.FALSE;
 expected = false;
 actual = BooleanUtils.toBooleanDefaultIfNull(bool, true);
 assertEquals(expected, actual);

 bool = null;
 expected = false;
 actual = BooleanUtils.toBooleanDefaultIfNull(null, false);
 assertEquals(expected, actual);

 bool = null;
 expected = true;
 actual = BooleanUtils.toBooleanDefaultIfNull(null, true);
 assertEquals(expected, actual);
 }

 // Test for toBoolean method with int value
 @Test
 public void testToBooleanWithIntValue() {
 int value = 1;
 boolean expected = true;
 boolean actual = BooleanUtils.toBoolean(value);
 assertEquals(expected, actual);

 value = 0;
 expected = false;
 actual = BooleanUtils.toBoolean(value);
 assertEquals(expected, actual);

 value = 2;
 try {
 BooleanUtils.toBoolean(value);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 // Test for toBooleanObject method with int value
 @Test
 public void testToBooleanObjectWithIntValue() {
 int value = 1;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(value);
 assertEquals(expected, actual);

 value = 0;
 expected = Boolean.FALSE;
 actual = BooleanUtils.toBooleanObject(value);
 assertEquals(expected, actual);

 value = 2;
 try {
 BooleanUtils.toBooleanObject(value);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 // Test for toBoolean method with int values and default values
 @Test
 public void testToBooleanWithIntValuesAndDefaultValues() {
 int trueValue = 1;
 int falseValue = 0;
 int nullValue = -1;
 int value = 1;
 boolean expected = true;
 boolean actual = BooleanUtils.toBoolean(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = 0;
 expected = false;
 actual = BooleanUtils.toBoolean(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = null;
 expected = false;
 actual = BooleanUtils.toBoolean(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = 2;
 try {
 BooleanUtils.toBoolean(value, trueValue, falseValue, nullValue);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 // Test for toBooleanObject method with int values and default values
 @Test
 public void testToBooleanObjectWithIntValuesAndDefaultValues() {
 int trueValue = 1;
 int falseValue = 0;
 int nullValue = -1;
 int value = 1;
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = 0;
 expected = Boolean.FALSE;
 actual = BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = null;
 expected = null;
 actual = BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
 assertEquals(expected, actual);

 value = 2;
 try {
 BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 // Test for and method with boolean array
 @Test
 public void testAnd() {
 boolean[] array = {true, true, true};
 boolean expected = true;
 boolean actual = BooleanUtils.and(array);
 assertEquals(expected, actual);

 array = new boolean[] {true, false, true};
 expected = false;
 actual = BooleanUtils.and(array);
 assertEquals(expected, actual);

 array = new boolean[0];
 try {
 BooleanUtils.and(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test for and method with Boolean array
 @Test
 public void testAndWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.and(array);
 assertEquals(expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.FALSE;
 actual = BooleanUtils.and(array);
 assertEquals(expected, actual);

 array = new Boolean[0];
 try {
 BooleanUtils.and(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test for or method with boolean array
 @Test
 public void testOr() {
 boolean[] array = {true, true, true};
 boolean expected = true;
 boolean actual = BooleanUtils.or(array);
 assertEquals(expected, actual);

 array = new boolean[] {true, false, true};
 expected = true;
 actual = BooleanUtils.or(array);
 assertEquals(expected, actual);

 array = new boolean[0];
 try {
 BooleanUtils.or(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test for or method with Boolean array
 @Test
 public void testOrWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean actual = BooleanUtils.or(array);
 assertEquals(expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.or(array);
 assertEquals(expected, actual);

 array = new Boolean[0];
 try {
 BooleanUtils.or(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test for xor method with boolean array
 @Test
 public void testXor() {
 boolean[] array = {true, true, true};
 boolean expected = false;
 boolean actual = BooleanUtils.xor(array);
 assertEquals(expected, actual);

 array = new boolean[] {true, true, false};
 expected = true;
 actual = BooleanUtils.xor(array);
 assertEquals(expected, actual);

 array = new boolean[0];
 try {
 BooleanUtils.xor(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }

 // Test for xor method with Boolean array
 @Test
 public void testXorWithBooleanArray() {
 Boolean[] array = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.FALSE;
 Boolean actual = BooleanUtils.xor(array);
 assertEquals(expected, actual);

 array = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
 expected = Boolean.TRUE;
 actual = BooleanUtils.xor(array);
 assertEquals(expected, actual);

 array = new Boolean[0];
 try {
 BooleanUtils.xor(array);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be null", ex.getMessage());
 }
 }
}