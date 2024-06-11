package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-100, FieldUtils.safeNegate(100));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(1, FieldUtils.safeAdd(0, 1));
        assertEquals(103, FieldUtils.safeAdd(100, 3));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeAdd(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, 100000000000000L));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(99, FieldUtils.safeSubtract(100, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, 100000000000000L));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(625, FieldUtils.safeMultiply(5, 125));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(21, FieldUtils.safeMultiply(3, 7));
        assertEquals(99734720248L, FieldUtils.safeMultiply(1234567890L, 789012345L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(10, FieldUtils.safeToInt(10L));
        assertEquals(-200, FieldUtils.safeToInt(Long.MIN_VALUE + 200));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(100, FieldUtils.safeMultiplyToInt(5, 20));
        assertEquals(-30000, FieldUtils.safeMultiplyToInt(-5, 6000));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.DAY_OF_MONTH, 31, "dayOfMonth");
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 31, 32));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 31, 31));
        FieldUtils.verifyValueBounds(field, 30, 31, 31);
        FieldUtils.verifyValueBounds(field, 28, 31, 31);
        FieldUtils.verifyValueBounds(field, 31, 31, 31);

        DateTimeFieldType fieldType = DateTimeFieldType.HOUR_OF_DAY;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 23, 24));
        FieldUtils.verifyValueBounds(fieldType, 23, 23, 23);
        FieldUtils.verifyValueBounds(fieldType, 24, 23, 23);
        FieldUtils.verifyValueBounds(fieldType, 25, 23, 23);

        String fieldName = "dayOfMonth";
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, 31, 32));
        FieldUtils.verifyValueBounds(fieldName, 31, 31, 31);
        FieldUtils.verifyValueBounds(fieldName, 32, 31, 31);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(0, FieldUtils.getWrappedValue(0, 7, 0, 7));
        assertEquals(5, FieldUtils.getWrappedValue(12, 7, 0, 7));
        assertEquals(1, FieldUtils.getWrappedValue(11, 7, 0, 7));
        assertEquals(7, FieldUtils.getWrappedValue(-1, 7, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 7, 0, 7));
        assertEquals(7, FieldUtils.getWrappedValue(Integer.MIN_VALUE - 1, 7, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 7, 0, 7));
        assertEquals(7, FieldUtils.getWrappedValue(Integer.MAX_VALUE + 1, 7, 0, 7));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj1));
        assertFalse(FieldUtils.equals(obj1, obj2));
        assertTrue(FieldUtils.equals(obj1, obj3));
        assertFalse(FieldUtils.equals(obj1, null));
    }
}