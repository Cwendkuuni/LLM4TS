package org.joda.time.test.field;

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        int a = 1;
        int b = 2;
        int sum = FieldUtils.safeAdd(a, b);
        assertEquals(sum, a + b);
    }

    @org.junit.Test
    public void testSafeAddLong() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        long a = 1L;
        long b = 2L;
        long sum = FieldUtils.safeAdd(a, b);
        assertEquals(sum, a + b);
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = Long.MAX_VALUE;
        long val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        long a = 1L;
        long b = 2L;
        long diff = FieldUtils.safeSubtract(a, b);
        assertEquals(diff, a - b);
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        int a = 1;
        int b = 2;
        int product = FieldUtils.safeMultiply(a, b);
        assertEquals(product, a * b);
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        int val1 = 1;
        long val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        int a = 1;
        long b = 2;
        long product = FieldUtils.safeMultiply(a, b);
        assertEquals(product, a * b);
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 1L;
        long val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        long a = 1L;
        long b = 2L;
        long product = FieldUtils.safeMultiply(a, b);
        assertEquals(product, a * b);
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE + 1;
        try {
            FieldUtils.safeToInt((int) value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        long value1 = Integer.MIN_VALUE;
        int intValue = FieldUtils.safeToInt((int) value1);
        assertEquals(intValue, value1);

        long value2 = Integer.MAX_VALUE;
        int intValue2 = FieldUtils.safeToInt((int) value2);
        assertEquals(intValue2, value2);
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = Integer.MIN_VALUE;
        long val2 = 2;
        try {
            FieldUtils.safeMultiplyToInt((int) val1, (int) val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        int a = 1;
        int b = 2;
        int product = FieldUtils.safeMultiplyToInt(a, b);
        assertEquals(product, a * b);
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test", 1, 31);
        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 31);
            // expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            FieldUtils.verifyValueBounds(fieldType, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1, 1, 7);
            // expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;
        int value = 6;
        int wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, minValue);

        value = 1;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, value);

        value = 5;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, value);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, minValue);

        value = -5;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, maxValue);

        value = -6;
        wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(wrappedValue, maxValue);
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        Object obj3 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj3));

        Object obj4 = null;
        assertFalse(FieldUtils.equals(obj1, obj4));
    }
}