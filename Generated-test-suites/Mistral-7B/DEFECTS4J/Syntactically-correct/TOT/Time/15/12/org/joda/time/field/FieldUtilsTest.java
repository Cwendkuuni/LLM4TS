package org.joda.time.field.util; // adjust the package name if necessary

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for {@link org.joda.time.field.FieldUtils}.
 */
public class FieldUtilsTest {

    // -----------------------------------------------------------------------
    // Test safeAdd(int, int)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeAdd_int_int_smallValues() {
        int value1 = 5;
        int value2 = 3;
        int expected = 8;
        int result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSafeAdd_int_int_largePositiveValues() {
        int value1 = Integer.MAX_VALUE / 2;
        int value2 = Integer.MAX_VALUE / 2;
        int expected = Integer.MAX_VALUE;
        int result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSafeAdd_int_int_largeNegativeValues() {
        int value1 = Integer.MIN_VALUE;
        int value2 = Integer.MIN_VALUE;
        int expected = Integer.MIN_VALUE * 2;
        int result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testSafeAdd_int_int_overflow() {
        int value1 = Integer.MIN_VALUE;
        int value2 = Integer.MAX_VALUE;
        FieldUtils.safeAdd(value1, value2);
    }

    // -----------------------------------------------------------------------
    // Test safeAdd(long, long)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeAdd_long_long_smallValues() {
        long value1 = 5L;
        long value2 = 3L;
        long expected = 8L;
        long result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSafeAdd_long_long_largePositiveValues() {
        long value1 = Long.MAX_VALUE / 2;
        long value2 = Long.MAX_VALUE / 2;
        long expected = Long.MAX_VALUE;
        long result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSafeAdd_long_long_largeNegativeValues() {
        long value1 = Long.MIN_VALUE;
        long value2 = Long.MIN_VALUE;
        long expected = Long.MIN_VALUE * 2;
        long result = FieldUtils.safeAdd(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testSafeAdd_long_long_overflow() {
        long value1 = Long.MIN_VALUE;
        long value2 = Long.MAX_VALUE;
        FieldUtils.safeAdd(value1, value2);
    }

    // -----------------------------------------------------------------------
    // Test safeMultiply(int, int)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeMultiply_int_int_smallValues() {
        int value1 = 2;
        int value2 = 3;
        int expected = 6;
        int result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testSafeMultiply_int_int_overflow() {
        int value1 = Integer.MAX_VALUE;
        int value2 = Integer.MAX_VALUE;
        FieldUtils.safeMultiply(value1, value2);
    }

    // -----------------------------------------------------------------------
    // Test safeMultiply(long, int)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeMultiply_long_int_smallValues() {
        long value1 = 2L;
        int value2 = 3;
        long expected = 6L;
        long result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testSafeMultiply_long_int_overflow() {
        long value1 = Long.MAX_VALUE;
        int value2 = Integer.MAX_VALUE;
        FieldUtils.safeMultiply(value1, value2);
    }

    // -----------------------------------------------------------------------
    // Test safeMultiply(long, long)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeMultiply_long_long_smallValues() {
        long value1 = 2L;
        long value2 = 3L;
        long expected = 6L;
        long result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testSafeMultiply_long_long_largeValues() {
        long value1 = Long.MAX_VALUE / 2;
        long value2 = Long.MAX_VALUE / 2;
        long expected = Long.MAX_VALUE / 2;
        long result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testSafeMultiply_long_long_overflow() {
        long value1 = Long.MIN_VALUE;
        long value2 = Long.MIN_VALUE;
        FieldUtils.safeMultiply(value1, value2);
    }

    // -----------------------------------------------------------------------
    // Test safeToInt(long)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeToInt_long() {
        long value = Integer.MIN_VALUE + 1;
        int expected = Integer.MIN_VALUE;
        int result = FieldUtils.safeToInt(value);
        assertEquals(expected, result);

        long longMax = Long.MAX_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int resultMax = FieldUtils.safeToInt(longMax);
        assertEquals(expectedMax, resultMax);
    }

    // -----------------------------------------------------------------------
    // Test safeMultiplyToInt(long, long)
    // -----------------------------------------------------------------------

    @org.junit.Test
    public void testSafeMultiplyToInt_long_long() {
        long value1 = 10L;
        long value2 = 5L;
        int expected = 50;
        int result = FieldUtils.safeMultiplyToInt(value1, value2);
        assertEquals(expected, result);

        long longMax = Long.MAX_VALUE;
        int expectedMax = Integer.MAX_VALUE;
        int resultMax = FieldUtils.safeMultiplyToInt(longMax, 2L);
        assertEquals(expectedMax, resultMax);

        long valueNegative = Long.MIN_VALUE;
        long valueTwoNegative = Long.MIN_VALUE;
        int expectedNegative = Integer.MIN_VALUE;
        int resultNegative = FieldUtils.safeMultiplyToInt(valueNegative, valueTwoNegative);
        assertEquals(expectedNegative, resultNegative);
    }

    // -----------------------------------------------------------------------
    // Test verifyValueBounds
    // -----------------------------------------------------------------------

    @org.junit.Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_DateTimeField_int_wrongValue() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", 1);
        FieldUtils.verifyValueBounds(field, 8, DateTimeFieldType.dayOfWeek().getValues().length + 1, 7);
    }

    @org.junit.Test
    public void testVerifyValueBounds_DateTimeField_int_validValues() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", 1);
        FieldUtils.verifyValueBounds(field, 1, 7, 7);
    }

    @org.junit.Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_DateTimeFieldType_int_wrongValue() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, 8, DateTimeFieldType.dayOfWeek().getValues().length + 1, 7);
    }

    @org.junit.Test
    public void testVerifyValueBounds_DateTimeFieldType_int_validValues() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, 1, 7, 7);
    }

    @org.junit.Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_String_int_wrongValue() {
        String fieldName = "dayOfWeek";
        FieldUtils.verifyValueBounds(fieldName, 8, 7, 7);
    }

    @org.junit.Test
    public void testVerifyValueBounds_String_int_validValues() {
        String fieldName = "dayOfWeek";
        FieldUtils.verifyValueBounds(fieldName, 1, 7, 7);
    }
}