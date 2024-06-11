package org.joda.time.field.util; // adjust the package if necessary

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-value, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1 + val2, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(val1 - val2, diff);

        val1 = Long.MIN_VALUE;
        val2 = 3L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 5, val2 = 2;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1 * val2, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L, val2 = 2L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals((int) (val1 * val2), product);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for verifyValueBounds method with DateTimeField.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = DateTimeField.dayOfWeek();
        int value = DateTimeConstants.FRIDAY;
        int lowerBound = DateTimeConstants.MONDAY;
        int upperBound = DateTimeConstants.SATURDAY;

        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    /**
     * Test for verifyValueBounds method with DateTimeFieldType.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = DateTimeConstants.FRIDAY;
        int lowerBound = DateTimeConstants.MONDAY;
        int upperBound = DateTimeConstants.SATURDAY;

        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    /**
     * Test for verifyValueBounds method with String.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";
        int value = DateTimeConstants.FRIDAY;
        int lowerBound = DateTimeConstants.MONDAY;
        int upperBound = DateTimeConstants.SATURDAY;

        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }
}