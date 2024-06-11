package org.joda.time.field;

import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue(long duration) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong(long duration, long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis(long value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis(int value, long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis(long value, long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getMillis(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd(long instant, int value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd(long instant, long value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testgetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testcompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        String toString = field.toString();
        assertTrue(toString.contains("UnsupportedDurationField"));
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.SECONDS);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertSame(field.iType, resolved.iType);
    }
}