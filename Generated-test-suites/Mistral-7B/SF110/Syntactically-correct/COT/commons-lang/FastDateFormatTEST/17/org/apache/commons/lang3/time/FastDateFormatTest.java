package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatCache;
import org.junit.Test;

public class FastDateFormatTest {

 private static final FastDateFormat FULL_DATE_FORMAT = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss zzz");
 private static final FastDateFormat LONG_DATE_FORMAT = FastDateFormat.getInstance("MMMM dd, yyyy h:mm a z");
 private static final FastDateFormat MEDIUM_DATE_FORMAT = FastDateFormat.getInstance("dd-MM-yy h:mm a");
 private static final FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("dd-MM-yy h:mm a");

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
 public void testGetDateInstance() {
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
 assertNotNull(dateFormat);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDateInstanceWithNegativeStyle() {
 FastDateFormat.getDateInstance(-1);
 }

 @Test
 public void testFormatObject() {
 Date date = new Date();
 String formattedDate = FULL_DATE_FORMAT.format(date);
 assertNotNull(formattedDate);
 assertEquals("expected formatted date to match", "01-01-2022 12:00:00 +0000", formattedDate);
 }

 @Test(expected = NullPointerException.class)
 public void testFormatObjectWithNullDate() {
 FULL_DATE_FORMAT.format((Date)null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatObjectWithInvalidStyle() {
 FastDateFormat invalidStyleDateFormat = FastDateFormat.getDateInstance(-1);
 invalidStyleDateFormat.format((Date)null);
 }

 @Test
 public void testFormat() {
 Long currentTime = System.currentTimeMillis();
 String formattedTime = FULL_DATE_FORMAT.format(currentTime);
 assertNotNull(formattedTime);
 assertEquals("expected formatted time to match", "01-01-2022 12:30:45 +0000", formattedTime);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNullMilliseconds() {
 FULL_DATE_FORMAT.format((Long)null);
 }

 @Test
 public void testFormatWithStringBuffer() {
 Long currentTime = System.currentTimeMillis();
 StringBuffer stringBuffer = new StringBuffer();
 FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 dateFormat.format(currentTime, stringBuffer);
 assertNotNull(stringBuffer.toString());
 assertEquals("expected formatted date to match", "01-01-2022 12:30:45", stringBuffer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithNullStringBuffer() {
 FastDateFormat.getDateInstance(FastDateFormat.MEDIUM).format((StringBuffer)null, null);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "01-01-2022 12:30:45";
 Date parsedDate = FULL_DATE_FORMAT.parse(dateString);
 assertNotNull(parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidFormat() throws ParseException {
 String invalidDateString = "ab-cd-ef";
 FULL_DATE_FORMAT.parse(invalidDateString);
 }

 @Test(expected = ParsePosition.IndexOutOfBoundsException.class)
 public void testParseWithInvalidPosition() throws ParseException {
 String dateString = "01-01-2022 12:30:45";
 ParsePosition parsePosition = new ParsePosition(5);
 FULL_DATE_FORMAT.parse(dateString, parsePosition);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullString() throws ParseException {
 FULL_DATE_FORMAT.parse((String)null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullParsePosition() throws ParseException {
 FULL_DATE_FORMAT.parse((ParsePosition)null, null);
 }

 @Test
 public void testParseObject() throws ParseException {
 String dateString = "01-01-2022 12:30:45";
 Date parsedDate = FULL_DATE_FORMAT.parseObject(dateString);
 assertNotNull(parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseObjectWithInvalidFormat() throws ParseException {
 String invalidDateString = "ab-cd-ef";
 FULL_DATE_format.parseObject(invalidDateString);
 }

 @Test(expected = ParsePosition.IndexOutOfBoundsException.class)
 public void testParseObjectWithInvalidPosition() throws ParseException {
 String dateString = "01-01-2022 12:30:45";
 ParsePosition parsePosition = new ParsePosition(5);
 FULL_DATE_FORMAT.parseObject(dateString, parsePosition);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseObjectWithNullString() throws ParseException {
 FULL_DATE_FORMAT.parseObject((String)null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseObjectWithNullParsePosition() throws ParseException {
 FULL_DATE_FORMAT.parseObject((ParsePosition)null, null);
 }

 @Test
 public void testGetPattern() {
 String pattern = FULL_DATE_FORMAT.getPattern();
 assertNotNull(pattern);
 assertEquals("expected pattern to match", "dd-MM-yyyy HH:mm:ss zzz", pattern);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone timeZone = FULL_DATE_FORMAT.getTimeZone();
 assertNotNull(timeZone);
 }

 @Test
 public void testGetLocale() {
 Locale locale = FULL_DATE_FORMAT.getLocale();
 assertNotNull(locale);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 int maxLengthEstimate = FULL_DATE_FORMAT.getMaxLengthEstimate();
 assertNotNull(maxLengthEstimate);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = new FastDateFormat("dd-MM-yyyy HH:mm:ss zzz", null, null);
 FastDateFormat fastDateFormat2 = new FastDateFormat("dd-MM-yyyy HH:mm:ss zzz", null, null);
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = new FastDateFormat("dd-MM-yyyy HH:mm:ss zzz", null, null);
 FastDateFormat fastDateFormat2 = new FastDateFormat("dd-MM-yyyy HH:mm:ss zzz", null, null);
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 String toString = FULL_DATE_FORMAT.toString();
 assertNotNull(toString);
 }

 @Test
 public void testApplyRules() {
 Calendar calendar = Calendar.getInstance();
 String formattedDate = FULL_DATE_FORMAT.applyRules(calendar, new StringBuffer()).toString();
 assertNotNull(formattedDate);
 }
}