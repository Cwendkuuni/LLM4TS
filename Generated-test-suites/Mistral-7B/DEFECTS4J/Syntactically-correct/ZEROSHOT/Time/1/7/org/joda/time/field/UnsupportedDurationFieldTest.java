package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;

import org.junit.Test;

/**
 * JUnit 4 test file for UnsupportedDurationField class
 *
 * @author [Your Name]
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 1L;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0L;
        unsupportedField.getValue(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0L;
        unsupportedField.getValueAsLong(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 1;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 1L;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0L;
        int value = 1;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0L;
        long value = 1L;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0L;
        long subtrahendInstant = 0L;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0L;
        long subtrahendInstant = 0L;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int expectedHashCode = type.getName().hashCode();
        int actualHashCode = unsupportedField.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String expectedToString = "UnsupportedDurationField[HOURS]";
        String actualToString = unsupportedField.toString();
        assertEquals(expectedToString, actualToString);
    }
}