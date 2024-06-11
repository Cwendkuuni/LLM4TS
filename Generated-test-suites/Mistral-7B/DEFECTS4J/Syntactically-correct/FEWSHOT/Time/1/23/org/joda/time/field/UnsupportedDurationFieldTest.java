package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testCache() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField b = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(a, b);
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField b = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(a, b);
        UnsupportedDurationField c = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertNotSame(a, c);
    }

    @Test
    public void testComparison() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField b = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int result = a.compareTo(b);
        assertFalse(result >= 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValue(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAtInstantAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMilliseconds() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisecondsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisecondsAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisecondsAtInstantLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getMillis(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testZeroValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, field.getValue(0L));
        assertEquals(0L, field.getValueAsLong(0L));
        assertEquals(0L, field.getMillis(0));
        assertEquals(0L, field.getMillis(0L));
        assertEquals(0L, field.getMillis(0, 0L));
        assertEquals(0L, field.getMillis(0L, 0L));
        assertEquals(0, field.getDifference(0L, 0L));
        assertEquals(0L, field.getDifferenceAsLong(0L, 0L));
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[MILLISECONDS]", field.toString());
    }

    @Test
    public void testHashcode() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField b = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testEqualsNull() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(field.equals(null));
    }

    @Test
    public void testEqualsSame() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(field.equals(field));
    }

    @Test
    public void testEqualsOtherType() {
        UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField fieldB = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(fieldA.equals(fieldB));
    }

    @Test
    public void testEqualsOther() {
        UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        Object obj = new Object();
        assertFalse(fieldA.equals(obj));
    }

    @Test
    public void testEqualsSameName() {
        UnsupportedDurationField fieldA = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField fieldB = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(fieldA, fieldB);
    }

    @Test
    public void testClone() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField clonedField = (UnsupportedDurationField) field.clone();
        assertEquals(field, clonedField);
    }

    @Test
    public void testHashCodeName() {
        UnsupportedDurationField a = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField b = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(a.hashCode(), b.hashCode());
    }
}