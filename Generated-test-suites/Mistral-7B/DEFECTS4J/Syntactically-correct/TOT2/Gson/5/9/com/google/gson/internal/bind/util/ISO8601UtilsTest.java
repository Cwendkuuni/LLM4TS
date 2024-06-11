package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

 // Test for formatting methods

 @Test
 public void testFormatWithValidDate() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("Expected a valid ISO8601 formatted date, but got: " + formattedDate, "yyyy-MM-dd'T'hh:mm:ss.SSSZ", formattedDate.length());
 }

 @Test
 public void testFormatWithNullDate() {
 String formattedDate = ISO8601Utils.format(null);
 assertNull("Expected null for format method with null date, but got: " + formattedDate, formattedDate);
 }

 @Test
 public void testFormatWithDateInFuture() {
 Date date = new Date(System.currentTimeMillis() + 3600000L);
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("Expected a valid ISO8601 formatted date, but got: " + formattedDate, "yyyy-MM-dd'T'hh:mm:ss.SSSZ", formattedDate.length());
 }

 @Test
 public void testFormatWithDateInPast() {
 Date date = new Date(System.currentTimeMillis() - 3600000L);
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("Expected a valid ISO8601 formatted date, but got: " + formattedDate, "yyyy-MM-dd'T'hh:mm:ss.SSSZ", formattedDate.length());
 }

 @Test
 public void testFormatWithDateInUTC() {
 Date date = new Date(System.currentTimeMillis());
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals("Expected a valid ISO8601 formatted UTC date, but got: " + formattedDate, "yyyy-MM-dd'T'hh:mm:ss.SSSZ", formattedDate.length());
 }

 // Test for parsing methods

 @Test
 public void testParseWithValidDate() throws Exception {
 Date parsedDate = ISO8601Utils.parse("2019-03-31T12:34:56.789Z", new ParsePosition(0));
 assertEquals("Expected a valid Date object for parsed date, but got: " + parsedDate, new Date(1553606096000L), parsedDate.getTime());
 }

 @Test
 public void testParseWithNullString() throws Exception {
 assertNull("Expected null for parse method with null string, but got: null", ISO8601Utils.parse(null, new ParsePosition(0)));
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidFormat() throws Exception {
 ISO8601Utils.parse("2019-03-31T12:34:56.789abc", new ParsePosition(0));
 }

 @Test(expected = ParseException.class)
 public void testParseWithDateInFuture() throws Exception {
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse("2100-01-01T00:00:00.000+01:01", pos);
 assertEquals("Expected ParsePosition.getIndex() to be equal to input string length after parsing a date in future, but got: " + pos.getIndex(), pos.getIndex(), pos.getInput().length());
 }

 @Test(expected = ParseException.class)
 public void testParseWithDateInPast() throws Exception {
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse("1900-01-01T00:00:00.000-01:01", pos);
 assertEquals("Expected ParsePosition.getIndex() to be equal to input string length after parsing a date in past, but got: " + pos.getIndex(), pos.getIndex(), pos.getInput().length());
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidTimeZone() throws Exception {
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse("2020-01-01T00:00:00.000+InvalidTimeZone", pos);
 }

 @Test
 public void testParseWithDateAtMidnight() throws Exception {
 Date parsedDate = ISO8601Utils.parse("2019-03-31T00:00:00.000Z", new ParsePosition(0));
 assertEquals("Expected a valid Date object for parsed date at midnight, but got: " + parsedDate, new Date(1553571200000L), parsedDate.getTime());
 }

 @Test
 public void testParseWithDateAtMidnightWithMillis() throws Exception {
 Date parsedDate = ISO8601Utils.parse("2019-03-31T00:00:00.123Z", new ParsePosition(0));
 assertEquals("Expected a valid Date object for parsed date at midnight with milliseconds, but got: " + parsedDate, new Date(1553571200123L), parsedDate.getTime());
 }

 // Test for other methods

 @Test
 public void testGetUTCID() {
 assertEquals("Expected 'UTC' string for UTC ID", "UTC", ISO8601Utils.UTC_ID);
 }

 @Test
 public void testGetUTCTimeZone() {
 TimeZone timeZone = ISO8601Utils.TIMEZONE_UTC;
 assertEquals("Expected TimeZone with ID 'UTC' for UTC TimeZone", TimeZone.getTimeZone("UTC"), timeZone);
 }

 @Test
 public void testFormatWithNonNullCalendar() {
 Calendar calendar = new GregorianCalendar(2019, Calendar.MARCH, 31);
 String formattedDate = ISO8601Utils.format(calendar.getTime(), false, TimeZone.getDefault());
 assertEquals("Expected a valid ISO8601 formatted date, but got: " + formattedDate, "Expected a valid ISO8601 formatted date, but got: " + formattedDate, formattedDate.length());
 }

 @Test
 public void testParseWithNonNullParsePosition() throws Exception {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse("2019-03-31T12:34:56.789Z", pos);
 assertEquals("Expected null for parse method with null ParsePosition, but got: " + parsedDate, null, parsedDate);
 }

 @Test
 public void testFormatWithInvalidCalendar() {
 Calendar calendar = new GregorianCalendar(2019, Calendar.DECEMBER, 32);
 String formattedDate = ISO8601Utils.format(calendar.getTime(), false, TimeZone.getDefault());
 assertNull("Expected null for format method with invalid calendar, but got: " + formattedDate, formattedDate);
 }

 @Test
 public void testParseWithInvalidParsePosition() throws Exception {
 ParsePosition pos = new ParsePosition(-1);
 Date parsedDate = ISO8601Utils.parse("2019-03-31T12:34:56.789Z", pos);
 assertNull("Expected null for parse method with invalid ParsePosition, but got: " + parsedDate, parsedDate);
 }

 @Test
 public void testFormatUTCDateWithValidDate() {
 Date date = new Date(System.currentTimeMillis());
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals("Expected a valid ISO8601 formatted UTC date, but got: " + formattedDate, "Expected a valid ISO8601 formatted UTC date, but got: " + formattedDate, formattedDate.length());
 }

 @Test
 public void testParseUTCDateWithValidDate() throws Exception {
 Date parsedDate = ISO8601Utils.parse("2019-03-31T12:34:56.789Z", new ParsePosition(0));
 assertEquals("Expected a valid Date object for parsed UTC date, but got: " + parsedDate, new Date(1553571296000L), parsedDate.getTime());
 }

 @Test
 public void testFormatAndParseDateWithTimeZoneOffset() throws Exception {
 Date date = new Date(System.currentTimeMillis());
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/London"));
 Date parsedDate = ISO8601Utils.parse(formattedDate, new ParsePosition(0));
 assertEquals("Expected a valid Date object for parsed date with time zone offset, but got: " + parsedDate, date, parsedDate);
 }
}