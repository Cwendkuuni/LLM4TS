package org.joda.time.format;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.ParsePosition;
import java.util.*;
import java.io.*;
import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeFormatterTest {

    @Mock
    private DateTimePrinter mockPrinter;

    @Mock
    private DateTimeParser mockParser;

    @InjectMocks
    private DateTimeFormatter dateTimeFormatter;

    @Test
    public void testIsPrinter() {
        assertTrue(dateTimeFormatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        assertTrue(dateTimeFormatter.isParser());
    }

    @Test
    public void testGetPrinter() {
        assertEquals(mockPrinter, dateTimeFormatter.getPrinter());
    }

    @Test
    public void testGetParser() {
        assertEquals(mockParser, dateTimeFormatter.getParser());
    }

    @Test
    public void testWithLocale() {
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatter = dateTimeFormatter.withLocale(locale);
        assertEquals(locale, formatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = dateTimeFormatter.withOffsetParsed();
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testIsOffsetParsed() {
        assertFalse(dateTimeFormatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        Chronology chronology = ISODateTimeFormat.dateTimeParser().getChronology();
        DateTimeFormatter formatter = dateTimeFormatter.withChronology(chronology);
        assertEquals(chronology, formatter.getChronology());
    }

    @Test
    public void testGetChronology() {
        assertEquals(null, dateTimeFormatter.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = dateTimeFormatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(2);
        DateTimeFormatter formatter = dateTimeFormatter.withZone(dateTimeZone);
        assertEquals(dateTimeZone, formatter.getZone());
    }

    @Test
    public void testGetZone() {
        assertEquals(null, dateTimeFormatter.getZone());
    }

    @Test
    public void testWithPivotYear() {
        Integer year = Integer.valueOf(2010);
        DateTimeFormatter formatter = dateTimeFormatter.withPivotYear(year);
        assertEquals(year, formatter.getPivotYear());
    }

    @Test
    public void testWithPivotYearInt() {
        DateTimeFormatter formatter = dateTimeFormatter.withPivotYear(2010);
        assertEquals(Integer.valueOf(2010), formatter.getPivotYear());
    }

    @Test
    public void testGetPivotYear() {
        assertEquals(null, dateTimeFormatter.getPivotYear());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = dateTimeFormatter.withDefaultYear(2010);
        assertEquals(2010, formatter.getDefaultYear());
    }

    @Test
    public void testGetDefaultYear() {
        assertEquals(2000, dateTimeFormatter.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() throws Exception {
        when(mockPrinter.printTo(any(StringBuffer.class), any(Instant.class))).thenReturn(new StringBuffer("Printed"));

        StringBuffer stringBuffer = new StringBuffer();
        dateTimeFormatter.printTo(stringBuffer, new Instant());

        assertEquals("Printed", stringBuffer.toString());
        verify(mockPrinter).printTo(stringBuffer, any(Instant.class));
    }

    @Test
    public void testPrintToWriter() throws Exception {
        when(mockPrinter.printTo(any(Writer.class), any(Instant.class))).thenWriteTo(new StringWriter(), "Printed");

        StringWriter stringWriter = new StringWriter();
        dateTimeFormatter.printTo(stringWriter, new Instant());

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(stringWriter, any(Instant.class));
    }

    @Test
    public void testPrintToAppendable() throws Exception {
        when(mockPrinter.printTo(any(Appendable.class), any(Instant.class))).thenAppend("Printed");

        StringWriter stringWriter = new StringWriter();
        Appendable appendable = stringWriter.getBuffer();
        dateTimeFormatter.printTo(appendable, new Instant());

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(appendable, any(Instant.class));
    }

    @Test
    public void testPrintToStringBufferInstant() throws Exception {
        when(mockPrinter.printTo(any(StringBuffer.class), anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class))).thenReturn(new StringBuffer("Printed"));

        StringBuffer stringBuffer = new StringBuffer();
        dateTimeFormatter.printTo(stringBuffer, new Instant(0L), null, 0, null, null);

        assertEquals("Printed", stringBuffer.toString());
        verify(mockPrinter).printTo(stringBuffer, anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class));
    }

    @Test
    public void testPrintToWriterInstant() throws Exception {
        when(mockPrinter.printTo(any(Writer.class), anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class))).thenWriteTo(new StringWriter(), "Printed");

        StringWriter stringWriter = new StringWriter();
        dateTimeFormatter.printTo(stringWriter, new Instant(0L), null, 0, null, null);

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(stringWriter, anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class));
    }

    @Test
    public void testPrintToAppendableInstant() throws Exception {
        when(mockPrinter.printTo(any(Appendable.class), anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class))).thenAppend("Printed");

        StringWriter stringWriter = new StringWriter();
        Appendable appendable = stringWriter.getBuffer();
        dateTimeFormatter.printTo(appendable, new Instant(0L), null, 0, null, null);

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(appendable, anyLong(), anyChronology(), anyInt(), any(DateTimeZone.class), any(Locale.class));
    }

    @Test
    public void testPrintToStringBufferReadablePartial() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.printTo(any(StringBuffer.class), any(ReadablePartial.class), any(Locale.class))).thenReturn(new StringBuffer("Printed"));

        StringBuffer stringBuffer = new StringBuffer();
        dateTimeFormatter.printTo(stringBuffer, mock(ReadablePartial.class));

        assertEquals("Printed", stringBuffer.toString());
        verify(mockPrinter).printTo(stringBuffer, any(ReadablePartial.class), any(Locale.class));
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testPrintToWriterReadablePartial() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.printTo(any(Writer.class), any(ReadablePartial.class), any(Locale.class))).thenWriteTo(new StringWriter(), "Printed");

        StringWriter stringWriter = new StringWriter();
        dateTimeFormatter.printTo(stringWriter, mock(ReadablePartial.class));

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(stringWriter, any(ReadablePartial.class), any(Locale.class));
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testPrintToAppendableReadablePartial() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.printTo(any(Appendable.class), any(ReadablePartial.class), any(Locale.class))).thenAppend("Printed");

        StringWriter stringWriter = new StringWriter();
        Appendable appendable = stringWriter.getBuffer();
        dateTimeFormatter.printTo(appendable, mock(ReadablePartial.class));

        assertEquals("Printed", stringWriter.toString());
        verify(mockPrinter).printTo(appendable, any(ReadablePartial.class), any(Locale.class));
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testPrintToString() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.print(mock(ReadableInstant.class))).thenReturn("Printed");

        assertEquals("Printed", dateTimeFormatter.print(new Instant()));
        verify(mockPrinter).print(any(ReadableInstant.class));
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testPrintToStringLong() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.print(anyLong())).thenReturn("Printed");

        assertEquals("Printed", dateTimeFormatter.print(new Instant(0L).getMillis()));
        verify(mockPrinter).print(anyLong());
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testPrintToStringReadablePartial() throws Exception {
        when(mockPrinter.requirePrinter()).thenReturn(mockPrinter);
        when(mockPrinter.print(mock(ReadablePartial.class))).thenReturn("Printed");

        assertEquals("Printed", dateTimeFormatter.print(mock(ReadablePartial.class)));
        verify(mockPrinter).print(any(ReadablePartial.class));
        verify(mockPrinter).requirePrinter();
    }

    @Test
    public void testParseIntoReadWritableInstantStringInt() throws Exception {
        long millis = 1234567890123L;
        ReadWritableInstant readWritableInstant = mock(ReadWritableInstant.class);
        when(mockParser.parseInto(any(DateTimeParserBucket.class), anyString(), anyInt())).thenReturn(millis);

        int result = dateTimeFormatter.parseInto(readWritableInstant, "2010-01-01 12:34:56.789012", 0);

        assertEquals(millis, readWritableInstant.getMillis());
        assertEquals(millis, result);
        verify(mockParser).parseInto(any(DateTimeParserBucket.class), eq("2010-01-01 12:34:56.789012"), eq(0));
    }

    @Test
    public void testParseMillisString() throws Exception {
        long millis = 1234567890123L;

        long result = dateTimeFormatter.parseMillis("2010-01-01 12:34:56.789012");

        assertEquals(millis, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisStringInvalid() throws Exception {
        dateTimeFormatter.parseMillis("Invalid");
    }

    @Test
    public void testParseLocalDateString() throws Exception {
        String date = "2010-01-01";
        LocalDate localDate = new LocalDate();

        localDate = dateTimeFormatter.parseLocalDate(date);

        assertEquals("2010-01-01", date);
        assertEquals(new LocalDate(2010, 1, 1), localDate);
    }

    @Test
    public void testParseLocalTimeString() throws Exception {
        String time = "12:34:56";
        LocalTime localTime = new LocalTime();

        localTime = dateTimeFormatter.parseLocalTime(time);

        assertEquals("12:34:56", time);
        assertEquals(new LocalTime(12, 34, 56), localTime);
    }

    @Test
    public void testParseLocalDateTimeString() throws Exception {
        String dateTime = "2010-01-01 12:34:56";
        LocalDateTime localDateTime = new LocalDateTime();

        localDateTime = dateTimeFormatter.parseLocalDateTime(dateTime);

        assertEquals("2010-01-01 12:34:56", dateTime);
        assertEquals(new LocalDateTime(2010, 1, 1, 12, 34, 56), localDateTime);
    }

    @Test
    public void testParseDateTimeString() throws Exception {
        String dateTime = "2010-01-01 12:34:56.789012";
        DateTime dateTimeInstance = new DateTime();

        dateTimeInstance = dateTimeFormatter.parseDateTime(dateTime);

        assertEquals("2010-01-01 12:34:56.789012", dateTime);
        assertEquals(new DateTime(2010, 1, 1, 12, 34, 56, 789), dateTimeInstance);
    }

    @Test
    public void testParseMutableDateTimeString() throws Exception {
        String dateTime = "2010-01-01 12:34:56.789012";
        MutableDateTime mutableDateTime = new MutableDateTime();

        mutableDateTime = dateTimeFormatter.parseMutableDateTime(dateTime);

        assertEquals("2010-01-01 12:34:56.789012", dateTime);
        assertEquals(new MutableDateTime(2010, 1, 1, 12, 34, 56, 789), mutableDateTime);
    }
}