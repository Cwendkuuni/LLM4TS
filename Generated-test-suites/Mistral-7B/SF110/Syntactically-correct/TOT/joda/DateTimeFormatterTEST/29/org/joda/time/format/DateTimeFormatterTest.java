package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @Test
 public void testDefaultInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter();
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
 public void testWithPrinterAndParser() {
 DateTimePrinter printer = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeParser parser = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 assertTrue(formatter.isPrinter());
 assertTrue(formatter.isParser());
 assertSame(printer, formatter.getPrinter());
 assertSame(parser, formatter.getParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testWithLocale() {
 DateTimePrinter printer = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeParser parser = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 Locale locale = Locale.US;
 DateTimeFormatter formatterWithLocale = formatter.withLocale(locale);
 assertEquals(locale, formatterWithLocale.getLocale());
 assertSame(printer, formatterWithLocale.getPrinter());
 assertSame(parser, formatterWithLocale.getParser());
 assertSame(formatter, formatterWithLocale.withLocale(locale));
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimePrinter printer = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeParser parser = new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z");
 DateTimeFormatter formatter = new DateTimeFormatter(printer, parser);
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 assertFalse(formatter.isOffsetParsed());
 assertSame(printer, formatterWithOffsetParsed.getPrinter());
 assertSame(parser, formatterWithOffsetParsed.getParser());
 assertNull(formatterWithOffsetParsed.getLocale());
 assertSame(formatter, formatterWithOffsetParsed.withOffsetParsed());
 }

 @Test
 public void testPrinting() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z"));
 Instant instant = new DateTime(2022, 1, 1, 12, 30, 45, 123, 0, DateTimeZone.forOffsetHours(-5));
 try {
 String formatted = formatter.print(instant);
 assertEquals("01-01-2022 17:30:45.123-05:00", formatted);
 }
 catch (IOException e) {
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testParsing() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z"));
 String dateString = "01-01-2022 17:30:45.123-05:00";
 long millis = formatter.parseMillis(dateString);
 Instant instant = new Instant(millis);
 DateTimeZone zone = DateTimeZone.forOffsetHours(-5);
 assertEquals(instant.getMillis(), millis);
 assertEquals(2022, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getYear());
 assertEquals(1, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getMonthOfYear());
 assertEquals(1, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getDayOfMonth());
 assertEquals(12, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getHourOfDay());
 assertEquals(30, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getMinuteOfHour());
 assertEquals(45, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getSecondOfMinute());
 assertEquals(123, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getMillisOfSecond());
 assertEquals(zone, new DateTime(millis, DateTimeUtils.getChronology(DateTimeZone.UTC)).getZone());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFailed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS Z"));
 String dateString = "XX-XX-XXXX XX:XX:XX.XXX-XX:XX";
 formatter.parseMillis(dateString);
 fail("IllegalArgumentException should be thrown");
 }
}