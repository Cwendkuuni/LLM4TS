package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-3, FieldUtils.safeNegate(3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-3));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3, FieldUtils.safeSubtract(5, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-12, FieldUtils.safeMultiply(-2, 6));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, 2147483648));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2147483648, 1));
    }

    @Test
    public void testSafeMultiplyLongToInt() {
        assertEquals(10, FieldUtils.safeMultiply(2, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-2, 2147483648));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2147483648, -2));
    }

    @Test
    public void testSafeMultiplyLongToLong() {
        assertEquals(3600000, FieldUtils.safeMultiply(60, 60000));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, 2147483648L * -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2147483648L * -1, 1));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(10, FieldUtils.safeMultiplyToInt(2, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(-2, 2147483648));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2147483648, -2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MILLISECONDS, "test");

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, -1, 0, 1));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 2, 0, 1));
        assertEquals(1, FieldUtils.verifyValueBounds(field, 1, 0, 1));

        DateTimeFieldType fieldType = DateTimeFieldType.MILLISECONDS;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, -1, 0, 1));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 2, 0, 1));
        assertEquals(1, FieldUtils.verifyValueBounds(fieldType, 1, 0, 1));

        String fieldName = "test";
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, -1, 0, 1));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 2, 0, 1));
        assertEquals(1, FieldUtils.verifyValueBounds(fieldName, 1, 0, 1));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(4, FieldUtils.getWrappedValue(6, 2, 1, 5));
        assertEquals(3, FieldUtils.getWrappedValue(5, 2, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(0, 2, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 2, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-4, 2, 1, 5));
        assertEquals(2, FieldUtils.getWrappedValue(-3, 2, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-6, 2, 1, 5));

        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 1, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MIN_VALUE + 1, 1, 1, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 1, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE - 1, 1, 1, 1));
    }
}