package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field, field2);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("hours", field.getName());
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test
    public void testValue() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(0L, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0, 0));
    }

    @Test
    public void testCompareTo() {
        DurationField type1 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        DurationField type2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(0, type1.compareTo(type2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.HOURS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String name = field.getName();
        int hashCode = field.hashCode();
        assertEquals(name.hashCode(), hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[hours]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field.readResolve();
        assertEquals(field, field2);
    }

    @Test
    public void testNull() {
        assertNull(UnsupportedDurationField.getInstance(null));
    }
}