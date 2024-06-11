package org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.apache.commons.lang3.time.FastDatePrinter;
import org.apache.commons.lang3.time.FastDateParser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;
import java.text.ParsePosition;

public class FastDateFormatTest {

 private static final String DEFAULT_PATTERN = "dd-MM-yyyy HH:mm:ss z";
 private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
 private static final Locale DEFAULT_LOCALE = Locale.getDefault();

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, DEFAULT_TIME_ZONE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, DEFAULT_TIME_ZONE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, DEFAULT_TIME_ZONE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testParseDate() {
 final String dateString = "01-01-2023 12:34:56 EDT";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(dateString);
 Date date = fastDateFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() {
 final String dateString = "InvalidFormat";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(dateString);
 try {
 fastDateFormat.parse(dateString);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseDateWithPattern() {
 final String dateString = "01-Jan-2023 12:34:56 EDT";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 Date date = fastDateFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithPatternInvalidFormat() {
 final String dateString = "InvalidFormat";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 try {
 fastDateFormat.parse(dateString);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFormat() {
 final Date date = new Date();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatWithPattern() {
 final Date date = new Date();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 }

 @Test
 public void testFormatWithPatternTimeZone() {
 final Date date = new Date();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE);
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 }

 @Test
 public void testFormatWithPatternLocale() {
 final Date date = new Date();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_LOCALE);
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testFormatWithPatternTimeZoneLocale() {
 final Date date = new Date();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testFormatMilliseconds() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatMillisecondsWithPattern() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 }

 @Test
 public void testFormatMillisecondsWithPatternTimeZone() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE);
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 }

 @Test
 public void testFormatMillisecondsWithPatternLocale() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_LOCALE);
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testFormatMillisecondsWithPatternTimeZoneLocale() {
 long millis = System.currentTimeMillis();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN, DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 assertEquals(DEFAULT_PATTERN, fastDateFormat.getPattern());
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testParseObjectWithParsePosition() {
 final String dateString = "01-Jan-2023 12:34:56 EDT";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 ParsePosition parsePosition = new ParsePosition(0);
 Date date = (Date)fastDateFormat.parseObject(dateString, parsePosition);
 assertNotNull(date);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithParsePositionInvalidFormat() {
 final String dateString = "InvalidFormat";
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
 ParsePosition parsePosition = new ParsePosition(0);
 try {
 fastDateFormat.parseObject(dateString, parsePosition);
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals("dd-MM-yyyy HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals(DEFAULT_TIME_ZONE, fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals(DEFAULT_LOCALE, fastDateFormat.getLocale());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals(35, fastDateFormat.getMaxLengthEstimate());
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testNotEqualsDifferentObjects() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = new Object();
 assertFalse(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String toString = fastDateFormat.toString();
 assertTrue(toString.startsWith("FastDateFormat["));
 }

 @Test
 public void testApplyRules() {
 final Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 StringBuffer buffer = new StringBuffer();
 String formattedDate = fastDateFormat.applyRules(calendar, buffer);
 assertNotNull(formattedDate);
 }
}