package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.Pattern;

public class ValidateTest {

 @Test
 public void testIsTrue_boolean_noMessage_true() {
 Validate.isTrue(true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_boolean_noMessage_false() {
 Validate.isTrue(false);
 }

 @Test
 public void testIsTrue_boolean_customMessage_true() {
 Validate.isTrue(true, "The validated expression is true: %d", 42);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_boolean_customMessage_false() {
 Validate.isTrue(false, "The validated expression is true: %d", 42);
 }

 @Test
 public void testIsTrue_booleanArray_noMessage_true() {
 Validate.isTrue(new boolean[0], true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_booleanArray_noMessage_false() {
 Validate.isTrue(new boolean[0], false);
 }

 @Test
 public void testIsTrue_booleanArray_customMessage_true() {
 Validate.isTrue(new boolean[0], "The validated array contains true elements: %d", new Object[] { 1 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_booleanArray_customMessage_false() {
 Validate.isTrue(new boolean[0], "The validated array contains true elements: %d", new Object[] { 1 });
 }

 @Test
 public void testIsTrue_double_noMessage_true() {
 Validate.isTrue(1.0d, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_double_noMessage_false() {
 Validate.isTrue(1.0d, false);
 }

 @Test
 public void testIsTrue_double_customMessage_true() {
 Validate.isTrue(1.0d, "The validated double is positive: %f", 1.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_double_customMessage_false() {
 Validate.isTrue(1.0d, "The validated double is positive: %f", 1.5);
 }

 @Test
 public void testIsTrue_Object_noMessage_true() {
 Validate.isTrue(new Object(), true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_Object_noMessage_false() {
 Validate.isTrue(new Object(), false);
 }

 @Test
 public void testIsTrue_Object_customMessage_true() {
 Validate.isTrue(new Object(), "The validated object is not null: %s", "not null");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_Object_customMessage_false() {
 Validate.isTrue(new Object(), "The validated object is not null: %s", "not null");
 }

 @Test
 public void testNotNull_Object() {
 Validate.notNull(new Object());
 }

 @Test(expected = NullPointerException.class)
 public void testNotNull_Object_customMessage_null() {
 Validate.notNull(null, "The validated object is null: %s", "not null");
 }

 @Test
 public void testNotEmpty_ObjectArray() {
 Validate.notEmpty(new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_ObjectArray_customMessage_empty() {
 Validate.notEmpty(new Object[0], "The validated array is empty: %d", 1);
 }

 @Test
 public void testNotEmpty_ObjectArray_customMessage_nonEmpty() {
 Validate.notEmpty(new Object[] { new Object() }, "The validated array is empty: %d", 1);
 }

 @Test
 public void testNotEmpty_Collection() {
 Validate.notEmpty((Collection<?>) new ArrayList<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Collection_customMessage_empty() {
 Validate.notEmpty((Collection<?>) new ArrayList<>(), "The validated collection is empty: %d", 1);
 }

 @Test
 public void testNotEmpty_Collection_customMessage_nonEmpty() {
 Validate.notEmpty((Collection<?>) new ArrayList<>(), "The validated collection is empty: %d", 1);
 Validate.notEmpty((Collection<?>) new ArrayList<>(), "The validated collection is empty: %d", new Object[] { 1, 2 });
 }

 @Test
 public void testNotEmpty_Map() {
 Validate.notEmpty((Map<?, ?>) new HashMap<>());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Map_customMessage_empty() {
 Validate.notEmpty((Map<?, ?>) new HashMap<>(), "The validated map is empty: %d", 1);
 }

 @Test
 public void testNotEmpty_Map_customMessage_nonEmpty() {
 Validate.notEmpty((Map<?, ?>) new HashMap<>(), "The validated map is empty: %d", 1);
 Validate.notEmpty((Map<?, ?>) new HashMap<>(), "The validated map is empty: %d", new Object[] { 1, 2, 3 });
 }

 @Test
 public void testNotEmpty_CharSequence() {
 Validate.notEmpty("").isEmpty();
 Validate.notEmpty("abc");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_CharSequence_customMessage_empty() {
 Validate.notEmpty("", "The validated character sequence is empty: %d", 1);
 }

 @Test
 public void testNotBlank_CharSequence() {
 Validate.notBlank("abc");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotBlank_CharSequence_customMessage_blank() {
 Validate.notBlank("", "The validated character sequence is blank: %d", 1);
 }

 @Test
 public void testNoNullElements_ObjectArray() {
 Validate.noNullElements(new Object[] { null, new Object(), new Object() }, "The validated array contains null element at index: %d", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_ObjectArray_customMessage_invalidIndex() {
 Validate.noNullElements(new Object[] { null, new Object(), new Object() }, "The validated array contains null element at index: %d", -1);
 }

 @Test
 public void testNoNullElements_Collection() {
 Validate.noNullElements((Collection<?>) new ArrayList<>(), "The validated collection contains null element at index: %d", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_Collection_customMessage_invalidIndex() {
 Validate.noNullElements((Collection<?>) new ArrayList<>(), "The validated collection contains null element at index: %d", -1);
 }

 @Test
 public void testValidIndex_ObjectArray() {
 Validate.validIndex(new Object[0], 0, "The validated array index is invalid: %d", 1);
 Validate.validIndex(new Object[0], 1, "The validated array index is invalid: %d", 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_ObjectArray_invalidIndex() {
 Validate.validIndex(new Object[0], 10, "The validated array index is invalid: %d", 1);
 }

 @Test
 public void testValidIndex_Collection() {
 Validate.validIndex((Collection<?>) new ArrayList<>(), 0, "The validated collection index is invalid: %d", 1);
 Validate.validIndex((Collection<?>) new ArrayList<>(), 1, "The validated collection index is invalid: %d", 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_Collection_invalidIndex() {
 Validate.validIndex((Collection<?>) new ArrayList<>(), 10, "The validated collection index is invalid: %d", 1);
 }

 @Test
 public void testValidIndex_CharSequence() {
 Validate.validIndex("abcde", 2, "The validated character sequence index is invalid: %d", 1);
 Validate.validIndex("abcde", 5, "The validated character sequence index is invalid: %d", 1);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testValidIndex_CharSequence_invalidIndex() {
 Validate.validIndex("abcde", 10, "The validated character sequence index is invalid: %d", 1);
 }

 @Test
 public void testValidState_boolean() {
 Validate.validState(true);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidState_boolean_customMessage() {
 Validate.validState(false, "The validated state is false: %d", 1);
 }

 @Test
 public void testMatchesPattern_CharSequence() {
 Validate.matchesPattern("abc", "a.b.*c");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPattern_CharSequence_invalidPattern() {
 Validate.matchesPattern("abc", "a.b.*c.d");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPattern_CharSequence_customMessage_mismatch() {
 Validate.matchesPattern("abc", "a.b.*c", "The string %s does not match the pattern %s: %s", "abcde", "a.b.*c");
 }

 @Test
 public void testInclusiveBetween_Integer() {
 Validate.inclusiveBetween(5, 10, 7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Integer_customMessage_lessThanStart() {
 Validate.inclusiveBetween(5, 10, 4, "The value %d is not in the specified inclusive range of %d to %d", 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Integer_customMessage_greaterThanEnd() {
 Validate.inclusiveBetween(5, 10, 12, "The value %d is not in the specified inclusive range of %d to %d", 11);
 }

 @Test
 public void testInclusiveBetween_Long() {
 Validate.inclusiveBetween(10L, 20L, 15L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Long_customMessage_lessThanStart() {
 Validate.inclusiveBetween(10L, 20L, 5L, "The value %d is not in the specified inclusive range of %d to %d", 4L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Long_customMessage_greaterThanEnd() {
 Validate.inclusiveBetween(10L, 20L, 25L, "The value %d is not in the specified inclusive range of %d to %d", 24L);
 }

 @Test
 public void testInclusiveBetween_Double() {
 Validate.inclusiveBetween(5.5, 10.5, 7.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Double_customMessage_lessThanStart() {
 Validate.inclusiveBetween(5.5, 10.5, 4.5, "The value %f is not in the specified inclusive range of %f to %f", 3.5F);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Double_customMessage_greaterThanEnd() {
 Validate.inclusiveBetween(5.5, 10.5, 11.5, "The value %f is not in the specified inclusive range of %f to %f", 12.5F);
 }

 @Test
 public void testExclusiveBetween_Integer() {
 Validate.exclusiveBetween(5, 10, 7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Integer_customMessage_lessThanStart() {
 Validate.exclusiveBetween(5, 10, 4, "The value %d is not in the specified exclusive range of %d to %d", 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Integer_customMessage_greaterThanEnd() {
 Validate.exclusiveBetween(5, 10, 12, "The value %d is not in the specified exclusive range of %d to %d", 11);
 }

 @Test
 public void testExclusiveBetween_Long() {
 Validate.exclusiveBetween(10L, 20L, 15L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Long_customMessage_lessThanStart() {
 Validate.exclusiveBetween(10L, 20L, 5L, "The value %d is not in the specified exclusive range of %d to %d", 4L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Long_customMessage_greaterThanEnd() {
 Validate.exclusiveBetween(10L, 20L, 25L, "The value %d is not in the specified exclusive range of %d to %d", 24L);
 }

 @Test
 public void testExclusiveBetween_Double() {
 Validate.exclusiveBetween(5.5, 10.5, 7.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Double_customMessage_lessThanStart() {
 Validate.exclusiveBetween(5.5, 10.5, 4.5, "The value %f is not in the specified exclusive range of %f to %f", 3.5F);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Double_customMessage_greaterThanEnd() {
 Validate.exclusiveBetween(5.5, 10.5, 11.5, "The value %f is not in the specified exclusive range of %f to %f", 12.5F);
 }

 @Test
 public void testInstanceOf_Class_Object() {
 Validate.isInstanceOf(String.class, "abc");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInstanceOf_Class_Object_customMessage() {
 Validate.isInstanceOf(StringBuilder.class, "abc", "Expected type: %s, actual: %s", String.class.getName());
 }

 @Test
 public void testAssignableFrom_Class_Class() {
 Validate.isAssignableFrom(String.class, StringBuilder.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAssignableFrom_Class_Class_customMessage() {
 Validate.isAssignableFrom(String.class, ArrayList.class, "Cannot assign a %s to a %s", StringBuilder.class.getName());
 }
}