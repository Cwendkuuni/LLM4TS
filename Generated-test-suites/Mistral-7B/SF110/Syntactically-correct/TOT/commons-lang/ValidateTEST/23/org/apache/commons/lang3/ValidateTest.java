package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValidateTest {

 @Test
 public void testTrueWithMessageAndLong() {
 Validate.isTrue(true, "The validated expression is false: %d", 1L);
 Validate.isTrue(true, "The validated expression is false: %d", 2L);
 Validate.isTrue(false, "The validated expression is false: %d", 1L);
 Validate.isTrue(false, "The validated expression is false: %d", 2L);
 }

 @Test
 public void testTrueWithMessageAndDouble() {
 Validate.isTrue(true, "The validated expression is false: %f", 1.0);
 Validate.isTrue(true, "The validated expression is false: %f", 2.0);
 Validate.isTrue(false, "The validated expression is false: %f", 1.0);
 Validate.isTrue(false, "The validated expression is false: %f", 2.0);
 }

 @Test
 public void testTrueWithMessageAndObjectArray() {
 Validate.isTrue(true, "The validated expression is false: %s", new Object[]{"one", "two", "three"});
 Validate.isTrue(true, "The validated expression is false: %s", new Object[]{"four", "five", "six"});
 Validate.isTrue(false, "The validated expression is false: %s", new Object[]{"one", "two", "three"});
 Validate.isTrue(false, "The validated expression is false: %s", new Object[]{"four", "five", "six"});
 }

 @Test
 public void testTrueWithoutMessage() {
 Validate.isTrue(true);
 Validate.isTrue(true);
 Validate.isTrue(false);
 Validate.isTrue(false);
 }

 @Test
 public void testNotNullWithMessageAndObject() {
 Validate.notNull(null, "The validated object is null");
 Validate.notNull(null, "The validated object is null", "errorMessage");
 Validate.notNull(new Object(), "The validated object is null");
 Validate.notNull(new Object(), "The validated object is null", "errorMessage");
 }

 @Test
 public void testNotNullWithMessageAndObjectArray() {
 Validate.notNull(new Object[0], "The validated array is empty", "errorMessage1", "errorMessage2");
 Validate.notNull(new Object[0], "The validated array is empty");
 Validate.notNull(new Object[]{"one", "two", "three"}, "The validated array is empty", "errorMessage1", "errorMessage2");
 Validate.notNull(new Object[]{"one", "two", "three"});
 }

 @Test
 public void testNotEmptyWithMessageAndArray() {
 Validate.notEmpty(new Object[0], "The validated array is empty", "errorMessage");
 Validate.notEmpty(new Object[0]);
 Validate.notEmpty(new Object[]{1, 2, 3}, "The validated array is empty", "errorMessage");
 Validate.notEmpty(new Object[]{1, 2, 3});
 }

 @Test
 public void testNotEmptyWithMessageAndCollection() {
 Validate.notEmpty(Collections.emptyList(), "The validated collection is empty", "errorMessage");
 Validate.notEmpty(Collections.emptyList());
 Validate.notEmpty(Arrays.asList(1, 2, 3), "The validated collection is empty", "errorMessage");
 Validate.notEmpty(Arrays.asList(1, 2, 3));
 }

 @Test
 public void testNotEmptyWithMessageAndMap() {
 Validate.notEmpty(Collections.emptyMap(), "The validated map is empty", "errorMessage");
 Validate.notEmpty(Collections.emptyMap());
 Validate.notEmpty(Collections.singletonMap("key", 1), "The validated map is empty", "errorMessage");
 Validate.notEmpty(Collections.singletonMap("key", 1));
 }

 @Test
 public void testNotEmptyWithMessageAndCharSequence() {
 Validate.notEmpty("", "The validated character sequence is empty", "errorMessage");
 Validate.notEmpty("");
 Validate.notEmpty("hello", null, "The validated character sequence is empty", "errorMessage");
 Validate.notEmpty("hello");
 }

 @Test
 public void testNotBlankWithMessageAndCharSequence() {
 Validate.notBlank("", "The validated character sequence is blank", "errorMessage");
 Validate.notBlank("");
 Validate.notBlank("hello", null, "The validated character sequence is blank", "errorMessage");
 Validate.notBlank("hello");
 }

 @Test
 public void testNoNullElementsWithMessageAndArray() {
 Validate.noNullElements(new Object[0], "The validated array contains null element at index: %d", 0);
 Validate.noNullElements(new Object[0]);
 Validate.noNullElements(new Object[]{null, 1, 2, 3}, "The validated array contains null element at index: %d", 1);
 Validate.noNullElements(new Object[]{null, 1, 2, 3});
 }

 @Test
 public void testNoNullElementsWithMessageAndIterable() {
 Validate.noNullElements(Collections.emptyList(), "The validated collection contains null element at index: %d");
 Validate.noNullElements(Collections.emptyList());
 Validate.noNullElements(Arrays.asList(1, null, 2, 3), "The validated collection contains null element at index: %d", 2);
 Validate.noNullElements(Arrays.asList(1, null, 2, 3));
 }

 @Test
 public void testValidIndexWithMessageAndArray() {
 Validate.validIndex(new Object[0], 10, "The validated array index is invalid: %d");
 Validate.validIndex(new Object[0]);
 Validate.validIndex(new Object[0], 0);
 Validate.validIndex(new Object[0], 1);
 }

 @Test
 public void testValidIndexWithMessageAndCharSequence() {
 Validate.validIndex("", 10, "The validated character sequence index is invalid: %d");
 Validate.validIndex("");
 Validate.validIndex("", 0);
 Validate.validIndex("", 1);
 }

 @Test
 public void testValidIndexWithMessageAndCollection() {
 Validate.validIndex(Collections.emptyList(), 10);
 Validate.validIndex(Collections.emptyList());
 Validate.validIndex(Arrays.asList(1, 2, 3), 0);
 Validate.validIndex(Arrays.asList(1, 2, 3), 1);
 }

 @Test
 public void testValidState() {
 Validate.validState(true);
 Validate.validState(false);
 Validate.validState(false, "The validated state is false");
 }

 @Test
 public void testMatchesPatternWithMessageAndCharSequence() {
 Validate.matchesPattern("abc", "[a-z]+");
 Validate.matchesPattern("abc", "[a-z]+", "The string %s does not match the pattern %s");
 Validate.matchesPattern("123", "[a-z]+");
 Validate.matchesPattern("123", "[a-z]+", "The string %s does not match the pattern %s");
 }

 @Test
 public void testInclusiveBetweenWithMessageAndComparable() {
 Validate.inclusiveBetween(1, 3, 2);
 Validate.inclusiveBetween(1, 3, 2, "The value %s is not in the specified inclusive range of %s to %s");
 Validate.inclusiveBetween(1, 3, 4);
 Validate.inclusiveBetween(1, 3, 4, "The value %s is not in the specified inclusive range of %s to %s");
 }

 @Test
 public void testInclusiveBetweenWithMessageAndLong() {
 Validate.inclusiveBetween(1, 3, 2L);
 Validate.inclusiveBetween(1, 3, 2L, "The value %s is not in the specified inclusive range of %s to %s");
 Validate.inclusiveBetween(1, 3, 4L);
 Validate.inclusiveBetween(1, 3, 4L, "The value %s is not in the specified inclusive range of %s to %s");
 }

 @Test
 public void testInclusiveBetweenWithMessageAndDouble() {
 Validate.inclusiveBetween(1.0, 3.0, 2.0);
 Validate.inclusiveBetween(1.0, 3.0, 2.0, "The value %s is not in the specified inclusive range of %s to %s");
 Validate.inclusiveBetween(1.0, 3.0, 4.0);
 Validate.inclusiveBetween(1.0, 3.0, 4.0, "The value %s is not in the specified inclusive range of %s to %s");
 }

 @Test
 public void testExclusiveBetweenWithMessageAndComparable() {
 Validate.exclusiveBetween(1, 3, 2);
 Validate.exclusiveBetween(1, 3, 2, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 2);
 Validate.exclusiveBetween(1, 3, 2, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 4);
 Validate.exclusiveBetween(1, 3, 4, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 4);
 Validate.exclusiveBetween(1, 3, 4, "The value %s is not in the specified exclusive range of %s to %s");
 }

 @Test
 public void testExclusiveBetweenWithMessageAndLong() {
 Validate.exclusiveBetween(1, 3, 2L);
 Validate.exclusiveBetween(1, 3, 2L, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 2L);
 Validate.exclusiveBetween(1, 3, 2L, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 4L);
 Validate.exclusiveBetween(1, 3, 4L, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1, 3, 4L);
 Validate.exclusiveBetween(1, 3, 4L, "The value %s is not in the specified exclusive range of %s to %s");
 }

 @Test
 public void testExclusiveBetweenWithMessageAndDouble() {
 Validate.exclusiveBetween(1.0, 3.0, 2.0);
 Validate.exclusiveBetween(1.0, 3.0, 2.0, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1.0, 3.0, 2.0);
 Validate.exclusiveBetween(1.0, 3.0, 2.0, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1.0, 3.0, 4.0);
 Validate.exclusiveBetween(1.0, 3.0, 4.0, "The value %s is not in the specified exclusive range of %s to %s");
 Validate.exclusiveBetween(1.0, 3.0, 4.0);
 Validate.exclusiveBetween(1.0, 3.0, 4.0, "The value %s is not in the specified exclusive range of %s to %s");
 }

 @Test
 public void testIsInstanceOfWithMessageAndClass() {
 Validate.isInstanceOf(String.class, new String("hello"));
 Validate.isInstanceOf(String.class, new String("hello"), "Expected type: %s, actual: %s");
 Validate.isInstanceOf(Integer.class, new Integer(1));
 Validate.isInstanceOf(Integer.class, new Integer(1), "Expected type: %s, actual: %s");
 }

 @Test
 public void testIsInstanceOfWithMessageAndObject() {
 Validate.isInstanceOf(String.class, "hello");
 Validate.isInstanceOf(String.class, "hello", "Expected type: %s, actual: %s");
 Validate.isInstanceOf(Integer.class, 1);
 Validate.isInstanceOf(Integer.class, 1, "Expected type: %s, actual: %s");
 }

 @Test
 public void testIsAssignableFromWithMessageAndClass() {
 Validate.isAssignableFrom(String.class, StringBuffer.class);
 Validate.isAssignableFrom(String.class, StringBuffer.class, "Cannot assign a %s to a %s");
 Validate.isAssignableFrom(String.class, String[].class);
 Validate.isAssignableFrom(String.class, String[].class, "Cannot assign a %s to a %s");
 }

 @Test
 public void testIsAssignableFromWithMessageAndClassObject() {
 Validate.isAssignableFrom(StringBuffer.class, String.class);
 Validate.isAssignableFrom(StringBuffer.class, String.class, "Cannot assign a %s to a %s");
 Validate.isAssignableFrom(String[].class, String.class);
 Validate.isAssignableFrom(String[].class, String.class, "Cannot assign a %s to a %s");
 }

 @Test
 public void testIllegalArgumentExceptionWithMessage() {
 assertThrows(IllegalArgumentException.class, () -> {
 Validate.isTrue(false, "The validated expression is false");
 });
 }

 @Test
 public void testIllegalArgumentExceptionWithMessageAndObjects() {
 assertThrows(IllegalArgumentException.class, () -> {
 Validate.isTrue(false, "The validated expression is false: %s", new Object[]{"errorMessage1", "errorMessage2"});
 });
 }

 @Test
 public void testNullPointerExceptionWithMessage() {
 assertThrows(NullPointerException.class, () -> {
 Validate.notNull(null, "The validated object is null");
 });
 }

 @Test
 public void testNullPointerExceptionWithMessageAndObjects() {
 assertThrows(NullPointerException.class, () -> {
 Validate.notNull(null, "The validated object is null: %s", new Object[]{"errorMessage1", "errorMessage2"});
 });
 }

 @Test
 public void testIndexOutOfBoundsExceptionWithMessage() {
 assertThrows(IndexOutOfBoundsException.class, () -> {
 Validate.validIndex(new Object[0], 10, "The validated array index is invalid: %d");
 });
 }

 @Test
 public void testIndexOutOfBoundsExceptionWithMessageAndObjects() {
 assertThrows(IndexOutOfBoundsException.class, () -> {
 Validate.validIndex(new Object[0], 10, "The validated array index is invalid: %d", new Object[]{1, 2, 3});
 });
 }
}