package org.joda.time.field.util; // make sure the package is correct

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    /**
     * Test safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = 0;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals(0, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int value1 = 5;
        int value2 = 3;
        int sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(8, sum);

        value1 = Integer.MIN_VALUE;
        value2 = 1;
        sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(Integer.MIN_VALUE - 1, sum);

        value1 = 5;
        value2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeAdd method for long type.
     */
    @Test
    public void testSafeAddLong() {
        long value1 = 5L;
        long value2 = 3L;
        long sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(8L, sum);

        value1 = Long.MIN_VALUE;
        value2 = 1L;
        sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(Long.MIN_VALUE - 1L, sum);

        value1 = 5L;
        value2 = Long.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        int value1 = 10;
        int value2 = 3;
        int difference = FieldUtils.safeSubtract(value1, value2);
        assertEquals(7, difference);

        value1 = Integer.MIN_VALUE;
        value2 = 1;
        try {
            difference = FieldUtils.safeSubtract(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = 5;
        value2 = Integer.MIN_VALUE;
        try {
            difference = FieldUtils.safeSubtract(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        try {
            difference = FieldUtils.safeSubtract(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method for int type.
     */
    @Test
    public void testSafeMultiplyInt() {
        int value1 = 5;
        int value2 = 3;
        int product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15, product);

        value1 = Integer.MIN_VALUE;
        value2 = 2;
        try {
            product = FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method for long type.
     */
    @Test
    public void testSafeMultiplyLong() {
        long value1 = 5L;
        int value2 = 3;
        long product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15L, product);

        value1 = 5L;
        long value3 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(value1, value3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = 2;
        try {
            product = FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value3 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(value1, value3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method for long and int types.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long value1 = 5L;
        int value2 = 3;
        long product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15L, product);

        value1 = 5L;
        int value3 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(value1, value3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long value1 = 5L;
        long value2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(value1, value2);
        assertEquals(15, product);

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiplyToInt(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeToInt method.
     */
    @Test
    public void testSafeToInt() {
        long value = 10L;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(10, intValue);

        value = Long.MIN_VALUE + 1L;
        try {
            intValue = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test verifyValueBounds method for DateTimeField.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1) {
            @Override
            public int getValue(long millis) {
                return 1;
            }
        };

        try {
            FieldUtils.verifyValueBounds(field, 2, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 7);
            // expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    /**
     * Test verifyValueBounds method for DateTimeFieldType.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        try {
            FieldUtils.verifyValueBounds(fieldType, 31, 1, 28);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 28, 1, 28);
            // expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    /**
     * Test verifyValueBounds method for String.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";

        try {
            FieldUtils.verifyValueBounds(fieldName, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 1, 1, 7);
            // expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    /**
     * Test getWrappedValue method with positive values.
     */
    @Test
    public void testGetWrappedValuePositive() {
        int value = 1;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);
    }

    /**
     * Test getWrappedValue method with negative values.
     */
    @Test
    public void testGetWrappedValueNegative() {
        int value = -1;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = -6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);
    }
}