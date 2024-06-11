package org.joda.time.field.util; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        // Test with positive values
        int number = 5;
        int expected = -number;
        int result = FieldUtils.safeNegate(number);
        assertEquals("Negative of positive number should be correct", expected, result);

        // Test with zero
        number = 0;
        expected = number;
        result = FieldUtils.safeNegate(number);
        assertEquals("Negative of zero should be zero", expected, result);

        // Test with minimum integer value
        number = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(number);
            fail("Expected ArithmeticException when negating minimum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        // Test with maximum integer value
        number = Integer.MAX_VALUE;
        try {
            result = FieldUtils.safeNegate(number);
            fail("Expected ArithmeticException when negating maximum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    /**
     * Test for safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        // Test with positive values
        int val1 = 5;
        int val2 = 3;
        long expected = (long) val1 + (long) val2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals("Addition of positive values should be correct", expected, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException when adding to minimum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        // Test with underflow
        val1 = Integer.MAX_VALUE;
        val2 = -1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException when adding to maximum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    /**
     * Test for safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        // Test with positive values
        int val1 = 5;
        int val2 = 3;
        long expected = (long) val1 - (long) val2;
        int result = FieldUtils.safeSubtract(val1, val2);
        assertEquals("Subtraction of positive values should be correct", expected, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException when subtracting from minimum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        // Test with underflow
        val1 = Integer.MAX_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException when subtracting from maximum integer value");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    /**
     * Test for safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        // Test with positive values
        int val1 = 5;
        int val2 = 3;
        long expected = (long) val1 * (long) val2;
        long result = FieldUtils.safeMultiply((long) val1, (long) val2);
        assertEquals("Multiplication of positive values should be correct", expected, result);

        // Test with overflow for int
        val1 = Integer.MAX_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeMultiply((int) val1, (int) val2);
            fail("Expected ArithmeticException when multiplying maximum integer value with 1");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        // Test with overflow for long
        long longVal1 = Long.MAX_VALUE;
        long longVal2 = 1;
        try {
            result = FieldUtils.safeMultiply(longVal1, longVal2);
            fail("Expected ArithmeticException when multiplying maximum long value with 1");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    /**
     * Test for verifyValueBounds method.
     */
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 32;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
    }

    /**
     * Test for equals method.
     */
    @Test
    public void testEquals() {
        String object1 = "Hello";
        String object2 = "Hello";
        String object3 = "World";

        assertTrue("Objects equal should return true for equal objects", FieldUtils.equals(object1, object2));
        assertFalse("Objects not equal should return false for different objects", FieldUtils.equals(object1, object3));
        assertFalse("Objects not equal should return false for one null object", FieldUtils.equals(null, object1));
    }
}