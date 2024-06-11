package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(10001, FieldUtils.safeAdd(10000, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_Value, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3, FieldUtils.safeSubtract(5, 2));
        assertEquals(-9999, FieldUtils.safeSubtract(0, 10000));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-3, FieldUtils.safeMultiply(-1, 3));
        assertEquals(10003, FieldUtils.safeMultiply(10001, 1 + 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(10003, FieldUtils.safeMultiply(10001, 1 + 2));
        assertEquals(-1, FieldUtils.safeMultiply(-1, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, 1L + Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(11, FieldUtils.safeMultiply(2, 5 + 3));
        assertEquals(-22, FieldUtils.safeMultiply(-1, 11));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, 1 + Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(11, FieldUtils.safeMultiplyToInt(2, 5 + 3));
        assertEquals(-22, FieldUtils.safeMultiplyToInt(-1, 11));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2, 1 + Long.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MILLIS_PER_MINUTE, "test", null);
        int[] testValues = {-1, Integer.MIN_VALUE, 0, 60, 120, 121, Integer.MAX_VALUE};
        for (int testValue : testValues) {
            try {
                FieldUtils.verifyValueBounds(field, testValue, 0, 60);
            } catch (IllegalFieldValueException e) {
                assertTrue(true);
            }
        }

        try {
            FieldUtils.verifyValueBounds(field, 60, 0, 60);
        } catch (IllegalFieldValueException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.MILLI_SECONDS;
        int[] testValues = {-1, Integer.MIN_VALUE, 0, 1000, 2000, 2001, Integer.MAX_VALUE};
        for (int testValue : testValues) {
            try {
                FieldUtils.verifyValueBounds(fieldType, testValue, 0, 1000);
            } catch (IllegalFieldValueException e) {
                assertTrue(true);
            }
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 1000, 0, 1000);
        } catch (IllegalFieldValueException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "test";
        int[] testValues = {-1, Integer.MIN_VALUE, 0, 60, 120, 121, Integer.MAX_VALUE};
        for (int testValue : testValues) {
            try {
                FieldUtils.verifyValueBounds(fieldName, testValue, 0, 60);
            } catch (IllegalFieldValueException e) {
                assertTrue(true);
            }
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 60, 0, 60);
        } catch (IllegalFieldValueException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 1;
        int maxValue = 5;
        for (int i = 0; i < 10; i++) {
            int value = i * 2 + 1;
            int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
            assertEquals(value % (maxValue - minValue + 1) + minValue, wrappedValue);
        }

        try {
            FieldUtils.getWrappedValue(-1, minValue, maxValue);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            FieldUtils.getWrappedValue(minValue, minValue, maxValue);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            FieldUtils.getWrappedValue(maxValue, minValue, maxValue);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertEquals(obj1, obj3);
        assertFalse(FieldUtils.equals(obj1, obj2));
        assertFalse(FieldUtils.equals(null, obj1));
        assertFalse(FieldUtils.equals(obj1, null));
    }
}