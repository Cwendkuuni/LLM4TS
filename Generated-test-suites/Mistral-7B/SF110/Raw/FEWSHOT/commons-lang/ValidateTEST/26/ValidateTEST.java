package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThrows;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(true, "Expected true");
        Validate.isTrue(false, "Expected false", 42);
        Validate.isTrue(false, "Expected false", 42.0);
        Validate.isTrue(false, "Expected false", new Object[] { 1, 2, 3 });
    }

    @Test
    public void testNotNull() {
        Validate.notNull(new Object());
        Validate.notNull(null, "Expected non-null object");
    }

    @Test
    public void testNotEmptyArray() {
        Validate.notEmpty(new String[0], "Expected non-empty array");
        Validate.notEmpty(new String[] { "foo" }, "Expected non-empty array");
        Validate.notEmpty(new String[] { "foo", null }, "Expected non-empty array");
        Validate.notEmpty(new String[] { null }, "Expected non-empty array", new Object[] { 0 });
    }

    @Test
    public void testNotEmptyCollection() {
        Validate.notEmpty(Collections.emptyList(), "Expected non-empty collection");
        Validate.notEmpty(Collections.singletonList("foo"), "Expected non-empty collection");
        Validate.notEmpty(Collections.singletonList(null), "Expected non-empty collection", new Object[] { 0 });
        Validate.notEmpty(new ArrayList<>(), "Expected non-empty collection");
        Validate.notEmpty(new ArrayList<>(), "Expected non-empty collection", new Object[] { 0 });
    }

    @Test
    public void testNotEmptyMap() {
        Validate.notEmpty(Collections.emptyMap(), "Expected non-empty map");
        Validate.notEmpty(Collections.singletonMap("foo", "bar"), "Expected non-empty map");
        Validate.notEmpty(Collections.singletonMap(null, "bar"), "Expected non-empty map", new Object[] { 0 });
        Validate.notEmpty(new HashMap<>(), "Expected non-empty map");
        Validate.notEmpty(new HashMap<>(), "Expected non-empty map", new Object[] { 0 });
    }

    @Test
    public void testNotEmptyCharSequence() {
        Validate.notEmpty("", "Expected non-empty character sequence");
        Validate.notEmpty("foo", "Expected non-empty character sequence");
        Validate.notEmpty(null, "Expected non-empty character sequence", new Object[] { 0 });
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = new String[] { "foo", null };
        Validate.noNullElements(array, "Expected non-null array elements at index: %d", new Object[] { 1 });
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add(null);
        Validate.noNullElements(list, "Expected non-null collection elements at index: %d", new Object[] { 1 });
    }

    @Test
    public void testValidIndexArray() {
        String[] array = new String[5];
        Validate.validIndex(array, 2, "Expected valid array index: %d");
        Validate.validIndex(array, 5, "Expected valid array index: %d");
        Validate.validIndex(array, -1, "Expected valid array index: %d", new Object[] { -1 });
        try {
            Validate.validIndex(array, 10, "Expected valid array index: %d", new Object[] { 10 });
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        Validate.validIndex(list, 0, "Expected valid collection index: %d");
        Validate.validIndex(list, 1, "Expected valid collection index: %d");
        Validate.validIndex(list, -1, "Expected valid collection index: %d", new Object[] { -1 });
        try {
            Validate.validIndex(list, 10, "Expected valid collection index: %d", new Object[] { 10 });
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexCharSequence() {
        String chars = "foo";
        Validate.validIndex(chars, 0, "Expected valid character sequence index: %d");
        Validate.validIndex(chars, 1, "Expected valid character sequence index: %d");
        Validate.validIndex(chars, -1, "Expected valid character sequence index: %d", new Object[] { -1 });
        try {
            Validate.validIndex(chars, 10, "Expected valid character sequence index: %d", new Object[] { 10 });
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidState() {
        Validate.validState(true);
        Validate.validState(false, "Expected valid state");
    }

    @Test
    public void testMatchesPattern() {
        String input = "foo[bar]";
        String pattern = "[a-z]+[0-9]";
        Validate.matchesPattern(input, pattern);
        Validate.matchesPattern(input, pattern, "Expected matching pattern: %s", pattern);
    }

    @Test
    public void testInclusiveBetween() {
        int value = 5;
        Validate.inclusiveBetween(0, 10, value);
        Validate.inclusiveBetween(0, 10, value, "Expected inclusive range: %d to %d", 0, 10);
        int start = 5;
        int end = 10;
        Validate.inclusiveBetween(start, end, value);
        Validate.inclusiveBetween(start, end, value, "Expected inclusive range: %d to %d", start, end);
        Validate.inclusiveBetween(start + 1, end, value);
        try {
            Validate.inclusiveBetween(start, end - 1, value);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.inclusiveBetween(start, end, null, "Expected non-null value");

        long valueLong = 5L;
        Validate.inclusiveBetween(0L, 10L, valueLong);
        Validate.inclusiveBetween(0L, 10L, valueLong, "Expected inclusive range: %d to %d", 0L, 10L);
        long startLong = 5L;
        long endLong = 10L;
        Validate.inclusiveBetween(startLong, endLong, valueLong);
        Validate.inclusiveBetween(startLong, endLong, valueLong, "Expected inclusive range: %d to %d", startLong, endLong);
        Validate.inclusiveBetween(startLong + 1, endLong, valueLong);
        try {
            Validate.inclusiveBetween(startLong, endLong - 1, valueLong);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.inclusiveBetween(startLong, endLong, null, "Expected non-null value");

        double valueDouble = 5.0;
        Validate.inclusiveBetween(0.0, 10.0, valueDouble);
        Validate.inclusiveBetween(0.0, 10.0, valueDouble, "Expected inclusive range: %f to %f", 0.0, 10.0);
        double startDouble = 5.0;
        double endDouble = 10.0;
        Validate.inclusiveBetween(startDouble, endDouble, valueDouble);
        Validate.inclusiveBetween(startDouble, endDouble, valueDouble, "Expected inclusive range: %f to %f", startDouble, endDouble);
        Validate.inclusiveBetween(startDouble + 1.0, endDouble, valueDouble);
        try {
            Validate.inclusiveBetween(startDouble, endDouble - 1.0, valueDouble);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.inclusiveBetween(startDouble, endDouble, null, "Expected non-null value");
    }

    @Test
    public void testExclusiveBetween() {
        int value = 5;
        Validate.exclusiveBetween(1, 10, value);
        Validate.exclusiveBetween(1, 10, value, "Expected exclusive range: %d to %d", 1, 10);
        int start = 5;
        int end = 10;
        Validate.exclusiveBetween(start, end, value);
        Validate.exclusiveBetween(start, end, value, "Expected exclusive range: %d to %d", start, end);
        Validate.exclusiveBetween(start - 1, end, value);
        try {
            Validate.exclusiveBetween(start, end - 1, value);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.exclusiveBetween(start, end, null, "Expected non-null value");

        long valueLong = 5L;
        Validate.exclusiveBetween(1L, 10L, valueLong);
        Validate.exclusiveBetween(1L, 10L, valueLong, "Expected exclusive range: %d to %d", 1L, 10L);
        long startLong = 5L;
        long endLong = 10L;
        Validate.exclusiveBetween(startLong, endLong, valueLong);
        Validate.exclusiveBetween(startLong, endLong, valueLong, "Expected exclusive range: %d to %d", startLong, endLong);
        Validate.exclusiveBetween(startLong - 1, endLong, valueLong);
        try {
            Validate.exclusiveBetween(startLong, endLong - 1, valueLong);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.exclusiveBetween(startLong, endLong, null, "Expected non-null value");

        double valueDouble = 5.0;
        Validate.exclusiveBetween(1.0, 10.0, valueDouble);
        Validate.exclusiveBetween(1.0, 10.0, valueDouble, "Expected exclusive range: %f to %f", 1.0, 10.0);
        double startDouble = 5.0;
        double endDouble = 10.0;
        Validate.exclusiveBetween(startDouble, endDouble, valueDouble);
        Validate.exclusiveBetween(startDouble, endDouble, valueDouble, "Expected exclusive range: %f to %f", startDouble, endDouble);
        Validate.exclusiveBetween(startDouble - 1.0, endDouble, valueDouble);
        try {
            Validate.exclusiveBetween(startDouble, endDouble - 1.0, valueDouble);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Validate.exclusiveBetween(startDouble, endDouble, null, "Expected non-null value");
    }

    @Test
    public void testIsInstanceOf() {
        Object obj = new Object();
        Validate.isInstanceOf(Object.class, obj);
        Validate.isInstanceOf(String.class, obj);
        try {
            Validate.isInstanceOf(Integer.class, obj);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Object[] array = new Object[0];
        Validate.isInstanceOf(Object[].class, array);
        Validate.isInstanceOf(String[].class, array);
        try {
            Validate.isInstanceOf(Integer[].class, array);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Map<String, Integer> map = new HashMap<>();
        Validate.isInstanceOf(Map.class, map);
        Validate.isInstanceOf(HashMap.class, map);
        try {
            Validate.isInstanceOf(List.class, map);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAssignableFrom() {
        Object obj = new Object();
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        Validate.isAssignableFrom(superClass, subClass);
        Validate.isAssignableFrom(subClass, superClass);

        String str = "foo";
        Validate.isAssignableFrom(String.class, Object.class);
        Validate.isAssignableFrom(Object.class, String.class);
        Validate.isAssignableFrom(String[].class, Object[].class);
        Validate.isAssignableFrom(Object[].class, String[].class);
        Validate.isAssignableFrom(List[].class, Iterable.class);
        Validate.isAssignableFrom(Iterable.class, List[].class);

        try {
            Validate.isAssignableFrom(Integer.class, Object.class);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isAssignableFrom(Object.class, Integer.class);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}