package org.joda.time.format;

import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertTrue(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), null, true, null, null, null, 2000);
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithZone = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), null, false, null, DateTimeZone.UTC, null, 2000);
        assertEquals(DateTimeZone.UTC, formatterWithZone.getZone());

        DateTimeFormatter formatterWithChronology = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), null, false, GregorianChronology.getInstance(), null, null, 2000);
        assertEquals(GregorianChronology.getInstance(), formatterWithChronology.getChronology());
    }

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        assertFalse(formatterWithoutPrinter.isPrinter());
    }

    @Test
    public void testGetPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertEquals(new SimpleDateTimePrinter(), formatter.getPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatterWithoutParser = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), null, false, null, null, null, 2000);
        assertFalse(formatterWithoutParser.isParser());
    }

    @Test
    public void testGetParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertEquals(new SimpleDateTimeParser(), formatter.getParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertEquals(Locale.getDefault(), formatter.getLocale());

        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithSameLocale = formatter.withLocale(Locale.getDefault());
        assertEquals(Locale.getDefault(), formatterWithSameLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        assertFalse(formatter.isOffsetParsed());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithoutOffsetParsed = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), null, false, null, null, null, 2000);
        assertFalse(formatterWithoutOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());

        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
        String expectedOutput = "2022-12-31 23:59:59";

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime.toInstant());
        assertEquals(expectedOutput, sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, dateTime.toInstant());
            assertEquals(expectedOutput, writer.toString());
        } finally {
            writer.close();
        }

        Appendable appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, dateTime.toInstant());
            assertEquals(expectedOutput, appendable.toString());
        } finally {
            appendable.close();
        }

        long millis = dateTime.toInstant().getMillis();
        sb = new StringBuffer();
        formatter.printTo(sb, millis);
        assertEquals(expectedOutput, sb.toString());

        writer = new StringWriter();
        try {
            formatter.printTo(writer, millis);
            assertEquals(expectedOutput, writer.toString());
        } finally {
            writer.close();
        }

        appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, millis);
            assertEquals(expectedOutput, appendable.toString());
        } finally {
            appendable.close();
        }

        ReadablePartial readablePartial = new DateTime(2022, 12, 31, 23, 59, 59, 0).toLocalDateTime();
        sb = new StringBuffer();
        formatter.printTo(sb, readablePartial);
        assertEquals(expectedOutput, sb.toString());

        writer = new StringWriter();
        try {
            formatter.printTo(writer, readablePartial);
            assertEquals(expectedOutput, writer.toString());
        } finally {
            writer.close();
        }

        appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, readablePartial);
            assertEquals(expectedOutput, appendable.toString());
        } finally {
            appendable.close();
        }
    }

    @Test
    public void testPrint() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());

        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
        String expectedOutput = "2022-12-31 23:59:59";

        String result = formatter.print(dateTime.toInstant());
        assertEquals(expectedOutput, result);

        millis = dateTime.toInstant().getMillis();
        result = formatter.print(millis);
        assertEquals(expectedOutput, result);

        readablePartial = dateTime.toLocalDateTime();
        result = formatter.print(readablePartial);
        assertEquals(expectedOutput, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintUnsupportedOperationException() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        formatter.printTo(null, new Instant(System.currentTimeMillis()));
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());

        ReadWritableInstant readWritableInstant = new DateTime(2022, 12, 31, 23, 59, 59, 0);
        String input = "2022-12-31 23:59:59";

        int result = formatter.parseInto(readWritableInstant, input, 0);
        assertEquals(input.length(), result);
        assertEquals(2022, readWritableInstant.getYear());
        assertEquals(12, readWritableInstant.getMonthOfYear());
        assertEquals(31, readWritableInstant.getDayOfMonth());
        assertEquals(23, readWritableInstant.getHourOfDay());
        assertEquals(59, readWritableInstant.getMinuteOfHour());
        assertEquals(59, readWritableInstant.getSecondOfMinute());

        // Edge case - string length is less than required
        result = formatter.parseInto(readWritableInstant, "2022-12-31", 0);
        assertEquals(10, result);
        assertEquals(2022, readWritableInstant.getYear());
        assertEquals(12, readWritableInstant.getMonthOfYear());
        assertEquals(31, readWritableInstant.getDayOfMonth());

        // Invalid input
        input = "2022-12-32 23:59:59";
        result = formatter.parseInto(readWritableInstant, input, 0);
        fail("Expected IllegalArgumentException but got: " + result);

        // Invalid input with offset
        input = "2022-12-31 23:59:59+01:00";
        result = formatter.parseInto(readWritableInstant, input, 0);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseMillis(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseLocalDate(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseLocalTime(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseLocalDateTime(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseDateTime(invalidInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
        String invalidInput = "";

        formatter.parseMutableDateTime(invalidInput);
    }
}