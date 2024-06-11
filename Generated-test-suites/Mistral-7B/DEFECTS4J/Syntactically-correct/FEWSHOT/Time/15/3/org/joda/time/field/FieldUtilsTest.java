package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

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
        assertEquals(-3, FieldUtils.safeAdd(1, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3, FieldUtils.safeSubtract(5, 2));
        assertEquals(-3, FieldUtils.safeSubtract(5, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(5, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(-4, FieldUtils.safeMultiply(2, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyWithLong() {
        assertEquals(10, FieldUtils.safeMultiply(3, 3L));
        assertEquals(-10, FieldUtils.safeMultiply(3, -3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(3, Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyWithIntAndLong() {
        assertEquals(15, FieldUtils.safeMultiply(3, 5));
        assertEquals(-15, FieldUtils.safeMultiply(3, -5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(3, Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(127, FieldUtils.safeToInt(127L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(648, FieldUtils.safeMultiplyToInt(4, 162));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 162));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(4, Integer.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MINUTE_OF_HOUR, 0, null);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 60, 0, 59));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 61, 0, 59));
        FieldUtils.verifyValueBounds(field, 59, 0, 59);
        FieldUtils.verifyValueBounds(field, 0, 0, 59);

        DateTimeFieldType fieldType = DateTimeFieldType.MILLISECOND_OF_MINUTE;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 1001, 0, 999));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 999, 0, 999));
        FieldUtils.verifyValueBounds(fieldType, 999, 0, 999);
        FieldUtils.verifyValueBounds(fieldType, 0, 0, 999);

        String fieldName = "testField";
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 10, 0, 9));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 9, 0, 9));
        FieldUtils.verifyValueBounds(fieldName, 9, 0, 9);
        FieldUtils.verifyValueBounds(fieldName, 0, 0, 9);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 1));
        assertEquals(2, FieldUtils.getWrappedValue(3, 0, 2));
        assertEquals(0, FieldUtils.getWrappedValue(2, 1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(2, -1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(0, 1, 2));
        assertEquals(2, FieldUtils.getWrappedValue(0, -1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(4, 1, 2));
        assertEquals(3, FieldUtils.getWrappedValue(4, -1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(-3, 1, 2));
        assertEquals(0, FieldUtils.getWrappedValue(-3, -1, 2));

        assertEquals(1, FieldUtils.getWrappedValue(1, 3, 3));
        assertEquals(2, FieldUtils.getWrappedValue(4, 3, 3));
        assertEquals(3, FieldUtils.getWrappedValue(5, 3, 3));
        assertEquals(1, FieldUtils.getWrappedValue(0, 3, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-1, 3, 3));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 3, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-3, 3, 3));
        assertEquals(3, FieldUtils.getWrappedValue(-4, 3, 3));

        assertEquals(1, FieldUtils.getWrappedValue(1, 5, 5));
        assertEquals(5, FieldUtils.getWrappedValue(6, 5, 5));
        assertEquals(1, FieldUtils.getWrappedValue(0, 5, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 5, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 5, 5));
        assertEquals(4, FieldUtils.getWrappedValue(-3, 5, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-4, 5, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 5, 5));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(new Object(), new Object()));
        assertFalse(FieldUtils.equals(new Object(), null));
        assertFalse(FieldUtils.equals(null, new Object()));
        assertTrue(FieldUtils.equals("test", "test"));
        assertTrue(FieldUtils.equals(1, 1));
        assertFalse(FieldUtils.equals(1, 2));
        assertFalse(FieldUtils.equals(null, null));
    }
}