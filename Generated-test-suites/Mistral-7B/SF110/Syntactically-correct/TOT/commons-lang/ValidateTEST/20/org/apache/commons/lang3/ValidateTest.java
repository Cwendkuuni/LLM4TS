package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanExpression() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanExpression() {
        Validate.isTrue(false);
    }

    @Test
    public void testIsTrueWithStringMessageAndLongValue() {
        Validate.isTrue(true, "Is true message %d", 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndLongValue() {
        Validate.isTrue(false, "Is true message %d", 1L);
    }

    @Test
    public void testIsTrueWithStringMessageAndDoubleValue() {
        Validate.isTrue(true, "Is true message %f", 1.0D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndDoubleValue() {
        Validate.isTrue(false, "Is true message %f", 1.0D);
    }

    @Test
    public void testIsTrueWithStringMessageAndObjects() {
        Validate.isTrue(true, "Is true message %s %s %s", "apple", "is", "red");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseStringMessageAndObjects() {
        Validate.isTrue(false, "Is true message %s %s %s", "apple", "is", "red");
    }

    @Test
    public void testIsTrueWithoutMessageOrValues() {
        Validate.isTrue(true);
    }

    @Test
    public void testIsTrueWithoutMessageOrValuesAndBooleanTrue() {
        Validate.isTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithoutMessageOrValuesAndBooleanFalse() {
        Validate.isTrue(false);
    }

    @Test
    public void testNotNullForObject() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNullForNullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotEmptyForArray() {
        Object[] array = new Object[10];
        Validate.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyForEmptyArray() {
        Object[] array = new Object[0];
        Validate.notEmpty(array);
    }

    @Test
    public void testNotEmptyForCollection() {
        List<Object> list = new LinkedList<>();
        Validate.notEmpty(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyForEmptyCollection() {
        List<Object> list = Collections.emptyList();
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmptyForMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        Validate.notEmpty(map);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyForEmptyMap() {
        Map<String, Object> map = new HashMap<>();
        Validate.notEmpty(map);
    }

    @Test
    public void testNotBlankForCharSequence() {
        String str = "Hello World";
        Validate.notBlank(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankForEmptyCharSequence() {
        String str = "";
        Validate.notBlank(str);
    }

    @Test
    public void testNoNullElementsForArray() {
        Object[] array = new Object[10];
        array[0] = new Object();
        array[1] = null;
        try {
            Validate.noNullElements(array);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoNullElementsForIterable() {
        List<Object> list = new LinkedList<>();
        list.add(new Object());
        list.add(null);
        try {
            Validate.noNullElements(list);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexForArray() {
        Object[] array = new Object[10];
        Validate.validIndex(array, 0);
        Validate.validIndex(array, 1);
        Validate.validIndex(array, 2);
        try {
            Validate.validIndex(array, 10);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexForCollection() {
        List<Object> list = new LinkedList<>();
        list.add(new Object());
        list.add(null);
        list.add(new Object());
        Validate.validIndex(list, 0);
        Validate.validIndex(list, 1);
        Validate.validIndex(list, 2);
        try {
            Validate.validIndex(list, 3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidIndexForCharSequence() {
        String str = "Hello World";
        Validate.validIndex(str, 0);
        Validate.validIndex(str, 1);
        Validate.validIndex(str, 2);
        try {
            Validate.validIndex(str, 11);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testValidState() {
        boolean state = true;
        Validate.validState(state);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseState() {
        boolean state = false;
        Validate.validState(state);
    }

    @Test
    public void testValidStateWithMessage() {
        boolean state = true;
        Validate.validState(state, "Valid state message");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidStateWithFalseStateAndMessage() {
        boolean state = false;
        Validate.validState(state, "Valid state message");
    }

    @Test
    public void testMatchesPatternForCharSequence() {
        String input = "1234abcd1234";
        String pattern = "\\d{4}[a-z]{4}\\d{4}";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternForNonMatchingCharSequence() {
        String input = "1234abcd1234";
        String pattern = "\\d{4}[a-z]{5}\\d{4}";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testMatchesPatternForCharSequenceWithMessage() {
        String input = "1234abcd1234";
        String pattern = "\\d{4}[a-z]{5}\\d{4}";
        Validate.matchesPattern(input, pattern, "String does not match pattern %s", pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPatternForNonMatchingCharSequenceWithMessage() {
        String input = "1234abcd1234";
        String pattern = "\\d{4}[a-z]{4}\\d{4}";
        Validate.matchesPattern(input, pattern, "String does not match pattern %s", pattern);
    }

    @Test
    public void testInclusiveBetweenForComparable() {
        Integer value = 10;
        Validate.inclusiveBetween(5, 15, value);
        Validate.inclusiveBetween(5, 15, value, "Value %d is not in the specified inclusive range of %s to %s", 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenForComparableOutOfRange() {
        Integer value = 10;
        Validate.inclusiveBetween(2, 8, value);
        Validate.inclusiveBetween(2, 8, value, "Value %d is not in the specified inclusive range of %s to %s", 12);
    }

    @Test
    public void testInclusiveBetweenForLong() {
        long value = 10L;
        Validate.inclusiveBetween(5, 15, value);
        Validate.inclusiveBetween(5, 15, value, "Value %d is not in the specified inclusive range of %s to %s", 12L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenForLongOutOfRange() {
        long value = 10L;
        Validate.inclusiveBetween(2, 8, value);
        Validate.inclusiveBetween(2, 8, value, "Value %d is not in the specified inclusive range of %s to %s", 12L);
    }

    @Test
    public void testInclusiveBetweenForDouble() {
        double value = 10.5;
        Validate.inclusiveBetween(5.0, 15.0, value);
        Validate.inclusiveBetween(5.0, 15.0, value, "Value %f is not in the specified inclusive range of %f to %f", 12.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetweenForDoubleOutOfRange() {
        double value = 10.5;
        Validate.inclusiveBetween(2.0, 8.0, value);
        Validate.inclusiveBetween(2.0, 8.0, value, "Value %f is not in the specified inclusive range of %f to %f", 12.5);
    }

    @Test
    public void testExclusiveBetweenForComparable() {
        Integer value = 10;
        Validate.exclusiveBetween(4, 14, value);
        Validate.exclusiveBetween(4, 14, value, "Value %d is not in the specified exclusive range of %s to %s", 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenForComparableOutOfRange() {
        Integer value = 10;
        Validate.exclusiveBetween(1, 9, value);
        Validate.exclusiveBetween(1, 9, value, "Value %d is not in the specified exclusive range of %s to %s", 11);
    }

    @Test
    public void testExclusiveBetweenForLong() {
        long value = 10L;
        Validate.exclusiveBetween(4, 14, value);
        Validate.exclusiveBetween(4, 14, value, "Value %d is not in the specified exclusive range of %s to %s", 11L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenForLongOutOfRange() {
        long value = 10L;
        Validate.exclusiveBetween(1, 9, value);
        Validate.exclusiveBetween(1, 9, value, "Value %d is not in the specified exclusive range of %s to %s", 11L);
    }

    @Test
    public void testExclusiveBetweenForDouble() {
        double value = 10.5;
        Validate.exclusiveBetween(4.0, 14.0, value);
        Validate.exclusiveBetween(4.0, 14.0, value, "Value %f is not in the specified exclusive range of %f to %f", 11.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetweenForDoubleOutOfRange() {
        double value = 10.5;
        Validate.exclusiveBetween(1.0, 9.0, value);
        Validate.exclusiveBetween(1.0, 9.0, value, "Value %f is not in the specified exclusive range of %f to %f", 11.5);
    }

    @Test
    public void testIsInstanceOf() {
        String str = "Hello World";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithWrongType() {
        String str = "Hello World";
        Validate.isInstanceOf(Integer.class, str);
    }

    @Test
    public void testIsAssignableFrom() {
        String str = "Hello World";
        Validate.isAssignableFrom(String.class, StringBuffer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithWrongTypes() {
        String str = "Hello World";
        Validate.isAssignableFrom(String.class, Integer.class);
    }
}