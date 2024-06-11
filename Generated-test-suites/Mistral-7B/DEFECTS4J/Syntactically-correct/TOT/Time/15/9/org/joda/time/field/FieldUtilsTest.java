package org.joda.time.field.util; // make sure the package name matches the given class

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, diff);

        val1 = 5;
        val2 = 10;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 10;
        val2 = Long.MIN_VALUE;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 5;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 2;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(10, product);

        val1 = Integer.MAX_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MAX_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = -2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        try {
            FieldUtils.verifyValueBounds(fieldType, 0, 1, 2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 2);
            assertEquals(1, 1); // verify that no exception was thrown
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 2, 1, 2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 1;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, wrappedValue); // value is in the range [1, 5], so the wrapped value is 3 (the same as value % (maxValue - minValue + 1) + minValue)

        value = 0;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue); // value is at the minimum, so the wrapped value is 1

        value = -1;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue, wrappedValue); // value is at the maximum, so the wrapped value is maxValue

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(2, wrappedValue); // value is slightly above the maximum, so the wrapped value is 2 (the same as (wrapRange - (value % wrapRange) % wrapRange) + minValue, where wrapRange = maxValue - minValue + 1)
    }
}