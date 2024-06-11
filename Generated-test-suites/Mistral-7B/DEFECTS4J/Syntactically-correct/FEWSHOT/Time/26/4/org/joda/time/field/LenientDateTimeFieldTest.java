package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneId;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new LocalDateTimeField(fieldType, ISOChronology.INSTANCE);
        Chronology chronology = ISOChronology.INSTANCE;
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        assertEquals(strictField, lenientField.getWrappedField());
        assertEquals(chronology, lenientField.iBase);
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSetLenientDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new LocalDateTimeField(fieldType, ISOChronology.INSTANCE);
        Chronology chronology = ISOChronology.INSTANCE;
        LenientDateTimeField lenientField = new LenientDateTimeField(strictField, chronology);

        LocalDateTime dateTime = new LocalDateTime(2023, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant().getMillis();

        // Test setting a value within bounds
        int value = 1;
        long expectedResult = instant + FieldUtils.getMillisPerDay() * (value - getValue(getValue(instant, lenientField)));
        long result = lenientField.set(instant, value);
        assertEquals(expectedResult, result);

        // Test setting a value out of bounds but still within the same day
        value = 32;
        expectedResult = instant + FieldUtils.getMillisPerDay() * (value - getValue(getValue(instant, lenientField)));
        result = lenientField.set(instant, value);
        assertEquals(expectedResult, result);

        // Test setting a value out of bounds but in the next day
        value = -10;
        expectedResult = instant + FieldUtils.getMillisPerDay() * (value + getValue(getValue(instant, lenientField)));
        result = lenientField.set(instant, value);
        assertEquals(expectedResult, result);

        // Test setting a null value
        result = lenientField.set(instant, null);
        assertNull(result);
    }

    @Test
    public void testGetLenientDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new LocalDateTimeField(fieldType, ISOChronology.INSTANCE);
        Chronology chronology = ISOChronology.INSTANCE;
        LenientDateTimeField lenientField = new LenientDateTimeField(strictField, chronology);

        LocalDateTime dateTime = new LocalDateTime(2023, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        long instant = mutableDateTime.toInstant().getMillis();

        int value = 1;
        lenientField.set(instant, value);
        int expectedResult = value;
        int result = lenientField.get(instant);
        assertEquals(expectedResult, result);
    }

    private int getValue(long instant, DateTimeField field) {
        MutableDateTime mutableDateTime = new MutableDateTime(instant);
        return field.get(mutableDateTime.toLocalDateTime());
    }

    private int getValue(long instant, LenientDateTimeField lenientField) {
        return getValue(instant, lenientField.getWrappedField());
    }

    @Test
    public void testGetInstance() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new LocalDateTimeField(fieldType, ISOChronology.INSTANCE);
        Chronology chronology = ISOChronology.INSTANCE;

        LenientDateTimeField lenientField1 = LenientDateTimeField.getInstance(strictField, chronology);
        LenientDateTimeField lenientField2 = LenientDateTimeField.getInstance(strictField, chronology);

        assertEquals(lenientField1, lenientField2);
    }

    @Test
    public void testGetInstanceWithNullField() {
        Chronology chronology = ISOChronology.INSTANCE;
        assertNull(LenientDateTimeField.getInstance(null, chronology));
    }

    @Test
    public void testGetInstanceWithNullChronology() {
        DateTimeField strictField = mock(DateTimeField.class);
        when(strictField.isLenient()).thenReturn(false);
        assertNull(LenientDateTimeField.getInstance(strictField, null));
    }

    @Test
    public void testGetInstanceWithLenientField() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        DateTimeField strictField = new LocalDateTimeField(fieldType, ISOChronology.INSTANCE);
        Chronology chronology = ISOChronology.INSTANCE;

        LenientDateTimeField lenientField = (LenientDateTimeField) strictField;
        LenientDateTimeField lenientResult = LenientDateTimeField.getInstance(strictField, chronology);

        assertEquals(lenientField, lenientResult);
    }
}