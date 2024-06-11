package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.junit.Test;

public class FastDateFormatTest {

 private static final String PATTERN_DEFAULT = "dd/MM/yyyy HH:mm:ss z";
 private static final String PATTERN_SHORT = "d/M/yy h:mm a z";
 private static final String PATTERN_LONG = "EEEE, dd-MM-yyyy 'at' h:mm a z";
 private static final String PATTERN_MEDIUM = "dd-MM-yy h:mm a";
 private static final String PATTERN_FULL = "EEEE, dd-MM-yyyy 'at' hh:mm a zzzz";

 @Test
 public void testGetInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance();
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceNullPattern() {
 FastDateFormat.getInstance((String)null);
 }

 @Test
 public void testGetInstanceWithPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullTimeZone() {
 FastDateFormat.getInstance((TimeZone)null, PATTERN_DEFAULT);
 }

 @Test
 public void testGetInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT, timeZone);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithNullLocale() {
 FastDateFormat.getInstance((Locale)null, PATTERN_DEFAULT, timeZone);
 }

 @Test
 public void testGetInstanceWithLocale() {
 Locale locale = Locale.US;
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT, null, locale);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInstanceWithAllNull() {
 FastDateFormat.getInstance((String)null, (TimeZone)null, (Locale)null);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateInstanceInvalidStyle() {
 FastDateFormat.getDateInstance(-1);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 Locale locale = Locale.US;
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDateInstanceWithNullLocale() {
 FastDateFormat.getDateInstance(FastDateFormat.FULL, (Locale)null);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDateInstanceWithAllNull() {
 FastDateFormat.getDateInstance(FastDateFormat.FULL, (TimeZone)null, (Locale)null);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetTimeInstanceInvalidStyle() {
 FastDateFormat.getTimeInstance(-1);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 Locale locale = Locale.US;
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTimeInstanceWithNullLocale() {
 FastDateFormat.getTimeInstance(FastDateFormat.SHORT, (Locale)null);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTimeInstanceWithAllNull() {
 FastDateFormat.getTimeInstance(FastDateFormat.SHORT, (TimeZone)null, (Locale)null);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceInvalidDateStyle() {
 FastDateFormat.getDateTimeInstance(-1, FastDateFormat.SHORT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateTimeInstanceInvalidTimeStyle() {
 FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, -1);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 Locale locale = Locale.US;
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDateTimeInstanceWithNullLocale() {
 FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, (Locale)null);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
 assertNotNull(fastDateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDateTimeInstanceWithAllNull() {
 FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, (TimeZone)null, (Locale)null);
 }

 @Test
 public void testFormatObjectWithDate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 Date date = new Date();
 String formattedDate = fastDateFormat.format(date);
 assertNotNull(formattedDate);
 assertEquals(formattedDate, fastDateFormat.format(new Date()));
 }

 @Test
 public void testFormatObjectWithCalendar() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 Calendar calendar = Calendar.getInstance();
 String formattedCalendar = fastDateFormat.format(calendar);
 assertNotNull(formattedCalendar);
 }

 @Test
 public void testFormatObjectWithLongMilliseconds() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_LONG);
 long milliseconds = System.currentTimeMillis();
 String formattedLong = fastDateFormat.format(milliseconds);
 assertNotNull(formattedLong);
 }

 @Test
 public void testFormatObjectWithDateAndBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Date date = new Date();
 StringBuffer buffer = new StringBuffer(10);
 String formattedDate = fastDateFormat.format(date, buffer);
 assertNotNull(formattedDate);
 assertEquals(formattedDate, fastDateFormat.format(date));
 }

 @Test
 public void testFormatObjectWithLongMillisecondsAndBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 long milliseconds = System.currentTimeMillis();
 StringBuffer buffer = new StringBuffer(50);
 String formattedLong = fastDateFormat.format(milliseconds, buffer);
 assertNotNull(formattedLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithNullObject() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_LONG);
 fastDateFormat.format(null, new StringBuffer(10));
 }

 @Test
 public void testParseObjectWithString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 String dateString = fastDateFormat.format(new Date());
 Date parsedDate = fastDateFormat.parse(dateString);
 assertNotNull(parsedDate);
 assertEquals(parsedDate.getTime(), System.currentTimeMillis());
 }

 @Test
 public void testParseObjectWithParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 String dateString = fastDateFormat.format(new Date());
 ParsePosition parsePosition = new ParsePosition(0);
 Date parsedDate = fastDateFormat.parseObject(dateString, parsePosition);
 assertNotNull(parsedDate);
 assertEquals(parsedDate.getTime(), System.currentTimeMillis());
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 String invalidString = "abc";
 fastDateFormat.parseObject(invalidString, new ParsePosition(0));
 }

 @Test(expected = ParsePosition.IndexOutOfBoundsException.class)
 public void testParseObjectWithInvalidStringWithParsePosition() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 String invalidString = "abc";
 ParsePosition parsePosition = new ParsePosition(1);
 fastDateFormat.parseObject(invalidString, parsePosition);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseObjectWithNullString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 fastDateFormat.parseObject((String)null, new ParsePosition(0));
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 String pattern = fastDateFormat.getPattern();
 assertNotNull(pattern);
 assertEquals(pattern, PATTERN_DEFAULT);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 TimeZone timeZone = fastDateFormat.getTimeZone();
 assertNotNull(timeZone);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 Locale locale = fastDateFormat.getLocale();
 assertNotNull(locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMaxLengthEstimateNullFormat() {
 FastDateFormat.getMaxLengthEstimate(null);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN_DEFAULT);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN_DEFAULT);
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN_DEFAULT);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN_SHORT);
 assertFalse(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getInstance(PATTERN_DEFAULT);
 FastDateFormat fastDateFormat2 = FastDateFormat.getInstance(PATTERN_DEFAULT);
 int hashCode1 = fastDateFormat1.hashCode();
 int hashCode2 = fastDateFormat2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_DEFAULT);
 String toString = fastDateFormat.toString();
 assertNotNull(toString);
 }

 @Test
 public void testApplyRulesWithCalendarAndBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Calendar calendar = Calendar.getInstance();
 StringBuffer buffer = new StringBuffer(10);
 fastDateFormat.applyRules(calendar, buffer);
 String formattedDate = buffer.toString();
 assertNotNull(formattedDate);
 }

 @Test
 public void testFormatObjectWithDateAndNullBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Date date = new Date();
 fastDateFormat.format(date, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithLongAndNullBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 long milliseconds = System.currentTimeMillis();
 fastDateFormat.format(milliseconds, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithDateAndEmptyBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Date date = new Date();
 StringBuffer buffer = new StringBuffer(0);
 fastDateFormat.format(date, buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithLongAndEmptyBuffer() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 long milliseconds = System.currentTimeMillis();
 StringBuffer buffer = new StringBuffer(0);
 fastDateFormat.format(milliseconds, buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithDateAndNegativeBufferSize() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_MEDIUM);
 Date date = new Date();
 StringBuffer buffer = new StringBuffer(-1);
 fastDateFormat.format(date, buffer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithLongAndNegativeBufferSize() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance(PATTERN_FULL);
 long milliseconds = System.currentTimeMillis();
 StringBuffer buffer = new StringBuffer(-1);
 fastDateFormat.format(milliseconds, buffer);
 }

}