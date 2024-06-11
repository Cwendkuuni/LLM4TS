package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

 // Test constructor
 @Test
 public void testStdDateFormatConstructor() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 assertNotNull(stdDateFormat);
 }

 // Test getInstance method
 @Test
 public void testGetInstance() {
 StdDateFormat stdDateFormat = StdDateFormat.getInstance();
 assertNotNull(stdDateFormat);
 }

 // Test parse method with valid ISO-8601 format
 @Test
 public void testParseISO8601Format() throws ParseException {
 String dateString = "2022-12-31T23:59:59.000Z";
 Date expectedDate = new Date(1672502399000L);
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test parse method with valid RFC-1123 format
 @Test
 public void testParseRFC1123Format() throws ParseException {
 String dateString = "Thu, 31 Dec 2022 23:59:59 GMT";
 Date expectedDate = new Date(1672502399000L);
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test parse method with invalid format
 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() throws ParseException {
 String dateString = "InvalidFormat";
 Date date = StdDateFormat.getInstance().parse(dateString);
 fail("Expected ParseException but got " + date);
 }

 // Test parse method with missing time and timezone
 @Test
 public void testParseMissingTimeAndTimezone() throws ParseException {
 String dateString = "2022-12-31";
 Date expectedDate = new Date(1672502399000L - 3600000); // subtract 1 hour
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test parse method with missing time but with timezone
 @Test
 public void testParseMissingTimeWithTimezone() throws ParseException {
 String dateString = "2022-12-31 23:00:00Z";
 Date expectedDate = new Date(1672502399000L);
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test parse method with timezone but missing time
 @Test
 public void testParseWithTimezoneButMissingTime() throws ParseException {
 String dateString = "2022-12-31Z";
 Date expectedDate = new Date(1672502399000L); // no change
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test parse method with time and timezone
 @Test
 public void testParseWithTimeAndTimezone() throws ParseException {
 String dateString = "2022-12-31 23:59:59+01:00";
 Date expectedDate = new Date(1672511199000L); // add 1 hour
 Date date = StdDateFormat.getInstance().parse(dateString);
 assertEquals(expectedDate, date);
 }

 // Test format method with valid ISO-8601 format
 @Test
 public void testFormatISO8601Format() {
 Date date = new Date(1672502399000L);
 String expectedDateString = "2022-12-31T23:59:59.000Z";
 String dateString = StdDateFormat.getInstance().format(date);
 assertEquals(expectedDateString, dateString);
 }

 // Test format method with valid RFC-1123 format
 @Test
 public void testFormatRFC1123Format() {
 Date date = new Date(1672502399000L);
 String expectedDateString = "Thu, 31 Dec 2022 23:59:59 GMT";
 String dateString = StdDateFormat.getInstance().format(date);
 assertEquals(expectedDateString, dateString);
 }

 // Test format method with custom locale and timezone
 @Test
 public void testFormatWithCustomLocaleAndTimezone() {
 Date date = new Date(1672502399000L);
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "GMT+0");
 StdDateFormat stdDateFormat = new StdDateFormat(locale, timeZone);
 String expectedDateString = "jeu. 31 déc. 2022 23:59:59 GMT+00:00";
 String dateString = stdDateFormat.format(date);
 assertEquals(expectedDateString, dateString);
 }
}