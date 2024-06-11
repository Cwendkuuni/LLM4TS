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
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertEquals(DateTimeZone.getDefault(), formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
        assertEquals(null, formatter.getPivotYear());
    }

    @Test
    public void testNewInstanceWithPrinterParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertEquals(DateTimeZone.getDefault(), formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
        assertEquals(null, formatter.getPivotYear());
    }

    @Test
    public void testNewInstanceWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault(), Locale.ENGLISH);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.ENGLISH, formatter.getLocale());
        assertEquals(DateTimeZone.getDefault(), formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
        assertEquals(null, formatter.getPivotYear());
    }

    @Test
    public void testNewInstanceWithAllParams() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault(), Locale.ENGLISH, false, null, null, null, 2005);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.ENGLISH, formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(Integer.valueOf(2005), formatter.getPivotYear());
        assertEquals(2005, formatter.getDefaultYear());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeFormatter newFormatter = formatter.withLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, newFormatter.getLocale());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new DateTimeParserDefault(), new DateTimePrinterDefault());
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertTrue(formatter2.isParser());

        DateTimeFormatter formatter3 = new DateTimeFormatter(new DateTimeParserDefault(), new DateTimePrinterDefault());
        assertFalse(formatter3.isParser());
    }

    @Test
    public void testGetPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertSame(formatter.getPrinter(), formatter.iPrinter);
    }

    @Test
    public void testGetParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertSame(formatter.getParser(), formatter.iParser);
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeFormatter newFormatter = formatter.withOffsetParsed();
        assertTrue(newFormatter.isOffsetParsed());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertFalse(formatter.isOffsetParsed());

        DateTimeFormatter newFormatter = formatter.withOffsetParsed();
        assertTrue(newFormatter.isOffsetParsed());
    }

    @Test
    public void testGetChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertNull(formatter.getChronology());

        DateTimeFormatter newFormatter = formatter.withChronology(ISOChronology.getInstance());
        assertSame(ISOChronology.getInstance(), newFormatter.getChronology());
        assertNotSame(formatter, newFormatter);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetChronolgy() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.getChronolgy();
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeFormatter newFormatter = formatter.withZoneUTC();
        assertSame(DateTimeZone.UTC, newFormatter.getZone());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeZone zone = DateTimeZone.forID("America/Los_Angeles");
        DateTimeFormatter newFormatter = formatter.withZone(zone);
        assertSame(zone, newFormatter.getZone());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testGetZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertNull(formatter.getZone());

        DateTimeFormatter newFormatter = formatter.withZone(DateTimeZone.UTC);
        assertSame(DateTimeZone.UTC, newFormatter.getZone());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeFormatter newFormatter = formatter.withPivotYear(2010);
        assertEquals(Integer.valueOf(2010), newFormatter.getPivotYear());
        assertEquals(2010, newFormatter.getDefaultYear());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTimeFormatter newFormatter = formatter.withDefaultYear(2015);
        assertEquals(2015, newFormatter.getDefaultYear());
        assertNotSame(formatter, newFormatter);
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = new LocalDateTime(2012, 1, 1, 12, 0);
        formatter.printTo(sb, localDateTime.toDateTime());
        assertEquals("2012-01-01T12:00:00.000Z", sb.toString());

        StringWriter writer = new StringWriter();
        formatter.printTo(writer, localDateTime.toDateTime());
        assertEquals("2012-01-01T12:00:00.000Z", writer.toString());

        StringWriter writer2 = new StringWriter();
        formatter.printTo(writer2, localDateTime.getMillis(), formatter.getChronology());
        assertEquals("2012-01-01T12:00:00.000Z", writer2.toString());
    }

    @Test
    public void testPrintToWithZone() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        StringWriter writer = new StringWriter();
        LocalDateTime localDateTime = new LocalDateTime(2012, 1, 1, 12, 0);
        formatter.printTo(writer, localDateTime.toDateTime(), DateTimeZone.forID("America/Los_Angeles"));
        assertEquals("2012-01-01T19:00:00.000-08:00", writer.toString());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        assertEquals(1234567890000L, formatter.parseMillis("2012-01-01T12:00:01.123Z"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseMillis("invalidFormat");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        LocalDate localDate = formatter.parseLocalDate("2012-01-01");
        assertEquals(new LocalDate(2012, 1, 1), localDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseLocalDate("invalidFormat");
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        LocalTime localTime = formatter.parseLocalTime("12:00:01.123");
        assertEquals(new LocalTime(12, 0, 1, 123), localTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseLocalTime("invalidFormat");
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2012-01-01T12:00:01.123");
        assertEquals(new LocalDateTime(2012, 1, 1, 12, 0, 1, 123), localDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseLocalDateTime("invalidFormat");
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        DateTime dateTime = formatter.parseDateTime("2012-01-01T12:00:01.123");
        assertEquals(new DateTime(2012, 1, 1, 12, 0, 1, 123), dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseDateTime("invalidFormat");
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2012-01-01T12:00:01.123");
        assertEquals(new MutableDateTime(2012, 1, 1, 12, 0, 1, 123), mutableDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterDefault(), new DateTimeParserDefault());
        formatter.parseMutableDateTime("invalidFormat");
    }
}