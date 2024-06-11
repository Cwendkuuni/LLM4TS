package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeAdd(Long.MIN_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeAdd(Long.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(4, 2));
        assertEquals(-1, FieldUtils.safeSubtract(4, 5));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(1, Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1, Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(1, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(123456L, FieldUtils.safeMultiply(2, 61728L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(1, Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1, Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(1, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(1, Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1, Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(1, Long.MIN_VALUE));
        assertEquals(4567890123456789L, FieldUtils.safeMultiply(1234567890123456L, 3725490183274679L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Long.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(4, FieldUtils.safeMultiplyToInt(2, 2));
        assertEquals(-1728, FieldUtils.safeMultiplyToInt(4, -436));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(1, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(-1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.DAY_OF_MONTH, "Day of month", null, null);
        int value = 30;

        FieldUtils.verifyValueBounds(field, value, 1, 31);

        assertFalse(FieldUtils.equals(field, null));
        FieldUtils.verifyValueBounds(field, value, 1, 31);
        FieldUtils.verifyValueBounds(field, value, 1, 30);
        FieldUtils.verifyValueBounds(field, value, 32, 31);
        FieldUtils.verifyValueBounds(field, 30, 1, 31);
        FieldUtils.verifyValueBounds(field, 31, 1, 31);
        FieldUtils.verifyValueBounds(field, -1, 1, 31);
        FieldUtils.verifyValueBounds(field, Integer.MAX_VALUE, 1, 31);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 0, 31));
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.DAY_OF_MONTH;
        int value = 30;

        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        assertFalse(FieldUtils.equals(fieldType, null));
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, value, 1, 30);
        FieldUtils.verifyValueBounds(fieldType, value, 32, 31);
        FieldUtils.verifyValueBounds(fieldType, 30, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, -1, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, Integer.MAX_VALUE, 1, 31);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 0, 31));
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "Day of month";
        int value = 30;

        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        assertFalse(FieldUtils.equals(fieldName, null));
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, value, 1, 30);
        FieldUtils.verifyValueBounds(fieldName, value, 32, 31);
        FieldUtils.verifyValueBounds(fieldName, 30, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, 31, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, -1, 1, 31);
        FieldUtils.verifyValueBounds(fieldName, Integer.MAX_VALUE, 1, 31);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 0, 31));
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 1;
        int maxValue = 10;
        int currentValue = 15;
        int wrapValue = 2;

        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        assertEquals(11, wrappedValue);
    }

    @Test
    public void testGetWrappedValueMinGreaterThanMax() {
        int minValue = 10;
        int maxValue = 1;
        int currentValue = 15;
        int wrapValue = -2;

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));
    }

    @Test
    public void testGetWrappedValueWithinBounds() {
        int minValue = 1;
        int maxValue = 10;
        int currentValue = 2;
        int wrapValue = 2;

        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        assertEquals(4, wrappedValue);
    }

    @Test
    public void testGetWrappedValueBelowMin() {
        int minValue = 1;
        int maxValue = 10;
        int currentValue = 0;
        int wrapValue = 2;

        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        assertEquals(10, wrappedValue);
    }

    @Test
    public void testGetWrappedValueAboveMax() {
        int minValue = 1;
        int maxValue = 10;
        int currentValue = 12;
        int wrapValue = 2;

        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        assertEquals(2, wrappedValue);
    }

    @Test
    public void testGetWrappedValueNegativeValues() {
        int minValue = 1;
        int maxValue = 10;
        int currentValue = -2;
        int wrapValue = 2;

        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        assertEquals(8, wrappedValue);
    }
}