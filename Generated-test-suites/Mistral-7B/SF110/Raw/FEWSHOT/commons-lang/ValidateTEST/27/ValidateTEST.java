package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but was false");
        Validate.isTrue(5 > 3, "Expected 5 > 3 but was %d", 3);
        Validate.isTrue(3.14 > 2.718, "Expected 3.14 > 2.718 but was %f", 2.718);
    }

    @Test
    public void testNotNull() {
        Validate.notNull(new Object());
        assertEquals(new Object(), Validate.notNull(new Object()));

        assertThrows(NullPointerException.class, () -> Validate.notNull(null));
        assertThrows(NullPointerException.class, () -> Validate.notNull(null, "Expected non-null but was null"));
    }

    @Test
    public void testNotEmptyArray() {
        Validate.notEmpty(new String[0]);
        Validate.notEmpty(new String[]{"foo"});

        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new String[0], "Expected non-empty but was empty"));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new String[]{null}, "Expected non-empty but was empty"));
    }

    @Test
    public void testNotEmptyCollection() {
        Validate.notEmpty(Collections.emptyList());
        Validate.notEmpty(Arrays.asList("foo", "bar"));

        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyList(), "Expected non-empty but was empty"));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Arrays.asList(null), "Expected non-empty but was empty"));
    }

    @Test
    public void testNotEmptyMap() {
        Validate.notEmpty(Collections.emptyMap());
        Validate.notEmpty(new HashMap<>());
        Validate.notEmpty(new HashMap<>(){ { put("foo", "bar"); } });

        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(Collections.emptyMap(), "Expected non-empty but was empty"));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<>(), "Expected non-empty but was empty"));
    }

    @Test
    public void testNotBlankCharSequence() {
        Validate.notBlank("foo");
        Validate.notBlank("  foo ");
        Validate.notBlank("foo bar");

        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank(""));
        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank(""));
    }

    @Test
    public void testNoNullElementsArray() {
        Object[] array = new Object[3];
        array[0] = new Object();
        array[1] = new Object();
        array[2] = null;

        Validate.noNullElements(array);
        Validate.noNullElements(array, "Expected non-null elements but found null at index %d", 2);

        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(new Object[0], "Expected non-null elements but found null at index %d", 0));
    }

    @Test
    public void testNoNullElementsCollection() {
        List<Object> list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());
        list.add(null);

        Validate.noNullElements(list);
        Validate.noNullElements(list, "Expected non-null elements but found null at index %d", 2);

        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(Collections.emptyList(), "Expected non-null elements but found null at index %d", 0));
    }

    @Test
    public void testValidIndexArray() {
        String[] array = new String[3];
        array[0] = "foo";
        array[1] = "bar";

        Validate.validIndex(array, 0);
        Validate.validIndex(array, 1);

        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(array, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(array, 2, "Expected valid index but was out of bounds"));
    }

    @Test
    public void testValidIndexCollection() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");

        Validate.validIndex(list, 0);
        Validate.validIndex(list, 1);

        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, 2, "Expected valid index but was out of bounds"));
    }

    @Test
    public void testValidState() {
        assertTrue(true);
        assertFalse(false);

        Validate.validState(true);
        Validate.validState(false, "Expected true but was false");
    }

    @Test
    public void testMatchesPattern() {
        String input = "foo[bar]";
        String pattern = "[a-z]+";

        Validate.matchesPattern(input, pattern);
        Validate.matchesPattern(input, pattern, "Expected input to match pattern %s but did not", pattern);

        assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern("foo[bar]", "([a-z]+)"));
        assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern("foo[bar]", "([a-z]+)", "Expected input to match pattern %s but did not", "[a-z]+"));
    }

    @Test
    public void testInclusiveBetween() {
        Integer start = 0;
        Integer end = 5;

        Validate.inclusiveBetween(5, start, Integer.class);
        Validate.inclusiveBetween(3, start, Integer.class);
        Validate.inclusiveBetween(start, start, Integer.class);

        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(6, start, Integer.class));
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(6, start, Integer.class, "Expected value to be within inclusive range of %s to %s but was %d", start, end, 6));
    }

    @Test
    public void testInclusiveBetweenLong() {
        Long start = 0L;
        Long end = 5L;

        Validate.inclusiveBetween(6L, start, Long.class);
        Validate.inclusiveBetween(3L, start, Long.class);
        Validate.inclusiveBetween(start, start, Long.class);

        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(7L, start, Long.class));
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(7L, start, Long.class, "Expected value to be within inclusive range of %s to %s but was %d", start, end, 7));
    }

    @Test
    public void testInclusiveBetweenDouble() {
        Double start = 0.0;
        Double end = 5.0;

        Validate.inclusiveBetween(6.0, start, Double.class);
        Validate.inclusiveBetween(3.0, start, Double.class);
        Validate.inclusiveBetween(start, start, Double.class);

        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(7.0, start, Double.class));
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(7.0, start, Double.class, "Expected value to be within inclusive range of %s to %s but was %f", start, end, 7.0));
    }

    @Test
    public void testExclusiveBetween() {
        Integer start = 0;
        Integer end = 5;

        Validate.exclusiveBetween(5, start, Integer.class);
        Validate.exclusiveBetween(3, start, Integer.class);
        Validate.exclusiveBetween(start, start, Integer.class);

        Validate.exclusiveBetween(5, start, Integer.class, "Expected value to be outside exclusive range of %s to %s but was %d", start, end, 5);
        Validate.exclusiveBetween(4, start, Integer.class, "Expected value to be outside exclusive range of %s to %s but was %d", start, end, 4);
    }

    @Test
    public void testExclusiveBetweenLong() {
        Long start = 0L;
        Long end = 5L;

        Validate.exclusiveBetween(5L, start, Long.class);
        Validate.exclusiveBetween(3L, start, Long.class);
        Validate.exclusiveBetween(start, start, Long.class);

        Validate.exclusiveBetween(5L, start, Long.class, "Expected value to be outside exclusive range of %s to %s but was %d", start, end, 5);
        Validate.exclusiveBetween(4L, start, Long.class, "Expected value to be outside exclusive range of %s to %s but was %d", start, end, 4);
    }

    @Test
    public void testExclusiveBetweenDouble() {
        Double start = 0.0;
        Double end = 5.0;

        Validate.exclusiveBetween(5.0, start, Double.class);
        Validate.exclusiveBetween(3.0, start, Double.class);
        Validate.exclusiveBetween(start, start, Double.class);

        Validate.exclusiveBetween(5.0, start, Double.class, "Expected value to be outside exclusive range of %s to %s but was %f", start, end, 5.0);
        Validate.exclusiveBetween(4.0, start, Double.class, "Expected value to be outside exclusive range of %s to %s but was %f", start, end, 4.0);
    }

    @Test
    public void testIsInstanceOf() {
        Integer i = 5;
        String s = "foo";

        Validate.isInstanceOf(Integer.class, i);
        Validate.isInstanceOf(String.class, s);

        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(Double.class, i));
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(Double.class, i, "Expected instance of %s but was %s", Double.class.getName(), i.getClass().getName()));
    }

    @Test
    public void testIsAssignableFrom() {
        String s = "foo";
        Integer i = 5;

        Validate.isAssignableFrom(String.class, s.getClass());
        Validate.isAssignableFrom(Number.class, i.getClass());

        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Double.class, i.getClass()));
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(Double.class, i.getClass(), "Expected assignable from %s but was %s", Double.class.getName(), i.getClass().getName()));
    }
}