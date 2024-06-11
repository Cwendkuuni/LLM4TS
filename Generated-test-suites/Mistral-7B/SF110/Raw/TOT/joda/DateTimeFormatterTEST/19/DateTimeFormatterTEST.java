package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;

import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @BeforeClass
 public static void setup() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 formatter = formatter.withLocale(Locale.US);
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 assertTrue(formatter.isPrinter());
 formatter = new DateTimeFormatter(null, new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertTrue(formatter.isPrinter());
 formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertTrue(formatter.isPrinter());
 formatter = new DateTimeFormatter(null, new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertFalse(formatter.isPrinter());
 formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertFalse(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 assertFalse(formatter.isParser());
 formatter = new DateTimeFormatter(null, new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertFalse(formatter.isParser());
 formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertFalse(formatter.isParser());
 formatter = new DateTimeFormatter(null, new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertTrue(formatter.isParser());
 formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertTrue(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertEquals(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), formatter.getPrinter());
 formatter = new DateTimeFormatter(null, new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertEquals(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 assertEquals(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), formatter.getParser());
 formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 assertEquals(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 assertNull(formatter.getLocale());
 formatter = formatter.withLocale(Locale.US);
 assertEquals(Locale.US, formatter.getLocale());
 formatter = formatter.withLocale(Locale.CHINA);
 assertEquals(Locale.CHINA, formatter.getLocale());
 formatter = formatter.withLocale(Locale.GERMAN);
 assertEquals(Locale.GERMAN, formatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 assertFalse(formatter.isOffsetParsed());
 formatter = formatter.withOffsetParsed();
 assertTrue(formatter.isOffsetParsed());
 formatter = formatter.withOffsetParsed();
 assertTrue(formatter.isOffsetParsed());
 formatter = formatter.withoutOffsetParsed();
 assertFalse(formatter.isOffsetParsed());
 }

 @Test
 public void testPrintTo() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertEquals("10-11-2015 12:30:45.678 UTC", sb.toString());
 }

 @Test
 public void testPrintToWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 formatter = formatter.withChronology(ISODateTimeFormat.dateTimeParser());
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant, ISODateTimeFormat.dateTimeParser().getChronology());
 assertEquals("2015-11-10T12:30:45.678Z", sb.toString());
 }

 @Test
 public void testPrintToWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 formatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertEquals("12-11-2015 15:30:45.678 +02:00", sb.toString());
 }

 @Test
 public void testPrintToWithZoneAndOffset() {
 DateTimeFormatter formatter = new DateTimeFormatter(null, null);
 formatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 formatter = formatter.withOffsetParsed();
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant);
 assertEquals("12-11-2015 15:30:45.678 +02:00", sb.toString());
 instant += 3600000L * 24L; // add one day
 formatter.printTo(sb, instant);
 assertEquals("13-11-2015 15:30:45.678 +02:00", sb.toString());
 }

 @Test
 public void testPrintToWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, instant, Locale.GERMAN);
 assertEquals("10.11.2015 12:30:45.678", sb.toString());
 }

 @Test
 public void testPrintToWithInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 Instant instant = new DateTime(2015, 11, 10, 12, 30, 45, 678, 0, DateTimeZone.UTC).toInstant();
 StringBuffer sb = new StringBuffer();
 try {
 formatter.printTo(sb, instant, new BasicDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US));
 fail("Should have thrown IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseInto() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 MutableDateTime mutableDateTime = new MutableDateTime();
 int parsed = formatter.parseInto(mutableDateTime, "10-11-2015 12:30:45.678 +02:00");
 assertEquals(2015, mutableDateTime.getYear());
 assertEquals(11, mutableDateTime.getMonthOfYear());
 assertEquals(10, mutableDateTime.getDayOfMonth());
 assertEquals(12, mutableDateTime.getHourOfDay());
 assertEquals(30, mutableDateTime.getMinuteOfHour());
 assertEquals(45, mutableDateTime.getSecondOfMinute());
 assertEquals(678, mutableDateTime.getMillisOfSecond());
 assertEquals(2, mutableDateTime.getZone().getOffset(mutableDateTime.getMillis()));
 assertEquals("10-11-2015 12:30:45.678 +02:00", formatter.print(mutableDateTime));
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 long millis = formatter.parseMillis("10-11-2015 12:30:45.678 +02:00");
 assertEquals(1447534745678L, millis);
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy", Locale.US), null);
 LocalDate localDate = formatter.parseLocalDate("10-11-2015");
 assertEquals(2015, localDate.getYear());
 assertEquals(11, localDate.getMonthOfYear());
 assertEquals(10, localDate.getDayOfMonth());
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("HH:mm:ss.SSS", Locale.US), null);
 LocalTime localTime = formatter.parseLocalTime("12:30:45.678");
 assertEquals(12, localTime.getHour());
 assertEquals(30, localTime.getMinute());
 assertEquals(45, localTime.getSecond());
 assertEquals(678, localTime.getMillis());
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 LocalDateTime localDateTime = formatter.parseLocalDateTime("10-11-2015 12:30:45.678");
 assertEquals(2015, localDateTime.getYear());
 assertEquals(11, localDateTime.getMonthOfYear());
 assertEquals(10, localDateTime.getDayOfMonth());
 assertEquals(12, localDateTime.getHourOfDay());
 assertEquals(30, localDateTime.getMinuteOfHour());
 assertEquals(45, localDateTime.getSecondOfMinute());
 assertEquals(678, localDateTime.getMillisOfSecond());
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 DateTime dateTime = formatter.parseDateTime("10-11-2015 12:30:45.678 +02:00");
 assertEquals(2015, dateTime.getYear());
 assertEquals(11, dateTime.getMonthOfYear());
 assertEquals(10, dateTime.getDayOfMonth());
 assertEquals(12, dateTime.getHourOfDay());
 assertEquals(30, dateTime.getMinuteOfHour());
 assertEquals(45, dateTime.getSecondOfMinute());
 assertEquals(678, dateTime.getMillisOfSecond());
 assertEquals("10-11-2015 12:30:45.678 +02:00", formatter.print(dateTime));
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 MutableDateTime mutableDateTime = new MutableDateTime();
 int parsed = formatter.parseInto(mutableDateTime, "10-11-2015 12:30:45.678 +02:00");
 assertEquals(2015, mutableDateTime.getYear());
 assertEquals(11, mutableDateTime.getMonthOfYear());
 assertEquals(10, mutableDateTime.getDayOfMonth());
 assertEquals(12, mutableDateTime.getHourOfDay());
 assertEquals(30, mutableDateTime.getMinuteOfHour());
 assertEquals(45, mutableDateTime.getSecondOfMinute());
 assertEquals(678, mutableDateTime.getMillisOfSecond());
 assertEquals(2, mutableDateTime.getZone().getOffset(mutableDateTime.getMillis()));
 assertEquals("10-11-2015 12:30:45.678 +02:00", formatter.print(mutableDateTime));
 }

 @Test
 public void testParseWithInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 try {
 formatter.parseMillis("10-11-2015 12:30:45.678");
 fail("Should have thrown IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithInvalidLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 try {
 formatter.parseLocalDateTime("10-11-2015 12:30:45.678", Locale.CHINESE);
 fail("Should have thrown IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseWithInvalidZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser("dd-MM-yyyy HH:mm:ss.SSS z", Locale.US), null);
 try {
 formatter.parseLocalDateTime("10-11-2015 12:30:45.678", DateTimeZone.forOffsetHours(-5));
 fail("Should have thrown IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }
}