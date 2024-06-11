package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true, "Expected: true, but was: false");
 Validate.isTrue(false, "Expected: false, but was: true", 42);
 Validate.isTrue(42 > 10, "Expected: condition to be true", 42, 10);
 Validate.isTrue(42.0 > 10.0, "Expected: condition to be true", 42.0, 10.0);
 Validate.isTrue(new Object[]{1, 2, 3}, "Expected: condition to be true");
 Validate.isTrue(new String[]{"a", "b", "c"}, "Expected: condition to be true");
 Validate.isTrue(new ArrayList<>(), "Expected: condition to be true");
 Validate.isTrue(new HashMap<>(), "Expected: condition to be true");
 Validate.isTrue(() -> true, "Expected: lambda expression to return true");

 Validate.isTrue(false, "Expected: true, but was: false");
 Validate.isTrue(42 < 10, "Expected: condition to be false", 42, 10);
 Validate.isTrue(42.0 < 10.0, "Expected: condition to be false", 42.0, 10.0);
 Validate.isTrue(new Object[]{null}, "Expected: condition to be false");
 Validate.isTrue(new String[]{"", "", ""}, "Expected: condition to be false");
 Validate.isTrue(new ArrayList<>(), "Expected: condition to be false");
 Validate.isTrue(new HashMap<>(), "Expected: condition to be false");
 Validate.isTrue(() -> false, "Expected: lambda expression to return false");
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 Validate.notNull(obj, "Expected: object to not be null");

 Object nullObj = null;
 try {
 Validate.notNull(nullObj, "Expected: object to not be null");
 fail();
 } catch (NullPointerException e) {
 // expected
 }

 String str = "not null";
 Validate.notNull(str, "Expected: string to not be null");

 int[] intArray = new int[]{1, 2, 3};
 Validate.notNull(intArray, "Expected: array to not be null");

 List<String> list = new ArrayList<>();
 Validate.notNull(list, "Expected: collection to not be null");

 Map<String, Integer> map = new HashMap<>();
 Validate.notNull(map, "Expected: map to not be null");
 }

 @Test
 public void testNotEmptyArray() {
 int[] intArray = new int[]{1, 2, 3};
 Validate.notEmpty(intArray, "Expected: array to not be empty");

 int[] emptyIntArray = new int[0][];
 try {
 Validate.notEmpty(emptyIntArray, "Expected: array to not be empty");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNotEmptyCollection() {
 List<String> list = new ArrayList<>();
 list.add("a");
 list.add("b");
 Validate.notEmpty(list, "Expected: collection to not be empty");

 List<String> emptyList = new ArrayList<>();
 try {
 Validate.notEmpty(emptyList, "Expected: collection to not be empty");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNotEmptyMap() {
 Map<String, Integer> map = new HashMap<>();
 map.put("key1", 1);
 map.put("key2", 2);
 Validate.notEmpty(map, "Expected: map to not be empty");

 Map<String, Integer> emptyMap = new HashMap<>();
 try {
 Validate.notEmpty(emptyMap, "Expected: map to not be empty");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNoNullElementsArray() {
 int[] intArray = new int[]{1, 2, 3};
 Validate.noNullElements(intArray, "Expected: array to not contain null elements");

 int[] intArrayWithNull = new int[]{1, 2, null, 3};
 try {
 Validate.noNullElements(intArrayWithNull, "Expected: array to not contain null elements");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNoNullElementsCollection() {
 List<String> list = new ArrayList<>();
 list.add("a");
 list.add("b");
 Validate.noNullElements(list, "Expected: collection to not contain null elements");

 List<String> listWithNull = new ArrayList<>();
 listWithNull.add("a");
 listWithNull.add(null);
 listWithNull.add("c");
 try {
 Validate.noNullElements(listWithNull, "Expected: collection to not contain null elements");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testValidIndexArray() {
 int[] intArray = new int[]{1, 2, 3};
 Validate.validIndex(intArray, 1, "Expected: valid index");
 Validate.validIndex(intArray, 2, "Expected: valid index");
 Validate.validIndex(intArray, 3, "Expected: valid index");
 Validate.validIndex(intArray, 4, "Expected: invalid index");
 }

 @Test
 public void testValidIndexCollection() {
 List<String> list = new ArrayList<>();
 list.add("a");
 list.add("b");
 Validate.validIndex(list, 0, "Expected: valid index");
 Validate.validIndex(list, 1, "Expected: valid index");
 Validate.validIndex(list, 2, "Expected: valid index");
 Validate.validIndex(list, 3, "Expected: invalid index");
 }

 @Test
 public void testValidIndexCharSequence() {
 String str = "abc";
 Validate.validIndex(str, 0, "Expected: valid index");
 Validate.validIndex(str, 1, "Expected: valid index");
 Validate.validIndex(str, 2, "Expected: valid index");
 Validate.validIndex(str, 3, "Expected: invalid index");
 }

 @Test
 public void testValidState() {
 boolean validState = true;
 Validate.validState(validState);
 Validate.validState(false, "Expected: false, but was: true");
 }

 @Test
 public void testMatchesPattern() {
 String input = "abcd1234";
 Validate.matchesPattern(input, "^[a-zA-Z0-9]+$");

 String inputWithInvalidPattern = "abcd1234x";
 try {
 Validate.matchesPattern(inputWithInvalidPattern, "^[a-zA-Z0-9]+$");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInclusiveBetween() {
 int value = 10;
 Validate.inclusiveBetween(5, 15, value);
 Validate.inclusiveBetween(5, 15, 11, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5, 15, 5, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5, 15, 16, "Expected: value to be inclusive between 5 and 15");

 long valueLong = 10L;
 Validate.inclusiveBetween(5L, 15L, valueLong, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5L, 15L, 5L, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5L, 15L, 16L, "Expected: value to be inclusive between 5 and 15");

 double valueDouble = 10.0;
 Validate.inclusiveBetween(5.0, 15.0, valueDouble, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5.0, 15.0, 5.0, "Expected: value to be inclusive between 5 and 15");
 Validate.inclusiveBetween(5.0, 15.0, 16.0, "Expected: value to be inclusive between 5 and 15");
 }

 @Test
 public void testExclusiveBetween() {
 int value = 10;
 Validate.exclusiveBetween(4, 14, value);
 Validate.exclusiveBetween(4, 14, 11, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4, 14, 4, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4, 14, 15, "Expected: value to be exclusive between 4 and 14");

 long valueLong = 10L;
 Validate.exclusiveBetween(4L, 14L, valueLong, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4L, 14L, 4L, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4L, 14L, 15L, "Expected: value to be exclusive between 4 and 14");

 double valueDouble = 10.0;
 Validate.exclusiveBetween(4.0, 14.0, valueDouble, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4.0, 14.0, 4.0, "Expected: value to be exclusive between 4 and 14");
 Validate.exclusiveBetween(4.0, 14.0, 15.0, "Expected: value to be exclusive between 4 and 14");
 }

 @Test
 public void testIsInstanceOf() {
 String str = "test";
 Validate.isInstanceOf(String.class, str);
 Validate.isInstanceOf(Integer.class, 10);
 Validate.isInstanceOf(ArrayList.class, new ArrayList<>());
 Validate.isInstanceOf(HashMap.class, new HashMap<>());

 String wrongTypeStr = new Object();
 try {
 Validate.isInstanceOf(String.class, wrongTypeStr);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 Integer wrongTypeInt = new Object();
 try {
 Validate.isInstanceOf(Integer.class, wrongTypeInt);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIsAssignableFrom() {
 String str = "test";
 Validate.isAssignableFrom(String.class, String.class);
 Validate.isAssignableFrom(String.class, Object.class);
 Validate.isAssignableFrom(Object.class, String.class);
 Validate.isAssignableFrom(Integer.class, Object.class);
 Validate.isAssignableFrom(Object.class, Integer.class);

 String differentTypeStr = new Object();
 try {
 Validate.isAssignableFrom(String.class, differentTypeStr.getClass());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}