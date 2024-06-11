package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() {
 // Test formatting a date with no time or milliseconds
 Date date = new GregorianCalendar(2023, 1, 31).getTime();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2023-02-01", formattedDate);

 // Test formatting a date with time and milliseconds
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US, 2023, 1, 31, 12, 30, 500);
 Date dateWithTimeAndMillis = calendar.getTime();
 String formattedDateWithTimeAndMillis = ISO8601Utils.format(dateWithTimeAndMillis, true, TimeZone.getTimeZone("UTC"));
 assertEquals("2023-02-01T12:30:500Z", formattedDateWithTimeAndMillis);
 }

 @Test
 public void testFormatDateWithInvalidTimeZone() {
 // Test formatting a date with an invalid timezone
 Date date = new GregorianCalendar(2023, 1, 31).getTime();
 String formattedDateWithInvalidTimeZone = ISO8601Utils.format(date, true, new SimpleDateFormat("GMT+00:00").getTimeZone());
 assertNull(formattedDateWithInvalidTimeZone);
 }

 @Test
 public void testParseDate() throws ParseException {
 // Test parsing a date with no time or milliseconds
 String dateString = "2023-02-01";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(new GregorianCalendar(2023, 1, 1).getTime(), parsedDate);

 // Test parsing a date with time and milliseconds
 String dateStringWithTimeAndMillis = "2023-02-01T12:30:500Z";
 Date parsedDateWithTimeAndMillis = ISO8601Utils.parse(dateStringWithTimeAndMillis, new ParsePosition(0));
 assertEquals(new GregorianCalendar(2023, 1, 1, 12, 30, 500, 0, TimeZone.getTimeZone("UTC").getRawOffset()).getTime(), parsedDateWithTimeAndMillis.getTime());
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 // Test parsing a date with an invalid format
 String dateStringWithInvalidFormat = "2023-02-01T12:30:500E";
 ISO8601Utils.parse(dateStringWithInvalidFormat, new ParsePosition(0));
 }

 @Test
 public void testParseDateWithInvalidTimeZone() throws ParseException {
 // Test parsing a date with an invalid timezone
 String dateString = "2023-02-01T12:30:500Z";
 ParsePosition pos = new ParsePosition(0);
 // Create a SimpleDateFormat object with an invalid timezone, which will throw an exception when used to parse a date
 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 sdf.setTimeZone(new SimpleTimeZone(1000, "InvalidTimeZone"));
 Date parsedDate = ISO8601Utils.parse(dateString, pos, sdf);
 assertNull(parsedDate);
 assertEquals(-1, pos.getIndex());
 }
}