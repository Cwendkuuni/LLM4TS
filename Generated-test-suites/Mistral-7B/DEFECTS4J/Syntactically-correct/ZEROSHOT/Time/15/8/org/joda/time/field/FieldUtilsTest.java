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
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 2;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = Fieldutils.safeSubtract(val1, val2);
        assertEquals(7L, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = Fieldutils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        try {
            result = Fieldutils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply_long_int() {
        long val1 = 10L;
        int val2 = 4;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(40L, result);
    }

    @Test
    public void testSafeMultiply_long_long() {
        long val1 = 10L;
        long val2 = 4L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(40L, result);
    }

    @Test
    public void testSafeMultiply_long_long_overflow() {
        long val1 = Long.MIN_VALUE;
        long val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10;
        int result = FieldUtils.safeToInt((int) value);
        assertEquals(10, result);

        value = Integer.MIN_VALUE + 1;
        try {
            result = FieldUtils.safeToInt((int) value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10L;
        long val2 = 4L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(40, result);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31);

        try {
            FieldUtils.verifyValueBounds(field, 0, 30, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 32, 30, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;
        int value = 10;
        int result = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(2, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        minValue = maxValue;
        try {
            result = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}