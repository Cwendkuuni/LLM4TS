package org.joda.time.format;

import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

 @Test
 public void testNewInstanceWithPrinterAndParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), new SimpleDateTimeFormat("dd/MM/yyyy"));
 assertTrue(formatter.isPrinter());
 assertTrue(formatter.isParser());
 assertNull(formatter.getLocale());
 assertFalse(formatter.isOffsetParsed());
 assertNull(formatter.getChrono());
 assertNull(formatter.getZone());
 assertNull(formatter.getPivotYear());
 assertDefaultYear(formatter, 2000);
 }

 @Test
 public void testNewInstanceWithPrinterParserLocaleOffsetParsedChronoZonePivotYear() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), new SimpleDateTimeFormat("dd/MM/yyyy"), Locale.US, true, GregorianChronology.getInstance(), DateTimeZone.forOffsetHours(-5), 2010, 2005);
 assertTrue(formatter.isPrinter());
 assertTrue(formatter.isParser());
 assertEquals(Locale.US, formatter.getLocale());
 assertTrue(formatter.isOffsetParsed());
 assertEquals(GregorianChronology.getInstance(), formatter.getChrono());
 assertEquals(DateTimeZone.forOffsetHours(-5), formatter.getZone());
 assertEquals(2010, formatter.getPivotYear());
 assertDefaultYear(formatter, 2005);
 }

 @Test
 public void testGetters() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), new SimpleDateTimeFormat("dd/MM/yyyy"));
 assertFalse(formatter.isPrinter(), "Printer should be false");
 assertFalse(formatter.isParser(), "Parser should be false");
 assertNull(formatter.getLocale(), "Locale should be null");
 assertFalse(formatter.isOffsetParsed(), "OffsetParsed should be false");
 assertNull(formatter.getChrono(), "Chrono should be null");
 assertNull(formatter.getZone(), "Zone should be null");
 assertNull(formatter.getPivotYear(), "PivotYear should be null");
 assertDefaultYear(formatter, 2000);

 DateTimeFormatter newFormatter = formatter.withLocale(Locale.US);
 assertSame(formatter, newFormatter);
 assertSame(formatter.getLocale(), Locale.US);

 DateTimeFormatter newFormatter2 = formatter.withOffsetParsed();
 assertNotSame(formatter, newFormatter2);
 assertTrue(newFormatter2.isOffsetParsed());
 assertFalse(formatter.isOffsetParsed());

 DateTimeFormatter newFormatter3 = formatter.withChronology(GregorianChronology.getInstance());
 assertNotSame(formatter, newFormatter3);
 assertEquals(GregorianChronology.getInstance(), newFormatter3.getChrono());
 assertNull(formatter.getChrono());

 DateTimeFormatter newFormatter4 = formatter.withZone(DateTimeZone.UTC);
 assertNotSame(formatter, newFormatter4);
 assertEquals(DateTimeZone.UTC, newFormatter4.getZone());
 assertNull(formatter.getZone());

 DateTimeFormatter newFormatter5 = formatter.withPivotYear(2020);
 assertNotSame(formatter, newFormatter5);
 assertEquals(2020, newFormatter5.getPivotYear());
 assertNull(formatter.getPivotYear());

 DateTimeFormatter newFormatter6 = formatter.withDefaultYear(2021);
 assertNotSame(formatter, newFormatter6);
 assertEquals(2021, newFormatter6.getDefaultYear());
 assertEquals(2000, formatter.getDefaultYear());
 }

 @Test
 public void testIsPrinterAndIsParser() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), new SimpleDateTimeFormat("dd/MM/yyyy"));
 assertTrue(formatter.isPrinter());
 assertTrue(formatter.isParser());

 DateTimeFormatter formatterNoPrinter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), null);
 assertFalse(formatterNoPrinter.isPrinter());
 assertTrue(formatterNoPrinter.isParser());

 DateTimeFormatter formatterNoParser = new DateTimeFormatter(null, new SimpleDateTimeFormat("dd/MM/yyyy"));
 assertTrue(formatterNoParser.isPrinter());
 assertFalse(formatterNoParser.isParser());
 }

 @Test
 public void testParseAndPrint() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"), new SimpleDateTimeFormat("dd/MM/yyyy"));
 Date date = new Date();
 String dateString = new SimpleDateFormat("dd/MM/yyyy").format(date);

 assertNull(formatter.print(null));

 LocalDateTime dateTime = LocalDateTime.now();
 DateTime localDateTimeFormatted = formatter.printTo(new StringBuffer(), dateTime.toDate());
 assertEquals(dateString, localDateTimeFormatted.toString());

 LocalDate dateOnly = LocalDate.now();
 DateTime dateOnlyFormatted = formatter.printTo(new StringBuffer(), dateOnly.atStartOfDay().toDate());
 assertEquals(dateString.substring(0, 10), dateOnlyFormatted.toString());

 LocalTime timeOnly = LocalTime.now();
 DateTime timeOnlyFormatted = formatter.printTo(new StringBuffer(), timeOnly.atDate(dateOnly).toDate());
 assertEquals(dateString.substring(0, 10) + " " + timeOnly.format("HH:mm:ss"), timeOnlyFormatted.toString());

 Instant instant = Instant.now();
 DateTime instantFormatted = formatter.printTo(new StringBuffer(), instant);
 assertEquals(dateString, instantFormatted.toString("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'"));

 LocalDateTime dateTimeWithOffset = LocalDateTime.now().plusHours(2);
 DateTime dateTimeWithOffsetFormatted = formatter.withOffsetParsed().printTo(new StringBuffer(), dateTimeWithOffset.atZone(DateTimeZone.forOffsetHours(2)).toInstant());
 assertEquals(dateString + " " + dateTimeWithOffset.getHour() + ":", dateTimeWithOffsetFormatted.toString());
 }

 @Test
 public void testParseInvalidInput() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"));
 try {
 formatter.parseMillis("Invalid Input");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 24:00:00");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60.123");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60.123Z");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60.123+01:00");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60.123+01:00");
 fail("Expected DateTimeParseException");
 } catch (DateTimeParseException e) {
 // expected
 }

 try {
 formatter.parseMillis("2010-12-31 23:59:60.123+01:00:01");
 fail("Expected DateTimeParseException");
 } catch (DateTimeParseException e) {
 // expected
 }
}

 @Test
 public void testParseToLocalDate() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy"));
 String dateString = "31/12/2010";
 LocalDate localDate = formatter.parseLocalDate(dateString);
 assertEquals(new Date(new SimpleDateFormat("dd/MM/yyyy").parse(dateString).getTime()), localDate.toDate());
 assertEquals("31", localDate.getDayOfMonth());
 assertEquals(12, localDate.getMonthValue());
 assertEquals(2010, localDate.getYear());
 }

 @Test
 public void testParseToLocalTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"));
 String timeString = "23:59:60";
 LocalTime localTime = formatter.parseLocalTime(timeString);
 assertEquals(new Date(new SimpleDateFormat("HH:mm:ss").parse(timeString).getTime()), localTime.toDate().toInstant().toEpochMilli());
 assertEquals(23, localTime.getHourOfDay());
 assertEquals(59, localTime.getMinuteOfHour());
 assertEquals(60, localTime.getSecondOfMinute());
 }

 @Test
 public void testParseToLocalDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy HH:mm:ss"));
 String dateTimeString = "31/12/2010 23:59:60";
 LocalDateTime localDateTime = formatter.parseLocalDateTime(dateTimeString);
 assertEquals(new Date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeString).getTime()), localDateTime.toDate().toInstant().toEpochMilli());
 assertEquals("31", localDateTime.getDayOfMonth());
 assertEquals(12, localDateTime.getMonthValue());
 assertEquals(2010, localDateTime.getYear());
 assertEquals(23, localDateTime.getHour());
 assertEquals(59, localDateTime.getMinute());
 assertEquals(60, localDateTime.getSecond());
 }

 @Test
 public void testParseToDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy HH:mm:ss"));
 String dateTimeString = "31/12/2010 23:59:60";
 DateTime dateTime = formatter.parseDateTime(dateTimeString);
 assertEquals(new Date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeString).getTime()), dateTime.toInstant().toEpochMilli());
 assertEquals("31", dateTime.getDayOfMonth());
 assertEquals(12, dateTime.getMonthOfYear());
 assertEquals(2010, dateTime.getYear());
 assertEquals(23, dateTime.getHourOfDay());
 assertEquals(59, dateTime.getMinuteOfHour());
 assertEquals(60, dateTime.getSecondOfMinute());
 assertEquals(DateTimeZone.UTC, dateTime.getZone());
 }

 @Test
 public void testParseToMutableDateTime() {
 DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd/MM/yyyy HH:mm:ss"));
 String dateTimeString = "31/12/2010 23:59:60";
 MutableDateTime mutableDateTime = formatter.parseMutableDateTime(dateTimeString);
 assertEquals(new Date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeString).getTime()), mutableDateTime.toInstant().toEpochMilli());
 assertEquals("31", mutableDateTime.getDayOfMonth());
 assertEquals(12, mutableDateTime.getMonthOfYear());
 assertEquals(2010, mutableDateTime.getYear());
 assertEquals(23, mutableDateTime.getHourOfDay());
 assertEquals(59, mutableDateTime.getMinuteOfHour());
 assertEquals(60, mutableDateTime.getSecondOfMinute());
 }
}