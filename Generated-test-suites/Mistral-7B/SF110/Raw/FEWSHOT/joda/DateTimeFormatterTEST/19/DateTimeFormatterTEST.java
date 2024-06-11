package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithPrinterAndParser() {
        DateTimePrinter printer = MockDateTimePrinter.create();
        DateTimeParser parser = MockDateTimeParser.create();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithPrinterParserLocale() {
        DateTimePrinter printer = MockDateTimePrinter.create();
        DateTimeParser parser = MockDateTimeParser.create();
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser, locale);
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertEquals(locale, formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithAllArgs() {
        DateTimePrinter printer = MockDateTimePrinter.create();
        DateTimeParser parser = MockDateTimeParser.create();
        Locale locale = Locale.ENGLISH;
        boolean offsetParsed = true;
        Chronology chrono = GregorianChronology.getInstance();
        DateTimeZone zone = DateTimeZone.UTC;
        Integer pivotYear = Integer.valueOf(2010);
        int defaultYear = 2020;
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser, locale, offsetParsed, chrono, zone, pivotYear, defaultYear);
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertEquals(locale, formatter.getLocale());
        assertEquals(offsetParsed, formatter.isOffsetParsed());
        assertEquals(chrono, formatter.getChronology());
        assertEquals(zone, formatter.getZone());
        assertEquals(pivotYear, formatter.getPivotYear());
        assertEquals(defaultYear, formatter.getDefaultYear());
    }

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isPrinter());
        DateTimeFormatter formatter2 = new DateTimeFormatter(MockDateTimePrinter.create(), null);
        assertTrue(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());
        DateTimeFormatter formatter2 = new DateTimeFormatter(null, MockDateTimeParser.create());
        assertTrue(formatter2.isParser());
    }

    @Test
    public void testGetPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(MockDateTimePrinter.create(), null);
        assertEquals(MockDateTimePrinter.class, formatter.getPrinter().getClass());
    }

    @Test
    public void testGetParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, MockDateTimeParser.create());
        assertEquals(MockDateTimeParser.class, formatter.getParser().getClass());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
        assertEquals(locale, formatterWithLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatter.withChronology(chronology);
        assertEquals(chronology, formatterWithChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone zone = DateTimeZone.forOffsetHours(-5);
        DateTimeFormatter formatterWithZone = formatter.withZone(zone);
        assertEquals(zone, formatterWithZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Integer pivotYear = Integer.valueOf(2010);
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(pivotYear);
        assertEquals(pivotYear, formatterWithPivotYear.getPivotYear());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        int defaultYear = 2020;
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(defaultYear);
        assertEquals(defaultYear, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        StringBuffer sb = new StringBuffer();
        ReadableInstant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        formatter.printTo(sb, instant);
        assertEquals("2022-01-01T12:30:00Z", sb.toString());
    }

    @Test
    public void testPrintToWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, GregorianChronology.getInstance());
        StringBuffer sb = new StringBuffer();
        ReadableInstant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        formatter.printTo(sb, instant);
        assertEquals("2022-01-01T12:30:00.000Z", sb.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        String input = "2022-01-01T12:30:00Z";
        long millis = formatter.parseMillis(input);
        assertEquals(1641756800000L, millis);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDate date = formatter.parseLocalDate("2022-01-01");
        assertEquals(new LocalDate(2022, 1, 1), date);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalTime time = formatter.parseLocalTime("12:30:00");
        assertEquals(new LocalTime(12, 30), time);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime dateTime = formatter.parseLocalDateTime("2022-01-01T12:30:00");
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 30), dateTime);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTime dateTime = formatter.parseDateTime("2022-01-01T12:30:00Z");
        assertEquals(new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01T12:30:00Z");
        assertEquals(new MutableDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC), mutableDateTime);
    }
}