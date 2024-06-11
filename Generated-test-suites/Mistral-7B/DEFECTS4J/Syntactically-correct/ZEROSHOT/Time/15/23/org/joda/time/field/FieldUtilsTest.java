package org.joda.time.test.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int negated = FieldUtils.safeNegate(value);
        assertEquals(-5, negated);

        value = -5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(7, sum);

        val1 = 1;
        val2 = 5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, diff);

        val1 = 10L;
        val2 = 11L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12, product);

        val1 = Integer.MIN_VALUE;
        val2 = 5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = 3L;
        int val2 = 4;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12L, product);

        val1 = 3L;
        val2 = -1;
        long expected = -12L;
        long actual = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, actual);

        val1 = 0L;
        val2 = 0;
        long expected2 = 0L;
        long actual2 = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected2, actual2);
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 3L;
        long val2 = 4L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12L, product);

        val1 = 3L;
        val2 = -1L;
        long expected = -11L;
        long actual = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, actual);

        val1 = Long.MIN_VALUE;
        val2 = 5L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = 1234567890L;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(1234567890, intValue);

        value = Integer.MIN_VALUE - 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 3L;
        long val2 = 4L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(12, product);

        val1 = Long.MIN_VALUE;
        val2 = 5L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfYear(), "DOY");
        int value = 32;
        field.setValidValues(1, 31);

        FieldUtils.verifyValueBounds(field, value, 1, 31);

        assertFalse(FieldUtils.verifyValueBounds(field, value, 1, 30));
        assertFalse(FieldUtils.verifyValueBounds(field, value, 1, 32));
    }

    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 29;
        fieldType.setValidValues(1, 30);

        FieldUtils.verifyValueBounds(fieldType, value, 1, 30);

        assertFalse(FieldUtils.verifyValueBounds(fieldType, value, 1, 29));
        assertFalse(FieldUtils.verifyValueBounds(fieldType, value, 1, 31));
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "DOY";
        int value = 29;
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfYear();
        fieldType.setValidValues(1, 30);

        FieldUtils.verifyValueBounds(fieldName, value, 1, 30);

        assertFalse(FieldUtils.verifyValueBounds(fieldName, value, 1, 29));
        assertFalse(FieldUtils.verifyValueBounds(fieldName, value, 1, 31));
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 11;
        int minValue = 1;
        int maxValue = 10;
        int wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(6, wrappedValue);

        value = 1;
        wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = -1;
        wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(8, wrappedValue);

        value = Integer.MIN_VALUE - 3;
        wrappedValue = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(maxValue - 2, wrappedValue);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, 3, minValue, maxValue));
    }

    @org.junit.Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(FieldUtils.equals(object1, object2));

        object1 = "test";
        object2 = "test";
        assertTrue(FieldUtils.equals(object1, object2));

        object1 = new Object();
        object2 = null;
        assertFalse(FieldUtils.equals(object1, object2));
    }
}