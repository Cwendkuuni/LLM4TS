package org.apache.commons.lang3;

import static org.junit.Assert.fail;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueWithMessageAndValue() {
        Validate.isTrue(true, "Expected true but was false", 1L);
    }

    @Test
    public void testIsTrueWithMessageAndDoubleValue() {
        Validate.isTrue(true, "Expected true but was false", 2.5);
    }

    @Test
    public void testIsTrueWithMessageAndObjects() {
        Validate.isTrue(true, "Expected true but was false", "foo", "bar");
    }

    @Test
    public void testIsTrueWithoutMessage() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseValue() {
        Validate.isTrue(false);
    }

    // Repeat the above test cases for isTrue methods with different types of parameters

    // Test notNull method

    @Test
    public void testNotNullWithNonnullObject() {
        Validate.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObjectAndMessage() {
        Validate.notNull(null, "The validated object is null", "foo", "bar");
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    // Test notEmpty method for Array

    @Test
    public void testNotEmptyArrayWithNonEmptyArray() {
        String[] array = {"foo", "bar", "baz"};
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        String[] array = {};
        Validate.notEmpty(array, "The validated array is empty", "foo", "bar");
    }

    // Test notEmpty method for Collection

    @Test
    public void testNotEmptyCollectionWithNonEmptyCollection() {
        List<String> collection = Arrays.asList("foo", "bar", "baz");
        Validate.notEmpty(collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        List<String> collection = Collections.emptyList();
        Validate.notEmpty(collection, "The validated collection is empty", "foo", "bar");
    }

    // Test notEmpty method for Map

    @Test
    public void testNotEmptyMapWithNonEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "qux");
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        Map<String, String> map = new HashMap<>();
        Validate.notEmpty(map, "The validated map is empty", "foo", "bar");
    }

    // Test notEmpty method for CharSequence

    @Test
    public void testNotEmptyCharSequenceWithNonEmptyCharSequence() {
        String chars = "foo bar baz";
        Validate.notEmpty(chars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithEmptyCharSequence() {
        String chars = "";
        Validate.notEmpty(chars, "The validated character sequence is empty", "foo", "bar");
    }

    // Test noNullElements method for Array

    @Test
    public void testNoNullElementsArrayWithNonNullElements() {
        String[] array = {"foo", "bar", "baz"};
        Validate.noNullElements(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElements() {
        String[] array = {"foo", null, "baz"};
        Validate.noNullElements(array, "The validated array contains null element at index: %d", "foo", "bar");
    }

    // Test noNullElements method for Iterable

    @Test
    public void testNoNullElementsIterableWithNonNullElements() {
        List<String> iterable = Arrays.asList("foo", "bar", "baz");
        Validate.noNullElements(iterable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsIterableWithNullElements() {
        List<String> iterable = Arrays.asList("foo", null, "baz");
        Validate.noNullElements(iterable, "The validated collection contains null element at index: %d", "foo", "bar");
    }

    // Test validIndex method for Array

    @Test
    public void testValidIndexArrayWithValidIndex() {
        String[] array = {"foo", "bar", "baz"};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        String[] array = {"foo", "bar", "baz"};
        Validate.validIndex(array, 4);
    }

    // Test validIndex method for CharSequence

    @Test
    public void testValidIndexCharSequenceWithValidIndex() {
        String chars = "foo bar baz";
        Validate.validIndex(chars, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String chars = "foo bar baz";
        Validate.validIndex(chars, 4);
    }

    // Test validIndex method for Collection

    @Test
    public void testValidIndexCollectionWithValidIndex() {
        List<String> collection = Arrays.asList("foo", "bar", "baz");
        Validate.validIndex(collection, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        List<String> collection = Arrays.asList("foo", "bar", "baz");
        Validate.validIndex(collection, 4);
    }

    // Test validState method

    @Test
    public void testValidStateWithTrueState() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        Validate.validState(false);
    }

    // Test matchesPattern method

    @Test
    public void testMatchesPatternWithValidInputAndPattern() {
        String input = "foo";
        String pattern = "[fF][oO]";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInputAndPattern() {
        String input = "bar";
        String pattern = "[fF][oO]";
        Validate.matchesPattern(input, pattern);
    }

    // Test inclusiveBetween method for Comparable type

    @Test
    public void testInclusiveBetweenWithComparableTypeInRange() {
        Integer value = 10;
        Integer start = 5;
        Integer end = 15;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithComparableTypeOutOfRange() {
        Integer value = 15;
        Integer start = 5;
        Integer end = 10;
        Validate.inclusiveBetween(start, end, value);
    }

    // Test inclusiveBetween method for long type

    @Test
    public void testInclusiveBetweenWithLongTypeInRange() {
        long value = 10L;
        long start = 5L;
        long end = 15L;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongTypeOutOfRange() {
        long value = 15L;
        long start = 5L;
        long end = 10L;
        Validate.inclusiveBetween(start, end, value);
    }

    // Test inclusiveBetween method for double type

    @Test
    public void testInclusiveBetweenWithDoubleTypeInRange() {
        double value = 10.5;
        double start = 5.0;
        double end = 15.0;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleTypeOutOfRange() {
        double value = 15.5;
        double start = 5.0;
        double end = 10.0;
        Validate.inclusiveBetween(start, end, value);
    }

    // Test exclusiveBetween method for Comparable type

    @Test
    public void testExclusiveBetweenWithComparableTypeInRange() {
        Integer value = 10;
        Integer start = 5;
        Integer end = 15;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithComparableTypeOutOfRange() {
        Integer value = 15;
        Integer start = 5;
        Integer end = 10;
        Validate.exclusiveBetween(start, end, value);
    }

    // Test exclusiveBetween method for long type

    @Test
    public void testExclusiveBetweenWithLongTypeInRange() {
        long value = 10L;
        long start = 5L;
        long end = 15L;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongTypeOutOfRange() {
        long value = 15L;
        long start = 5L;
        long end = 10L;
        Validate.exclusiveBetween(start, end, value);
    }

    // Test exclusiveBetween method for double type

    @Test
    public void testExclusiveBetweenWithDoubleTypeInRange() {
        double value = 10.5;
        double start = 5.0;
        double end = 15.0;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleTypeOutOfRange() {
        double value = 15.5;
        double start = 5.0;
        double end = 10.0;
        Validate.exclusiveBetween(start, end, value);
    }

    // Test isInstanceOf method

    @Test
    public void testIsInstanceOfWithInstanceOfType() {
        String str = "foo";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithNonInstanceOfType() {
        String str = "foo";
        Validate.isInstanceOf(Integer.class, str);
    }

    // Test isAssignableFrom method

    @Test
    public void testIsAssignableFromWithAssignableFromType() {
        String str = "foo";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
        StringBuffer buffer = new StringBuffer(str);
        Validate.isAssignableFrom(String.class, buffer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithNonAssignableFromType() {
        String str = "foo";
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}