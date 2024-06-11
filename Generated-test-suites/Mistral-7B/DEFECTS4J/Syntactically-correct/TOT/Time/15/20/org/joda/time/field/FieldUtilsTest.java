package org.joda.time.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test safeNegate method.
     */
    @org.junit.Test
    public void testSafeNegate() {
        // Test with positive value
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        // Test with minimum integer value
        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));

        // Test with zero
        value = 0;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals(0, negatedValue);
    }

    /**
     * Test safeAdd method.
     */
    @org.junit.Test
    public void testSafeAdd() {
        // Test with positive values
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        // Test with values near the minimum integer limit
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE + 1, sum);

        // Test with values near the maximum integer limit
        val1 = Integer.MAX_VALUE;
        val2 = -1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MAX_VALUE - 1, sum);

        // Test with values that cause an overflow
        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    /**
     * Test safeSubtract method.
     */
    @org.junit.Test
    public void testSafeSubtract() {
        // Test with positive values
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        // Test with values near the minimum integer limit
        val1 = Long.MIN_VALUE;
        val2 = 1;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(Long.MIN_VALUE + 1, diff);

        // Test with values near the maximum integer limit
        val1 = Long.MAX_VALUE;
        val2 = -1;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(Long.MAX_VALUE - 1, diff);

        // Test with values that cause an overflow
        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    /**
     * Test safeMultiply method.
     */
    @org.junit.Test
    public void testSafeMultiply() {
        // Test with positive values
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        // Test with values near the minimum integer limit
        val1 = Integer.MIN_VALUE;
        val2 = 2;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Integer.MIN_VALUE * 2, product);

        // Test with values near the maximum integer limit and a small multiplier
        val1 = Integer.MAX_VALUE;
        val2 = 100;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Long.MAX_VALUE, product);

        // Test with values near the maximum integer limit and a large multiplier
        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(Long.MAX_VALUE, product);

        // Test with values that cause an overflow
        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        // Test with negative values
        val1 = -5;
        val2 = 3;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15, product);

        // Test with one value as long and the other as int
        val1 = 5;
        long val3 = 3L;
        product = FieldUtils.safeMultiply(val1, val3);
        assertEquals(15L, product);
    }

    /**
     * Test safeMultiplyToInt method.
     */
    @org.junit.Test
    public void testSafeMultiplyToInt() {
        // Test with positive values
        long val1 = 10L;
        long val2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(30, product);

        // Test with values near the minimum integer limit
        long val1_min = Long.MIN_VALUE;
        long val2 = 2;
        int product = FieldUtils.safeMultiplyToInt(val1_min, val2);
        assertEquals(Long.MIN_VALUE * 2, product);

        // Test with values near the maximum integer limit
        long val1_max = Long.MAX_VALUE;
        long val2 = -1;
        int product = FieldUtils.safeMultiplyToInt(val1_max, val2);
        assertEquals(Long.MAX_VALUE - 1, product);

        // Test with values that cause an overflow
        long val1 = Long.MIN_VALUE;
        long val2 = Long.MAX_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));

        // Test with negative values
        long val1 = -5L;
        long val2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(-15, product);

        // Test with one value as long and the other as int
        int val3 = 5;
        long val4 = 3L;
        int product = FieldUtils.safeMultiplyToInt(val3, val4);
        assertEquals(15, product);
    }

    /**
     * Test verifyValueBounds method.
     */
    @org.junit.Test
    public void testVerifyValueBounds() {
        // Test with a DateTimeField
        DateTimeField testField = DateTimeFieldType.dayOfWeek();
        int value = 7; // Sunday

        FieldUtils.verifyValueBounds(testField, value, 1, 7);

        // Test with a DateTimeFieldType
        DateTimeFieldType testFieldType = DateTimeFieldType.dayOfWeek();
        int value2 = 8; // Not a valid day of the week

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(testFieldType, value2, 1, 7));

        // Test with a String
        String testName = "testName";
        int value3 = 7;

        FieldUtils.verifyValueBounds(testName, value3, 1, 7);

        int value4 = 8;

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(testName, value4, 1, 7));
    }
}