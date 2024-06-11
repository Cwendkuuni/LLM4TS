package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() {
 Date date = new Date(1640995200000L); // 21-Mar-2022 12:00:00
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2022-03-21T12:00:00Z", formattedDate);
 }

 @Test
 public void testFormatDateWithTimezone() {
 Date date = new Date(1640995200000L); // 21-Mar-2022 12:00:00
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/Los_Angeles"));
 assertEquals("2022-03-21T19:00:00-07:00", formattedDate);
 }

 @Test
 public void testFormatDateWithMilliseconds() {
 Date date = new Date(1640995200001L); // 21-Mar-2022 12:00:00.001
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals("2022-03-21T12:00:00.001Z", formattedDate);
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "2022-03-21T12:00:00Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse("2022-03-21T12:00:00Z").getTime(), parsedDate.getTime());
 }

 @Test
 public void testParseDateWithTimezoneOffset() throws ParseException {
 String dateString = "2022-03-21T12:00:00-07:00";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse("2022-03-21T19:00:00Z").getTime(), parsedDate.getTime());
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 String dateString = "2022-03-21T12:00:00A";
 ISO8601Utils.parse(dateString, new ParsePosition(0));
 }

 @Test
 public void testParseDateWithInvalidTimezoneIndicator() throws ParseException {
 String dateString = "2022-03-21T12:00:00X";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseDateWithInvalidTimezoneOffset() throws ParseException {
 String dateString = "2022-03-21T12:00:00--1";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testFormatDateWithInvalidTimezone() {
 try {
 ISO8601Utils.format(new Date(), false, new SimpleDateFormat("GMT+01:00").getTimeZone());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatDateWithInvalidFormat() {
 try {
 ISO8601Utils.format(new Date(), false, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}