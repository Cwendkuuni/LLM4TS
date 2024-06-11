package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        Validate.isTrue(true, "The expression is false: 10", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "The expression is false: 10", 10);
    }

    @Test
    public void testIsTrueWithObjectArray() {
        Validate.isTrue(new boolean[] {true, true, true}, "The expression is false: %s", new Object[] {"a", "b", "c"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseObjectArray() {
        Validate.isTrue(new boolean[] {false, true, true}, "The expression is false: %s", new Object[] {"a", "b", "c"});
    }

    @Test
    public void testNotNull() {
        String str = new String("Hello, World!");
        Validate.notNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyArray() {
        int[] numbers = new int[] {1, 2, 3};
        Validate.notEmpty(numbers, "The array is empty", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyArrayWithEmptyArray() {
        int[] numbers = new int[0];
        Validate.notEmpty(numbers, "The array is empty", 1);
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> words = Arrays.asList("Apple", "Banana", "Orange");
        Validate.notEmpty(words, "The collection is empty", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithEmptyCollection() {
        List<String> words = Collections.emptyList();
        Validate.notEmpty(words, "The collection is empty", 1);
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);
        Validate.notEmpty(map, "The map is empty", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithEmptyMap() {
        HashMap<String, Integer> map = new HashMap<>();
        Validate.notEmpty(map, "The map is empty", 1);
    }

    @Test
    public void testNotEmptyCharSequence() {
        String text = "Hello, World!";
        Validate.notEmpty(text, "The character sequence is empty", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCharSequenceWithEmptyCharSequence() {
        String text = "";
        Validate.notEmpty(text, "The character sequence is empty", 1);
    }

    @Test
    public void testNotBlankCharSequence() {
        String text = "Hello, World!";
        Validate.notBlank(text, "The character sequence is blank", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankCharSequenceWithBlankCharSequence() {
        String text = "";
        Validate.notBlank(text, "The character sequence is blank", 1);
    }

    @Test
    public void testNoNullElementsInArray() {
        String[] strings = new String[] {"Apple", "Banana", "Orange"};
        Validate.noNullElements(strings, "The array contains a null element", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsInArrayWithNullElement() {
        String[] strings = new String[] {"Apple", null, "Orange"};
        Validate.noNullElements(strings, "The array contains a null element", 1);
    }

    @Test
    public void testNoNullElementsInCollection() {
        List<String> strings = Arrays.asList("Apple", "Banana", "Orange");
        Validate.noNullElements(strings, "The collection contains a null element", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsInCollectionWithNullElement() {
        List<String> strings = Arrays.asList("Apple", null, "Orange");
        Validate.noNullElements(strings, "The collection contains a null element", 1);
    }

    @Test
    public void testValidIndexInArray() {
        int[] numbers = new int[] {1, 2, 3};
        Validate.validIndex(numbers, 1, "The index is invalid: %d", 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexInArrayWithInvalidIndex() {
        int[] numbers = new int[] {1, 2, 3};
        Validate.validIndex(numbers, 4, "The index is invalid: %d", 1);
    }

    @Test
    public void testValidIndexInCollection() {
        List<String> strings = Arrays.asList("Apple", "Banana", "Orange");
        Validate.validIndex(strings, 1, "The index is invalid: %d", 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexInCollectionWithInvalidIndex() {
        List<String> strings = Arrays.asList("Apple", "Banana", "Orange");
        Validate.validIndex(strings, 4, "The index is invalid: %d", 1);
    }

    @Test
    public void testValidIndexInCharSequence() {
        String text = "Hello, World!";
        Validate.validIndex(text, 0, "The index is invalid: %d", 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexInCharSequenceWithInvalidIndex() {
        String text = "Hello, World!";
        Validate.validIndex(text, 10, "The index is invalid: %d", 1);
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
    public void testMatchesPatternWithStringInputAndPattern() {
        Validate.matchesPattern("Hello, World!", "Hello.*World!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithMismatchingStringInputAndPattern() {
        Validate.matchesPattern("Hello, World!", "Hello.*Worlds!");
    }

    @Test
    public void testInclusiveBetweenWithLongValues() {
        Validate.inclusiveBetween(1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongValuesOutOfRange() {
        Validate.inclusiveBetween(1L, 10L, 15L);
    }

    @Test
    public void testInclusiveBetweenWithIntegerValues() {
        Validate.inclusiveBetween(1, 10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithIntegerValuesOutOfRange() {
        Validate.inclusiveBetween(1, 10, 15);
    }

    @Test
    public void testExclusiveBetweenWithLongValues() {
        Validate.exclusiveBetween(1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongValuesAtBoundary() {
        Validate.exclusiveBetween(1L, 10L, 10L);
    }

    @Test
    public void testExclusiveBetweenWithIntegerValues() {
        Validate.exclusiveBetween(1, 10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithIntegerValuesAtBoundary() {
        Validate.exclusiveBetween(1, 10, 10);
    }

    @Test
    public void testIsInstanceOf() {
        String str = new String("Hello, World!");
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithMismatchedTypes() {
        String str = new String("Hello, World!");
        Validate.isInstanceOf(Integer.class, str);
    }

    @Test
    public void testIsAssignableFrom() {
        String str = new String("Hello, World!");
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithMismatchedTypes() {
        String str = new String("Hello, World!");
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}