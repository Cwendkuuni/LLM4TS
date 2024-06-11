package org.joda.time.field.util; // adjust the package name if necessary

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals("Negated value should be correct", -5, negatedValue);

        value = -5;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals("Negated value should be correct for negative values", 5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Negating Integer.MIN_VALUE should throw an ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals("Sum should be correct", 8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        int expectedMinSum = Integer.MIN_VALUE + 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals("Sum should be correct when one value is MIN_VALUE", expectedMinSum, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Adding two MIN_VALUE should throw an ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals("Difference should be correct", 7L, difference);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        long expectedMinDifference = Long.MIN_VALUE + 1L;
        difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals("Difference should be correct when one value is MIN_VALUE", expectedMinDifference, difference);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            difference = FieldUtils.safeSubtract(val1, val2);
            fail("Subtracting two MIN_VALUE should throw an ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product should be correct", 15, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Multiplying MIN_VALUE by 2 should throw an ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method with int and long operands.
     */
    @Test
    public void testSafeMultiplyIntLong() {
        int val1 = 5;
        long val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product should be correct", 15L, product);
    }

    /**
     * Test for safeMultiply method with long and long operands.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product should be correct", 15L * 5L, product);
    }

    /**
     * Test for safeToInt method.
     */
    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals("Casting long to int should be correct", Integer.MAX_VALUE, intValue);

        value = Long.MIN_VALUE;
        try {
            intValue = FieldUtils.safeToInt(value);
            fail("Casting long to int should throw an ArithmeticException for MIN_VALUE");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals("Product should be correct", 15, product);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Multiplying MIN_VALUE by 2 should throw an ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for verifyValueBounds method.
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "weekday");
        int value = 2;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Value should be out of bounds");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 7;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Value should be out of bounds (wrong direction)");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test for equals method.
     */
    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse("Objects should not be equal by default", FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue("Objects should be equal when they are the same instance", FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse("Objects should not be equal when one is null", FieldUtils.equals(obj1, obj2));

        obj2 = new Object();
        assertTrue("Objects should be equal when their 'equals' methods return true", FieldUtils.equals(obj1, obj2) && obj1.equals(obj2));
    }
}