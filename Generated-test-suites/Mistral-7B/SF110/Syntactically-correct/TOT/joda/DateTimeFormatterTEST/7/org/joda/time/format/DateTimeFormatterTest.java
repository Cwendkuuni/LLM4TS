package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter()
 .withLocale(Locale.US)
 .withOffsetParsed();
 }

 @Test
 public void testPrinter() {
 final DateTimeFormatter printerFormatter = new DateTimeFormatter();
 assertTrue(printerFormatter.isPrinter());
 assertNull(printerFormatter.getParser());
 }

 @Test
 public void testParser() {
 final DateTimeFormatter parserFormatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
 assertTrue(parserFormatter.isParser());
 assertNull(parserFormatter.getPrinter());
 }

 @Test
 public void testWithLocale() {
 final DateTimeFormatter originalFormatter = formatter;
 final DateTimeFormatter localFormatter = originalFormatter.withLocale(Locale.UK);
 assertNotSame(originalFormatter, localFormatter);
 assertEquals(Locale.UK, localFormatter.getLocale());
 }

 @Test
 public void testIsOffsetParsed() {
 final DateTimeFormatter offsetParsedFormatter = new DateTimeFormatter().withOffsetParsed();
 assertTrue(offsetParsedFormatter.isOffsetParsed());
 final DateTimeFormatter notOffsetParsedFormatter = new DateTimeFormatter();
 assertFalse(notOffsetParsedFormatter.isOffsetParsed());
 }

 @Test
 public void testPrintTo() {
 final Instant instant = new DateTime(2019, 11, 30, 15, 30, 0, 0).toInstant();
 final StringBuffer sb = new StringBuffer(32);
 formatter.printTo(sb, instant);
 assertEquals("2019-11-30 15:30:00.000", sb.toString());
 }

 @Test
 public void testPrintToWithZone() {
 final Instant instant = new DateTime(2019, 11, 30, 15, 30, 0, 0).toInstant();
 final DateTimeZone zone = DateTimeZone.forID("Europe/London");
 formatter = formatter.withZone(zone);
 final String expected = "2019-11-30 15:30:00.000Z";
 final StringBuffer sb = new StringBuffer(expected.length());
 formatter.printTo(sb, instant);
 assertEquals(expected, sb.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintToWithNull() {
 formatter.printTo(null, new Instant(System.currentTimeMillis()));
 }

 @Test
 public void testPrintToWithNullInstant() {
 final StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, null);
 assertEquals("", sb.toString());
 }

 @Test
 public void testParseMillis() {
 final String dateString = "2019-11-30 15:30:00.000";
 final long parsedMillis = formatter.parseMillis(dateString);
 final Instant expectedInstant = new DateTime(2019, 11, 30, 15, 30, 0, 0).toInstant();
 assertEquals(expectedInstant.getMillis(), parsedMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisWithInvalidFormat() {
 formatter.parseMillis("InvalidFormat");
 }

 @Test
 public void testParseLocalDate() {
 final String dateString = "2019-11-30";
 final LocalDate parsedLocalDate = formatter.parseLocalDate(dateString);
 assertEquals(new LocalDate(2019, 11, 30), parsedLocalDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateWithInvalidFormat() {
 formatter.parseLocalDate("InvalidFormat");
 }

 @Test
 public void testParseLocalTime() {
 final String timeString = "15:30:00.000";
 final LocalTime parsedLocalTime = formatter.parseLocalTime(timeString);
 assertEquals(new LocalTime(15, 30, 0), parsedLocalTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeWithInvalidFormat() {
 formatter.parseLocalTime("InvalidFormat");
 }

 @Test
 public void testParseLocalDateTime() {
 final String dateTimeString = "2019-11-30 15:30:00.000";
 final LocalDateTime parsedLocalDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(new LocalDateTime(2019, 11, 30, 15, 30, 0), parsedLocalDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeWithInvalidFormat() {
 formatter.parseLocalDateTime("InvalidFormat");
 }

 @Test
 public void testParseDateTime() {
 final String dateTimeString = "2019-11-30T15:30:00.000Z";
 final DateTime parsedDateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(new DateTime(2019, 11, 30, 15, 30, 0, 0).toInstant(), parsedDateTime.toInstant());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeWithInvalidFormat() {
 formatter.parseDateTime("InvalidFormat");
 }

 @Test
 public void testParseMutableDateTime() {
 final String dateTimeString = "2019-11-30T15:30:00.000Z";
 final MutableDateTime parsedMutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(new DateTime(2019, 11, 30, 15, 30, 0, 0).toInstant(), parsedMutableDateTime.toInstant());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeWithInvalidFormat() {
 formatter.parseMutableDateTime("InvalidFormat");
 }
}