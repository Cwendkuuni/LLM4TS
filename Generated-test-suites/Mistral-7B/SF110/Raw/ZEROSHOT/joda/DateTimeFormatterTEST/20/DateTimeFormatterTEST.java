package org.joda.time.format;

import org.joda.time.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterInitialization() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withLocale = formatter.withLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, withLocale.getLocale());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testDateTimeFormatterWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testDateTimeFormatterWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(1);
        DateTimeFormatter withZone = formatter.withZone(dateTimeZone);
        assertEquals(dateTimeZone, withZone.getZone());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2022);
        assertEquals(2022, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2021);
        assertEquals(2021, withDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
        formatter.printTo(sb, localDateTime);
        assertEquals("2022-12-31 23:59:59", sb.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        long millis = formatter.parseMillis("2022-12-31 23:59:59");
        LocalDateTime localDateTime = new LocalDateTime(millis);
        assertEquals(2022, localDateTime.getYear());
        assertEquals(12, localDateTime.getMonthOfYear());
        assertEquals(31, localDateTime.getDayOfMonth());
        assertEquals(23, localDateTime.getHourOfDay());
        assertEquals(59, localDateTime.getMinuteOfHour());
        assertEquals(59, localDateTime.getSecondOfMinute());
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalDate localDate = formatter.parseLocalDate("2022-12-31");
        assertEquals(2022, localDate.getYear());
        assertEquals(12, localDate.getMonthOfYear());
        assertEquals(31, localDate.getDayOfMonth());
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalTime localTime = formatter.parseLocalTime("23:59:59");
        assertEquals(23, localTime.getHourOfDay());
        assertEquals(59, localTime.getMinuteOfHour());
        assertEquals(59, localTime.getSecondOfMinute());
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2022-12-31 23:59:59");
        assertEquals(2022, localDateTime.getYear());
        assertEquals(12, localDateTime.getMonthOfYear());
        assertEquals(31, localDateTime.getDayOfMonth());
        assertEquals(23, localDateTime.getHourOfDay());
        assertEquals(59, localDateTime.getMinuteOfHour());
        assertEquals(59, localDateTime.getSecondOfMinute());
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        DateTime dateTime = formatter.parseDateTime("2022-12-31 23:59:59");
        assertEquals(2022, dateTime.getYear());
        assertEquals(12, dateTime.getMonthOfYear());
        assertEquals(31, dateTime.getDayOfMonth());
        assertEquals(23, dateTime.getHourOfDay());
        assertEquals(59, dateTime.getMinuteOfHour());
        assertEquals(59, dateTime.getSecondOfMinute());
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-12-31 23:59:59");
        assertEquals(2022, mutableDateTime.getYear());
        assertEquals(12, mutableDateTime.getMonthOfYear());
        assertEquals(31, mutableDateTime.getDayOfMonth());
        assertEquals(23, mutableDateTime.getHourOfDay());
        assertEquals(59, mutableDateTime.getMinuteOfHour());
        assertEquals(59, mutableDateTime.getSecondOfMinute());
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, "2022-12-31 23:59:59", 0);
        assertEquals(result, readWritableInstant.getMillis());
        assertEquals(2022, readWritableInstant.getYear());
        assertEquals(12, readWritableInstant.getMonthOfYear());
        assertEquals(31, readWritableInstant.getDayOfMonth());
        assertEquals(23, readWritableInstant.getHourOfDay());
        assertEquals(59, readWritableInstant.getMinuteOfHour());
        assertEquals(59, readWritableInstant.getSecondOfMinute());
    }
}