package org.joda.time.format.test;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter formatterWithLocale;
 private DateTimeFormatter formatterWithOffsetParsed;
 private DateTimeFormatter formatterWithChronology;
 private DateTimeFormatter formatterWithZoneUTC;
 private DateTimeFormatter formatterWithZone;
 private DateTimeFormatter formatterWithPivotYear;
 private DateTimeFormatter formatterWithDefaultYear;

 @Test
 public void testIsPrinter() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 formatter = new DateTimeFormatter(null, new SimpleDateFormatDateTimeParser());
 assertTrue(formatter.isParser());
 }

 @Test
 public void testGetPrinter() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 assertSame(new SimpleDateFormatDateTimePrinter(), formatter.getPrinter());
 }

 @Test
 public void testGetParser() {
 formatter = new DateTimeFormatter(null, new SimpleDateFormatDateTimeParser());
 assertSame(new SimpleDateFormatDateTimeParser(), formatter.getParser());
 }

 @Test
 public void testWithLocale() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithLocale = formatter.withLocale(Locale.US);
 assertNotSame(formatter, formatterWithLocale);
 }

 @Test
 public void testGetLocale() {
 formatterWithLocale = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withLocale(Locale.US);
 assertEquals(Locale.US, formatterWithLocale.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertNotSame(formatter, formatterWithOffsetParsed);
 assertFalse(formatter.isOffsetParsed());
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testIsOffsetParsed() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 assertFalse(formatter.isOffsetParsed());
 formatterWithOffsetParsed = formatter.withOffsetParsed();
 assertTrue(formatterWithOffsetParsed.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithChronology = formatter.withChronology(ISOChronology.getInstance());
 assertNotSame(formatter, formatterWithChronology);
 }

 @Test
 public void testGetChronology() {
 formatterWithChronology = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withChronology(ISOChronology.getInstance());
 assertEquals(ISOChronology.getInstance(), formatterWithChronology.getChronology());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testWithChronolgyDeprecated() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithChronology = formatter.withChronolgy(ISOChronology.getInstance());
 }

 @Test
 public void testWithZoneUTC() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithZoneUTC = formatter.withZoneUTC();
 assertNotSame(formatter, formatterWithZoneUTC);
 }

 @Test
 public void testGetZone() {
 formatterWithZoneUTC = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withZoneUTC();
 assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
 }

 @Test
 public void testWithZone() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(2));
 assertNotSame(formatter, formatterWithZone);
 }

 @Test
 public void testGetZoneDefault() {
 formatterWithZone = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withZone(DateTimeZone.UTC);
 assertNull(formatterWithZone.getZone());
 }

 @Test
 public void testWithPivotYear() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithPivotYear = formatter.withPivotYear(2022);
 assertNotSame(formatter, formatterWithPivotYear);
 }

 @Test
 public void testGetPivotYear() {
 formatterWithPivotYear = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withPivotYear(2022);
 assertEquals(Integer.valueOf(2022), formatterWithPivotYear.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatterWithDefaultYear = formatter.withDefaultYear(2022);
 assertNotSame(formatter, formatterWithDefaultYear);
 }

 @Test
 public void testGetDefaultYear() {
 formatterWithDefaultYear = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null).withDefaultYear(2022);
 assertEquals(2022, formatterWithDefaultYear.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC);
 formatter.printTo(new StringBuffer(), localDateTime);
 String expected = "2022-01-01T12:30:20.000000Z";
 assertEquals(expected, formatter.print(localDateTime));
 }

 @Test
 public void testPrintToWriter() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC);
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, localDateTime);
 String expected = "2022-01-01T12:30:20.000000Z";
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC);
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, localDateTime);
 String expected = "2022-01-01T12:30:20.000000Z";
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToStringBufferWithInstant() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 Instant instant = new Instant(1648286000000L);
 formatter.printTo(new StringBuffer(), instant);
 String expected = "2022-01-01T12:00:00.000Z";
 assertEquals(expected, formatter.print(instant));
 }

 @Test
 public void testPrintToWriterWithInstant() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 Instant instant = new Instant(1648286000000L);
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, instant);
 String expected = "2022-01-01T12:00:00.000Z";
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendableWithInstant() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 Instant instant = new Instant(1648286000000L);
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, instant);
 String expected = "2022-01-01T12:00:00.000Z";
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToStringBufferWithReadablePartial() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC).toLocalDateTime();
 formatter.printTo(new StringBuffer(), readablePartial);
 String expected = "12:30:20";
 assertEquals(expected, formatter.print(readablePartial));
 }

 @Test
 public void testPrintToWriterWithReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC).toLocalDateTime();
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, readablePartial);
 String expected = "12:30:20";
 assertEquals(expected, stringWriter.toString());
 }

 @Test
 public void testPrintToAppendableWithReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 ReadablePartial readablePartial = new DateTime(2022, 1, 1, 12, 30, 20, 0, DateTimeUtils.UTC).toLocalDateTime();
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, readablePartial);
 String expected = "12:30:20";
 assertEquals(expected, stringBuffer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintToStringBufferWithNullReadablePartial() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 formatter.printTo(new StringBuffer(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintToWriterWithNullReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 StringWriter stringWriter = new StringWriter();
 formatter.printTo(stringWriter, null);
 }

 @Test(expected = IOException.class)
 public void testPrintToAppendableWithNullReadablePartial() throws IOException {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), null);
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, null);
 }

 @Test
 public void testParseIntoInstant() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 Instant instant = Instant.now();
 String dateTimeString = formatter.print(instant);
 formatter.parseInto(DateTimeUtils.forDate(new Date()), dateTimeString, 0);
 Instant parsedInstant = DateTimeUtils.getInstant(DateTimeUtils.toLocalDateTime(DateTimeUtils.forDate(new Date())));
 assertEquals(instant.getMillis(), parsedInstant.getMillis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntoWithNullReadableInstant() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 formatter.parseInto(DateTimeUtils.forDate(new Date()), null, 0);
 }

 @Test
 public void testParseMillis() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 long millis = System.currentTimeMillis();
 String dateTimeString = formatter.print(new Instant(millis));
 long parsedMillis = formatter.parseMillis(dateTimeString);
 assertEquals(millis, parsedMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisWithInvalidDateTimeString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidDateTimeString = "InvalidDateTime";
 formatter.parseMillis(invalidDateTimeString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseLocalDate() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String dateString = "2022-01-01";
 LocalDate localDate = formatter.parseLocalDate(dateString);
 assertEquals(new LocalDate(2022, 1, 1), localDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateWithInvalidDateString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidDateString = "InvalidDate";
 formatter.parseLocalDate(invalidDateString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseLocalTime() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String timeString = "12:30:20";
 LocalTime localTime = formatter.parseLocalTime(timeString);
 assertEquals(new LocalTime(12, 30, 20), localTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeWithInvalidTimeString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidTimeString = "InvalidTime";
 formatter.parseLocalTime(invalidTimeString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseLocalDateTime() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String dateTimeString = "2022-01-01T12:30:20";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(new LocalDateTime(2022, 1, 1, 12, 30, 20), localDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeWithInvalidDateTimeString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidDateTimeString = "InvalidDateTime";
 formatter.parseLocalDateTime(invalidDateTimeString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseDateTime() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String dateTimeString = "2022-01-01T12:30:20.123";
 DateTime parsedDateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(new DateTime(2022, 1, 1, 12, 30, 20, 123), parsedDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeWithInvalidDateTimeString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidDateTimeString = "InvalidDateTime";
 formatter.parseDateTime(invalidDateTimeString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseMutableDateTime() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String dateTimeString = "2022-01-01T12:30:20.123";
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(new DateTime(2022, 1, 1, 12, 30, 20, 123), mutableDateTime.toDateTime());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeWithInvalidDateTimeString() {
 formatter = new DateTimeFormatter(new SimpleDateFormatDateTimeParser(), null);
 String invalidDateTimeString = "InvalidDateTime";
 formatter.parseMutableDateTime(invalidDateTimeString);
 fail("Expected IllegalArgumentException");
 }
}