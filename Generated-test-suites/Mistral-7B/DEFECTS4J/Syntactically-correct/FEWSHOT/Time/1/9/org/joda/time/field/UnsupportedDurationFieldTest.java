package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("instantSeconds", unsupported.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_instant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_value() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_value_instant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_instant() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(0, 0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType type2 = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(unsupported1.compareTo(unsupported2) < 0);
        assertFalse(unsupported1.compareTo(unsupported2) > 0);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType type2 = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(unsupported1.equals(unsupported2));
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int hashcode = unsupported.hashCode();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(hashcode, unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[instantSeconds]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertEquals(unsupported, UnsupportedDurationField.getInstance(type));
    }

    @Test
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertSame(unsupported.getClass(), UnsupportedOperationException.class);
        assertEquals("instantSeconds field is unsupported", unsupported.getMessage());
    }
}