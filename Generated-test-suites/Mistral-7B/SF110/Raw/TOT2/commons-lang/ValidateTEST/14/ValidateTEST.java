package org.apache.commons.lang3.test;

import org.apache.commons.lang3.Validate;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test with boolean expression and message, long value
        Validate.isTrue(true, "Expected: true, actual: %d", 42);
        Validate.isTrue(true);

        // Test with boolean expression and message, double value
        Validate.isTrue(true, "Expected: true, actual: %f", 3.14);
        Validate.isTrue(true);

        // Test with boolean expression and message, Object... values
        Object[] values = new Object[]{"Expected: true, actual: %s", "value"};
        Validate.isTrue(true, "Expected: true, actual: %s", "value");
        Validate.isTrue(true, (String)values[0], values);

        // Test with boolean expression only
        Validate.isTrue(true);
    }

    @Test
    public void testNotNull_Object() {
        Object obj = new Object();
        Validate.notNull(obj);
    }

    @Test(expected = NullPointerException.class)
    public void testNotNull_NullObject() {
        Validate.notNull(null);
    }

    @Test
    public void testNotNull_Message() {
        Object obj = null;
        Validate.notNull(obj, "Expected: not null, actual: null");
    }

    @Test
    public void testNotEmpty_Array() {
        String[] arr = {"Hello", "World"};
        Validate.notEmpty(arr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmpty_EmptyArray() {
        String[] arr = new String[0];
        Validate.notEmpty(arr);
    }

    @Test
    public void testNotEmpty_Array_Message() {
        String[] arr = new String[0];
        Validate.notEmpty(arr, "Expected: not empty, actual: empty");
    }

    @Test
    public void testNotEmpty_Collection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Validate.notEmpty(list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmpty_EmptyCollection() {
        List<Integer> list = Collections.emptyList();
        Validate.notEmpty(list);
    }

    @Test
    public void testNotEmpty_Collection_Message() {
        List<Integer> list = Collections.emptyList();
        Validate.notEmpty(list, "Expected: not empty, actual: empty");
    }

    @Test
    public void testValidIndex_Array() {
        String[] arr = {"Hello", "World"};
        Validate.validIndex(arr, 0);
        Validate.validIndex(arr, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndex_Array_InvalidIndex() {
        String[] arr = {"Hello", "World"};
        Validate.validIndex(arr, 2);
    }

    @Test
    public void testValidIndex_Collection() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Validate.validIndex(list, 0);
        Validate.validIndex(list, 1);
        Validate.validIndex(list, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValidIndex_Collection_InvalidIndex() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Validate.validIndex(list, 3);
    }

    @Test
    public void testMatchesPattern() {
        String input = "abcd1234";
        String pattern = "^[a-z]\\d{4}$";
        Validate.matchesPattern(input, pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesPattern_InvalidInput() {
        String input = "abcd1234x";
        String pattern = "^[a-z]\\d{4}$";
        Validate.matchesPattern(input, pattern);
    }

    @Test
    public void testInclusiveBetween_Int() {
        int value = 2;
        Validate.inclusiveBetween(1, 3, value);
        Validate.inclusiveBetween(1, 3, value, "Expected: in inclusive range [1, 3], actual: %d", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetween_Int_OutOfRange() {
        int value = 0;
        Validate.inclusiveBetween(1, 3, value);
    }

    @Test
    public void testInclusiveBetween_Long() {
        long value = 10L;
        Validate.inclusiveBetween(5L, 15L, value);
        Validate.inclusiveBetween(5L, 15L, value, "Expected: in inclusive range [5, 15], actual: %d", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusiveBetween_Long_OutOfRange() {
        long value = 4L;
        Validate.inclusiveBetween(5L, 15L, value);
    }

    @Test
    public void testExclusiveBetween_Int() {
        int value = 2;
        Validate.exclusiveBetween(1, 3, value);
        Validate.exclusiveBetween(1, 3, value, "Expected: in exclusive range [1, 3), actual: %d", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetween_Int_OutOfRange() {
        int value = 1;
        Validate.exclusiveBetween(1, 3, value);
    }

    @Test
    public void testExclusiveBetween_Long() {
        long value = 10L;
        Validate.exclusiveBetween(5L, 15L, value);
        Validate.exclusiveBetween(5L, 15L, value, "Expected: in exclusive range (5, 15], actual: %d", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExclusiveBetween_Long_OutOfRange() {
        long value = 4L;
        Validate.exclusiveBetween(5L, 15L, value);
    }

    @Test
    public void testIsInstanceOf() {
        String str = "Hello";
        Validate.isInstanceOf(String.class, str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOf_InvalidType() {
        int num = 42;
        Validate.isInstanceOf(String.class, num);
    }

    @Test
    public void testIsAssignableFrom() {
        Number num = 42;
        Validate.isAssignableFrom(Number.class, num.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFrom_InvalidTypes() {
        String str = "Hello";
        Validate.isAssignableFrom(Number.class, str.getClass());
    }
}