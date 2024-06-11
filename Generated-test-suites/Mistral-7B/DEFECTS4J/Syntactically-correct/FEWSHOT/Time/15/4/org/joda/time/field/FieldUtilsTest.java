package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(-2, FieldUtils.safeAdd(1, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(-10, FieldUtils.safeMultiply(-2, -5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(-3, FieldUtils.safeMultiply(-1, 3));
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(0, FieldUtils.safeMultiply(0, 10));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(-10, FieldUtils.safeMultiply(-2, 5));
        assertEquals(0, FieldUtils.safeMultiply(0, 10));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(1, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(0, FieldUtils.safeToInt(0));
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(4, FieldUtils.safeMultiplyToInt(2, 2));
        assertEquals(-16, FieldUtils.safeMultiplyToInt(2, -4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.instance("type");
        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 5);
        } catch (IllegalFieldValueException e) {
            assertFalse(true);
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 6, 1, 5);
            fail("Should have thrown IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds("name", 1, 1, 5);
        } catch (IllegalFieldValueException e) {
            fail("Should not have thrown IllegalFieldValueException");
        }

        try {
            FieldUtils.verifyValueBounds("name", 6, 1, 5);
            fail("Should have thrown IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 3));
        assertEquals(3, FieldUtils.getWrappedValue(3, 0, 3));
        assertEquals(0, FieldUtils.getWrappedValue(-3, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(4, 0, 3));
        assertEquals(1, FieldUtils.getWrappedValue(5, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(6, 0, 3));

        assertEquals(1, FieldUtils.getWrappedValue(1, 2, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(1, 3, 0, 3));
        assertEquals(0, FieldUtils.getWrappedValue(1, 4, 0, 3));
        assertEquals(1, FieldUtils.getWrappedValue(5, 2, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(5, 3, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(5, 4, 0, 3));
    }

    @Test
    public void testGetWrappedValueMinGreaterThanMax() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 0, 3, 2));
    }
}