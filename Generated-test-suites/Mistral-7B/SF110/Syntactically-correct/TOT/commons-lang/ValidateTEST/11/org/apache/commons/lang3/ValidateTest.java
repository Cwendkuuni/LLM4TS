package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrueWithBoolean() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBoolean() {
        Validate.isTrue(false);
    }

    @Test
    public void testIsTrueWithStringMessageAndLongValue() {
        Validate.isTrue(true, "Expected true but was false", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "Expected true but was false", 1L);
    }

    @Test
    public void testIsTrueWithStringMessageObjectArgs() {
        Validate.isTrue(true, "Expected true but was false", "a", 1L, 2.0F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageObjectArgs() {
        Validate.isTrue(false, "Expected true but was false", "a", 1L, 2.0F);
    }

    @Test
    public void testNotNull() {
        String str = new String("Hello, World!");
        Validate.notNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        int[] intArray = {1, 2, 3};
        Validate.notEmpty(intArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArray() {
        int[] intArray = {};
        Validate.notEmpty(intArray);
    }

    @Test
    public void testNotEmptyCollection() {
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.notEmpty(intList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        List<Integer> intList = Collections.emptyList();
        Validate.notEmpty(intList);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("key1", 1);
        stringIntegerMap.put("key2", 2);
        Validate.notEmpty(stringIntegerMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMap() {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        Validate.notEmpty(stringIntegerMap);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String str = "Hello, World!";
        Validate.notEmpty(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCharSequence() {
        String str = "";
        Validate.notEmpty(str);
    }

    @Test
    public void testNotBlankCharSequence() {
        String str = "Hello, World!";
        Validate.notBlank(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlankCharSequence() {
        String str = "";
        Validate.notBlank(str);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] stringArray = {"Hello", "World!"};
        Validate.noNullElements(stringArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayWithNullElement() {
        String[] stringArray = {"Hello", null, "World!"};
        Validate.noNullElements(stringArray);
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> stringList = Arrays.asList("Hello", "World!");
        Validate.noNullElements(stringList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterableWithNullElement() {
        List<String> stringList = Arrays.asList("Hello", null, "World!");
        Validate.noNullElements(stringList);
    }

    @Test
    public void testValidIndexArray() {
        int[] intArray = {1, 2, 3};
        Validate.validIndex(intArray, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexArray() {
        int[] intArray = {1, 2, 3};
        Validate.validIndex(intArray, 4);
    }

    @Test
    public void testValidIndexCollection() {
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.validIndex(intList, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCollection() {
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.validIndex(intList, 4);
    }

    @Test
    public void testValidIndexCharSequence() {
        String str = "Hello, World!";
        Validate.validIndex(str, 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidIndexCharSequence() {
        String str = "Hello, World!";
        Validate.validIndex(str, 10);
    }

    @Test
    public void testValidState() {
        boolean validState = true;
        Validate.validState(validState);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidState() {
        boolean validState = false;
        Validate.validState(validState);
    }

    @Test
    public void testMatchesPattern() {
        String input = "Hello, World!";
        String pattern = "^[Hh]ello[, ]*[Ww]orld!$";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedPattern() {
        String input = "Hello, World!";
        String pattern = "^[Hh]ello[, ]*[Ww]ord!$";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetweenComparable() {
        Integer value = 3;
        Integer start = 1;
        Integer end = 5;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenComparableOutOfRange() {
        Integer value = 1;
        Integer start = 1;
        Integer end = 5;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testInclusiveBetweenLong() {
        long value = 5L;
        long start = 1L;
        long end = 10L;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongOutOfRange() {
        long value = 1L;
        long start = 1L;
        long end = 10L;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double value = 5.5;
        double start = 1.0;
        double end = 10.0;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleOutOfRange() {
        double value = 1.0;
        double start = 1.0;
        double end = 10.0;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test
    public void testExclusiveBetweenComparable() {
        Integer value = 3;
        Integer start = 1;
        Integer end = 5;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenComparableOutOfRangeStart() {
        Integer value = 3;
        Integer start = 1;
        Integer end = 5;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenComparableOutOfRangeEnd() {
        Integer value = 3;
        Integer start = 1;
        Integer end = 5;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test
    public void testExclusiveBetweenLong() {
        long value = 5L;
        long start = 1L;
        long end = 10L;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongOutOfRangeStart() {
        long value = 5L;
        long start = 1L;
        long end = 10L;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongOutOfRangeEnd() {
        long value = 5L;
        long start = 1L;
        long end = 10L;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double value = 5.5;
        double start = 1.0;
        double end = 10.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleOutOfRangeStart() {
        double value = 5.5;
        double start = 1.0;
        double end = 10.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleOutOfRangeEnd() {
        double value = 5.5;
        double start = 1.0;
        double end = 10.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test
    public void testIsInstanceOf() {
        String string = "Hello, World!";
        Validate.isInstanceOf(String.class, string);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotInstanceOf() {
        String string = "Hello, World!";
        Validate.isInstanceOf(Integer.class, string);
    }

    @Test
    public void testIsAssignableFrom() {
        String string = "Hello, World!";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
        Validate.isAssignableFrom(StringBuffer.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNotAssignableFrom() {
        String string = "Hello, World!";
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}