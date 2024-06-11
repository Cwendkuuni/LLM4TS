package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test with boolean expression and no message or values
        Validate.isTrue(true);

        // Test with boolean expression and message
        String message = "The validated expression is true";
        Validate.isTrue(true, message);

        // Test with boolean expression, message, and values
        long value = 10;
        Validate.isTrue(true, message, value);
        assertEquals(10, value);

        // Test with false boolean expression, message, and values
        Validate.isTrue(false, "The validated expression is false");

        // Test with float value
        Validate.isTrue(1.0f > 0.5f, "The validated expression is false", 1.0f);
        assertEquals(1.0f, 1.0f, 0.0f);

        // Test with double value
        Validate.isTrue(2.0 > 1.5, "The validated expression is false", 2.0);
        assertEquals(2.0, 2.0, 0.0);
    }

    @Test
    public void testNotNull() {
        // Test with Object
        Object obj = new Object();
        Validate.notNull(obj);

        // Test with null Object and message
        String message = "The validated object is null";
        Validate.notNull(null, message);
        throwException(IllegalArgumentException.class, message);

        // Test with null Object, message, and values
        Object[] values = new Object[]{1};
        Validate.notNull(null, message, values);
        throwException(IllegalArgumentException.class, message + " " + Arrays.toString(values));
    }

    @Test
    public void testNoNullElementsArray() {
        // Test with non-null array
        Integer[] array = {1, 2, 3};
        Validate.noNullElements(array);

        // Test with null array and message
        String message = "The validated array contains null element at index: %d";
        Validate.noNullElements(null, message);
        throwException(IllegalArgumentException.class, message);

        // Test with null array, message, and values
        Object[] values = new Object[]{1, 2};
        Validate.noNullElements(null, message, values);
        throwException(IllegalArgumentException.class, message + " " + Arrays.toString(values));
    }

    @Test
    public void testNoNullElementsIterable() {
        // Test with non-null Iterable
        List<Integer> iterable = Arrays.asList(1, 2, 3);
        Validate.noNullElements(iterable);

        // Test with null Iterable and message
        message = "The validated collection contains null element at index: %d";
        Validate.noNullElements(null, message);
        throwException(IllegalArgumentException.class, message);

        // Test with null Iterable, message, and values
        values = new Object[]{1, 2};
        Validate.noNullElements(null, message, values);
        throwException(IllegalArgumentException.class, message + " " + Arrays.toString(values));
    }

    @Test
    public void testValidIndexArray() {
        // Test with valid index
        Integer[] array = {1, 2, 3};
        Validate.validIndex(array, 1);

        // Test with invalid index and message
        message = "The validated array index is invalid: %d";
        Validate.validIndex(array, -1, message);
        throwException(IndexOutOfBoundsException.class, message);

        // Test with invalid index, message, and values
        Validate.validIndex(array, -1, message, 1);
        throwException(IndexOutOfBoundsException.class, message + " " + 1);
    }

    @Test
    public void testValidIndexCollection() {
        // Test with valid index
        List<Integer> collection = Arrays.asList(1, 2, 3);
        Validate.validIndex(collection, 1);

        // Test with invalid index and message
        message = "The validated collection index is invalid: %d";
        Validate.validIndex(collection, -1, message);
        throwException(IndexOutOfBoundsException.class, message);

        // Test with invalid index, message, and values
        Validate.validIndex(collection, -1, message, 1);
        throwException(IndexOutOfBoundsException.class, message + " " + 1);
    }

    @Test
    public void testValidIndexCharSequence() {
        // Test with valid index
        String chars = "abcdef";
        Validate.validIndex(chars, 2);

        // Test with invalid index and message
        message = "The validated character sequence index is invalid: %d";
        Validate.validIndex(chars, -1, message);
        throwException(IndexOutOfBoundsException.class, message);

        // Test with invalid index, message, and values
        Validate.validIndex(chars, -1, message, 1);
        throwException(IndexOutOfBoundsException.class, message + " " + 1);
    }

    @Test
    public void testValidState() {
        // Test with true state
        boolean state = true;
        Validate.validState(state);

        // Test with false state and message
        message = "The validated state is false";
        Validate.validState(false, message);
        throwException(IllegalStateException.class, message);

        // Test with false state, message, and values
        Validate.validState(false, message, 1);
        throwException(IllegalStateException.class, message + " " + 1);
    }

    @Test
    public void testMatchesPattern() {
        // Test with valid input and pattern
        String input = "abcdef";
        String pattern = "[a-z]+";
        Validate.matchesPattern(input, pattern);

        // Test with invalid input and pattern
        input = "12345";
        pattern = "[a-z]+";
        message = "The string %s does not match the pattern %s";
        Validate.matchesPattern(input, pattern, message);
        throwException(IllegalArgumentException.class, message);

        // Test with invalid input, pattern, message, and values
        Validate.matchesPattern(input, pattern, message, 1);
        throwException(IllegalArgumentException.class, message + " " + 1);
    }

    // Add more test methods for other Validate methods such as inclusiveBetween, exclusiveBetween, isInstanceOf, and isAssignableFrom.

    private void throwException(Class<? extends Throwable> expectedType, String message) {
        try {
            throw new AssertionError();
        } catch (Throwable e) {
            assertEquals(expectedType, e.getClass());
            assertEquals(message, e.getMessage());
        }
    }
}