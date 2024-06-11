package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        assertNull(unsupportedField1.getValue(0L));
        assertNull(unsupportedField2.getValue(0L));
        assertNull(unsupportedField1.compareTo(unsupportedField2));
        assertNull(unsupportedField2.compareTo(unsupportedField1));
        assertEquals(unsupportedField1.hashCode(), unsupportedField1.hashCode());
        assertEquals(unsupportedField1.hashCode(), unsupportedField1.hashCode());
        assertFalse(unsupportedField1.equals(unsupportedField2));
        assertFalse(unsupportedField2.equals(unsupportedField1));
        UnsupportedDurationField sameInstance = UnsupportedDurationField.getInstance(type1);
        assertTrue(unsupportedField1.equals(sameInstance));
        assertTrue(sameInstance.equals(unsupportedField1));
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", unsupportedField.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_2() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getValueAsLong(1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_1() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getValue(1234567890123L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_2() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getValue(0L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_1() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getMillis(1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_2() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getMillis(0, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_3() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getMillis(1234567890123L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_1() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.add(1234567890123L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getDifference(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getDifferenceAsLong(0L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
        unsupportedField.getUnitMillis();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.HOURS;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type2);
        unsupportedField1.compareTo(unsupportedField2);
    }
}