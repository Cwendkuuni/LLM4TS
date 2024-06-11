package org.joda.time.format.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;

import java.text.ParsePosition;
import java.util.Locale;

import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, new DefaultDateTimeParser());
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DefaultDateTimeParser());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        assertFalse(formatter2.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertNotSame(formatter, withLocale);
        assertEquals(Locale.US, withLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertNotSame(formatter, withOffsetParsed);
        assertFalse(formatter.isOffsetParsed());
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        assertFalse(formatter.isOffsetParsed());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withOffsetParsed();
        assertTrue(formatter2.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withChronology = formatter.withChronology(ISOChronology.getInstance());
        assertNotSame(formatter, withChronology);
        assertEquals(ISOChronology.getInstance(), withChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertNotSame(formatter, withZoneUTC);
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeZone timeZone = DateTimeZone.forID("Europe/London");
        DateTimeFormatter withZone = formatter.withZone(timeZone);
        assertNotSame(formatter, withZone);
        assertEquals(timeZone, withZone.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2022);
        assertNotSame(formatter, withPivotYear);
        assertEquals(2022, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2022);
        assertNotSame(formatter, withDefaultYear);
        assertEquals(2022, withDefaultYear.getDefaultYear());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintToWithNullInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, null);
    }

    @Test(expected = IOException.class)
    public void testPrintToWithWriterNullInstant() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Writer writer = null;
        formatter.printTo(writer, null);
    }

    @Test(expected = IOException.class)
    public void testPrintToWithAppendableNullInstant() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Appendable appendable = null;
        formatter.printTo(appendable, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintToWithNullPartial() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, null);
    }

    @Test(expected = IOException.class)
    public void testPrintToWithWriterNullPartial() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Writer writer = new StringWriter();
        formatter.printTo(writer, null);
    }

    @Test(expected = IOException.class)
    public void testPrintToWithAppendableNullPartial() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, null);
    }

    @Test
    public void testPrintTo() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null);
        Instant instant = new Instant(123456789000L);
        String expected = "2000-01-01T12:34:56.789Z";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals(expected, writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToWithChronology() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withChronology(ISOChronology.getInstance());
        Instant instant = new Instant(123456789000L);
        String expected = "2000-01-01T12:34:56.789Z";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals(expected, writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToWithZone() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withZone(DateTimeZone.forID("Europe/London"));
        Instant instant = new Instant(123456789000L);
        String expected = "2000-01-01T13:34:56.789+00:00";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals(expected, writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToWithPivotYear() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withPivotYear(2022);
        Instant instant = new Instant(123456789000L);
        String expected = "2022-01-01T12:34:56.789Z";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals(expected, writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToWithDefaultYear() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimePrinter(), null).withDefaultYear(2022);
        Instant instant = new Instant(123456789000L);
        String expected = "2022-01-01T12:34:56.789Z";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, instant);
        assertEquals(expected, writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, instant);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testParseIntoWithNullInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, null, 0);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoWithNullString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, null, 0);
    }

    @Test
    public void testParseIntoWithEmptyString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, "", 0);
        assertEquals(-1, result);
    }

    @Test
    public void testParseIntoWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        ReadWritableInstant readWritableInstant = new MutableDateTime();
        int result = formatter.parseInto(readWritableInstant, "2000-01-01T12:34:56.789Z", 0);
        assertEquals(17, result);
        assertEquals(new DateTime(2000, 1, 1, 12, 34, 56, 789, DateTimeZone.UTC), readWritableInstant.toDateTime());
    }

    @Test
    public void testParseMillisWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        long result = formatter.parseMillis("2000-01-01T12:34:56.789Z");
        assertEquals(123456789000L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseMillis("InvalidDate");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseLocalDateWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalDate result = formatter.parseLocalDate("2000-01-01");
        assertEquals(new LocalDate(2000, 1, 1), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseLocalDate("InvalidDate");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseLocalTimeWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalTime result = formatter.parseLocalTime("12:34:56");
        assertEquals(new LocalTime(12, 34, 56), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseLocalTime("InvalidTime");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseLocalDateTimeWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        LocalDateTime result = formatter.parseLocalDateTime("2000-01-01T12:34:56");
        assertEquals(new LocalDateTime(2000, 1, 1, 12, 34, 56), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseLocalDateTime("InvalidDateTime");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseDateTimeWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        DateTime result = formatter.parseDateTime("2000-01-01T12:34:56.789Z");
        assertEquals(new DateTime(2000, 1, 1, 12, 34, 56, 789, DateTimeZone.UTC), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseDateTime("InvalidDateTime");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseMutableDateTimeWithValidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        MutableDateTime result = formatter.parseMutableDateTime("2000-01-01T12:34:56.789Z");
        assertEquals(new DateTime(2000, 1, 1, 12, 34, 56, 789, DateTimeZone.UTC), result.toDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidString() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeParser(), null);
        try {
            formatter.parseMutableDateTime("InvalidDateTime");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}