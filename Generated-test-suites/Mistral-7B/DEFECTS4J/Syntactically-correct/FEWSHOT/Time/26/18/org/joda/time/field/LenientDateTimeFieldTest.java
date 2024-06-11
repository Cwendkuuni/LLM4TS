package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.MutableLocalTime;
import org.joda.time.ZoneUtils;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeField_differentBase() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base1 = ZoneUtils.UTC;
        Chronology base2 = ZoneUtils.UTC.withBaseCalendar(ZoneUtils.getUTC());
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField1 = LenientDateTimeField.getInstance(field, base1);
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(field, base2);
        assertNotSame(lenientField1, lenientField2);
        assertTrue(lenientField1.isLenient());
        assertTrue(lenientField2.isLenient());
    }

    @Test
    public void testLenientDateTimeField_sameInstance() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = new LenientDateTimeField(field, base);
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(field, base);
        assertEquals(lenientField, lenientField2);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testLenientDateTimeField_wrappedFieldIsNull() {
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = null;
        assertNull(LenientDateTimeField.getInstance(field, base));
    }

    @Test
    public void testLenientDateTimeField_wrappedFieldIsLenient() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = new FixedDateTimeField(0, DateTimeFieldType.dayOfMonth(), ZoneUtils.UTC);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertEquals(field, lenientField);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_valid() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableDateTime dt = new MutableDateTime();
        long value = 15;
        long expected = dt.getLocalDate().getDayOfMonth() + value;
        long result = lenientField.set(dt.getMilliseconds(), value);
        assertEquals(expected, result);
    }

    @Test
    public void testSet_outOfBounds_positive() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableDateTime dt = new MutableDateTime();
        long value = 32;
        long expected = dt.getLocalDate().getDayOfMonth() + value;
        long result = lenientField.set(dt.getMilliseconds(), value);
        assertEquals(expected, result);
    }

    @Test
    public void testSet_outOfBounds_negative() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableDateTime dt = new MutableDateTime();
        long value = -3;
        long expected = dt.getLocalDate().getDayOfMonth() + value;
        long result = lenientField.set(dt.getMilliseconds(), value);
        assertEquals(expected, result);
    }

    @Test
    public void testSet_localDateTime() {
        DateTimeFieldType type = DateTimeFieldType.dayOfMonth();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableLocalDateTime dt = new MutableLocalDateTime();
        int year = 2023;
        int month = 1;
        int day = 1;
        LocalDateTime ldt = new LocalDateTime(year, month, day, 12, 0, 0, 0);
        long value = 15;
        int expectedMonth = month + value;
        int expectedDay = day + FieldUtils.getDaysInMonth(expectedMonth, year);
        long result = lenientField.set(ldt.getMillis(), value);
        MutableLocalDateTime expectedDt = new MutableLocalDateTime(year, expectedMonth, expectedDay, 12, 0, 0, 0);
        assertEquals(expectedDt.getMillis(), result);
    }

    @Test
    public void testSet_localTime() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableLocalTime lt = new MutableLocalTime();
        int hour = 1;
        int minute = 30;
        LocalTime ldt = new LocalTime(hour, minute);
        long value = 15;
        int expectedMinute = minute + value;
        long result = lenientField.set(lt.getMillis(), value);
        assertEquals(expectedMinute, getMinuteOfHour(result));
    }

    @Test
    public void testSet_negativeValue() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        MutableLocalTime lt = new MutableLocalTime();
        int hour = 1;
        int minute = 30;
        LocalTime ldt = new LocalTime(hour, minute);
        long value = -15;
        int expectedMinute = minute + value;
        long result = lenientField.set(lt.getMillis(), value);
        assertEquals(expectedMinute, getMinuteOfHour(result));
    }

    @Test
    public void testSet_null() {
        DateTimeFieldType type = DateTimeFieldType.minuteOfHour();
        Chronology base = ZoneUtils.UTC;
        DateTimeField field = type.getFields()[0];
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNull(lenientField.set(null, 0));
    }

    private int getMinuteOfHour(long millis) {
        MutableLocalTime lt = new MutableLocalTime(millis);
        return lt.getMinuteOfHour();
    }
}