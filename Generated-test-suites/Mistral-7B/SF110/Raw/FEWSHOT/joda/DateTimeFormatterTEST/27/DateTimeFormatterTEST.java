package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.ParsePosition;
import java.util.*;
import java.io.*;
import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class DateTimeFormatterTest {

    private DateTimeFormatter formatter;
    private DateTimePrinter printer;
    private DateTimeParser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        printer = mock(DateTimePrinter.class);
        parser = mock(DateTimeParser.class);
        formatter = new DateTimeFormatter(printer, parser);
    }

    @Test
    public void testIsPrinter() {
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        assertFalse(formatter.isParser());
    }

    @Test
    public void testGetPrinter() {
        assertEquals(printer, formatter.getPrinter());
    }

    @Test
    public void testGetParser() {
        assertEquals(parser, formatter.getParser());
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("en", "US");
        DateTimeFormatter newFormatter = formatter.withLocale(locale);
        assertEquals(locale, newFormatter.getLocale());
        assertNotEquals(formatter, newFormatter);
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter newFormatter = formatter.withOffsetParsed();
        assertTrue(newFormatter.isOffsetParsed());
        assertFalse(formatter.isOffsetParsed());
        assertNotEquals(formatter, newFormatter);
    }

    @Test
    public void testWithChronology() {
        Chronology chronology = ISODateTimeFormat.dateTimeParser().getChronology();
        DateTimeFormatter newFormatter = formatter.withChronology(chronology);
        assertEquals(chronology, newFormatter.getChronology());
        assertNotEquals(formatter, newFormatter);
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter newFormatter = formatter.withZoneUTC();
        assertNull(newFormatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        DateTimeFormatter newFormatter = formatter.withZone(zone);
        assertEquals(zone, newFormatter.getZone());
        assertNotEquals(formatter, newFormatter);
    }

    @Test
    public void testPrintToStringBuffer() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T12:00";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriter() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T12:00\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(baos));
        formatter.printTo(writer, dateTime);
        writer.flush();
        assertEquals(expected, baos.toString());
    }

    @Test
    public void testPrintToAppendable() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T12:00";
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.printTo(appendable, dateTime);
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferWithOffset() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0).plusHours(2);
        String expected = "2022-05-03T14:00+02:00";
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterWithOffset() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0).plusHours(2);
        String expected = "2022-05-03T14:00+02:00\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(baos));
        formatter.printTo(writer, dateTime);
        writer.flush();
        assertEquals(expected, baos.toString());
    }

    @Test
    public void testPrintToAppendableWithOffset() throws Exception {
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0).plusHours(2);
        String expected = "2022-05-03T14:00+02:00";
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.printTo(appendable, dateTime);
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferWithZone() throws Exception {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T16:00-04:00";
        StringBuffer sb = new StringBuffer();
        formatter.withZone(zone).printTo(sb, dateTime);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterWithZone() throws Exception {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T16:00-04:00\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(baos));
        formatter.withZone(zone).printTo(writer, dateTime);
        writer.flush();
        assertEquals(expected, baos.toString());
    }

    @Test
    public void testPrintToAppendableWithZone() throws Exception {
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        LocalDateTime dateTime = new LocalDateTime(2022, 5, 3, 12, 0);
        String expected = "2022-05-03T16:00-04:00";
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.withZone(zone).printTo(appendable, dateTime);
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testParseIntoReadWritableInstant() throws Exception {
        Instant instant = new DateTime(2022, 5, 3, 12, 0, 0).toInstant();
        ReadWritableInstant readWritableInstant = mock(ReadWritableInstant.class);
        when(readWritableInstant.getMillis()).thenReturn(instant.getEpochSecond() * 1000L);
        when(readWritableInstant.getChronology()).thenReturn(DateTimeUtils.getChronology(ISODateTimeFormat.dateTimeParser().getChronology()));
        formatter.parseInto(readWritableInstant, "2022-05-03T12:00", 10);
        assertTrue(readWritableInstant.isValid());
        assertEquals(instant, readWritableInstant.getInstant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoReadWritableInstantInvalid() throws Exception {
        Instant instant = new DateTime(2022, 5, 3, 12, 0, 0).toInstant();
        ReadWritableInstant readWritableInstant = mock(ReadWritableInstant.class);
        when(readWritableInstant.getMillis()).thenReturn(instant.getEpochSecond() * 1000L);
        when(readWritableInstant.getChronology()).thenReturn(DateTimeUtils.getChronology(ISODateTimeFormat.dateTimeParser().getChronology()));
        formatter.parseInto(readWritableInstant, "invalid", 10);
    }

    @Test
    public void testParseMillis() throws Exception {
        long millis = DateTimeUtils.getInstantMillis(new DateTime(2022, 5, 3, 12, 0, 0));
        String s = "2022-05-03T12:00";
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();
        long parsedMillis = formatter.parseMillis(s);
        assertEquals(millis, parsedMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalid() throws Exception {
        long millis = DateTimeUtils.getInstantMillis(new DateTime(2022, 5, 3, 12, 0, 0));
        String s = "invalid";
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();
        formatter.parseMillis(s);
    }

    @Test
    public void testParseLocalDate() throws Exception {
        String s = "2022-05-03";
        LocalDate localDate = formatter.parseLocalDate(s);
        assertEquals(new LocalDate(2022, 5, 3), localDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalid() throws Exception {
        String s = "invalid";
        LocalDate localDate = formatter.parseLocalDate(s);
    }

    @Test
    public void testParseLocalTime() throws Exception {
        String s = "12:30";
        LocalTime localTime = formatter.parseLocalTime(s);
        assertEquals(new LocalTime(12, 30), localTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalid() throws Exception {
        String s = "invalid";
        LocalTime localTime = formatter.parseLocalTime(s);
    }

    @Test
    public void testParseLocalDateTime() throws Exception {
        String s = "2022-05-03T12:30";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(s);
        assertEquals(new LocalDateTime(2022, 5, 3, 12, 30), localDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalid() throws Exception {
        String s = "invalid";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(s);
    }

    @Test
    public void testParseDateTime() throws Exception {
        String s = "2022-05-03T12:30Z";
        DateTime dateTime = formatter.parseDateTime(s);
        assertEquals(new DateTime(2022, 5, 3, 12, 30, 0, 0, DateTimeZone.UTC), dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalid() throws Exception {
        String s = "invalid";
        DateTime dateTime = formatter.parseDateTime(s);
    }

    @Test
    public void testParseMutableDateTime() throws Exception {
        String s = "2022-05-03T12:30Z";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(s);
        assertEquals(new MutableDateTime(2022, 5, 3, 12, 30, 0, 0, DateTimeZone.UTC), mutableDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalid() throws Exception {
        String s = "invalid";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(s);
    }
}