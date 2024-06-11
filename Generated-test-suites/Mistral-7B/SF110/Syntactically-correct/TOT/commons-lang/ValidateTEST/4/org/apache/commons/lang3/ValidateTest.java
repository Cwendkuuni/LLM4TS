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
    public void testIsTrueBooleanBoolean() {
        Validate.isTrue(true, "Expected true but was false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanFalse() {
        Validate.isTrue(false, "Expected true but was false");
    }

    @Test
    public void testIsTrueBooleanBooleanDouble() {
        Validate.isTrue(true, "Expected true but was false", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanBooleanFalse() {
        Validate.isTrue(false, "Expected true but was false", 1.0);
    }

    @Test
    public void testIsTrueBooleanBooleanObjects() {
        Validate.isTrue(true, "Expected true but was false", "Test", 1, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanBooleanObjectsFalse() {
        Validate.isTrue(false, "Expected true but was false", "Test", 1, 2, 3);
    }

    @Test
    public void testIsTrueBoolean() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueBooleanFalse() {
        Validate.isTrue(false);
    }

    // ... Add similar tests for other isTrue methods

    @Test
    public <T> void testNotNullObject() {
        T obj = new Object();
        Validate.notNull(obj);
        assertNotNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public <T> void testNotNullObjectNull() {
        T obj = null;
        Validate.notNull(obj);
        assertNull(obj);
    }

    @Test
    public <T> void testNotEmptyArray() {
        String[] array = {"Test1", "Test2"};
        Validate.notEmpty(array);
        assertArrayEquals(new String[]{"Test1", "Test2"}, array);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testNotEmptyArrayEmpty() {
        String[] array = {};
        Validate.notEmpty(array);
    }

    @Test
    public <T extends Collection<?>> void testNotEmptyCollectionArrayList() {
        List<String> collection = new ArrayList<>();
        collection.add("Test1");
        collection.add("Test2");
        Validate.notEmpty(collection);
        assertFalse(collection.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Collection<?>> void testNotEmptyCollectionEmpty() {
        List<String> collection = new ArrayList<>();
        Validate.notEmpty(collection);
        assertTrue(collection.isEmpty());
    }

    // ... Add similar tests for other notEmpty methods

    @Test
    public <T extends Map<?, ?>> void testNotEmptyMapHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Test1", 1);
        map.put("Test2", 2);
        Validate.notEmpty(map);
        assertFalse(map.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Map<?, ?>> void testNotEmptyMapEmpty() {
        Map<String, Integer> map = new HashMap<>();
        Validate.notEmpty(map);
        assertTrue(map.isEmpty());
    }

    // ... Add similar tests for other notEmpty methods

    @Test
    public <T extends CharSequence> void testNotEmptyCharSequenceString() {
        String chars = "Test";
        Validate.notEmpty(chars);
        assertTrue(chars.length() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends CharSequence> void testNotEmptyCharSequenceEmpty() {
        String chars = "";
        Validate.notEmpty(chars);
        assertTrue(chars.length() == 0);
    }

    // ... Add similar tests for other notEmpty and notBlank methods

    @Test
    public <T> void testNoNullElementsArray() {
        String[] array = {"Test1", "Test2"};
        Validate.noNullElements(array);
        assertArrayEquals(new String[]{"Test1", "Test2"}, array);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testNoNullElementsArrayNull() {
        String[] array = null;
        Validate.noNullElements(array);
    }

    @Test
    public <T extends Iterable<?>> void testNoNullElementsIterableArrayList() {
        List<String> iterable = new ArrayList<>();
        iterable.add("Test1");
        iterable.add("Test2");
        Validate.noNullElements(iterable);
        assertFalse(iterable.contains(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Iterable<?>> void testNoNullElementsIterableNull() {
        List<String> iterable = null;
        Validate.noNullElements(iterable);
    }

    // ... Add similar tests for other noNullElements methods

    @Test
    public <T> void testValidIndexArray() {
        String[] array = {"Test1", "Test2"};
        Validate.validIndex(array, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public <T> void testValidIndexArrayNegative() {
        String[] array = {"Test1", "Test2"};
        Validate.validIndex(array, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public <T> void testValidIndexArrayOutOfBounds() {
        String[] array = {"Test1", "Test2"};
        Validate.validIndex(array, 2);
    }

    // ... Add similar tests for other validIndex methods

    @Test
    public void testValidStateBoolean() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateBooleanFalse() {
        Validate.validState(false);
    }

    // ... Add similar tests for other validState methods

    @Test
    public void testMatchesPatternStringRegex() {
        String input = "TestTest";
        String pattern = "[Tt]est.*";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternStringRegexInvalid() {
        String input = "TestTest";
        String pattern = "[Tt]est.*Test";
        Validate.matchesPattern(input, pattern);
    }

    // ... Add similar tests for other matchesPattern methods

    @Test
    public <T extends Comparable<? super T>> void testInclusiveBetween() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 3;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testInclusiveBetweenInvalidStart() {
        Integer start = 5;
        Integer end = 3;
        Integer value = 3;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testInclusiveBetweenInvalidEnd() {
        Integer start = 1;
        Integer end = 3;
        Integer value = 3;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testInclusiveBetweenInvalidValueStart() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 2;
        Validate.inclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testInclusiveBetweenInvalidValueEnd() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 6;
        Validate.inclusiveBetween(start, end, value);
    }

    // ... Add similar tests for other inclusiveBetween methods

    @Test
    public <T extends Comparable<? super T>> void testExclusiveBetween() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 3;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testExclusiveBetweenInvalidStart() {
        Integer start = 5;
        Integer end = 3;
        Integer value = 3;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testExclusiveBetweenInvalidEnd() {
        Integer start = 1;
        Integer end = 3;
        Integer value = 3;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testExclusiveBetweenInvalidValueStart() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 2;
        Validate.exclusiveBetween(start, end, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T extends Comparable<? super T>> void testExclusiveBetweenInvalidValueEnd() {
        Integer start = 1;
        Integer end = 5;
        Integer value = 6;
        Validate.exclusiveBetween(start, end, value);
    }

    // ... Add similar tests for other exclusiveBetween methods

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, new String("Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfInvalid() {
        Validate.isInstanceOf(String.class, new Integer(1));
    }

    // ... Add similar tests for other isAssignableFrom and isInstanceOf methods
}