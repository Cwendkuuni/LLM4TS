package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 String message = "The validated expression is false";
 Validate.isTrue(true, message);
 Validate.isTrue(false, message);
 Validate.isTrue(3 > 2, message);
 Validate.isTrue(1.5 > 1.0, message);
 Validate.isTrue(Arrays.asList(1, 2, 3).contains(2), message);
 Validate.isTrue(new String[]{"a", "b", "c"}.length > 2, message);
 Validate.isTrue(new HashSet<>(Arrays.asList(1, 2, 3)).size() > 2, message);
 Validate.isTrue(new HashMap<>().isEmpty(), message);
 Validate.isTrue(new StringBuilder().toString().isEmpty(), message);
 Validate.isTrue(new StringBuffer().toString().isEmpty(), message);

 Validate.isTrue(true, message); // empty argument list
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithMessage() {
 String message = "The validated expression is false";
 String value = "false";
 Validate.isTrue(false, message, value.toCharArray());
 }

 @Test
 public void testIsTrueWithValues() {
 String message = "The validated expression is false";
 Validate.isTrue(true, message, "expected", "value");
 Validate.isTrue(false, message, 1, "expected", "value");
 Validate.isTrue(false, message, 1.5, "expected", "value");
 Validate.isTrue(false, message, new int[]{1, 2, 3}, "expected", "value");
 Validate.isTrue(false, message, new double[]{1.5, 2.5, 3.5}, "expected", "value");
 Validate.isTrue(false, message, new Object[]{"a", "b", "c"}, "expected", "value");
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 Validate.notNull(obj);
 Validate.notNull((Object) null, "The validated object is null");
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithMessage() {
 Object obj = null;
 Validate.notNull(obj, "The validated object is null");
 }

 @Test
 public void testNotEmptyArray() {
 int[] arr = {1, 2, 3};
 Validate.notEmpty(arr, "The validated array is empty");
 Validate.notEmpty(new int[0], "The validated array is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArrayWithMessage() {
 int[] arr = new int[0];
 Validate.notEmpty(arr, "The validated array is empty", new Object[0]);
 }

 @Test
 public void testNotEmptyCollection() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Validate.notEmpty(list, "The validated collection is empty");
 Validate.notEmpty(Collections.emptyList(), "The validated collection is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCollectionWithMessage() {
 List<Integer> list = Collections.emptyList();
 Validate.notEmpty(list, "The validated collection is empty", new Object[0]);
 }

 @Test
 public void testNotEmptyMap() {
 Map<Integer, String> map = new HashMap<>();
 map.put(1, "one");
 map.put(2, "two");
 Validate.notEmpty(map, "The validated map is empty");
 Validate.notEmpty(new HashMap<>(), "The validated map is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyMapWithMessage() {
 Map<Integer, String> map = new HashMap<>();
 Validate.notEmpty(map, "The validated map is empty", new Object[0]);
 }

 @Test
 public void testNotEmptyCharSequence() {
 String str = "abc";
 Validate.notEmpty(str, "The validated character sequence is empty");
 Validate.notEmpty(new String(""), "The validated character sequence is empty");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCharSequenceWithMessage() {
 String str = "";
 Validate.notEmpty(str, "The validated character sequence is empty", new Object[0]);
 }

 @Test
 public void testNoNullElementsArray() {
 String[] arr = {"a", "b", "c"};
 Validate.noNullElements(arr, "The validated array contains null element at index: %d");
 Validate.noNullElements(new String[0], "The validated array contains null element at index: %d");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsArrayWithMessage() {
 String[] arr = new String[0];
 Validate.noNullElements(arr, "The validated array contains null element at index: %d", new Object[]{0});
 }

 @Test
 public void testNoNullElementsCollection() {
 List<String> list = Arrays.asList("a", "b", "c");
 Validate.noNullElements(list, "The validated collection contains null element at index: %d");
 Validate.noNullElements(Collections.emptyList(), "The validated collection contains null element at index: %d");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsCollectionWithMessage() {
 List<String> list = Collections.emptyList();
 Validate.noNullElements(list, "The validated collection contains null element at index: %d", new Object[]{0});
 }

 @Test
 public void testValidIndexArray() {
 int[] arr = {1, 2, 3};
 Validate.validIndex(arr, 2, "The validated array index is invalid: %d");
 Validate.validIndex(arr, -1);
 Validate.validIndex(arr, 4);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexArrayWithMessage() {
 int[] arr = {1, 2, 3};
 Validate.validIndex(arr, 2, "The validated array index is invalid: %d", new Object[]{3});
 }

 @Test
 public void testValidIndexCollection() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Validate.validIndex(list, 2, "The validated collection index is invalid: %d");
 Validate.validIndex(list, -1);
 Validate.validIndex(list, 4);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCollectionWithMessage() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Validate.validIndex(list, 2, "The validated collection index is invalid: %d", new Object[]{4});
 }

 @Test
 public void testValidIndexCharSequence() {
 String str = "abc";
 Validate.validIndex(str, 2, "The validated character sequence index is invalid: %d");
 Validate.validIndex(str, -1);
 Validate.validIndex(str, 4);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCharSequenceWithMessage() {
 String str = "abc";
 Validate.validIndex(str, 2, "The validated character sequence index is invalid: %d", new Object[]{5});
 }

 @Test
 public void testValidState() {
 String message = "The validated state is false";
 Validate.validState(true, message);
 Validate.validState(false, message);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateWithMessage() {
 String message = "The validated state is false";
 Validate.validState(false, message);
 }

 @Test
 public void testMatchesPattern() {
 String input = "abcd1234";
 String pattern = "\\d{4}";
 Validate.matchesPattern(input, pattern);
 Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s", input, pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithMessage() {
 String input = "abcd1234";
 String pattern = "\\w{4}";
 Validate.matchesPattern(input, pattern, "The string %s does not match the pattern %s", input, pattern);
 }

 @Test
 public void testInclusiveBetween() {
 int value = 2;
 int start = 1;
 int end = 3;
 Validate.inclusiveBetween(start, end, value);
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithMessage() {
 int value = 1;
 int start = 1;
 int end = 3;
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testInclusiveBetweenLong() {
 long value = 3L;
 long start = 1L;
 long end = 5L;
 Validate.inclusiveBetween(start, end, value);
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongWithMessage() {
 long value = 2L;
 long start = 1L;
 long end = 5L;
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testInclusiveBetweenDouble() {
 double value = 2.5;
 double start = 1.5;
 double end = 4.0;
 Validate.inclusiveBetween(start, end, value);
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleWithMessage() {
 double value = 1.5;
 double start = 1.5;
 double end = 4.0;
 Validate.inclusiveBetween(start, end, value, "The value %s is not in the specified inclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testExclusiveBetween() {
 int value = 2;
 int start = 1;
 int end = 3;
 Validate.exclusiveBetween(start, end, value);
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithMessage() {
 int value = 1;
 int start = 1;
 int end = 3;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testExclusiveBetweenLong() {
 long value = 3L;
 long start = 1L;
 long end = 5L;
 Validate.exclusiveBetween(start, end, value);
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongWithMessage() {
 long value = 2L;
 long start = 1L;
 long end = 5L;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testExclusiveBetweenDouble() {
 double value = 2.5;
 double start = 1.5;
 double end = 4.0;
 Validate.exclusiveBetween(start, end, value);
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleWithMessage() {
 double value = 1.5;
 double start = 1.5;
 double end = 4.0;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testIsInstanceOf() {
 Class<?> type = String.class;
 Object obj = new String("abc");
 Validate.isInstanceOf(type, obj);
 Validate.isInstanceOf(type, obj, "Expected type: %s, actual: %s", type.getName(), obj.getClass().getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithMessage() {
 Class<?> type = String.class;
 Object obj = new Integer(1);
 Validate.isInstanceOf(type, obj, "Expected type: %s, actual: %s", type.getName(), obj.getClass().getName());
 }

 @Test
 public void testIsAssignableFrom() {
 Class<?> superType = Number.class;
 Class<?> type = Integer.class;
 Validate.isAssignableFrom(superType, type);
 Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s", superType.getName(), type.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithMessage() {
 Class<?> superType = Number.class;
 Class<?> type = Float.class;
 Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s", superType.getName(), type.getName());
 }
}