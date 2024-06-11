package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

public class FastDateFormatTest {

 private FastDateFormat getInstance(String pattern) {
 return FastDateFormat.getInstance(pattern);
 }

 @ org.junit.Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 assertNotNull(fastDateFormat);
 }

 @ org.junit.Test
 public void testGetInstanceAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @ org.junit.Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @ org.junit.Test
 public void testGetDateInstanceAllParams() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @ org.junit.Test
 public void testFormat() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 Date date = new Date();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 }

 @ org.junit.Test
 public void testFormatWithBuffer() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(new Date(), buffer);
 assertNotNull(buffer.toString());
 }

 @ org.junit.Test
 public void testFormatMilliseconds() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 long millis = System.currentTimeMillis();
 String result = fastDateFormat.format(millis);
 assertNotNull(result);
 }

 @ org.junit.Test
 public void testFormatMillisecondsWithBuffer() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy HH:mm:ss z");
 long millis = System.currentTimeMillis();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(millis, buffer);
 assertNotNull(buffer.toString());
 }

 @ org.junit.Test
 public void testFormatCalendar() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 Calendar calendar = Calendar.getInstance();
 String result = fastDateFormat.format(calendar.getTime());
 assertNotNull(result);
 }

 @ org.junit.Test
 public void testFormatCalendarWithBuffer() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 Calendar calendar = Calendar.getInstance();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(calendar, buffer);
 assertNotNull(buffer.toString());
 }

 @ org.junit.Test
 public void testParse() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 String dateString = "12-12-2022";
 Date date = fastDateFormat.parse(dateString);
 assertNotNull(date);
 }

 @ org.junit.Test
 public void testParseWithParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 String dateString = "12-12-2022";
 ParsePosition parsePosition = new ParsePosition(0);
 Date date = fastDateFormat.parse(dateString, parsePosition);
 assertNotNull(date);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @ org.junit.Test
 public void testParseObject() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 String dateString = "12-12-2022";
 Date date = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(date);
 }

 @ org.junit.Test(expected = ParseException.class)
 public void testParseInvalidPattern() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy-HH-mm");
 String dateString = "12-12-2022";
 fastDateFormat.parse(dateString);
 fail("Expected ParseException");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormatWithNullDate() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(null, buffer);
 fail("Expected IllegalArgumentException");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithNullString() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 fastDateFormat.parse(null);
 fail("Expected IllegalArgumentException");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormatWithNullBuffer() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 fastDateFormat.format(new Date(), null);
 fail("Expected IllegalArgumentException");
 }

 @ org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithNullParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 ParsePosition parsePosition = null;
 fastDateFormat.parse("12-12-2022", parsePosition);
 fail("Expected IllegalArgumentException");
 }

 @ org.junit.Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 String pattern = fastDateFormat.getPattern();
 assertNotNull(pattern);
 assertEquals("dd-MM-yyyy", pattern);
 }

 @ org.junit.Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @ org.junit.Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 }

 @ org.junit.Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 }

 @ org.junit.Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = getInstance("dd-MM-yyyy");
 FastDateFormat fastDateFormat2 = getInstance("dd-MM-yyyy");
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @ org.junit.Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = getInstance("dd-MM-yyyy");
 FastDateFormat fastDateFormat2 = getInstance("dd-MM-yyyy");
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @ org.junit.Test
 public void testToString() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 String result = fastDateFormat.toString();
 assertNotNull(result);
 }

 @ org.junit.Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = getInstance("dd-MM-yyyy");
 Calendar calendar = Calendar.getInstance();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.applyRules(calendar, buffer);
 assertNotNull(buffer.toString());
 }
}