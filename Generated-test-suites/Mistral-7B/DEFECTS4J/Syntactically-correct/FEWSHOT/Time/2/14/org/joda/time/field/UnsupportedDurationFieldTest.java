package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(type);
        assertEquals(udf1, udf2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertFalse(udf.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("MILLISECONDS", udf.getName());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(DurationFieldType.SECONDS, udf.getType());
    }

    @Test
    public void testValue() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getValue(1));
    }

    @Test
    public void testValueAsLong() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getValueAsLong(1));
    }

    @Test
    public void testValueWithInstant() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getValue(1, 0));
    }

    @Test
    public void testValueAsLongWithInstant() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertThrows(UnsupportedOperationException.class, () -> udf.getValueAsLong(1, 0));
    }

    @Test
    public void testMillis() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getMillis(1));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> udf.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> udf.add(0, 1L));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getDifference(0, 0));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> udf.getDifferenceAsLong(0, 0));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(0, udf.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, udf1.compareTo(udf2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField udf3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(udf1.equals(udf1));
        assertTrue(udf1.equals(udf2));
        assertFalse(udf1.equals(udf3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        int hashCode = udf.hashCode();
        UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DurationFieldType.YEARS);
        assertEquals(hashCode, udf2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        String toString = udf.toString();
        assertTrue(toString.startsWith("UnsupportedDurationField["));
    }

    @Test
    public void testSingletonSerialization() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES);
        assertSame(udf, udf.readResolve());
    }

    @Test
    public void testUnsupportedOperationException() {
        UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        UnsupportedOperationException e = udf.unsupported();
        assertEquals("HOURS field is unsupported", e.getMessage());
    }
}