package org.apache.commons.lang3.test;

import static org.apache.commons.lang3.Validate.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrowsExactly;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueWithLongArgument() {
        isTrue(true, "Expected true but received: %d", 1L);
        assertThrows(IllegalArgumentException.class, () -> isTrue(false, "Expected true but received: %d", 1L));
    }

    @Test
    public void testIsTrueWithDoubleArgument() {
        isTrue(true, "Expected true but received: %f", 1.0);
        assertThrows(IllegalArgumentException.class, () -> isTrue(false, "Expected true but received: %f", 1.0));
    }

    @Test
    public void testIsTrueWithStringMessageAndVariables() {
        isTrue(true, "The validated expression is %s", "true");
        isTrue(true, "The validated expression is %s", "success");

        assertThrows(IllegalArgumentException.class, () -> isTrue(false, "The validated expression is %s", "success"));
    }

    @Test
    public void testIsTrueWithNoArguments() {
        isTrue(true);
        assertThrows(IllegalArgumentException.class, () -> isTrue(false));
    }

    @Test
    public void testNotNull() {
        assertNotNull(new Object());
        assertThrows(NullPointerException.class, () -> notNull(null));
    }

    @Test
    public void testNotEmptyArray() {
        Object[] array = new Object[10];
        notEmpty(array);

        assertThrows(IllegalArgumentException.class, () -> notEmpty(new Object[0]));
    }

    @Test
    public void testNotEmptyCollection() {
        List<Object> list = new ArrayList<>();
        notEmpty(list);

        assertThrows(IllegalArgumentException.class, () -> notEmpty(new ArrayList<>()));
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        notEmpty(map);

        assertThrows(IllegalArgumentException.class, () -> notEmpty(new HashMap<>()));
    }

    @Test
    public void testNotEmptyCharSequence() {
        String chars = "abc";
        notEmpty(chars);

        assertThrows(IllegalArgumentException.class, () -> notEmpty(new String("")));
    }

    @Test
    public void testNotBlankCharSequence() {
        String chars = "abc";
        notBlank(chars);

        assertThrows(IllegalArgumentException.class, () -> notBlank(new String("")));
    }

    @Test
    public void testNoNullElementsArray() {
        Object[] array = new Object[10];
        array[0] = new Object();
        notNull(array);
        noNullElements(array);

        Object[] arrayWithNull = new Object[10];
        arrayWithNull[0] = null;
        assertThrows(IllegalArgumentException.class, () -> noNullElements(arrayWithNull));
    }

    @Test
    public void testNoNullElementsIterable() {
        List<Object> iterable = Arrays.asList(new Object(), new Object(), new Object());
        notNull(iterable);
        noNullElements(iterable);

        List<Object> iterableWithNull = Arrays.asList(new Object(), null, new Object());
        assertThrows(IllegalArgumentException.class, () -> noNullElements(iterableWithNull));
    }

    @Test
    public void testValidIndexArray() {
        Object[] array = new Object[10];
        validIndex(array, 5);

        assertThrows(IndexOutOfBoundsException.class, () -> validIndex(array, 11));
    }

    @Test
    public void testValidIndexCollection() {
        List<Object> collection = Arrays.asList(new Object(), new Object(), new Object());
        validIndex(collection, 5);

        assertThrows(IndexOutOfBoundsException.class, () -> validIndex(collection, 11));
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "abcdefghij";
        validIndex(chars, 5);

        assertThrows(IndexOutOfBoundsException.class, () -> validIndex(chars, 11));
    }

    @Test
    public void testValidState() {
        boolean state = true;
        validState(state);

        boolean stateFalse = false;
        assertThrows(IllegalStateException.class, () -> validState(stateFalse));
    }

    @Test
    public void testMatchesPattern() {
        String input = "abc";
        matchesPattern(input, "[a-z]");

        assertThrows(IllegalArgumentException.class, () -> matchesPattern(input, "[z]"));
    }

    @Test
    public void testMatchesPatternWithMessage() {
        String input = "abc";
        String message = "The input string %s does not match the pattern [a-z]";
        matchesPattern(input, "[a-z]", message);

        assertThrows(IllegalArgumentException.class, () -> matchesPattern(input, "[z]", message));
    }

    @Test
    public void testInclusiveBetweenWithComparable() {
        Integer value = 5;
        inclusiveBetween(3, 7, value);

        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(3, 5, value));
        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(7, 5, value));
    }

    @Test
    public void testInclusiveBetweenWithLong() {
        long value = 5L;
        inclusiveBetween(3L, 7L, value);

        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(3L, 4L, value));
        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(7L, 4L, value));
    }

    @Test
    public void testInclusiveBetweenWithDouble() {
        double value = 5.0;
        inclusiveBetween(3.0, 7.0, value);

        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(3.0, 4.0, value));
        assertThrows(IllegalArgumentException.class, () -> inclusiveBetween(7.0, 4.0, value));
    }

    @Test
    public void testExclusiveBetweenWithComparable() {
        Integer value = 5;
        exclusiveBetween(3, 7, value);

        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(3, 5, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(7, 5, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(5, 5, value));
    }

    @Test
    public void testExclusiveBetweenWithLong() {
        long value = 5L;
        exclusiveBetween(3L, 7L, value);

        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(3L, 4L, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(7L, 4L, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(5L, 5L, value));
    }

    @Test
    public void testExclusiveBetweenWithDouble() {
        double value = 5.0;
        exclusiveBetween(3.0, 7.0, value);

        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(3.0, 4.0, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(7.0, 4.0, value));
        assertThrows(IllegalArgumentException.class, () -> exclusiveBetween(5.0, 5.0, value));
    }

    @Test
    public void testIsInstanceOf() {
        String str = "Hello World";
        isInstanceOf(String.class, str);

        assertThrows(IllegalArgumentException.class, () -> isInstanceOf(Integer.class, str));
    }

    @Test
    public void testIsInstanceOfWithMessage() {
        String str = "Hello World";
        String message = "Expected String, actual: %s";
        isInstanceOf(String.class, str);

        assertThrows(IllegalArgumentException.class, () -> isInstanceOf(Integer.class, str, message));
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "Hello World";
        isAssignableFrom(String.class.getClass(), String.class);

        assertThrows(IllegalArgumentException.class, () -> isAssignableFrom(Integer.class.getClass(), String.class));
    }

    @Test
    public void testIsAssignableFromWithMessage() {
        String str = "Hello World";
        String message = "Expected %s, actual: %s";
        isAssignableFrom(String.class.getClass(), String.class);

        assertThrows(IllegalArgumentException.class, () -> isAssignableFrom(Integer.class.getClass(), String.class, message));
    }

    @Test
    public void testExplicitExceptionThrowsWhenValidStateIsFalse() {
        boolean state = false;
        assertThrows(IllegalStateException.class, () -> validState(state));
    }
}