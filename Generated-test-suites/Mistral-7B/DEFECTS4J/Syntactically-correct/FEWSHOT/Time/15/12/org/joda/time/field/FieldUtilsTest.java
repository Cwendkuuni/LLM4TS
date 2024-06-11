package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(1, 4));
        assertEquals(12345, FieldUtils.safeAdd(12345, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-2, 2));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(4, FieldUtils.safeSubtract(5, 1));
        assertEquals(12345, FieldUtils.safeSubtract(12345, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(-2, 2));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(3, FieldUtils.safeMultiply(2, 1.5f));
        assertEquals(-12345, FieldUtils.safeMultiply(-1, 12345));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(10L, FieldUtils.safeMultiply(5L, 2));
        assertEquals(-12345L, FieldUtils.safeMultiply(-1L, 12345L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(100L, FieldUtils.safeMultiply(5L, 20L));
        assertEquals(-123456789012345L, FieldUtils.safeMultiply(-1L, 123456789012345L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(-1, FieldUtils.safeToInt(-1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Long.MAX_VALUE));
        assertEquals(13, FieldUtils.safeToInt(13L));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(123, FieldUtils.safeMultiplyToInt(5, 24));
        assertEquals(-12345, FieldUtils.safeMultiplyToInt(-2, 6172));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MILLISECONDS, "milliseconds");
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 1, 2));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 3, 1, 2));
        assertEquals(2, FieldUtils.verifyValueBounds(field, 2, 1, 2));

        DateTimeFieldType fieldType = DateTimeFieldType.MILLISECONDS;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 1, 2));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 3, 1, 2));
        assertEquals(2, FieldUtils.verifyValueBounds(fieldType, 2, 1, 2));

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(null, 2, 1, 2));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(new DateTimeField(null, "milliseconds"), 2, 1, 2));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 1, 1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(1, 1, 2, 1));
        assertEquals(2, FieldUtils.getWrappedValue(2, 1, 1, 2));
        assertEquals(2, FieldUtils.getWrappedValue(2, 1, 2, 1));
        assertEquals(3, FieldUtils.getWrappedValue(3, 1, 1, 2));
        assertEquals(3, FieldUtils.getWrappedValue(3, 1, 2, 1));
        assertEquals(0, FieldUtils.getWrappedValue(0, 1, 1, 2));
        assertEquals(0, FieldUtils.getWrappedValue(0, 1, 2, 1));
        assertEquals(-1, FieldUtils.getWrappedValue(-1, 1, 1, 2));
        assertEquals(-1, FieldUtils.getWrappedValue(-1, 1, 2, 1));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 1, 1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 1, 2, 1));
        assertEquals(2, FieldUtils.getWrappedValue(-3, 1, 1, 2));
        assertEquals(2, FieldUtils.getWrappedValue(-3, 1, 2, 1));
        assertEquals(3, FieldUtils.getWrappedValue(-4, 1, 1, 2));
        assertEquals(3, FieldUtils.getWrappedValue(-4, 1, 2, 1));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 1, 2));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 2, 1));
        assertEquals(2, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 1, 3));
        assertEquals(2, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 3, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 2, 1));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 1, 0));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 0, 1));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertFalse(FieldUtils.equals(null, new Object()));
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "tester"));
        assertFalse(FieldUtils.equals(new Object(), new Object()));
        assertTrue(FieldUtils.equals(new Object(), new Object()));
    }
}