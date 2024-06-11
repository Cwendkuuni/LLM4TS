package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    /**
     * Test getting field name.
     */
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("seconds", field.getName());
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    /**
     * Test getValue method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 10L;
        field.getValue(duration);
    }

    /**
     * Test getValueAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 10L;
        field.getValueAsLong(duration);
    }

    /**
     * Test getting value with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 10L;
        long instant = 10L;
        field.getValue(duration, instant);
    }

    /**
     * Test getting value as long with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 10L;
        long instant = 10L;
        field.getValueAsLong(duration, instant);
    }

    /**
     * Test getMillis method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        field.getMillis(value);
    }

    /**
     * Test getMillis method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 10L;
        field.getMillis(value);
    }

    /**
     * Test getting millis with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 10L;
        field.getMillis(value, instant);
    }

    /**
     * Test getting millis with long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 10L;
        long instant = 10L;
        field.getMillis(value, instant);
    }

    /**
     * Test add method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 10L;
        int value = 1;
        field.add(instant, value);
    }

    /**
     * Test add method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 10L;
        long value = 10L;
        field.add(instant, value);
    }

    /**
     * Test getDifference method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 10L;
        long subtrahendInstant = 5L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getDifferenceAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 10L;
        long subtrahendInstant = 5L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.SECONDS();
        DurationFieldType type2 = DurationFieldType.MILLIS();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, field1.compareTo(field2));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS());
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNull(field1.equals(null));
        assertNull(field1.equals(field3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type.hashCode(), field.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[seconds]", field.toString());
    }

}