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

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(4, FieldUtils.safeAdd(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(-2, FieldUtils.safeAdd(1, -2));
        assertEquals(0, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3, FieldUtils.safeSubtract(4, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MAX_VALUE, 1));
        assertEquals(2, FieldUtils.safeSubtract(4, 2));
        assertEquals(2, FieldUtils.safeSubtract(1, -1));
        assertEquals(0, FieldUtils.safeSubtract(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-16, FieldUtils.safeMultiply(-2, -8));
        assertEquals(12, FieldUtils.safeMultiply(2, 6));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
        assertEquals(484576, FieldUtils.safeMultiply(233, 2041));
    }

    @Test
    public void testSafeMultiplyLongToInt() {
        assertEquals(57646075230888320, FieldUtils.safeMultiply(1234567890L, 45678901234567890L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(1234567890, FieldUtils.safeMultiplyToInt(1234567890L, 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = DateTimeFieldType.dayOfMonth();
        assertNull(FieldUtils.verifyValueBounds(field, 31, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 1, 31));

        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        assertNull(FieldUtils.verifyValueBounds(fieldType, 23, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 24, 0, 23));
        assertNull(FieldUtils.verifyValueBounds("testField", 23, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds("testField", 24, 0, 23));

        DateTimeFieldType fieldWithNegativeBounds = DateTimeFieldType.minuteOfHour().withMinValue(60).withMaxValue(-10);
        assertNull(FieldUtils.verifyValueBounds(fieldWithNegativeBounds, 55, 0, 59));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldWithNegativeBounds, 60, 0, 59));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldWithNegativeBounds, -5, 0, 59));
    }
}