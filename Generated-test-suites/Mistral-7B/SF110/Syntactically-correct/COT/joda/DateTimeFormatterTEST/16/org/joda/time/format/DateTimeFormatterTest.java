package org.junit.tests;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isParser());
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testNewInstanceWithPrinterAndParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertSame(formatter.getPrinter(), new DateTimePrinter());
        assertSame(formatter.getParser(), new DateTimeParser());
    }

    @Test
    public void testNewInstanceWithLocalePrinterAndParser() {
        Locale locale = Locale.US;
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), locale);
        assertEquals(locale, formatter.getLocale());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, true);
        assertTrue(formatter.isOffsetParsed());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithChronology() {
        Chronology chronology = DateTimeUtils.getISO8601DateInstance();
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), null, false, chronology);
        assertEquals(chronology, formatter.getChronology());
        assertFalse(formatter.isOffsetParsed());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser()).withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatter.getZone());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithZone() {
        DateTimeZone zone = DateTimeZone.forID("America/Los_Angeles");
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser()).withZone(zone);
        assertEquals(zone, formatter.getZone());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser()).withPivotYear(2022);
        assertEquals(2022, formatter.getPivotYear().intValue());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testNewInstanceWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser()).withDefaultYear(2023);
        assertEquals(2023, formatter.getDefaultYear());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        formatter.printTo(new StringBuffer(), instant);
        String expected = "2022-01-01T14:00:00.000Z";
        String actual = formatter.getPrinter().print(instant);
        assertEquals(expected, actual);
        assertEquals(expected, formatter.getPrinter().print(formatter.getParser().parseLocalDateTime("2022-01-01T14:00:00.000Z")));
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, instant);
        String expected = "2022-01-01T14:00:00.000Z\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals("2022-01-01T14:00:00.000Z", sb.toString());
        Appendable appendable = new StringBuilder();
        formatter.printTo(appendable, instant);
        assertEquals("2022-01-01T14:00:00.000Z", appendable.toString());
    }

    @Test
    public void testPrintToStringBufferWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), DateTimeUtils.getISO8601DateInstance());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        formatter.printTo(new StringBuffer(), instant);
        String expected = "2022-01-01T14:00:00.000Z";
        String actual = formatter.getPrinter().print(instant);
        assertEquals(expected, actual);
        assertEquals(expected, formatter.getPrinter().print(formatter.getParser().parseLocalDateTime("2022-01-01T14:00:00.000Z")));
    }

    @Test
    public void testPrintToWriterWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), DateTimeUtils.getISO8601DateInstance());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, instant);
        String expected = "2022-01-01T14:00:00.000Z\n";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintToAppendableWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser(), DateTimeUtils.getISO8601DateInstance());
        Instant instant = new BaseDateTime(1633075200000L, DateTimeZone.UTC).toInstant();
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals("2022-01-01T14:00:00.000Z", sb.toString());
        Appendable appendable = new StringBuilder();
        formatter.printTo(appendable, instant);
        assertEquals("2022-01-01T14:00:00.000Z", appendable.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintToStringBufferWithNull() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(1633075200000L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintToWriterWithNull() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(1633075200000L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintToAppendableWithNull() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter.printTo(null, new Instant(1633075200000L));
    }

    @Test
    public void testParseIntoInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Instant instant = DateTimeUtils.currentInstant();
        String input = formatter.print(instant);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, input, 0);
        assertEquals(input.length(), result);
        assertEquals(instant, readWritableInstant.toInstant());
    }

    @Test
    public void testParseMillisString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "2022-01-01T14:00:00.000Z";
        long expected = 1633075200000L;
        long result = formatter.parseMillis(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisStringInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseMillis(input);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "2022-01-01";
        LocalDate expected = new LocalDate(2022, 1, 1);
        LocalDate result = formatter.parseLocalDate(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseLocalDate(input);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "14:00:00.000";
        LocalTime expected = new LocalTime(14, 0, 0, 0);
        LocalTime result = formatter.parseLocalTime(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseLocalTime(input);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "2022-01-01T14:00:00.000Z";
        LocalDateTime expected = new LocalDateTime(2022, 1, 1, 14, 0);
        LocalDateTime result = formatter.parseLocalDateTime(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseLocalDateTime(input);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "2022-01-01T14:00:00.000Z";
        DateTime expected = new DateTime(1633075200000L, DateTimeZone.UTC);
        DateTime result = formatter.parseDateTime(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseDateTime(input);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "2022-01-01T14:00:00.000Z";
        MutableDateTime expected = new MutableDateTime(1633075200000L, DateTimeZone.UTC);
        MutableDateTime result = formatter.parseMutableDateTime(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String input = "InvalidFormat";
        formatter.parseMutableDateTime(input);
    }
}