package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "Expected true, but got false");
        Validate.isTrue(false, "Expected false, but got true");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseExpressionAndMessage() {
        Validate.isTrue(false, "Expected true, but got false");
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
        Validate.isTrue(false);
    }

    @Test
    public void testNotNullWithObject() {
        String str = "Hello World!";
        Validate.notNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = {"One", "Two", "Three"};
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        String[] array = {};
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = Arrays.asList("One", "Two", "Three");
        Validate.notEmpty(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        List<String> list = Collections.emptyList();
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("One", "One");
        map.put("Two", "Two");
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        Map<String, String> map = new HashMap<>();
        Validate.notEmpty(map);
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = {"One", null, "Three"};
        Validate.noNullElements(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElement() {
        String[] array = {"One", null, "Three"};
        Validate.noNullElements(array);
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> list = Arrays.asList("One", null, "Three");
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionWithNullElement() {
        List<String> list = Arrays.asList("One", null, "Three");
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexArray() {
        String[] array = {"One", "Two", "Three"};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        String[] array = {"One", "Two", "Three"};
        Validate.validIndex(array, 4);
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = Arrays.asList("One", "Two", "Three");
        Validate.validIndex(list, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        List<String> list = Arrays.asList("One", "Two", "Three");
        Validate.validIndex(list, 4);
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "OneTwoThree";
        Validate.validIndex(chars, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String chars = "OneTwoThree";
        Validate.validIndex(chars, 4);
    }

    @Test
    public void testValidState() {
        boolean state = true;
        Validate.validState(state);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        boolean state = false;
        Validate.validState(state);
    }

    @Test
    public void testMatchesPattern() {
        String input = "abcdefg";
        String pattern = "[a-z]+";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInput() {
        String input = "12345";
        String pattern = "[a-z]+";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetweenWithComparable() {
        Integer number = 5;
        Validate.inclusiveBetween(2, 10, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithComparableOutOfRange() {
        Integer number = 5;
        Validate.inclusiveBetween(1, 4, number);
    }

    @Test
    public void testInclusiveBetweenWithLong() {
        long number = 5L;
        Validate.inclusiveBetween(2L, 10L, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongOutOfRange() {
        long number = 5L;
        Validate.inclusiveBetween(1L, 4L, number);
    }

    @Test
    public void testInclusiveBetweenWithDouble() {
        double number = 5.0;
        Validate.inclusiveBetween(2.0, 10.0, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleOutOfRange() {
        double number = 5.0;
        Validate.inclusiveBetween(1.0, 4.0, number);
    }

    @Test
    public void testExclusiveBetweenWithComparable() {
        Integer number = 5;
        Validate.exclusiveBetween(1, 10, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithComparableOutOfRange() {
        Integer number = 5;
        Validate.exclusiveBetween(0, 9, number);
    }

    @Test
    public void testExclusiveBetweenWithLong() {
        long number = 5L;
        Validate.exclusiveBetween(1L, 10L, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongOutOfRange() {
        long number = 5L;
        Validate.exclusiveBetween(0L, 9L, number);
    }

    @Test
    public void testExclusiveBetweenWithDouble() {
        double number = 5.0;
        Validate.exclusiveBetween(1.0, 10.0, number);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleOutOfRange() {
        double number = 5.0;
        Validate.exclusiveBetween(0.0, 9.0, number);
    }

    @Test
    public void testInstanceOf() {
        String str = "Hello World!";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstanceOfWithInvalidType() {
        Integer number = 5;
        Validate.isInstanceOf(String.class, number);
    }

    @Test
    public void testAssignableFrom() {
        Integer number = 5;
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignableFromWithInvalidType() {
        Integer number = 5;
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}