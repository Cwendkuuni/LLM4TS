package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

public class FastDateFormatTest {

 private FormatCache<FastDateFormat> cache = new FormatCache<FastDateFormat>() {
  @Override
  protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
    return new FastDateFormat(pattern, timeZone, locale);
  }
 };

 @org.junit.Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testFormat() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String dateString = "25-12-2022 10:30:45";
 Date date = new Date(System.currentTimeMillis());
 StringBuilder stringBuilder = new StringBuilder();
 FastDateFormat formatter = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String formattedDate = formatter.format(date);
 assertEquals(dateString, formattedDate);
 }

 @org.junit.Test
 public void testParse() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String dateString = "25-12-2022 10:30:45";
 Date date = fastDateFormat.parse(dateString);
 assertNotNull(date);
 }

 @org.junit.Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String dateString = "25-12-2022 10:30:45Z";
 fastDateFormat.parse(dateString);
 }

 @org.junit.Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 String pattern = fastDateFormat.getPattern();
 assertEquals("dd-MM-yyyy HH:mm:ss", pattern);
 }

 @org.junit.Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @org.junit.Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 Locale locale = fastDateFormat.getLocale();
 assertNull(locale);
 }

 @org.junit.Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertEquals(19, maxLengthEstimate);
 }

 @org.junit.Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @org.junit.Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 FastDateFormat fastDateFormat3 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 FastDateFormat fastDateFormat4 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 FastDateFormat fastDateFormat5 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", Locale.FRANCE);
 FastDateFormat fastDateFormat6 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"));
 FastDateFormat fastDateFormat7 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.FRANCE);
 boolean equals1 = fastDateFormat1.equals(fastDateFormat1);
 assertTrue(equals1);
 boolean equals2 = fastDateFormat1.equals(fastDateFormat2);
 assertTrue(equals2);
 boolean equals3 = fastDateFormat1.equals(fastDateFormat3);
 assertTrue(equals3);
 boolean equals4 = fastDateFormat1.equals(fastDateFormat4);
 assertTrue(equals4);
 boolean equals5 = fastDateFormat1.equals(fastDateFormat5);
 assertFalse(equals5);
 boolean equals6 = fastDateFormat1.equals(fastDateFormat6);
 assertFalse(equals6);
 boolean equals7 = fastDateFormat1.equals(fastDateFormat7);
 assertFalse(equals7);
 boolean equals8 = fastDateFormat1.equals(null);
 assertFalse(equals8);
 boolean equals9 = fastDateFormat1.equals("String");
 assertFalse(equals9);
 }
}