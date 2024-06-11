package org.joda.time.test.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.joda.time.test.BaseTestCase;

public class FieldUtilsTest extends BaseTestCase {

    /**
     * Test for safeNegate method.
     */
    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    /**
     * Test for safeAdd method.
     */
    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    /**
     * Test for safeSubtract method.
     */
    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    /**
     * Test for safeMultiply method.
     */
    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    /**
     * Test for safeMultiply method with long type.
     */
    @org.junit.Test
    public void testSafeMultiplyLong() {
        long val1 = 5L;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = 3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    /**
     * Test for safeMultiplyToInt method.
     */
    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, product);

        val1 = Long.MIN_VALUE;
        val2 = 3L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    /**
     * Test for verifyValueBounds method.
     */
    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", null);
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;

        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        field = new DateTimeField(DateTimeFieldType.hourOfDay(), "hourOfDay", null);
        value = 25;
        lowerBound = 0;
        upperBound = 23;

        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 24;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 26;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = -1;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = 100;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        // Test with null value
        value = null;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));
    }

    /**
     * Test for getWrappedValue method.
     */
    @org.junit.Test
    public void testGetWrappedValue() {
        int wrapValue = 5;
        int minValue = 1;
        int maxValue = 5;
        int value = 0;
        int wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(value, wrappedValue);

        value = wrapValue;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        value = minValue - 1;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(maxValue, wrappedValue);

        value = maxValue + 1;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = 0;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue));

        value = Integer.MIN_VALUE;
        minValue = 0;
        maxValue = Integer.MAX_VALUE;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        value = Integer.MAX_VALUE;
        wrapValue = Integer.MIN_VALUE;
        minValue = 0;
        maxValue = Integer.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue));
    }

    /**
     * Test for equals method.
     */
    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        assertTrue(FieldUtils.equals(obj1, obj2));
    }
}