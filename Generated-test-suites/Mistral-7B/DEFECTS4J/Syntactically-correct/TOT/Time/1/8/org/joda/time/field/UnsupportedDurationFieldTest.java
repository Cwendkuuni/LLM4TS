package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests the getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
        assertEquals("Instants in seconds", field.getName());
        assertEquals(0, field.getUnitMillis());
        assertEquals(-1, field.compareTo(DurationField.SECONDS));
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    /**
     * Tests the getValue method when called with a valid duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 12345L;
        field.getValue(duration);
    }

    /**
     * Tests the getValueAsLong method when called with a valid duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 12345L;
        field.getValueAsLong(duration);
    }

    /**
     * Tests the getValue method when called with a valid duration and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 12345L;
        long instant = 123456789L;
        field.getValue(duration, instant);
    }

    /**
     * Tests the getValueAsLong method when called with a valid duration and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 12345L;
        long instant = 123456789L;
        field.getValueAsLong(duration, instant);
    }

    /**
     * Tests the getMillis method when called with a valid value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 123;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method when called with a valid long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 123L;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method when called with a valid value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 123;
        long instant = 123456789L;
        field.getMillis(value, instant);
    }

    /**
     * Tests the getMillis method when called with a valid long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 123L;
        long instant = 123456789L;
        field.getMillis(value, instant);
    }

    /**
     * Tests the add method when called with a valid instant and value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 123456789L;
        int value = 123;
        field.add(instant, value);
    }

    /**
     * Tests the add method when called with a valid instant and long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 123456789L;
        long value = 123L;
        field.add(instant, value);
    }

    /**
     * Tests the getDifference method when called with valid instant values.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 123456789L;
        long subtrahendInstant = 987654321L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests the getDifferenceAsLong method when called with valid instant values.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 123456789L;
        long subtrahendInstant = 987654321L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }
}