package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true, "Expected: true, but was: false");
 Validate.isTrue(false, "Expected: false, but was: true", 1L);
 Validate.isTrue(1 > 2, "Expected: false, but was: true", 1.5);
 Validate.isTrue(1 > 2, "Expected: false", (Object[]) new Object[]{1, 2, 3});
 Validate.isTrue(1 > 2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrue_failure() {
 Validate.isTrue(false, "Expected: true");
 }

 @Test
 public void testNotNull() {
 Validate.notNull(new Object());
 Validate.notNull(null, "Expected: not null");
 }

 @Test(expected = NullPointerException.class)
 public void testNotNull_failure() {
 Validate.notNull(null);
 }

 @Test
 public void testNotEmpty_Array() {
 Validate.notEmpty(new Object[0], "Expected: not empty array", (Object[]) new Object[]{});
 Validate.notEmpty(new Object[]{1, 2, 3}, "Expected: not empty array");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Array_failure() {
 Validate.notEmpty(new Object[0], "Expected: not empty array", new Object[]{});
 }

 @Test
 public void testNotEmpty_Collection() {
 Validate.notEmpty(Collections.emptyList(), "Expected: not empty collection", (Object[]) new Object[]{});
 Validate.notEmpty(Arrays.asList(1, 2, 3), "Expected: not empty collection");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Collection_failure() {
 Validate.notEmpty(Collections.emptyList(), "Expected: not empty collection", new Object[]{});
 }

 @Test
 public void testNotEmpty_Map() {
 Validate.notEmpty(Collections.emptyMap(), "Expected: not empty map", (Object[]) new Object[]{});
 Validate.notEmpty(Collections.singletonMap("key", 1), "Expected: not empty map");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_Map_failure() {
 Validate.notEmpty(Collections.emptyMap(), "Expected: not empty map", new Object[]{});
 }

 @Test
 public void testNotEmpty_CharSequence() {
 Validate.notEmpty("", "Expected: not empty character sequence");
 Validate.notEmpty("abc", "Expected: not empty character sequence");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmpty_CharSequence_failure() {
 Validate.notEmpty("", "Expected: not empty character sequence");
 }

 @Test
 public void testNoNullElements_Array() {
 Validate.noNullElements(new Object[0], "Expected: no null elements in array at index: %d", new Object[]{0});
 Validate.noNullElements(new Object[]{null, 1, 2}, "Expected: no null elements in array");
 Validate.noNullElements(new Object[]{1, 2, 3}, "Expected: no null elements in array");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_Array_failure() {
 Validate.noNullElements(new Object[0], "Expected: no null elements in array at index: %d", new Object[]{1});
 }

 @Test
 public void testNoNullElements_Collection() {
 Validate.noNullElements(Collections.emptyList(), "Expected: no null elements in collection at index: %d", new Object[]{0});
 Validate.noNullElements(Arrays.asList(1, null, 3), "Expected: no null elements in collection");
 Validate.noNullElements(Arrays.asList(1, 2, 3), "Expected: no null elements in collection");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElements_Collection_failure() {
 Validate.noNullElements(Collections.emptyList(), "Expected: no null elements in collection at index: %d", new Object[]{1});
 }

 @Test
 public void testValidIndex_Array() {
 Validate.validIndex(new Object[0], 1, "Expected: valid index in array: %d", 1);
 Validate.validIndex(new Object[0], -1, "Expected: valid index in array: %d", 1);
 Validate.validIndex(new Object[0], 5, "Expected: valid index in array: %d", 1);
 Validate.validIndex(new Object[]{1, 2, 3}, 1, "Expected: valid index in array: %d", 1);
 Validate.validIndex(new Object[]{1, 2, 3}, -1, "Expected: valid index in array: %d", 1);
 Validate.validIndex(new Object[]{1, 2, 3}, 5, "Expected: valid index in array: %d", 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_Array_failure() {
 Validate.validIndex(new Object[0], 1);
 }

 @Test
 public void testValidIndex_Collection() {
 Validate.validIndex(Collections.emptyList(), 1, "Expected: valid index in collection: %d", 1);
 Validate.validIndex(Arrays.asList(1, 2, 3), 1, "Expected: valid index in collection: %d", 1);
 Validate.validIndex(Arrays.asList(1, 2, 3), -1, "Expected: valid index in collection: %d", 1);
 Validate.validIndex(Arrays.asList(1, 2, 3), 5, "Expected: valid index in collection: %d", 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndex_Collection_failure() {
 Validate.validIndex(Collections.emptyList(), 1);
 }

 @Test
 public void testValidIndex_CharSequence() {
 Validate.validIndex("", 1, "Expected: valid index in character sequence: %d", 1);
 Validate.validIndex("abc", 1, "Expected: valid index in character sequence: %d", 1);
 Validate.validIndex("abc", -1, "Expected: valid index in character sequence: %d", 1);
 Validate.validIndex("abc", 5, "Expected: valid index in character sequence: %d", 1);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testValidIndex_CharSequence_failure() {
 Validate.validIndex("abc", 1);
 }

 @Test
 public void testValidState() {
 Validate.validState(true);
 Validate.validState(false, "Expected: valid state: false");
 }

 @Test(expected = IllegalStateException.class)
 public void testValidState_failure() {
 Validate.validState(false);
 }

 @Test
 public void testMatchesPattern() {
 Validate.matchesPattern("123", "^[0-9]{3}$");
 Validate.matchesPattern("abc", "^[a-z]+$");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPattern_failure() {
 Validate.matchesPattern("123", "^[a-z]+$");
 }

 @Test
 public void testInclusiveBetween_Long() {
 Validate.inclusiveBetween(0L, 5L, 3L);
 Validate.inclusiveBetween(0L, 5L, 5L, "Expected: value is in the inclusive range: %d", 5);
 Validate.inclusiveBetween(0L, 5L, 1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Long_failure() {
 Validate.inclusiveBetween(0L, 5L, 6L);
 }

 @Test
 public void testInclusiveBetween_Double() {
 Validate.inclusiveBetween(0.0, 5.0, 3.0);
 Validate.inclusiveBetween(0.0, 5.0, 5.0, "Expected: value is in the inclusive range: %f", 5.0);
 Validate.inclusiveBetween(0.0, 5.0, 1.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetween_Double_failure() {
 Validate.inclusiveBetween(0.0, 5.0, 6.0);
 }

 @Test
 public void testExclusiveBetween_Long() {
 Validate.exclusiveBetween(0L, 5L, 3L);
 Validate.exclusiveBetween(0L, 5L, 2L, "Expected: value is in the exclusive range: %d", 2);
 Validate.exclusiveBetween(0L, 5L, 6L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Long_failure() {
 Validate.exclusiveBetween(0L, 5L, 5L);
 }

 @Test
 public void testExclusiveBetween_Double() {
 Validate.exclusiveBetween(0.0, 5.0, 3.0);
 Validate.exclusiveBetween(0.0, 5.0, 2.5, "Expected: value is in the exclusive range: %f", 2.5);
 Validate.exclusiveBetween(0.0, 5.0, 6.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetween_Double_failure() {
 Validate.exclusiveBetween(0.0, 5.0, 5.0);
 }

 @Test
 public void testIsInstanceOf() {
 Validate.isInstanceOf(String.class, "abc");
 Validate.isInstanceOf(Integer.class, 1);
 Validate.isInstanceOf(Integer.class, new Integer(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOf_failure() {
 Validate.isInstanceOf(String.class, 1);
 }

 @Test
 public void testIsAssignableFrom() {
 Validate.isAssignableFrom(Number.class, Integer.class);
 Validate.isAssignableFrom(Integer.class, Number.class);
 Validate.isAssignableFrom(String.class, Object.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFrom_failure() {
 Validate.isAssignableFrom(Integer.class, String.class);
 }
}