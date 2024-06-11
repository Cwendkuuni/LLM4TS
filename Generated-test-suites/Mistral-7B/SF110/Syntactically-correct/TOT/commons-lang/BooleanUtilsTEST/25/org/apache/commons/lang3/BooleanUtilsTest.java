package org.apache.commons.lang3.test;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean result = BooleanUtils.negate(bool);
 assertEquals(expected, result);

 bool = null;
 result = BooleanUtils.negate(bool);
 assertNull(result);
 }

 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.isTrue(bool);
 assertEquals(expected, result);

 bool = Boolean.FALSE;
 expected = Boolean.FALSE;
 result = BooleanUtils.isTrue(bool);
 assertEquals(expected, result);

 bool = null;
 assertFalse(BooleanUtils.isTrue(bool));
 }

 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 Boolean expected = Boolean.FALSE;
 Boolean result = BooleanUtils.isNotTrue(bool);
 assertEquals(expected, result);

 bool = Boolean.FALSE;
 expected = Boolean.TRUE;
 result = BooleanUtils.isNotTrue(bool);
 assertEquals(expected, result);

 bool = null;
 assertFalse(BooleanUtils.isNotTrue(bool));
 }

 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.isFalse(bool);
 assertEquals(expected, result);

 bool = Boolean.TRUE;
 expected = Boolean.FALSE;
 result = BooleanUtils.isFalse(bool);
 assertEquals(expected, result);

 bool = null;
 assertFalse(BooleanUtils.isFalse(bool));
 }

 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.isNotFalse(bool);
 assertEquals(expected, result);

 bool = Boolean.TRUE;
 expected = Boolean.FALSE;
 result = BooleanUtils.isNotFalse(bool);
 assertEquals(expected, result);

 bool = null;
 assertFalse(BooleanUtils.isNotFalse(bool));
 }

 @Test
 public void testToBoolean() {
 Integer intValue = 1;
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.toBoolean(intValue);
 assertEquals(expected, result);

 intValue = 0;
 expected = Boolean.FALSE;
 result = BooleanUtils.toBoolean(intValue);
 assertEquals(expected, result);

 intValue = null;
 assertNull(BooleanUtils.toBoolean(intValue));

 try {
 BooleanUtils.toBoolean(-1);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match either specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanObject() {
 Integer intValue = 1;
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.toBooleanObject(intValue);
 assertEquals(expected, result);

 intValue = 0;
 expected = Boolean.FALSE;
 result = BooleanUtils.toBooleanObject(intValue);
 assertEquals(expected, result);

 intValue = null;
 assertNull(BooleanUtils.toBooleanObject(intValue));

 try {
 BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE.negate().intValue());
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Integer did not match any specified value", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanDefaultIfNull() {
 Integer intValue = null;
 Boolean expected = Boolean.FALSE;
 Boolean result = BooleanUtils.toBooleanDefaultIfNull(intValue, expected);
 assertEquals(expected, result);

 intValue = 1;
 expected = Boolean.TRUE;
 result = BooleanUtils.toBooleanDefaultIfNull(intValue, expected);
 assertEquals(expected, result);
 }

 @Test
 public void testToBooleanWithArray() {
 boolean[] boolArray = {true, true, true};
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.and(boolArray);
 assertEquals(expected, result);

 boolArray = new boolean[] {true, false, true};
 expected = Boolean.FALSE;
 result = BooleanUtils.and(boolArray);
 assertEquals(expected, result);

 boolArray = new boolean[] {false};
 assertFalse(BooleanUtils.and(boolArray));

 try {
 boolean[] emptyArray = new boolean[0];
 BooleanUtils.and(emptyArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArray() {
 Boolean[] boolArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.and(boolArray);
 assertEquals(expected, result);

 boolArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.FALSE;
 result = BooleanUtils.and(boolArray);
 assertEquals(expected, result);

 boolArray = new Boolean[] {null};
 assertFalse(BooleanUtils.and(boolArray));

 try {
 Boolean[] emptyArray = new Boolean[0];
 BooleanUtils.and(emptyArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitive() {
 boolean[] boolArray = {true, true, true};
 Boolean expected = Boolean.TRUE;
 boolean[] primitiveBoolArray = ArrayUtils.toPrimitive(boolArray);
 Boolean result = BooleanUtils.and(primitiveBoolArray);
 assertEquals(expected, result);

 try {
 boolean[] nullArray = new boolean[0];
 BooleanUtils.and(ArrayUtils.toPrimitive(nullArray));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithNullArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 assertFalse(BooleanUtils.and(boolArray));
 }

 @Test
 public void testToBooleanWithNullElementInArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 try {
 BooleanUtils.and(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithEmptyArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.and(boolArray));
 }

 @Test
 public void testToBooleanWithEmptyBooleanArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.and(boolArray));
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullArray() {
 boolean[] boolArray = {};
 try {
 BooleanUtils.and(ArrayUtils.toPrimitive(boolArray));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullElement() {
 boolean[] boolArray = {true, false, null};
 try {
 BooleanUtils.and(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testOr() {
 boolean[] boolArray = {true, true, true};
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.or(boolArray);
 assertEquals(expected, result);

 boolArray = new boolean[] {true, false, true};
 expected = Boolean.TRUE;
 result = BooleanUtils.or(boolArray);
 assertEquals(expected, result);

 boolArray = new boolean[] {false};
 assertFalse(BooleanUtils.or(boolArray));

 try {
 boolean[] emptyArray = new boolean[0];
 BooleanUtils.or(emptyArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] boolArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Boolean expected = Boolean.TRUE;
 Boolean result = BooleanUtils.or(boolArray);
 assertEquals(expected, result);

 boolArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 expected = Boolean.TRUE;
 result = BooleanUtils.or(boolArray);
 assertEquals(expected, result);

 boolArray = new Boolean[] {null};
 assertFalse(BooleanUtils.or(boolArray));

 try {
 Boolean[] emptyArray = new Boolean[0];
 BooleanUtils.or(emptyArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitive() {
 boolean[] boolArray = {true, true, true};
 Boolean expected = Boolean.TRUE;
 boolean[] primitiveBoolArray = ArrayUtils.toPrimitive(boolArray);
 Boolean result = BooleanUtils.or(primitiveBoolArray);
 assertEquals(expected, result);

 try {
 boolean[] nullArray = new boolean[0];
 BooleanUtils.or(ArrayUtils.toPrimitive(nullArray));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithNullArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 assertFalse(BooleanUtils.or(boolArray));
 }

 @Test
 public void testToBooleanWithNullElementInArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 try {
 BooleanUtils.or(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithEmptyArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.or(boolArray));
 }

 @Test
 public void testToBooleanWithEmptyBooleanArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.or(boolArray));
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullArray() {
 boolean[] boolArray = {};
 try {
 BooleanUtils.or(ArrayUtils.toPrimitive(boolArray));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullElement() {
 boolean[] boolArray = {true, false, null};
 try {
 BooleanUtils.or(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testXor() {
 boolean[] boolArray = {true, true, true};
 boolean result = BooleanUtils.xor(boolArray);
 assertEquals(false, result);

 boolArray = new boolean[] {true, false, true};
 result = BooleanUtils.xor(boolArray);
 assertEquals(true, result);

 boolArray = new boolean[] {false, false, false};
 result = BooleanUtils.xor(boolArray);
 assertEquals(false, result);
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] boolArray = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 Boolean result = BooleanUtils.xor(boolArray);
 assertEquals(Boolean.TRUE, result);

 boolArray = new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
 result = BooleanUtils.xor(boolArray);
 assertEquals(Boolean.TRUE, result);

 boolArray = new Boolean[] {null};
 assertFalse(BooleanUtils.xor(boolArray));

 try {
 Boolean[] emptyArray = new Boolean[0];
 BooleanUtils.xor(emptyArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitive() {
 boolean[] boolArray = {true, true, true};
 boolean result = BooleanUtils.xor(boolArray);
 assertEquals(false, result);

 try {
 boolean[] nullArray = new boolean[0];
 BooleanUtils.xor(ArrayUtils.toPrimitive(nullArray));
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithNullArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 assertFalse(BooleanUtils.xor(boolArray));
 }

 @Test
 public void testToBooleanWithNullElementInArray() {
 Boolean[] boolArray = {Boolean.TRUE, null, Boolean.TRUE};
 try {
 BooleanUtils.xor(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }

 @Test
 public void testToBooleanWithEmptyArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.xor(boolArray));
 }

 @Test
 public void testToBooleanWithEmptyBooleanArray() {
 Boolean[] boolArray = {};
 assertFalse(BooleanUtils.xor(boolArray));
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullArray() {
 boolean[] boolArray = {};
 try {
 BooleanUtils.xor(ArrayUtils.toPrimitive(boolArray));
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException ex) {
 assertEquals("The Array must not be empty", ex.getMessage());
 }
 }

 @Test
 public void testToBooleanWithBooleanArrayUsingPrimitiveWithNullElement() {
 boolean[] boolArray = {true, false, null};
 try {
 BooleanUtils.xor(boolArray);
 fail("Expected NullPointerException to be thrown");
 } catch (NullPointerException ex) {
 // Expected
 }
 }
}