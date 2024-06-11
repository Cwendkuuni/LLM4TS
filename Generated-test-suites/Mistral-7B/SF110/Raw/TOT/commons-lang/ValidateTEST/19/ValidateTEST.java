package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but was false");
        Validate.isTrue(1 > 0, "Expected true but was false: %d > %d", 1, 0);
        Validate.isTrue(3.14 > 3.1, "Expected true but was false: %f > %f", 3.14, 3.1);
        Validate.isTrue(Arrays.asList(1, 2, 3).contains(2), "Expected true but was false");
        Validate.isTrue(new String("abc").length() > 2, "Expected true but was false: %s.length() > %d", "abc", 2);
    }

    @Test
    public void testIsTrueThrowsException() {
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(false, "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(0 != 1, "Expected true but was false: %b != %b", 0, 1));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(Double.NaN, "Expected true but was false: %s", Double.NaN));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(Collections.emptyList(), "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(new Object[0], "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(new StringBuffer(), "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(new TreeMap<>(), "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(new LinkedList<>(), "Expected true but was false"));
        assertThrows("IllegalArgumentException", () -> Validate.isTrue(new HashMap<>(), "Expected true but was false"));
    }

    @Test
    public void testIsTrueNoArgs() {
        Validate.isTrue();
    }

    @Test
    public void testNotNull() {
        assertNotNull(new Object());
        Validate.notNull(new Object());
        assertNull(null);
        Validate.notNull(null, "Expected non-null but was null");
    }

    @Test
    public void testNotEmptyArray() {
        assertNotNull(new int[0]);
        Validate.notEmpty(new int[0]);
        int[] arr = {1, 2, 3};
        assertArrayEquals(arr, Validate.notEmpty(arr));
        Validate.notEmpty(new int[0], "Expected non-empty array but was empty");
    }

    @Test
    public void testNotEmptyCollection() {
        List<String> list = new LinkedList<>();
        Validate.notEmpty(list);
        list.add("hello");
        Validate.notEmpty(list);
        Validate.notEmpty(Collections.emptyList(), "Expected non-empty collection but was empty");
    }

    @Test
    public void testNotEmptyMap() {
        Map<Integer, String> map = new TreeMap<>();
        map.put(1, "one");
        map.put(2, "two");
        Validate.notEmpty(map);
        Validate.notEmpty(new TreeMap<>(), "Expected non-empty map but was empty");
    }

    @Test
    public void testNoNullElementsArray() {
        String[] arr = new String[0];
        Validate.noNullElements(arr);
        arr = new String[]{"hello", null, "world"};
        assertArrayEquals(new String[]{"hello", "world"}, Validate.noNullElements(arr, "Expected non-null array but contained null element at index: %d", 1));
        Validate.noNullElements(arr, "Expected non-null array but contained null element at index: %d", 2);
    }

    @Test
    public void testNoNullElementsCollection() {
        List<String> list = new LinkedList<>();
        Validate.noNullElements(list);
        list.add("hello");
        list.add(null);
        list.add("world");
        assertArrayEquals(new String[]{"hello", "world"}, Validate.noNullElements(list.toArray(), "Expected non-null array but contained null element at index: %d", 1));
        Validate.noNullElements(list, "Expected non-null array but contained null element at index: %d", 2);
    }

    @Test
    public void testValidIndexArray() {
        int[] arr = new int[3];
        Validate.validIndex(arr, 0);
        Validate.validIndex(arr, 2);
        try {
            Validate.validIndex(arr, 10);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
        String[] strArr = new String[3];
        Validate.validIndex(strArr, 0, "Expected non-negative index: %d", 10
        );
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new LinkedList<>();
        list.add("hello");
        list.add("world");
        Validate.validIndex(list, 0);
        Validate.validIndex(list, 1);
        try {
            Validate.validIndex(list, 10);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
        Validate.validIndex(list, 1, "Expected non-negative index: %d", 10);
    }

    @Test
    public void testValidState() {
        assertTrue(true);
        Validate.validState(true);
        assertFalse(false);
        Validate.validState(false, "Expected true but was false");
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("abc", "ab[c]?d");
        Validate.matchesPattern("abc", "ab[c]?d", "Expected pattern to match but did not: %s does not match %s", "abc", "ab[c]?d");
    }

    @Test
    public void testInclusiveBetween() {
        Validate.inclusiveBetween(0, 10, 5);
        Validate.inclusiveBetween(0, 10, 15, "Expected value %d to be within inclusive range of %d to %d", 15, 0, 10);
        Validate.inclusiveBetween(0, 10, 5d);
        Validate.inclusiveBetween(0, 10, 15d, "Expected value %f to be within inclusive range of %f to %f", 15d, 0d, 10d);
        Validate.inclusiveBetween(0, 10, 5L);
        Validate.inclusiveBetween(0, 10, 15L, "Expected value %d to be within inclusive range of %d to %d", 15L, 0L, 10L);
        Validate.inclusiveBetween(0, 10, 5.0f);
        Validate.inclusiveBetween(0, 10, 15.0f, "Expected value %f to be within inclusive range of %f to %f", 15.0f, 0.0f, 10.0f);
        Validate.inclusiveBetween(0, 10, 5.0d);
        Validate.inclusiveBetween(0, 10, 15.0d, "Expected value %f to be within inclusive range of %f to %f", 15.0d, 0.0d, 10.0d);
        Validate.inclusiveBetween(0, 10, null);
        Validate.inclusiveBetween(0, 10, null, "Expected value %s to be within inclusive range of %d to %d", null, 0, 10);
    }

    @Test
    public void testExclusiveBetween() {
        Validate.exclusiveBetween(0, 10, 5);
        Validate.exclusiveBetween(0, 10, 5, "Expected value %d to be outside exclusive range of %d to %d", 5, 0, 10);
        Validate.exclusiveBetween(0, 10, 5d);
        Validate.exclusiveBetween(0, 10, 5d, "Expected value %f to be outside exclusive range of %f to %f", 5d, 0d, 10d);
        Validate.exclusiveBetween(0, 10, 5L);
        Validate.exclusiveBetween(0, 10, 5L, "Expected value %d to be outside exclusive range of %d to %d", 5L, 0L, 10L);
        Validate.exclusiveBetween(0, 10, 5.0f);
        Validate.exclusiveBetween(0, 10, 5.0f, "Expected value %f to be outside exclusive range of %f to %f", 5.0f, 0.0f, 10.0f);
        Validate.exclusiveBetween(0, 10, 5.0d);
        Validate.exclusiveBetween(0, 10, 5.0d, "Expected value %f to be outside exclusive range of %f to %f", 5.0d, 0.0d, 10.0d);
        Validate.exclusiveBetween(0, 10, null);
        Validate.exclusiveBetween(0, 10, null, "Expected value %s to be outside exclusive range of %d to %d", null, 0, 10);
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(String.class, String.class);
        Validate.isAssignableFrom(String.class, Object.class, "Expected %s to be assignable from %s", String.class, Object.class);
        Validate.isAssignableFrom(String.class, Integer.class, "Expected %s to be assignable from %s", String.class, Integer.class);
    }

    @Test
    public void testIsInstanceOf() {
        Object obj = new Object();
        Validate.isInstanceOf(String.class, "abc");
        Validate.isInstanceOf(String.class, "abc", "Expected %s to be an instance of %s", "abc", String.class);
        Validate.isInstanceOf(String.class, obj, "Expected %s to be an instance of %s", obj, String.class);
    }
}