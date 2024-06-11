package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 15600000; // 3 hours 40 minutes
 String expected = "03:40:00.000";
 assertEquals(expected, DurationFormatUtils.formatDurationHMS(durationMillis));
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 15600000; // 3 hours 40 minutes
 String expected = "PT03H40M00S";
 assertEquals(expected, DurationFormatUtils.formatDurationISO(durationMillis));
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 15600000; // 3 hours 40 minutes
 String expected = "03:40:00.000";
 assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS"));

 expected = "3 hours 40 mins";
 assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "h hours 'm' mins"));
 }

 @Test
 public void testFormatDurationWithZeroPadding() {
 long durationMillis = 15600000; // 3 hours 40 minutes
 String expected = "03:40:00.000";
 assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS", true));

 expected = "03:40:00";
 assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss"));
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 15600000; // 3 hours 40 minutes
 String expected = "3 hours 40 minutes";
 assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, false, false));

 expected = "3 hours";
 assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, true, false));

 expected = "3 hours, 40 minutes";
 assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, false, true));
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1462307200000L; // 2016-02-29 12:00:00
 long endMillis = 1462310800000L; // 2016-02-29 18:00:00
 String expected = "PT6H0M0S";
 assertEquals(expected, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1462307200000L; // 2016-02-29 12:00:00
 long endMillis = 1462310800000L; // 2016-02-29 18:00:00
 String expected = "6 hours";
 assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, "h hours"));
 }

 @Test
 public void testFormatPeriodWithTimeZone() {
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
 calendar.setTime(new Date(1462307200000L)); // 2016-02-29 12:00:00 UTC
 long startMillis = calendar.getTimeInMillis();

 calendar.add(Calendar.HOUR_OF_DAY, 6);
 long endMillis = calendar.getTimeInMillis();

 String expected = "6 hours";
 assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, "h hours", true, calendar.getTimeZone()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnmatchedQuoteInFormat() {
 String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S''"; // Unmatched quote
 DurationFormatUtils.formatDuration(15600000, format);
 }

}