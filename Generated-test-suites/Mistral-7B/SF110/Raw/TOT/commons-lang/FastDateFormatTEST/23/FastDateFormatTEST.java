package org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FormatUtils;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

 private FastDateFormat dateFormat;
 private FastDateFormat timeFormat;
 private FastDateFormat datetimeFormat;
 private SimpleDateFormat referenceFormat;

 @Before
 public void setup() {
 dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 datetimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 referenceFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz", Locale.ENGLISH);
 }

 @Test
 public void testFormatDate() {
 Calendar calendar = new GregorianCalendar(2022, 10, 31);
 Date date = calendar.getTime();
 String expected = "31-11-2022";
 String actual = dateFormat.format(date);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatTime() {
 Calendar calendar = new GregorianCalendar(2022, 10, 31, 23, 59, 59);
 Date time = calendar.getTime();
 String expected = "23:59:59";
 String actual = timeFormat.format(time);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDateTime() {
 Calendar calendar = new GregorianCalendar(2022, 10, 31, 23, 59, 59);
 Date dateTime = calendar.getTime();
 String expected = "31-11-2022 23:59:59";
 String actual = datetimeFormat.format(dateTime);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatMilliseconds() {
 long millis = System.currentTimeMillis();
 String expected = FormatUtils.formatShortDate(new Date(millis));
 String actual = dateFormat.format(millis);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Calendar calendar = new GregorianCalendar(2022, 10, 31, 23, 59, 59, Calendar.ZONE_OFFSET, Calendar.UTC);
 Date date = calendar.getTime();
 String expected = "31-11-2022 22:59:59 GMT";
 String actual = datetimeFormat.format(date);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDateWithLocale() {
 Calendar calendar = new GregorianCalendar(2022, 10, 31);
 Date date = calendar.getTime();
 String expected = "31.11.2022.";
 String actual = dateFormat.format(date, Locale.GERMAN);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseDate() throws ParseException {
 String dateString = "31-11-2022";
 Date date = dateFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test
 public void testParseDateWithTimeZone() throws ParseException {
 String dateString = "31-11-2022 22:59:59 GMT";
 Date date = datetimeFormat.parse(dateString);
 assertNotNull(date);
 }

 @Test
 public void testParseDateWithLocale() throws ParseException {
 String dateString = "31.11.2022.";
 Date date = dateFormat.parse(dateString, Locale.GERMAN);
 assertNotNull(date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 String invalidDateString = "31-13-2022";
 dateFormat.parse(invalidDateString);
 }

 @Test
 public void testEquals() {
 FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 assertTrue(format1.equals(format2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat format1 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 FastDateFormat format2 = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.ENGLISH, TimeZone.getDefault());
 assertEquals(format1.hashCode(), format2.hashCode());
 }

 @Test
 public void testToString() {
 String expected = "FastDateFormat[dd-MM-yyyy, en_US, America/New_York]";
 String actual = dateFormat.toString();
 assertNotNull(actual);
 assertEquals(expected, actual);
 }
}