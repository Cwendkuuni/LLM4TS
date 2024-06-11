package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertMapEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrowsExactly;
import static org.junit.Assert.assertLinesMatch;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.equalToIgnoringCase;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.hasEntry;
import static org.hamcrest.CoreMatchers.hasProperty;
import static org.hamcrest.CoreMatchers.isString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true, "Expected true, but was false");
        Validate.isTrue(false, "Expected false, but was true", 1L);
        Validate.isTrue(false, "Expected false, but was true", 1.0);
        Validate.isTrue(false, "Expected false", true, 1L);
        Validate.isTrue(false, "Expected false", 1L);
        Validate.isTrue(false, "Expected false", 1.0, 1L);
        Validate.isTrue(false, "Expected false", new Object[] { 1, "foo" });

        try {
            Validate.isTrue(false, "Expected false");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.isTrue(null, "Expected false");
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            //Expected
        }

        try {
            Validate.isTrue(false, "Expected false", new Object[] { null, null });
            fail("Expected an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //Expected
        }
    }

    @Test
    public void testNotNull() {
        Validate.notNull(new Object(), "Expected a non-null object");

        try {
            Validate.notNull(null, "Expected a non-null object");
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            //Expected
        }
    }

    @Test
    public void testNotEmptyArray() {
        Validate.notEmpty(new String[] {}, "Expected a non-empty array");

        try {
            Validate.notEmpty(new String[0], "Expected a non-empty array");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        Validate.notEmpty(new String[] { "foo", "bar" }, "Expected a non-empty array");
    }

    @Test
    public void testNotEmptyList() {
        Validate.notEmpty(Collections.emptyList(), "Expected a non-empty list");

        try {
            Validate.notEmpty(Collections.emptyList(), "Expected a non-empty list");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        Validate.notEmpty(Arrays.asList("foo", "bar"), "Expected a non-empty list");
    }

    @Test
    public void testNotEmptySet() {
        Validate.notEmpty(Collections.emptySet(), "Expected a non-empty set");

        try {
            Validate.notEmpty(Collections.emptySet(), "Expected a non-empty set");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        Validate.notEmpty(new HashSet<>(Arrays.asList("foo", "bar")), "Expected a non-empty set");
    }

    @Test
    public void testNotEmptyMap() {
        Validate.notEmpty(Collections.emptyMap(), "Expected a non-empty map");

        try {
            Validate.notEmpty(Collections.emptyMap(), "Expected a non-empty map");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //Expected
        }

        Validate.notEmpty(new HashMap<>(), "Expected a non-empty map");
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = new String[] { null, "foo" };

        try {
            Validate.noNullElements(array, "Expected a non-null array with no null elements");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        String[] noNullArray = new String[] { "foo", "bar" };
        Validate.noNullElements(noNullArray, "Expected a non-null array with no null elements");
    }

    @Test
    public void testNoNullElementsIterable() {
        List<String> iterable = Arrays.asList(null, "foo");

        try {
            Validate.noNullElements(iterable, "Expected a non-null iterable with no null elements");
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            //Expected
        }

        List<String> noNullIterable = Arrays.asList("foo", "bar");
        Validate.noNullElements(noNullIterable, "Expected a non-null iterable with no null elements");
    }

    @Test
    public void testValidIndexArray() {
        int[] array = new int[] { 0, 1, 2 };
        Validate.validIndex(array, 1, "Expected a valid index for the array");

        try {
            Validate.validIndex(array, -1, "Expected a valid index for the array");
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }

        try {
            Validate.validIndex(array, 3, "Expected a valid index for the array");
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }
    }

    @Test
    public void testValidIndexList() {
        List<String> list = Arrays.asList("foo", "bar", "baz");
        Validate.validIndex(list, 1, "Expected a valid index for the list");

        try {
            Validate.validIndex(list, -1, "Expected a valid index for the list");
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }

        try {
            Validate.validIndex(list, 3, "Expected a valid index for the list");
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }
    }

    @Test
    public void testValidIndexMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", 2);
        map.put("baz", 3);

        Validate.validIndex(map, "foo", "Expected a valid index for the map");

        try {
            Validate.validIndex(map, "qux", "Expected a valid index for the map");
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException e) {
            //Expected
        }

        try {
            Validate.validIndex(map, "qux", "Expected a valid index for the map");
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException e) {
            //Expected
        }
    }

    @Test
    public void testValidState() {
        Validate.validState(true, "Expected valid state");
        Validate.validState(false, "Expected valid state", "Invalid state: %s", "foo");
    }

    @Test
    public void testMatchesPattern() {
        Validate.matchesPattern("foo", "foo[bar]");
        Validate.matchesPattern("foo", "foo[^bar]");
        Validate.matchesPattern("barfoo", "foo[^bar]");

        try {
            Validate.matchesPattern("qux", "foo[bar]");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.matchesPattern("qux", "foo[^bar]");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testInclusiveBetweenLong() {
        Validate.inclusiveBetween(1L, 3L, 2L);
        Validate.inclusiveBetween(1L, 1L, 1L);
        Validate.inclusiveBetween(1L, 3L, 4L);

        try {
            Validate.inclusiveBetween(1L, 1L, 0L);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.inclusiveBetween(1L, 3L, 5L);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testInclusiveBetweenDouble() {
        Validate.inclusiveBetween(1.0, 3.0, 2.0);
        Validate.inclusiveBetween(1.0, 1.0, 1.0);
        Validate.inclusiveBetween(1.0, 3.0, 4.0);

        try {
            Validate.inclusiveBetween(1.0, 1.0, 0.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.inclusiveBetween(1.0, 3.0, 5.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @ParameterizedTest(name = "InclusiveBetween{0} with {1} and {2}")
    @MethodSource("inclusiveBetweenTestData")
    public void testInclusiveBetweenWithCustomMessage(Integer start, Integer end, Integer value) {
        Validate.inclusiveBetween(start, end, value, "InclusiveBetween with start=%s, end=%s, value=%s", start, end, value);
    }

    static Object[] inclusiveBetweenTestData() {
        return new Object[][] {
                {1, 3, 2},
                {1, 1, 1},
                {1, 3, 4},
                {1, 1, 0},
                {1, 3, 5}
        };
    }

    @Test
    public void testExclusiveBetweenLong() {
        Validate.exclusiveBetween(1L, 3L, 2L);
        Validate.exclusiveBetween(1L, 1L, 1L);
        Validate.exclusiveBetween(1L, 3L, 4L);

        try {
            Validate.exclusiveBetween(1L, 1L, 0L);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.exclusiveBetween(1L, 3L, 5L);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testExclusiveBetweenDouble() {
        Validate.exclusiveBetween(1.0, 3.0, 2.0);
        Validate.exclusiveBetween(1.0, 1.0, 1.0);
        Validate.exclusiveBetween(1.0, 3.0, 4.0);

        try {
            Validate.exclusiveBetween(1.0, 1.0, 0.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.exclusiveBetween(1.0, 3.0, 5.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @ParameterizedTest(name = "ExclusiveBetween{0} with {1} and {2}")
    @MethodSource("exclusiveBetweenTestData")
    public void testExclusiveBetweenWithCustomMessage(Long start, Long end, Long value) {
        Validate.exclusiveBetween(start, end, value, "ExclusiveBetween with start=%s, end=%s, value=%s", start, end, value);
    }

    static Object[] exclusiveBetweenTestData() {
        return new Object[][] {
                {1L, 3L, 2L},
                {1L, 1L, 1L},
                {1L, 3L, 4L},
                {1L, 1L, 0L},
                {1L, 3L, 5L}
        };
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "foo");
        Validate.isInstanceOf(Integer.class, 1);
        Validate.isInstanceOf(String.class, new String("foo"));

        try {
            Validate.isInstanceOf(Float.class, 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Validate.isInstanceOf(String.class, 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(String.class, String.class);
        Validate.isAssignableFrom(Integer.class, Number.class);
        Validate.isAssignableFrom(Number.class, Integer.class);

        try {
            Validate.isAssignableFrom(String.class, Integer.class);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }
}