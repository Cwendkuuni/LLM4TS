package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValidateTest {

    

    @Test
    public void testIsTrueWithBooleanExpressionAndMessage() {
        Validate.isTrue(true, "Expected true");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpressionAndMessageAndValue() {
        Validate.isTrue(false, "Expected true, got false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpressionAndMessageAndValues() {
        Validate.isTrue(false, "Expected true, got false", "Value is false");
    }

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    // Similary write test cases for other methods, following the same pattern.

    // isTrue methods with different types and variations

    @Test
    public void testNotNullWithObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObjectAndMessage() {
        Validate.notNull(null, "Expected non-null object");
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObjectAndMessageAndValues() {
        Validate.notNull(null, "Expected non-null object", new Object[]{"Null object passed"});
    }

    // noNullElements method for T[]

    @Test
    public void testNoNullElementsWithNotNullArray() {
        String[] arr = new String[]{"A", "B", "C"};
        Validate.noNullElements(arr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithNullArrayAndMessage() {
        String[] arr = null;
        Validate.noNullElements(arr, "Array contains null element at index: %d", new Object[]{0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithNullArrayAndMessageAndValues() {
        String[] arr = null;
        Validate.noNullElements(arr, "Array contains null element at index: %d", new Object[]{0, "Array is null"});
    }

    // noNullElements method for Iterable

    @Test
    public void testNoNullElementsWithNotNullList() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        Validate.noNullElements(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithNullListAndMessage() {
        List<String> list = null;
        Validate.noNullElements(list, "List contains null element at index: %d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithNullListAndMessageAndValues() {
        List<String> list = null;
        Validate.noNullElements(list, "List contains null element at index: %d", new Object[]{0, "List is null"});
    }

    // validIndex methods for T[]

    @Test
    public void testValidIndexWithValidArrayIndex() {
        String[] arr = new String[]{"A", "B", "C"};
        Validate.validIndex(arr, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidArrayIndexAndMessage() {
        String[] arr = new String[]{"A", "B", "C"};
        Validate.validIndex(arr, 3, "Invalid index %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidArrayIndexAndMessageAndValues() {
        String[] arr = new String[]{"A", "B", "C"};
        Validate.validIndex(arr, 3, "Invalid index %d", new Object[]{3, "Array index is invalid"});
    }

    // validIndex methods for Collection

    @Test
    public void testValidIndexWithValidCollectionIndex() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        Validate.validIndex(list, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCollectionIndexAndMessage() {
        List<String> list = new ArrayList<>();
        list.add("A");
        Validate.validIndex(list, 2, "Invalid index %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCollectionIndexAndMessageAndValues() {
        List<String> list = new ArrayList<>();
        list.add("A");
        Validate.validIndex(list, 2, "Invalid index %d", new Object[]{2, "Collection index is invalid"});
    }

    // validIndex methods for CharSequence

    @Test
    public void testValidIndexWithValidCharSequenceIndex() {
        String str = "ABC";
        Validate.validIndex(str, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCharSequenceIndexAndMessage() {
        String str = "ABC";
        Validate.validIndex(str, 3, "Invalid index %d");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCharSequenceIndexAndMessageAndValues() {
        String str = "ABC";
        Validate.validIndex(str, 3, "Invalid index %d", new Object[]{3, "CharSequence index is invalid"});
    }

    // validState method

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        Validate.validState(false);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseStateAndMessage() {
        Validate.validState(false, "State is false");
    }

    // matchesPattern method

    @Test
    public void testMatchesPatternWithValidMatch() {
        String input = "ABC";
        String pattern = "[A-Z]";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidMatch() {
        String input = "ABC";
        String pattern = "[B-Z]";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidMatchAndMessage() {
        String input = "ABC";
        String pattern = "[B-Z]";
        Validate.matchesPattern(input, pattern, "String does not match pattern: %s", new Object[]{"Invalid pattern"});
    }

    // inclusiveBetween method for T

    @Test
    public void testInclusiveBetweenWithValidValue() {
        Integer value = 1;
        Validate.inclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidValueAndMessage() {
        Integer value = 0;
        Validate.inclusiveBetween(1, 3, value, "Value is not within inclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidValueAndMessageAndValues() {
        Integer value = 0;
        Validate.inclusiveBetween(1, 3, value, "Value is not within inclusive range", new Object[]{value, "Value is out of range"});
    }

    // inclusiveBetween method for long

    @Test
    public void testInclusiveBetweenWithValidLongValue() {
        long value = 1;
        Validate.inclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidLongValueAndMessage() {
        long value = 0;
        Validate.inclusiveBetween(1, 3, value, "Value is not within inclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidLongValueAndMessageAndValues() {
        long value = 0;
        Validate.inclusiveBetween(1, 3, value, "Value is not within inclusive range", new Object[]{value, "Value is out of range"});
    }

    // inclusiveBetween method for double

    @Test
    public void testInclusiveBetweenWithValidDoubleValue() {
        double value = 1.5;
        Validate.inclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidDoubleValueAndMessage() {
        double value = 0.5;
        Validate.inclusiveBetween(1.0, 3.0, value, "Value is not within inclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidDoubleValueAndMessageAndValues() {
        double value = 0.5;
        Validate.inclusiveBetween(1.0, 3.0, value, "Value is not within inclusive range", new Object[]{value, "Value is out of range"});
    }

    // exclusiveBetween method for T

    @Test
    public void testExclusiveBetweenWithValidValue() {
        Integer value = 1;
        Validate.exclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValueAndMessage() {
        Integer value = 1;
        Validate.exclusiveBetween(1, 3, value, "Value is not within exclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValueAndMessageAndValues() {
        Integer value = 1;
        Validate.exclusiveBetween(1, 3, value, "Value is not within exclusive range", new Object[]{value, "Value is at the boundary"});
    }

    // exclusiveBetween method for long

    @Test
    public void testExclusiveBetweenWithValidLongValue() {
        long value = 1;
        Validate.exclusiveBetween(1, 3, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidLongValueAndMessage() {
        long value = 1;
        Validate.exclusiveBetween(1, 3, value, "Value is not within exclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidLongValueAndMessageAndValues() {
        long value = 1;
        Validate.exclusiveBetween(1, 3, value, "Value is not within exclusive range", new Object[]{value, "Value is at the boundary"});
    }

    // exclusiveBetween method for double

    @Test
    public void testExclusiveBetweenWithValidDoubleValue() {
        double value = 1.5;
        Validate.exclusiveBetween(1.0, 3.0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidDoubleValueAndMessage() {
        double value = 1.5;
        Validate.exclusiveBetween(1.0, 3.0, value, "Value is not within exclusive range");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidDoubleValueAndMessageAndValues() {
        double value = 1.5;
        Validate.exclusiveBetween(1.0, 3.0, value, "Value is not within exclusive range", new Object[]{value, "Value is at the boundary"});
    }

    // isInstanceOf and isAssignableFrom methods

    @Test
    public void testIsInstanceOfWithValidObjectTypeAndObject() {
        String s = "Hello";
        Validate.isInstanceOf(String.class, s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidObjectTypeAndObject() {
        String s = "Hello";
        Validate.isInstanceOf(Integer.class, s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidObjectTypeAndObjectAndMessage() {
        String s = "Hello";
        Validate.isInstanceOf(Integer.class, s, "Expected Integer, got String");
    }

    @Test
    public void testIsAssignableFromWithValidSuperTypeAndSubType() {
        class SubType extends Object {
        }

        class SuperType {
        }

        Validate.isAssignableFrom(SuperType.class, SubType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidSuperTypeAndSubType() {
        class SubType extends Object {
        }

        class SuperType {
        }

        Validate.isAssignableFrom(Integer.class, SubType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidSuperTypeAndSubTypeAndMessage() {
        class SubType extends Object {
        }

        class SuperType {
        }

        Validate.isAssignableFrom(Integer.class, SubType.class, "Expected SuperType, got SubType");
    }

    
}