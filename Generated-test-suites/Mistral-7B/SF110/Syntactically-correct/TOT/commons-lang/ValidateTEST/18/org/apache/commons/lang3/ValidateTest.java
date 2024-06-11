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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
        Validate.isTrue(true, "Expected value to be true, but was false", 42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "Expected value to be true, but was false", 42);
    }

    @Test
    public void testIsTrueWithStringMessageAndDoubleValue() {
        Validate.isTrue(true, "Expected value to be true, but was false", 3.14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndDoubleValue() {
        Validate.isTrue(false, "Expected value to be true, but was false", 3.14);
    }

    @Test
    public void testIsTrueWithStringMessageAndObjectsArray() {
        Validate.isTrue(true, "Expected value to be true, but was false", new Object[]{"apple", "orange", "banana"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndObjectsArray() {
        Validate.isTrue(false, "Expected value to be true, but was false", new Object[]{"apple", "orange", "banana"});
    }

    @Test
    public void testIsTrueWithoutMessageOrValues() {
        Validate.isTrue(true);
    }

    @Test
    public void testIsTrueWithoutMessageOrValuesButWithExpressionFalse() {
        Validate.isTrue(false);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testNotNullWithNotNullObject() {
        String testString = "hello world";
        String result = Validate.notNull(testString);
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        String testString = null;
        Validate.notNull(testString);
        // this test should throw a NullPointerException
    }

    @Test
    public void testNotEmptyArrayWithNonEmptyArray() {
        String[] testArray = new String[]{"apple", "banana", "cherry"};
        String[] result = Validate.notEmpty(testArray);
        assertArrayEquals(testArray, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        String[] testArray = new String[0];
        Validate.notEmpty(testArray);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testNotEmptyCollectionWithNonEmptyCollection() {
        List<String> testList = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        List<String> result = Validate.notEmpty(testList);
        assertArrayEquals(testList.toArray(), result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        List<String> testList = new ArrayList<>();
        Validate.notEmpty(testList);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testNotEmptyMapWithNonEmptyMap() {
        Map<String, String> testMap = new HashMap<String, String>() {{
            put("apple", "fruit");
            put("banana", "fruit");
            put("cherry", "fruit");
        }};
        Map<String, String> result = Validate.notEmpty(testMap);
        assertArrayEquals(testMap.keySet().toArray(), result.keySet().toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        Map<String, String> testMap = new HashMap<>();
        Validate.notEmpty(testMap);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testNoNullElementsArrayWithNonNullElementsArray() {
        String[] testArray = new String[]{"apple", "banana", "cherry"};
        String[] result = Validate.noNullElements(testArray);
        assertArrayEquals(testArray, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithNullElementsArray() {
        String[] testArray = new String[]{"apple", null, "cherry"};
        Validate.noNullElements(testArray);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testNoNullElementsCollectionWithNonNullElementsCollection() {
        List<String> testList = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        List<String> result =
                Validate.noNullElements(testList);
        assertArrayEquals(testList.toArray(), result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionWithNullElementsCollection() {
        List<String> testList = new ArrayList<>(Arrays.asList("apple", null, "cherry"));
        Validate.noNullElements(testList);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testValidIndexArrayWithValidIndex() {
        int[] testArray = {1, 2, 3, 4, 5};
        int index = 3;
        int[] result = Validate.validIndex(testArray, index);
        assertArrayEquals(testArray, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexArrayWithInvalidIndex() {
        int[] testArray = {1, 2, 3, 4, 5};
        int index = 6;
        Validate.validIndex(testArray, index);
        // this test should throw an IndexOutOfBoundsException
    }

    @Test
    public void testValidIndexCollectionWithValidIndex() {
        List<String> testList = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        int index = 1;
        List<String> result = Validate.validIndex(testList, index);
        assertArrayEquals(testList.toArray(), result.toArray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCollectionWithInvalidIndex() {
        List<String> testList = new ArrayList<>(Arrays.asList("apple", "banana", "cherry"));
        int index = 5;
        Validate.validIndex(testList, index);
        // this test should throw an IndexOutOfBoundsException
    }

    @Test
    public void testValidIndexCharSequenceWithValidIndex() {
        String testString = "hello world";
        int index = 5;
        String result = Validate.validIndex(testString, index);
        assertTrue(testString.charAt(index) == 'd');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexCharSequenceWithInvalidIndex() {
        String testString = "hello world";
        int index = 15;
        Validate.validIndex(testString, index);
        // this test should throw an IndexOutOfBoundsException
    }

    @Test
    public void testValidStateWithTrueState() {
        boolean testState = true;
        Validate.validState(testState);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        boolean testState = false;
        Validate.validState(testState);
        // this test should throw an IllegalStateException
    }

    @Test
    public void testMatchesPatternWithMatchingStringAndPattern() {
        String testString = "hello1234world";
        String testPattern = "hello.*world";
        Validate.matchesPattern(testString, testPattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithNonMatchingStringAndPattern() {
        String testString = "hello1234world";
        String testPattern = "hello.*worl";
        Validate.matchesPattern(testString, testPattern);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testMatchesPatternWithMatchingStringAndPatternWithMessage() {
        String testString = "hello1234world";
        String testPattern = "hello.*world";
        String message = "Expected string to match pattern, but it did not";
        Validate.matchesPattern(testString, testPattern, message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithNonMatchingStringAndPatternWithMessage() {
        String testString = "hello1234world";
        String testPattern = "hello.*worl";
        String message = "Expected string to match pattern, but it did not";
        Validate.matchesPattern(testString, testPattern, message);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testInclusiveBetweenWithValidComparableValue() {
        Integer testValue = 2;
        Integer lowerBound = 1;
        Integer upperBound = 3;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidComparableValue() {
        Integer testValue = 1;
        Integer lowerBound = 1;
        Integer upperBound = 3;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testInclusiveBetweenWithValidLongValue() {
        long testValue = 2;
        long lowerBound = 1;
        long upperBound = 3;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidLongValue() {
        long testValue = 1;
        long lowerBound = 1;
        long upperBound = 3;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testInclusiveBetweenWithValidDoubleValue() {
        double testValue = 2.5;
        double lowerBound = 1.5;
        double upperBound = 3.5;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidDoubleValue() {
        double testValue = 1.5;
        double lowerBound = 1.5;
        double upperBound = 3.5;
        Validate.inclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testExclusiveBetweenWithValidComparableValue() {
        Integer testValue = 2;
        Integer lowerBound = 1;
        Integer upperBound = 3;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidComparableValue() {
        Integer testValue = 1;
        Integer lowerBound = 1;
        Integer upperBound = 3;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testExclusiveBetweenWithValidLongValue() {
        long testValue = 2;
        long lowerBound = 1;
        long upperBound = 3;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidLongValue() {
        long testValue = 1;
        long lowerBound = 1;
        long upperBound = 3;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testExclusiveBetweenWithValidDoubleValue() {
        double testValue = 2.5;
        double lowerBound = 1.5;
        double upperBound = 3.5;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidDoubleValue() {
        double testValue = 1.5;
        double lowerBound = 1.5;
        double upperBound = 3.5;
        Validate.exclusiveBetween(lowerBound, upperBound, testValue);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testIsInstanceOfWithValidTypeAndObject() {
        String testString = "hello world";
        Validate.isInstanceOf(String.class, testString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithInvalidTypeAndObject() {
        String testString = "hello world";
        Validate.isInstanceOf(Integer.class, testString);
        // this test should throw an IllegalArgumentException
    }

    @Test
    public void testIsAssignableFromWithValidTypes() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithInvalidTypes() {
        Validate.isAssignableFrom(Number.class, String.class);
        // this test should throw an IllegalArgumentException
    }
}