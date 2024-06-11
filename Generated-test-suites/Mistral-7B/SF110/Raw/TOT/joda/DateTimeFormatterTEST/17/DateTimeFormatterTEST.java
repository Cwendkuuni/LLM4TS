package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

 @Test
 public void testNewInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 assertEquals(false, formatter.isPrinter());
 assertEquals(false, formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testNewInstanceWithLocale() {
 Locale locale = Locale.US;
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser(), locale);
 assertEquals(locale, formatter.getLocale());
 }

 @Test
 public void testNewInstanceWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 assertFalse(formatter.isOffsetParsed());
 assertSame(formatter, formatter.getPrinter());
 assertSame(formatter, formatter.getParser());
 }

 @Test
 public void testNewInstanceWithChronology() {
 Chronology chronology = GregorianChronology.getInstance();
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithChronology = formatter.withChronology(chronology);
 assertEquals(chronology, formatterWithChronology.getChronology());
 assertSame(formatter, formatter.getPrinter());
 assertSame(formatter, formatter.getParser());
 }

 @Test
 public void testNewInstanceWithZone() {
 DateTimeZone zone = DateTimeZone.UTC;
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithZone = formatter.withZone(zone);
 assertEquals(zone, formatterWithZone.getZone());
 assertSame(formatter, formatter.getPrinter());
 assertSame(formatter, formatter.getParser());
 }

 @Test
 public void testNewInstanceWithPivotYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2010);
 assertEquals(2010, formatterWithPivotYear.getPivotYear().intValue());
 }

 @Test
 public void testNewInstanceWithDefaultYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2012);
 assertEquals(2012, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintTo() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 StringBuffer sb = new StringBuffer();
 Instant instant = new DateTime(2012, 1, 1, 12, 30, 45, 123, Gregorian.getInstance()).toInstant();
 formatter.printTo(sb, instant);
 assertEquals("2012-01-01 12:30:45.123", sb.toString());
 }

 @Test
 public void testPrintToWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTimeZone zone = DateTimeZone.forID("America/New_York");
 StringBuffer sb = new StringBuffer();
 Instant instant = new DateTime(2012, 1, 1, 12, 30, 45, 123, Gregorian.getInstance()).toInstant().withZone(zone);
 formatter.printTo(sb, instant);
 assertEquals("2012-01-01 16:30:45.123-05:00", sb.toString());
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 String input = "2012-01-01 12:30:45.123";
 long parsedMillis = formatter.parseMillis(input);
 assertEquals(1326251235123L, parsedMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
 String input = "2012-01-01 12:30:45.123z";
 formatter.parseMillis(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 formatter.parseMillis("");
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
 LocalDate date = formatter.parseLocalDate("2012-01-01");
 assertEquals(new LocalDate(2012, 1, 1), date);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
 formatter.parseLocalDate("2012-01-01 12:30:45");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
 formatter.parseLocalDate("");
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), new SimpleDateTimeParser());
 LocalTime time = formatter.parseLocalTime("12:30:45.123");
 assertEquals(new LocalTime(12, 30, 45, 123), time);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), new SimpleDateTimeParser());
 formatter.parseLocalTime("12:30:45.123z");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), new SimpleDateTimeParser());
 formatter.parseLocalTime("");
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 LocalDateTime dateTime = formatter.parseLocalDateTime("2012-01-01 12:30:45.123");
 assertEquals(new LocalDateTime(2012, 1, 1, 12, 30, 45, 123), dateTime.toLocalDateTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
 formatter.parseLocalDateTime("2012-01-01 12:30:45.123z");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 formatter.parseLocalDateTime("");
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 DateTime dateTime = formatter.parseDateTime("2012-01-01 12:30:45.123");
 assertEquals(new DateTime(2012, 1, 1, 12, 30, 45, 123), dateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
 formatter.parseDateTime("2012-01-01 12:30:45.123z");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 formatter.parseDateTime("");
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2012-01-01 12:30:45.123");
 assertEquals(new DateTime(2012, 1, 1, 12, 30, 45, 123), mutableDateTime.toDateTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
 formatter.parseMutableDateTime("2012-01-01 12:30:45.123z");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeEmptyInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
 formatter.parseMutableDateTime("");
 }
}