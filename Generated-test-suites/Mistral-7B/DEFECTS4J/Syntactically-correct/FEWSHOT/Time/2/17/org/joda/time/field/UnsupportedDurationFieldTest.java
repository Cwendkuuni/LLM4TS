package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);

        unsupported.getValue(1L);
        unsupported.getValueAsLong(1L);
        unsupported.getValue(1L, 2L);
        unsupported.getValueAsLong(1L, 2L);
        unsupported.getMillis(1);
        unsupported.getMillis(1L);
        unsupported.getMillis(1, 2L);
        unsupported.getMillis(1L, 2L);
        unsupported.add(1L, 1);
        unsupported.add(1L, 1L);
        unsupported.getDifference(1L, 2L);
        unsupported.getDifferenceAsLong(1L, 2L);
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.SECONDS;
        DurationFieldType type2 = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);

        assertEquals(unsupported1, unsupported1);
        assertNotEquals(unsupported1, unsupported2);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String name = unsupported.getName();
        int expectedHashCode = name.hashCode();
        int actualHashCode = unsupported.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}