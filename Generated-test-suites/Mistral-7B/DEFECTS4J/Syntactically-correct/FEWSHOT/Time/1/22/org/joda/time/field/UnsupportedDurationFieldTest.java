package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("Millis", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.add(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.add(0, 0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field3 = new UnsupportedDurationField(null);
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(51, field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[Millis]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertNotSame(field, field.readResolve());
    }
}