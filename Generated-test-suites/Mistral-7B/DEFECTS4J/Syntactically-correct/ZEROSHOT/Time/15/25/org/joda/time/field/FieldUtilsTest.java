package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;

import org.junit.Test;

/**
 * Test file for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test method for {@link FieldUtils#safeNegate(int)}.
     */
    @Test
    public void testSafeNegate() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            int negated = FieldUtils.safeNegate(i);
            int expected = -i;
            assertEquals(expected, negated);
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#safeAdd(int, int)}.
     */
    @Test
    public void testSafeAdd() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                int sum = FieldUtils.safeAdd(i, j);
                int expected = i + j;
                assertEquals(expected, sum);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#safeSubtract(long, long)}.
     */
    @Test
    public void testSafeSubtract() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            for (long j = Long.MIN_VALUE; j <= Long.MAX_VALUE; j++) {
                long difference = FieldUtils.safeSubtract(i, j);
                long expected = i - j;
                assertEquals(expected, difference);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(int, int)}.
     */
    @Test
    public void testSafeMultiply() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                int product = FieldUtils.safeMultiply(i, j);
                int expected = i * j;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(long, int)}.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            for (int j = Integer.MIN_VALUE; j <= Integer.MAX_VALUE; j++) {
                long product = FieldUtils.safeMultiply(i, j);
                long expected = i * j;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(long, long)}.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            for (long j = Long.MIN_VALUE; j <= Long.MAX_VALUE; j++) {
                long product = FieldUtils.safeMultiply(i, j);
                long expected = i * j;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#safeToInt(long)}.
     */
    @Test
    public void testSafeToInt() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            int intValue = FieldUtils.safeToInt(i);
            int expected = (int) i;
            assertEquals(expected, intValue);
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1));
    }

    /**
     * Test method for {@link FieldUtils#safeMultiplyToInt(long, long)}.
     */
    @Test
    public void testSafeMultiplyToInt() {
        for (long i = Long.MIN_VALUE; i <= Long.MAX_VALUE; i++) {
            for (long j = Long.MIN_VALUE; j <= Long.MAX_VALUE; j++) {
                int product = FieldUtils.safeMultiplyToInt(i, j);
                long longProduct = FieldUtils.safeMultiply(i, j);
                int expected = (int) longProduct;
                assertEquals(expected, product);
            }
        }
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(DateTimeField, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField dateTimeField = new DateTimeField(DateTimeFieldType.millis(), "TestField");
        int[] valuesToTest = {0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int value : valuesToTest) {
            for (int lowerBound = Integer.MIN_VALUE; lowerBound <= value; lowerBound++) {
                for (int upperBound = value + 1; upperBound <= Integer.MAX_VALUE; upperBound++) {
                    FieldUtils.verifyValueBounds(dateTimeField, value, lowerBound, upperBound);
                }
            }
        }
        // Test with invalid bounds
        int[] invalidBounds = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        DateTimeFieldType dateTimeFieldType = new DateTimeFieldType("TestType");
        for (int i = 0; i < 6; i++) {
            int lowerBound = invalidBounds[i];
            int upperBound = invalidBounds[i + 1];
            assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(dateTimeFieldType, 0, lowerBound, upperBound));
        }
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(DateTimeFieldType, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType dateTimeFieldType = new DateTimeFieldType("TestType");
        int[] valuesToTest = {0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
        String fieldName = "TestField";
        for (int value : valuesToTest) {
            for (int lowerBound = Integer.MIN_VALUE; lowerBound <= value; lowerBound++) {
                for (int upperBound = value + 1; upperBound <= Integer.MAX_VALUE; upperBound++) {
                    FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
                }
            }
        }
        // Test with invalid bounds
        int[] invalidBounds = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int i = 0; i < 6; i++) {
            int lowerBound = invalidBounds[i];
            int upperBound = invalidBounds[i + 1];
            assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(dateTimeFieldType, 0, lowerBound, upperBound));
        }
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(String, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "TestField";
        int[] valuesToTest = {0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int value : valuesToTest) {
            for (int lowerBound = Integer.MIN_VALUE; lowerBound <= value; lowerBound++) {
                for (int upperBound = value + 1; upperBound <= Integer.MAX_VALUE; upperBound++) {
                    FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
                }
            }
        }
        // Test with invalid bounds
        int[] invalidBounds = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int i = 0; i < 6; i++) {
            int lowerBound = invalidBounds[i];
            int upperBound = invalidBounds[i + 1];
            assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, lowerBound, upperBound));
        }
    }

    /**
     * Test method for {@link FieldUtils#getWrappedValue(int, int, int, int)}.
     */
    @Test
    public void testGetWrappedValue() {
        for (int i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            for (int minValue = Integer.MIN_VALUE; minValue <= Integer.MAX_VALUE; minValue++) {
                for (int maxValue = minValue + 1; maxValue <= Integer.MAX_VALUE; maxValue++) {
                    int wrappedValue = FieldUtils.getWrappedValue(i, Integer.MIN_VALUE, minValue, maxValue);
                    int expected = minValue + ((i - minValue) % (maxValue - minValue + 1));
                    assertEquals(expected, wrappedValue);
                }
            }
        }
    }

    /**
     * Test method for {@link FieldUtils#equals(Object, Object)}.
     */
    @Test
    public void testEquals() {
        Object a = new Object();
        Object b = new Object();
        Object c = a;
        Object d = new Object();

        assertTrue(FieldUtils.equals(a, a));
        assertTrue(FieldUtils.equals(a, c));
        assertFalse(FieldUtils.equals(a, b));
        assertFalse(FieldUtils.equals(a, d));
        assertFalse(FieldUtils.equals(null, a));
    }
}