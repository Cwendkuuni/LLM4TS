package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

 // Test constructor and getters
 @Test
 public void testConstructorAndGetters() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 assertEquals(Locale.US, stdDateFormat.getLocale());
 assertNull(stdDateFormat.getTimeZone());

 StdDateFormat stdDateFormatWithTimeZone = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.UK);
 assertEquals(Locale.UK, stdDateFormatWithTimeZone.getLocale());
 assertEquals(TimeZone.getTimeZone("UTC"), stdDateFormatWithTimeZone.getTimeZone());
 }

 // Test parsing with default locale and timezone
 @Test
 public void testParseWithDefaultLocaleAndTimezone() {
 Date date = new Date(); // get current date
 String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
 StdDateFormat stdDateFormat = new StdDateFormat();
 Date parsedDate = stdDateFormat.parse(dateString);
 assertEquals(date, parsedDate);
 }

 // Test parsing with specified locale and timezone
 @Test
 public void testParseWithSpecifiedLocaleAndTimezone() {
 Date date = new Date(); // get current date
 String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
 StdDateFormat stdDateFormat = new StdDateFormat(Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
 Date parsedDate = stdDateFormat.parse(dateString);
 assertEquals(date, parsedDate);
 }

 // Test parsing with invalid date string
 @Test(expected = ParseException.class)
 public void testParseWithInvalidDateString() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 stdDateFormat.parse("invalid date string");
 }

 // Test parsing with date strings that do not match any format
 @Test(expected = ParseException.class)
 public void testParseWithDateStringsThatDoNotMatchAnyFormat() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 stdDateFormat.parse("2023-03-30T14:30:59.123ABC");
 }

 // Test parsing with date strings that have invalid timezone indicators
 @Test(expected = ParseException.class)
 public void testParseWithDateStringsThatHaveInvalidTimezoneIndicators() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 stdDateFormat.parse("2023-03-30T14:30:59.123+11123");
 }

 // Test parsing with date strings that have missing milliseconds
 @Test
 public void testParseWithDateStringsThatHaveMissingMilliseconds() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 Date parsedDate = stdDateFormat.parse("2023-03-30T14:30:59");
 assertNotNull(parsedDate);
 }

 // Test parsing with date strings that have extra characters
 @Test(expected = ParseException.class)
 public void testParseWithDateStringsThatHaveExtraCharacters() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 stdDateFormat.parse("2023-03-30T14:30:59.123Z XYZ");
 }

 // Test formatting with default locale and timezone
 @Test
 public void testFormatWithDefaultLocaleAndTimezone() {
 Date date = new Date(); // get current date
 StdDateFormat stdDateFormat = new StdDateFormat();
 String formattedDateString = stdDateFormat.format(date, new StringBuilder(), new FieldPosition(0));
 assertEquals("Sat Mar 25 11:11:11 GMT+00:00 2023", formattedDateString.toString());
 }

 // Test formatting with specified locale and timezone
 @Test
 public void testFormatWithSpecifiedLocaleAndTimezone() {
 Date date = new Date(); // get current date
 StdDateFormat stdDateFormat = new StdDateFormat(Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
 String formattedDateString = stdDateFormat.format(date, new StringBuilder(), new FieldPosition(0));
 assertEquals("sam. 25 mars 2023 11:11:11 GMT+01:00", formattedDateString.toString());
 }

 // Test formatting with null locale and timezone
 @Test
 public void testFormatWithNullLocaleAndTimezone() {
 Date date = new Date(); // get current date
 StdDateFormat stdDateFormat = new StdDateFormat(null, null);
 String formattedDateString = stdDateFormat.format(date, new StringBuilder(), new FieldPosition(0));
 assertEquals("Sat Mar 25 11:11:11 GMT", formattedDateString.toString());
 }

 // Test formatting with specified timezone and null locale
 @Test
 public void testFormatWithSpecifiedTimezoneAndNullLocale() {
 Date date = new Date(); // get current date
 StdDateFormat stdDateFormat = new StdDateFormat(null, TimeZone.getTimeZone("UTC"));
 String formattedDateString = stdDateFormat.format(date, new StringBuilder(), new FieldPosition(0));
 assertEquals("Sat Mar 25 11:11:11 GMT", formattedDateString.toString());
 }

 // Test cloning
 @Test
 public void testCloning() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
 assertNotSame(stdDateFormat, clonedStdDateFormat);
 assertEquals(stdDateFormat.getLocale(), clonedStdDateFormat.getLocale());
 assertEquals(stdDateFormat.getTimeZone(), clonedStdDateFormat.getTimeZone());
 }

 // Test cloning with specified locale and timezone
 @Test
 public void testCloningWithSpecifiedLocaleAndTimezone() {
 StdDateFormat stdDateFormat = new StdDateFormat(Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris"));
 StdDateFormat clonedStdDateFormat = stdDateFormat.clone();
 assertEquals(stdDateFormat.getLocale(), clonedStdDateFormat.getLocale());
 assertEquals(stdDateFormat.getTimeZone(), clonedStdDateFormat.getTimeZone());
 }

 // Test setting timezone and locale
 @Test
 public void testSettingTimezoneAndLocale() {
 StdDateFormat stdDateFormat = new StdDateFormat();
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Locale locale = Locale.FRENCH;
 stdDateFormat.setTimeZone(timeZone);
 stdDateFormat.setLocale(locale);
 assertEquals(timeZone, stdDateFormat.getTimeZone());
 assertEquals(locale, stdDateFormat.getLocale());
 }
}