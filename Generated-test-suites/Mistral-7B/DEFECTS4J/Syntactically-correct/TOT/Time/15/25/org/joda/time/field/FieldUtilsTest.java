package org.joda.time.field.util; // adjust the package name if needed

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test of safeNegate method, with int.
     */
    @Test
    public void testSafeNegateInt() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-10, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeNegate method, with long.
     */
    @Test
    public void testSafeNegateLong() {
        long value = 10L;
        long result = FieldUtils.safeNegate((int) value);
        assertEquals(-10, (int) result);

        value = 100L;
        result = FieldUtils.safeNegate(value);
        assertEquals(-100, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeAdd method, with int.
     */
    @Test
    public void testSafeAddInt() {
        int val1 = 5, val2 = 3, sum;

        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeAdd method, with long.
     */
    @Test
    public void testSafeAddLong() {
        long val1 = 5L, val2 = 3L, sum;

        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, sum);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeSubtract method, with long.
     */
    @Test
    public void testSafeSubtractLong() {
        long val1 = 10L, val2 = 3L, diff;

        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        val1 = 10L;
        val2 = Long.MAX_VALUE;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeMultiply method, with int.
     */
    @Test
    public void testSafeMultiplyInt() {
        int val1 = 5, val2 = 3, product;

        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeMultiply method, with long and int.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L, val2 = 3, product;

        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 3;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15L, product);
    }

    /**
     * Test of safeMultiply method, with long and long.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L, val2 = 3L, product;

        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 3L;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15L, product);

        val1 = 3L;
        val2 = Long.MIN_VALUE;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15L, product);
    }

    /**
     * Test of safeToInt method.
     */
    @Test
    public void testSafeToInt() {
        long value;
        int result;

        value = 5;
        result = FieldUtils.safeToInt(value);
        assertEquals(5, result);

        value = Long.MAX_VALUE;
        result = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L, val2 = 3L, product, result;

        product = FieldUtils.safeMultiply(val1, val2);
        result = FieldUtils.safeToInt(product);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test of verifyValueBounds with DateTimeField.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1);
        int value = 2;

        FieldUtils.verifyValueBounds(field, value, 1, 7);

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, value, 8, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test of verifyValueBounds with DateTimeFieldType.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 2;

        FieldUtils.verifyValueBounds(fieldType, value, 1, 7);

        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, value, 8, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test of verifyValueBounds with String.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";
        int value = 2;

        FieldUtils.verifyValueBounds(fieldName, value, 1, 7);

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 8, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test of getWrappedValue method, with int.
     */
    @Test
    public void testGetWrappedValueInt() {
        int value = 5, minValue = 1, maxValue = 7, wrappedValue;

        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(-1, minValue, maxValue);
        assertEquals(6, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(minValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(maxValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(minValue, maxValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(maxValue, maxValue, maxValue);
        assertEquals(minValue, wrappedValue);
    }

    /**
     * Test of getWrappedValue method, with long.
     */
    @Test
    public void testGetWrappedValueLong() {
        int minValue = 1, maxValue = 7, wrapRange = 7;
        long value = 5L, wrappedValue;

        wrappedValue = FieldUtils.getWrappedValue((int) value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = Long.MIN_VALUE;
        wrappedValue = FieldUtils.getWrappedValue(minValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(minValue, maxValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(maxValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(minValue, maxValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(maxValue, maxValue, maxValue);
        assertEquals(minValue, wrappedValue);

        wrappedValue = FieldUtils.getWrappedValue(-value, minValue, maxValue);
        assertEquals(wrapRange + minValue, wrappedValue);
    }
}