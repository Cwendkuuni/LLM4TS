package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.ZoneId;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFields;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.util.FieldUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientInstance() {
        DateTimeField field = DateTimeFields.minuteOfHour();
        Chronology base = mock(Chronology.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() throws Exception {
        DateTimeFieldType type = DateTimeFieldType.millisOfDay();
        Chronology base = mock(Chronology.class);
        DateTimeField field = new BaseDateTimeField(type, null);
        DateTimeField lenientField = new LenientDateTimeField(field, base);

        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime dateTime = new LocalDateTime(123456789000L, base);
        long instant = dateTime.toInstant().toEpochMilli();

        when(base.getZone()).thenReturn(zone);

        long newValue = 99999999999L;
        long oldValue = getValue(instant, lenientField);
        long result = lenientField.set(instant, newValue);

        assertEquals(oldValue + (newValue - oldValue), getValue(instant, lenientField));
        assertEquals(instant, result);
    }

    @Test
    public void testSetNegativeValue() throws Exception {
        DateTimeFieldType type = DateTimeFieldType.millisOfDay();
        Chronology base = mock(Chronology.class);
        DateTimeField field = new BaseDateTimeField(type, null);
        DateTimeField lenientField = new LenientDateTimeField(field, base);

        long instant = 123456789000L;

        long newValue = -123456789000L;
        long oldValue = getValue(instant, lenientField);
        long result = lenientField.set(instant, newValue);

        assertEquals(oldValue + (newValue + oldValue), getValue(instant, lenientField));
        assertEquals(instant, result);
    }

    private long getValue(long instant, DateTimeField lenientField) {
        long localInstant = lenientField.iBase.getZone().convertUTCToLocal(instant);
        long value = lenientField.get(localInstant);
        return value;
    }
}