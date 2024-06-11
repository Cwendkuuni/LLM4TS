package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit. Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, other);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(DurationFieldType.MILLISECONDS.getName(), unsupported.getName());
    }

    @Test
    public void testValueThrowsException() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long duration = 1L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(duration));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(duration));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(duration, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(duration, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(unsupported.getValue(duration)));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(unsupported.getValueAsLong(duration)));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(unsupported.getValue(duration), 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(unsupported.getValueAsLong(duration), 0L));
    }

    @Test
    public void testAddThrowsException() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        long instant = 1L;
        int value = 1;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, value));
        long value2 = 2L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, value2));
    }

    @Test
    public void testCompareToReturnsZero() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(unsupported1.equals(unsupported2));
        Object obj = new Object();
        assertFalse(unsupported1.equals(obj));
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertFalse(unsupported1.equals(unsupported3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        int hashCode = unsupported.hashCode();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(hashCode, unsupported2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[MILLISECONDS]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertInstanceOf(UnsupportedDurationField.class, unsupported.readResolve());
    }

}