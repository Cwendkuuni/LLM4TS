package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 String expected = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // replace with the actual expected format
 assertTrue(formattedDate.matches(expected));
 }

 @Test
 public void testFormatDateWithMillis() {
 Date date = new Date();
 boolean millis = true;
 String formattedDate = ISO8601Utils.format(date, millis);
 String expected = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // replace with the actual expected format
 assertTrue(formattedDate.matches(expected));
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Date date = new Date();
 TimeZone timeZone = new TimeZone("Europe/London");
 String formattedDate = ISO8601Utils.format(date, false, timeZone);
 String expected = "yyyy-MM-dd'T'HH:mm:ss'Z'"; // replace with the actual expected format with timezone
 assertTrue(formattedDate.matches(expected));
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "2022-01-01T12:34:56.789Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 Calendar calendar = new GregorianCalendar();
 calendar.setTime(parsedDate);
 int expectedYear = calendar.get(Calendar.YEAR);
 int expectedMonth = calendar.get(Calendar.MONTH);
 int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
 int expectedHour = calendar.get(Calendar.HOUR_OF_DAY);
 int expectedMinute = calendar.get(Calendar.MINUTE);
 int expectedSecond = calendar.get(Calendar.SECOND);
 int expectedMillisecond = calendar.get(Calendar.MILLISECOND);
 assertEquals("Year is incorrect", expectedYear, 2022);
 assertEquals("Month is incorrect", expectedMonth, 1);
 assertEquals("Day is incorrect", expectedDay, 1);
 assertEquals("Hour is incorrect", expectedHour, 12);
 assertEquals("Minute is incorrect", expectedMinute, 34);
 assertEquals("Second is incorrect", expectedSecond, 56);
 assertEquals("Millisecond is incorrect", expectedMillisecond, 789);
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() throws ParseException {
 String dateString = "2022-01-01T12:34:56Za";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ParseException expected but not thrown");
 }

 @Test(expected = ParseException.class)
 public void testParseDateMissingTimeZone() throws ParseException {
 String dateString = "2022-01-01T12:34:56";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ParseException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateInvalidTimeZoneIdentifier() throws ParseException {
 String dateString = "2022-01-01T12:34:56Zinvalid";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testParseDateWithTimeZone() throws ParseException {
 String dateString = "2022-01-01T12:34:56+01:00";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 Calendar calendar = new GregorianCalendar();
 calendar.setTime(parsedDate);
 int expectedYear = calendar.get(Calendar.YEAR);
 int expectedMonth = calendar.get(Calendar.MONTH);
 int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
 int expectedHour = calendar.get(Calendar.HOUR_OF_DAY);
 int expectedMinute = calendar.get(Calendar.MINUTE);
 int expectedSecond = calendar.get(Calendar.SECOND);
 int expectedMillisecond = calendar.get(Calendar.MILLISECOND);
 int expectedOffset = calendar.get(Calendar.EXACT_DST_OFFSET);
 int expectedRawOffset = calendar.get(Calendar.ZONE_OFFSET);
 assertEquals("Year is incorrect", expectedYear, 2022);
 assertEquals("Month is incorrect", expectedMonth, 1);
 assertEquals("Day is incorrect", expectedDay, 1);
 assertEquals("Hour is incorrect", expectedHour, 13); // DST
 assertEquals("Minute is incorrect", expectedMinute, 34);
 assertEquals("Second is incorrect", expectedSecond, 56);
 assertEquals("Millisecond is incorrect", expectedMillisecond, 0);
 assertEquals("Offset is incorrect", expectedOffset, 3600000);
 assertEquals("RawOffset is incorrect", expectedRawOffset, 3600000);
 }

}