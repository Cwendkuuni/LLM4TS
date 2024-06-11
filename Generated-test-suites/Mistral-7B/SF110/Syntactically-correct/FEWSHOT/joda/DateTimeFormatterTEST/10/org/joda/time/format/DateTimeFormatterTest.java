package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isPrinter());

        DateTimeFormatter printerFormatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        assertTrue(printerFormatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        DateTimeFormatter parserFormatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        assertTrue(parserFormatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertNotSame(formatter, withLocale);
        assertEquals(Locale.US, withLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertNotSame(formatter, withOffsetParsed);
        assertFalse(formatter.isOffsetParsed());
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone datetimeZone = DateTimeZone.forID("America/New_York");
        Chronology chronology = GregorianChronology.getInstance(datetimeZone);
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertNotSame(formatter, withChronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertNotSame(formatter, withZoneUTC);
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone datetimeZone = DateTimeZone.forID("America/New_York");
        DateTimeFormatter withZone = formatter.withZone(datetimeZone);
        assertNotSame(formatter, withZone);
        assertEquals(datetimeZone, withZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2023);
        assertNotSame(formatter, withPivotYear);
        assertEquals(2023, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2022);
        assertNotSame(formatter, withDefaultYear);
        assertEquals(2022, withDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Date date = new Date();
        StringBuffer sb = new StringBuffer(10);
        formatter.printTo(sb, date);
        assertEquals("Sun Jan 30 15:41:52 GMT 2023", sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, date);
            assertEquals("Sun Jan 30 15:41:52 GMT 2023", writer.toString());
        } finally {
            writer.close();
        }

        Appendable appendable = new StringWriter();
        try {
            formatter.printTo(appendable, date);
            assertEquals("Sun Jan 30 15:41:52 GMT 2023", appendable.toString());
        } finally {
            appendable.close();
        }
    }

    @Test
    public void testPrintToWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.CENTISECONDS, GregorianChronology.getInstance(DateTimeZone.forID("Europe/London")));
        StringBuffer sb = new StringBuffer(10);
        formatter.printTo(sb, localDateTime.toDate());
        assertEquals("15:41:52 GMT", sb.toString());
    }

    @Test
    public void testPrintToWithOffset() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withOffsetParsed();
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.CENTISECONDS, GregorianChronology.getInstance());
        StringBuffer sb = new StringBuffer(10);
        formatter.printTo(sb, localDateTime.toDate());
        assertEquals("15:41:52.000", sb.toString());
    }

    @Test
    public void testPrintToWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withZone(DateTimeZone.forID("Europe/London"));
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.CENTISECONDS, GregorianChronology.getInstance());
        StringBuffer sb = new StringBuffer(10);
        formatter.printTo(sb, localDateTime.toDate());
        assertEquals("15:41:52 GMT+01:00", sb.toString());
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.CENTISECONDS, GregorianChronology.getInstance());
        long millis = localDateTime.toInstant().getMillis();
        ReadWritableInstant readWritableInstant = ISODateTimeFormat.dateTimeParser().withZone(DateTimeZone.UTC).parseLocalDateTime("2023-01-30T15:41:52.000Z").toInstant();
        int result = formatter.parseInto(readWritableInstant, "2023-01-30T15:41:52.000Z", 0);
        assertEquals(millis, readWritableInstant.getMillis());
        assertEquals(result, 0);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        long result = formatter.parseMillis("2023-01-30T15:41:52.000Z");
        assertEquals(1669013212000L, result);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        LocalDate localDate = formatter.parseLocalDate("2023-01-30");
        assertEquals(new LocalDate(2023, 1, 30), localDate);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        LocalTime localTime = formatter.parseLocalTime("15:41:52");
        assertEquals(new LocalTime(15, 41, 52), localTime);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2023-01-30T15:41:52.000");
        assertEquals(new LocalDateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.MILLIS, GregorianChronology.getInstance()), localDateTime);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        DateTime dateTime = formatter.parseDateTime("2023-01-30T15:41:52.000Z");
        assertEquals(new DateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.MILLIS, GregorianChronology.getInstance(), DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2023-01-30T15:41:52.000Z");
        assertEquals(new DateTime(2023, 1, 30, 15, 41, 52, 0, DateTimeUtils.MILLIS, GregorianChronology.getInstance(), DateTimeZone.UTC), mutableDateTime.toDateTime());
    }
}