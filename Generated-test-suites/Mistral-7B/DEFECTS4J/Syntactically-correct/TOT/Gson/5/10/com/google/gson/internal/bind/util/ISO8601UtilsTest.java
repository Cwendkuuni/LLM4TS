package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Test for ISO8601Utils class.
 */
public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 24, 14, 30, 45, 123)
 .getTime();
 String formatDate = ISO8601Utils.format(date);
 assertEquals("2022-11-24T14:30:45.123Z", formatDate);
 }

 @Test
 public void testFormatDateMilliseconds() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 24, 14, 30, 45, 123)
 .getTime();
 String formatDateMillis = ISO8601Utils.format(date, true, TimeZone.getDefault());
 assertEquals("2022-11-24T14:30:45.123Z", formatDateMillis);
 }

 @Test
 public void testFormatDateUTC() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 24, 14, 30, 45, 123)
 .getTime();
 String formatDateUTC = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
 assertEquals("2022-11-24T14:30:45.123Z", formatDateUTC);
 }

 @Test
 public void testFormatDateNonUTC() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 24, 14, 30, 45, 123)
 .getTime();
 String formatDateNonUTC = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Europe/Paris"));
 assertEquals("2022-11-24T15:30:45.123+01:00", formatDateNonUTC);
 }

 @Test
 public void testParseDateUTC() throws ParseException {
 String dateString = "2022-11-24T14:30:45.123Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Thu Nov 24 14:30:45 CET 2022", parsedDate.toString());
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseDateNonUTC() throws ParseException {
 String dateString = "2022-11-24T15:30:45.123+01:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Fri Nov 25 13:30:45 CET 2022", parsedDate.toString());
 assertEquals(dateString.length(), pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() throws ParseException {
 String dateString = "2022-11-24T14:30:45.123A";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDateInvalidTimeZone() throws ParseException {
 String dateString = "2022-11-24T14:30:45.123A";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos, new TimeZone("InvalidTimeZone"));
 }

 @Test
 public void testParseDateUTCWithOffset() throws ParseException {
 String dateString = "2022-11-24T14:30:45.123+02:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Thu Nov 24 15:30:45 CET 2022", parsedDate.toString());
 assertEquals(dateString.length() - 3, pos.getIndex());
 }

 @Test
 public void testParseDateInvalidOffset() throws ParseException {
 String dateString = "2022-11-24T14:30:45.123+04:60";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }
}