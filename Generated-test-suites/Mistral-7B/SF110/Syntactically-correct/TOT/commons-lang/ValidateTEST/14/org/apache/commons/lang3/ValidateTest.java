package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpression() {
        Validate.isTrue(false);
    }

    @Test
    public void testIsTrueWithStringMessageAndLongValue() {
        Validate.isTrue(true, "Expected true, but got false", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "Expected true, but got false", 1L);
    }

    @Test
    public void testIsTrueWithDoubleValue() {
        Validate.isTrue(Math.E > 2.71828);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseDoubleValue() {
        Validate.isTrue(Math.E < 2.71828);
    }

    @Test
    public void testIsTrueWithObjects() {
        Validate.isTrue(new Object().equals(new Object()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseObjects() {
        Validate.isTrue(new Object() != new Object());
    }

    @Test
    public void testNotNullWithNotNullObject() {
        Validate.notNull(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyWithEmptyArray() {
        Validate.notEmpty(new String[0], "Array is empty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyWithNullArray() {
        Validate.notEmpty((String[]) null, "Array is empty");
    }

    @Test
    public void testNotEmptyWithArrayContainingNull() {
        String[] array = new String[]{"foo", null, "bar"};
        try {
            Validate.noNullElements(array, "Array contains null elements");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyWithArrayContainingNoNull() {
        String[] array = new String[]{"foo", "bar", "baz"};
        Validate.noNullElements(array);
    }

    @Test
    public void testNotEmptyWithEmptyCollection() {
        List<String> list = new ArrayList<>();
        Validate.notEmpty(list, "Collection is empty");
    }

    @Test(expected = NullPointerException.class)
    public void testNotEmptyWithNullCollection() {
        Validate.notEmpty((List<String>) null, "Collection is empty");
    }

    @Test
    public void testNotEmptyWithCollectionContainingNull() {
        List<String> list = new ArrayList<>(Arrays.asList("foo", null, "bar"));
        try {
            Validate.noNullElements(list, "Collection contains null elements");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyWithCollectionContainingNoNull() {
        List<String> list = new ArrayList<>(Arrays.asList("foo", "bar", "baz"));
        Validate.noNullElements(list);
    }

    @Test
    public void testValidIndexWithValidArrayIndex() {
        String[] array = new String[]{"foo", "bar", "baz"};
        Validate.validIndex(array, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidArrayIndex() {
        String[] array = new String[]{"foo", "bar", "baz"};
        Validate.validIndex(array, 3);
    }

    @Test
    public void testValidIndexWithValidCollectionIndex() {
        List<String> list = new ArrayList<>(Arrays.asList("foo", "bar", "baz"));
        Validate.validIndex(list, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCollectionIndex() {
        List<String> list = new ArrayList<>(Arrays.asList("foo", "bar", "baz"));
        Validate.validIndex(list, 3);
    }

    @Test
    public void testValidIndexWithValidCharSequenceIndex() {
        String chars = "foo bar baz";
        Validate.validIndex(chars, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCharSequenceIndex() {
        String chars = "foo bar baz";
        Validate.validIndex(chars, 3);
    }

    @Test
    public void testValidStateWithValidBooleanExpression() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithInvalidBooleanExpression() {
        Validate.validState(false);
    }

    @Test
    public void testMatchesPatternWithValidStringAndPattern() {
        String input = "12345";
        Validate.matchesPattern(input, "[0-9]{5}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidStringAndPattern() {
        String input = "123a45";
        Validate.matchesPattern(input, "[0-9]{5}");
    }

    @Test
    public void testInclusiveBetweenWithLongValues() {
        Validate.inclusiveBetween(1L, 5L, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongValuesOutsideRange() {
        Validate.inclusiveBetween(1L, 3L, 5L);
    }

    @Test
    public void testInclusiveBetweenWithIntValues() {
        Validate.inclusiveBetween(1, 5, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithIntValuesOutsideRange() {
        Validate.inclusiveBetween(1, 3, 5);
    }

    @Test
    public void testExclusiveBetweenWithLongValues() {
        Validate.exclusiveBetween(1L, 5L, 3L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongValuesAtEdge() {
        Validate.exclusiveBetween(1L, 5L, 1L);
    }

    @Test
    public void testExclusiveBetweenWithIntValues() {
        Validate.exclusiveBetween(1, 5, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithIntValuesAtEdge() {
        Validate.exclusiveBetween(1, 5, 1);
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidType() {
        Validate.isInstanceOf(Integer.TYPE.getClass(), "foo");
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(Object.class.getClass(), String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidSuperType() {
        Validate.isAssignableFrom(String.class, Object.class.getClass());
    }
}