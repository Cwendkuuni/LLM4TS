package org.joda.time.format;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter printerFormatter;
 private DateTimeFormatter parserFormatter;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter()
 .withLocale(Locale.US)
 .withOffsetParsed()
 .withChronology(ISOChronology.getInstance())
 .withZoneUTC();
 printerFormatter = formatter.getPrinter();
 parserFormatter = formatter.getParser();
 }

 @Test
 public void testPrintToString() {
 final Instant instant = Instant.now();
 String expected = printerFormatter.print(instant);
 String actual = formatter.print(instant).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPrintToWriter() throws IOException {
 final Instant instant = Instant.now();
 StringWriter writer = new StringWriter();
 formatter.printTo(writer, instant);
 String expected = printerFormatter.print(instant);
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 final Instant instant = Instant.now();
 StringBuilder sb = new StringBuilder();
 formatter.printTo(sb, instant);
 String expected = printerFormatter.print(instant);
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testPrintToLong() {
 final Instant instant = Instant.now();
 long expected = printerFormatter.printToBuffer(new StringBuffer(10), instant).length();
 long actual = formatter.printTo(new StringBuffer(10), instant).length();
 assertEquals(expected, actual);
 }

 @Test
 public void testPrintToReadableInstant() {
 final Instant instant = Instant.now();
 String expected = printerFormatter.print(instant);
 String actual = formatter.print(instant).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMillis() {
 final String dateString = "2012-05-01T12:30:45.123Z";
 long expected = 1336412245123L;
 long actual = formatter.parseMillis(dateString);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 final String dateString = "InvalidFormat";
 formatter.parseMillis(dateString);
 }

 @Test
 public void testParseLocalDate() {
 final String dateString = "2012-05-01";
 LocalDate expected = new LocalDate(2012, 5, 1);
 LocalDate actual = formatter.parseLocalDate(dateString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalTime() {
 final String timeString = "12:30:45.123";
 LocalTime expected = new LocalTime(12, 30, 45, 123);
 LocalTime actual = formatter.parseLocalTime(timeString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLocalDateTime() {
 final String datetimeString = "2012-05-01T12:30:45.123Z";
 LocalDateTime expected = new LocalDateTime(2012, 5, 1, 12, 30, 45, 123);
 LocalDateTime actual = formatter.parseLocalDateTime(datetimeString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDateTime() {
 final String datetimeString = "2012-05-01T12:30:45.123Z";
 DateTime expected = new DateTime(2012, 5, 1, 12, 30, 45, 123);
 DateTime actual = formatter.parseDateTime(datetimeString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseMutableDateTime() {
 final String datetimeString = "2012-05-01T12:30:45.123Z";
 MutableDateTime expected = new MutableDateTime(2012, 5, 1, 12, 30, 45, 123);
 MutableDateTime actual = formatter.parseMutableDateTime(datetimeString);
 assertEquals(expected, actual);
 }
}