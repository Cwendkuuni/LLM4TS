package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField anotherInstance = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        assertEquals(DurationFieldType.DAYS, unsupportedField.getType());
        assertEquals(unsupportedField, anotherInstance);
    }

    @Test
    public void testUnsupportedMethod() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
    }

    @Test
    public void testSingleton() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DurationFieldType.HOURS);

        assertEquals(unsupportedField1, unsupportedField2);
        assertNotEquals(unsupportedField1, unsupportedField3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals("UnsupportedDurationField[DAYS]", unsupportedField.toString());
    }

    @Test
    public void testCache() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);

        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals(DurationFieldType.DAYS, unsupportedField.getType());
    }

    @Test
    public void testgetName() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals("DAYS", unsupportedField.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertTrue(unsupportedField.isPrecise());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L));
    }

    @Test
    public void testGetValue_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getValue(1L, 1L));
    }

    @Test
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getValueAsLong(1L, 1L));
    }

    @Test
    public void testGetMillis_int() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1));
    }

    @Test
    public void testGetMillis_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L));
    }

    @Test
    public void testGetMillis_int_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1, 1L));
    }

    @Test
    public void testGetMillis_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getMillis(1L, 1L));
    }

    @Test
    public void testAdd_long_int() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1));
    }

    @Test
    public void testAdd_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.add(1L, 1L));
    }

    @Test
    public void testGetDifference_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getDifference(1L, 1L));
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        throwsException(UnsupportedOperationException.class, () -> unsupportedField.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.DAYS);

        assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
    }
}