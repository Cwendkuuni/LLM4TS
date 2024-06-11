package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import org.junit.Test;

public class FastDateFormatTest {

 @Test
 public void testInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", Locale.ITALY);
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z", new TimeZone("GMT+5:30"), Locale.ITALY);
 assertNotNull(fastDateFormat);
 assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ITALY);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ITALY, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.ITALY);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.ITALY, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.ITALY);
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.ITALY, new TimeZone("GMT+5:30"));
 assertNotNull(fastDateFormat);
 assertEquals(FastDateFormat.FULL, fastDateFormat.getDateStyle());
 assertEquals(FastDateFormat.FULL, fastDateFormat.getTimeStyle());
 assertEquals(Locale.ITALY, fastDateFormat.getLocale());
 assertEquals("Asia/Calcutta", fastDateFormat.getTimeZone().getID());
 }

 @Test
 public void testFormatLong() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 String result = fastDateFormat.format(System.currentTimeMillis());
 assertNotNull(result);
 }

 @Test
 public void testFormatDate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Date date = new Date();
 String result = fastDateFormat.format(date);
 assertNotNull(result);
 }

 @Test
 public void testFormatCalendar() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Calendar calendar = Calendar.getInstance();
 String result = fastDateFormat.format(calendar.getTime());
 assertNotNull(result);
 }

 @Test
 public void testFormatLongWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(System.currentTimeMillis(), buffer);
 assertNotNull(buffer);
 assertTrue(buffer.length() > 0);
 }

 @Test
 public void testFormatDateWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Date date = new Date();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(date, buffer);
 assertNotNull(buffer);
 assertTrue(buffer.length() > 0);
 }

 @Test
 public void testFormatCalendarWithBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Calendar calendar = Calendar.getInstance();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.format(calendar.getTime(), buffer);
 assertNotNull(buffer);
 assertTrue(buffer.length() > 0);
 }

 @Test
 public void testParseString() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 String dateString = "25-12-2021 10:30:45.123 GMT+5:30";
 Date date = fastDateFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test
 public void testParseStringWithParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 String dateString = "25-12-2021 10:30:45.123 GMT+5:30";
 ParsePosition parsePosition = new ParsePosition(0);
 Date date = fastDateFormat.parse(dateString, parsePosition);
 assertNotNull(date);
 assertEquals(dateString.length(), parsePosition.getIndex());
 }

 @Test
 public void testParseObjectString() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Object date = fastDateFormat.parseObject("25-12-2021 10:30:45.123 GMT+5:30", new ParsePosition(0));
 assertNotNull(date);
 assertInstanceOf(Date.class, date);
 }

 @Test
 public void testParseObjectStringWithParsePosition() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Object date = fastDateFormat.parseObject("25-12-2021 10:30:45.123 GMT+5:30", new ParsePosition(0));
 assertNotNull(date);
 assertInstanceOf(Date.class, date);
 ParsePosition parsePosition = (ParsePosition)date;
 assertEquals("25-12-2021 10:30:45.123 GMT+5:30".length(), parsePosition.getIndex());
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertEquals("dd-MM-yyyy HH:mm:ss.SSS z", fastDateFormat.getPattern());
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 assertEquals("GMT+00:00", timeZone.getID());
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 assertEquals("en_US", locale.getLanguage());
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertTrue(fastDateFormat1.hashCode() == fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 assertEquals("FastDateFormat[dd-MM-yyyy HH:mm:ss.SSS z, en_US, GMT+00:00]", fastDateFormat.toString());
 }

 @Test
 public void testApplyRulesWithCalendarAndBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss.SSS z");
 Calendar calendar = Calendar.getInstance();
 StringBuffer buffer = new StringBuffer();
 fastDateFormat.applyRules(calendar, buffer);
 assertNotNull(buffer);
 assertTrue(buffer.length() > 0);
 }
}