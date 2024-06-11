package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

 private Date createDate(String dateString) {
 Date date;
 try {
 date = ISO8601Utils.parse(dateString, new ParsePosition(0));
 } catch (ParseException e) {
 throw new RuntimeException("Failed to parse: " + dateString, e);
 }
 return date;
 }

 @Test
 public void testFormat() {
 // Test with default timezone and no milliseconds
 Date date = new Date(1516736000000L); // 2018-02-14 10:00:00
 String formatted = ISO8601Utils.format(date);
 assertEquals("2018-02-14T10:00:00Z", formatted);

 // Test with custom timezone and milliseconds
 TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
 date = new Date(1516736660000L); // 2018-02-14 11:01:00 in Paris time
 formatted = ISO8601Utils.format(date, true, timeZone);
 assertEquals("2018-02-14T11:01:00+01:00", formatted);

 // Test with date only
 Calendar calendar = new GregorianCalendar(Locale.US, 2018, 2, 14);
 date = calendar.getTime();
 formatted = ISO8601Utils.format(date);
 assertEquals("2018-02-14", formatted);
 }

 @Test
 public void testParse() {
 // Test parsing with milliseconds and default timezone
 String dateString = "2018-02-14T10:00:00.123Z";
 Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(1516736000123L, date.getTime());

 // Test parsing with custom timezone
 dateString = "2018-02-14T11:01:00+01:00";
 TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
 date = ISO8601Utils.parse(dateString, new ParsePosition(0), timeZone);
 assertEquals(1516736660000L, date.getTime());

 // Test parsing with date only
 String dateStringOnly = "2018-02-14";
 Calendar calendar = new GregorianCalendar(Locale.US, 2018, 2, 14);
 date = calendar.getTime();
 ParsePosition pos = new ParsePosition(0);
 date = ISO8601Utils.parse(dateStringOnly, pos);
 assertEquals(calendar.getTimeInMillis(), date.getIndex());
 assertNotNull(date);

 // Test parsing with invalid format
 assertThrows(ParseException.class, () -> ISO8601Utils.parse("2018-02-14T10:00:00.123ABC", new ParsePosition(0)));

 // Test parsing with missing timezone indicator
 assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.parse("2018-02-14T10:00:00", new ParsePosition(0)));
 }

 @Test
 public void testFormatParseRoundTrip() {
 // Test round trip with default timezone and no milliseconds
 String dateString = "2018-02-14T10:00:00Z";
 Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
 String formatted = ISO8601Utils.format(date);
 assertEquals(dateString, formatted);

 // Test round trip with custom timezone and milliseconds
 dateString = "2018-02-14T11:01:00+01:00";
 timeZone = TimeZone.getTimeZone("Europe/Paris");
 date = ISO8601Utils.parse(dateString, new ParsePosition(0), timeZone);
 formatted = ISO8601Utils.format(date, true, timeZone);
 assertEquals(dateString, formatted);

 // Test round trip with date only
 calendar = new GregorianCalendar(Locale.US, 2018, 2, 14);
 date = calendar.getTime();
 formatted = ISO8601Utils.format(date);
 date = ISO8601Utils.parse(formatted, new ParsePosition(0));
 assertEquals(calendar.getTimeInMillis(), date.getTime());
 }

 @Test
 public void testFormatParseRoundTripWithTimeZoneID() {
 // Test round trip with default timezone and no milliseconds
 String dateString = "2018-02-14T10:00:00Z";
 Date date = ISO8601Utils.parse(dateString, new ParsePosition(0), TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
 String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
 assertEquals(dateString, formatted);

 // Test round trip with custom timezone and milliseconds
 dateString = "2018-02-14T11:01:00+01:00";
 timeZone = TimeZone.getTimeZone("Europe/Paris");
 date = ISO8601Utils.parse(dateString, new ParsePosition(0), timeZone);
 formatted = ISO8601Utils.format(date, true, timeZone);
 assertEquals(dateString, formatted);
 }

 @Test
 public void testFormatParseRoundTripWithZonedDateTime() {
 DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT();
 ZonedDateTime date = ZonedDateTime.parse("2018-02-14T10:00:00Z", formatter);
 Date parsedDate = ISO8601Utils.parse(date.format(formatter), new ParsePosition(0));
 assertEquals(date.toInstant().toEpochMilli(), parsedDate.getTime());

 String formattedString = ISO8601Utils.format(parsedDate, true, null);
 ZonedDateTime parsedZonedDateTime = ZonedDateTime.parse(formattedString, formatter);
 assertEquals(date.toInstant().toEpochMilli(), parsedZonedDateTime.toInstant().toEpochMilli());
 }
}