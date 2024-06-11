package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but was false");
        Validate.isTrue(1 > 0, "Expected true but was false");
        Validate.isTrue(1.0 > 0.0, "Expected true but was false");
        Validate.isTrue(Arrays.asList(1, 2, 3).contains(2), "Expected true but was false");
        Validate.isTrue(new HashMap<>().containsKey("key"), "Expected true but was false");
        Validate.isTrue(new ArrayList<>(Arrays.asList(1, 2, 3)).remove(1) == 2, "Expected true but was false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithMessage() {
        Validate.isTrue(false, "Expected true but was false with message: %s", "Message");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithValues() {
        Validate.isTrue(false, "Expected true but was false with values: %s, %d", new Object[]{"Message", 1});
    }

    // Add test cases for other isTrue methods with different argument types and values

    // Test notNull method

    @Test
    public void testNotNull() {
        Object obj = new Object();
        Validate.notNull(obj);
        assertSame(obj, Validate.notNull(obj));
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithMessage() {
        Validate.notNull(null, "Expected non-null but was null with message: %s", "Message");
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullWithValues() {
        Validate.notNull(null, "Expected non-null but was null with values: %s, %d", new Object[]{"Message", 1});
    }

    // Test noNullElements method

    @Test
    public void testNoNullElementsWithArray() {
        Integer[] array = {1, 2, 3};
        Validate.noNullElements(array);
        assertArrayEquals(new Integer[]{1, 2, 3}, array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithArrayNullElement() {
        Integer[] array = {1, null, 3};
        Validate.noNullElements(array, "Expected non-null array but contained null element at index: %d", new Object[]{1});
    }

    @Test
    public void testNoNullElementsWithIterable() {
        List<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3));
        Validate.noNullElements(list);
        assertEquals(Arrays.asList(1, 2, 3), list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsWithIterableNullElement() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, null, 3));
        Validate.noNullElements(list, "Expected non-null collection but contained null element at index: %d", new Object[]{1});
    }

    // Test validIndex method

    @Test
    public void testValidIndexWithArray() {
        Integer[] array = {1, 2, 3};
        Validate.validIndex(array, 1);
        assertEquals(2, array[1]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithArrayInvalidIndex() {
        Integer[] array = {1, 2, 3};
        Validate.validIndex(array, 4);
    }

    @Test
    public void testValidIndexWithCollection() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.validIndex(list, 1);
        assertEquals(2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithCollectionInvalidIndex() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.validIndex(list, 4);
    }

    // Test validState method

    @Test
    public void testValidStateTrue() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateFalse() {
        Validate.validState(false);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithMessage() {
        Validate.validState(false, "Expected state to be true but was false with message: %s", "Message");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithValues() {
        Validate.validState(false, "Expected state to be true but was false with values: %s, %d", new Object[]{"Message", 1});
    }

    // Test matchesPattern method

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithInvalidInputAndPattern() {
        Validate.matchesPattern("abc", "[1-2]");
    }

    @Test
    public void testMatchesPatternWithValidInputAndPattern() {
        Validate.matchesPattern("123", "[1-2]");
    }

    @Test
    public void testMatchesPatternWithValidInputAndPatternWithMessage() {
        Validate.matchesPattern("123", "[1-2]", "Expected string to match pattern: %s", "Pattern");
    }

    // Test inclusiveBetween method

    @Test
    public void testInclusiveBetweenWithValidValues() {
        Validate.inclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidValues() {
        Validate.inclusiveBetween(1, 3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithInvalidValuesAndMessage() {
        Validate.inclusiveBetween(1, 3, 0, "Expected value to be in inclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    @Test
    public void testInclusiveBetweenWithLongValues() {
        Validate.inclusiveBetween(1L, 3L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongValuesInvalid() {
        Validate.inclusiveBetween(1L, 3L, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongValuesMessage() {
        Validate.inclusiveBetween(1L, 3L, 0L, "Expected value to be in inclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    @Test
    public void testInclusiveBetweenWithDoubleValues() {
        Validate.inclusiveBetween(1.0, 3.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleValuesInvalid() {
        Validate.inclusiveBetween(1.0, 3.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoubleValuesMessage() {
        Validate.inclusiveBetween(1.0, 3.0, 0.0, "Expected value to be in inclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    // Test exclusiveBetween method

    @Test
    public void testExclusiveBetweenWithValidValues() {
        Validate.exclusiveBetween(1, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValues() {
        Validate.exclusiveBetween(1, 3, 2); // Should not throw an exception with inclusiveBetween behaving as exclusiveBetween
        Validate.exclusiveBetween(1, 3, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithInvalidValuesAndMessage() {
        Validate.exclusiveBetween(1, 3, 1, "Expected value to be in exclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    @Test
    public void testExclusiveBetweenWithLongValues() {
        Validate.exclusiveBetween(1L, 3L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongValuesInvalid() {
        Validate.exclusiveBetween(1L, 3L, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongValuesMessage() {
        Validate.exclusiveBetween(1L, 3L, 1L, "Expected value to be in exclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    @Test
    public void testExclusiveBetweenWithDoubleValues() {
        Validate.exclusiveBetween(1.0, 3.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleValuesInvalid() {
        Validate.exclusiveBetween(1.0, 3.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoubleValuesMessage() {
        Validate.exclusiveBetween(1.0, 3.0, 1.0, "Expected value to be in exclusive range: %s, %s, %s", "Start", "Value", "End");
    }

    // Test isInstanceOf method

    @Test
    public void testIsInstanceOfWithCorrectTypes() {
        Validate.isInstanceOf(String.class, "Hello World");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithIncorrectTypes() {
        Validate.isInstanceOf(String.class, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithIncorrectTypesAndMessage() {
        Validate.isInstanceOf(String.class, new Object(), "Expected type: %s, actual: %s", "String", "Object");
    }

    // Test isAssignableFrom method

    @Test
    public void testIsAssignableFromWithCorrectTypes() {
        Validate.isAssignableFrom(Number.class, Integer.class);
    }

    @Test
    public void testIsAssignableFromWithIncorrectTypes() {
        Validate.isAssignableFrom(Number.class, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithIncorrectTypesAndMessage() {
        Validate.isAssignableFrom(Number.class, String.class, "Expected super type: %s, actual type: %s", "Number", "String");
    }

    
}