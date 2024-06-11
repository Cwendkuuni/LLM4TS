package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(-3, FieldUtils.safeAdd(1, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(3, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-3, FieldUtils.safeMultiply(-1, 3));
        assertEquals(0, FieldUtils.safeMultiply(0, 5));
        assertEquals(10, FieldUtils.safeMultiply(2, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(-3, FieldUtils.safeMultiply(-1, 3L));
        assertEquals(0, FieldUtils.safeMultiply(0L, 5L));
        assertEquals(10L, FieldUtils.safeMultiply(2L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5L, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5L, Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(127, FieldUtils.safeToInt(127L));
        assertEquals(-128, FieldUtils.safeToInt(-128L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE - 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(-320, FieldUtils.safeMultiplyToInt(-10, 32));
        assertEquals(500, FieldUtils.safeMultiplyToInt(10, 50));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType dayOfWeek = DateTimeFieldType.dayOfWeek();
        DateTimeField dayOfWeekField = new DateTimeField(dayOfWeek, 1, DateTimeFields.WEEK_FIRST, null);

        FieldUtils.verifyValueBounds(dayOfWeekField, -1, 1, 7);
        FieldUtils.verifyValueBounds(dayOfWeekField, 0, 1, 7);
        FieldUtils.verifyValueBounds(dayOfWeekField, 1, 1, 7);
        FieldUtils.verifyValueBounds(dayOfWeekField, 7, 1, 7);
        FieldUtils.verifyValueBounds(dayOfWeekField, 8, 1, 7);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(dayOfWeekField, 9, 1, 7));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(dayOfWeekField, -1, 1, 7));

        DateTimeFieldType minuteOfHour = DateTimeFieldType.minuteOfHour();
        DateTimeField minuteOfHourField = new DateTimeField(minuteOfHour, 1, DateTimeFields.minutes(), null);

        FieldUtils.verifyValueBounds(minuteOfHourField, -1, 0, 59);
        FieldUtils.verifyValueBounds(minuteOfHourField, 0, 0, 59);
        FieldUtils.verifyValueBounds(minuteOfHourField, 59, 0, 59);
        FieldUtils.verifyValueBounds(minuteOfHourField, 60, 0, 59);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(minuteOfHourField, -1, 0, 59));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(minuteOfHourField, 60, 0, 59));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(5, FieldUtils.getWrappedValue(5, 2, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(2, 2, 0, 7));
        assertEquals(2, FieldUtils.getWrappedValue(-5, 2, 0, 7));
        assertEquals(7, FieldUtils.getWrappedValue(-2, 2, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(-8, 2, 0, 7));
        assertEquals(5, FieldUtils.getWrappedValue(-3, 2, 0, 7));
        assertEquals(2, FieldUtils.getWrappedValue(-6, 2, 0, 7));
    }

    @Test
    public void testGetWrappedValueWithNull() {
        assertNull(FieldUtils.getWrappedValue(null, 2, 0, 7));
    }
}