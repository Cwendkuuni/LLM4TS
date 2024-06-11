package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

public class StdDateFormatTest {

 // Test StdDateFormat constructor
 @Test
 public void testStdDateFormatConstruction() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertEquals("DateFormat StdDateFormat (default locale and timezone)",
 "DateFormat " + StdDateFormat.class.getName() + " (default locale and timezone)",
 dateFormat.toString());
 }

 // Test StdDateFormat construction with timezone
 @Test
 public void testStdDateFormatConstructionWithTimeZone() {
 StdDateFormat dateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC+1"));
 assertEquals("DateFormat StdDateFormat (UTC+1 timezone)",
 "DateFormat " + StdDateFormat.class.getName() + " (" + TimeZone.getTimeZone("UTC+1") + " timezone)",
 dateFormat.toString());
 }

 // Test StdDateFormat construction with locale and timezone
 @Test
 public void testStdDateFormatConstructionWithLocaleAndTimeZone() {
 StdDateFormat dateFormat = new StdDateFormat(Locale.CHINESE, TimeZone.getTimeZone("Asia/Shanghai"));
 assertEquals("DateFormat StdDateFormat (locale: zh_CN, timezone: Asia/Shanghai)",
 "DateFormat " + StdDateFormat.class.getName() + " (locale: zh_CN, timezone: Asia/Shanghai)",
 dateFormat.toString());
 }

 // Test StdDateFormat clone method
 @Test
 public void testStdDateFormatClone() {
 StdDateFormat original = new StdDateFormat(Locale.US, TimeZone.getTimeZone("UTC"));
 StdDateFormat clone = original.clone();
 assertEquals("DateFormat cloned StdDateFormat",
 "DateFormat " + StdDateFormat.class.getName() + " (locale: US, timezone: UTC)",
 clone.toString());
 }

 // Test parse method with valid ISO8601 formatted date string
 @Test
 public void testParseValidISO8601FormattedDateString() throws ParseException {
 String isoDateString = "2023-03-17T14:30:23.456Z";
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = dateFormat.parse(isoDateString);
 assertEquals("Parsed date from ISO8601 formatted date string: " + isoDateString,
 new Date(1678867230000L), date); // 17th March 2023, 14:30:23.456Z
 }

 // Test parse method with valid RFC1123 formatted date string
 @Test
 public void testParseValidRFC1123FormattedDateString() throws ParseException {
 String rfcDateString = "Fri, 17 Mar 2023 14:30:23 +0100";
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = dateFormat.parse(rfcDateString);
 assertEquals("Parsed date from RFC1123 formatted date string: " + rfcDateString,
 new Date(1678867230000L), date); // 17th March 2023, 14:30:23 +0100
 }

 // Test parse method with valid plain date string
 @Test
 public void testParseValidPlainDateString() throws ParseException {
 String plainDateString = "2023-03-17";
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = dateFormat.parse(plainDateString);
 assertEquals("Parsed date from plain date string: " + plainDateString,
 new Date(1678307200000L), date); // 17th March 2023
 }

 // Test parse method with null date string
 @Test (expected = NullPointerException.class)
 public void testParseNullDateString() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 dateFormat.parse(null);
 }

 // Test parse method with empty date string
 @Test
 public void testParseEmptyDateString() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNull("Parsed empty date string should be null",
 dateFormat.parse(""));
 }

 // Test parse method with invalid date string (missing components)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateStringMissingComponents() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 dateFormat.parse("2023-");
 }

 // Test parse method with invalid date string (wrong format)
 @Test (expected = ParseException.class)
 public void testParseInvalidDateStringWrongFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 dateFormat.parse("InvalidFormat: 2023-03-17-14:30:23.456Z");
 }

 // Test format method with valid date object
 @Test
 public void testFormatValidDateObject() {
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = new Date(1678867230000L);
 String formattedDateString = dateFormat.format(date, new StringBuffer(), new FieldPosition(0));
 assertEquals("Formatted date from date object: " + date,
 "2023-03-17T14:30:23.456Z", formattedDateString);
 }

 // Test format method with null date object
 @Test
 public void testFormatNullDateObject() {
 StdDateFormat dateFormat = new StdDateFormat();
 String formattedDateString = dateFormat.format(null, new StringBuffer(), new FieldPosition(0));
 assertEquals("Formatted date from null date object should be an empty string",
 "", formattedDateString);
 }
}