package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

 @Test
 public void testFormatUTCDate() {
 Date date = new Date(System.currentTimeMillis());
 String formattedUtcDate = ISO8601Utils.format(date);
 assertEquals("yyyy-MM-dd'T'HH:mm:ss'Z'", formattedUtcDate.length(), "ISO format for UTC date without milliseconds");
 assertEquals("2023-03-15T12:34:56Z", formattedUtcDate);
 }

 @Test
 public void testFormatDateWithMilliseconds() {
 Date date = new Date(System.currentTimeMillis() + 123);
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone(ISO8601Utils.UTC_ID));
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedDate.length(), "ISO format for date with milliseconds");
 assertEquals("2023-03-15T12:34:56.123Z", formattedDate);
 }

 @Test
 public void testFormatDateWithCustomTimeZone() {
 Date date = new Date(System.currentTimeMillis() + 123);
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/Los_Angeles"));
 assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS[+08:00]", formattedDate.length(), "ISO format for date with custom timezone and milliseconds");
 assertEquals("2023-03-15T20:34:56.123+08:00", formattedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullDate() {
 String formattedDate = ISO8601Utils.format(null);
 fail("ISO8601Utils.format should throw IllegalArgumentException when given a null Date");
 }

 @Test
 public void testParseDate_Valid() {
 String dateString = "2023-03-15T12:34:56.123Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(2023, parsedDate.getYear(), "Year validation");
 assertEquals(3, parsedDate.getMonth(), "Month validation");
 assertEquals(15, parsedDate.getDate(), "Day validation");
 assertEquals(12, parsedDate.getHours(), "Hour validation");
 assertEquals(34, parsedDate.getMinutes(), "Minute validation");
 assertEquals(56, parsedDate.getSeconds(), "Second validation");
 assertEquals(123, parsedDate.getMilliseconds(), "Millisecond validation");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_InvalidFormat() {
 String dateString = "2023-03-15T12:34:56ZAB";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given an invalid format");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_MissingYear() {
 String dateString = "03-15T12:34:56.123Z";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given a missing year");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_MissingMonth() {
 String dateString = "2023-03-15T12:34:56.123Z";
 dateString = dateString.replaceAll("[^0-9]", ""); // remove all non-digit characters
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given a missing month");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_MissingDay() {
 String dateString = "2023-03-15T12:34:56.123Z";
 dateString = dateString.replaceAll("[^0-9]", ""); // remove all non-digit characters
 dateString = dateString.substring(0, dateString.length() - 2); // remove trailing Z
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given a missing day");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_MissingTime() {
 String dateString = "2023-03-15";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given a missing time");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_MissingMilliseconds() {
 String dateString = "2023-03-15T12:34:56Z";
 dateString = dateString.replaceAll("[.]", ""); // remove all dots
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given a missing milliseconds");
 }

 @Test(expected = ParseException.class)
 public void testParseDate_InvalidTimeZoneOffset() {
 String dateString = "2023-03-15T12:34:56+123:00";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("ISO8601Utils.parse should throw ParseException when given an invalid timezone offset");
 }

 @Test
 public void testParseDate_ValidWithTimeZone() {
 String dateString = "2023-03-15T12:34:56.123+08:00";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(2023, parsedDate.getYear(), "Year validation");
 assertEquals(3, parsedDate.getMonth(), "Month validation");
 assertEquals(15, parsedDate.getDate(), "Day validation");
 assertEquals(12, parsedDate.getHours(), "Hour validation");
 assertEquals(34, parsedDate.getMinutes(), "Minute validation");
 assertEquals(56, parsedDate.getSeconds(), "Second validation");
 assertEquals(123, parsedDate.getMilliseconds(), "Millisecond validation");
 assertEquals("America/Los_Angeles", parsedDate.getTimeZone().getId(), "Timezone validation");
 }

}