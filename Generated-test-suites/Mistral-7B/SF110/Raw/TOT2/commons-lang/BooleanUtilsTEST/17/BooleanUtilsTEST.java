package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 final Boolean bool = Boolean.TRUE;
 final Boolean expected = Boolean.FALSE;
 final Boolean actual = BooleanUtils.negate(bool);
 assertTrue(expected.equals(actual));
 final Boolean nullBool = null;
 final Boolean expectedNull = null;
 final Boolean actualNull = BooleanUtils.negate(nullBool);
 assertEquals(expectedNull, actualNull);
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean result = BooleanUtils.isTrue(bool);
 assertTrue(result);
 final Boolean falseBool = Boolean.FALSE;
 final boolean resultFalse = BooleanUtils.isTrue(falseBool);
 assertFalse(resultFalse);
 final Boolean nullBool = null;
 final boolean resultNull = BooleanUtils.isTrue(nullBool);
 assertFalse(resultNull);
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 final Boolean bool = Boolean.TRUE;
 final boolean result = BooleanUtils.isNotTrue(bool);
 assertFalse(result);
 final Boolean falseBool = Boolean.FALSE;
 final boolean resultFalse = BooleanUtils.isNotTrue(falseBool);
 assertTrue(resultFalse);
 final Boolean nullBool = null;
 final boolean resultNull = BooleanUtils.isNotTrue(nullBool);
 assertTrue(resultNull);
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean result = BooleanUtils.isFalse(bool);
 assertTrue(result);
 final Boolean trueBool = Boolean.TRUE;
 final boolean resultFalse = BooleanUtils.isFalse(trueBool);
 assertFalse(resultFalse);
 final Boolean nullBool = null;
 final boolean resultNull = BooleanUtils.isFalse(nullBool);
 assertFalse(resultNull);
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 final Boolean bool = Boolean.FALSE;
 final boolean result = BooleanUtils.isNotFalse(bool);
 assertFalse(result);
 final Boolean trueBool = Boolean.TRUE;
 final boolean resultTrue = BooleanUtils.isNotFalse(trueBool);
 assertTrue(resultTrue);
 final Boolean nullBool = null;
 final boolean resultNull = BooleanUtils.isNotFalse(nullBool);
 assertTrue(resultNull);
 }

 // Test for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 final Boolean bool = Boolean.TRUE;
 final boolean expected = true;
 final boolean actual = BooleanUtils.toBoolean(bool);
 assertEquals(expected, actual);
 final Boolean falseBool = Boolean.FALSE;
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.toBoolean(falseBool);
 assertEquals(expectedFalse, actualFalse);
 final Boolean nullBool = null;
 final boolean expectedNull = false;
 final boolean actualNull = BooleanUtils.toBoolean(nullBool);
 assertEquals(expectedNull, actualNull);
 }

 // Test for toBoolean method with integer argument
 @Test
 public void testToBooleanWithInteger() {
 final int integerValue = 1;
 final boolean expected = true;
 final boolean actual = BooleanUtils.toBoolean(integerValue);
 assertEquals(expected, actual);
 final int falseIntegerValue = 0;
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.toBoolean(falseIntegerValue);
 assertEquals(expectedFalse, actualFalse);
 }

 // Test for toBooleanObject method with Boolean argument
 @Test
 public void testToBooleanObjectWithBoolean() {
 final Boolean bool = Boolean.TRUE;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.toBooleanObject(bool);
 assertEquals(expected, actual);
 final Boolean falseBool = Boolean.FALSE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.toBooleanObject(falseBool);
 assertEquals(expectedFalse, actualFalse);
 final Boolean nullBool = null;
 final Boolean expectedNull = null;
 final Boolean actualNull = BooleanUtils.toBooleanObject(nullBool);
 assertEquals(expectedNull, actualNull);
 }

 // Test for toBooleanObject method with integer argument
 @Test
 public void testToBooleanObjectWithInteger() {
 final int integerValue = 1;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.toBooleanObject(integerValue);
 assertEquals(expected, actual);
 final int falseIntegerValue = 0;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.toBooleanObject(falseIntegerValue);
 assertEquals(expectedFalse, actualFalse);
 final int nullIntegerValue = 0;
 final Boolean expectedNull = null;
 final Boolean actualNull = BooleanUtils.toBooleanObject(nullIntegerValue);
 assertEquals(expectedNull, actualNull);
 }

 // Test for toBoolean method with int, int, int arguments
 @Test
 public void testToBooleanWithIntIntInt() {
 final int trueIntegerValue = 1;
 final boolean expected = true;
 final boolean actual = BooleanUtils.toBoolean(trueIntegerValue, 1, 0);
 assertEquals(expected, actual);
 final int falseIntegerValue = 0;
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.toBoolean(falseIntegerValue, 1, 0);
 assertEquals(expectedFalse, actualFalse);
 final int nullIntegerValue = -1;
 try {
 BooleanUtils.toBoolean(nullIntegerValue, 1, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Integer did not match either specified value", e.getMessage());
 }
 }

 // Test for toBoolean method with Integer, Integer, Integer arguments
 @Test
 public void testToBooleanWithIntegerIntegerInteger() {
 final Integer trueInteger = Integer.valueOf(1);
 final boolean expected = true;
 final boolean actual = BooleanUtils.toBoolean(trueInteger, Integer.valueOf(1), Integer.valueOf(0));
 assertEquals(expected, actual);
 final Integer falseInteger = Integer.valueOf(0);
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.toBoolean(falseInteger, Integer.valueOf(1), Integer.valueOf(0));
 assertEquals(expectedFalse, actualFalse);
 final Integer nullInteger = null;
 try {
 BooleanUtils.toBoolean(nullInteger, Integer.valueOf(1), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Integer did not match either specified value", e.getMessage());
 }
 }

 // Test for toBooleanObject method with int, int, int, int arguments
 @Test
 public void testToBooleanObjectWithIntIntIntInt() {
 final int trueIntegerValue = 1;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.toBooleanObject(trueIntegerValue, 1, 0, 0);
 assertEquals(expected, actual);
 final int falseIntegerValue = 0;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.toBooleanObject(falseIntegerValue, 1, 0, 0);
 assertEquals(expectedFalse, actualFalse);
 final int nullIntegerValue = -1;
 try {
 BooleanUtils.toBooleanObject(nullIntegerValue, 1, 0, 0);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Integer did not match any specified value", e.getMessage());
 }
 }

 // Test for toBooleanObject method with Integer, Integer, Integer, Integer arguments
 @Test
 public void testToBooleanObjectWithIntegerIntegerIntegerInteger() {
 final Integer trueInteger = Integer.valueOf(1);
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.toBooleanObject(trueInteger, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
 assertEquals(expected, actual);
 final Integer falseInteger = Integer.valueOf(0);
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.toBooleanObject(falseInteger, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
 assertEquals(expectedFalse, actualFalse);
 final Integer nullInteger = null;
 try {
 BooleanUtils.toBooleanObject(nullInteger, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Integer did not match any specified value", e.getMessage());
 }
 }

 // Test for and method with Boolean arguments
 @Test
 public void testAndWithBoolean() {
 final Boolean bool1 = Boolean.TRUE;
 final Boolean bool2 = Boolean.TRUE;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.and(bool1, bool2);
 assertEquals(expected, actual);
 final Boolean bool1False = Boolean.FALSE;
 final Boolean bool2False = Boolean.FALSE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.and(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final Boolean bool1Null = null;
 try {
 BooleanUtils.and(bool1Null, bool2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 // Test for and method with boolean arguments
 @Test
 public void testAndWithBooleanBoolean() {
 final boolean bool1 = true;
 final boolean bool2 = true;
 final boolean expected = true;
 final boolean actual = BooleanUtils.and(bool1, bool2);
 assertEquals(expected, actual);
 final boolean bool1False = false;
 final boolean bool2False = false;
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.and(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final boolean bool1Null = false;
 try {
 BooleanUtils.and(bool1, bool1Null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 // Test for or method with Boolean arguments
 @Test
 public void testOrWithBoolean() {
 final Boolean bool1 = Boolean.TRUE;
 final Boolean bool2 = Boolean.TRUE;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.or(bool1, bool2);
 assertEquals(expected, actual);
 final Boolean bool1False = Boolean.FALSE;
 final Boolean bool2False = Boolean.FALSE;
 final Boolean expectedFalse = Boolean.FALSE;
 final Boolean actualFalse = BooleanUtils.or(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final Boolean bool1Null = null;
 try {
 BooleanUtils.or(bool1Null, bool2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 // Test for or method with boolean arguments
 @Test
 public void testOrWithBooleanBoolean() {
 final boolean bool1 = true;
 final boolean bool2 = true;
 final boolean expected = true;
 final boolean actual = BooleanUtils.or(bool1, bool2);
 assertEquals(expected, actual);
 final boolean bool1False = false;
 final boolean bool2False = false;
 final boolean expectedFalse = false;
 final boolean actualFalse = BooleanUtils.or(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final boolean bool1Null = false;
 try {
 BooleanUtils.or(bool1, bool1Null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 // Test for xor method with Boolean arguments
 @Test
 public void testXorWithBoolean() {
 final Boolean bool1 = Boolean.TRUE;
 final Boolean bool2 = Boolean.FALSE;
 final Boolean expected = Boolean.TRUE;
 final Boolean actual = BooleanUtils.xor(bool1, bool2);
 assertEquals(expected, actual);
 final Boolean bool1False = Boolean.FALSE;
 final Boolean bool2False = Boolean.TRUE;
 final Boolean expectedFalse = Boolean.TRUE;
 final Boolean actualFalse = BooleanUtils.xor(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final Boolean bool1Null = null;
 try {
 BooleanUtils.xor(bool1, bool1Null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 // Test for xor method with boolean arguments
 @Test
 public void testXorWithBooleanBoolean() {
 final boolean bool1 = true;
 final boolean bool2 = false;
 final boolean expected = true;
 final boolean actual = BooleanUtils.xor(bool1, bool2);
 assertEquals(expected, actual);
 final boolean bool1False = false;
 final boolean bool2False = true;
 final boolean expectedFalse = true;
 final boolean actualFalse = BooleanUtils.xor(bool1False, bool2False);
 assertEquals(expectedFalse, actualFalse);
 final boolean bool1Null = false;
 try {
 BooleanUtils.xor(bool1, bool1Null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

}