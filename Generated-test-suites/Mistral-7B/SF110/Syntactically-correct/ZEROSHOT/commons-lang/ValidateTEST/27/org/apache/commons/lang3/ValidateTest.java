package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ValidateTest {

 @Test
 public void testIsTrue() {
 Validate.isTrue(true, "%s should be true", "expectedValue");
 Validate.isTrue(false, "%s should be false", "expectedValue");
 Validate.isTrue(true, "");
 Validate.isTrue(true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsTrueWithException() {
 Validate.isTrue(false, "%s should be true", "expectedValue");
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 Validate.notNull(obj, "%s should not be null", "expectedValue");
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNull() {
 Validate.notNull(null, "%s should not be null", "expectedValue");
 }

 @Test
 public void testNotEmptyArray() {
 int[] array = new int[] {1, 2, 3};
 Validate.notEmpty(array, "%s should not be empty", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyArrayWithEmpty() {
 Validate.notEmpty(new int[0], "%s should not be empty", "expectedValue");
 }

 @Test
 public void testNotEmptyCollection() {
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 Validate.notEmpty(list, "%s should not be empty", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyCollectionWithEmpty() {
 Validate.notEmpty(new ArrayList<>(), "%s should not be empty", "expectedValue");
 }

 @Test
 public void testNotEmptyMap() {
 HashMap<String, Integer> map = new HashMap<>();
 map.put("key1", 1);
 map.put("key2", 2);
 Validate.notEmpty(map, "%s should not be empty", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotEmptyMapWithEmpty() {
 Validate.notEmpty(new HashMap<>(), "%s should not be empty", "expectedValue");
 }

 @Test
 public void testNotBlank() {
 String str = "test";
 Validate.notBlank(str, "%s should not be blank", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotBlankWithBlank() {
 Validate.notBlank("", "%s should not be blank", "expectedValue");
 }

 @Test
 public void testNoNullElementsArray() {
 String[] array = new String[] {"test1", "test2", null};
 Validate.noNullElements(array, "%s contains null element at index: %d", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsArrayWithNull() {
 Validate.noNullElements(new String[0], "%s contains null element at index: %d", "expectedValue");
 }

 @Test
 public void testNoNullElementsCollection() {
 List<String> list = new ArrayList<>();
 list.add("test1");
 list.add("test2");
 list.add(null);
 Validate.noNullElements(list, "%s contains null element at index: %d", "expectedValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNoNullElementsCollectionWithNull() {
 Validate.noNullElements(new ArrayList<>(), "%s contains null element at index: %d", "expectedValue");
 }

 @Test
 public void testValidIndexArray() {
 int[] array = new int[] {1, 2, 3};
 Validate.validIndex(array, 1, "%s is an invalid index for array of size %d", array.length);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexArrayOutOfBounds() {
 int[] array = new int[] {1, 2, 3};
 Validate.validIndex(array, 4);
 }

 @Test
 public void testValidIndexCollection() {
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 Validate.validIndex(list, 1, "%s is an invalid index for collection of size %d", list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCollectionOutOfBounds() {
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 Validate.validIndex(list, 3);
 }

 @Test
 public void testValidIndexCharSequence() {
 String str = "test";
 Validate.validIndex(str, 1, "%s is an invalid index for character sequence of length %d", str.length());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValidIndexCharSequenceOutOfBounds() {
 String str = "test";
 Validate.validIndex(str, str.length() + 1);
 }

 @Test
 public void testValidState() {
 boolean state = true;
 Validate.validState(state);
 }

 @Test(expected = IllegalStateException.class)
 public void testValidStateFalse() {
 boolean state = false;
 Validate.validState(state);
 }

 @Test
 public void testMatchesPattern() {
 String str = "test";
 String pattern = "[a-z]";
 Validate.matchesPattern(str, pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchesPatternWithInvalidInput() {
 String str = "test";
 String pattern = "[a-zA-Z]";
 Validate.matchesPattern(str, pattern);
 }

 @Test
 public void testInclusiveBetween() {
 int value = 2;
 Validate.inclusiveBetween(1, 3, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenWithOutOfRangeValue() {
 int value = 4;
 Validate.inclusiveBetween(1, 3, value);
 }

 @Test
 public void testInclusiveBetweenLong() {
 long value = 4L;
 Validate.inclusiveBetween(1L, 3L, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenLongOutOfRange() {
 long value = 5L;
 Validate.inclusiveBetween(1L, 3L, value);
 }

 @Test
 public void testInclusiveBetweenDouble() {
 double value = 4.5;
 Validate.inclusiveBetween(1.5, 3.5, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusiveBetweenDoubleOutOfRange() {
 double value = 5.5;
 Validate.inclusiveBetween(1.5, 3.5, value);
 }

 @Test
 public void testExclusiveBetween() {
 int value = 2;
 Validate.exclusiveBetween(1, 3, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenWithInclusiveValue() {
 int value = 1;
 Validate.exclusiveBetween(1, 3, value);
 }

 @Test
 public void testExclusiveBetweenLong() {
 long value = 4L;
 Validate.exclusiveBetween(1L, 3L, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenLongInclusiveValue() {
 long value = 3L;
 Validate.exclusiveBetween(1L, 3L, value);
 }

 @Test
 public void testExclusiveBetweenDouble() {
 double value = 4.5;
 Validate.exclusiveBetween(1.5, 3.5, value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExclusiveBetweenDoubleInclusiveValue() {
 double value = 3.5;
 Validate.exclusiveBetween(1.5, 3.5, value);
 }

 @Test
 public void testIsInstanceOf() {
 Object obj = new Object();
 Validate.isInstanceOf(Object.class, obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInstanceOfWithInvalidType() {
 Object obj = new Object();
 Validate.isInstanceOf(String.class, obj);
 }

 @Test
 public void testIsAssignableFrom() {
 Object parent = new Object();
 Object child = new Object();
 Validate.isAssignableFrom(parent.getClass(), child.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsAssignableFromWithInvalidParent() {
 Object parent = new Object();
 Object child = new String();
 Validate.isAssignableFrom(parent.getClass(), child.getClass());
 }
}