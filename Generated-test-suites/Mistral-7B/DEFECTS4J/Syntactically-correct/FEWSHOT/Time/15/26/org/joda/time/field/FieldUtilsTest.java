package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-123, FieldUtils.safeNegate(123));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(-11, FieldUtils.safeMultiply(2, -5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-2, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(10, FieldUtils.safeMultiply(3, 3));
        assertEquals(-20, FieldUtils.safeMultiply(-2, 10));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-2, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(100, FieldUtils.safeMultiply(10, 10));
        assertEquals(-200, FieldUtils.safeMultiply(-20, 10));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(5, FieldUtils.safeMultiplyToInt(3, 2));
        assertEquals(-5, FieldUtils.safeMultiplyToInt(2, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(-2, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        int value = 123;
        int lowerBound = 10;
        int upperBound = 20;

        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsException() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        int value = 123;
        int lowerBound = 20;
        int upperBound = 10;

        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;

        int value1 = 1;
        int wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(1, wrappedValue1);

        int value2 = 6;
        int wrappedValue2 = FieldUtils.getWrappedValue(value2, 1, minValue, maxValue);
        assertEquals(1, wrappedValue2);

        int value3 = 7;
        int wrappedValue3 = FieldUtils.getWrappedValue(value3, 1, minValue, maxValue);
        assertEquals(2, wrappedValue3);

        int value4 = -1;
        int wrappedValue4 = FieldUtils.getWrappedValue(value4, 1, minValue, maxValue);
        assertEquals(4, wrappedValue4);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;

        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj3));
        assertFalse(FieldUtils.equals(obj1, obj2));
        assertFalse(FieldUtils.equals(obj1, null));
    }
}