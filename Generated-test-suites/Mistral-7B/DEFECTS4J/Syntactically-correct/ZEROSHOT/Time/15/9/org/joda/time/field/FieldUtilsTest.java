package org.joda.time.test.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 2;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, sum);

        long lVal1 = 2;
        long lVal2 = 3;
        long sumL = FieldUtils.safeAdd(lVal1, lVal2);
        assertEquals(5, sumL);

        val1 = -2;
        val2 = 3;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(1, sum);

        lVal1 = -2;
        lVal2 = 3;
        sumL = FieldUtils.safeAdd(lVal1, lVal2);
        assertEquals(1, sumL);

        val1 = 2;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        lVal1 = 2;
        lVal2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(lVal1, lVal2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7, diff);

        val1 = 10;
        val2 = -3;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(13, diff);

        val1 = Long.MIN_VALUE;
        val2 = 3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = -3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        long lVal1 = 2;
        int lVal2 = 3;
        long resultL = FieldUtils.safeMultiply(lVal1, lVal2);
        assertEquals(6, resultL);

        long lVal1L = 2;
        long lVal2L = 3;
        long resultLL = FieldUtils.safeMultiply(lVal1L, lVal2L);
        assertEquals(6, resultLL);

        lVal1 = 2;
        long lVal2 = 3L;
        long resultLM = FieldUtils.safeMultiply(lVal1, lVal2);
        assertEquals(6, resultLM);

        long lVal1 = 2L;
        long lVal2 = 3;
        long resultML = FieldUtils.safeMultiply(lVal1, lVal2);
        assertEquals(6, resultML);

        lVal1 = 2L;
        lVal2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(lVal1, lVal2));

        int val1 = Integer.MIN_VALUE;
        int val2 = 3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 2;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(6, result);

        long val1L = Long.MIN_VALUE;
        long val2L = 3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1L, val2L));

        long val1L2 = 2;
        long val2L2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1L2, val2L2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "test");
        int value = 7;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 8;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = 0;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        field = new DateTimeField(DateTimeFieldType.hourOfDay(), "test");
        int lowerBound2 = 0;
        int upperBound2 = 23;
        value = 24;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound2, upperBound2));

        value = -1;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound2, upperBound2));

        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfHour();
        value = 63;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 0, 59));

        value = 0;
        FieldUtils.verifyValueBounds(fieldType, value, 0, 59);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 1;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = 0;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = 7;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));

        minValue = 10;
        maxValue = 5;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(FieldUtils.equals(object1, object2));

        Object object3 = new Object();
        object3 = object1;
        assertTrue(FieldUtils.equals(object1, object3));
        assertTrue(FieldUtils.equals(object3, object1));

        object1 = null;
        assertFalse(FieldUtils.equals(object2, object1));
        assertFalse(FieldUtils.equals(object1, object2));
    }
}