package org.joda.time.test.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }

        int negated = FieldUtils.safeNegate(5);
        assertEquals(-5, negated);
    }

    @Test
    public void testSafeAdd() {
        int sum = FieldUtils.safeAdd(-1, 1);
        assertEquals(0, sum);

        int sum1 = FieldUtils.safeAdd(Integer.MIN_VALUE, 1);
        assertEquals(Integer.MIN_VALUE, sum1);

        int sum2 = FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
        assertEquals(Integer.MAX_VALUE, sum2);

        int sum3 = FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE);
        try {
            sum3 = FieldUtils.safeAdd(sum3, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long diff = FieldUtils.safeSubtract(Long.MAX_VALUE, 1);
        assertEquals(Long.MAX_VALUE - 1, diff);

        long diff1 = FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
        assertEquals(Long.MIN_VALUE + 1, diff1);

        long diff2 = FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE);
        try {
            diff2 = FieldUtils.safeSubtract(diff2, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int result = FieldUtils.safeMultiply(5, 5);
        assertEquals(25, result);

        int result1 = FieldUtils.safeMultiply(-5, 5);
        assertEquals(-25, result1);

        int result2 = FieldUtils.safeMultiply(-5, -5);
        assertEquals(25, result2);

        long result3 = FieldUtils.safeMultiply(Long.MIN_VALUE, 5);
        try {
            result3 = FieldUtils.safeMultiply(result3, 5);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }

        long result4 = FieldUtils.safeMultiply(5, Long.MIN_VALUE);
        try {
            result4 = FieldUtils.safeMultiply(result4, 5);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testSafeMultiplyWithLongAndInt() {
        long result = FieldUtils.safeMultiply(5, 5L);
        assertEquals(25L, result);
    }

    @Test
    public void testSafeMultiplyWithLongAndLong() {
        long result = FieldUtils.safeMultiply(5L, 5L);
        assertEquals(25L, result);

        long result1 = FieldUtils.safeMultiply(Long.MIN_VALUE, 5L);
        try {
            result1 = FieldUtils.safeMultiply(result1, 5L);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }

        long result2 = FieldUtils.safeMultiply(5L, Long.MIN_VALUE);
        try {
            result2 = FieldUtils.safeMultiply(result2, 5L);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }

        long value1 = 10;
        int intValue = FieldUtils.safeToInt(value1);
        assertEquals(10, intValue);
    }

    @Test
    public void testSafeMultiplyToInt() {
        long multiplicand = 5;
        long multiplier = 5;
        int result = FieldUtils.safeMultiplyToInt(multiplicand, multiplier);
        assertEquals(25, result);

        long multiplicand1 = Long.MIN_VALUE;
        long multiplier1 = 5;
        try {
            result = FieldUtils.safeMultiplyToInt(multiplicand1, multiplier1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }

        long multiplicand2 = 5;
        long multiplier2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiplyToInt(multiplicand2, multiplier2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.instance(), "testField");
        int value = Integer.MIN_VALUE;
        int lowerBound = 0;
        int upperBound = 5;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }

        int value1 = 0;
        FieldUtils.verifyValueBounds(field, value1, lowerBound, upperBound);

        int value2 = 6;
        try {
            FieldUtils.verifyValueBounds(field, value2, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }

        DateTimeFieldType fieldType = DateTimeFieldType.instance();
        int value3 = Integer.MIN_VALUE;
        int lowerBound1 = 0;
        int upperBound1 = 5;
        try {
            FieldUtils.verifyValueBounds(fieldType, value3, lowerBound1, upperBound1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }

        int value4 = 0;
        FieldUtils.verifyValueBounds(fieldType, value4, lowerBound1, upperBound1);

        int value5 = 6;
        try {
            FieldUtils.verifyValueBounds(fieldType, value5, lowerBound1, upperBound1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }

        String fieldName = "testField";
        int value6 = Integer.MIN_VALUE;
        int lowerBound2 = 0;
        int upperBound2 = 5;
        try {
            FieldUtils.verifyValueBounds(fieldName, value6, lowerBound2, upperBound2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }

        int value7 = 0;
        FieldUtils.verifyValueBounds(fieldName, value7, lowerBound2, upperBound2);

        int value8 = 6;
        try {
            FieldUtils.verifyValueBounds(fieldName, value8, lowerBound2, upperBound2);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            //expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int currentValue = Integer.MIN_VALUE - 1;
        int wrapValue = 1;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        currentValue = Integer.MIN_VALUE + 1;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(maxValue, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(minValue, wrappedValue);

        currentValue = Integer.MAX_VALUE;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(maxValue, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrapValue = Integer.MIN_VALUE;
        try {
            wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        currentValue = Integer.MAX_VALUE;
        wrapValue = Integer.MAX_VALUE;
        try {
            wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(FieldUtils.equals(object1, object2));

        Object object3 = new Object();
        Object object4 = object3;
        assertTrue(FieldUtils.equals(object3, object4));

        Object object5 = null;
        assertFalse(FieldUtils.equals(object1, object5));
    }
}