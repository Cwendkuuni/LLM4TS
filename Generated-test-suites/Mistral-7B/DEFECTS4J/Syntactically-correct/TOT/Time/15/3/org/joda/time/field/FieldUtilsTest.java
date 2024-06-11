package org.joda.time.field.util; // adjust the package name if necessary

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    /**
     * Test safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-value, negatedValue);

        value = 0;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
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
        int val1 = 5;
        int val2 = 3;
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
     * Test safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(val1 - val2, diff);

        val1 = 10;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method for ints.
     */
    @Test
    public void testSafeMultiplyInts() {
        int val1 = 5;
        int val2 = 3;
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
    }

    /**
     * Test safeMultiply method for longs and ints.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1 * val2, product);

        val1 = 5L;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method for longs.
     */
    @Test
    public void testSafeMultiplyLongs() {
        long val1 = 5;
        long val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1 * val2, product);

        val1 = 5L;
        val2 = 5L;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1 * val2, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
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
        long value = 123;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals((int) value, intValue);

        value = Integer.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
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
        long val1 = 5;
        long val2 = 3;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals((int) (val1 * val2), product);

        val1 = 5L;
        val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test verifyValueBounds method with DateTimeField.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", null);
        int value = 32;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = -1;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test verifyValueBounds method with DateTimeFieldType.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 32;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        value = 32;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = -1;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test verifyValueBounds method with String.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";
        int value = 32;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 32;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = -1;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }
}