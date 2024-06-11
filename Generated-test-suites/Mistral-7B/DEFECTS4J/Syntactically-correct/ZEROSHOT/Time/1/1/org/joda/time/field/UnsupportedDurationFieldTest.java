package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("instant seconds", field.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        field.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        long instant = 1L;
        field.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        long instant = 1L;
        field.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        int value = 1;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        long value = 1L;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[instant seconds]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = new UnsupportedDurationField(type);
        assertEquals(type, field.getType());
    }

}