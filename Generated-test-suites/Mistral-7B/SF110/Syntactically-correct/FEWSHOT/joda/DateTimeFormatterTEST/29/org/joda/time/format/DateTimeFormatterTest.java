package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithPrinterAndParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimePrinter printer = formatter.getPrinter();
        DateTimeParser parser = formatter.getParser();
        DateTimeFormatter newFormatter = new DateTimeFormatter(printer, parser);
        assertEquals(formatter, newFormatter);
    }

    @Test
    public void testNewInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser(), locale);
        assertEquals(locale, formatter.getLocale());
    }

    @Test
    public void testNewInstanceWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
        assertFalse(formatter.isOffsetParsed());
    }

    @Test
    public void testNewInstanceWithChronology() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
        assertNull(formatter.getChronology());
    }

    @Test
    public void testNewInstanceWithZone() {
        DateTimeZone zone = DateTimeZone.UTC;
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withZone = formatter.withZone(zone);
        assertEquals(zone, withZone.getZone());
        assertNull(formatter.getZone());
    }

    @Test
    public void testNewInstanceWithPivotYear() {
        int pivotYear = 2020;
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(pivotYear);
        assertEquals(pivotYear, withPivotYear.getPivotYear().intValue());
        assertNull(formatter.getPivotYear());
    }

    @Test
    public void testNewInstanceWithDefaultYear() {
        int defaultYear = 2019;
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(defaultYear);
        assertEquals(defaultYear, withDefaultYear.getDefaultYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testPrintTo() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDateTime dateTime = LocalDateTime.parse("2021-12-31T23:59:59");
        formatter.printTo(sb, dateTime.toInstant());
        assertEquals("2021-12-31 23:59:59.000", sb.toString());
    }

    @Test
    public void testPrintToWithZone() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Writer writer = new StringWriter();
        LocalDateTime dateTime = LocalDateTime.parse("2021-12-31T23:59:59");
        formatter.printTo(writer, dateTime.toInstant(), DateTimeZone.forOffsetHours(1));
        assertEquals("2021-12-31 22:59:59.000+01:00", writer.toString());
    }

    @Test
    public void testPrintToWithChronology() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        Appendable appendable = new StringBuffer();
        LocalDateTime dateTime = LocalDateTime.parse("2021-12-31T23:59:59");
        formatter.printTo(appendable, dateTime.toInstant(), DateTimeUtils.ISO_8601_UTC_CHRONOLOGY);
        assertEquals("2021-12-31T23:59:59Z", appendable.toString());
    }

    @Test
    public void testPrintToWithLocale() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDateTime dateTime = LocalDateTime.parse("2021-12-31T23:59:59");
        formatter.printTo(sb, dateTime.toInstant(), Locale.FRENCH);
        assertEquals("31 déc. 2021 23:59:59", sb.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        long millis = formatter.parseMillis("2021-12-31T23:59:59.123");
        LocalDateTime dateTime = new LocalDateTime(millis, DateTimeUtils.ISO_8601_UTC);
        assertEquals("2021-12-31T23:59:59.123", formatter.print(dateTime.toInstant()));
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        LocalDate date = formatter.parseLocalDate("2021-12-31");
        assertEquals("2021-12-31", formatter.print(date));
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        LocalTime time = formatter.parseLocalTime("23:59:59");
        assertEquals("23:59:59", formatter.print(time));
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        LocalDateTime dateTime = formatter.parseLocalDateTime("2021-12-31T23:59:59");
        assertEquals("2021-12-31T23:59:59", formatter.print(dateTime));
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        DateTime dateTime = formatter.parseDateTime("2021-12-31T23:59:59");
        assertEquals("2021-12-31T23:59:59.000", formatter.print(dateTime.toInstant()));
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), new DefaultDateTimeParser());
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2021-12-31T23:59:59");
        assertEquals("2021-12-31T23:59:59.000", formatter.print(mutableDateTime.toInstant()));
    }
}