package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @Test
 public void testNewInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 assertTrue(formatter.isPrinter());
 assertFalse(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChrono());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
 assertEquals(Locale.US, withLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss.SSSZ"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss.SSSZ"));
 DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
 assertTrue(withOffsetParsed.isOffsetParsed());
 assertFalse(withOffsetParsed.isPrinter());
 assertFalse(withOffsetParsed.isParser());
 assertNull(withOffsetParsed.getLocale());
 assertNull(withOffsetParsed.getChrono());
 assertNull(withOffsetParsed.getZone());
 assertNull(withOffsetParsed.getPivotYear());
 assertDefaultYear(withOffsetParsed, 2000);
 }

 @Test
 public void testPrintTo() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 StringBuffer sb = new StringBuffer();
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 45, 123);
 formatter.printTo(sb, dateTime);
 assertEquals("2022-01-01 12:30:45.123", sb.toString());
 }

 @Test
 public void testPrintToWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("EEE, dd-MMM-yy 'at' hh:mma zzz"), new SimpleDateTimeParser("EEE, dd-MMM-yy 'at' hh:mma zzz"));
 Locale locale = Locale.US;
 StringBuffer sb = new StringBuffer();
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 45, 123);
 formatter = formatter.withLocale(locale);
 formatter.printTo(sb, dateTime);
 assertEquals("Fri, 01-Jan-22 at 12:30:45 PM EST", sb.toString());
 }

 @Test
 public void testPrintToWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSZ"), new SimpleDateTimeParser("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
 StringBuffer sb = new StringBuffer();
 LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30, 45, 123);
 DateTimeZone zone = DateTimeZone.forOffsetHours(5);
 formatter.withZone(zone);
 formatter.withOffsetParsed();
 formatter.printTo(sb, dateTime.toInstant());
 assertEquals("2022-01-01T17:30:45.123+05:00", sb.toString());
 }

 @Test
 public void testParseInto() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 ReadWritableInstant readWritableInstant = new DateTime();
 int result = formatter.parseInto(readWritableInstant, "2022-01-01 12:30:45", 0);
 assertEquals(23, result);
 Long expectedMillis = 1641312445000L;
 assertEquals(expectedMillis, readWritableInstant.getMillis());
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser("yyyy-MM-dd HH:mm:ss"));
 long result = formatter.parseMillis("2022-01-01 12:30:45");
 assertEquals(1641312445000L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseError() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss"), new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss"));
 formatter.parseMillis("31-12-2022 23:59:60");
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy"), new SimpleDateTimeParser("dd-MM-yyyy"));
 LocalDate result = formatter.parseLocalDate("22-12-2022");
 assertEquals(new LocalDate(2022, 12, 22), result);
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("HH:mm:ss.SSS"), new SimpleDateTimeParser("HH:mm:ss.SSS"));
 LocalTime result = formatter.parseLocalTime("12:30:45.123");
 assertEquals(new LocalTime(12, 30, 45, 123), result);
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS"));
 LocalDateTime result = formatter.parseLocalDateTime("22-12-2022 12:30:45.123");
 assertEquals(new LocalDateTime(2022, 12, 22, 12, 30, 45, 123), result);
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSSZ"), new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSSZ"));
 DateTime result = formatter.parseDateTime("22-12-2022 12:30:45.123+05:00");
 assertEquals(new DateTime(2022, 12, 22, 17, 30, 45, 123, 500, DateTimeZone.forOffsetHours(5)), result);
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSSZ"), new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSSZ"));
 MutableDateTime result = formatter.parseMutableDateTime("22-12-2022 12:30:45.123+05:00");
 assertEquals(new MutableDateTime(2022, 12, 22, 17, 30, 45, 123, 500, DateTimeZone.forOffsetHours(5)), result.toDateTime());
 }
}