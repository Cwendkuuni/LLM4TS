package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-256, FieldUtils.safeNegate(256));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-2147483648));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(1024, FieldUtils.safeAdd(512, 512));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-511, FieldUtils.safeSubtract(512, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-15, FieldUtils.safeMultiply(-3, 5));
        assertEquals(-2401, FieldUtils.safeMultiply(-8, 300));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 5));
    }

    @Test
    public void testSafeMultiply_intAndLong() {
        assertEquals(-20, FieldUtils.safeMultiply(-2, 10));
        assertEquals(120, FieldUtils.safeMultiply(2, 60));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 5));
    }

    @Test
    public void testSafeMultiply_longAndInt() {
        assertEquals(-60, FieldUtils.safeMultiply(10, -6));
        assertEquals(-120, FieldUtils.safeMultiply(60, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_longAndLong() {
        assertEquals(-2401, FieldUtils.safeMultiply(-8, 300));
        assertEquals(-120000, FieldUtils.safeMultiply(-10, 12000));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 5));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(255, FieldUtils.safeToInt(255L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(-12, FieldUtils.safeMultiplyToInt(6, 2));
        assertEquals(1024, FieldUtils.safeMultiplyToInt(512, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.DAY_OF_MONTH, 5, null);
        int[] values = {0, 1, 2, 31, 32, Integer.MIN_VALUE, Integer.MAX_VALUE};

        for (int value : values) {
            try {
                FieldUtils.verifyValueBounds(field, value, 1, 31);
            } catch (IllegalFieldValueException ex) {
                fail("Expected no exception for value: " + value);
            }
        }

        for (int value : new int[]{-1, 32, Integer.MIN_VALUE - 1, Integer.MAX_VALUE + 1}) {
            assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 1, 31));
        }
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.HOUR_OF_DAY;
        int[] values = {-1, 0, 23, 24, Integer.MIN_VALUE, Integer.MAX_VALUE};

        for (int value : values) {
            try {
                FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
            } catch (IllegalFieldValueException ex) {
                fail("Expected no exception for value: " + value);
            }
        }

        for (int value : new int[]{-2, 24, Integer.MIN_VALUE - 1, Integer.MAX_VALUE + 1}) {
            assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 0, 23));
        }
    }

    @Test
    public void testVerifyValueBounds_String() {
        String fieldName = "testField";
        int[] values = {-1, 0, 5, 10, Integer.MIN_VALUE, Integer.MAX_VALUE};

        for (int value : values) {
            try {
                FieldUtils.verifyValueBounds(fieldName, value, 0, 5);
            } catch (IllegalFieldValueException ex) {
                fail("Expected no exception for value: " + value);
            }
        }

        for (int value : new int[]{-2, 6, Integer.MIN_VALUE - 1, Integer.MAX_VALUE + 1}) {
            assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 0, 5));
        }
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(-1, FieldUtils.getWrappedValue(1, 0, 1, 1));
        assertEquals(3, FieldUtils.getWrappedValue(4, 0, 1, 3));
        assertEquals(-2, FieldUtils.getWrappedValue(-3, 0, 2, 2));
        assertEquals(-2, FieldUtils.getWrappedValue(-2, 2, 2, 2));
        assertEquals(0, FieldUtils.getWrappedValue(-1, 3, 3, 3));
        assertEquals(1, FieldUtils.getWrappedValue(1, 3, 3, 3));
        assertEquals(2, FieldUtils.getWrappedValue(4, 3, 3, 3));

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 3, 3, 2));
    }

    @Test
    public void testEquals() {
        assertEquals(1, FieldUtils.equals(1, 1));
        assertEquals("Hello", FieldUtils.equals("Hello", "Hello"));
        assertFalse(FieldUtils.equals(1, "one"));
        assertFalse(FieldUtils.equals(null, 1));
        assertFalse(FieldUtils.equals(1, null));
    }
}