package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(Integer.MAX_VALUE + 1, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAddLong() {
        assertEquals(3L, FieldUtils.safeAdd(1L, 2L));
        assertEquals(Long.MAX_VALUE + 1L, FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-2147483647, FieldUtils.safeSubtract(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(-1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(5L, FieldUtils.safeMultiply(2, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(9L, FieldUtils.safeMultiply(3L, 3L));
        assertEquals(-9L, FieldUtils.safeMultiply(3L, -3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(10, FieldUtils.safeMultiplyToInt(5, 2));
        assertEquals(-15, FieldUtils.safeMultiplyToInt(5, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(5, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 2022, 1) {
            @Override
            public int getMinimumValue() {
                return 1;
            }

            @Override
            public int getMaximumValue() {
                return 31;
            }
        };

        FieldUtils.verifyValueBounds(field, 1, 1, 31);
        FieldUtils.verifyValueBounds(field, 31, 1, 31);
        FieldUtils.verifyValueBounds(field, 32, 1, 31);
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(fieldType, 1, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
    }

    @Test
    public void testVerifyValueBoundsString() {
        FieldUtils.verifyValueBounds("dayOfMonth", 1, 1, 31);
        FieldUtils.verifyValueBounds("dayOfMonth", 31, 1, 31);
        FieldUtils.verifyValueBounds("dayOfMonth", 32, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 1, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(1, 0, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(6, 1, 1, 5));
        assertEquals(4, FieldUtils.getWrappedValue(6, 1, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(6, 5, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-1, 1, 1, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 4, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-5, 1, 1, 5));
        assertEquals(4, FieldUtils.getWrappedValue(-5, 4, 1, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-5, 5, 1, 5));
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = object1;
        assertEquals(object1, object3);
        assertFalse(FieldUtils.equals(object1, null));
        assertFalse(FieldUtils.equals(null, object1));
        assertFalse(FieldUtils.equals(object1, object2));
    }
}