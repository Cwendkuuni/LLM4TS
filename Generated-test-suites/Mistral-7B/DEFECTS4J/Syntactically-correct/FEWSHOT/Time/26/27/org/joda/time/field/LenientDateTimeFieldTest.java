package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeValidator;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertEquals(LenientDateTimeField.class, lenientField.getClass());
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSetSimple() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getType()).thenReturn(type);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, DateTimeConstants.JANUARY, 31, 23, 59, 59);
        long instant = dateTime.toInstant().getMillis();

        int value = 32;
        long result = lenientField.set(instant, value);
        LocalDateTime expectedDateTime = new LocalDateTime(2022, DateTimeConstants.FEBRUARY, 1, 23, 59, 59);
        assertEquals(expectedDateTime.toInstant().getMillis(), result);
    }

    @Test
    public void testSetWithOutOfBoundsValue() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getType()).thenReturn(type);
        when(field.isLenient()).thenReturn(false);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, DateTimeConstants.JANUARY, 31, 23, 59, 59);
        long instant = dateTime.toInstant().getMillis();

        int value = 32;
        long result = lenientField.set(instant, value);
        LocalDateTime expectedDateTime = new LocalDateTime(2022, DateTimeConstants.FEBRUARY, 1, 23, 59, 59);
        assertEquals(expectedDateTime.toInstant().getMillis(), result);

        value = -1;
        assertThrows(IllegalArgumentException.class, () -> lenientField.set(instant, value));
    }

    @Test
    public void testSetWithValidValueFromAnotherFieldType() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        DateTimeFieldTypeValidator validator = mock(DateTimeFieldTypeValidator.class);
        when(validator.isValidValue(eq(type), anyInt())).thenReturn(true);
        when(validator.isValidValue(eq(DateTimeFieldType.dayOfMonth()), anyInt())).thenReturn(false);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getType()).thenReturn(type);
        when(field.isLenient()).thenReturn(false);
        when(field.getValidator()).thenReturn(validator);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, DateTimeConstants.JANUARY, 31, 23, 59, 59);
        long instant = dateTime.toInstant().getMillis();

        int value = 60;
        long result = lenientField.set(instant, value);
        assertEquals(instant, result);
    }

    @Test
    public void testSetWithInvalidValueFromAnotherFieldType() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        DateTimeFieldTypeValidator validator = mock(DateTimeFieldTypeValidator.class);
        when(validator.isValidValue(eq(type), anyInt())).thenReturn(false);
        when(validator.isValidValue(eq(DateTimeFieldType.dayOfMonth()), anyInt())).thenReturn(false);
        DateTimeField field = mock(DateTimeField.class);
        when(field.getType()).thenReturn(type);
        when(field.isLenient()).thenReturn(false);
        when(field.getValidator()).thenReturn(validator);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        LocalDateTime dateTime = new LocalDateTime(2022, DateTimeConstants.JANUARY, 31, 23, 59, 59);
        long instant = dateTime.toInstant().getMillis();

        int value = 61;
        assertThrows(IllegalArgumentException.class, () -> lenientField.set(instant, value));
    }

    @Test
    public void testSetWithNull() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = mock(Chronology.class);
        DateTimeField field = mock(DateTimeField.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        assertNull(lenientField.set(null, 32));
    }

    @Test
    public void testGetInstanceWithNullField() {
        Chronology base = mock(Chronology.class);
        DateTimeField field = null;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNull(lenientField);
    }

    @Test
    public void testGetInstanceWithNullBase() {
        DateTimeField field = mock(DateTimeField.class);
        Chronology base = null;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNull(lenientField);
    }

    @Test
    public void testGetInstanceWithSameObject() {
        DateTimeField field = mock(DateTimeField.class);
        Chronology base = mock(Chronology.class);
        DateTimeField lenientField1 = new LenientDateTimeField(field, base);
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(field, base);
        assertNotSame(lenientField1, lenientField2);
    }
}