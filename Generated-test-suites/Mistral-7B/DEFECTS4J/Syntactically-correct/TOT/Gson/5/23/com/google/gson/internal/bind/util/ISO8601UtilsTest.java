package com.google.gson.internal.bind.util;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ISO8601UtilsTest {

 private ISO8601Utils utils;
 private TimeZone utcTimeZone;

 @Before
 public void setUp() {
 utils = new ISO8601Utils();
 utcTimeZone = TimeZone.getTimeZone("UTC");
 }

 @Test
 public void testFormatUtcDateWithNoMillis() {
 Calendar calendar = new GregorianCalendar(utcTimeZone, Locale.US);
 calendar.setTime(new Date(1609459200000L)); // 2020-10-01
 String formattedDate = utils.format(calendar.getTime());
 assertEquals("2020-10-01T00:00:00Z", formattedDate);
 }

 @Test
 public void testFormatUtcDateWithMillis() {
 Calendar calendar = new GregorianCalendar(utcTimeZone, Locale.US);
 calendar.setTime(new Date(1609459200001L)); // 2020-10-01T00:00:01Z
 String formattedDate = utils.format(calendar.getTime(), true);
 assertEquals("2020-10-01T00:00:01.001Z", formattedDate);
 }

 @Test
 public void testFormatLocalDateWithNoMillis() {
 Calendar calendar = new GregorianCalendar(Locale.US);
 calendar.setTime(new Date(1609459200000L)); // 2020-10-01
 String formattedDate = utils.format(calendar.getTime());
 assertEquals("2020-10-01T07:00:00-04:00", formattedDate); // assuming local time is in EDT
 }

 @Test
 public void testFormatLocalDateWithMillis() {
 Calendar calendar = new GregorianCalendar(Locale.US);
 calendar.setTime(new Date(1609459200001L)); // 2020-10-01T00:00:01Z
 String formattedDate = utils.format(calendar.getTime(), true);
 assertEquals("2020-10-01T00:00:01.001-04:00", formattedDate); // assuming local time is in EDT
 }

 @Test
 public void testParseUTCDate() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T00:00:00Z", pos);
 assertEquals("Thu Oct 01 00:00:00 EDT 2020", parsedDate.toString());
 assertEquals(0, pos.getIndex());
 }

 @Test
 public void testParseUTCDateWithMillis() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T00:00:01.001Z", pos);
 assertEquals("Thu Oct 01 00:00:01 EDT 2020", parsedDate.toString());
 assertEquals(6, pos.getIndex());
 }

 @Test
 public void testParseLocalDate() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T07:00:00-04:00", pos);
 assertEquals("Thu Oct 01 11:00:00 EDT 2020", parsedDate.toString());
 assertEquals(15, pos.getIndex());
 }

 @Test
 public void testParseLocalDateWithMillis() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T07:00:01.001-04:00", pos);
 assertEquals("Thu Oct 01 11:00:01 EDT 2020", parsedDate.toString());
 assertEquals(19, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T07:00:00X", pos);
 assertNull(parsedDate);
 assertEquals(6, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTimeZone() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("2020-10-01T07:00:00-04:60", pos);
 assertNull(parsedDate);
 assertEquals(13, pos.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseNoDate() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = utils.parse("Z", pos);
 assertNull(parsedDate);
 assertEquals(1, pos.getIndex());
 }
}