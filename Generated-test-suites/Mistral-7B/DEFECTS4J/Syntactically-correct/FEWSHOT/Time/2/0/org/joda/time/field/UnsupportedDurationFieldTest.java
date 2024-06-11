package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetInstanceNull() {
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testGetInstanceUnknown() {
        DurationFieldType type = new DurationFieldType("UNKNOWN");
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UNKNOWN", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    @Test
    public void testGetValueInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 1));
    }

    @Test
    public void testGetValueAsLongInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 1));
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    @Test
    public void testGetMillisLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
    }

    @Test
    public void testGetMillisInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1));
    }

    @Test
    public void testGetMillisLongInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 1));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1));
    }

    @Test
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1L));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1, 1));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1, 1));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(1, field.hashCode()); // for testing purposes
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertSame(field, field.readResolve());
    }

}