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
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @org.junit.Test
    public void testSafeAdd() {
        int value1 = 5;
        int value2 = 3;
        int result = FieldUtils.safeAdd(value1, value2);
        assertEquals(8, result);

        value1 = Integer.MIN_VALUE;
        value2 = 1;
        result = FieldUtils.safeAdd(value1, value2);
        assertEquals(Integer.MIN_VALUE, result);

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(value1, value2));
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long value1 = 10;
        long value2 = 3;
        long result = FieldUtils.safeSubtract(value1, value2);
        assertEquals(7, result);

        value1 = Long.MIN_VALUE;
        value2 = 1;
        result = FieldUtils.safeSubtract(value1, value2);
        assertEquals(Long.MIN_VALUE, result);

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(value1, value2));
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int value1 = 5;
        int value2 = 3;
        int result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15, result);

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(value1, value2));
    }

    @org.junit.Test
    public void testSafeMultiplyIntLong() {
        long value1 = 5;
        int value2 = 3;
        long result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15, result);

        value1 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(value1, value2));
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long value1 = 5;
        long value2 = 3;
        long result = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15, result);

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(value1, value2));
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long value1 = 5;
        long value2 = 3;
        int result = FieldUtils.safeMultiplyToInt(value1, value2);
        assertEquals(15, result);

        value1 = Long.MIN_VALUE;
        value2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(value1, value2));
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.millis(), "test");
        int value = 10;
        FieldUtils.verifyValueBounds(field, value, 0, 10);

        value = 15;
        FieldUtils.verifyValueBounds(field, value, 0, 10);

        value = 5;
        FieldUtils.verifyValueBounds(field, value, 0, 5);

        value = 12;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 0, 5));
    }

    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        int value = 10;
        FieldUtils.verifyValueBounds(fieldType, value, 0, 10);

        value = 15;
        FieldUtils.verifyValueBounds(fieldType, value, 0, 10);

        value = 5;
        FieldUtils.verifyValueBounds(fieldType, value, 0, 5);

        value = 12;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 0, 5));
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "test";
        int value = 10;
        FieldUtils.verifyValueBounds(fieldName, value, 0, 10);

        value = 15;
        FieldUtils.verifyValueBounds(fieldName, value, 0, 10);

        value = 5;
        FieldUtils.verifyValueBounds(fieldName, value, 0, 5);

        value = 12;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 0, 5));
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 10;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value, result);

        value = Integer.MIN_VALUE;
        minValue = 0;
        maxValue = 10;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));

        value = 11;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);

        minValue = 11;
        maxValue = 15;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        minValue = 11;
        maxValue = 10;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue, result);
    }

    @org.junit.Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(FieldUtils.equals(object1, object2));

        object1 = null;
        assertFalse(FieldUtils.equals(object1, object2));

        object1 = new Object();
        assertTrue(FieldUtils.equals(object1, object1));

        object1 = new String("hello");
        object2 = new String("hello");
        assertTrue(FieldUtils.equals(object1, object2));
    }
}