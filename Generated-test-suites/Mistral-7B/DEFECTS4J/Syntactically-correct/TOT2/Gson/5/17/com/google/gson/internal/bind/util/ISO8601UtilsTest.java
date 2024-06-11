package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

import org.junit.Test;

/**
 * Test class for ISO8601Utils.
 */
public class ISO8601UtilsTest {

 /**
  * Test for format method with default timezone and no milliseconds precision.
  */
 @Test
 public void testFormatDefault() {
 Date date = new Date();
 String formatted = ISO8601Utils.format(date);
 assertEquals("Expected format to be yyyy-MM-ddThh:mm:ssZ", "yyyy-MM-ddThh:mm:ssZ", formatted);
 }

 /**
  * Test for format method with timezone and milliseconds precision.
  */
 @Test
 public void testFormatWithTimezoneAndMillis() {
 Date date = new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000); // two hours before current time
 String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/Paris"));
 assertEquals("Expected format to be yyyy-MM-ddThh:mm:ss.sss[Z|[+-]hh:mm]",
 "2022-05-18T14:33:55.123+02:00", formatted);
 }

 /**
  * Test for parse method with valid ISO-8601 formatted string and default timezone.
  */
 @Test
 public void testParseValid() throws ParseException {
 String dateString = "2022-05-18T14:33:55.123";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals("Expected parsed date to match input", dateString, ISO8601Utils.format(parsedDate));
 }

 /**
  * Test for parse method with valid ISO-8601 formatted string and custom timezone.
  */
 @Test
 public void testParseCustomTimezone() throws ParseException {
 String dateString = "2022-05-18T14:33:55.123+02:00";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 TimeZone customTimezone = TimeZone.getTimeZone("Europe/Paris");
 assertEquals("Expected parsed date to match input in custom timezone",
 "Thu May 19 16:33:55 CEST 2022", ISO8601Utils.format(parsedDate, false, customTimezone));
 }

 /**
  * Test for parse method with invalid ISO-8601 formatted string.
  */
 @Test(expected = ParseException.class)
 public void testParseInvalid() throws ParseException {
 String dateString = "2022-05-18T14:33:55Zzzz"; // invalid timezone indicator
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 fail("Expected ParseException to be thrown");
 }

}