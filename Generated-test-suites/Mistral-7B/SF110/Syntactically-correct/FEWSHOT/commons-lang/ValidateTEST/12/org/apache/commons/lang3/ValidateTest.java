package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        try {
            Validate.isTrue(true, "Expected: true, but was: false");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(false, "Expected: false, but was: true");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(true, "Expected: true, but was: %d", 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(true, "Expected: true, but was: %d", 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(true, "Expected: true, but was: %s", "false");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(false, "Expected: false, but was: true");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Validate.isTrue(true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotNull() {
        Object obj = new Object();
        Validate.notNull(obj, "Expected: not null, but was: %s", "an object");
        assertEquals(obj, Validate.notNull(obj));

        Object nullObj = null;
        try {
            Validate.notNull(nullObj, "Expected: not null, but was: %s", "a null object");
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyArray() {
        String[] array = new String[0];
        try {
            Validate.notEmpty(array, "Expected: not empty, but was: %s", "an empty array");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String[] notEmptyArray = {"foo", "bar"};
        assertEquals(notEmptyArray, Validate.notEmpty(notEmptyArray));
    }

    @Test
    public void testNotEmptyCollection() {
        ArrayList<String> collection = new ArrayList<>();
        try {
            Validate.notEmpty(collection, "Expected: not empty, but was: %s", "an empty collection");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ArrayList<String> notEmptyCollection = new ArrayList<>(Arrays.asList("foo", "bar"));
        assertEquals(notEmptyCollection, Validate.notEmpty(notEmptyCollection));
    }

    @Test
    public void testNotEmptyMap() {
        HashMap<String, String> map = new HashMap<>();
        try {
            Validate.notEmpty(map, "Expected: not empty, but was: %s", "an empty map");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        HashMap<String, String> notEmptyMap = new HashMap<>(Map.of("foo", "bar"));
        assertEquals(notEmptyMap, Validate.notEmpty(notEmptyMap));
    }

    @Test
    public void testNotEmptyCharSequence() {
        StringBuilder chars = new StringBuilder();
        try {
            Validate.notEmpty(chars, "Expected: not empty, but was: %s", "an empty character sequence");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        StringBuilder notEmptyChars = new StringBuilder("foo");
        assertEquals(notEmptyChars, Validate.notEmpty(notEmptyChars));
    }

    @Test
    public void testNoNullElementsArray() {
        String[] array = {"foo", "bar", null};
        try {
            Validate.noNullElements(array, "Expected: no null elements, but found one at index: %d", 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String[] noNullElementsArray = {"foo", "bar"};
        assertEquals(noNullElementsArray, Validate.noNullElements(noNullElementsArray));
    }

    @Test
    public void testNoNullElementsCollection() {
        ArrayList<String> collection = new ArrayList<>(Arrays.asList("foo", null, "bar"));
        try {
            Validate.noNullElements(collection, "Expected: no null elements, but found one at index: %d", 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        ArrayList<String> noNullElementsCollection = new ArrayList<>(Arrays.asList("foo", "bar"));
        assertEquals(noNullElementsCollection, Validate.noNullElements(noNullElementsCollection));
    }

    @Test
    public void testValidIndexArray() {
        String[] array = {"foo", "bar"};
        try {
            Validate.validIndex(array, 3, "Expected: valid index, but was: %d", 3);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            Validate.validIndex(array, 1);
            assertEquals("bar", array[1]);
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }

    @Test
    public void testValidIndexCollection() {
        ArrayList<String> collection = new ArrayList<>(Arrays.asList("foo", "bar"));
        try {
            Validate.validIndex(collection, 3, "Expected: valid index, but was: %d", 3);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            Validate.validIndex(collection, 1);
            assertEquals("bar", collection.get(1));
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }

    @Test
    public void testValidIndexCharSequence() {
        StringBuilder chars = new StringBuilder("foo");
        try {
            Validate.validIndex(chars, 4, "Expected: valid index, but was: %d", 4);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            Validate.validIndex(chars, 1);
            assertEquals("o", chars.charAt(1));
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }

    @Test
    public void testValidState() {
        boolean state = false;
        try {
            Validate.validState(state);
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        state = true;
        Validate.validState(state);
    }

    @Test
    public void testMatchesPattern() {
        String input = "foo[bar]";
        String pattern = "[a-z]+";
        try {
            Validate.matchesPattern(input, pattern);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String input2 = "abcd";
        String pattern2 = "[a-z]+";
        Validate.matchesPattern(input2, pattern2);
    }

    @Test
    public void testInclusiveBetweenLong() {
        long value = 5;
        long start = 3;
        long end = 7;
        try {
            Validate.inclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }

        long value2 = 0;
        try {
            Validate.inclusiveBetween(start, end, value2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value3 = 8;
        try {
            Validate.inclusiveBetween(start, end, value3);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInclusiveBetweenDouble() {
        double value = 5.5;
        double start = 3.0;
        double end = 7.0;
        try {
            Validate.inclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }

        double value2 = 0.0;
        try {
            Validate.inclusiveBetween(start, end, value2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value3 = 8.5;
        try {
            Validate.inclusiveBetween(start, end, value3);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenLong() {
        long value = 5;
        long start = 3;
        long end = 7;
        try {
            Validate.exclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }

        long value2 = 3;
        try {
            Validate.exclusiveBetween(start, end, value2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long value3 = 7;
        try {
            Validate.exclusiveBetween(start, end, value3);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenDouble() {
        double value = 5.5;
        double start = 3.0;
        double end = 7.0;
        try {
            Validate.exclusiveBetween(start, end, value);
        } catch (IllegalArgumentException e) {
            fail("Expected no exception");
        }

        double value2 = 3.0;
        try {
            Validate.exclusiveBetween(start, end, value2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        double value3 = 7.0;
        try {
            Validate.exclusiveBetween(start, end, value3);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsInstanceOf() {
        String str = "foo";
        Class<String> stringClass = String.class;
        Validate.isInstanceOf(stringClass, str);

        Object obj = new Object();
        try {
            Validate.isInstanceOf(String.class, obj);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAssignableFrom() {
        String s = "foo";
        Number n = (Number) s; // should compile but throw a ClassCastException at runtime

        Number numberClass = Number.class;
        String stringClass = String.class;
        Validate.isAssignableFrom(stringClass, numberClass);

        try {
            Validate.isAssignableFrom(numberClass, stringClass);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}