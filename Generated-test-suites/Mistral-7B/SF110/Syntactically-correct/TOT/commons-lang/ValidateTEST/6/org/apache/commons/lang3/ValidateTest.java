package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

 @Test
 public void testIsTrueWithBooleanExpressionAndMessage() {
 Validate.isTrue(true, "Expected true, but got false", 42);
 Validate.isTrue(false, "Expected true, but got false", 42);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanExpressionAndMessageAndValue() {
 Validate.isTrue(false, "Expected true, but got false", 42);
 }

 @Test
 public void testIsTrueWithBooleanExpression() {
 Validate.isTrue(true);
 Validate.isTrue(false);
 }

 @Test
 public void testNotNullWithNonNullObject() {
 Validate.notNull(new Object());
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNullObjectAndMessage() {
 Validate.notNull(null, "Expected non-null object, but got null", new Object[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNullObject() {
 Validate.notNull(null);
 }

 @Test
 public void testIsTrueWithBooleanArrayAndMessageAndValues() {
 Validate.isTrue(new boolean[] { true, true, true }, "Expected all elements in the array to be true, but got %s or %s or %s to be false", 0, 1, 2);
 Validate.isTrue(new boolean[] { true, true, true }, "Expected all elements in the array to be true");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithFalseBooleanArrayAndMessageAndValues() {
 Validate.isTrue(new boolean[] { false, true, true }, "Expected all elements in the array to be true, but got %s or %s or %s to be false", 0, 1, 2);
 }

 @Test
 public void testIsTrueWithBooleanArrayAndMessage() {
 Validate.isTrue(new boolean[] { true, true, true }, "Expected all elements in the array to be true");
 }

 @Test
 public void testIsTrueWithBooleanArray() {
 Validate.isTrue(new boolean[] { true, true, true });
 Validate.isTrue(new boolean[] { false, false, false });
 }

 @Test
 public void testNoNullElementsWithNullArrayAndMessage() {
 Validate.noNullElements(null, "Expected no null elements in the array, but got null at index 0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsWithNullArray() {
 Validate.noNullElements(null);
 }

 @Test
 public void testNoNullElementsWithArrayContainingNulls() {
 Object[] array = { new Object(), null, new Object(), null, new Object() };
 Validate.noNullElements(array, "Expected no null elements in the array, but got null at index %d", 2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsWithArrayContainingNullsAndMessage() {
 Object[] array = { new Object(), null, new Object(), null, new Object() };
 Validate.noNullElements(array, "Expected no null elements in the array, but got null at index %d", 2, 3, 4);
 }

 @Test
 public void testIsInstanceOfWithArrayAndInstanceOfClassObject() {
 Validate.isInstanceOf(Object[].class, new Object[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithArrayAndInstanceOfClassNotArray() {
 Validate.isInstanceOf(String.class, new Object[0]);
 }

 @Test
 public void testIsAssignableFromWithClassAndSubclass() {
 Validate.isAssignableFrom(Number.class, Integer.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithClassAndNonRelatedClass() {
 Validate.isAssignableFrom(Number.class, String.class);
 }

 @Test
 public void testMatchesPatternWithStringAndPattern() {
 Validate.matchesPattern("1234abcd5678", ".*[a-z].*");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithStringAndIncorrectPattern() {
 Validate.matchesPattern("1234abcd5678", "[a-z]");
 }

 @Test
 public void testInclusiveBetweenWithLongAndStartAndEnd() {
 Validate.inclusiveBetween(1L, 10L, 5L);
 Validate.inclusiveBetween(1L, 10L, 15L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithLongAndStartAndEndAndMessage() {
 Validate.inclusiveBetween(1L, 10L, 5L, "The value 5 is not in the specified inclusive range of 1 to 10");
 }

 @Test
 public void testInclusiveBetweenWithLongAndStartAndEndAndMessageEmpty() {
 Validate.inclusiveBetween(1L, 10L, 5L, "");
 }

 @Test
 public void testExclusiveBetweenWithLongAndStartAndEnd() {
 Validate.exclusiveBetween(1L, 10L, 5L);
 Validate.exclusiveBetween(1L, 10L, 4L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithLongAndStartAndEndAndMessage() {
 Validate.exclusiveBetween(1L, 10L, 5L, "The value 5 is not in the specified exclusive range of 1 to 10");
 }

 @Test
 public void testExclusiveBetweenWithLongAndStartAndEndAndMessageEmpty() {
 Validate.exclusiveBetween(1L, 10L, 5L, "");
 }

 @Test
 public void testValidIndexWithArrayAndIndex() {
 Validate.validIndex(new String[] { "apple", "banana", "cherry" }, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithArrayAndNegativeIndex() {
 Validate.validIndex(new String[] { "apple", "banana", "cherry" }, -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithArrayAndIndexGreaterThanLength() {
 Validate.validIndex(new String[] { "apple", "banana", "cherry" }, 3);
 }

 @Test
 public void testValidIndexWithCollectionAndIndex() {
 Validate.validIndex(Arrays.asList("apple", "banana", "cherry"), 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithCollectionAndNegativeIndex() {
 Validate.validIndex(Arrays.asList("apple", "banana", "cherry"), -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexWithCollectionAndIndexGreaterThanSize() {
 Validate.validIndex(Arrays.asList("apple", "banana", "cherry"), 3);
 }

 @Test
 public void testValidStateWithBooleanExpression() {
 Validate.validState(true);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateWithFalseBooleanExpression() {
 Validate.validState(false);
 }

 @Test
 public void testValidStateWithBooleanExpressionAndMessage() {
 Validate.validState(true, "The validated state is false");
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateWithFalseBooleanExpressionAndMessage() {
 Validate.validState(false, "The validated state is false");
 }
}