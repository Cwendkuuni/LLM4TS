package org.joda.time.format;

import org.joda.time.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithLocale() {
        Locale locale = Locale.US;
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), locale);
        assertEquals(locale, formatter.getLocale());
    }

    @Test
    public void testNewInstanceWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, true, null, null, null, 2000);
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testNewInstanceWithChronology() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, false, chronology, null, null, 2000);
        assertEquals(chronology, formatter.getChronology());
    }

    @Test
    public void testNewInstanceWithZone() {
        DateTimeZone zone = DateTimeZone.UTC;
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, false, null, zone, null, 2000);
        assertEquals(zone, formatter.getZone());
    }

    @Test
    public void testNewInstanceWithPivotYear() {
        int pivotYear = 2010;
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, false, null, null, pivotYear, 2000);
        assertEquals(pivotYear, formatter.getPivotYear());
    }

    @Test
    public void testNewInstanceWithDefaultYear() {
        int defaultYear = 2020;
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, false, null, null, null, defaultYear);
        assertEquals(defaultYear, formatter.getDefaultYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullPrinter() {
        new DateTimeFormatter(null, new DateTimeParser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullParser() {
        new DateTimeFormatter(new DateTimePrinter(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testPrintToWithNullStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(System.currentTimeMillis()));
    }

    @Test(expected = NullPointerException.class)
    public void testPrintToWithNullWriter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(System.currentTimeMillis()), null);
    }

    @Test(expected = IOException.class)
    public void testPrintToWithNullAppendable() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(System.currentTimeMillis()));
    }

    @Test
    public void testPrintToWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Instant instant = new Instant(System.currentTimeMillis());
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, instant);
        String expected = formatter.getPrinter().print(instant);
        assertEquals(expected, stringBuffer.toString());
    }

    @Test
    public void testParseMillisWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2021-12-31T23:59:59.999Z";
        long millis = formatter.parseMillis(dateString);
        Instant instant = new Instant(millis);
        assertEquals(instant, new Instant(System.currentTimeMillis()).plus(millis));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2021-12-31T23:59:59.999a";
        formatter.parseMillis(dateString);
    }

    @Test
    public void testParseLocalDateWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2021-12-31";
        LocalDate localDate = formatter.parseLocalDate(dateString);
        LocalDate expected = LocalDate.of(2021, 12, 31);
        assertEquals(expected, localDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2021-12-32";
        formatter.parseLocalDate(dateString);
    }

    @Test
    public void testParseLocalTimeWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String timeString = "23:59:59.999";
        LocalTime localTime = formatter.parseLocalTime(timeString);
        LocalTime expected = LocalTime.of(23, 59, 59, 0);
        assertEquals(expected, localTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String timeString = "23:59:60";
        formatter.parseLocalTime(timeString);
    }

    @Test
    public void testParseLocalDateTimeWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:59.999Z";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
        LocalDateTime expected = LocalDateTime.of(2021, 12, 31, 23, 59, 59, 0);
        assertEquals(expected, localDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:60Z";
        formatter.parseLocalDateTime(dateTimeString);
    }

    @Test
    public void testParseDateTimeWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:59.999Z";
        DateTime dateTime = formatter.parseDateTime(dateTimeString);
        DateTime expected = new DateTime(System.currentTimeMillis() + dateTimeString.length() * 1000);
        assertEquals(expected, dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:60Z";
        formatter.parseDateTime(dateTimeString);
    }

    @Test
    public void testParseMutableDateTimeWithValidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:59.999Z";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
        MutableDateTime expected = new MutableDateTime(System.currentTimeMillis() + dateTimeString.length() * 1000);
        assertEquals(expected, mutableDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidData() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateTimeString = "2021-12-31T23:59:60Z";
        formatter.parseMutableDateTime(dateTimeString);
    }
}