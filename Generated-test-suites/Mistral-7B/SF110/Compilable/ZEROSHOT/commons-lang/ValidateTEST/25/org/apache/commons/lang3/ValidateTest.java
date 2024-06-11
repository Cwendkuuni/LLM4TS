package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        try {
            Validate.isTrue(true, "Expected true", 42);
            Validate.isTrue(false, "Expected false", 42);
            Validate.isTrue(false, "Expected false", 3.14);
            Validate.isTrue(false, "Expected false", new Object[] { 1, 2, 3 });
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isTrue(true);
    }

    @Test
    public void testNotNull() {
        Object object = new Object();
        String message = "The validated object is null";
        Object[] values = new Object[] { "Value is null" };

        try {
            Validate.notNull(null, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.notNull(object);
        Validate.notNull(object, message, values);
    }

    @Test
    public void testNotEmptyArray() {
        Object[] array = new Object[0];
        String message = "The validated array is empty";
        Object[] values = new Object[] { "Array is empty" };

        try {
            Validate.notEmpty(array, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object[] nonEmptyArray = new Object[] { 1, 2, 3 };
        Validate.notEmpty(nonEmptyArray);
        Validate.notEmpty(nonEmptyArray, message, values);
    }

    @Test
    public void testNoNullElementsArray() {
        Object[] array = new Object[] { null, 1, 2, 3 };
        String message = "The validated array contains null element at index: %d";
        Object[] values = new Object[] { 0 };

        try {
            Validate.noNullElements(array, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object[] noNullElementsArray = new Object[] { 1, 2, 3, 4 };
        Validate.noNullElements(array);
        Validate.noNullElements(array, message, values);
    }

    @Test
    public void testValidIndexArray() {
        Integer[] array = new Integer[3];
        int index = 2;
        String message = "The validated array index is invalid: %d";
        Object[] values = new Object[] { 3 };

        try {
            Validate.validIndex(array, index, message, values);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        Validate.validIndex(array, index);
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> collection = new ArrayList<>();
        String message = "The validated collection is empty";
        Object[] values = new Object[] { "Collection is empty" };

        try {
            Validate.notEmpty(collection, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> nonEmptyCollection = Arrays.asList("One", "Two", "Three");
        Validate.notEmpty(nonEmptyCollection);
        Validate.notEmpty(nonEmptyCollection, message, values);
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> collection = Arrays.asList(null, "One", "Two", "Three");
        String message = "The validated collection contains null element at index: %d";
        Object[] values = new Object[] { 0 };

        try {
            Validate.noNullElements(collection, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        List<String> noNullElementsCollection = Arrays.asList("One", "Two", "Three");
        Validate.noNullElements(collection);
        Validate.noNullElements(collection, message, values);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> collection = Arrays.asList("One", "Two", "Three");
        int index = 2;
        String message = "The validated collection index is invalid: %d";
        Object[] values = new Object[] { 3 };

        try {
            Validate.validIndex(collection, index, message, values);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        Validate.validIndex(collection, index);
    }

    @Test
    public void testNotBlank() {
        String chars = "";
        String message = "The validated character sequence is blank";
        Object[] values = new Object[] { "Sequence is blank" };

        try {
            Validate.notBlank(chars, message, values);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String notBlankChars = "One";
        Validate.notBlank(chars);
        Validate.notBlank(chars, message, values);
    }

    @Test
    public void testMatchesPattern() {
        String input = "OneTwoThree";
        String pattern = "[OneTwoThree]";

        try {
            Validate.matchesPattern(input, pattern);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetween() {
        Integer start = 0;
        Integer end = 2;
        Integer value = 1;

        try {
            Validate.inclusiveBetween(start, end, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.inclusiveBetween(start, end, value);

        Validate.inclusiveBetween(start, end, value, "Value %s is not in the specified inclusive range of %s to %s", start, end);
    }

    @Test
    public void testExclusiveBetween() {
        Integer start = 0;
        Integer end = 2;
        Integer value = 1;

        try {
            Validate.exclusiveBetween(start, end, value);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.exclusiveBetween(start, end, value);

        Validate.exclusiveBetween(start, end, value, "Value %s is not in the specified exclusive range of %s to %s", start, end);
    }

    @Test
    public void testIsInstanceOf() {
        Class<String> type = String.class;
        Object obj = new Object();

        try {
            Validate.isInstanceOf(type, obj);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object strObj = "One";
        Validate.isInstanceOf(type, strObj);
        Validate.isInstanceOf(type, strObj, "Expected type: %s, actual: %s", type.getName(), strObj.getClass().getName());
    }

    @Test
    public void testIsAssignableFrom() {
        Class<String> superType = String.class;
        Class<Integer> type = Integer.class;

        try {
            Validate.isAssignableFrom(superType, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Validate.isAssignableFrom(superType, type);
        Validate.isAssignableFrom(superType, type, "Cannot assign a %s to a %s", superType.getName(), type.getName());
    }
}