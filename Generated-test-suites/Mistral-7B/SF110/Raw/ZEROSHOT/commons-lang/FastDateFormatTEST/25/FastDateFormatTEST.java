package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParsePosition;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

 @org.junit.Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a");
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", new TimeZone("UTC"));
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a", new TimeZone("UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"));
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"));
 assertNotNull(fastDateFormat);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new TimeZone("UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("hh:mm a z", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm a", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm a", fastDateFormat.getPattern());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, new TimeZone("UTC"));
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm a", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, new TimeZone("UTC"), Locale.US);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy hh:mm a", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 assertEquals(Locale.US, fastDateFormat.getLocale());
 }

 @org.junit.Test
 public void testFormat() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Date date = calendar.getTime();
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 }

 @org.junit.Test
 public void testFormatWithPattern() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy hh:mm:ss a");
 Date date = calendar.getTime();
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testFormatWithCalendar() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String formattedDate = fastDateFormat.format(calendar);
 assertNotNull(formattedDate);
 }

 @org.junit.Test
 public void testFormatWithLong() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 long millis = System.currentTimeMillis();
 String formattedDate = fastDateFormat.format(millis);
 assertNotNull(formattedDate);
 }

 @org.junit.Test
 public void testFormatWithStringBuffer() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 StringBuffer stringBuffer = new StringBuffer();
 String formattedDate = fastDateFormat.format(calendar, stringBuffer);
 assertNotNull(formattedDate);
 assertEquals(formattedDate.toString(), fastDateFormat.format(calendar).toString());
 }

 @org.junit.Test
 public void testFormatWithLongStringBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 long millis = System.currentTimeMillis();
 StringBuffer stringBuffer = new StringBuffer();
 String formattedDate = fastDateFormat.format(millis, stringBuffer);
 assertNotNull(formattedDate);
 assertEquals(formattedDate.toString(), fastDateFormat.format(millis).toString());
 }

 @org.junit.Test
 public void testParse() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String dateString = "25/12/2021 10:30:00 AM";
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 }

 @org.junit.Test
 public void testParseWithParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String dateString = "25/12/2021 10:30:00 AM";
 ParsePosition parsePosition = new ParsePosition(0);
 Date parsedDate = fastDateFormat.parse(dateString, parsePosition);
 assertNotNull(parsedDate);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @org.junit.Test
 public void testParseObject() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String dateString = "25/12/2021 10:30:00 AM";
 Date parsedDate = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(parsedDate);
 }

 @org.junit.Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals("dd/MM/yyyy hh:mm:ss a", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals("default time zone", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals("default locale", fastDateFormat.getLocale().getLanguage());
 }

 @org.junit.Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertEquals(32, fastDateFormat.getMaxLengthEstimate());
 }

 @org.junit.Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @org.junit.Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @org.junit.Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String toString = fastDateFormat.toString();
 assertNotNull(toString);
 }

 @org.junit.Test
 public void testApplyRulesWithCalendarStringBuffer() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 StringBuffer stringBuffer = new StringBuffer();
 fastDateFormat.applyRules(calendar, stringBuffer);
 assertNotNull(stringBuffer.toString());
 }
}