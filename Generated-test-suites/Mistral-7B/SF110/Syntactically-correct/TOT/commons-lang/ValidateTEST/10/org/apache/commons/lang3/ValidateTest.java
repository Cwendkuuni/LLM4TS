package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanAndIntMessage() {
        Validate.isTrue(true, "Expected true, actual: %d", 42);
        Validate.isTrue(false, "Expected true, actual: %d", 42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBooleanAndIntMessageThrowsException() {
        Validate.isTrue(false, "Expected true, actual: %d", 42);
    }

    @Test
    public void testIsTrueWithBooleanAndDoubleMessage() {
        Validate.isTrue(true, "Expected true, actual: %f", 3.14);
        Validate.isTrue(false, "Expected true, actual: %f", 3.14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBooleanAndDoubleMessageThrowsException() {
        Validate.isTrue(false, "Expected true, actual: %f", 3.14);
    }

    @Test
    public void testIsTrueWithBooleanOnly() {
        Validate.isTrue(true);
        Validate.isTrue(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithBooleanOnlyThrowsException() {
        Validate.isTrue(false);
    }

    @Test
    public void testNotNullWithObject() {
        String obj = new String("hello");
        Validate.notNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        String obj = null;
        Validate.notNull(obj, "Expected non-null object, actual: null");
    }

    @Test
    public void testNotEmptyArray() {
        int[] arr = {1, 2, 3, 4};
        Validate.notEmpty(arr, "Expected non-empty array, actual size: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayThrowsException() {
        int[] arr = {};
        Validate.notEmpty(arr, "Expected non-empty array, actual size: %d");
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new LinkedList<>(Arrays.asList("apple", "banana", "cherry"));
        Validate.notEmpty(list, "Expected non-empty collection, actual size: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionThrowsException() {
        List<String> list = Collections.emptyList();
        Validate.notEmpty(list, "Expected non-empty collection, actual size: %d");
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        Validate.notEmpty(map, "Expected non-empty map, actual size: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapThrowsException() {
        HashMap<String, Integer> map = new HashMap<>();
        Validate.notEmpty(map, "Expected non-empty map, actual size: %d");
    }

    @Test
    public void testNoNullElementsArray() {
        Integer[] arr = {1, 2, 3, null};
        Validate.noNullElements(arr, "Expected non-null elements array, actual index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayThrowsException() {
        Integer[] arr = {1, 2, 3, null};
        Validate.noNullElements(arr);
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> list = Arrays.asList("apple", "banana", null);
        Validate.noNullElements(list, "Expected non-null elements iterable, actual index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsIterableThrowsException() {
        List<String> list = Arrays.asList("apple", "banana", null);
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexArray() {
        Integer[] arr = {1, 2, 3, 4};
        Validate.validIndex(arr, 2, "Expected valid index for array, actual index: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayThrowsException() {
        Integer[] arr = {1, 2, 3, 4};
        Validate.validIndex(arr, 5);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(list, 1, "Expected valid index for collection, actual index: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionThrowsException() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Validate.validIndex(list, 3);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "applebanana";
        Validate.validIndex(chars, 6, "Expected valid index for char sequence, actual index: %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceThrowsException() {
        String chars = "applebanana";
        Validate.validIndex(chars, 10);
    }

    @Test
    public void testValidState() {
        boolean state = true;
        Validate.validState(state);
        state = false;
        Validate.validState(state, "Expected valid state, actual: %b");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateThrowsException() {
        boolean state = false;
        Validate.validState(state);
    }

    @Test
    public void testMatchesPattern() {
        String str = "apple123";
        Validate.matchesPattern(str, "^[a-z]+[0-9]+$");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternThrowsException() {
        String str = "apple123";
        Validate.matchesPattern(str, "^[a-z]+[0-9]+$", "Expected string to match pattern, string: %s, pattern: %s", str, "^[a-z]+[0-9]+$");
    }

    @Test
    public void testInclusiveBetweenComparable() {
        Integer num = 5;
        Validate.inclusiveBetween(2, 7, num);
        num = 1;
        Validate.inclusiveBetween(2, 7, num, "Expected number to be inclusive between 2 and 7, actual: %d", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenComparableThrowsException() {
        Integer num = 1;
        Validate.inclusiveBetween(2, 7, num, "Expected number to be inclusive between 2 and 7, actual: %d", num);
    }

    @Test
    public void testInclusiveBetweenLong() {
        long num = 5L;
        Validate.inclusiveBetween(2L, 7L, num);
        num = 1L;
        Validate.inclusiveBetween(2L, 7L, num, "Expected number to be inclusive between 2 and 7, actual: %d", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenLongThrowsException() {
        long num = 1L;
        Validate.inclusiveBetween(2L, 7L, num, "Expected number to be inclusive between 2 and 7, actual: %d", num);
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double num = 5.5;
        Validate.inclusiveBetween(2.0, 7.0, num);
        num = 1.5;
        Validate.inclusiveBetween(2.0, 7.0, num, "Expected number to be inclusive between 2 and 7, actual: %f", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenDoubleThrowsException() {
        double num = 1.5;
        Validate.inclusiveBetween(2.0, 7.0, num, "Expected number to be inclusive between 2 and 7, actual: %f", num);
    }

    @Test
    public void testExclusiveBetweenComparable() {
        Integer num = 5;
        Validate.exclusiveBetween(1, 7, num);
        num = 6;
        Validate.exclusiveBetween(1, 7, num, "Expected number to be exclusive between 1 and 7, actual: %d", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenComparableThrowsException() {
        Integer num = 6;
        Validate.exclusiveBetween(1, 7, num, "Expected number to be exclusive between 1 and 7, actual: %d", num);
    }

    @Test
    public void testExclusiveBetweenLong() {
        long num = 5L;
        Validate.exclusiveBetween(1L, 7L, num);
        num = 6L;
        Validate.exclusiveBetween(1L, 7L, num, "Expected number to be exclusive between 1 and 7, actual: %d", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenLongThrowsException() {
        long num = 6L;
        Validate.exclusiveBetween(1L, 7L, num, "Expected number to be exclusive between 1 and 7, actual: %d", num);
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double num = 5.5;
        Validate.exclusiveBetween(1.0, 7.0, num);
        num = 6.5;
        Validate.exclusiveBetween(1.0, 7.0, num, "Expected number to be exclusive between 1 and 7, actual: %f", num);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenDoubleThrowsException() {
        double num = 6.5;
        Validate.exclusiveBetween(1.0, 7.0, num, "Expected number to be exclusive between 1 and 7, actual: %f", num);
    }

    @Test
    public void testIsInstanceOf() {
        String str = "hello";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfThrowsException() {
        String str = "hello";
        Validate.isInstanceOf(Integer.class, str, "Expected string to be instance of %s, actual: %s", String.class, str);
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "hello";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromThrowsException() {
        String str = "hello";
        Validate.isAssignableFrom(Integer.class, String.class, "Expected %s to be assignable from %s, actual: %s", Integer.class, String.class, String.class);
    }
}