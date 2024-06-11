package org.joda.time.format;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @org.junit.Test
    public void testConstruction() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @org.junit.Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Locale locale = Locale.US;
        DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
        assertEquals(locale, formatterWithLocale.getLocale());
        assertFalse(formatterWithLocale.isPrinter());
        assertFalse(formatterWithLocale.isParser());
        assertNull(formatterWithLocale.getPrinter());
        assertNull(formatterWithLocale.getParser());
        assertNull(formatterWithLocale.getChronology());
        assertNull(formatterWithLocale.getZone());
        assertEquals(2000, formatterWithLocale.getDefaultYear());
    }

    @org.junit.Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertFalse(formatterWithOffsetParsed.isOffsetParsed());
        assertTrue(formatterWithOffsetParsed.isParser());
        assertFalse(formatterWithOffsetParsed.isPrinter());
        assertNull(formatterWithOffsetParsed.getParser());
        assertNull(formatterWithOffsetParsed.getPrinter());
        assertNull(formatterWithOffsetParsed.getChronology());
        assertNull(formatterWithOffsetParsed.getZone());
        assertEquals(2000, formatterWithOffsetParsed.getDefaultYear());
    }

    @org.junit.Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatter.withChronology(chronology);
        assertEquals(chronology, formatterWithChronology.getChronology());
        assertFalse(formatterWithChronology.isPrinter());
        assertFalse(formatterWithChronology.isParser());
        assertNull(formatterWithChronology.getPrinter());
        assertNull(formatterWithChronology.getParser());
        assertNull(formatterWithChronology.getLocale());
        assertNull(formatterWithChronology.getZone());
        assertEquals(2000, formatterWithChronology.getDefaultYear());
    }

    @org.junit.Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertNull(formatterWithZoneUTC.getZone());
        assertFalse(formatterWithZoneUTC.isPrinter());
        assertFalse(formatterWithZoneUTC.isParser());
        assertNull(formatterWithZoneUTC.getPrinter());
        assertNull(formatterWithZoneUTC.getParser());
        assertNull(formatterWithZoneUTC.getLocale());
        assertNotNull(formatterWithZoneUTC.getChronology());
        assertEquals(UTC, formatterWithZoneUTC.getChronology());
        assertEquals(2000, formatterWithZoneUTC.getDefaultYear());
    }

    @org.junit.Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateTimeFormatter formatterWithZone = formatter.withZone(timeZone);
        assertNotNull(formatterWithZone.getZone());
        assertFalse(formatterWithZone.isPrinter());
        assertFalse(formatterWithZone.isParser());
        assertNull(formatterWithZone.getPrinter());
        assertNull(formatterWithZone.getParser());
        assertNull(formatterWithZone.getLocale());
        assertNotNull(formatterWithZone.getChronology());
        assertNotEquals(UTC, formatterWithZone.getChronology());
        assertEquals(2000, formatterWithZone.getDefaultYear());
    }

    @org.junit.Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2010);
        assertEquals(2010, formatterWithPivotYear.getPivotYear().intValue());
        assertFalse(formatterWithPivotYear.isPrinter());
        assertFalse(formatterWithPivotYear.isParser());
        assertNull(formatterWithPivotYear.getPrinter());
        assertNull(formatterWithPivotYear.getParser());
        assertNull(formatterWithPivotYear.getLocale());
        assertNull(formatterWithPivotYear.getZone());
        assertEquals(2010, formatterWithPivotYear.getDefaultYear());
    }

    @org.junit.Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2010);
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());
        assertFalse(formatterWithDefaultYear.isPrinter());
        assertFalse(formatterWithDefaultYear.isParser());
        assertNull(formatterWithDefaultYear.getPrinter());
        assertNull(formatterWithDefaultYear.getParser());
        assertNull(formatterWithDefaultYear.getLocale());
        assertNull(formatterWithDefaultYear.getZone());
        assertNotNull(formatterWithDefaultYear.getChronology());
    }

    @org.junit.Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        StringWriter stringWriter = new StringWriter();
        Instant instant = new Instant(1234567890000L);
        formatter.printTo(stringWriter, instant);
        assertEquals("2000-01-01T01:23:11.245Z", stringWriter.toString());

        Locale locale = Locale.US;
        DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
        StringWriter stringWriterWithLocale = new StringWriter();
        formatterWithLocale.printTo(stringWriterWithLocale, instant);
        assertEquals("1/1/00 1:23:11 AM", stringWriterWithLocale.toString());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        StringWriter stringWriterWithOffsetParsed = new StringWriter();
        formatterWithOffsetParsed.printTo(stringWriterWithOffsetParsed, instant);
        assertEquals("2000-01-01T01:23:11.245Z", stringWriterWithOffsetParsed.toString());

        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatter.withChronology(chronology);
        StringWriter stringWriterWithChronology = new StringWriter();
        formatterWithChronology.printTo(stringWriterWithChronology, instant);
        assertEquals("2000-01-01T01:23:11.245", stringWriterWithChronology.toString());

        DateTimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateTimeFormatter formatterWithZone = formatter.withZone(timeZone);
        StringWriter stringWriterWithZone = new StringWriter();
        formatterWithZone.printTo(stringWriterWithZone, instant);
        assertEquals("2000-01-01T02:23:11.245+01:00", stringWriterWithZone.toString());
    }

    @org.junit.Test
    public void testPrint() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        assertEquals("2000-01-01T01:23:11.245Z", formatter.print(new Instant(1234567890000L)));

        Locale locale = Locale.US;
        DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
        assertEquals("1/1/00 1:23:11 AM", formatterWithLocale.print(new Instant(1234567890000L)));

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertEquals("2000-01-01T01:23:11.245Z", formatterWithOffsetParsed.print(new Instant(1234567890000L)));

        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatter.withChronology(chronology);
        assertEquals("2000-01-01T01:23:11.245", formatterWithChronology.print(new Instant(1234567890000L)));

        DateTimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        DateTimeFormatter formatterWithZone = formatter.withZone(timeZone);
        assertEquals("2000-01-01T02:23:11.245+01:00", formatterWithZone.print(new Instant(1234567890000L)));
    }

    @org.junit.Test
    public void testPrintToCalendar() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Calendar calendar = new GregorianCalendar();
        Instant instant = new Instant(1234567890000L);
        formatter.printTo(calendar, instant);
        assertEquals(2000, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(23, calendar.get(Calendar.MINUTE));
        assertEquals(11, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @org.junit.Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        Instant instant = new Instant(1234567890000L);
        ReadWritableDateTime readWritableDateTime = new DateTime(instant.getMillis(), DateTimeUtils.UTC);
        String text = "2000-01-01T01:23:11.245Z";
        int result = formatter.parseInto(readWritableDateTime, text, 0);
        assertEquals(text.length(), result);
        assertEquals(instant.getMillis(), readWritableDateTime.getMillis());
    }

    @org.junit.Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        long millis = formatter.parseMillis("2000-01-01T01:23:11.245Z");
        assertEquals(1234567890000L, millis);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        formatter.parseMillis("InvalidFormat");
    }

    @org.junit.Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "2000-01-01";
        LocalDate localDate = formatter.parseLocalDate(text);
        assertEquals(new Date(new GregorianCalendar(2000, 0, 1).getTimeInMillis()), localDate.toDate());
    }

    @org.junit.Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "14:23:11";
        LocalTime localTime = formatter.parseLocalTime(text);
        assertEquals(new Time(14, 23, 11), localTime.toLocalTime());
    }

    @org.junit.Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "2000-01-01T14:23:11";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(text);
        assertEquals(new DateTime(new GregorianCalendar(2000, 0, 1, 14, 23, 11).getTimeInMillis()), localDateTime.toDateTime());
    }

    @org.junit.Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "2000-01-01T14:23:11Z";
        DateTime dateTime = formatter.parseDateTime(text);
        assertEquals(new DateTime(1234567890000L), dateTime.toDateTime());
    }

    @org.junit.Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "2000-01-01T14:23:11Z";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(text);
        assertEquals(new DateTime(1234567890000L), mutableDateTime.toDateTime());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), new DefaultDateTimePrinter());
        String text = "InvalidFormat";
        formatter.parseDateTime(text);
    }
}