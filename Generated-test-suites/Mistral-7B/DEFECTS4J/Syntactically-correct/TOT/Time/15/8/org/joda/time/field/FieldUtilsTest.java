package org.joda.time.util.test;

import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negated = FieldUtils.safeNegate(value);
        assertEquals(-5, negated);

        value = -5;
        try {
            negated = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Integer.MIN_VALUE;
        try {
            negated = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int a = 3, b = 4, sum;

        sum = FieldUtils.safeAdd(a, b);
        assertEquals(7, sum);

        a = Integer.MIN_VALUE;
        b = 5;
        try {
            sum = FieldUtils.safeAdd(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 5;
        b = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long a = 10L, b = 3L, difference;

        difference = FieldUtils.safeSubtract(a, b);
        assertEquals(7L, difference);

        a = -10L;
        b = 3L;
        try {
            difference = FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 10L;
        b = -3L;
        try {
            difference = FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = Long.MIN_VALUE;
        b = 3L;
        try {
            difference = FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 10L;
        b = Long.MIN_VALUE;
        try {
            difference = FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int a = 5, b = 2, product;

        product = FieldUtils.safeMultiply(a, b);
        assertEquals(10, product);

        a = Integer.MIN_VALUE;
        b = 5;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 5;
        b = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLong() {
        long a = 5L, b = 2L, product;

        product = FieldUtils.safeMultiply(a, b);
        assertEquals(10L, product);

        a = Long.MIN_VALUE;
        b = 5L;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 5L;
        b = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = -5L;
        b = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long a = 5L, b = 2;
        long product = FieldUtils.safeMultiply(a, b);
        assertEquals(10L, product);

        a = Long.MIN_VALUE;
        b = 5;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        a = 5;
        b = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long testValue;
        int intValue;

        testValue = 5;
        intValue = FieldUtils.safeToInt(testValue);
        assertEquals(5, intValue);

        testValue = Integer.MIN_VALUE + 1;
        try {
            intValue = FieldUtils.safeToInt(testValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        testValue = Integer.MIN_VALUE;
        try {
            intValue = FieldUtils.safeToInt(testValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.millis(), "test");

        try {
            FieldUtils.verifyValueBounds(field, 0, 1, 1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 1);
            fail("Expected no exception");
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception");
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 2, 1);
            fail("Expected no exception");
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();

        try {
            FieldUtils.verifyValueBounds(fieldType, 0, 1, 1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 1);
            fail("Expected no exception");
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception");
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 2, 1);
            fail("Expected no exception");
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        try {
            FieldUtils.verifyValueBounds("test", 0, 1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds("test", 1, 1, 1);
            fail("Expected no exception");
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception");
        }

        try {
            FieldUtils.verifyValueBounds("test", 1, 1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds("test", 1, 2, 1);
            fail("Expected no exception");
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5, minValue = 0, maxValue = 7, wrappedValue;

        wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(5, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value + 3, minValue, maxValue);
        assertEquals(2, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value - 3, minValue, maxValue);
        assertEquals(8, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value, minValue - 1, maxValue);
        assertEquals(8, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value, maxValue + 1, maxValue);
        assertEquals(0, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value, 0, minValue);
        assertEquals(7, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(value, maxValue, maxValue);
        assertEquals(5, wrappedValue);
    }
}