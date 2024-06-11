package org.joda.time.field.util; // Assuming the package is correct

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);
    }

    @Test
    public void testSafeAdd() {
        int val1 = Integer.MIN_VALUE - 1;
        int val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MAX_VALUE, sum);
    }

    @Test
    public void testSafeSubtract() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 10;
        long diff = FieldUtils.safeSubtract(val1, 5);
        assertEquals(5, diff);

        val1 = Long.MAX_VALUE - 1;
        diff = FieldUtils.safeSubtract(val1, 1);
        assertEquals(Long.MAX_VALUE, diff);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 3;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        int result = FieldUtils.safeMultiply(val1, 3);
        assertEquals(15, result);

        val1 = Integer.MAX_VALUE;
        val2 = 3;
        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testSafeMultiplyWithLong() {
        int val1 = 5;
        long val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

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
    public void testSafeMultiplyWithLongAndNegative() {
        int val1 = Integer.MIN_VALUE;
        long val2 = -3;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndZero() {
        int val1 = Integer.MIN_VALUE;
        long val2 = 0;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndMaxValue() {
        int val1 = Integer.MAX_VALUE;
        long val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyWithLongAndMaxValueNegative() {
        int val1 = Integer.MAX_VALUE;
        long val2 = -3;
        FieldUtils.safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiplyWithLongAndMinValue() {
        long val1 = Long.MIN_VALUE;
        int val2 = 3;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndMinValueNegative() {
        long val1 = Long.MIN_VALUE;
        long val2 = -3;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndMinValueZero() {
        long val1 = Long.MIN_VALUE;
        long val2 = 0;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndMaxValueZero() {
        long val1 = Long.MAX_VALUE;
        int val2 = 0;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(30, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(fieldType, 31, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
            // Expected success
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds("test", 31, 1, 31);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}