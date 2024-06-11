package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ValidateTest {
    @Test
    public void testIsTrue() {
        // Testing with different types and messages
        Validate.isTrue(true, "Is true (boolean)", 1L);
        Validate.isTrue(true, "Is true (boolean)", 1.0);
        Validate.isTrue(true, "Is true (boolean)", new Object[]{1, 2, 3});
        Validate.isTrue(true, "Is true");

        // Testing with false condition
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Is true", 1L));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Is true", 1.0));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Is true", new Object[]{1, 2, 3}));
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false));
    }

    @Test
    public void testNotNull() {
        // Testing with different types and messages
        String str = new String("Test");
        Integer intVal = 1;
        Validate.notNull(str, "NotNull (String)", new Object[]{str});
        Validate.notNull(intVal, "NotNull (Integer)", new Object[]{intVal});

        // Testing with null object
        assertThrows(NullPointerException.class, () -> Validate.notNull(null, "NotNull (Object)", new Object[0]));
    }

    @Test
    public void testNotEmptyArray() {
        // Testing with different types and messages
        int[] intArray = new int[]{1, 2, 3};
        String[] strArray = new String[]{"Test1", "Test2", "Test3"};
        Validate.notEmpty(intArray, "Not empty (int[])", new Object[]{intArray});
        Validate.notEmpty(strArray, "Not empty (String[])", new Object[]{strArray});

        // Testing with empty array
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new int[0], "Not empty (int[])", new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new String[0], "Not empty (String[])", new Object[0]));
    }

    @Test
    public void testNotEmptyCollection() {
        // Testing with different types and messages
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Set<String> strSet = new HashSet<>(Arrays.asList("Test1", "Test2", "Test3"));
        Validate.notEmpty(intList, "Not empty (List<Integer>)", new Object[]{intList});
        Validate.notEmpty(strSet, "Not empty (Set<String>)", new Object[]{strSet});

        // Testing with empty collection
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyList(), "Not empty (List<Integer>)", new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptySet(), "Not empty (Set<String>)", new Object[0]));
    }

    @Test
    public void testNotEmptyMap() {
        // Testing with different types and messages
        Map<String, Integer> map = new HashMap<>();
        map.put("TestKey1", 1);
        map.put("TestKey2", 2);
        Validate.notEmpty(map, "Not empty (Map<String, Integer>)");

        // Testing with empty map
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<>(), "Not empty (Map<String, Integer>)"));
    }

    @Test
    public void testNotBlank() {
        // Testing with different types and messages
        String str = new String("Test");
        Validate.notBlank(str, "Not blank (String)", new Object[]{str});

        // Testing with blank string
        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank(new String(""), "Not blank (String)", new Object[0]));
    }

    @Test
    public void testNoNullElementsArray() {
        // Testing with different types and messages
        int[] intArray = new int[]{1, 2, 3};
        String[] strArray = new String[]{"Test1", "Test2", "Test3"};
        Validate.noNullElements(intArray, "No null elements (int[])", new Object[]{intArray});
        Validate.noNullElements(strArray, "No null elements (String[])", new Object[]{strArray});

        // Testing with null element in array
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(new int[]{1, null, 3}, "No null elements (int[])", new Object[]{new int[]{1, null, 3}}));
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(new String[]{"Test1", null, "Test3"}, "No null elements (String[])", new Object[]{new String[]{"Test1", null, "Test3"}}));
    }

    @Test
    public void testNoNullElementsCollection() {
        // Testing with different types and messages
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Set<String> strSet = new HashSet<>(Arrays.asList("Test1", "Test2", "Test3"));
        Validate.noNullElements(intList, "No null elements (List<Integer>)");
        Validate.noNullElements(strSet, "No null elements (Set<String>)");

        // Testing with null element in collection
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(Collections.singletonList(null), "No null elements (List<Integer>)"));
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(Collections.singleton(null), "No null elements (Set<String>)"));
    }

    @Test
    public void testValidIndexArray() {
        // Testing with different types and messages
        int[] intArray = new int[]{1, 2, 3};
        Validate.validIndex(intArray, 1, "Valid index (int[])");
        Validate.validIndex(intArray, 2, "Valid index (int[])");

        // Testing with invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(intArray, 4, "Valid index (int[])"));
    }

    @Test
    public void testValidIndexCollection() {
        // Testing with different types and messages
        List<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Validate.validIndex(intList, 1, "Valid index (List<Integer>)");
        Validate.validIndex(intList, 2, "Valid index (List<Integer>)");

        // Testing with invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(intList, 4, "Valid index (List<Integer>)"));
    }

    @Test
    public void testValidIndexCharSequence() {
        // Testing with different types and messages
        String str = new String("Test");
        Validate.validIndex(str, 1, "Valid index (CharSequence)");
        Validate.validIndex(str, 2, "Valid index (CharSequence)");

        // Testing with invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(str, 4, "Valid index (CharSequence)"));
    }

    @Test
    public void testValidState() {
        // Testing with true state
        Validate.validState(true);

        // Testing with false state
        assertThrows(IllegalStateException.class, () -> Validate.validState(false));
    }

    @Test
    public void testMatchesPattern() {
        // Testing with valid input and pattern
        String strInput = "Test123";
        String pattern = "Test[0-9]{3}";
        Validate.matchesPattern(strInput, pattern);

        // Testing with invalid input and pattern
        assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern(new String("InvalidInput"), "Test[0-9]{3}"));
    }

    @Test
    public void testInclusiveBetween() {
        // Testing with valid values
        Validate.inclusiveBetween(1L, 3L, 2L);
        Validate.inclusiveBetween(1.0, 3.0, 2.0);

        // Testing with invalid values
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1L, 3L, 0L));
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 3.0, 0.0));
    }

    @Test
    public void testExclusiveBetween() {
        // Testing with valid values
        Validate.exclusiveBetween(1L, 3L, 2L);
        Validate.exclusiveBetween(1.0, 3.0, 2.0);

        // Testing with invalid values
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1L, 3L, 2L));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 3.0, 2.0));

        // Testing with edge cases
        Validate.exclusiveBetween(1L, 3L, 1L);
        Validate.exclusiveBetween(1.0, 3.0, 1.0);
        Validate.exclusiveBetween(1.0, 3.0, 2.999999999D);
        Validate.exclusiveBetween(1.0, 3.0, 3.0);
    }

    @Test
    public void testIsInstanceOf() {
        // Testing with valid types
        Validate.isInstanceOf(String.class, "Test String");
        Validate.isInstanceOf(Integer.class, 1);

        // Testing with invalid types
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(Double.class, 1L));
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(Integer.class, 1.0));
    }

    @Test
    public void testIsAssignableFrom() {
        // Testing with valid types
        Validate.isAssignableFrom(Number.class, Integer.class);
        Validate.isAssignableFrom(List.class, ArrayList.class);

        // Testing with invalid types
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Integer.class, Double.class));
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(List.class, LinkedList.class));
    }
}