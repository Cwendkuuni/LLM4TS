package org.joda.time.field.test;

import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.field.UnsupportedDurationField.*;

/**
 * Tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests getting an instance of UnsupportedDurationField.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field = getInstance(type);
        assertEquals(type, field.getType());
        assertEquals(type.getName(), field.getName());
        assertEquals(false, field.isSupported());
    }

    /**
     * Tests getting a value from an UnsupportedDurationField.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field = getInstance(type);
        long duration = 1000L;
        field.getValue(duration);
    }

    /**
     * Tests adding a value to an UnsupportedDurationField.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field = getInstance(type);
        long instant = 1234567890123L;
        long value = 50L;
        field.add(instant, value);
    }

    /**
     * Tests getting the difference between two instants using an UnsupportedDurationField.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field = getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567880123L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests comparing two UnsupportedDurationFields.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS_30;
        DurationFieldType type2 = DurationFieldType.MINUTES_10;
        UnsupportedDurationField field1 = getInstance(type1);
        UnsupportedDurationField field2 = getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    /**
     * Tests the equals method for UnsupportedDurationField.
     */
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field1 = getInstance(type);
        UnsupportedDurationField field2 = getInstance(type);
        UnsupportedDurationField field3 = getInstance(DurationFieldType.MINUTES_10);
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    /**
     * Tests the hashcode method for UnsupportedDurationField.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field1 = getInstance(type);
        UnsupportedDurationField field2 = getInstance(type);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    /**
     * Tests the toString method for UnsupportedDurationField.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS_30;
        UnsupportedDurationField field = getInstance(type);
        String expected = "UnsupportedDurationField[HOURS_30]";
        assertEquals(expected, field.toString());
    }
}