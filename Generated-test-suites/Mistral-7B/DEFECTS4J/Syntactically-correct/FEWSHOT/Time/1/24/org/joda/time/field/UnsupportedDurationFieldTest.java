package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
    public void testGetName() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("DAYS", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue(long duration, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong(long duration, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, int value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, long value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1, 2);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = new UnsupportedDurationField(null);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertNull(field.readResolve());
    }

    @Test
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedOperationException exception = field.unsupported();
        assertEquals("DAYS field is unsupported", exception.getMessage());
    }
}