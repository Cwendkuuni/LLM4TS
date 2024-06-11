package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class ValidateTest {

 @Test
 public void testIsTrue_boolean_string_long() {
 String message = "The value is not true";
 long value = -1;
 Validate.isTrue(true, message, value);
 Validate.isTrue(false, message, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_boolean_string_long_false() {
 String message = "The value is not true";
 long value = 0;
 Validate.isTrue(false, message, value);
 }

 @Test
 public void testIsTrue_boolean_string_double() {
 String message = "The value is not true";
 double value = -1.0;
 Validate.isTrue(true, message, value);
 Validate.isTrue(false, message, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_boolean_string_double_false() {
 String message = "The value is not true";
 double value = 0.0;
 Validate.isTrue(false, message, value);
 }

 @Test
 public void testIsTrue_boolean_string_Object_Array() {
 String message = "The value is not true";
 Object[] values = new Object[3];
 Validate.isTrue(true, message, values);
 Validate.isTrue(false, message, values);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_boolean_string_Object_Array_false() {
 String message = "The value is not true";
 Object[] values = new Object[3];
 Validate.isTrue(false, message, values);
 }

 @Test
 public void testIsTrue_boolean() {
 Validate.isTrue(true);
 Validate.isTrue(false);
 }

 @Test
 public void testNotNull_Object() {
 Object obj = new Object();
 Validate.notNull(obj);
 }

 @Test(expected = NullPointerException.class)
 public void testNotNull_Object_String_Object_Array_null() {
 String message = "The validated object is null";
 Validate.notNull(null, message, new Object[0]);
 }

 @Test
 public void testNotEmpty_Object_Array() {
 Object[] array = new Object[3];
 Validate.notEmpty(array);
 Validate.notEmpty(new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Object_Array_empty() {
 Object[] array = new Object[0];
 Validate.notEmpty(array);
 }

 @Test
 public void testNotEmpty_Iterable_Collection() {
 Collection<Object> collection = Arrays.asList(new Object[3]);
 Validate.notEmpty(collection);
 Validate.notEmpty(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Iterable_Collection_empty() {
 Collection<Object> collection = Collections.emptyList();
 Validate.notEmpty(collection);
 }

 @Test
 public void testNotEmpty_Map() {
 Map<String, Object> map = new HashMap<>();
 map.put("key", null);
 Validate.notEmpty(map);
 Validate.notEmpty(Collections.emptyMap());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Map_empty() {
 Map<String, Object> map = Collections.emptyMap();
 Validate.notEmpty(map);
 }

 @Test
 public void testNotEmpty_CharSequence() {
 String chars = "hello";
 Validate.notEmpty(chars);
 Validate.notEmpty("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_CharSequence_empty() {
 String chars = "";
 Validate.notEmpty(chars);
 }

 @Test
 public void testNotBlank_CharSequence() {
 String chars = "hello";
 Validate.notBlank(chars);
 Validate.notBlank("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotBlank_CharSequence_blank() {
 String chars = "";
 Validate.notBlank(chars);
 }

 @Test
 public void testNoNullElements_Object_Array() {
 Object[] array = new Object[3];
 array[0] = null;
 Validate.noNullElements(array);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_Object_Array_null_element() {
 Object[] array = new Object[3];
 array[0] = null;
 Validate.noNullElements(array, "The validated array contains null element at index: %d", new Object[]{0});
 }

 @Test
 public void testNoNullElements_Iterable_Collection() {
 Collection<Object> collection = Arrays.asList(new Object[3]);
 collection.add(null);
 Validate.noNullElements(collection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_Iterable_Collection_null_element() {
 Collection<Object> collection = Arrays.asList(new Object[3]);
 collection.add(null);
 Validate.noNullElements(collection, "The validated collection contains null element at index: %d", new Object[]{0});
 }

 @Test
 public void testValidIndex_Object_Array_int() {
 Object[] array = new Object[3];
 Validate.validIndex(array, 1);
 Validate.validIndex(array, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_Object_Array_int_invalid_index() {
 Object[] array = new Object[3];
 Validate.validIndex(array, -1);
 Validate.validIndex(array, 3);
 }

 @Test
 public void testValidIndex_Collection_int() {
 List<Object> collection = Arrays.asList(new Object[3]);
 Validate.validIndex(collection, 1);
 Validate.validIndex(collection, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_Collection_int_invalid_index() {
 List<Object> collection = Arrays.asList(new Object[3]);
 Validate.validIndex(collection, -1);
 Validate.validIndex(collection, 3);
 }

 @Test
 public void testValidIndex_CharSequence_int() {
 String chars = "hello";
 Validate.validIndex(chars, 1);
 Validate.validIndex(chars, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_CharSequence_int_invalid_index() {
 String chars = "hello";
 Validate.validIndex(chars, -1);
 Validate.validIndex(chars, 3);
 }

 @Test
 public void testValidState_boolean() {
 Validate.validState(true);
 Validate.validState(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidState_boolean_String() {
 Validate.validState(false, "The validated state is false");
 }

 @Test
 public void testMatchesPattern_CharSequence_String() {
 String input = "test";
 String pattern = "test";
 Validate.matchesPattern(input, pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPattern_CharSequence_String_invalid_match() {
 String input = "test";
 String pattern = "tes";
 Validate.matchesPattern(input, pattern);
 }

 @Test
 public void testMatchesPattern_CharSequence_String_custom_message() {
 String input = "test";
 String pattern = "tes";
 String message = "The string %s does not match the pattern %s";
 Validate.matchesPattern(input, pattern, message, input, pattern);
 }

 @Test
 public void testInclusiveBetween_Comparable_Comparable_Comparable() {
 Integer start = 1;
 Integer end = 3;
 Integer value = 2;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Comparable_Comparable_Comparable_invalid() {
 Integer start = 1;
 Integer end = 3;
 Integer value = 4;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test
 public void testInclusiveBetween_long_long_long() {
 long start = 1;
 long end = 3;
 long value = 2;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_long_long_long_invalid() {
 long start = 1;
 long end = 3;
 long value = 4;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test
 public void testInclusiveBetween_double_double_double() {
 double start = 1.0;
 double end = 3.0;
 double value = 2.0;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_double_double_double_invalid() {
 double start = 1.0;
 double end = 3.0;
 double value = 4.0;
 Validate.inclusiveBetween(start, end, value);
 }

 @Test
 public void testExclusiveBetween_Comparable_Comparable_Comparable() {
 Integer start = 1;
 Integer end = 3;
 Integer value = 2;
 Validate.exclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Comparable_Comparable_Comparable_invalid() {
 Integer start = 1;
 Integer end = 3;
 Integer value = 2;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testExclusiveBetween_long_long_long() {
 long start = 1;
 long end = 3;
 long value = 2;
 Validate.exclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_long_long_long_invalid() {
 long start = 1;
 long end = 3;
 long value = 2;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testExclusiveBetween_double_double_double() {
 double start = 1.0;
 double end = 3.0;
 double value = 2.0;
 Validate.exclusiveBetween(start, end, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_double_double_double_invalid() {
 double start = 1.0;
 double end = 3.0;
 double value = 2.0;
 Validate.exclusiveBetween(start, end, value, "The value %s is not in the specified exclusive range of %s to %s", value, start, end);
 }

 @Test
 public void testIsInstanceOf() {
 Class<String> stringClass = String.class;
 String obj = new String("test");
 Validate.isInstanceOf(stringClass, obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOf_String_Object() {
 Class<String> stringClass = String.class;
 Object obj = 1;
 Validate.isInstanceOf(stringClass, obj);
 }

 @Test
 public void testIsAssignableFrom() {
 Class<String> stringClass = String.class;
 Class<Object> objectClass = Object.class;
 Validate.isAssignableFrom(objectClass, stringClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFrom_Class_Class() {
 Class<String> stringClass = String.class;
 Class<Object> objectClass = Object.class;
 Validate.isAssignableFrom(stringClass, objectClass);
 }
 }