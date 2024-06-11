package org.joda.time.format;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;

public class DateTimeFormatterTest {

    

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, null, null, false, null, null, null, 2000);
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, null, null, false, null, null, null, 2000);
        assertTrue(formatter2.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertNotSame(formatter, formatterWithLocale);
        assertEquals(Locale.US, formatterWithLocale.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.withLocale(new Locale("XX"));
    }

    @Test
    public void testIsOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isOffsetParsed());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTimeZone dateTimeZone = DateTimeZone.UTC;
        DateTimeFormatter formatterWithZone = formatter.withZone(dateTimeZone);
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter formatterWithChronology = formatterWithZone.withChronology(chronology);
        assertNotSame(formatter, formatterWithChronology);
        assertEquals(chronology, formatterWithChronology.getChronology());
        assertEquals(dateTimeZone, formatterWithChronology.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.withChronology(null);
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 UTC 2012";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        assertEquals(expected, stringBuffer.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 UTC 2012\n";
        WriteableByteArrayOutputStream outputStream = new WriteableByteArrayOutputStream();
        formatter.printTo(outputStream, dateTime);
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 UTC 2012";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        Appendable appendable = stringBuffer.append("");
        formatter.printTo(appendable, dateTime);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToStringBufferWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withZone(DateTimeZone.forOffsetHours(2));
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 05:23:20 +0200 2012";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        assertEquals(expected, stringBuffer.toString());
    }

    @Test
    public void testPrintToWriterWithZone() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withZone(DateTimeZone.forOffsetHours(2));
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 05:23:20 +0200 2012\n";
        WriteableByteArrayOutputStream outputStream = new WriteableByteArrayOutputStream();
        formatter.printTo(outputStream, dateTime);
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintToAppendableWithZone() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withZone(DateTimeZone.forOffsetHours(2));
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 05:23:20 +0200 2012";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        Appendable appendable = stringBuffer.append("");
        formatter.printTo(appendable, dateTime);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToStringBufferWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withOffsetParsed();
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 +0200";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        assertEquals(expected, stringBuffer.toString());
    }

    @Test
    public void testPrintToWriterWithOffsetParsed() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withOffsetParsed();
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 +0200\n";
        WriteableByteArrayOutputStream outputStream = new WriteableByteArrayOutputStream();
        formatter.printTo(outputStream, dateTime);
        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintToAppendableWithOffsetParsed() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withOffsetParsed();
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "Thu Sep 30 01:23:20 +0200";
        StringBuffer stringBuffer = new StringBuffer();
        formatter.printTo(stringBuffer, dateTime);
        Appendable appendable = stringBuffer.append("");
        formatter.printTo(appendable, dateTime);
        assertEquals(expected, appendable.toString());
    }

    @Test
    public void testPrintToStringBufferWithInvalidChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null).withChronology(BCEChronology.getInstance());
        DateTime dateTime = new DateTime(1357041600000L, DateTimeZone.UTC);
        String expected = "FormatParser cannot parse a DateTime with the given chronology: org.joda.time.BCEChronology";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            formatter.printTo(stringBuffer, dateTime);
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    public void testParseIntoReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        ReadWritableInstant readWritableInstant = new FixedDuration(24 * 60 * 60 * 1000, DateTimeZone.UTC);
        String dateString = "2012-09-30T01:23:20.000Z";
        int parsedInt = formatter.parseInto(readWritableInstant, dateString, 0);
        assertEquals(parsedInt, dateString.length());
        assertEquals(1357041600000L, readWritableInstant.getMillis());
        assertEquals(GregorianChronology.getInstance(), readWritableInstant.getChronology());
        assertEquals(DateTimeZone.UTC, readWritableInstant.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoReadWritableInstantWithInvalidDateString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        ReadWritableInstant readWritableInstant = new FixedDuration(24 * 60 * 60 * 1000, DateTimeZone.UTC);
        String dateString = "XXX";
        formatter.parseInto(readWritableInstant, dateString, 0);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        long millis = formatter.parseMillis("2012-09-30T01:23:20.000Z");
        assertEquals(1357041600000L, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithInvalidDateString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseMillis("XXX");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDate localDate = formatter.parseLocalDate("2012-09-30");
        assertEquals(new DateMidnight(1352324800000L, GregorianChronology.getInstance()), localDate.toDateTimeAtStartOfDay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidDateString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseLocalDate("XXX");
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalTime localTime = formatter.parseLocalTime("13:23:20");
        assertEquals(new LocalTime(13, 23, 20, 0), localTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidTimeString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseLocalTime("XX:XX:XX");
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        LocalDateTime localDateTime = formatter.parseLocalDateTime("2012-09-30T13:23:20");
        assertEquals(new DateTime(1352324800000L, GregorianChronology.getInstance()), localDateTime.toDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidDateTimeString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseLocalDateTime("XXX");
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        DateTime dateTime = formatter.parseDateTime("2012-09-30T13:23:20Z");
        assertEquals(new DateTime(1352324800000L, GregorianChronology.getInstance()), dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithInvalidDateTimeString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseDateTime("XXX");
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2012-09-30T13:23:20Z");
        assertEquals(new DateTime(1352324800000L, GregorianChronology.getInstance()), mutableDateTime.toDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidDateTimeString() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        formatter.parseMutableDateTime("XXX");
    }

    
}