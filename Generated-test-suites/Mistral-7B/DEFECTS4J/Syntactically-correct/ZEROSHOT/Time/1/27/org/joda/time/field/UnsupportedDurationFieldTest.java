package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.milliseconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetInstanceDifferentTypes() {
        DurationFieldType type1 = DurationFieldType.milliseconds();
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertNotSame(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.isSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithDuration() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithDuration() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.add(0, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        field.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.milliseconds());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.seconds());
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.milliseconds());
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.milliseconds());
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        int hashCode = field.hashCode();
        assertEquals(hashCode, field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.milliseconds());
        assertEquals("UnsupportedDurationField[milliseconds]", field.toString());
    }
}