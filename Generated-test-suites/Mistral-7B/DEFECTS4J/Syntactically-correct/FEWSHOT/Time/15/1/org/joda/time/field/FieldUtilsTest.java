package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-1, FieldUtils.safeSubtract(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(1, FieldUtils.safeMultiply(1, 1));
        assertEquals(-1, FieldUtils.safeMultiply(1, -1));
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(-4, FieldUtils.safeMultiply(2, -2));
        assertEquals(100, FieldUtils.safeMultiply(10, 10));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_Long_Int() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals(-1L, FieldUtils.safeMultiply(1L, -1));
        assertEquals(4L, FieldUtils.safeMultiply(2L, 2));
        assertEquals(-4L, FieldUtils.safeMultiply(2L, -2));
        assertEquals(100L, FieldUtils.safeMultiply(10L, 10));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_Long_Long() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals(-1L, FieldUtils.safeMultiply(1L, -1L));
        assertEquals(4L, FieldUtils.safeMultiply(2L, 2L));
        assertEquals(-4L, FieldUtils.safeMultiply(2L, -2L));
        assertEquals(100L, FieldUtils.safeMultiply(10L, 10L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(0, FieldUtils.safeToInt(0));
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(-1, FieldUtils.safeToInt(-1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(0, FieldUtils.safeMultiplyToInt(0, 0));
        assertEquals(1, FieldUtils.safeMultiplyToInt(1, 1));
        assertEquals(-1, FieldUtils.safeMultiplyToInt(1, -1));
        assertEquals(10, FieldUtils.safeMultiplyToInt(2, 5));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.hours();
        DateTimeField field = new DateTimeField(fieldType, "hours", 0, 23);

        int[] values = {-1, 0, 24, 23, 25, Integer.MIN_VALUE, Integer.MAX_VALUE};

        for (int value : values) {
            FieldUtils.verifyValueBounds(field, value, 0, 23);
        }

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, -1, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 24, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, Integer.MIN_VALUE, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, Integer.MAX_VALUE, 0, 23));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(0, FieldUtils.getWrappedValue(0, 7, 0, 6));
        assertEquals(1, FieldUtils.getWrappedValue(1, 7, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(7, 7, 0, 6));
        assertEquals(5, FieldUtils.getWrappedValue(8, 7, 0, 6));
        assertEquals(-1, FieldUtils.getWrappedValue(-1, 7, 0, 6));
        assertEquals(5, FieldUtils.getWrappedValue(13, 7, 0, 6));
        assertEquals(-2, FieldUtils.getWrappedValue(12, 7, 0, 6));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 7, 0, 6));
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = object1;
        assertEquals(object1, object3);
        assertFalse(FieldUtils.equals(object1, null));
        assertFalse(FieldUtils.equals(object1, object2));
    }
}