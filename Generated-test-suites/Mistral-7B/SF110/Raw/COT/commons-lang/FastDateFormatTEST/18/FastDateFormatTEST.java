package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class FastDateFormatTest {

 private FastDateFormat getFastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
 return FastDateFormat.getInstance(pattern, timeZone, locale);
 }

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z", Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
 Assert.assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormatStringBuffer() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Calendar calendar = new GregorianCalendar(2022, 11, 30, 15, 30, 0, Calendar.MILLISECOND);
 StringBuffer output = new StringBuffer();
 fastDateFormat.applyRules(calendar, output);
 String expected = "30-12-2022 15:30:00 h AP HUT";
 Assert.assertEquals(expected, output.toString());
 }

 @Test
 public void testFormatDate() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Date date = new Date();
 String expected = "30-12-2022 15:30:00 h AP HUT";
 String result = fastDateFormat.format(date);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatCalendar() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Calendar calendar = new GregorianCalendar(2022, 11, 30, 15, 30, 0, Calendar.MILLISECOND);
 String expected = "30-12-2022 15:30:00 h AP HUT";
 String result = fastDateFormat.format(calendar);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatLongMilliseconds() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 long milliseconds = System.currentTimeMillis();
 String expected = fastDateFormat.format(milliseconds);
 String result = fastDateFormat.format(milliseconds, new StringBuffer());
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatDateWithPattern() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Date date = new Date();
 String expected = "30-12-2022 15:30:00 h AP HUT";
 String result = fastDateFormat.format(date, "dd-MM-yyyy hh:mm:ss a z");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatCalendarWithPattern() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Calendar calendar = new GregorianCalendar(2022, 11, 30, 15, 30, 0, Calendar.MILLISECOND);
 String expected = "30-12-2022 15:30:00 h AP HUT";
 String result = fastDateFormat.format(calendar, "dd-MM-yyyy hh:mm:ss a z");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testFormatLongMillisecondsWithPattern() throws Exception {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 long milliseconds = System.currentTimeMillis();
 String expected = "30-12-2022 15:30:00 h AP HUT";
 String result = fastDateFormat.format(milliseconds, "dd-MM-yyyy hh:mm:ss a z");
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidPattern() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 String input = "InvalidPattern";
 fastDateFormat.parse(input);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidDate() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 String input = "31-13-2022";
 fastDateFormat.parse(input);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidDateTimeZone() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 String input = "31-12-2022 24:60:00 EET";
 fastDateFormat.parse(input);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidLocale() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.ROOT);
 String input = "31-12-2022 24:60:00 h AP HUT";
 fastDateFormat.parse(input);
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 String expected = "dd-MM-yyyy hh:mm:ss a z";
 String result = fastDateFormat.getPattern();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 TimeZone expected = TimeZone.getTimeZone("UTC");
 TimeZone result = fastDateFormat.getTimeZone();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 Locale expected = Locale.FRENCH;
 Locale result = fastDateFormat.getLocale();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 int expected = 31;
 int result = fastDateFormat.getMaxLengthEstimate();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 FastDateFormat fastDateFormat2 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 FastDateFormat fastDateFormat3 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
 Assert.assertTrue(fastDateFormat1.equals(fastDateFormat2));
 Assert.assertFalse(fastDateFormat1.equals(fastDateFormat3));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 FastDateFormat fastDateFormat2 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 FastDateFormat fastDateFormat3 = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
 Assert.assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 Assert.assertNotEquals(fastDateFormat1.hashCode(), fastDateFormat3.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = getFastDateFormat("dd-MM-yyyy hh:mm:ss a z", TimeZone.getTimeZone("UTC"), Locale.FRENCH);
 String expected = "FastDateFormat[dd-MM-yyyy hh:mm:ss a z, fr_FR, UTC]";
 String result = fastDateFormat.toString();
 Assert.assertEquals(expected, result);
 }
}