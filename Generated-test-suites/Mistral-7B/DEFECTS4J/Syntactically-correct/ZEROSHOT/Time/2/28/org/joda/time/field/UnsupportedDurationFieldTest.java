package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.MINUTES();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);

        assertEquals(type, field.getType());
        assertEquals(type.getName(), field.getName());
        assertFalse(field.isSupported());
        assertTrue(field.isPrecise());
        assertNull(field.getValue(1));
        assertNull(field.getValueAsLong(1));
        assertNull(field.getValue(1, 1));
        assertNull(field.getValueAsLong(1, 1));
        assertNull(field.getMillis(1));
        assertNull(field.getMillis(1L));
        assertNull(field.getMillis(1, 1));
        assertNull(field.getMillis(1L, 1));
        assertNull(field.add(1, 1));
        assertNull(field.add(1, 1L));
        assertNull(field.getDifference(1, 1));
        assertNull(field.getDifferenceAsLong(1, 1));
        assertEquals(0, field.getUnitMillis());
        assertEquals(0, field.compareTo(null));

        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(1, 1, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(1, 1, 1));
    }

    @Test
    public void testUnsupportedDurationFieldCache() {
        DurationFieldType type = DurationFieldType.MINUTES();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);

        assertSame(field1, field2);
    }
}