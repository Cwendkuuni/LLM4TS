package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.test.ReflectionTestUtils;
import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
        assertEquals(type, other.getType());
        assertSame(unsupported, other);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1L));
    }

    @Test
    public void testGetValueAsLong_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1L));
    }

    @Test
    public void testGetValue_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1L, 1L));
    }

    @Test
    public void testGetValueAsLong_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1L, 1L));
    }

    @Test
    public void testGetMillis_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
    }

    @Test
    public void testGetMillis_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
    }

    @Test
    public void testGetMillis_Int_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1L));
    }

    @Test
    public void testGetMillis_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 1L));
    }

    @Test
    public void testAdd_Long_Int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1));
    }

    @Test
    public void testAdd_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.add(1L, 1L));
    }

    @Test
    public void testGetDifference_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1L, 1L));
    }

    @Test
    public void testGetDifferenceAsLong_Long_Long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1L, 1L));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals_Object() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(null));
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(field.hashCode(), ReflectionTestUtils.invokeMethod(field, "hashCode", new Object[0]));
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertSame(field, ReflectionTestUtils.invokeMethod(field, "readResolve", new Object[0]));
    }

}