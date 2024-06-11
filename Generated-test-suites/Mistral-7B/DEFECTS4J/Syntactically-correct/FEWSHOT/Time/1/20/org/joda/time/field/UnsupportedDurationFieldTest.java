package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals("hours", unsupported.getName());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(DurationFieldType.HOURS, unsupported.getType());
    }

    @Test
    public void testGetValue_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1000L));
    }

    @Test
    public void testGetValueAsLong_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1000L));
    }

    @Test
    public void testGetValue_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1000L, 1));
    }

    @Test
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1000L, 1));
    }

    @Test
    public void testGetMillis_int() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    @Test
    public void testGetMillis_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1000L));
    }

    @Test
    public void testGetMillis_int_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 1));
    }

    @Test
    public void testGetMillis_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1000L, 1));
    }

    @Test
    public void testAdd_long_int() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1));
    }

    @Test
    public void testAdd_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1000L));
    }

    @Test
    public void testGetDifference_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(1, 1));
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(1, 1));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertNull(unsupported.compareTo(null));

        DurationField supported = DurationFieldType.HOURS;
        assertEquals(1, unsupported.compareTo(supported));
    }

    @Test
    public void testEquals_Object() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertFalse(unsupported.equals(null));
        assertTrue(unsupported.equals(unsupported));
        assertFalse(unsupported.equals(new Object()));

        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertTrue(unsupported.equals(other));

        DurationField supported = DurationFieldType.HOURS;
        assertFalse(unsupported.equals(supported));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(DurationFieldType.HOURS.hashCode(), unsupported.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals("UnsupportedDurationField[hours]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.HOURS);
        assertEquals(unsupported, UnsupportedDurationField.getInstance(DurationFieldType.HOURS));
    }
}