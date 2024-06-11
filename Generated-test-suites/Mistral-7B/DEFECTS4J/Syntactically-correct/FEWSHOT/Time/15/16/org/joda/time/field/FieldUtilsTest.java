package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    //-----------------------------------------------------------------------
    @Test
    public void testSafeNegate() {
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-2));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(1, FieldUtils.safeAdd(0, 1));
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(-1, FieldUtils.safeAdd(0, -1));
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-1, FieldUtils.safeSubtract(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(-2, FieldUtils.safeMultiply(2, -1));
        assertEquals(0, FieldUtils.safeMultiply(0, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(-8, FieldUtils.safeMultiply(-2, 2));
        assertEquals(0, FieldUtils.safeMultiply(2, 0));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(-10, FieldUtils.safeMultiply(-2, 2));
        assertEquals(0, FieldUtils.safeMultiply(2, 0));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(2, FieldUtils.safeToInt(2));
        assertEquals(-2, FieldUtils.safeToInt(-2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE - 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(12, FieldUtils.safeMultiplyToInt(3, 4));
        assertEquals(-12, FieldUtils.safeMultiplyToInt(-3, 4));
        assertEquals(0, FieldUtils.safeMultiplyToInt(3, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31, "dayOfMonth");
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 1, 31));
        FieldUtils.verifyValueBounds(field, 31, 1, 31);

        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 25, 0, 23));
        FieldUtils.verifyValueBounds(fieldType, 23, 0, 23);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds("invalidFieldName", 5, 1, 3));
        FieldUtils.verifyValueBounds("fieldName", 3, 1, 3);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 1, 1, 1));
        assertEquals(0, FieldUtils.getWrappedValue(0, 1, 1, 1));
        assertEquals(1, FieldUtils.getWrappedValue(2, 1, 1, 1));
        assertEquals(2, FieldUtils.getWrappedValue(3, 1, 1, 1));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 1, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 1, 1, 1));

        assertEquals(-1, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 1, 2, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MIN_VALUE, -1, 2, 1));
        assertEquals(2, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 2, 2, 1));
        assertEquals(3, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 3, 2, 1));
        assertEquals(1, FieldUtils.getWrappedValue(Integer.MAX_VALUE, -1, 2, 1));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 2, 2, 1));

        assertEquals(2, FieldUtils.getWrappedValue(5, 2, 3, 4));
        assertEquals(3, FieldUtils.getWrappedValue(5, 3, 3, 4));
        assertEquals(4, FieldUtils.getWrappedValue(5, 4, 3, 4));
        assertEquals(3, FieldUtils.getWrappedValue(5, 5, 3, 4));
        assertEquals(3, FieldUtils.getWrappedValue(5, 6, 3, 4));
        assertEquals(3, FieldUtils.getWrappedValue(5, 7, 3, 4));

        assertEquals(-2, FieldUtils.getWrappedValue(5, 7, 1, 2));
        assertEquals(-1, FieldUtils.getWrappedValue(5, 8, 1, 2));
        assertEquals(0, FieldUtils.getWrappedValue(5, 9, 1, 2));
        assertEquals(1, FieldUtils.getWrappedValue(5, 10, 1, 2));
        assertEquals(2, FieldUtils.getWrappedValue(5, 11, 1, 2));
        assertEquals(3, FieldUtils.getWrappedValue(5, 12, 1, 2));
        assertEquals(-3, FieldUtils.getWrappedValue(5, 13, 1, 2));
        assertEquals(-2, FieldUtils.getWrappedValue(5, 14, 1, 2));
        assertEquals(-1, FieldUtils.getWrappedValue(5, 15, 1, 2));
        assertEquals(0, FieldUtils.getWrappedValue(5, 16, 1, 2));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertTrue(FieldUtils.equals("string1", "string1"));
        assertTrue(FieldUtils.equals(new Object(), new Object()));
        assertFalse(FieldUtils.equals("string1", "string2"));
        assertFalse(FieldUtils.equals(new Object(), new Object() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        }));
    }
}