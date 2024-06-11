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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidateTest {

    @Test
    public void testIsTrueWithBoolean() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBoolean() {
        Validate.isTrue(false);
    }

    @Test
    public void testIsTrueWithLong() {
        Validate.isTrue(1L > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithNegativeLong() {
        Validate.isTrue(-1L > 0);
    }

    @Test
    public void testIsTrueWithDouble() {
        Validate.isTrue(1.0D > 0.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithNegativeDouble() {
        Validate.isTrue(-1.0D > 0.0D);
    }

    @Test
    public void testIsTrueWithStrings() {
        Validate.isTrue(" abcdefg".length() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithEmptyString() {
        Validate.isTrue("".length() > 0);
    }

    @Test
    public void testIsTrueWithObjectArray() {
        Object[] array = new Object[]{1, 2, 3};
        Validate.isTrue(array.length > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithEmptyObjectArray() {
        Object[] array = new Object[0];
        Validate.isTrue(array.length > 0);
    }

    @Test
    public void testIsTrueWithNoArguments() {
        Validate.isTrue(true);
    }

    @Test
    public void testNotNullWithNullObject() {
        try {
            Validate.notNull(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNotNullWithNonNullObject() {
        Object obj = new Object();
        Validate.notNull(obj);
        assertNotNull(obj);
    }

    @Test
    public void testNotEmptyWithEmptyArray() {
        try {
            Validate.notEmpty(new Integer[0]);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyWithNonEmptyArray() {
        Integer[] array = new Integer[]{1, 2, 3};
        Validate.notEmpty(array);
        assertNotNull(array);
        assertArrayEquals(new Integer[]{1, 2, 3}, array);
    }

    @Test
    public void testNotEmptyWithEmptyCollection() {
        try {
            Validate.notEmpty(Collections.emptyList());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyWithNonEmptyCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Validate.notEmpty(list);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testNotEmptyWithEmptyMap() {
        try {
            Validate.notEmpty(Collections.emptyMap());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotEmptyWithNonEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Validate.notEmpty(map);
        assertNotNull(map);
        assertTrue(map.size() > 0);
    }

    @Test
    public void testNotBlankWithEmptyString() {
        try {
            Validate.notBlank("");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNotBlankWithNonEmptyString() {
        String str = "abcdefg";
        Validate.notBlank(str);
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    public void testNoNullElementsWithEmptyArray() {
        try {
            Validate.noNullElements(new Integer[0]);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoNullElementsWithNonEmptyArray() {
        Integer[] array = new Integer[]{1, 2, 3};
        Validate.noNullElements(array);
        assertNotNull(array);
        assertArrayEquals(new Integer[]{1, 2, 3}, array);
    }

    @Test
    public void testNoNullElementsWithEmptyList() {
        try {
            Validate.noNullElements(Collections.emptyList());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoNullElementsWithNonEmptyList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Validate.noNullElements(list);
        assertNotNull(list);
        assertTrue(list.size() > 0);
        for (Object obj : list) {
            assertNotNull(obj);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithNegativeIndex() {
        String[] arr = new String[0];
        Validate.validIndex(arr, -1);
    }

    @Test
    public void testValidIndexWithValidIndex() {
        String[] arr = new String[]{"one", "two", "three"};
        Validate.validIndex(arr, 1);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        assertNotNull(arr[1]);
        assertEquals("two", arr[1]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCollectionIndex() {
        List<String> list = new ArrayList<>();
        Validate.validIndex(list, -1);
    }

    @Test
    public void testValidIndexWithValidCollectionIndex() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        Validate.validIndex(list, 1);
        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertNotNull(list.get(1));
        assertEquals("two", list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndexWithInvalidCharSequenceIndex() {
        String str = "abcdefg";
        Validate.validIndex(str, -1);
    }

    @Test
    public void testValidIndexWithValidCharSequenceIndex() {
        String str = "abcdefg";
        Validate.validIndex(str, 5);
        assertNotNull(str);
        assertTrue(str.length() > 0);
        assertNotNull(str.charAt(5));
        assertEquals('f', str.charAt(5));
    }

    @Test
    public void testValidStateWithTrueState() {
        Validate.validState(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        Validate.validState(false);
    }

    @Test
    public void testMatchesPatternWithMatchingPatternAndInput() {
        Validate.matchesPattern("[a-z]+", "abcdefg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternWithMismatchingPatternAndInput() {
        Validate.matchesPattern("[a-z]+", "12345");
    }

    @Test
    public void testInclusiveBetweenWithLongs() {
        Validate.inclusiveBetween(0L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithLongsOutOfRange() {
        try {
            Validate.inclusiveBetween(0L, 10L, 11L);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInclusiveBetweenWithIntegers() {
        Validate.inclusiveBetween(0, 10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithIntegersOutOfRange() {
        try {
            Validate.inclusiveBetween(0, 10, 11);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInclusiveBetweenWithDoubles() {
        Validate.inclusiveBetween(0.0D, 10.0D, 5.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenWithDoublesOutOfRange() {
        try {
            Validate.inclusiveBetween(0.0D, 10.0D, 11.0D);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenWithLongs() {
        Validate.exclusiveBetween(1L, 10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithLongsOutOfRange() {
        try {
            Validate.exclusiveBetween(1L, 10L, 4L);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenWithIntegers() {
        Validate.exclusiveBetween(1, 10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithIntegersOutOfRange() {
        try {
            Validate.exclusiveBetween(1, 10, 4);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExclusiveBetweenWithDoubles() {
        Validate.exclusiveBetween(1.0D, 10.0D, 5.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenWithDoublesOutOfRange() {
        try {
            Validate.exclusiveBetween(1.0D, 10.0D, 4.0D);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsInstanceOf() {
        Validate.isInstanceOf(String.class, "abcdefg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithMismatchingTypes() {
        try {
            Validate.isInstanceOf(Integer.class, "abcdefg");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAssignableFrom() {
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithMismatchingTypes() {
        try {
            Validate.isAssignableFrom(Integer.class, StringBuffer.class);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}