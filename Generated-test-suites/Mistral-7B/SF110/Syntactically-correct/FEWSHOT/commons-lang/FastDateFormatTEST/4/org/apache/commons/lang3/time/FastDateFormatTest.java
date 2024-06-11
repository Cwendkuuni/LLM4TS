package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.junit.Test;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInstanceWithNullPattern() {
 FastDateFormat.getInstance(null);
 }

 @Test
 public void testGetInstanceWithDefaultPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithCustomPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithCustomTimeZone() {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z", timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetInstanceWithCustomLocale() {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z", null, locale);
 assertNotNull(fastDateFormat);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithCustomPatternTimeZoneAndLocale() {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z", timeZone, locale);
 assertNotNull(fastDateFormat);
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithCustomLocale() {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
 assertNotNull(fastDateFormat);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithCustomTimeZone() {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateInstanceWithCustomPatternLocaleAndTimeZone() {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance("dd/MM/yyyy", locale, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy", fastDateFormat.getPattern());
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithCustomLocale() {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
 assertNotNull(fastDateFormat);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithCustomTimeZone() {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetTimeInstanceWithCustomPatternLocaleAndTimeZone() {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance("HH:mm", locale, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("HH:mm", fastDateFormat.getPattern());
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithCustomLocale() {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
 assertNotNull(fastDateFormat);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithCustomTimeZone() {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateTimeInstanceWithCustomPatternLocaleAndTimeZone() {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale, timeZone);
 assertNotNull(fastDateFormat);
 assertEquals("dd/MM/yyyy HH:mm:ss z", fastDateFormat.getPattern());
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testFormat() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 }

 @Test
 public void testFormatWithCustomLocale() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testFormatWithCustomTimeZone() throws ParseException {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", null, timeZone);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testFormatWithCustomPatternLocaleAndTimeZone() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale, timeZone);
 Calendar calendar = Calendar.getInstance();
 Date date = calendar.getTime();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testParse() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test
 public void testParseWithCustomLocale() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testParseWithCustomTimeZone() throws ParseException {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", null, timeZone);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testParseWithCustomPatternLocaleAndTimeZone() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale, timeZone);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testParseObject() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(parsedDate);
 }

 @Test
 public void testParseObjectWithCustomLocale() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(parsedDate);
 assertEquals(locale, fastDateFormat.getLocale());
 }

 @Test
 public void testParseObjectWithCustomTimeZone() throws ParseException {
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", null, timeZone);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(parsedDate);
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testParseObjectWithCustomPatternLocaleAndTimeZone() throws ParseException {
 Locale locale = new Locale("fr", "FR");
 TimeZone timeZone = new SimpleTimeZone(0, "UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance("dd/MM/yyyy HH:mm:ss z", locale, timeZone);
 String dateString = "21/06/2023 08:25:47 EDT";
 Date parsedDate = (Date)fastDateFormat.parseObject(dateString);
 assertNotNull(parsedDate);
 assertEquals(locale, fastDateFormat.getLocale());
 assertEquals("UTC", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 assertEquals("dd/MM/yyyy HH:mm:ss z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 assertEquals(TimeZone.getDefault(), fastDateFormat.getTimeZone());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 assertEquals(Locale.getDefault(), fastDateFormat.getLocale());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 assertEquals(32, fastDateFormat.getMaxLengthEstimate());
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance();
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 assertEquals("FastDateFormat[dd/MM/yyyy HH:mm:ss z, default locale, default timezone]", fastDateFormat.toString());
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance();
 Calendar calendar = Calendar.getInstance();
 StringBuffer result = new StringBuffer();
 fastDateFormat.applyRules(calendar, result);
 assertNotNull(result.toString());
 }

}