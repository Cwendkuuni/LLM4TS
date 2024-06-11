package org.joda.time.format;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import org.junit.*;

public class DateTimeFormatterTest {
    

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, new DateTimeParser());
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        assertTrue(formatter.isParser());

        formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        assertFalse(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, formatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter = formatter.withOffsetParsed();
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        Chronology chrono = GregorianChronology.getInstance();
        formatter = formatter.withChronology(chrono);
        assertEquals(chrono, formatter.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        formatter = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(2);
        formatter = formatter.withZone(dateTimeZone);
        assertEquals(dateTimeZone, formatter.getZone());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, instant);
        String expected = "2019-01-01T12:00:00.000Z";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, instant);
        String expected = "2019-01-01T12:00:00.000Z";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        StringWriter stringWriter = new StringWriter();
        StringBuffer stringBuffer = stringWriter.getBuffer();
        formatter.printTo(stringBuffer, instant);
        String expected = "2019-01-01T12:00:00.000Z";
        assertEquals(expected, stringBuffer.toString());

        StringBuilder stringBuilder = new StringBuilder();
        formatter.printTo(stringBuilder, instant);
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void testPrintToStringBufferWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        Instant instant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        formatter = formatter.withChronology(GregorianChronology.getInstance());
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, instant);
        String expected = "2019-01-01T12:00:00.000Z";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testParseIntoReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        Instant readableInstant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        MutableInstant writableInstant = new MutableInstant();
        int offset = formatter.parseInto(writableInstant, "2019-01-01T12:00:00Z", 0);
        assertEquals(offset, 23);
        assertEquals(readableInstant, writableInstant.toInstant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoReadWritableInstantWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        Instant readableInstant = new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        MutableInstant writableInstant = new MutableInstant();
        int offset = formatter.parseInto(writableInstant, "InvalidFormat", 0);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        long millis = formatter.parseMillis("2019-01-01T12:00:00Z");
        assertEquals(millis, 1546300800000L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        long millis = formatter.parseMillis("InvalidFormat");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalDate localDate = formatter.parseLocalDate("2019-01-01");
        assertEquals(new DateTime(2019, 1, 1).toLocalDate(), localDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalDate localDate = formatter.parseLocalDate("InvalidFormat");
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalTime localTime = formatter.parseLocalTime("12:00:00");
        assertEquals(new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toLocalTime(), localTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalTime localTime = formatter.parseLocalTime("InvalidFormat");
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2019-01-01T12:00:00");
        assertEquals(new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toLocalDateTime(), localDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("InvalidFormat");
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        DateTime dateTime = formatter.parseDateTime("2019-01-01T12:00:00Z");
        assertEquals(new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        DateTime dateTime = formatter.parseDateTime("InvalidFormat");
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2019-01-01T12:00:00Z");
        assertEquals(new DateTime(2019, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC), mutableDateTime.toDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeParser(), null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("InvalidFormat");
    }

    @Test
    public void testRequirePrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        DateTimePrinter printer = formatter.requirePrinter();
        assertEquals(formatter.getPrinter(), printer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequirePrinterWhenPrinterIsNull() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        formatter.requirePrinter();
    }

    @Test
    public void testRequireParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeParser());
        DateTimeParser parser = formatter.requireParser();
        assertEquals(formatter.getParser(), parser);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequireParserWhenParserIsNull() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), null);
        formatter.requireParser();
    }

    @Test
    public void testSelectChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Chronology chronology = formatter.selectChronology(IsoChronology.getInstance());
        assertEquals(IsoChronology.getInstance(), chronology);
    }

    @Test
    public void testSelectChronologyWhenChronologyIsNull() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Chronology chronology = formatter.selectChronology(null);
        assertEquals(GregorianChronology.getInstance(), chronology);
    }

    
}