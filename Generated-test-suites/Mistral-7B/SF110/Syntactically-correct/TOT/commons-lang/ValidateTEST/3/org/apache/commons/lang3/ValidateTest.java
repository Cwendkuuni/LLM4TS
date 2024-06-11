package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test with boolean and integer values
        Validate.isTrue(true, "Expected true but was false: %d", 1);
        Validate.isTrue(false, "Expected false but was true: %d", 1);

        // Test with boolean and double values
        Validate.isTrue(true, "Expected true but was false: %f", 1.0);
        Validate.isTrue(false, "Expected false but was true: %f", 1.0);

        // Test with boolean expression
        Validate.isTrue(true, "Expected true but was false");
        Validate.isTrue(false, "Expected false but was true");

        // Test with error message and no arguments
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Error message"));

        // Test with error message and one argument
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Error message: %s", "value"));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Error message: %d", 1));

        // Test with error message and multiple arguments
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Error message: %s %d", "value1", 1));
    }

    @Test
    public void testNotNull() {
        Object obj = new Object();
        Validate.notNull(obj);
        assertNotNull(obj);

        obj = null;
        Validate.notNull(obj, "Expected non-null object but was null");
        assertNull(obj);

        // Test with error message and no arguments
        assertThrows(NullPointerException.class, () -> Validate.notNull(null));
        assertThrows(NullPointerException.class, () -> Validate.notNull(null, "Error message"));

        // Test with error message and one argument
        assertThrows(NullPointerException.class, () -> Validate.notNull(null, "Error message: %s", "value"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNotEmpty() {
        Object[] emptyArray = new Object[0];
        Validate.notEmpty(emptyArray, "Expected non-empty array but was empty");
        assertArrayEquals(emptyArray, emptyArray);

        Object[] nonEmptyArray = new Object[]{1, 2, 3};
        Validate.notEmpty(nonEmptyArray, "Expected non-empty array but was empty");
        assertArrayEquals(nonEmptyArray, nonEmptyArray);

        // Test with error message and no arguments
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new Object[0], "Error message"));

        // Test with error message and one argument
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new Object[0], "Error message: %d", 0));

        // Test with List
        List<Integer> emptyList = Collections.emptyList();
        Validate.notEmpty(emptyList, "Expected non-empty list but was empty");
        assertSame(emptyList, emptyList);

        List<Integer> nonEmptyList = new LinkedList<>(Arrays.asList(1, 2, 3));
        Validate.notEmpty(nonEmptyList, "Expected non-empty list but was empty");
        assertSame(nonEmptyList, nonEmptyList);

        // Test with error message and no arguments
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyList(), "Error message"));

        // Test with error message and one argument
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyList(), "Error message: %d", 0));

        // Test with Map
        Map<String, Integer> emptyMap = new HashMap<>();
        Validate.notEmpty(emptyMap, "Expected non-empty map but was empty");
        assertSame(emptyMap, emptyMap);

        Map<String, Integer> nonEmptyMap = new HashMap<>() {{
            put("key1", 1);
            put("key2", 2);
        }};
        Validate.notEmpty(nonEmptyMap, "Expected non-empty map but was empty");
        assertSame(nonEmptyMap, nonEmptyMap);

        // Test with error message and no arguments
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<>()));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<>(), "Error message"));

        // Test with error message and one argument
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<>(), "Error message: %d", 0));
    }
}