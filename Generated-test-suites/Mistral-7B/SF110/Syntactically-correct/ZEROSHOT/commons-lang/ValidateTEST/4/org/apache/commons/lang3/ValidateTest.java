package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 // Test boolean expression with message and value
 String message = "The validated expression is false";
 long value = 10;
 Validate.isTrue(true, message, value);
 Validate.isTrue(false, message, value);

 // Test boolean expression without message and value
 Validate.isTrue(true);
 Validate.isTrue(false);
 }

 @Test
 public void testIsTrueWithMessageAndArrayValues() {
 // Test boolean expression with message and array values
 String message = "The validated expression is false";
 Object[] values = new Object[] { "Test", 10 };
 Validate.isTrue(true, message, values);
 Validate.isTrue(false, message, values);

 // Test different values for message and array values
 message = "The validated expression is false for %s and %d";
 values = new Object[] { "Test2", 20 };
 Validate.isTrue(true, message, values);
 Validate.isTrue(false, message, values);
 }

 @Test
 public void testNotNull() {
 // Test null object with message and array values
 String message = "The validated object is null";
 Object obj = null;
 Validate.notNull(obj, message, new Object[0]);

 try {
 Validate.notNull(obj, message, new Object[] { "Test" });
 fail();
 } catch (NullPointerException e) {
 // Expected
 }

 // Test non-null object
 Object nonNullObj = new Object();
 Validate.notNull(nonNullObj);
 }

 @Test
 public void testNotEmptyArray() {
 // Test empty array with message and array values
 String message = "The validated array is empty";
 Object[] array = new Object[0];
 Validate.notEmpty(array, message, new Object[0]);

 try {
 Validate.notEmpty(array, message, new Object[] { "Test" });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test non-empty array
 Object[] nonEmptyArray = new Object[] { "Test1", "Test2" };
 Validate.notEmpty(nonEmptyArray);
 }

 @Test
 public void testNotEmptyCollection() {
 // Test empty collection with message and array values
 String message = "The validated collection is empty";
 List<Object> collection = new ArrayList<>();
 Validate.notEmpty(collection, message, new Object[0]);

 try {
 Validate.notEmpty(collection, message, new Object[] { "Test" });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test non-empty collection
 List<Object> nonEmptyCollection = new ArrayList<>(Arrays.asList(new Object[] { "Test1", "Test2" }));
 Validate.notEmpty(nonEmptyCollection);
 }

 @Test
 public void testNotEmptyMap() {
 // Test empty map with message and array values
 String message = "The validated map is empty";
 Map<String, Object> map = new HashMap<>();
 Validate.notEmpty(map, message, new Object[0]);

 try {
 Validate.notEmpty(map, message, new Object[] { "Test" });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test non-empty map
 Map<String, Object> nonEmptyMap = new HashMap<>(Map.of("Key1", "Value1", "Key2", "Value2"));
 Validate.notEmpty(nonEmptyMap);
 }

 @Test
 public void testNotEmptyCharSequence() {
 // Test empty character sequence with message and array values
 String message = "The validated character sequence is empty";
 String charSequence = "";
 Validate.notEmpty(charSequence, message, new Object[0]);

 try {
 Validate.notEmpty(charSequence, message, new Object[] { "Test" });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test non-empty character sequence
 String nonEmptyCharSequence = "Test1Test2";
 Validate.notEmpty(nonEmptyCharSequence);
 }

 @Test
 public void testNotBlankCharSequence() {
 // Test blank character sequence with message and array values
 String message = "The validated character sequence is blank";
 String charSequence = "";
 Validate.notBlank(charSequence, message, new Object[0]);

 try {
 Validate.notBlank(charSequence, message, new Object[] { "Test" });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test non-blank character sequence
 String nonEmptyCharSequence = "Test1Test2";
 Validate.notBlank(nonEmptyCharSequence);
 }

 @Test
 public void testNoNullElementsArray() {
 // Test array with null element with message and array values
 String message = "The validated array contains null element at index: %d";
 Object[] array = new Object[] { "Test1", null, "Test3" };
 try {
 Validate.noNullElements(array, message, new Object[0]);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test array without null element
 Object[] noNullArray = new Object[] { "Test1", "Test2", "Test3" };
 Validate.noNullElements(noNullArray);
 }

 @Test
 public void testNoNullElementsCollection() {
 // Test collection with null element with message and array values
 String message = "The validated collection contains null element at index: %d";
 List<Object> collection = new ArrayList<>(Arrays.asList(new Object[] { "Test1", null, "Test3" }));
 try {
 Validate.noNullElements(collection, message, new Object[0]);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test collection without null element
 List<Object> noNullCollection = new ArrayList<>(Arrays.asList(new Object[] { "Test1", "Test2", "Test3" }));
 Validate.noNullElements(noNullCollection);
 }

 @Test
 public void testValidIndexArray() {
 // Test valid index
 Object[] array = new Object[] { "Test1", "Test2", "Test3" };
 Validate.validIndex(array, 1);

 // Test negative index
 Object[] array2 = new Object[] { "Test1", "Test2", "Test3" };
 try {
 Validate.validIndex(array2, -1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }

 // Test index out of bounds
 Object[] array3 = new Object[] { "Test1" };
 try {
 Validate.validIndex(array3, 1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testValidIndexCollection() {
 // Test valid index
 List<Object> collection = new ArrayList<>(Arrays.asList(new Object[] { "Test1", "Test2", "Test3" }));
 Validate.validIndex(collection, 1);

 // Test negative index
 List<Object> collection2 = new ArrayList<>(Arrays.asList(new Object[] { "Test1", "Test2", "Test3" }));
 try {
 Validate.validIndex(collection2, -1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }

 // Test index out of bounds
 List<Object> collection3 = new ArrayList<>(Arrays.asList(new Object[] { "Test1" }));
 try {
 Validate.validIndex(collection3, 1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testValidIndexCharSequence() {
 // Test valid index
 String charSequence = "Test1Test2Test3";
 Validate.validIndex(charSequence, 1);

 // Test negative index
 String charSequence2 = "Test1Test2Test3";
 try {
 Validate.validIndex(charSequence2, -1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }

 // Test index out of bounds
 String charSequence3 = "Test1";
 try {
 Validate.validIndex(charSequence3, 1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testValidState() {
 // Test valid state
 boolean validState = true;
 Validate.validState(validState);

 // Test invalid state
 boolean invalidState = false;
 try {
 Validate.validState(invalidState);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testMatchesPattern() {
 // Test valid character sequence against pattern
 String pattern = "^Test.*";
 String charSequence = "Test1Test2";
 Validate.matchesPattern(charSequence, pattern);

 // Test invalid character sequence against pattern
 String invalidCharSequence = "InvalidTest";
 try {
 Validate.matchesPattern(invalidCharSequence, pattern);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMatchesPatternWithMessage() {
 // Test valid character sequence against pattern with message
 String message = "The string %s does not match the pattern %s";
 String pattern = "^Test.*";
 String charSequence = "Test1Test2";
 Validate.matchesPattern(charSequence, pattern, message, new Object[] { charSequence, pattern });

 // Test invalid character sequence against pattern with message
 String invalidCharSequence = "InvalidTest";
 try {
 Validate.matchesPattern(invalidCharSequence, pattern, message, new Object[] { invalidCharSequence, pattern });
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInclusiveBetween() {
 // Test inclusive between long values
 long start = 0;
 long end = 10;
 long value = 5;
 Validate.inclusiveBetween(start, end, value);
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", start, end, value);

 // Test inclusive between double values
 double start2 = 0.0;
 double end2 = 10.0;
 double value2 = 5.5;
 Validate.inclusiveBetween(start2, end2, value2);
 Validate.inclusiveBetween(start2, end2, value2, "The value %s is not in the specified inclusive range of %s to %s", start2, end2, value2);
 }

 @Test
 public void testExclusiveBetween() {
 // Test exclusive between long values
 long start = 0;
 long end = 10;
 long value = 5;
 Validate.exclusiveBetween(start, end, value);
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", start, end, value);

 // Test exclusive between double values
 double start2 = 0.0;
 double end2 = 10.0;
 double value2 = 5.5;
 Validate.exclusiveBetween(start2, end2, value2);
 Validate.exclusiveBetween(start2, end2, value2, "The value %s is not in the specified exclusive range of %s to %s", start2, end2, value2);
 }

 @Test
 public void testIsInstanceOf() {
 // Test valid instance of class
 Class<?> expectedType = String.class;
 String value = "Test";
 Validate.isInstanceOf(expectedType, value);
 Validate.isInstanceOf(expectedType, value, "Expected type: %s, actual: %s", expectedType.getName(), value.getClass().getName());

 // Test invalid instance of class
 Class<?> invalidType = Integer.class;
 String value2 = "Test";
 try {
 Validate.isInstanceOf(invalidType, value2);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test null object
 Validate.isInstanceOf(String.class, null);
 try {
 Validate.isInstanceOf(String.class, null, "Expected type: %s, actual: %s", String.class.getName(), null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsAssignableFrom() {
 // Test valid assignment from class to interface
 Class<?> superType = Iterator.class;
 Class<?> type = ListIterator.class;
 Validate.isAssignableFrom(superType, type);
 Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s", superType.getName(), type.getName());

 // Test invalid assignment from class to interface
 Class<?> superType2 = Iterator.class;
 Class<?> type2 = String.class;
 try {
 Validate.isAssignableFrom(superType2, type2);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test null super type
 Validate.isAssignableFrom(null, String.class);
 try {
 Validate.isAssignableFrom(null, String.class, "Cannot assign a %s to a %s", null, String.class.getName());
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}