package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

 private DateTimeFormatter formatter;
 private DateTimeFormatter withLocaleFormatter;
 private DateTimeFormatter withOffsetParsedFormatter;
 private DateTimeFormatter withZoneUTCFormatter;
 private DateTimeFormatter withZoneFormatter;
 private DateTimeFormatter withPivotYearFormatter;
 private DateTimeFormatter withDefaultYearFormatter;

 @Before
 public void setUp() {
 formatter = new DateTimeFormatter()
 .withPrinter()
 .withParser();
 withLocaleFormatter = formatter.withLocale(Locale.US);
 withOffsetParsedFormatter = formatter.withOffsetParsed();
 withZoneUTCFormatter = formatter.withZoneUTC();
 withZoneFormatter = formatter.withZone(DateTimeZone.forOffsetHours(2));
 withPivotYearFormatter = formatter.withPivotYear(2020);
 withDefaultYearFormatter = formatter.withDefaultYear(2021);
 }

 @Test
 public void testIsPrinter() {
 assertTrue(formatter.isPrinter());
 assertTrue(withLocaleFormatter.isPrinter());
 assertTrue(withOffsetParsedFormatter.isPrinter());
 assertTrue(withZoneUTCFormatter.isPrinter());
 assertTrue(withZoneFormatter.isPrinter());
 assertTrue(withPivotYearFormatter.isPrinter());
 assertTrue(withDefaultYearFormatter.isPrinter());
 }

 @Test
 public void testIsParser() {
 assertTrue(formatter.isParser());
 assertTrue(withLocaleFormatter.isParser());
 assertTrue(withOffsetParsedFormatter.isParser());
 assertTrue(withZoneUTCFormatter.isParser());
 assertTrue(withZoneFormatter.isParser());
 assertTrue(withPivotYearFormatter.isParser());
 assertTrue(withDefaultYearFormatter.isParser());
 }

 @Test
 public void testWithLocale() {
 assertEquals(Locale.getDefault(), formatter.getLocale());
 assertEquals(Locale.US, withLocaleFormatter.getLocale());
 }

 @Test
 public void testWithOffsetParsed() {
 assertFalse(formatter.isOffsetParsed());
 assertTrue(withOffsetParsedFormatter.isOffsetParsed());
 }

 @Test
 public void testWithChronology() {
 assertEquals(DateTimeUtils.getLocalChronology(), formatter.getChronology());
 assertEquals(DateTimeUtils.getLocalChronology(), withLocaleFormatter.getChronology());
 assertEquals(DateTimeUtils.UTC_INSTANT, withZoneUTCFormatter.getChronology());
 }

 @Test
 public void testWithZoneUTC() {
 assertEquals(DateTimeZone.UTC, withZoneUTCFormatter.getZone());
 }

 @Test
 public void testWithZone() {
 assertEquals(DateTimeZone.getDefault(), formatter.getZone());
 assertEquals(DateTimeZone.forOffsetHours(2), withZoneFormatter.getZone());
 }

 @Test
 public void testWithPivotYear() {
 assertNull(formatter.getPivotYear());
 assertEquals(2020, withPivotYearFormatter.getPivotYear());
 }

 @Test
 public void testWithDefaultYear() {
 assertEquals(2000, formatter.getDefaultYear());
 assertEquals(2021, withDefaultYearFormatter.getDefaultYear());
 }

 @Test
 public void testPrintToStringBuffer() {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z";
 StringBuffer stringBuffer = new StringBuffer();
 formatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriter() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z\n";
 Writer writer = new StringWriter();
 formatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendable() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z";
 Appendable appendable = new StringBuffer();
 formatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithLocale() {
 Instant instant = new Instant(1516044423000L);
 Locale locale = new Locale("en", "US");
 String expected = "2018-02-24T11:40:23.000-05:00";
 StringBuffer stringBuffer = new StringBuffer();
 withLocaleFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithLocale() throws IOException {
 Instant instant = new Instant(1516044423000L);
 Locale locale = new Locale("en", "US");
 String expected = "2018-02-24T11:40:23.000-05:00\n";
 Writer writer = new StringWriter();
 withLocaleFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithLocale() throws IOException {
 Instant instant = new Instant(1516044423000L);
 Locale locale = new Locale("en", "US");
 String expected = "2018-02-24T11:40:23.000-05:00";
 Appendable appendable = new StringBuffer();
 withLocaleFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithOffsetParsed() {
 Instant instant = new Instant(1516044423000L + 3600 * 1000);
 String expected = "2018-02-24T12:40:23.000+01:00";
 StringBuffer stringBuffer = new StringBuffer();
 withOffsetParsedFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithOffsetParsed() throws IOException {
 Instant instant = new Instant(1516044423000L + 3600 * 1000);
 String expected = "2018-02-24T12:40:23.000+01:00\n";
 Writer writer = new StringWriter();
 withOffsetParsedFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithOffsetParsed() throws IOException {
 Instant instant = new Instant(1516044423000L + 3600 * 1000);
 String expected = "2018-02-24T12:40:23.000+01:00";
 Appendable appendable = new StringBuffer();
 withOffsetParsedFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithZoneUTC() {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z";
 StringBuffer stringBuffer = new StringBuffer();
 withZoneUTCFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithZoneUTC() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z\n";
 Writer writer = new StringWriter();
 withZoneUTCFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithZoneUTC() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:40:23.000Z";
 Appendable appendable = new StringBuffer();
 withZoneUTCFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithZone() {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:42:23.000-02:00";
 StringBuffer stringBuffer = new StringBuffer();
 withZoneFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithZone() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:42:23.000-02:00\n";
 Writer writer = new StringWriter();
 withZoneFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithZone() throws IOException {
 Instant instant = new Instant(1516044423000L);
 String expected = "2018-02-24T11:42:23.000-02:00";
 Appendable appendable = new StringBuffer();
 withZoneFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithPivotYear() {
 Instant instant = new Instant(1556512000000L);
 String expected = "2025-01-01T00:00:00.000+00:00";
 StringBuffer stringBuffer = new StringBuffer();
 withPivotYearFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithPivotYear() throws IOException {
 Instant instant = new Instant(1556512000000L);
 String expected = "2025-01-01T00:00:00.000+00:00\n";
 Writer writer = new StringWriter();
 withPivotYearFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithPivotYear() throws IOException {
 Instant instant = new Instant(1556512000000L);
 String expected = "2025-01-01T00:00:00.000+00:00";
 Appendable appendable = new StringBuffer();
 withPivotYearFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testPrintToStringBufferWithDefaultYear() {
 Instant instant = new Instant(1556512000000L);
 String expected = "2021-01-01T00:00:00.000+00:00";
 StringBuffer stringBuffer = new StringBuffer();
 withDefaultYearFormatter.printTo(stringBuffer, instant);
 assertEquals(expected, stringBuffer.toString());
 }

 @Test
 public void testPrintToWriterWithDefaultYear() throws IOException {
 Instant instant = new Instant(1556512000000L);
 String expected = "2021-01-01T00:00:00.000+00:00\n";
 Writer writer = new StringWriter();
 withDefaultYearFormatter.printTo(writer, instant);
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testPrintToAppendableWithDefaultYear() throws IOException {
 Instant instant = new Instant(1556512000000L);
 String expected = "2021-01-01T00:00:00.000+00:00";
 Appendable appendable = new StringBuffer();
 withDefaultYearFormatter.printTo(appendable, instant);
 assertEquals(expected, appendable.toString());
 }

 @Test
 public void testParseIntoReadWritableInstant() {
 Instant instant = new Instant(1516044423000L);
 String dateTimeString = "2018-02-24T11:40:23.000Z";
 ReadWritableInstant readWritableInstant = new DateTime().parse(dateTimeString, formatter);
 assertEquals(instant, readWritableInstant.getInstant());
 assertEquals(instant.getMillis(), readWritableInstant.getMillis());
 assertEquals(instant.getChronology(), readWritableInstant.getChronology());
 assertEquals(instant.getZone(), readWritableInstant.getZone());
 }

 @Test
 public void testParseMillisString() {
 long millis = 1516044423000L;
 String dateTimeString = "2018-02-24T11:40:23.000Z";
 assertEquals(millis, formatter.parseMillis(dateTimeString));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMillisInvalidString() {
 String dateTimeString = "InvalidDate";
 formatter.parseMillis(dateTimeString);
 }

 @Test
 public void testParseLocalDate() {
 Instant instant = new Instant(1516044423000L);
 String dateString = "2018-02-24";
 LocalDate localDate = formatter.parseLocalDate(dateString);
 assertEquals(new LocalDate(instant), localDate);
 }

 @Test
 public void testParseLocalTime() {
 Instant instant = new Instant(1516044423000L);
 String timeString = "11:40:23.000";
 LocalTime localTime = formatter.parseLocalTime(timeString);
 assertEquals(new LocalTime(instant), localTime);
 }

 @Test
 public void testParseLocalDateTime() {
 Instant instant = new Instant(1516044423000L);
 String dateTimeString = "2018-02-24T11:40:23.000Z";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(new LocalDateTime(instant), localDateTime);
 }

 @Test
 public void testParseDateTime() {
 Instant instant = new Instant(1516044423000L);
 String dateTimeString = "2018-02-24T11:40:23.000Z";
 DateTime dateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(new DateTime(instant), dateTime);
 }

 @Test
 public void testParseMutableDateTime() {
 Instant instant = new Instant(1516044423000L);
 String dateTimeString = "2018-02-24T11:40:23.000Z";
 MutableDateTime mutableDateTime = new MutableDateTime();
 formatter.parseMutableDateTime(dateTimeString, mutableDateTime);
 assertEquals(new DateTime(instant), mutableDateTime.toDateTime());
 }
}