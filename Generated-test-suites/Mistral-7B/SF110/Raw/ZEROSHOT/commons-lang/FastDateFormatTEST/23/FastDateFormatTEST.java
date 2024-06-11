package org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", TimeZone.getTimeZone("Asia/Kolkata"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", TimeZone.getTimeZone("Asia/Kolkata"), Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("Asia/Kolkata"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("Asia/Kolkata"), Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("Asia/Kolkata"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("Asia/Kolkata"), Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("Asia/Kolkata"));
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("Asia/Kolkata"), Locale.ITALY);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormatLong() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Date date = new Date();
 String formattedDate = fastDateFormat.format(date.getTime());
 Date parsedDate = fastDateFormat.parse(formattedDate);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testFormatCalendar() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Calendar calendar = new GregorianCalendar(2018, 5, 1);
 String formattedDate = fastDateFormat.format(calendar.getTimeInMillis());
 Date parsedDate = fastDateFormat.parse(formattedDate);
 assertEquals(calendar.getTime(), parsedDate);
 }

 @Test
 public void testFormatDate() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Date date = new Date();
 String formattedDate = fastDateFormat.format(date);
 Date parsedDate = fastDateFormat.parse(formattedDate);
 assertEquals(date, parsedDate);
 }

 @Test
 public void testParseLong() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 long millis = fastDateFormat.parse(fastDateFormat.format(new Date().getTime())).getTime();
 assertEquals(new Date().getTime(), millis);
 }

 @Test
 public void testParseString() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Date date = fastDateFormat.parse("12-12-2012");
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidString() throws ParseException {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 fastDateFormat.parse("InvalidDateFormat");
 fail("Expected ParseException but got none");
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String pattern = fastDateFormat.getPattern();
 assertNotNull(pattern);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertTrue(maxLengthEstimate > 0);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 Object obj = new Object();
 assertFalse(fastDateFormat1.equals(obj));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
 FastDateFormat fastDateFormat3 = FastDateFormat.getInstance();
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 int hashCode3 = fastDateFormat3.hashCode();
 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode2, hashCode3);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 String toString = fastDateFormat.toString();
 assertNotNull(toString);
 }

 @Test
 public void testApplyRules() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm a");
 Calendar calendar = new GregorianCalendar(2018, 5, 1, 10, 30, 0, Calendar.MILLIS_IN_DAY);
 StringBuffer stringBuffer = new StringBuffer();
 fastDateFormat.applyRules(calendar, stringBuffer);
 String formattedDate = stringBuffer.toString();
 assertEquals("01-06-2018 10:30 AM", formattedDate);
 }
}