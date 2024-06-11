package com.fasterxml.jackson.databind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 // Test constructor
 @Test
 public void testStdDateFormatConstructor() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 assertNotNull(stdDateFormat);
 }

 // Test getInstance method
 @Test
 public void testStdDateFormatInstance() {
 StdDateFormat stdDateFormat1 = StdDateFormat.instance;
 StdDateFormat stdDateFormat2 = StdDateFormat.instance;
 assertSame(stdDateFormat1, stdDateFormat2);
 }

 // Test getBlueprintISO8601Format method (deprecated)
 @Test(expected = Deprecated.class)
 public void testGetBlueprintISO8601FormatDeprecated() {
 DateFormat dateFormat = StdDateFormat.getBlueprintISO8601Format();
 assertNotNull(dateFormat);
 }

 // Test getISO8601Format method
 @Test
 public void testGetISO8601Format() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 assertNotNull(dateFormat);
 }

 // Test parse method with valid date string - ISO-8601 format
 @Test
 public void testParseValidDateStringISO8601Format() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 String validDateString = "2022-12-31T23:59:59.000Z";
 Date date = dateFormat.parse(validDateString);
 assertNotNull(date);
 }

 // Test parse method with valid date string - RFC-1123 format
 @Test
 public void testParseValidDateStringRFC1123Format() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getRFC1123Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 String validDateString = "Sat, 31 Dec 2022 23:59:59 GMT";
 Date date = dateFormat.parse(validDateString);
 assertNotNull(date);
 }

 // Test parse method with invalid date string - not matching any supported format
 @Test(expected = ParseException.class)
 public void testParseInvalidDateString() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 String invalidDateString = "InvalidDateString";
 dateFormat.parse(invalidDateString);
 fail("Expected ParseException but no exception was thrown.");
 }

 // Test parse method with negative number date string
 @Test
 public void testParseNegativeNumberDateString() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 String negativeNumberDateString = "-1234567890";
 Date date = dateFormat.parse(negativeNumberDateString);
 assertNotNull(date);
 }

 // Test format method with valid date object
 @Test
 public void testFormatValidDateObject() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 Date validDate = new Date();
 String formattedDateString = dateFormat.format(validDate);
 assertNotNull(formattedDateString);
 }

 // Test format method with null date object
 @Test
 public void testFormatNullDateObject() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 DateFormat dateFormat = stdDateFormat.getISO8601Format(getDefaultTimeZone(), DEFAULT_LOCALE);
 String formattedDateString = dateFormat.format(null);
 assertEquals("null", formattedDateString);
 }
}