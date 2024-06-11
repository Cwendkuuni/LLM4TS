package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expected = "1:46:11.7";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890L;
 String expected = "P3Y4M6DT14H26M11.7S";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567L;
 String expected = "-1:46:11.7";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 String format = "InvalidFormat";
 long durationMillis = 1234567L;
 DurationFormatUtils.formatDuration(durationMillis, format);
 }

 @Test
 public void testFormatDurationWithZeroPadding() {
 long durationMillis = 1234567L;
 String expected = "1:46:11.7";
 String actual = DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS", true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String expected = "1 day 4 hours 1 minute 11 seconds 0.7 second";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsWithNegativeDuration() {
 long durationMillis = -1234567L;
 String expected = "-1 day -4 hours -1 minute -11 seconds -0.7 second";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890000L;
 long endMillis = 1337890754678L;
 String expected = "P2Y3M8DT21H58M37.146S";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithNegativeDuration() {
 long startMillis = 1337890754678L;
 long endMillis = 1234567890000L;
 String expected = "P-2Y3M8DT-21H58M37.146S";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithEmptyFormat() {
 long startMillis = 1234567890000L;
 long endMillis = 1337890754678L;
 String expected = "2 years, 3 months, 8 days, 21 hours, 58 minutes, 37 seconds, 146 milliseconds";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "");
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1234567890000L;
 long endMillis = 1337890754678L;
 String format = "InvalidFormat";
 String expected = "Format error";
 String actual = "";
 try {
 actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 } catch (IllegalArgumentException e) {
 actual = e.getMessage();
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodWithSpecifiedTimeZone() {
 long startMillis = 1234567890000L;
 long endMillis = 1337890754678L;
 TimeZone timeZone = Calendar.getInstance().getTimeZone();
 String expected = "P2Y3M8DT21H58M37.146S";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", true, timeZone);
 assertEquals(expected, actual);
 }
}