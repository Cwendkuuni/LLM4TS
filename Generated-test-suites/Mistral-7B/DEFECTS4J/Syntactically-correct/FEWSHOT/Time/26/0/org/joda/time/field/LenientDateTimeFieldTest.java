package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;

public class LenientDateTimeFieldTest {
    @Test
    public void testLenientInstance() {
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, null);
        assertTrue(lenientField.isLenient());
        assertSame(strictField, lenientField.getWrappedField());
    }

    @Test
    public void testSet_validValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long initialValue = dateTime.getMillis();

        int value = 1;
        long newValue = lenientField.set(initialValue, value);
        assertEquals(initialValue + value * 86400000L, newValue);
    }

    @Test
    public void testSet_valueOutOfBounds() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long initialValue = dateTime.getMillis();

        int value = 32;
        long newValue = lenientField.set(initialValue, value);
        assertEquals(initialValue + value * 86400000L, newValue);
    }

    @Test
    public void testSet_valueTooLarge() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long initialValue = dateTime.getMillis();

        int value = Integer.MAX_VALUE + 1;
        long newValue = lenientField.set(initialValue, value);
        assertEquals(initialValue, newValue);
    }

    @Test
    public void testSet_invalidField() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long initialValue = dateTime.getMillis();

        assertThrows(UnsupportedOperationException.class, () -> lenientField.setValue(initialValue, "invalidField"));
    }

    @Test
    public void testSet_invalidChronology() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        assertThrows(IllegalArgumentException.class, () -> LenientDateTimeField.getInstance(field, null));
    }

    @Test
    public void testSet_nullField() {
        assertNull(LenientDateTimeField.getInstance(null, null));
    }

    @Test
    public void testSet_nullChronology() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        assertNull(LenientDateTimeField.getInstance(field, null));
    }

    @Test
    public void testSet_nullInstant() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = ZoneUtils.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        assertThrows(NullPointerException.class, () -> lenientField.set(null, 1));
    }

    @Test
    public void testSet_nullValue() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = ZoneUtils.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long instant = dateTime.getMillis();

        assertThrows(NullPointerException.class, () -> lenientField.set(instant, null));
    }

    @Test
    public void testSet_get() {
        DateTimeField field = new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 1);
        Chronology chronology = ZoneUtils.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);

        MutableDateTime dateTime = new MutableDateTime();
        long instant = dateTime.getMillis();

        int value = 5;
        long newInstant = lenientField.set(instant, value);
        assertEquals(value, lenientField.get(newInstant));
    }
}