package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-10, FieldUtils.safeNegate(10));
        assertEquals(-Integer.MIN_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(13, FieldUtils.safeAdd(5, 8));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(-3, FieldUtils.safeAdd(-1, 2));
        assertEquals(-13, FieldUtils.safeAdd(-5, 8));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE - 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(2, FieldUtils.safeMultiply(3, 1));
        assertEquals(7, FieldUtils.safeMultiply(3, 2));
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertEquals(-15, FieldUtils.safeMultiply(-3, 5));
        assertEquals(-1, FieldUtils.safeMultiply(1, -1));
        assertEquals(-21, FieldUtils.safeMultiply(-3, 7));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(3L, FieldUtils.safeMultiply(5L, 0x2));
        assertEquals(105L, FieldUtils.safeMultiply(5L, 21));
        assertEquals(-15L, FieldUtils.safeMultiply(-5L, 3));
        assertEquals(-105L, FieldUtils.safeMultiply(-5L, -21));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(5L, FieldUtils.safeMultiply(3L, 2L));
        assertEquals(1225L, FieldUtils.safeMultiply(3L, 411L));
        assertEquals(-1225L, FieldUtils.safeMultiply(-3L, 411L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals(-1225L, FieldUtils.safeMultiply(-3L, -411L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(2, FieldUtils.safeToInt(2));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(1, FieldUtils.safeMultiplyToInt(1, 1));
        assertEquals(3, FieldUtils.safeMultiplyToInt(1, 2));
        assertEquals(15, FieldUtils.safeMultiplyToInt(1, 5));
        assertEquals(-15, FieldUtils.safeMultiplyToInt(-1, 5));
        assertEquals(-1, FieldUtils.safeMultiplyToInt(1, -1));
        assertEquals(-21, FieldUtils.safeMultiplyToInt(-1, 7));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", 1, 31);

        // Test with valid values
        FieldUtils.verifyValueBounds(field, 10, 1, 31);
        FieldUtils.verifyValueBounds(field, 20, 1, 31);
        FieldUtils.verifyValueBounds(field, 30, 1, 31);

        // Test with invalid values
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 123, 1, 31));

        DateTimeFieldType fieldType = DateTimeFieldType.millisOfDay();
        FieldUtils.verifyValueBounds(fieldType, 10, 0, Long.MAX_VALUE);
        FieldUtils.verifyValueBounds(fieldType, 2000, 0, Long.MAX_VALUE);
        FieldUtils.verifyValueBounds(fieldType, Long.MAX_VALUE, 0, Long.MAX_VALUE);

        // Test with invalid values
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, -1, 0, Long.MAX_VALUE));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, Long.MAX_VALUE + 1, 0, Long.MAX_VALUE));

        String fieldName = "dayOfMonth";
        FieldUtils.verifyValueBounds(fieldName, 10, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, 20, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, 30, 1, 31);

        // Test with invalid values
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 32, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 123, 1, 31));
    }

    @Test
    public void testGetWrappedValue() {
        // Test with values within range
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 7));
        assertEquals(2, FieldUtils.getWrappedValue(2, 0, 7));
        assertEquals(7, FieldUtils.getWrappedValue(7, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(0, 0, 7));

        // Test with negative values
        assertEquals(6, FieldUtils.getWrappedValue(-1, 0, 7));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 0, 7));
        assertEquals(6, FieldUtils.getWrappedValue(-7, 0, 7));

        // Test with values outside range
        assertEquals(1, FieldUtils.getWrappedValue(8, 0, 7));
        assertEquals(1, FieldUtils.getWrappedValue(-8, 0, 7));
    }

    @Test
    public void testGetWrappedValueWithMinMax() {
        // Test with values within range
        assertEquals(1, FieldUtils.getWrappedValue(1, 3, 6));
        assertEquals(2, FieldUtils.getWrappedValue(2, 3, 6));
        assertEquals(5, FieldUtils.getWrappedValue(5, 3, 6));
        assertEquals(3, FieldUtils.getWrappedValue(3, 3, 6));

        // Test with negative values
        assertEquals(4, FieldUtils.getWrappedValue(-1, 3, 6));
        assertEquals(5, FieldUtils.getWrappedValue(-2, 3, 6));
        assertEquals(4, FieldUtils.getWrappedValue(-5, 3, 6));

        // Test with values outside range
        assertEquals(3, FieldUtils.getWrappedValue(4, 3, 6));
        assertEquals(3, FieldUtils.getWrappedValue(-4, 3, 6));
        assertEquals(3, FieldUtils.getWrappedValue(7, 3, 6));
        assertEquals(3, FieldUtils.getWrappedValue(-7, 3, 6));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        Object obj4 = new Object();

        assertTrue(FieldUtils.equals(obj1, obj1));
        assertFalse(FieldUtils.equals(obj1, obj2));
        assertTrue(FieldUtils.equals(obj1, obj3));
        assertFalse(FieldUtils.equals(obj1, obj4));
        assertFalse(FieldUtils.equals(null, obj1));
    }
}