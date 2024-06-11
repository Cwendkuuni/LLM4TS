package org.joda.time.field.test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        int expected = 3;
        assertEquals(expected, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long longVal1 = Long.MIN_VALUE;
        long longVal2 = 1L;
        try {
            FieldUtils.safeAdd(longVal1, longVal2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long longVal1_2 = Long.MIN_VALUE;
        long longVal2_2 = Long.MIN_VALUE;
        long sumLong = FieldUtils.safeAdd(longVal1_2, longVal2_2);
        long expectedLong = Long.MIN_VALUE - 1;
        assertEquals(expectedLong, sumLong);
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        long expected = 5L;
        assertEquals(expected, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int result = FieldUtils.safeMultiply(val1, val2);
        int expected = 12;
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long longVal1 = Long.MIN_VALUE;
        int val2_int = 2;
        try {
            FieldUtils.safeMultiply(longVal1, val2_int);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long longVal1_2 = Long.MIN_VALUE;
        long longVal2_2 = Long.MIN_VALUE;
        long resultLong = FieldUtils.safeMultiply(longVal1_2, longVal2_2);
        long expectedLong = Long.MIN_VALUE;
        assertEquals(expectedLong, resultLong);
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 3L;
        long val2 = 4L;
        long result = FieldUtils.safeMultiply(val1, val2);
        long expected = 12L;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        long val1_2 = Long.MIN_VALUE;
        long val2_2 = Long.MIN_VALUE;
        long resultLong = FieldUtils.safeMultiply(val1_2, val2_2);
        long expectedLong = Long.MIN_VALUE;
        assertEquals(expectedLong, resultLong);
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int expected = Integer.MAX_VALUE;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3L;
        long val2 = 4L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        int expected = 12;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.weekYear();
        int value = 52;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 52);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 53;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 52);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        DateTimeField field = new DateTimeField(DateTimeFieldType.weekdayOfMonth(), 1, 2000, 1);
        int expectedValue = 7;
        try {
            FieldUtils.verifyValueBounds(field, expectedValue, 1, 7);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, expectedValue, 1, 8);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, expectedValue, 1, 6);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int wrapValue = 3;
        int minValue = 0;
        int maxValue = 7;
        int expected = 2;
        int result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        value = 10;
        expected = 4;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        value = 13;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        value = 0;
        expected = 6;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        value = 7;
        expected = 0;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        minValue = maxValue + 1;
        try {
            FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}