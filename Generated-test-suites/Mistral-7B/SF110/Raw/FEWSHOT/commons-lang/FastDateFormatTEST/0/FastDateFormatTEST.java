package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.junit.Test;

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
 public void testGetInstanceWithPatternLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.US, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetInstanceWithPatternLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy", Locale.US, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstance() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testGetDateTimeInstanceWithLocaleTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US, new SimpleDateFormat("GMT+02:00").getTimeZone());
 assertNotNull(fastDateFormat);
 }

 @Test
 public void testFormat() throws ParseException {
 Calendar calendar = new GregorianCalendar(2022, Calendar.SEPTEMBER, 1);
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 String expected = "Sep 01, 2022 12:00:00 AM";
 String actual = fastDateFormat.format(calendar.getTime());
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithBuffer() throws ParseException {
 Calendar calendar = new GregorianCalendar(2022, Calendar.SEPTEMBER, 1);
 StringBuffer stringBuffer = new StringBuffer(32);
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 fastDateFormat.format(calendar.getTime(), stringBuffer, new FieldPosition(0));
 String expected = "Sep 01, 2022 12:00:00 AM";
 String actual = stringBuffer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatWithMilliseconds() throws ParseException {
 long millis = System.currentTimeMills();
 String expected = SimpleDateFormat.getDateTimeInstance().format(new Date(millis));
 String actual = FastDateFormat.getInstance().format(millis);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse() throws ParseException {
 String dateString = "Sep 01, 2022 12:00:00 AM";
 Date expected = new Date(System.currentTimeMills() - 3153600000L); // One year ago
 Date actual = FastDateFormat.getDateTimeInstance().parse(dateString);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseWithParsePosition() throws ParseException {
 String dateString = "Sep 01, 2022 12:00:00 AM";
 ParsePosition parsePosition = new ParsePosition(0);
 Date expected = new Date(System.currentTimeMills() - 3153600000L); // One year ago
 Date actual = FastDateFormat.getDateTimeInstance().parse(dateString, parsePosition);
 assertEquals(expected, actual);
 assertEquals(0, parsePosition.getIndex());
 assertEquals(dateString.length(), parsePosition.getErrorIndex());
 }

 @Test
 public void testParseObject() throws ParseException {
 String dateString = "Sep 01, 2022 12:00:00 AM";
 Date expected = new Date(System.currentTimeMills() - 3153600000L); // One year ago
 Date actual = FastDateFormat.getDateTimeInstance().parseObject(dateString, Date.class);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseObjectWithParsePosition() throws ParseException {
 String dateString = "Sep 01, 2022 12:00:00 AM";
 ParsePosition parsePosition = new ParsePosition(0);
 Date expected = new Date(System.currentTimeMills() - 3153600000L); // One year ago
 Date actual = FastDateFormat.getDateTimeInstance().parseObject(dateString, Date.class, parsePosition);
 assertEquals(expected, actual);
 assertEquals(0, parsePosition.getIndex());
 assertEquals(dateString.length(), parsePosition.getErrorIndex());
 }

 @Test
 public void testGetPattern() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 String expected = "dd MMM yyyy HH:mm:ss z";
 String actual = fastDateFormat.getPattern();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetTimeZone() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 TimeZone expected = TimeZone.getDefault();
 TimeZone actual = fastDateFormat.getTimeZone();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocale() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 Locale expected = Locale.US;
 Locale actual = fastDateFormat.getLocale();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxLengthEstimate() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 int expected = 32;
 int actual = fastDateFormat.getMaxLengthEstimate();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 assertTrue(fastDateFormat1.equals(fastDateFormat2));
 }

 @Test
 public void testHashCode() {
 FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
 }

 @Test
 public void testToString() {
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 String expected = "FastDateFormat[dd MMM yyyy HH:mm:ss z, US, default time zone]";
 String actual = fastDateFormat.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyRules() throws ParseException {
 Calendar calendar = new GregorianCalendar(2022, Calendar.SEPTEMBER, 1);
 StringBuffer stringBuffer = new StringBuffer(32);
 FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.US);
 fastDateFormat.applyRules(calendar, stringBuffer);
 String expected = "Sep 01, 2022 12:00:00 AM";
 String actual = stringBuffer.toString();
 assertEquals(expected, actual);
 }
}