package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FastDateFormatTest {

 private FastDateFormat dateFormat;
 private FastDateFormat timeFormat;
 private FastDateFormat datetimeFormat;

 @Before
 public void setup() {
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.ENGLISH);
 datetimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.ENGLISH);
 }

 // Test getInstance methods
 @Test
 public void testGetInstance() {
 FastDateFormat instance = FastDateFormat.getInstance();
 assertNotNull(instance);
 }

 // Test getInstance methods with arguments
 @Test
 public void testGetInstanceWithArguments() {
 FastDateFormat instanceWithPattern = FastDateFormat.getInstance("dd-MM-yyyy");
 assertEquals("dd-MM-yyyy", instanceWithPattern.getPattern());
 FastDateFormat instanceWithTimeZone = FastDateFormat.getInstance("dd-MM-yyyy", TimeZone.getTimeZone("UTC"));
 assertEquals(TimeZone.getTimeZone("UTC"), instanceWithTimeZone.getTimeZone());
 FastDateFormat instanceWithLocale = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRENCH);
 assertEquals(Locale.FRENCH, instanceWithLocale.getLocale());
 }

 // Test getInstance methods with different styles
 @Test
 public void testGetInstanceWithDifferentStyles() {
 FastDateFormat dateInstanceShort = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
 FastDateFormat dateInstanceMedium = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 FastDateFormat dateInstanceLong = FastDateFormat.getDateInstance(FastDateFormat.LONG);
 FastDateFormat dateInstanceFull = FastDateFormat.getDateInstance(FastDateFormat.FULL);

 FastDateFormat timeInstanceShort = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 FastDateFormat timeInstanceMedium = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
 FastDateFormat timeInstanceLong = FastDateFormat.getTimeInstance(FastDateFormat.LONG);

 FastDateFormat datetimeInstanceMedium = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
 FastDateFormat datetimeInstanceLong = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
 FastDateFormat datetimeInstanceMediumDateTimeLong = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, Locale.ENGLISH);

 assertEquals(FastDateFormat.SHORT, dateInstanceShort.getStyle());
 assertEquals(FastDateFormat.MEDIUM, dateInstanceMedium.getStyle());
 assertEquals(FastDateFormat.LONG, dateInstanceLong.getStyle());
 assertEquals(FastDateFormat.FULL, dateInstanceFull.getStyle());

 assertEquals(FastDateFormat.SHORT, timeInstanceShort.getStyle());
 assertEquals(FastDateFormat.MEDIUM, timeInstanceMedium.getStyle());
 assertEquals(FastDateFormat.LONG, timeInstanceLong.getStyle());

 assertEquals(FastDateFormat.MEDIUM, datetimeInstanceMedium.getStyle());
 assertEquals(FastDateFormat.LONG, datetimeInstanceLong.getStyle());
 assertEquals(FastDateFormat.MEDIUM_DATE_TIME_LONG_DATE, datetimeInstanceMediumDateTimeLong.getStyle());
 }

 // Test format methods
 @Test
 public void testFormatDate() {
 Date date = new Date();
 String dateFormatted = dateFormat.format(date);
 assertNotNull(dateFormatted);
 }

 @Test
 public void testFormatCalendar() {
 Calendar calendar = new GregorianCalendar(2022, 1, 1);
 String calendarFormatted = dateFormat.format(calendar);
 assertNotNull(calendarFormatted);
 }

 @Test
 public void testFormatLongMilliseconds() {
 long millis = System.currentTimeMillis();
 String millisFormatted = dateFormat.format(millis);
 assertNotNull(millisFormatted);
 }

 @Test
 public void testFormatStringBuffer() {
 Date date = new Date();
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(date, buffer);
 assertNotNull(buffer.toString());
 }

 @Test
 public void testFormatDateBuffer() {
 Date date = new Date();
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(date, buffer, new FieldPosition(0));
 assertNotNull(buffer.toString());
 }

 @Test
 public void testFormatCalendarBuffer() {
 Calendar calendar = new GregorianCalendar(2022, 1, 1);
 StringBuffer buffer = new StringBuffer();
 dateFormat.format(calendar, buffer, new FieldPosition(0));
 assertNotNull(buffer.toString());
 }

 // Test format methods with incorrect arguments
 @Test(expected = IllegalArgumentException.class)
 public void testFormatDateNull() {
 dateFormat.format(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatLongMillisecondsNegative() {
 long millis = -1L;
 dateFormat.format(millis);
 }

 // Test parse methods
 @Test
 public void testParseDate() throws ParseException {
 Date date = dateFormat.parse("25-12-2022");
 assertNotNull(date);
 }

 @Test
 public void testParseCalendar() throws ParseException {
 Calendar calendar = GregorianCalendar.getInstance();
 dateFormat.parse("25-12-2022", calendar);
 assertEquals(2022, calendar.get(Calendar.YEAR));
 assertEquals(11, calendar.get(Calendar.MONTH));
 assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
 }

 @Test
 public void testParseLongMilliseconds() throws ParseException {
 long millis = System.currentTimeMillis();
 Date date = dateFormat.parse(dateFormat.format(millis));
 assertEquals(millis, date.getTime());
 }

 @Test
 public void testParseStringBuffer() throws ParseException {
 StringBuffer buffer = new StringBuffer("25-12-2022");
 Date date = dateFormat.parse(buffer);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseDateIncorrectFormat() throws ParseException {
 dateFormat.parse("25-12-2022-01");
 }

 // Test parseObject methods
 @Test
 public void testParseObjectDate() throws ParseException {
 Object obj = dateFormat.parseObject("25-12-2022");
 assertNotNull(obj);
 assertEquals(Date.class, obj.getClass());
 }

 @Test
 public void testParseObjectCalendar() throws ParseException {
 Object obj = datetimeFormat.parseObject("25-12-2022 12:34:56");
 assertNotNull(obj);
 assertEquals(Calendar.class, obj.getClass());
 }

 @Test(expected = ParseException.class)
 public void testParseObjectIncorrectFormat() throws ParseException {
 datetimeFormat.parseObject("25-12-2022 12:34:56-01");
 }

 // Test getPattern method
 @Test
 public void testGetPattern() {
 assertEquals("dd-MM-yyyy", dateFormat.getPattern());
 }

 // Test getTimeZone method
 @Test
 public void testGetTimeZone() {
 assertEquals("GMT", timeFormat.getTimeZone().getID());
 }

 // Test getLocale method
 @Test
 public void testGetLocale() {
 assertEquals("en_US", datetimeFormat.getLocale().getLanguage());
 }

 // Test getMaxLengthEstimate method
 @Test
 public void testGetMaxLengthEstimate() {
 assertEquals(19, dateFormat.getMaxLengthEstimate());
 }

 // Test equals method
 @Test
 public void testEquals() {
 FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 assertTrue(format1.equals(format2));
 }

 @Test
 public void testNotEquals() {
 FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.ENGLISH);
 assertFalse(format1.equals(format2));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ENGLISH);
 assertEquals(format1.hashCode(), format2.hashCode());
 }

 // Test toString method
 @Test
 public void testToString() {
 assertEquals("FastDateFormat[dd-MM-yyyy, en_US, GMT]", dateFormat.toString());
 }

 // Test applyRules method
 @Test
 public void testApplyRules() {
 Calendar calendar = new GregorianCalendar(2022, 1, 1);
 StringBuffer buffer = new StringBuffer();
 dateFormat.applyRules(calendar, buffer);
 assertEquals("01/01/2022", buffer.toString());
 }
}