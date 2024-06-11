package org.joda.time.format.test;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter createFormatter() {
 return new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser());
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = createFormatter();
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = createFormatter();
 assertTrue(formatter.isParser());
 }

 @Test
 public void testCreateWithNullArguments() {
 assertNull(new DateTimeFormatter(null, null));
 }

 @Test
 public void testCreateWithArguments() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new BasicDateTimeParser(), Locale.ENGLISH, false, GregorianChronology.INSTANCE, null, null, 2000);
 assertEquals(Locale.ENGLISH, formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChronology());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertNotSame(2000, formatter.getDefaultYear());
 }

 @Test
 public void testCreateWithLocale() {
 DateTimeFormatter formatter = createFormatter().withLocale(Locale.FRENCH);
 assertEquals(Locale.FRENCH, formatter.getLocale());
 }

 @Test
 public void testCreateWithOffsetParsed() {
 DateTimeFormatter formatter = createFormatter().withOffsetParsed();
 assertTrue(formatter.isOffsetParsed());
 assertNull(formatter.getZone());
 }

 @Test
 public void testCreateWithChronology() {
 DateTimeFormatter formatter = createFormatter().withChronology(ISODateTimeFormat.dateTimeParser());
 assertNotNull(formatter.getChronology());
 }

 @Test
 public void testCreateWithZoneUTC() {
 DateTimeFormatter formatter = createFormatter().withZoneUTC();
 assertSame(DateTimeZone.UTC, formatter.getZone());
 }

 @Test
 public void testCreateWithZone() {
 DateTimeFormatter formatter = createFormatter().withZone(DateTimeZone.forOffsetHours(1));
 assertSame(DateTimeZone.forOffsetHours(1), formatter.getZone());
 }

 @Test
 public void testCreateWithPivotYear() {
 DateTimeFormatter formatter = createFormatter().withPivotYear(2022);
 assertEquals(2022, formatter.getPivotYear().intValue());
 }

 @Test
 public void testCreateWithDefaultYear() {
 DateTimeFormatter formatter = createFormatter().withDefaultYear(2100);
 assertNotSame(2100, formatter.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 DateTimeFormatter formatter = createFormatter();
 Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
 StringBuffer buffer = new StringBuffer();
 formatter.printTo(buffer, instant);
 assertEquals("2022-01-01T12:00:00.000Z", buffer.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 DateTimeFormatter formatter = createFormatter();
 Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
 StringWriter writer = new StringWriter();
 formatter.printTo(writer, instant);
 assertEquals("2022-01-01T12:00:00.000Z", writer.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 DateTimeFormatter formatter = createFormatter();
 Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
 StringWriter writer = new StringWriter();
 Appendable appendable = writer;
 formatter.printTo(appendable, instant);
 assertEquals("2022-01-01T12:00:00.000Z", writer.toString());
 }

 @Test
 public void testPrintToStringBufferWithInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 StringBuffer buffer = new StringBuffer();
 try {
 formatter.printTo(buffer, "invalid format");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Text 'invalid format' cannot be parsed at index 0", e.getMessage());
 }
 }

 @Test
 public void testPrintToWriterWithInvalidFormat() throws IOException {
 DateTimeFormatter formatter = createFormatter();
 StringWriter writer = new StringWriter();
 try {
 formatter.printTo(writer, "invalid format");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Text 'invalid format' cannot be parsed at index 0", e.getMessage());
 }
 }

 @Test
 public void testPrintToAppendableWithInvalidFormat() throws IOException {
 DateTimeFormatter formatter = createFormatter();
 StringWriter writer = new StringWriter();
 Appendable appendable = writer;
 try {
 formatter.printTo(appendable, "invalid format");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Text 'invalid format' cannot be parsed at index 0", e.getMessage());
 }
 }

 @Test
 public void testParseIntoInstant() {
 DateTimeFormatter formatter = createFormatter();
 Instant instant = Instant.now();
 String dateTimeString = "2022-01-01T12:00:00.123Z";
 int parseInt = formatter.parseInto(instant, dateTimeString, 0);
 assertEquals(dateTimeString.length(), parseInt);
 assertEquals("2022-01-01T12:00:00.123Z", formatter.print(instant));
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = createFormatter();
 long millis = System.currentTimeMillis();
 String dateTimeString = "2022-01-01T12:00:00.123Z";
 long parsedMillis = formatter.parseMillis(dateTimeString);
 assertEquals(millis + 123000L, parsedMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseMillis(invalidFormat);
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = createFormatter();
 LocalDate localDate = LocalDate.now();
 String dateString = "2022-01-01";
 LocalDate parsedLocalDate = formatter.parseLocalDate(dateString);
 assertEquals(localDate, parsedLocalDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseLocalDate(invalidFormat);
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = createFormatter();
 LocalTime localTime = LocalTime.now();
 String timeString = "12:00:00";
 LocalTime parsedLocalTime = formatter.parseLocalTime(timeString);
 assertEquals(localTime, parsedLocalTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseLocalTime(invalidFormat);
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = createFormatter();
 LocalDateTime localDateTime = LocalDateTime.now();
 String dateTimeString = "2022-01-01T12:00:00";
 LocalDateTime parsedLocalDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(localDateTime, parsedLocalDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseLocalDateTime(invalidFormat);
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = createFormatter();
 DateTime parsedDateTime = DateTime.now();
 String dateTimeString = "2022-01-01T12:00:00.123Z";
 DateTime parsedParsedDateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(parsedDateTime, parsedParsedDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseDateTime(invalidFormat);
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = createFormatter();
 MutableDateTime mutableDateTime = new MutableDateTime();
 String dateTimeString = "2022-01-01T12:00:00.123Z";
 MutableDateTime parsedMutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(mutableDateTime, parsedMutableDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeInvalidFormat() {
 DateTimeFormatter formatter = createFormatter();
 String invalidFormat = "invalid format";
 formatter.parseMutableDateTime(invalidFormat);
 }
}