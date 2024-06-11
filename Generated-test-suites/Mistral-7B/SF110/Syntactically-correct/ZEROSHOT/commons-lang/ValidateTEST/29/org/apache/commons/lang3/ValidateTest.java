package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true, "%s should be true", "expected true");
 Validate.isTrue(false, "%s should be false", "expected false");
 Validate.isTrue(1 > 0, "%s should be true", "expected result of 1 > 0");
 Validate.isTrue(2.0 > 1.0, "%s should be true", "expected result of 2.0 > 1.0");
 Validate.isTrue(Arrays.asList(1, 2, 3).contains(2), "%s should be true", "expected list to contain 2");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithException() {
 Validate.isTrue(false, "%s should be true", "expected false");
 }

 @Test
 public void testNotNull() {
 Validate.notNull(new Object(), "%s should not be null");
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNull() {
 Validate.notNull(null, "%s should not be null");
 }

 @Test
 public void testNotEmptyArray() {
 Validate.notEmpty((Object[]) new Object[0], "%s should not be empty", "expected empty array");
 Validate.notEmpty(new Object[] { 1, 2, 3 }, "%s should not be empty", "expected non-empty array");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArrayWithException() {
 Validate.notEmpty((Object[]) new Object[0], "%s should not be empty", "expected empty array");
 }

 @Test
 public void testNotEmptyCollection() {
 Validate.notEmpty(new ArrayList<>(), "%s should not be empty", "expected empty collection");
 Validate.notEmpty(Arrays.asList(1, 2, 3), "%s should not be empty", "expected non-empty collection");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCollectionWithException() {
 Validate.notEmpty(new ArrayList<>(), "%s should not be empty", "expected empty collection");
 }

 @Test
 public void testNotEmptyMap() {
 Validate.notEmpty(new HashMap<>(), "%s should not be empty", "expected empty map");
 Validate.notEmpty(Map.of("key1", 1, "key2", 2), "%s should not be empty", "expected non-empty map");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyMapWithException() {
 Validate.notEmpty(new HashMap<>(), "%s should not be empty", "expected empty map");
 }

 @Test
 public void testNoNullElementsArray() {
 Validate.noNullElements(new Object[] { 1, 2, 3 }, "%s should not contain null elements", "expected array without null elements");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsArrayWithException() {
 Validate.noNullElements(new Object[] { null, 2, 3 }, "%s should not contain null elements", "expected array without null elements");
 }

 @Test
 public void testNoNullElementsCollection() {
 Validate.noNullElements(Arrays.asList(1, 2, 3), "%s should not contain null elements", "expected collection without null elements");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsCollectionWithException() {
 Validate.noNullElements(Arrays.asList(1, null, 3), "%s should not contain null elements", "expected collection without null elements");
 }

 @Test
 public void testValidIndexArray() {
 Validate.validIndex(new Object[] { 1, 2, 3 }, 1, "%s should be a valid index for the array", "expected valid index for array");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexArrayOutOfBounds() {
 Validate.validIndex(new Object[] { 1, 2, 3 }, 4, "%s should be a valid index for the array", "expected valid index for array");
 }

 @Test
 public void testValidIndexCollection() {
 Validate.validIndex(Arrays.asList(1, 2, 3), 1, "%s should be a valid index for the collection", "expected valid index for collection");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCollectionOutOfBounds() {
 Validate.validIndex(Arrays.asList(1, 2, 3), 4, "%s should be a valid index for the collection", "expected valid index for collection");
 }

 @Test
 public void testValidIndexCharSequence() {
 Validate.validIndex("abcdef", 2, "%s should be a valid index for the character sequence", "expected valid index for char sequence");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCharSequenceOutOfBounds() {
 Validate.validIndex("abcdef", 6, "%s should be a valid index for the character sequence", "expected valid index for char sequence");
 }

 @Test
 public void testValidState() {
 Validate.validState(true, "%s should be true", "expected true state");
 Validate.validState(false, "%s should be false", "expected false state");
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateFalse() {
 Validate.validState(false, "%s should be true", "expected false state");
 }

 @Test
 public void testMatchesPattern() {
 String input = "abcd1234ef";
 Pattern pattern = Pattern.compile("^[a-z]+[0-9]+[a-z]+$");
 Validate.matchesPattern(input, pattern.pattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternMismatch() {
 String input = "abcd1234ef";
 Pattern pattern = Pattern.compile("^[a-z]+[a-z]+[a-z]+$");
 Validate.matchesPattern(input, pattern.pattern());
 }

 @Test
 public void testInclusiveBetweenLong() {
 Validate.inclusiveBetween(0L, 5L, 3L);
 Validate.inclusiveBetween(0L, 5L, 6L, "%s should be within inclusive range [%d, %d]", 3, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongOutOfRange() {
 Validate.inclusiveBetween(0L, 5L, 1L, "%s should be within inclusive range [%d, %d]", 3, 5);
 Validate.inclusiveBetween(0L, 5L, 7L, "%s should be within inclusive range [%d, %d]", 3, 5);
 }

 @Test
 public void testInclusiveBetweenDouble() {
 Validate.inclusiveBetween(0.0, 5.0, 3.0);
 Validate.inclusiveBetween(0.0, 5.0, 6.0, "%s should be within inclusive range [%f, %f]", 3.0, 5.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleOutOfRange() {
 Validate.inclusiveBetween(0.0, 5.0, 1.0, "%s should be within inclusive range [%f, %f]", 3.0, 5.0);
 Validate.inclusiveBetween(0.0, 5.0, 7.0, "%s should be within inclusive range [%f, %f]", 3.0, 5.0);
 }

 @Test
 public void testExclusiveBetweenLong() {
 Validate.exclusiveBetween(0L, 5L, 3L);
 Validate.exclusiveBetween(0L, 5L, 2L, "%s should be within exclusive range [%d, %d]", 3, 5);
 Validate.exclusiveBetween(0L, 5L, 4L, "%s should be within exclusive range [%d, %d]", 3, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongOutOfRange() {
 Validate.exclusiveBetween(0L, 5L, 1L, "%s should be within exclusive range [%d, %d]", 3, 5);
 Validate.exclusiveBetween(0L, 5L, 6L, "%s should be within exclusive range [%d, %d]", 3, 5);
 }

 @Test
 public void testExclusiveBetweenDouble() {
 Validate.exclusiveBetween(0.0, 5.0, 3.0);
 Validate.exclusiveBetween(0.0, 5.0, 2.0, "%s should be within exclusive range [%f, %f]", 3.0, 5.0);
 Validate.exclusiveBetween(0.0, 5.0, 4.0, "%s should be within exclusive range [%f, %f]", 3.0, 5.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleOutOfRange() {
 Validate.exclusiveBetween(0.0, 5.0, 1.0, "%s should be within exclusive range [%f, %f]", 3.0, 5.0);
 Validate.exclusiveBetween(0.0, 5.0, 6.0, "%s should be within exclusive range [%f, %f]", 3.0, 5.0);
 }

 @Test
 public void testIsInstanceOf() {
 Validate.isInstanceOf(String.class, "abcdef");
 Validate.isInstanceOf(Integer.class, 1);
 Validate.isInstanceOf(List.class, Arrays.asList(1, 2, 3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfMismatch() {
 Validate.isInstanceOf(String.class, 1);
 Validate.isInstanceOf(Integer.class, "abcdef");
 Validate.isInstanceOf(List.class, "abcdef");
 }

 @Test
 public void testIsAssignableFrom() {
 Validate.isAssignableFrom(String.class, StringBuffer.class);
 Validate.isAssignableFrom(List.class, ArrayList.class);
 Validate.isAssignableFrom(ArrayList.class, List.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromMismatch() {
 Validate.isAssignableFrom(String.class, Integer.class);
 Validate.isAssignableFrom(List.class, StringBuffer.class);
 Validate.isAssignableFrom(StringBuffer.class, List.class);
 }
}