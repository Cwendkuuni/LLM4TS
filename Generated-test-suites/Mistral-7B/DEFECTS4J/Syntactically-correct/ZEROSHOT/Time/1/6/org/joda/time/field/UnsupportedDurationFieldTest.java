package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * JUnit test file for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals("Unsupported field should be equal", unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        unsupportedField.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        unsupportedField.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        long instant = 0;
        unsupportedField.getValue(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        long instant = 0;
        unsupportedField.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        int value = 10;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 10;
        unsupportedField.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long value = 10;
        long instant = 0;
        unsupportedField.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        int value = 10;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        long value = 10;
        unsupportedField.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 10;
        unsupportedField.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 10;
        unsupportedField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType supportedFieldType = DurationFieldType.MILLISECONDS;
        DurationField supportedField = new MillisecondsField();
        DurationFieldType unsupportedFieldType = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(unsupportedFieldType);
        assertEquals("Supported field should be greater than unsupported field", 1, supportedField.compareTo(unsupportedField));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals("Unsupported fields should be equal", true, unsupportedField1.equals(unsupportedField2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String expectedName = type.getName();
        int hashCode = unsupportedField.hashCode();
        assertEquals("Unsupported field hashcode should match name hashcode", expectedName.hashCode(), hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[MILLISECONDS]";
        assertEquals("Unsupported field toString should match expected string", expected, unsupportedField.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(type);
        assertSame("UnsupportedDurationField singleton instance should be returned after readResolve", UnsupportedDurationField.getInstance(type), unsupportedField);
    }

}