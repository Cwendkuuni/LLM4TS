package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimeFormatter(), null);
        assertTrue(formatter.isPrinter());
        assertNull(formatter.getPrinter());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isParser());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new DateTimeFormatter());
        assertFalse(formatter.isPrinter());
        assertNull(formatter.getPrinter());
        assertNull(formatter.getLocale());
        assertTrue(formatter.isParser());
        assertNull(formatter.getParser());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter localeFormatter = formatter.withLocale(Locale.US);
        assertSame(formatter, localeFormatter.getPrinter());
        assertSame(formatter.getParser(), localeFormatter.getParser());
        assertSame(Locale.US, localeFormatter.getLocale());
        assertFalse(localeFormatter.isOffsetParsed());
        assertNull(localeFormatter.getChronology());
        assertNull(localeFormatter.getZone());
        assertNull(localeFormatter.getPivotYear());
        assertEquals(2000, localeFormatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter offsetParsedFormatter = formatter.withOffsetParsed();
        assertSame(formatter, offsetParsedFormatter.getPrinter());
        assertSame(formatter.getParser(), offsetParsedFormatter.getParser());
        assertSame(formatter.getLocale(), offsetParsedFormatter.getLocale());
        assertTrue(offsetParsedFormatter.isOffsetParsed());
        assertNull(offsetParsedFormatter.getChronology());
        assertNull(offsetParsedFormatter.getZone());
        assertNull(offsetParsedFormatter.getPivotYear());
        assertEquals(2000, offsetParsedFormatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter chronologyFormatter = formatter.withChronology(ISODateTimeFormat.dateTimeNoMillis());
        assertSame(formatter, chronologyFormatter.getPrinter());
        assertSame(formatter.getParser(), chronologyFormatter.getParser());
        assertSame(formatter.getLocale(), chronologyFormatter.getLocale());
        assertFalse(chronologyFormatter.isOffsetParsed());
        assertSame(ISODateTimeFormat.dateTimeNoMillis(), chronologyFormatter.getChronology());
        assertNull(chronologyFormatter.getZone());
        assertNull(chronologyFormatter.getPivotYear());
        assertEquals(2000, chronologyFormatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter zoneFormatter = formatter.withZone(DateTimeZone.UTC);
        assertSame(formatter, zoneFormatter.getPrinter());
        assertSame(formatter.getParser(), zoneFormatter.getParser());
        assertSame(formatter.getLocale(), zoneFormatter.getLocale());
        assertFalse(zoneFormatter.isOffsetParsed());
        assertNull(zoneFormatter.getChronology());
        assertSame(DateTimeZone.UTC, zoneFormatter.getZone());
        assertNull(zoneFormatter.getPivotYear());
        assertEquals(2000, zoneFormatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter pivotYearFormatter = formatter.withPivotYear(2022);
        assertSame(formatter, pivotYearFormatter.getPrinter());
        assertSame(formatter.getParser(), pivotYearFormatter.getParser());
        assertSame(formatter.getLocale(), pivotYearFormatter.getLocale());
        assertFalse(pivotYearFormatter.isOffsetParsed());
        assertNull(pivotYearFormatter.getChronology());
        assertNull(pivotYearFormatter.getZone());
        assertSame(2022, pivotYearFormatter.getPivotYear());
        assertEquals(2000, pivotYearFormatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTimeFormatter defaultYearFormatter = formatter.withDefaultYear(2050);
        assertSame(formatter, defaultYearFormatter.getPrinter());
        assertSame(formatter.getParser(), defaultYearFormatter.getParser());
        assertSame(formatter.getLocale(), defaultYearFormatter.getLocale());
        assertFalse(defaultYearFormatter.isOffsetParsed());
        assertNull(defaultYearFormatter.getChronology());
        assertNull(defaultYearFormatter.getZone());
        assertNull(defaultYearFormatter.getPivotYear());
        assertEquals(2050, defaultYearFormatter.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        StringBuffer sb = new StringBuffer();
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(sb, instant);
        assertEquals("2000-12-10 03:24:50.123", sb.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        StringWriter writer = new StringWriter();
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(writer, instant);
        assertEquals("2000-12-10 03:24:50.123", writer.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        StringWriter writer = new StringWriter();
        Appendable appendable = writer;
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(appendable, instant);
        assertEquals("2000-12-10 03:24:50.123", writer.toString());
    }

    @Test
    public void testPrintToStringBufferWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), ISODateTimeFormat.dateTimeNoMillis());
        StringBuffer sb = new StringBuffer();
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(sb, instant);
        assertEquals("2012-12-10T03:24:50.123Z", sb.toString());
    }

    @Test
    public void testPrintToWriterWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), ISODateTimeFormat.dateTimeNoMillis());
        StringWriter writer = new StringWriter();
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(writer, instant);
        assertEquals("2012-12-10T03:24:50.123Z", writer.toString());
    }

    @Test
    public void testPrintToAppendableWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), ISODateTimeFormat.dateTimeNoMillis());
        StringWriter writer = new StringWriter();
        Appendable appendable = writer;
        Instant instant = new Instant(1234567890123L);
        formatter.printTo(appendable, instant);
        assertEquals("2012-12-10T03:24:50.123Z", writer.toString());
    }

    @Test
    public void testParseIntoReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        ReadWritableInstant readWritableInstant = new DateTime();
        String dateTimeString = "2000-12-10 03:24:50.123";
        int result = formatter.parseInto(readWritableInstant, dateTimeString, 0);
        assertEquals(dateTimeString.length(), result);
        assertEquals(1234567890123L, readWritableInstant.getMillis());
        assertEquals(DateTimeZone.getDefault(), readWritableInstant.getZone());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        long millis = formatter.parseMillis("2000-12-10 03:24:50.123");
        assertEquals(1234567890123L, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z"), null);
        formatter.parseMillis("2000-12-10 03:24:50.123");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd"), null);
        LocalDate date = formatter.parseLocalDate("2000-12-10");
        assertEquals(new GregorianDate(2000, 11, 10), date.toDate());
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("HH:mm:ss.SSS"), null);
        LocalTime time = formatter.parseLocalTime("03:24:50.123");
        assertEquals(new Time(3, 24, 50, 123), time.toInstant().toDate().getTime());
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2000-12-10 03:24:50.123");
        assertEquals(new DateTime(1234567890123L), localDateTime.toInstant().toDate());
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        DateTime dateTime = formatter.parseDateTime("2000-12-10 03:24:50.123");
        assertEquals(new DateTime(1234567890123L), dateTime);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS z"), null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2000-12-10 03:24:50.123");
        assertEquals(new DateTime(1234567890123L), mutableDateTime.toInstant().toDate());
    }
}