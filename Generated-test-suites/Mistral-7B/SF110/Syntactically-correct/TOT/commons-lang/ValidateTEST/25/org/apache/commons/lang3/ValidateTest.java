package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class ValidateTest {

 @Test
 public void testIsTrueBooleanBooleanString() {
 Validate.isTrue(true, "Expected true, but was false", "actualValue");
 Validate.isTrue(false, "Expected true, but was false: %s", "false");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueBooleanBooleanStringFailure() {
 Validate.isTrue(false, "Expected true, but was false", "actualValue");
 }

 @Test
 public void testIsTrueBooleanBooleanDouble() {
 Validate.isTrue(true, "Expected true, but was false", 42.0);
 Validate.isTrue(false, "Expected true, but was false: %f", 3.14);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueBooleanBooleanDoubleFailure() {
 Validate.isTrue(false, "Expected true, but was false", 3.14);
 }

 @Test
 public void testIsTrueBooleanBooleanObject() {
 Validate.isTrue(true, "Expected true, but was false", new Object[]{"one", "two", "three"});
 Validate.isTrue(false, "Expected true, but was false: %s", new Object[]{"one", "two", null});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueBooleanBooleanObjectFailure() {
 Validate.isTrue(false, "Expected true, but was false: %s", new Object[]{"one", "two", null});
 }

 @Test
 public void testIsTrueBoolean() {
 Validate.isTrue(true);
 Validate.isTrue(false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueBooleanFailure() {
 Validate.isTrue(false);
 }

 @Test
 public void testNotNull() {
 Validate.notNull(new Object());
 Validate.notNull(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullFailure() {
 Validate.notNull(null);
 }

 @Test
 public void testNotEmptyArray() {
 Validate.notEmpty(new String[]{"one", "two", "three"});
 Validate.notEmpty(new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArrayFailure() {
 Validate.notEmpty(new String[0]);
 }

 @Test
 public void testNotEmptyCollection() {
 Validate.notEmpty(Arrays.asList(1, 2, 3));
 Validate.notEmpty(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCollectionFailure() {
 Validate.notEmpty(Collections.emptyList());
 }

 @Test
 public void testNotEmptyMap() {
 Validate.notEmpty(new HashMap<>());
 Validate.notEmpty(Collections.emptyMap());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyMapFailure() {
 Validate.notEmpty(Collections.emptyMap());
 }

 @Test
 public void testNotEmptyCharSequence() {
 Validate.notEmpty("one two three");
 Validate.notEmpty("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCharSequenceFailure() {
 Validate.notEmpty("");
 }

 @Test
 public void testNotBlankCharSequence() {
 Validate.notBlank("one two three");
 Validate.notBlank(" ");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotBlankCharSequenceFailure() {
 Validate.notBlank(" ");
 }

 @Test
 public void testNoNullElementsArray() {
 Validate.noNullElements(new String[]{"one", "two", "three"});
 Validate.noNullElements(new String[0]);
 Validate.noNullElements(new String[]{"one", null, "three"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsArrayFailure() {
 Validate.noNullElements(new String[]{"one", null, "three"});
 }

 @Test
 public void testNoNullElementsIterable() {
 Validate.noNullElements(Arrays.asList(1, 2, 3));
 Validate.noNullElements(Collections.emptyList());
 Validate.noNullElements(Arrays.asList(Collections.singletonMap("key", null)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsIterableFailure() {
 Validate.noNullElements(Arrays.asList(Collections.singletonMap("key", null)));
 }

 @Test
 public void testValidIndexArray() {
 Validate.validIndex(new String[]{"one", "two", "three"}, 1);
 Validate.validIndex(new String[]{"one", "two", "three"}, -1);
 Validate.validIndex(new String[]{"one", "two", "three"}, 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexArrayFailure() {
 Validate.validIndex(new String[]{"one", "two", "three"}, -1);
 }

 @Test
 public void testValidIndexCollection() {
 Validate.validIndex(Arrays.asList(1, 2, 3), 1);
 Validate.validIndex(Arrays.asList(1, 2, 3), -1);
 Validate.validIndex(Arrays.asList(1, 2, 3), 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCollectionFailure() {
 Validate.validIndex(Arrays.asList(1, 2, 3), -1);
 }

 @Test
 public void testValidIndexCharSequence() {
 Validate.validIndex("one two three", 1);
 Validate.validIndex("one two three", -1);
 Validate.validIndex("one two three", 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCharSequenceFailure() {
 Validate.validIndex("one two three", -1);
 }

 @Test
 public void testValidStateBoolean() {
 Validate.validState(true);
 Validate.validState(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateBooleanFailure() {
 Validate.validState(false);
 }

 @Test
 public void testMatchesPatternStringString() {
 String input = "hello world";
 Validate.matchesPattern(input, "hello\\s+[wW][or]ld");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternStringStringFailure() {
 String input = "hello world";
 Validate.matchesPattern(input, "hello\\s+[wW][or]ldo");
 }

 @Test
 public void testMatchesPatternStringStringMessage() {
 String input = "hello world";
 Validate.matchesPattern(input, "hello\\s+[wW][or]ld", "Input does not match pattern: %s", "input");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternStringStringMessageFailure() {
 String input = "hello world";
 Validate.matchesPattern(input, "hello\\s+[wW][or]ldo", "Input does not match pattern: %s", "input");
 }

 @Test
 public void testInclusiveBetweenComparableComparable() {
 Validate.inclusiveBetween(1, 3, 2);
 Validate.inclusiveBetween(1, 3, 1);
 Validate.inclusiveBetween(1, 3, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenComparableComparableFailure() {
 Validate.inclusiveBetween(1, 3, 0);
 }

 @Test
 public void testInclusiveBetweenLongLong() {
 Validate.inclusiveBetween(1, 3, 2L);
 Validate.inclusiveBetween(1, 3, 1L);
 Validate.inclusiveBetween(1, 3, 3L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongLongFailure() {
 Validate.inclusiveBetween(1, 3, 0L);
 }

 @Test
 public void testInclusiveBetweenDoubleDouble() {
 Validate.inclusiveBetween(1.0, 3.0, 2.0);
 Validate.inclusiveBetween(1.0, 3.0, 1.0);
 Validate.inclusiveBetween(1.0, 3.0, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleDoubleFailure() {
 Validate.inclusiveBetween(1.0, 3.0, 0.0);
 }

 @Test
 public void testExclusiveBetweenComparableComparable() {
 Validate.exclusiveBetween(1, 3, 2);
 Validate.exclusiveBetween(1, 3, 1);
 Validate.exclusiveBetween(1, 3, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenComparableComparableFailure() {
 Validate.exclusiveBetween(1, 3, 0);
 }

 @Test
 public void testExclusiveBetweenLongLong() {
 Validate.exclusiveBetween(1, 3, 2L);
 Validate.exclusiveBetween(1, 3, 1L);
 Validate.exclusiveBetween(1, 3, 3L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongLongFailure() {
 Validate.exclusiveBetween(1, 3, 0L);
 }

 @Test
 public void testExclusiveBetweenDoubleDouble() {
 Validate.exclusiveBetween(1.0, 3.0, 2.0);
 Validate.exclusiveBetween(1.0, 3.0, 1.0);
 Validate.exclusiveBetween(1.0, 3.0, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleDoubleFailure() {
 Validate.exclusiveBetween(1.0, 3.0, 0.0);
 }

 @Test
 public void testIsInstanceOfClassObject() {
 Validate.isInstanceOf(String.class, "String");
 Validate.isInstanceOf(Integer.class, 42);
 Validate.isInstanceOf(String.class, new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfClassObjectFailure() {
 Validate.isInstanceOf(Integer.class, "String");
 }

 @Test
 public void testIsAssignableFromClassClass() {
 Validate.isAssignableFrom(String.class, StringBuffer.class);
 Validate.isAssignableFrom(StringBuffer.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromClassClassFailure() {
 Validate.isAssignableFrom(String.class, Integer.class);
 }
}