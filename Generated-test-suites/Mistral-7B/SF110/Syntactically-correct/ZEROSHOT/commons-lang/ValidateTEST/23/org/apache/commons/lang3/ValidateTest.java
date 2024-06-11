package org.apache.commons.lang3;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ValidateTest {

 @Test
 public void testIsTrueWithExpressionAndMessage() {
 Validate.isTrue(true, "The validated expression is true: %d", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseAndMessage() {
 Validate.isTrue(false, "The validated expression is true: %d", 1);
 }

 @Test
 public void testIsTrueWithoutMessage() {
 Validate.isTrue(true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalse() {
 Validate.isTrue(false);
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 Validate.notNull(obj);
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNull() {
 Validate.notNull(null);
 }

 @Test
 public void testNotEmptyArray() {
 String[] arr = new String[4];
 Validate.notEmpty(arr);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArrayWithEmpty() {
 String[] arr = new String[0];
 Validate.notEmpty(arr);
 }

 @Test
 public void testNotEmptyCollection() {
 List<String> list = Arrays.asList("One", "Two", "Three");
 Validate.notEmpty(list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCollectionWithEmpty() {
 List<String> list = Collections.emptyList();
 Validate.notEmpty(list);
 }

 @Test
 public void testNotEmptyMap() {
 Map<String, Integer> map = new HashMap<>();
 map.put("One", 1);
 map.put("Two", 2);
 Validate.notEmpty(map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyMapWithEmpty() {
 Map<String, Integer> map = new HashMap<>();
 Validate.notEmpty(map);
 }

 @Test
 public void testNotEmptyCharSequence() {
 String str = "This is a test";
 Validate.notEmpty(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCharSequenceWithEmpty() {
 String str = "";
 Validate.notEmpty(str);
 }

 @Test
 public void testNotBlankCharSequence() {
 String str = "This is a test";
 Validate.notBlank(str);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotBlankCharSequenceWithBlank() {
 String str = "";
 Validate.notBlank(str);
 }

 @Test
 public void testNoNullElementsArray() {
 String[] arr = new String[]{"One", null, "Three", "Four"};
 Validate.noNullElements(arr, "The validated array contains null element at index: %d");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsArrayWithNull() {
 String[] arr = null;
 Validate.noNullElements(arr);
 }

 @Test
 public void testNoNullElementsIterable() {
 List<String> list = Arrays.asList("One", null, "Three", "Four");
 Validate.noNullElements(list, "The validated collection contains null element at index: %d");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsIterableWithNull() {
 List<String> list = null;
 Validate.noNullElements(list);
 }

 @Test
 public void testValidIndexArray() {
 String[] arr = new String[4];
 Validate.validIndex(arr, 0);
 Validate.validIndex(arr, 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexArrayWithInvalidIndex() {
 String[] arr = new String[4];
 Validate.validIndex(arr, 5);
 }

 @Test
 public void testValidIndexCollection() {
 List<String> list = Arrays.asList("One", "Two", "Three");
 Validate.validIndex(list, 0);
 Validate.validIndex(list, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCollectionWithInvalidIndex() {
 List<String> list = Arrays.asList("One", "Two", "Three");
 Validate.validIndex(list, 3);
 }

 @Test
 public void testValidIndexCharSequence() {
 String str = "This is a test";
 Validate.validIndex(str, 0);
 Validate.validIndex(str, 11);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCharSequenceWithInvalidIndex() {
 String str = "This is a test";
 Validate.validIndex(str, 12);
 }

 @Test
 public void testValidState() {
 boolean state = false;
 Validate.validState(state);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateWithFalseMessage() {
 boolean state = false;
 Validate.validState(state, "The validated state is false");
 }

 @Test
 public void testMatchesPattern() {
 String input = "1234567890";
 Validate.matchesPattern(input, "\\d{9}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithMismatch() {
 String input = "123456abc";
 Validate.matchesPattern(input, "\\d{9}");
 }

 @Test
 public void testInclusiveBetweenLong() {
 long value = 10;
 Validate.inclusiveBetween(0L, 20L, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongWithInvalidValue() {
 long value = 30;
 Validate.inclusiveBetween(0L, 20L, value);
 }

 @Test
 public void testInclusiveBetweenLongWithMessage() {
 long value = 30;
 Validate.inclusiveBetween(0L, 20L, value, "The value %s is not in the specified inclusive range of %s to %s");
 }

 @Test
 public void testInclusiveBetweenDouble() {
 double value = 10.5;
 Validate.inclusiveBetween(1.0, 20.5, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleWithInvalidValue() {
 double value = 30.5;
 Validate.inclusiveBetween(1.0, 20.5, value);
 }

 @Test
 public void testInclusiveBetweenDoubleWithMessage() {
 double value = 30.5;
 Validate.inclusiveBetween(1.0, 20.5, value, "The value %s is not in the specified inclusive range of %s to %s");
 }

 @Test
 public void testExclusiveBetweenLong() {
 long value = 11;
 Validate.exclusiveBetween(0L, 10L, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongWithInvalidValue() {
 long value = 10;
 Validate.exclusiveBetween(0L, 10L, value);
 }

 @Test
 public void testExclusiveBetweenLongWithMessage() {
 long value = 10;
 Validate.exclusiveBetween(0L, 10L, value, "The value %s is not in the specified exclusive range of %s to %s");
 }

 @Test
 public void testExclusiveBetweenDouble() {
 double value = 10.5;
 Validate.exclusiveBetween(1.0, 10.5, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleWithInvalidValue() {
 double value = 10.5;
 Validate.exclusiveBetween(1.0, 10.5, value);
 }

 @Test
 public void testExclusiveBetweenDoubleWithMessage() {
 double value = 10.5;
 Validate.exclusiveBetween(1.0, 10.5, value, "The value %s is not in the specified exclusive range of %s to %s");
 }

 @Test
 public void testIsInstanceOf() {
 Object obj = new Object();
 Validate.isInstanceOf(Object.class, obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithInvalidClass() {
 Object obj = new Object();
 Validate.isInstanceOf(String.class, obj);
 }

 @Test
 public void testIsInstanceOfWithMessage() {
 Object obj = new Object();
 Validate.isInstanceOf(Object.class, obj, "Expected type: %s, actual: %s");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithMessageAndInvalidClass() {
 Object obj = new Object();
 Validate.isInstanceOf(String.class, obj, "Expected type: %s, actual: %s");
 }

 @Test
 public void testIsAssignableFrom() {
 Class<?> superType = String[].class;
 Class<?> type = List.class;
 Validate.isAssignableFrom(superType, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithInvalidSuperType() {
 Class<?> superType = String[].class;
 Class<?> type = Map.class;
 Validate.isAssignableFrom(superType, type);
 }

 @Test
 public void testIsAssignableFromWithMessage() {
 Class<?> superType = String[].class;
 Class<?> type = List.class;
 Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithInvalidSuperTypeAndMessage() {
 Class<?> superType = String[].class;
 Class<?> type = Map.class;
 Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s");
 }
}