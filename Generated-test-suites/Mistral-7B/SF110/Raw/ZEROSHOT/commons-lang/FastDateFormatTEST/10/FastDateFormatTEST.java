package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

 @org.junit.BeforeClass
 static void setUpBeforeClass() throws Exception {
 FastDateFormat.cache.clear();
 }

 @org.junit.Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternAndTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new SimpleDateFormat("GMT+02:00").getTimeZone(), new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone(), new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone(), new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone(), new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testFormat() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String dateString = fastDateFormat.format(calendar);
 assertNotNull(dateString);
 assertTrue(dateString.length() > 0);
 }

 @org.junit.Test
 public void testFormatWithPattern() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String dateString = fastDateFormat.format(calendar);
 assertNotNull(dateString);
 assertTrue(dateString.length() > 0);
 }

 @org.junit.Test
 public void testFormatWithMilliseconds() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
 String dateString = fastDateFormat.format(millis);
 assertNotNull(dateString);
 assertTrue(dateString.length() > 0);
 }

 @org.junit.Test
 public void testParse() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 String dateString = "15 August, 2012";
 Date parseDate = fastDateFormat.parse(dateString);
 assertNotNull(parseDate);
 }

 @org.junit.Test
 public void testParseWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String dateString = "15-08-2012 12:34:56";
 Date parseDate = fastDateFormat.parse(dateString);
 assertNotNull(parseDate);
 }

 @org.junit.Test
 public void testParseWithMilliseconds() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
 String dateString = "15 August, 2012 12:34:56.789";
 Date parseDate = fastDateFormat.parse(dateString);
 assertNotNull(parseDate);
 }

 @org.junit.Test
 public void testParseWithInvalidPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy-HH:mm:ss");
 String dateString = "15-08-2012-12:34:56";
 try {
 FastDateFormat.parse(dateString);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // expected
 }
 }

 @org.junit.Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String pattern = fastDateFormat.getPattern();
 assertNotNull(pattern);
 assertTrue(pattern.length() > 0);
 }

 @org.junit.Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @org.junit.Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 }

 @org.junit.Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 assertTrue(maxLengthEstimate > 0);
 }

 @org.junit.Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @org.junit.Test
 public void testNotEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 Object obj = new Object();
 assertFalse(fastDateFormat1.equals(obj));
 }

 @org.junit.Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @org.junit.Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String toString = fastDateFormat.toString();
 assertNotNull(toString);
 assertTrue(toString.length() > 0);
 }

 @org.junit.Test
 public void testApplyRules() {
 Calendar calendar = Calendar.getInstance();
 calendar.setTime(new Date());
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 StringBuffer buf = new StringBuffer();
 String rulesApplied = fastDateFormat.applyRules(calendar, buf);
 assertNotNull(rulesApplied);
 assertTrue(rulesApplied.length() > 0);
 }
}