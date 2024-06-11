package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupportedField.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, unsupportedField1.compareTo(unsupportedField2));
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", unsupportedField.toString());
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedField1.hashCode(), unsupportedField2.hashCode());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupportedField1, unsupportedField2);
    }
}