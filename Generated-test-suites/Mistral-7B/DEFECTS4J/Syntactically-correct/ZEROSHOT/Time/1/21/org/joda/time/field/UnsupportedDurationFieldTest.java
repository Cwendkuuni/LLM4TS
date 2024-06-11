package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        unsupportedField.getValue(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        unsupportedField.getValueAsLong(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        unsupportedField.getMillis(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        unsupportedField.add(instant, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 1L;
        unsupportedField.add(instant, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType supportedType = DurationFieldType.DAYS;
        DurationField supportedField = new BasicDurationField(supportedType, 1);
        DurationFieldType unsupportedType = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(unsupportedType);
        assertEquals(1, supportedField.compareTo(unsupportedField));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupportedField1.equals(unsupportedField2));
        assertFalse(unsupportedField1.equals(null));
        assertFalse(unsupportedField1.equals(""));
        assertFalse(unsupportedField1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int hashCode = unsupportedField.hashCode();
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(hashCode, unsupportedField2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String toString = unsupportedField.toString();
        assertTrue(toString.contains("UnsupportedDurationField"));
        assertTrue(toString.contains("[DAYS]"));
    }
}