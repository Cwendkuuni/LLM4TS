package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class ISO8601UtilsTest {

 private TimeZone utc;
 private GregorianCalendar calendar;

 @Before
 public void setUp() {
 utc = TimeZone.getTimeZone("UTC");
 calendar = new GregorianCalendar(utc);
 }

 @Test
 public void testFormat() throws Exception {
 // Test format without milliseconds or timezone
 Date date = new Date(calendar.getTimeInMillis());
 String formatted = ISO8601Utils.format(date);
 assertEquals("2023-03-28T12:34:56", formatted);

 // Test format with milliseconds but no timezone
 date.setTime(calendar.getTimeInMillis() + 123);
 formatted = ISO8601Utils.format(date, true, utc);
 assertEquals("2023-03-28T12:34:56.123", formatted);

 // Test format with timezone
 calendar.setTime(date);
 calendar.set(Calendar.HOUR_OF_DAY, 13);
 calendar.set(Calendar.MINUTE, 45);
 calendar.set(Calendar.SECOND, 60);
 calendar.set(Calendar.MILLISECOND, 789);
 date = calendar.getTime();
 formatted = ISO8601Utils.format(date, true, utc);
 assertEquals("2023-03-28T13:45:60.789Z", formatted);

 // Test format with timezone offset
 utc = TimeZone.getTimeZone("UTC+2:00");
 calendar.setTime(date);
 calendar.set(Calendar.HOUR_OF_DAY, 11);
 calendar.set(Calendar.MINUTE, 30);
 calendar.set(Calendar.SECOND, 15);
 calendar.set(Calendar.MILLISECOND, 27);
 date = calendar.getTime();
 formatted = ISO8601Utils.format(date, true, utc);
 assertEquals("2023-03-28T11:30:15.27+02:00", formatted);
 }

 @Test
 public void testParse() throws Exception {
 // Test parse with no timezone
 String dateString = "2023-03-28T12:34:56";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertNotNull(parsedDate);
 assertEquals(calendar.getTimeInMillis(), parsedDate.getTime());

 // Test parse with milliseconds but no timezone
 dateString += ".123";
 ParsePosition pos = new ParsePosition(0);
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(calendar.getTimeInMillis() + 123, parsedDate.getTime());
 pos.setIndex(dateString.length());

 // Test parse with timezone
 dateString = "2023-03-28T13:45:60.789Z";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(calendar.getTimeInMillis() + 13 * 60 * 60 * 1000 + 45 * 60 * 1000 + 60 * 1000 + 789, parsedDate.getTime());
 pos.setIndex(dateString.length());

 // Test parse with timezone offset
 utc = TimeZone.getTimeZone("UTC+2:00");
 dateString = "2023-03-28T11:30:15.27+02:00";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(calendar.getTimeInMillis() + 11 * 60 * 60 * 1000 + 30 * 60 * 1000 + 15 * 60 * 1000 + 789 * 1000 + 2 * 60 * 60 * 60 * 1000, parsedDate.getTime());
 pos.setIndex(dateString.length());
 }

 @Test
 public void testFormatAndParseRoundtrip() throws Exception {
 Date date = new Date(calendar.getTimeInMillis());
 String formatted = ISO8601Utils.format(date, true, utc);
 Date parsedDate = ISO8601Utils.parse(formatted, new ParsePosition(0));
 assertEquals(date.getTime(), parsedDate.getTime());
 }

 @Test
 public void testFormatAndParseInvalid() throws Exception {
 // Test format and parse invalid input
 String invalidDateString1 = "2023-33-28T12:34:56";
 String invalidDateString2 = "2023-03-28T12:34:56.123Zz";
 String invalidDateString3 = "2023-03-28T12:34:56.123+02:00a";

 assertFalse(ISO8601Utils.isValidDateString(invalidDateString1));
 assertFalse(ISO8601Utils.isValidDateString(invalidDateString2));
 assertFalse(ISO8601Utils.isValidDateString(invalidDateString3));

 String dateString;
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate;

 // Test parse invalid 'T'
 dateString = "2023-03-28";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid year
 dateString = "123-03-28";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid month
 dateString = "2023-00-28";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid day
 dateString = "2023-03-32";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid time component
 dateString = "2023-03-28T12:60:56"; // 1 hour has 60 minutes
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid time zone component
 dateString = "2023-03-28T12:34:56.123Zz";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());

 // Test parse invalid time zone offset
 dateString = "2023-03-28T12:34:56.123+02:00a";
 parsedDate = ISO8601Utils.parse(dateString, pos);
 assertNull(parsedDate);
 assertEquals(dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseInvalidFormat() throws Exception {
 // Test parse with invalid format
 String invalidDateString = "2023-03-28T12:34:56.123";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(invalidDateString, pos);
 assertNull(parsedDate);
 assertEquals(invalidDateString.length(), pos.getIndex());
 }
}