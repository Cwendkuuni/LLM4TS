package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @Test
 public void testNewInstance() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
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
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), Locale.FRANCE);
 assertEquals(false, formatter.isPrinter());
 assertEquals(false, formatter.isParser());
 assertEquals(Locale.FRANCE, formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testNewInstanceWithAllParams() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser(), Locale.FRANCE, true, GregorianChronology.INSTANCE, DateTimeZone.UTC, Integer.valueOf(2010), 2005);
 assertEquals(false, formatter.isPrinter());
 assertEquals(false, formatter.isParser());
 assertEquals(Locale.FRANCE, formatter.getLocale());
 assertTrue(formatter.isOffsetParsed());
 assertEquals(GregorianChronology.INSTANCE, formatter.getChronology());
 assertEquals(DateTimeZone.UTC, formatter.getZone());
 assertEquals(Integer.valueOf(2010), formatter.getPivotYear());
 assertEquals(2005, formatter.getDefaultYear());
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 assertTrue(formatter.isPrinter());
 formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 assertFalse(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 assertTrue(formatter.isParser());
 formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 assertFalse(formatter.isParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.FRANCE);
 assertNotSame(formatter, formatterWithLocale);
 assertEquals(Locale.FRANCE, formatterWithLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertNotSame(formatter, formatterWithOffsetParsed);
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithChronology = formatter.withChronology(GregorianChronology.INSTANCE);
 assertNotSame(formatter, formatterWithChronology);
 assertEquals(GregorianChronology.INSTANCE, formatterWithChronology.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
 assertNotSame(formatter, formatterWithZoneUTC);
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(-2));
 assertNotSame(formatter, formatterWithZone);
 assertEquals(DateTimeZone.forOffsetHours(-2), formatterWithZone.getZone());
 }

 @Test
 public void testWithPivotYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2010);
 assertNotSame(formatter, formatterWithPivotYear);
 assertEquals(Integer.valueOf(2010), formatterWithPivotYear.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2008);
 assertNotSame(formatter, formatterWithDefaultYear);
 assertEquals(2008, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintTo() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 StringBuffer sb = new StringBuffer();
 LocalDateTime dateTime = LocalDateTime.of(2010, 1, 1, 12, 30);
 formatter.printTo(sb, dateTime.toInstant());
 assertEquals("2010-01-01T12:30:00.000", sb.toString());
 }

 @Test
 public void testPrintToWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 StringBuffer sb = new StringBuffer();
 ReadableInstant readableInstant = Instant.now();
 formatter = formatter.withChronology(JulianChronology.INSTANCE);
 formatter.printTo(sb, readableInstant);
 assertEquals("CCCC-MM-DD HH:mm:ss.SSS", sb.toString());
 }

 @Test
 public void testPrintToWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
 StringBuffer sb = new StringBuffer();
 ReadableInstant readableInstant = Instant.now();
 formatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 formatter.printTo(sb, readableInstant);
 assertEquals("2014-12-31 15:59:59.999+02:00", sb.toString());
 }

 @Test
 public void testParseInto() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 MutableDateTime mutableDateTime = new MutableDateTime();
 int result = formatter.parseInto(mutableDateTime, "2010-12-31 23:59:59");
 assertEquals(16, result);
 assertEquals(2010, mutableDateTime.getYear());
 assertEquals(12, mutableDateTime.getMonthOfYear());
 assertEquals(31, mutableDateTime.getDayOfMonth());
 assertEquals(23, mutableDateTime.getHourOfDay());
 assertEquals(59, mutableDateTime.getMinuteOfHour());
 assertEquals(59, mutableDateTime.getSecondOfMinute());
 assertEquals(0, mutableDateTime.getMillisOfSecond());
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 long result = formatter.parseMillis("2010-12-31 23:59:59");
 assertEquals(126230239959L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisWithInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 formatter.parseMillis("InvalidFormat");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntoWithInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeParser(), new SimpleDateTimePrinter());
 MutableDateTime mutableDateTime = new MutableDateTime();
 formatter.parseInto(mutableDateTime, "InvalidFormat");
 }
}