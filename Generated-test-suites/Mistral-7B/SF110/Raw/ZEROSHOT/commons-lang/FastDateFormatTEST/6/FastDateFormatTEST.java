package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;

public class FastDateFormatTest {

 private FastDateFormat getInstance() {
 return FastDateFormat.getInstance();
 }

 @org.junit.Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = getInstance();
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss");
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", new UTCTimeZone());
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss", fastDateFormat.getPattern());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss", fastDateFormat.getPattern());
 assertEquals("fr_FR", fastDateFormat.getLocale().getDisplayName());
 }

 @org.junit.Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss", new Locale("fr", "FR"), new UTCTimeZone());
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss", fastDateFormat.getPattern());
 assertEquals("fr_FR", fastDateFormat.getLocale().getDisplayName());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
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
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new Locale("fr", "FR"), new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, new Locale("fr", "FR"), new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new Locale("fr", "FR"));
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new Locale("fr", "FR"), new UTCTimeZone());
 assertNotNull(fastDateFormat);
 }

 @org.junit.Test
 public void testFormat() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTimeInMillis(System.currentTimeMillis());
 FastDateFormat fastDateFormat = getInstance();
 String formatted = fastDateFormat.format(calendar);
 assertNotNull(formatted);
 }

 @org.junit.Test
 public void testFormatWithPattern() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTimeInMillis(System.currentTimeMillis());
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
 String formatted = fastDateFormat.format(calendar);
 assertNotNull(formatted);
 assertEquals("yyyy-MM-dd HH:mm:ss.SSS", fastDateFormat.getPattern());
 }

 @org.junit.Test
 public void testFormatWithTimeZone() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTimeInMillis(System.currentTimeMillis());
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new UTCTimeZone());
 String formatted = fastDateFormat.format(calendar);
 assertNotNull(formatted);
 }

 @org.junit.Test
 public void testFormatWithLocale() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTimeInMillis(System.currentTimeMillis());
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new Locale("fr", "FR"));
 String formatted = fastDateFormat.format(calendar);
 assertNotNull(formatted);
 }

 @org.junit.Test
 public void testFormatWithLocaleTimeZone() throws ParseException {
 Calendar calendar = Calendar.getInstance();
 calendar.setTimeInMillis(System.currentTimeMillis());
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", new Locale("fr", "FR"), new UTCTimeZone());
 String formatted = fastDateFormat.format(calendar);
 assertNotNull(formatted);
 }

 @org.junit.Test
 public void testParse() throws ParseException {
 FastDateFormat fastDateFormat = getInstance();
 Date date = fastDateFormat.parse("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseWithPattern() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Date date = fastDateFormat.parse("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseWithTimeZone() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new UTCTimeZone());
 Date date = fastDateFormat.parse("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseWithLocale() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new Locale("fr", "FR"));
 Date date = fastDateFormat.parse("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseWithLocaleTimeZone() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new Locale("fr", "FR"), new UTCTimeZone());
 Date date = fastDateFormat.parse("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseObject() throws ParseException {
 FastDateFormat fastDateFormat = getInstance();
 Date date = (Date)fastDateFormat.parseObject("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseObjectWithPattern() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 Date date = (Date)fastDateFormat.parseObject("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseObjectWithTimeZone() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new UTCTimeZone());
 Date date = (Date)fastDateFormat.parseObject("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseObjectWithLocale() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new Locale("fr", "FR"));
 Date date = (Date)fastDateFormat.parseObject("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testParseObjectWithLocaleTimeZone() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", new Locale("fr", "FR"), new UTCTimeZone());
 Date date = (Date)fastDateFormat.parseObject("25-12-2021");
 assertNotNull(date);
 }

 @org.junit.Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = getInstance();
 String pattern = fastDateFormat.getPattern();
 assertNotNull(pattern);
 }

 @org.junit.Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = getInstance();
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @org.junit.Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = getInstance();
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 }

 @org.junit.Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = getInstance();
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 }

 @org.junit.Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = getInstance();
 FastDateFormat fastDateFormat2 = getInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @org.junit.Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = getInstance();
 FastDateFormat fastDateFormat2 = getInstance();
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @org.junit.Test
 public void testToString() {
 FastDateFormat fastDateFormat = getInstance();
 String toString = fastDateFormat.toString();
 assertNotNull(toString);
 }

 @org.junit.Test
 public void testApplyRules() {
 Calendar calendar = Calendar.getInstance();
 FastDateFormat fastDateFormat = getInstance();
 StringBuffer buf = new StringBuffer();
 fastDateFormat.applyRules(calendar, buf);
 assertNotNull(buf.toString());
 }
}