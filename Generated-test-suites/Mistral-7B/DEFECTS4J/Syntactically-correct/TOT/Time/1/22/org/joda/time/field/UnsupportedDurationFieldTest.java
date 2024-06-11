package org.joda.time.field.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test that an UnsupportedDurationField instance is created.
     */
    @Test
    public void testCreate() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(DurationFieldType.MINUTES, field.getType());
    }

    /**
     * Test that trying to get a value throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValue(1L);
    }

    /**
     * Test that trying to get a value as long throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValueAsLong(1L);
    }

    /**
     * Test that trying to get a value with an instant throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValue(1L, 2L);
    }

    /**
     * Test that trying to get a value as long with an instant throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getValueAsLong(1L, 2L);
    }

    /**
     * Test that trying to get millis throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(1);
    }

    /**
     * Test that trying to get millis as long throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(1L);
    }

    /**
     * Test that trying to get millis with an instant throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(1L, 2L);
    }

    /**
     * Test that trying to get millis as long with an instant throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAsLongWithInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getMillis(1L, 2L);
    }

    /**
     * Test that trying to add throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.add(1L, 2);
    }

    /**
     * Test that trying to add with a long throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.add(1L, 2L);
    }

    /**
     * Test that trying to get the difference throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getDifference(1L, 2L);
    }

    /**
     * Test that trying to get the difference as long throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        field.getDifferenceAsLong(1L, 2L);
    }

    /**
     * Test that getUnitMillis returns zero.
     */
    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test that compareTo returns zero.
     */
    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertEquals(0, field1.compareTo(field2));
    }

    /**
     * Test that equals returns true for same instance.
     */
    @Test
    public void testEqualsSame() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertTrue(field.equals(field));
    }

    /**
     * Test that equals returns false for different instances.
     */
    @Test
    public void testEqualsDifferent() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(field1.equals(field2));
    }

    /**
     * Test that hashCode is consistent.
     */
    @Test
    public void testHashCodeConsistent() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        int hashCode1 = field.hashCode();
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        int hashCode2 = field2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test that toString returns a descriptive string.
     */
    @Test
    public void testToString() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        String result = field.toString();
        assertTrue(result.startsWith("UnsupportedDurationField["));
    }
}