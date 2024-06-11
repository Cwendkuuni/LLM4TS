package org.joda.time.format;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
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
    public void testNewInstanceWithPrinterAndParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeFormat("yyyy-MM-dd"));
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithLocaleAndPrinterAndParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeFormat("yyyy-MM-dd"), Locale.US);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.US, formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss'Z'"), null, null, true, null, null, null, 2000);
        assertTrue(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertTrue(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, GregorianChronology.INSTANCE, null, null, 2000);
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertEquals(GregorianChronology.INSTANCE, formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, DateTimeZone.forOffsetHours(-5), null, 2000);
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertEquals(DateTimeZone.forOffsetHours(-5), formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, new Integer(1950), 2000);
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertEquals(new Integer(1950), formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, null, false, null, null, null, 1990);
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(1990, formatter.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        LocalDateTime dateTime = new LocalDateTime(2021, 1, 1, 12, 30, 0);
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, dateTime);
        assertEquals("2021-01-01", buffer.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        LocalDateTime dateTime = new LocalDateTime(2021, 1, 1, 12, 30, 0);
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, dateTime);
        assertEquals("2021-01-01\n", writer.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        LocalDateTime dateTime = new LocalDateTime(2021, 1, 1, 12, 30, 0);
        StringWriter writer = new StringWriter();
        Appendable appendable = writer;
        formatter.printTo(appendable, dateTime);
        assertEquals("2021-01-01", appendable.toString());
    }

    @Test
    public void testPrintToLong() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        long millis = DateTimeUtils.currentMillis();
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, millis);
        assertEquals(formatter.getPrinter().estimatePrintedLength(), buffer.length());
    }

    @Test
    public void testPrintToReadableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        Instant instant = DateTimeUtils.currentInstant();
        StringBuffer buffer = new StringBuffer();
        formatter.printTo(buffer, instant);
        assertEquals(formatter.getPrinter().estimatePrintedLength(), buffer.length());
    }

    @Test
    public void testParseIntoReadableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        Instant instant = DateTimeUtils.currentInstant();
        ReadWritableInstant readWritableInstant = new DurationInstant(0, DateTimeUtils.MILLISECS);
        int result = formatter.parseInto(readWritableInstant, "2021-12-31 23:59:59.999");
        assertEquals(result, readWritableInstant.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoReadableInstantWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        Instant instant = DateTimeUtils.currentInstant();
        ReadWritableInstant readWritableInstant = new DurationInstant(0, DateTimeUtils.MILLISECS);
        formatter.parseInto(readWritableInstant, "2021-12-31 23:59:59.999Z"); // Z is not supported by SimpleDateTimeFormat
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        long millis = DateTimeUtils.currentMillis();
        String text = formatter.print(instant);
        long parsedMillis = formatter.parseMillis(text);
        assertEquals(millis, parsedMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        long millis = DateTimeUtils.currentMillis();
        String text = formatter.print(instant) + "Z"; // Z is not supported by SimpleDateTimeFormat
        formatter.parseMillis(text);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        LocalDate date = LocalDate.of(2022, 2, 29);
        String text = formatter.print(date);
        LocalDate parsedDate = formatter.parseLocalDate(text);
        assertEquals(date, parsedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), null);
        LocalDate date = LocalDate.of(2022, 2, 29);
        String text = formatter.print(date) + "T"; // T is not supported by SimpleDateTimeFormat
        formatter.parseLocalDate(text);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), null);
        LocalTime time = LocalTime.of(12, 30, 0);
        String text = formatter.print(time);
        LocalTime parsedTime = formatter.parseLocalTime(text);
        assertEquals(time, parsedTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), null);
        LocalTime time = LocalTime.of(12, 30, 0);
        String text = formatter.print(time) + "Z"; // Z is not supported by SimpleDateTimeFormat
        formatter.parseLocalTime(text);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        LocalDateTime dateTime = LocalDateTime.of(2022, 2, 29, 12, 30, 0);
        String text = formatter.print(dateTime);
        LocalDateTime parsedDateTime = formatter.parseLocalDateTime(text);
        assertEquals(dateTime, parsedDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        LocalDateTime dateTime = LocalDateTime.of(2022, 2, 29, 12, 30, 0);
        String text = formatter.print(dateTime) + "Z"; // Z is not supported by SimpleDateTimeFormat
        formatter.parseLocalDateTime(text);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        DateTime dateTime = new DateTime(2022, 2, 29, 12, 30, 0, 0);
        String text = formatter.print(dateTime);
        DateTime parsedDateTime = formatter.parseDateTime(text);
        assertEquals(dateTime, parsedDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        DateTime dateTime = new DateTime(2022, 2, 29, 12, 30, 0, 0);
        String text = formatter.print(dateTime) + "Z"; // Z is not supported by SimpleDateTimeFormat
        formatter.parseDateTime(text);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        MutableDateTime mutableDateTime = new MutableDateTime();
        LocalDateTime dateTime = LocalDateTime.of(2022, 2, 29, 12, 30, 0);
        mutableDateTime.set(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(), dateTime.getMillisOfSecond());
        String text = formatter.print(mutableDateTime);
        MutableDateTime parsedDateTime = formatter.parseMutableDateTime(text);
        assertEquals(mutableDateTime, parsedDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeWithInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        MutableDateTime mutableDateTime = new MutableDateTime();
        LocalDateTime dateTime = LocalDateTime.of(2022, 2, 29, 12, 30, 0);
        mutableDateTime.set(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute(), dateTime.getMillisOfSecond());
        String text = formatter.print(mutableDateTime) + "Z"; // Z is not supported by SimpleDateTimeFormat
        formatter.parseMutableDateTime(text);
    }
}