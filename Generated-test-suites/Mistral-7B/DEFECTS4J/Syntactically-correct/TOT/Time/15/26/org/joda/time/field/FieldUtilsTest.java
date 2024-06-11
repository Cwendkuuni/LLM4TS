package org.joda.time.field.util; // make sure to import all necessary classes

import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = -5;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Integer.MIN_VALUE;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, diff);

        val1 = 10;
        val2 = 15;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = 10;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = 5;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(0, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 5;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(5, intValue);

        value = Long.MAX_VALUE;
        try {
            intValue = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Long.MIN_VALUE;
        try {
            intValue = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 32, "test");

        int value = 31;
        FieldUtils.verifyValueBounds(field, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 29);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected, for non-leap year
        }

        field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 30, "test");

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected, for leap year
        }

        try {
            FieldUtils.verifyValueBounds(field, 31, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 31, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        int value = 31;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 29);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected, for non-leap year
        }

        fieldType = DateTimeFieldType.monthOfYear();

        value = 13;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 12);

        try {
            FieldUtils.verifyValueBounds(fieldType, 13, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 12, 1, 12);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";

        int value = 31;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 29);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected, for non-leap year
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 31, 1, 30);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 31, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int wrapValue = 3;
        int minValue = 0;
        int maxValue = 7;
        int wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value += wrapValue;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(2, wrappedValue);

        value -= wrapValue;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = minValue;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(0, wrappedValue);

        value = maxValue;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(7, wrappedValue);

        wrapValue = -3;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(4, wrappedValue);

        value = Integer.MIN_VALUE + 1;
        wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(Integer.MIN_VALUE + 1, wrappedValue);

        value = Integer.MIN_VALUE;
        try {
            wrappedValue = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj2 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}