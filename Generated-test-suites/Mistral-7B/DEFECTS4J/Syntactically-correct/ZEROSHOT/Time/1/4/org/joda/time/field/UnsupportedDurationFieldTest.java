package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
        assertEquals("hours", unsupported.getName());
        assertFalse(unsupported.isSupported());
        assertTrue(unsupported.isPrecise());
        assertNull(unsupported.getValue(1));
        assertNull(unsupported.getValueAsLong(1));
        assertNull(unsupported.getValue(1, 1));
        assertNull(unsupported.getValueAsLong(1, 1));
        assertNull(unsupported.getMillis(1));
        assertNull(unsupported.getMillis(1L));
        assertNull(unsupported.getMillis(1, 1));
        assertNull(unsupported.getMillis(1L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(1, 1));
        assertEquals(0, unsupported.getUnitMillis());
        assertFalse(unsupported.compareTo(new DurationField(){
            @Override
            public DurationFieldType getType() {
                return DurationFieldType.MINUTES();
            }
        }));
    }

    @Test
    public void testSingleton() {
        DurationFieldType type1 = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        DurationFieldType type2 = DurationFieldType.MINUTES();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[hours]", unsupported.toString());
    }

}