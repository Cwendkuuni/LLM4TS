package org.joda.time.test.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(7, sum);

        long lVal1 = 100L;
        long lVal2 = 200L;
        long sumLong = FieldUtils.safeAdd(lVal1, lVal2);
        assertEquals(300L, sumLong);

        lVal1 = Long.MIN_VALUE;
        lVal2 = 1L;
        try {
            FieldUtils.safeAdd(lVal1, lVal2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        lVal1 = 1L;
        lVal2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeAdd(lVal1, lVal2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 100L;
        long val2 = 50L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(50L, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 1L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12, product);

        long lVal1 = 100L;
        int lVal2 = 5;
        long productLong = FieldUtils.safeMultiply(lVal1, lVal2);
        assertEquals(500L, productLong);

        long lVal1_1 = 1L;
        long lVal2_1 = 1L;
        long productLong_1 = FieldUtils.safeMultiply(lVal1_1, lVal2_1);
        assertEquals(1L, productLong_1);

        long lVal1_2 = Long.MIN_VALUE;
        int lVal2_2 = 2;
        try {
            FieldUtils.safeMultiply(lVal1_2, lVal2_2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        long lVal1_3 = 1L;
        long lVal2_3 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(lVal1_3, lVal2_3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 100L;
        long val2 = 50L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(5000, product);

        long lVal1 = Long.MIN_VALUE;
        long lVal2 = 10L;
        try {
            FieldUtils.safeMultiplyToInt(lVal1, lVal2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyValueBoundsWithInvalidArguments() {
        DateTimeField field = null;
        int value = 1;
        int lowerBound = 2;
        int upperBound = 1;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", null);
        int value = 1;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 8;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 0;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBoundsWithStringArgument() {
        String fieldName = "dayOfWeek";
        int value = 1;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);

        value = 8;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        value = 0;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueWithInvalidArguments() {
        int currentValue = Integer.MIN_VALUE;
        int wrapValue = 1;
        int minValue = 1;
        int maxValue = 0;
        FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = 10;
        int wrapValue = 3;
        int minValue = 0;
        int maxValue = 7;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(3, wrappedValue);

        currentValue = 10;
        wrapValue = -3;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(5, wrappedValue);

        currentValue = 11;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(1, wrappedValue);
    }

    @Test
    public void testEquals() {
        String object1 = new Object() {};
        String object2 = new Object() {};
        assertFalse(FieldUtils.equals(object1, object2));

        Object object3 = new Object() {};
        assertFalse(FieldUtils.equals(object1, object3));

        assertFalse(FieldUtils.equals(null, "test"));

        String string1 = "test";
        String string2 = "test";
        assertTrue(FieldUtils.equals(string1, string2));

        String string3 = null;
        assertFalse(FieldUtils.equals(string1, string3));

        assertEquals(string1, string1);
        assertTrue(FieldUtils.equals(string1, string1));
    }
}