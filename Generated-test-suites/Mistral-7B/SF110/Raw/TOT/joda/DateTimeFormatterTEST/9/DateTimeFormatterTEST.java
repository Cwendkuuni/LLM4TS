package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;

import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @BeforeClass
 public static void setup() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 }

 @Test
 public void testIsPrinter() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertTrue(formatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 assertTrue(formatter.isParser());
 }

 @Test
 public void testWithLocale() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 Locale locale = Locale.CHINESE;
 DateTimeFormatter newFormatter = formatter.withLocale(locale);
 assertEquals(locale, newFormatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTimeFormatter newFormatter = formatter.withOffsetParsed();
 assertTrue(newFormatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 Chronology chronology = GregorianChronology.getInstance();
 DateTimeFormatter newFormatter = formatter.withChronology(chronology);
 assertEquals(chronology, newFormatter.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTimeFormatter newFormatter = formatter.withZoneUTC();
 assertEquals(DateTimeZone.UTC, newFormatter.getZone());
 }

 @Test
 public void testWithZone() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTimeZone zone = DateTimeZone.forID("America/New_York");
 DateTimeFormatter newFormatter = formatter.withZone(zone);
 assertEquals(zone, newFormatter.getZone());
 }

 @Test
 public void testPrintToStringBuffer() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC);
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, dateTime);
 String expected = "2009-06-11 15:35:50.000";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC);
 StringWriter sw = new StringWriter();
 formatter.printTo(sw, dateTime);
 String expected = "2009-06-11 15:35:50.000";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC);
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, dateTime);
 String expected = "2009-06-11 15:35:50.000";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToStringBufferWithChronology() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC, GregorianChronology.getInstance());
 StringBuffer sb = new StringBuffer();
 formatter.printTo(sb, dateTime);
 String expected = "2009-06-11";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testPrintToWriterWithChronology() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC, GregorianChronology.getInstance());
 StringWriter sw = new StringWriter();
 formatter.printTo(sw, dateTime);
 String expected = "2009-06-11\n";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testPrintToAppendableWithChronology() throws IOException {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 DateTime dateTime = new DateTime(123456789000L, DateTimeZone.UTC, GregorianChronology.getInstance());
 StringWriter sw = new StringWriter();
 Appendable appendable = sw;
 formatter.printTo(appendable, dateTime);
 String expected = "2009-06-11\n";
 assertEquals(expected, sw.toString());
 }

 @Test
 public void testParseMillis() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateString = "2009-06-11 15:35:50.000";
 long millis = formatter.parseMillis(dateString);
 assertEquals(123456789000L, millis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateString = "InvalidFormat";
 formatter.parseMillis(dateString);
 }

 @Test
 public void testParseLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateString = "2009-06-11";
 LocalDate localDate = formatter.parseLocalDate(dateString);
 assertEquals(new LocalDate(2009, 6, 11), localDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateString = "InvalidFormat";
 formatter.parseLocalDate(dateString);
 }

 @Test
 public void testParseLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String timeString = "15:35:50";
 LocalTime localTime = formatter.parseLocalTime(timeString);
 assertEquals(new LocalTime(15, 35, 50), localTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String timeString = "InvalidFormat";
 formatter.parseLocalTime(timeString);
 }

 @Test
 public void testParseLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "2009-06-11 15:35:50";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(new LocalDateTime(2009, 6, 11, 15, 35, 50), localDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLocalDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "InvalidFormat";
 formatter.parseLocalDateTime(dateTimeString);
 }

 @Test
 public void testParseDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "2009-06-11 15:35:50.000";
 DateTime dateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(new DateTime(123456789000L), dateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "InvalidFormat";
 formatter.parseDateTime(dateTimeString);
 }

 @Test
 public void testParseMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "2009-06-11 15:35:50.000";
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(new MutableDateTime(123456789000L), mutableDateTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMutableDateTimeInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 String dateTimeString = "InvalidFormat";
 formatter.parseMutableDateTime(dateTimeString);
 }

 @Test
 public void testParseIntoReadWritableInstant() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 ReadWritableInstant readWritableInstant = new DateTime();
 String dateString = "2009-06-11 15:35:50.000";
 int into = formatter.parseInto(readWritableInstant, dateString, 0);
 assertEquals(dateString.length(), into);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntoReadWritableInstantInvalidFormat() {
 DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
 ReadWritableInstant readWritableInstant = new DateTime();
 String dateString = "InvalidFormat";
 formatter.parseInto(readWritableInstant, dateString, 0);
 }
}