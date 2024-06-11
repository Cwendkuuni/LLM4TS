package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, new SimpleDateTimeParser());
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new SimpleDateTimeParser());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        assertFalse(formatter2.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertNotSame(formatter, formatterWithLocale);
        assertEquals(Locale.US, formatterWithLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertNotSame(formatter, formatterWithOffsetParsed);
        assertFalse(formatterWithOffsetParsed.isOffsetParsed());
        assertTrue(formatterWithOffsetParsed.canSetZone(true));
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithChronology = formatter.withChronology(ISODateTimeFormat.dateTimeParser().getChronology());
        assertNotSame(formatter, formatterWithChronology);
        assertEquals(ISODateTimeFormat.dateTimeParser().getChronology(), formatterWithChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertNotSame(formatter, formatterWithZoneUTC);
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeZone zone = DateTimeZone.forOffsetHours(2);
        DateTimeFormatter formatterWithZone = formatter.withZone(zone);
        assertNotSame(formatter, formatterWithZone);
        assertEquals(zone, formatterWithZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2022);
        assertNotSame(formatter, formatterWithPivotYear);
        assertEquals(2022, formatterWithPivotYear.getPivotYear());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2022);
        assertNotSame(formatter, formatterWithDefaultYear);
        assertEquals(2022, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null);
        StringBuffer sb = new StringBuffer();
        DateTime dateTime = new DateTime(1600, 1, 1, 12, 0, 0, 0);
        formatter.printTo(sb, dateTime);
        assertEquals("1600-01-01 12:00:00.000", sb.toString());
    }

    @Test
    public void testPrintToWithOffset() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null).withOffsetParsed();
        StringBuffer sb = new StringBuffer();
        DateTime dateTime = new DateTime(1600, 1, 1, 12, 0, 0, 0).withZone(DateTimeZone.forOffsetHours(2));
        formatter.printTo(sb, dateTime);
        assertEquals("1600-01-01 14:00:00.000", sb.toString());
    }

    @Test
    public void testPrintToWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), null).withZone(DateTimeZone.UTC);
        StringBuffer sb = new StringBuffer();
        DateTime dateTime = new DateTime(1600, 1, 1, 12, 0, 0, 0).withZone(DateTimeZone.UTC);
        formatter.printTo(sb, dateTime);
        assertEquals("1600-01-01T12:00:00Z", sb.toString());
    }

    @Test
    public void testPrintToWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new GregorianCalendarDateTimeParser());
        StringBuffer sb = new StringBuffer();
        DateTime dateTime = new DateTime(1600, 1, 1, 12, 0, 0, 0);
        formatter.printTo(sb, dateTime);
        assertEquals("Thu, 01 Jan 1600 12:00:00 GMT", sb.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), null);
        long millis = formatter.parseMillis("1600-01-01 12:00:00.000");
        assertEquals(112236800000L, millis);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new LocalDateTimeFormatter(), null);
        LocalDate localDate = formatter.parseLocalDate("2022-03-01");
        assertEquals(new LocalDate(2022, 3, 1), localDate);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new LocalTimeFormatter(), null);
        LocalTime localTime = formatter.parseLocalTime("14:30:00.250");
        assertEquals(new LocalTime(14, 30, 0, 250), localTime);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new LocalDateTimeFormatter(), null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2022-03-01T14:30:00.250");
        assertEquals(new LocalDateTime(2022, 3, 1, 14, 30, 0, 250), localDateTime);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeFormatter(), null);
        DateTime dateTime = formatter.parseDateTime("2022-03-01T14:30:00.250");
        assertEquals(new DateTime(2022, 3, 1, 14, 30, 0, 250, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeFormatter(), null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-03-01T14:30:00.250");
        assertEquals(new DateTime(2022, 3, 1, 14, 30, 0, 250, DateTimeZone.UTC), mutableDateTime.toDateTime());
    }
}