package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.BAR);

        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.BAR);

        int hashCode1 = field1.hashCode();
        int hashCode2 = field2.hashCode();
        int hashCode3 = field3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(DurationFieldType.FOO, field.getType());
        assertEquals("FOO", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());

        field = UnsupportedDurationField.getInstance(DurationFieldType.BAR);
        assertEquals(DurationFieldType.BAR, field.getType());
        assertEquals("BAR", field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long duration = 1L;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long duration = 1L;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_2args() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long duration = 1L;
        long instant = 2L;
        field.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        int value = 1;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_2args() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long value = 1L;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_3args() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long value = 1L;
        long instant = 2L;
        field.getMillis(value, instant);
    }

    @Test
    public void testGetMillis_3args_returnZero() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long value = 1L;
        long instant = 2L;
        assertEquals(0, field.getMillis(value, instant));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long instant = 1L;
        int value = 1;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_2args() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long instant = 1L;
        long value = 1L;
        field.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long minuendInstant = 1L;
        long subtrahendInstant = 2L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        long minuendInstant = 1L;
        long subtrahendInstant = 2L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        int comparisonResult = field1.compareTo(field2);
        assertEquals(0, comparisonResult);
    }
}