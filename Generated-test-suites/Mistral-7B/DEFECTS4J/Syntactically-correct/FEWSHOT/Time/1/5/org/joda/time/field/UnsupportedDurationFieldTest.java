package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testSingleton() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(field1, field2);
    }

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertSame(field, UnsupportedDurationField.getInstance(DurationFieldType.FOO));
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(DurationFieldType.FOO, field.getType());
    }

    @Test
    public void testgetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals("FOO", field.getName());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    @Test
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 1L));
    }

    @Test
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 1L));
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    @Test
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    @Test
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
    }

    @Test
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1L));
    }

    @Test
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 1L));
    }

    @Test
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1));
    }

    @Test
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1L));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1, 1));
    }

    @Test
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1, 1));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertThrows(UnsupportedOperationException.class, () -> field.compareTo(null));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        int hashCode = field.hashCode();
        assertEquals(hashCode, field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals("UnsupportedDurationField[FOO]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.FOO);
        assertSame(field, field.readResolve());
    }

    @Test
    public void testUnsupportedOperationException() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.FOO);
        assertEquals(DurationFieldType.FOO, field.getType());
        assertEquals("FOO", field.getName());
        assertFalse(field.isSupported());
        assertFalse(field.isPrecise());
    }

}