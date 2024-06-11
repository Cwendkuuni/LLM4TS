package org.joda.time.format;

import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.*;
import java.util.Locale;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.getDefault(), formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(null, formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), Locale.GERMAN);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.GERMAN, formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(null, formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), Locale.GERMAN, true);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertEquals(Locale.GERMAN, formatter.getLocale());
        assertTrue(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(null, formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), null, false, GregorianChronology.getInstance(), null, null, 2000);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertEquals(GregorianChronology.getInstance(), formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(null, formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), null, false, null, DateTimeZone.forOffsetHours(-5), null, 2000);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertEquals(DateTimeZone.forOffsetHours(-5), formatter.getZone());
        assertEquals(null, formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), null, false, null, null, new Integer(2022), 2000);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(new Integer(2022), formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testNewInstanceWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), null, false, null, null, null, 2022);
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2022, formatter.getDefaultYear());
    }

    @Test
    public void testPrintTo() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 30, 45, 123);
        formatter.printTo(sb, dateTime.toDateMidnightAtOffset(DateTimeUtils.SECONDSOFDAY).getMillis(), DateTimeUtils.getIsoDateChronology());
        assertEquals("2022-01-01T00:00:00.0000123Z", sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, dateTime.toDateMidnightAtOffset(DateTimeUtils.SECONDSOFDAY).getMillis(), DateTimeUtils.getIsoDateChronology());
        assertEquals("2022-01-01T00:00:00.0000123Z", writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, dateTime.toDateMidnightAtOffset(DateTimeUtils.SECONDSOFDAY).getMillis(), DateTimeUtils.getIsoDateChronology());
        assertEquals("2022-01-01T00:00:00.0000123Z", appendable.toString());

        String s = "2022-01-01T12:30:45.123";
        DateTime dateTime2 = formatter.parseLocalDateTime(s);
        formatter.printTo(sb, dateTime2.getMillis(), DateTimeUtils.getIsoDateChronology());
        assertEquals("2022-01-01T12:30:45.123Z", sb.toString());
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        ReadWritableInstant readWritableInstant = new DateTime(2022, 1, 1, 12, 30, 45, 123, DateTimeZone.UTC);
        String s = "2022-01-01T12:30:45.123";
        formatter.parseInto(readWritableInstant, s, 0);
        assertEquals(2022, readWritableInstant.getYear());
        assertEquals(1, readWritableInstant.getMonthOfYear());
        assertEquals(1, readWritableInstant.getDayOfMonth());
        assertEquals(12, readWritableInstant.getHourOfDay());
        assertEquals(30, readWritableInstant.getMinuteOfHour());
        assertEquals(45, readWritableInstant.getSecondOfMinute());
        assertEquals(123, readWritableInstant.getMillisOfSecond());
        assertEquals(0, readWritableInstant.getFractionOfSecond());
        assertEquals(DateTimeZone.UTC, readWritableInstant.getZone());
        assertEquals(DateTimeUtils.getIsoDateChronology(), readWritableInstant.getChronology());
        assertEquals(0, readWritableInstant.getOffset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        ReadWritableInstant readWritableInstant = new DateTime(2022, 1, 1, 12, 30, 45, 123, DateTimeZone.UTC);
        String s = "XXX-XX-XX X:XX:XX.XXX";
        formatter.parseInto(readWritableInstant, s, 0);
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        long millis = formatter.parseMillis("2022-01-01T12:30:45.123");
        DateTime dateTime = new DateTime(millis, DateTimeUtils.getIsoDateChronology());
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthOfYear());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHourOfDay());
        assertEquals(30, dateTime.getMinuteOfHour());
        assertEquals(45, dateTime.getSecondOfMinute());
        assertEquals(123, dateTime.getMillisOfSecond());
        assertEquals(0, dateTime.getFractionOfSecond());
        assertEquals(DateTimeUtils.getIsoDateChronology(), dateTime.getChronology());
        assertEquals(DateTimeZone.UTC, dateTime.getZone());
        assertEquals(0, dateTime.getOffset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        long millis = formatter.parseMillis("XXX-XX-XX X:XX:XX.XXX");
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
        LocalDate date = formatter.parseLocalDate("2022-01-01");
        assertEquals(2022, date.getYear());
        assertEquals(1, date.getMonthOfYear());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
        formatter.parseLocalDate("XXX-XX-XX");
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), new SimpleDateTimeParser());
        LocalTime time = formatter.parseLocalTime("12:30:45.123");
        assertEquals(12, time.getHourOfDay());
        assertEquals(30, time.getMinuteOfHour());
        assertEquals(45, time.getSecondOfMinute());
        assertEquals(123, time.getMillisOfSecond());
        assertEquals(0, time.getNanosecond());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), new SimpleDateTimeParser());
        formatter.parseLocalTime("XX:XX:XX.XXX");
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        LocalDateTime dateTime = formatter.parseLocalDateTime("2022-01-01T12:30:45.123");
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthOfYear());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHourOfDay());
        assertEquals(30, dateTime.getMinuteOfHour());
        assertEquals(45, dateTime.getSecondOfMinute());
        assertEquals(123, dateTime.getMillisOfSecond());
        assertEquals(0, dateTime.getNanosecond());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLocalDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        formatter.parseLocalDateTime("XXX-XX-XX T:XX:XX.XXX");
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        DateTime dateTime = formatter.parseDateTime("2022-01-01T12:30:45.123");
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthOfYear());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHourOfDay());
        assertEquals(30, dateTime.getMinuteOfHour());
        assertEquals(45, dateTime.getSecondOfMinute());
        assertEquals(123, dateTime.getMillisOfSecond());
        assertEquals(0, dateTime.getFractionOfSecond());
        assertEquals(DateTimeUtils.getIsoDateChronology(), dateTime.getChronology());
        assertEquals(DateTimeZone.UTC, dateTime.getZone());
        assertEquals(0, dateTime.getOffset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        formatter.parseDateTime("XXX-XX-XX T:XX:XX.XXX");
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2022-01-01T12:30:45.123");
        assertEquals(2022, mutableDateTime.getYear());
        assertEquals(1, mutableDateTime.getMonthOfYear());
        assertEquals(1, mutableDateTime.getDayOfMonth());
        assertEquals(12, mutableDateTime.getHourOfDay());
        assertEquals(30, mutableDateTime.getMinuteOfHour());
        assertEquals(45, mutableDateTime.getSecondOfMinute());
        assertEquals(123, mutableDateTime.getMillisOfSecond());
        assertEquals(0, mutableDateTime.getNanosecond());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMutableDateTimeInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        formatter.parseMutableDateTime("XXX-XX-XX T:XX:XX.XXX");
    }
}