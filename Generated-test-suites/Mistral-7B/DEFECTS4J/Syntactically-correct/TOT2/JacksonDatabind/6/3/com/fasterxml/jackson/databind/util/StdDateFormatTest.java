package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

 // Test constructor
 @Test
 public void testStdDateFormat() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNotNull(dateFormat);
 }

 // Test getters
 @Test
 public void testGetters() {
 StdDateFormat dateFormat = new StdDateFormat();
 assertNotNull(dateFormat._locale);
 assertNull(dateFormat._timezone);
 assertNull(dateFormat._formatRFC1123);
 assertNull(dateFormat._formatISO8601);
 assertNull(dateFormat._formatISO8601_z);
 assertNull(dateFormat._formatPlain);
 }

 // Test withTimeZone
 @Test
 public void testWithTimeZone() {
 StdDateFormat dateFormat = new StdDateFormat();
 TimeZone timeZone = TimeZone.getTimeZone("UTC+1");
 StdDateFormat withTimeZone = dateFormat.withTimeZone(timeZone);
 assertNotNull(withTimeZone);
 assertNotNull(withTimeZone._timezone);
 assertNull(withTimeZone._locale);
 assertNull(withTimeZone._formatRFC1123);
 assertNull(withTimeZone._formatISO8601);
 assertNull(withTimeZone._formatISO8601_z);
 assertNull(withTimeZone._formatPlain);
 assertNotEquals(dateFormat._timezone, withTimeZone._timezone);
 }

 // Test withLocale
 @Test
 public void testWithLocale() {
 StdDateFormat dateFormat = new StdDateFormat();
 Locale locale = Locale.FRENCH;
 StdDateFormat withLocale = dateFormat.withLocale(locale);
 assertNotNull(withLocale);
 assertNotNull(withLocale._locale);
 assertNull(withLocale._timezone);
 assertNull(withLocale._formatRFC1123);
 assertNull(withLocale._formatISO8601);
 assertNull(withLocale._formatISO8601_z);
 assertNull(withLocale._formatPlain);
 assertNotEquals(dateFormat._locale, withLocale._locale);
 }

 // Test clone
 @Test
 public void testClone() {
 StdDateFormat dateFormat = new StdDateFormat();
 StdDateFormat clone = dateFormat.clone();
 assertNotNull(clone);
 assertNotNull(clone._locale);
 assertNull(clone._timezone);
 assertNull(clone._formatRFC1123);
 assertNull(clone._formatISO8601);
 assertNull(clone._formatISO8601_z);
 assertNull(clone._formatPlain);
 assertNotEquals(dateFormat, clone);
 }

 // Test parse with default format
 @Test
 public void testParseWithDefaultFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-03-21T14:30:23.456Z";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 assertEquals(LocalDateTime.of(2022, 3, 21, 14, 30, 23, 456),
 date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
 }

 // Test parse with RFC1123 format
 @Test
 public void testParseWithRFC1123Format() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "Mon, 21 Mar 2022 14:30:23 GMT";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 assertEquals(LocalDateTime.of(2022, 3, 21, 14, 30, 23),
 date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
 }

 // Test parse with ISO8601 format (plain)
 @Test
 public void testParseWithISOPlanFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-03-21";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 assertEquals(LocalDateTime.of(2022, 3, 21, 0, 0),
 date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
 }

 // Test parse with ISO8601 format (with time)
 @Test
 public void testParseWithISOTimeFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-03-21T14:30:23";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 assertEquals(LocalDateTime.of(2022, 3, 21, 14, 30),
 date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
 }

 // Test parse with ISO8601 format (with time and timezone)
 @Test
 public void testParseWithISOTimeZoneFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "2022-03-21T14:30:23Z";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 assertEquals(LocalDateTime.of(2022, 3, 21, 14, 30),
 date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
 }

 // Test parse with invalid format
 @Test(expected = ParseException.class)
 public void testParseWithInvalidFormat() throws ParseException {
 StdDateFormat dateFormat = new StdDateFormat();
 String dateString = "InvalidDate";
 dateFormat.parse(dateString);
 }

 // Test format
 @Test
 public void testFormat() {
 StdDateFormat dateFormat = new StdDateFormat();
 Date date = new Date();
 String dateString = dateFormat.format(date, null, null);
 assertNotNull(dateString);
 assertNotNull(dateFormat._formatISO8601);
 assertEquals(dateFormat._formatISO8601.format(date), dateString);
 }
}