package org.joda.time.test.field;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;

import java.util.Arrays;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        // Test int
        int value = 5;
        int negate = FieldUtils.safeNegate(value);
        Assert.assertEquals("-5", Integer.toString(negate));

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }

        // Test long
        long longValue = 10L;
        long negateLong = FieldUtils.safeNegate((int) longValue);
        Assert.assertEquals("-10", Long.toString(negateLong));

        longValue = Long.MIN_VALUE;
        try {
            FieldUtils.safeNegate(longValue);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        // Test int
        int val1 = 2;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals("5", Integer.toString(sum));

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + val1 + " + " + val2, e.getMessage());
        }

        // Test long
        long longVal1 = 3L;
        long longVal2 = 4L;
        long longSum = FieldUtils.safeAdd(longVal1, longVal2);
        Assert.assertEquals("7", Long.toString(longSum));

        longVal1 = Long.MIN_VALUE;
        longVal2 = 1L;
        try {
            FieldUtils.safeAdd(longVal1, longVal2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + longVal1 + " + " + longVal2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeSubtract() {
        // Test int
        int val1 = 5;
        int val2 = 1;
        int diff = FieldUtils.safeSubtract(val1, val2);
        Assert.assertEquals("4", Integer.toString(diff));

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + val1 + " - " + val2, e.getMessage());
        }

        // Test long
        long longVal1 = 10L;
        long longVal2 = 3L;
        long longDiff = FieldUtils.safeSubtract(longVal1, longVal2);
        Assert.assertEquals("7", Long.toString(longDiff));

        longVal1 = Long.MIN_VALUE;
        longVal2 = 1L;
        try {
            FieldUtils.safeSubtract(longVal1, longVal2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + longVal1 + " - " + longVal2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeMultiply() {
        // Test int
        int value1 = 3;
        int value2 = 4;
        int product = FieldUtils.safeMultiply(value1, value2);
        Assert.assertEquals("12", Integer.toString(product));

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: " + value1 + " * " + value2, e.getMessage());
        }

        // Test long
        long longValue1 = 5L;
        long longValue2 = 7L;
        long productLong = FieldUtils.safeMultiply(longValue1, longValue2);
        Assert.assertEquals("35", Long.toString(productLong));

        longValue1 = Long.MIN_VALUE;
        longValue2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(longValue1, longValue2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: " + longValue1 + " * " + longValue2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        // Test long
        long value1 = 5L;
        long value2 = 7L;
        int product = FieldUtils.safeMultiplyToInt(value1, value2);
        Assert.assertEquals("35", Integer.toString(product));

        value1 = Long.MIN_VALUE;
        value2 = 7L;
        try {
            FieldUtils.safeMultiplyToInt(value1, value2);
            Assert.fail();
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: " + value1 + " * " + value2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "day");

        // Test int
        int lowerBound = 1;
        int upperBound = 31;
        int value = 1;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 32 is not a valid day of month (1 to 31) for 'day'", e.getMessage());
        }

        // Test DateTimeFieldType
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        value = 7;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            Assert.assertEquals(7, value);
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        value = 8;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 8 is not a valid day of week (1 to 7)", e.getMessage());
        }

        // Test String
        String fieldName = "monthOfYear";
        value = 1;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 12);
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }

        value = 13;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 12);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 13 is not a valid month of year (1 to 12)", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int currentValue = 10;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 10;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals("5", Integer.toString(wrappedValue));

        currentValue = Integer.MIN_VALUE;
        minValue = 0;
        maxValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals("3", Integer.toString(wrappedValue));

        minValue = 11;
        maxValue = 15;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals("1", Integer.toString(wrappedValue));

        wrapValue = -2;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals("8", Integer.toString(wrappedValue));

        // Test long
        long currentLongValue = 20L;
        long wrapLongValue = 7L;
        long minLongValue = 0L;
        long maxLongValue = 20L;
        long wrappedLongValue = FieldUtils.getWrappedValue((int) currentLongValue, (int) wrapLongValue, (int) minLongValue, (int) maxLongValue);
        Assert.assertEquals("7", Long.toString(wrappedLongValue));

        currentLongValue = Long.MIN_VALUE;
        minLongValue = 0L;
        maxLongValue = 5L;
        wrappedLongValue = FieldUtils.getWrappedValue((int) currentLongValue, (int) wrapLongValue, (int) minLongValue, (int) maxLongValue);
        Assert.assertEquals("3", Long.toString(wrappedLongValue));

        minLongValue = 11L;
        maxLongValue = 15L;
        wrappedLongValue = FieldUtils.getWrappedValue((int) currentLongValue, (int) wrapLongValue, (int) minLongValue, (int) maxLongValue);
        Assert.assertEquals("1", Long.toString(wrappedLongValue));

        wrapLongValue = -2L;
        wrappedLongValue = FieldUtils.getWrappedValue((int) currentLongValue, (int) wrapLongValue, (int) minLongValue, (int) maxLongValue);
        Assert.assertEquals("18", Long.toString(wrappedLongValue));
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        Assert.assertTrue(FieldUtils.equals(obj1, obj2));

        obj2 = null;
        Assert.assertFalse(FieldUtils.equals(obj1, obj2));
    }
}