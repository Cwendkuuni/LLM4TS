package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientInstance() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
        assertEquals(field.getClass(), lenientField.getClass());
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testLenientInstanceWithNullField() {
        Chronology base = mock(Chronology.class);
        DateTimeField lenientField = LenientDateTimeField.getInstance(null, base);
        assertNull(lenientField);
    }

    @Test
    public void testLenientInstanceWithNullBase() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, null);
        assertNull(lenientField);
    }

    @Test
    public void testLenientSet() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime();
        long initialInstant = dateTime.getMilliseconds();
        int value = 10;
        long expectedInstant = initialInstant + DateTimeFieldType.DAYS.getMillis(value);

        long result = lenientField.set(initialInstant, value);
        assertEquals(expectedInstant, result);
    }

    @Test
    public void testLenientSetWithNullInstant() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertNull(lenientField.set(null, 1));
    }

    @Test
    public void testLenientSetWithInvalidValue() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime();
        long initialInstant = dateTime.getMilliseconds();
        int value = 100;

        long result = lenientField.set(initialInstant, value);
        long expectedInstant = initialInstant + DateTimeFieldType.DAYS.getMillis(value);
        assertEquals(expectedInstant, result);

        // check that the wrapped field was updated
        DateTimeField wrappedField = lenientField.getWrappedField();
        MutableDateTime mutableDateTime = new MutableDateTime(initialInstant);
        int currentValue = wrappedField.get(mutableDateTime.toLocalDateTime());
        assertEquals(value, wrappedField.set(mutableDateTime.toLocalDateTime(), currentValue + 1));
    }

    @Test
    public void testLenientSetWithSameValue() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime();
        long initialInstant = dateTime.getMilliseconds();
        int value = 1;

        long result = lenientField.set(initialInstant, value);
        long expectedInstant = initialInstant;
        assertEquals(expectedInstant, result);

        DateTimeField wrappedField = lenientField.getWrappedField();
        MutableDateTime mutableDateTime = new MutableDateTime(initialInstant);
        int currentValue = wrappedField.get(mutableDateTime.toLocalDateTime());
        assertEquals(value, wrappedField.get(mutableDateTime.toLocalDateTime()));
    }

    @Test
    public void testLenientSetWithSameValueButDifferentInstants() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime1 = new MutableDateTime();
        long initialInstant1 = dateTime1.getMilliseconds();
        int value = 1;

        long result1 = lenientField.set(initialInstant1, value);
        long expectedInstant1 = initialInstant1 + DateTimeFieldType.DAYS.getMillis(value);
        assertEquals(expectedInstant1, result1);

        MutableDateTime dateTime2 = new MutableDateTime();
        long initialInstant2 = dateTime2.getMilliseconds();
        long result2 = lenientField.set(initialInstant2, value);
        long expectedInstant2 = initialInstant2 + DateTimeFieldType.DAYS.getMillis(value);
        assertEquals(expectedInstant2, result2);

        // check that the wrapped field was updated for both instants
        DateTimeField wrappedField = lenientField.getWrappedField();
        MutableDateTime mutableDateTime1 = new MutableDateTime(initialInstant1);
        int currentValue1 = wrappedField.get(mutableDateTime1.toLocalDateTime());
        assertEquals(value, wrappedField.set(mutableDateTime1.toLocalDateTime(), currentValue1 + 1));

        MutableDateTime mutableDateTime2 = new MutableDateTime(initialInstant2);
        int currentValue2 = wrappedField.get(mutableDateTime2.toLocalDateTime());
        assertEquals(value, wrappedField.set(mutableDateTime2.toLocalDateTime(), currentValue2 + 1));
    }

    @Test
    public void testLenientSetWithNullBase() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, null);
        assertNull(lenientField.iBase);
        assertNull(lenientField.set(0, 1));
    }

    @Test
    public void testLenientSetWithDifferentBases() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base1 = mock(Chronology.class);
        Chronology base2 = mock(Chronology.class);
        LenientDateTimeField lenientField1 = new LenientDateTimeField(field, base1);
        LenientDateTimeField lenientField2 = new LenientDateTimeField(field, base2);

        assertNotSame(base1, base2);
        assertNotSame(lenientField1, lenientField2);

        MutableDateTime dateTime = new MutableDateTime();
        long initialInstant = dateTime.getMilliseconds();
        int value = 1;

        long result1 = lenientField1.set(initialInstant, value);
        long expectedInstant1 = initialInstant + DateTimeFieldType.DAYS.getMillis(value);
        assertEquals(expectedInstant1, result1);

        long result2 = lenientField2.set(initialInstant, value);
        long expectedInstant2 = initialInstant + DateTimeFieldType.DAYS.getMillis(value);
        assertEquals(expectedInstant2, result2);

        // check that both lenient fields have the same wrapped field
        assertEquals(lenientField1.getWrappedField(), lenientField2.getWrappedField());
    }

    @Test
    public void testLenientGet() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime();
        long instant = dateTime.getMilliseconds();

        int expectedValue = 5;
        MutableLocalDateTime mutableLocalDateTime = new MutableLocalDateTime(instant);
        when(base.getZone()).thenReturn(ZoneUtils.UTC);
        when(base.withUTC()).thenReturn(base);
        when(field.get(mutableLocalDateTime.toLocalDateTime())).thenReturn(expectedValue);

        int value = lenientField.get(instant);
        assertEquals(expectedValue, value);
    }

    @Test
    public void testLenientGetWithNullInstant() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertNull(lenientField.get(null));
    }

    @Test
    public void testLenientGetWithNullBase() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, null);
        assertNull(lenientField.get(0));
    }

    @Test
    public void testLenientGetWithSameInstantButDifferentDates() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base = mock(Chronology.class);
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime1 = new MutableDateTime();
        long instant1 = dateTime1.getMilliseconds();
        int expectedValue1 = 5;
        MutableLocalDateTime mutableLocalDateTime1 = new MutableLocalDateTime(instant1);
        when(base.getZone()).thenReturn(ZoneUtils.UTC);
        when(base.withUTC()).thenReturn(base);
        when(field.get(mutableLocalDateTime1.toLocalDateTime())).thenReturn(expectedValue1);

        MutableDateTime dateTime2 = new MutableDateTime();
        long instant2 = dateTime2.getMilliseconds();
        int expectedValue2 = 10;
        MutableLocalDateTime mutableLocalDateTime2 = new MutableLocalDateTime(instant2);
        when(base.getZone()).thenReturn(ZoneUtils.UTC);
        when(base.withUTC()).thenReturn(base);
        when(field.get(mutableLocalDateTime2.toLocalDateTime())).thenReturn(expectedValue2);

        int value1 = lenientField.get(instant1);
        assertEquals(expectedValue1, value1);

        int value2 = lenientField.get(instant2);
        assertEquals(expectedValue2, value2);

        // check that the wrapped field returns the correct value for both instants
        DateTimeField wrappedField = lenientField.getWrappedField();
        MutableLocalDateTime mutableLocalDateTime3 = new MutableLocalDateTime(instant1);
        int currentValue1 = wrappedField.get(mutableLocalDateTime3.toLocalDateTime());
        assertEquals(expectedValue1, wrappedField.get(mutableLocalDateTime3.toLocalDateTime()));

        MutableLocalDateTime mutableLocalDateTime4 = new MutableLocalDateTime(instant2);
        int currentValue2 = wrappedField.get(mutableLocalDateTime4.toLocalDateTime());
        assertEquals(expectedValue2, wrappedField.get(mutableLocalDateTime4.toLocalDateTime()));
    }

    @Test
    public void testLenientGetWithDifferentBases() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), null);
        Chronology base1 = mock(Chronology.class);
        Chronology base2 = mock(Chronology.class);
        LenientDateTimeField lenientField1 = new LenientDateTimeField(field, base1);
        LenientDateTimeField lenientField2 = new LenientDateTimeField(field, base2);

        assertNotSame(base1, base2);
        assertNotSame(lenientField1, lenientField2);

        MutableDateTime dateTime = new MutableDateTime();
        long instant = dateTime.getMilliseconds();

        int expectedValue1 = 5;
        MutableLocalDateTime mutableLocalDateTime1 = new MutableLocalDateTime(instant);
        when(base1.getZone()).thenReturn(ZoneUtils.UTC);
        when(base1.withUTC()).thenReturn(base1);
        when(field.get(mutableLocalDateTime1.toLocalDateTime())).thenReturn(expectedValue1);

        int expectedValue2 = 10;
        MutableLocalDateTime mutableLocalDateTime2 = new MutableLocalDateTime(instant);
        when(base2.getZone()).thenReturn(ZoneUtils.UTC);
        when(base2.withUTC()).thenReturn(base2);
        when(field.get(mutableLocalDateTime2.toLocalDateTime())).thenReturn(expectedValue2);

        int value1 = lenientField1.get(instant);
        assertEquals(expectedValue1, value1);

        int value2 = lenientField2.get(instant);
        assertEquals(expectedValue2, value2);

        // check that both lenient fields have the same wrapped field
        assertEquals(lenientField1.getWrappedField(), lenientField2.getWrappedField());
    }
}