package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but was false");
        Validate.isTrue(42.0d, "Expected true but was %f", 42.0d);
        Validate.isTrue(42L, "Expected true but was %d", 42L);
        Validate.isTrue(Arrays.asList("foo", "bar").contains("foo"), "Expected true but was false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithMessage() {
        Validate.isTrue(false, "Expected true but was false: 42");
    }

    @Test
    public void testNotNull() {
        Validate.notNull(new Object());
        Validate.notNull(null, "Expected not null but was null");
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithMessage() {
        Validate.notNull(null, "Expected not null but was null: 42");
    }

    @Test
    public void testNotEmptyArray() {
        Object[] arr = new Object[0];
        Validate.notEmpty(arr, "Expected not empty but was empty: %d", 0);
        Validate.notEmpty(arr = new Object[]{"foo", "bar"}, "Expected not empty but was empty: %d", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithMessage() {
        Validate.notEmpty(new Object[0], "Expected not empty but was empty: %d", 1);
    }

    @Test
    public void testNotEmptyCollection() {
        List<Object> col = new LinkedList<>();
        Validate.notEmpty(col, "Expected not empty but was empty: %d", 0);
        Validate.notEmpty(col = Arrays.asList("foo", "bar"), "Expected not empty but was empty: %d", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithMessage() {
        Validate.notEmpty(Collections.emptyList(), "Expected not empty but was empty: %d", 1);
    }

    @Test
    public void testNotEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        Validate.notEmpty(map, "Expected not empty but was empty: %d", 0);
        Validate.notEmpty(map = new HashMap<>(){ { put("foo", "bar"); } }, "Expected not empty but was empty: %d", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithMessage() {
        Validate.notEmpty(Collections.emptyMap(), "Expected not empty but was empty: %d", 1);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String cs = "";
        Validate.notEmpty(cs, "Expected not empty but was empty: %d", 0);
        Validate.notEmpty(cs = "foo", "Expected not empty but was empty: %d", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithMessage() {
        Validate.notEmpty("", "Expected not empty but was empty: %d", 1);
    }

    @Test
    public void testNotBlankCharSequence() {
        String cs = "";
        Validate.notBlank(cs, "Expected not blank but was blank");
        Validate.notBlank(cs = " foo", "Expected not blank but was blank");
        Validate.notBlank(cs = " foo ", "Expected not blank but was blank");
        Validate.notBlank(cs = "foo", "Expected not blank but was blank");
        Validate.notBlank(cs = "foo ", "Expected not blank but was blank");
        Validate.notBlank(cs = "foo\t", "Expected not blank but was blank");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankCharSequenceWithMessage() {
        Validate.notBlank("", "Expected not blank but was blank: 0");
    }

    @Test
    public void testNoNullElementsArray() {
        Object[] arr = new Object[0];
        Validate.noNullElements(arr, "Expected not null elements but found null at index: %d", 0);
        Object[] arr2 = {null, "foo"};
        Validate.noNullElements(arr2, "Expected not null elements but found null at index: %d", 0);
        Validate.noNullElements(arr2 = new Object[]{new Object[0], "foo"}, "Expected not null elements but found null at index: %d", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithMessage() {
        Validate.noNullElements(new Object[0], "Expected not null elements but found null at index: %d", 1);
    }

    @Test
    public void testNoNullElementsCollection() {
        List<Object> col = new LinkedList<>();
        Validate.noNullElements(col, "Expected not null elements but found null at index: %d", 0);
        List<Object> col2 = Arrays.asList(null, "foo");
        Validate.noNullElements(col2, "Expected not null elements but found null at index: %d", 0);
        Validate.noNullElements(col2 = new LinkedList<Object>(){ { add(null); add("foo"); } }, "Expected not null elements but found null at index: %d", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionWithMessage() {
        Validate.noNullElements(Collections.emptyList(), "Expected not null elements but found null at index: %d", 1);
    }

    @Test
    public void testValidIndexArray() {
        Object[] arr = new Object[0];
        Validate.validIndex(arr, 0, "Expected valid index but was invalid: %d", 1);
        Validate.validIndex(arr = new Object[2], 1, "Expected valid index but was invalid: %d", 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayOutOfBounds() {
        Object[] arr = new Object[0];
        Validate.validIndex(arr, 1, "Expected valid index but was invalid: %d", 1);
    }

    @Test
    public void testValidIndexCollection() {
        List<Object> col = new LinkedList<>();
        Validate.validIndex(col, 0, "Expected valid index but was invalid: %d", 1);
        Validate.validIndex(col = Arrays.asList("foo", "bar"), 1, "Expected valid index but was invalid: %d", 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionOutOfBounds() {
        List<Object> col = new LinkedList<>();
        Validate.validIndex(col, 1, "Expected valid index but was invalid: %d", 1);
    }

    @Test
    public void testValidIndexCharSequence() {
        String cs = "";
        Validate.validIndex(cs, 0, "Expected valid index but was invalid: %d", 1);
        Validate.validIndex(cs = "foo", 1, "Expected valid index but was invalid: %d", 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceOutOfBounds() {
        String cs = "";
        Validate.validIndex(cs, 1, "Expected valid index but was invalid: %d", 1);
    }

    @Test
    public void testValidState() {
        Validate.validState(true);
        Validate.validState(false, "Expected valid state but was invalid");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithMessage() {
        Validate.validState(false, "Expected valid state but was invalid: 42");
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("foo", "foo[a-z]*");
        Validate.matchesPattern("foo", "foo[a-z]*", "Expected string to match pattern: %s", "foo[a-z]*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternMismatch() {
        Validate.matchesPattern("bar", "foo[a-z]*");
    }

    @Test
    public void testInclusiveBetweenNumber() {
        Validate.inclusiveBetween(1, 10, 5);
        Validate.inclusiveBetween(1, 10, 5, "Expected value to be between %d and %d but was %d", 1, 10, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenNumberOutOfRange() {
        Validate.inclusiveBetween(1, 10, 11);
    }

    @Test
    public void testInclusiveBetweenNumberLong() {
        Validate.inclusiveBetween(1L, 10L, 5L);
        Validate.inclusiveBetween(1L, 10L, 5L, "Expected value to be between %d and %d but was %d", 1L, 10L, 6L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenNumberLongOutOfRange() {
        Validate.inclusiveBetween(1L, 10L, 11L);
    }

    @Test
    public void testInclusiveBetweenNumberDouble() {
        Validate.inclusiveBetween(1.0, 10.0, 5.5);
        Validate.inclusiveBetween(1.0, 10.0, 5.5, "Expected value to be between %f and %f but was %f", 1.0, 10.0, 6.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenNumberDoubleOutOfRange() {
        Validate.inclusiveBetween(1.0, 10.0, 11.0);
    }

    @Test
    public void testExclusiveBetweenNumber() {
        Validate.exclusiveBetween(1, 10, 5);
        Validate.exclusiveBetween(1, 10, 5, "Expected value to be between %d and %d (exclusive) but was %d", 1, 10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberOutOfRangeMin() {
        Validate.exclusiveBetween(1, 10, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberOutOfRangeMax() {
        Validate.exclusiveBetween(1, 10, 11);
    }

    @Test
    public void testExclusiveBetweenNumberLong() {
        Validate.exclusiveBetween(1L, 10L, 5L);
        Validate.exclusiveBetween(1L, 10L, 5L, "Expected value to be between %d and %d (exclusive) but was %d", 1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberLongOutOfRangeMin() {
        Validate.exclusiveBetween(1L, 10L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberLongOutOfRangeMax() {
        Validate.exclusiveBetween(1L, 10L, 11L);
    }

    @Test
    public void testExclusiveBetweenNumberDouble() {
        Validate.exclusiveBetween(1.0, 10.0, 5.5);
        Validate.exclusiveBetween(1.0, 10.0, 5.5, "Expected value to be between %f and %f (exclusive) but was %f", 1.0, 10.0, 5.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberDoubleOutOfRangeMin() {
        Validate.exclusiveBetween(1.0, 10.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenNumberDoubleOutOfRangeMax() {
        Validate.exclusiveBetween(1.0, 10.0, 11.0);
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "foo");
        Validate.isInstanceOf(String.class, null, "Expected instanceOf: %s but was: %s", Object.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfMismatch() {
        Validate.isInstanceOf(Integer.class, "foo");
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(String.class, Object.class);
        Validate.isAssignableFrom(String.class, Object.class, "Expected assignableFrom: %s but was: %s", Integer.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromMismatch() {
        Validate.isAssignableFrom(Integer.class, String.class);
    }
}