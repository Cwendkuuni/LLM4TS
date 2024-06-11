package org.joda.time.format;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.ChronologyUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDefaultInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter();
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getPrinter());
        assertNull(formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testWithPrinterAndParser() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("2008-08-12T12:34:56.789Z", writer.toString());

        LocalDateTime localDateTime = new LocalDateTime(2008, 8, 12, 12, 34, 56, 789);
        LocalDateTime parsedDateTime = formatter.parseLocalDateTime("2008-08-12T12:34:56.789Z");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test
    public void testWithLocale() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        Locale locale = Locale.FRANCE;
        DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
        assertTrue(formatterWithLocale.isPrinter());
        assertTrue(formatterWithLocale.isParser());
        assertEquals(printer, formatterWithLocale.getPrinter());
        assertEquals(parser, formatterWithLocale.getParser());
        assertEquals(locale, formatterWithLocale.getLocale());
        assertFalse(formatterWithLocale.isOffsetParsed());
        assertNull(formatterWithLocale.getChrono());
        assertNull(formatterWithLocale.getZone());
        assertNull(formatterWithLocale.getPivotYear());
        assertEquals(2000, formatterWithLocale.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatterWithLocale.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("12-08-2008 à 12:34:56.789", writer.toString());

        LocalDateTime localDateTime = new LocalDateTime(2008, 8, 12, 12, 34, 56, 789);
        LocalDateTime parsedDateTime = formatterWithLocale.parseLocalDateTime("12-08-2008 à 12:34:56.789");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
        assertNull(formatterWithOffsetParsed.getChrono());
        assertNull(formatterWithOffsetParsed.getZone());
        assertNull(formatterWithOffsetParsed.getPivotYear());
        assertEquals(2000, formatterWithOffsetParsed.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatterWithOffsetParsed.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("2008-08-12T12:34:56.789Z", writer.toString());

        long millis = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .parseLocalDateTime("2008-08-12T12:34:56.789-05:00").getMillis();
        LocalDateTime localDateTime = new LocalDateTime(millis, ChronologyUtils.UTC);
        LocalDateTime parsedDateTime = formatterWithOffsetParsed.parseLocalDateTime("2008-08-12T12:34:56.789-05:00");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test
    public void testWithChronology() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        DateTimeZone datetimeZone = DateTimeZone.forID("America/Los_Angeles");
        DateTimeFormatter formatterWithChronology = formatter.withZone(datetimeZone);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertEquals(DateTimeUtils.UTC, formatterWithChronology.getChrono());
        assertEquals(datetimeZone, formatterWithChronology.getZone());
        assertNull(formatterWithChronology.getPivotYear());
        assertEquals(2000, formatterWithChronology.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatterWithChronology.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("Mon, 12 Aug 2008 16:34:56 PDT", writer.toString());

        LocalDateTime localDateTime = new LocalDateTime(2008, 8, 12, 16, 34, 56, 789, DateTimeUtils.UTC);
        LocalDateTime parsedDateTime = formatterWithChronology.parseLocalDateTime("Mon, 12 Aug 2008 16:34:56 PDT");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test
    public void testWithPivotYear() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(DateTimeConstants.CE_2001);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(DateTimeConstants.CE_2001, formatterWithPivotYear.getPivotYear());
        assertEquals(2001, formatterWithPivotYear.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatterWithPivotYear.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("2002-01-12T12:34:56.789", writer.toString());

        LocalDateTime localDateTime = new LocalDateTime(2002, 1, 12, 12, 34, 56, 789);
        LocalDateTime parsedDateTime = formatterWithPivotYear.parseLocalDateTime("2002-01-12T12:34:56.789");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test
    public void testWithDefaultYear() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2010);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(printer, formatter.getPrinter());
        assertEquals(parser, formatter.getParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());

        StringWriter writer = new StringWriter();
        formatterWithDefaultYear.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
        assertEquals("2010-01-12T12:34:56.789", writer.toString());

        LocalDateTime localDateTime = new LocalDateTime(2010, 1, 12, 12, 34, 56, 789);
        LocalDateTime parsedDateTime = formatterWithDefaultYear.parseLocalDateTime("2010-01-12T12:34:56.789");
        assertEquals(localDateTime, parsedDateTime);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrintingWithoutPrinter() {
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(null, parser);
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(1234567890000L, DateTimeZone.UTC));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParsingWithoutParser() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, null);
        LocalDateTime localDateTime = new LocalDateTime();
        formatter.parseLocalDateTime("2008-08-12T12:34:56.789Z");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingWithInvalidFormat() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        String invalidFormat = "InvalidFormat";
        formatter.parseLocalDateTime(invalidFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingWithNullInput() {
        DateTimePrinter printer = new DateTimePrinterDefault();
        DateTimeParser parser = new DateTimeParserDefault();
        DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
        formatter.parseLocalDateTime(null);
    }
}