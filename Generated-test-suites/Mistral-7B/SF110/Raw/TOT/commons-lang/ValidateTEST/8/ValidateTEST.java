package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "Expected true but was: %s", "true");
        Validate.isTrue(false, "Expected true but was: %s", "false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBooleanExpressionAndValue() {
        Validate.isTrue(false, "Expected true but was: %s", 1L);
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithNullObject() {
        Validate.notNull(null).notNull(new Object());
        Validate.notNull(null).isTrue(true);
    }

    @Test
    public void testNotEmptyArray() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        Validate.notEmpty(strings, "The validated array is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArray() {
        String[] strings = new String[0];
        Validate.notEmpty(strings, "The validated array is empty");
    }

    @Test
    public void testNotEmptyCollection() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Validate.notEmpty(numbers, "The validated collection is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        List<Integer> numbers = Collections.emptyList();
        Validate.notEmpty(numbers, "The validated collection is empty");
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        Validate.notEmpty(map, "The validated map is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        Validate.notEmpty(map, "The validated map is empty");
    }

    @Test
    public void testNotBlankString() {
        String text = "The quick brown fox jumps over the lazy dog.";
        Validate.notBlank(text, "The validated character sequence is blank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankString() {
        String text = "";
        Validate.notBlank(text, "The validated character sequence is blank");
    }

    @Test
    public void testNoNullElementsInArray() {
        Integer[] numbers = {1, 2, 3};
        Validate.noNullElements(numbers, "The validated array contains null element at index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayWithNullElement() {
        Integer[] numbers = {1, null, 3};
        Validate.noNullElements(numbers, "The validated array contains null element at index: %d");
    }

    @Test
    public void testNoNullElementsInCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        Validate.noNullElements(strings, "The validated collection contains null element at index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionWithNullElement() {
        List<String> strings = Arrays.asList("apple", null, "cherry");
        Validate.noNullElements(strings, "The validated collection contains null element at index: %d");
    }

    @Test
    public void testValidIndexInArray() {
        int[] numbers = {1, 2, 3};
        Validate.validIndex(numbers, 1, "The validated array index is invalid: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexInArray() {
        int[] numbers = {1, 2, 3};
        Validate.validIndex(numbers, 4, "The validated array index is invalid: %d");
    }

    @Test
    public void testValidIndexInCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(strings, 1, "The validated collection index is invalid: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexInCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(strings, 4, "The validated collection index is invalid: %d");
    }

    @Test
    public void testValidState() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidState() {
        Validate.validState(false);
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("theQu!ckBrownFox", "\\w+\\s+\\w+\\s+\\w+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchesPattern() {
        Validate.matchesPattern("theQu!ckBrownFox", "\\w+\\s+\\d+\\s+\\w+");
    }

    @Test
    public void testInclusiveBetweenLong() {
        Validate.inclusiveBetween(1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongOutOfRangeMin() {
        Validate.inclusiveBetween(1L, 10L, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongOutOfRangeMax() {
        Validate.inclusiveBetween(1L, 10L, 11L);
    }

    @Test
    public void testInclusiveBetweenDouble() {
        Validate.inclusiveBetween(1.0D, 10.0D, 5.5D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleOutOfRangeMin() {
        Validate.inclusiveBetween(1.0D, 10.0D, 0.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleOutOfRangeMax() {
        Validate.inclusiveBetween(1.0D, 10.0D, 11.0D);
    }

    @Test
    public void testExclusiveBetweenLong() {
        Validate.exclusiveBetween(1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongMin() {
        Validate.exclusiveBetween(1L, 10L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongMax() {
        Validate.exclusiveBetween(1L, 10L, 11L);
    }

    @Test
    public void testExclusiveBetweenDouble() {
        Validate.exclusiveBetween(1.0D, 10.0D, 5.5D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleMin() {
        Validate.exclusiveBetween(1.0D, 10.0D, 1.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleMax() {
        Validate.exclusiveBetween(1.0D, 10.0D, 11.0D);
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "hello world");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotInstanceOf() {
        Validate.isInstanceOf(Integer.class, "hello world");
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotAssignableFrom() {
        Validate.isAssignableFrom(Integer.class, String.class);
    }
}