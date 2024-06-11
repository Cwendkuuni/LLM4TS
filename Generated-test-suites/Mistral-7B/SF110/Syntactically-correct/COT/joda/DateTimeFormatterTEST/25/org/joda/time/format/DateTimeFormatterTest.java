package org.joda.time.format;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;

public class DateTimeFormatterTest {
    

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(new BasicDateTimeFormatter(), null);
        assertFalse(formatter.isPrinter());
    }

    @Test
    public void testGetPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        DateTimePrinter printer = formatter.getPrinter();
        assertSame(printer, new SimpleDateTimeFormatter());

        formatter = new DateTimeFormatter(new BasicDateTimeFormatter(), null);
        assertNull(formatter.getPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        assertFalse(formatter.isParser());

        formatter = new DateTimeFormatter(null, new BasicDateTimeParser());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testGetParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        DateTimeParser parser = formatter.getParser();
        assertSame(parser, new BasicDateTimeParser());

        formatter = new DateTimeFormatter(null, new BasicDateTimeParser());
        assertSame(parser, new BasicDateTimeParser());

        formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), null);
        assertNull(formatter.getParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter = formatter.withLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, formatter.getLocale());

        formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter = formatter.withLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, formatter.getLocale());

        formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter = formatter.withLocale(null);
        assertNull(formatter.getLocale());
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        assertFalse(formatter.isOffsetParsed());

        formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, true, null, null, null, 2000);
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC).toInstant();
        String expected = "2022-01-01T12:30:00Z";
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC).toInstant();
        String expected = "2022-01-01T12:30:00Z\n";
        try (Writer writer = new StringWriter()) {
            formatter.printTo(writer, instant);
            assertEquals(expected, writer.toString());
        }
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC).toInstant();
        String expected = "2022-01-01T12:30:00Z";
        try (Appendable appendable = new StringBuffer(100)) {
            formatter.printTo(appendable, instant);
            assertEquals(expected, appendable.toString());
        }
    }

    @Test
    public void testPrintToStringBufferWithOffset() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, true, null, null, null, 2000);
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.forOffsetHours(5)).toInstant();
        String expected = "2022-01-01T12:30:00+05:00";
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintToStringBufferWithoutPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new BasicDateTimeParser());
        Instant instant = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC).toInstant();
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, instant);
    }

    @Test
    public void testParseIntoInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        Instant instant = Instant.now();
        String dateTimeString = "2022-01-01T12:30:00Z";
        int result = formatter.parseInto(instant, dateTimeString, 0);
        assertEquals(dateTimeString.length(), result);
        assertEquals(DateMidnight.parse("2022-01-01").toInstant(DateTimeZone.UTC), instant);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        long millis = formatter.parseMillis("2022-01-01T12:30:00Z");
        assertEquals(DateMidnight.parse("2022-01-01").getMillis(), millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseMillis("InvalidFormat");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        LocalDate expected = LocalDate.of(2022, 1, 1);
        LocalDate result = formatter.parseLocalDate("2022-01-01");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseLocalDate("InvalidFormat");
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        LocalTime expected = LocalTime.of(12, 30);
        LocalTime result = formatter.parseLocalTime("12:30");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseLocalTime("InvalidFormat");
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        LocalDateTime expected = new LocalDateTime(2022, 1, 1, 12, 30);
        LocalDateTime result = formatter.parseLocalDateTime("2022-01-01T12:30");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseLocalDateTime("InvalidFormat");
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        DateTime expected = new DateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        DateTime result = formatter.parseDateTime("2022-01-01T12:30:00Z");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseDateTime("InvalidFormat");
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        MutableDateTime expected = new MutableDateTime(2022, 1, 1, 12, 30, 0, 0, DateTimeZone.UTC);
        MutableDateTime result = formatter.parseMutableDateTime("2022-01-01T12:30:00Z");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseMutableDateTime("InvalidFormat");
    }

    
}