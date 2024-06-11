package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expectedResult = "11:04:37.607";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expectedResult = "P1d PT11H PT4M PT37S.607S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithNegativeDurations() {
 long durationMillis = -1234567;
 String expectedResult = "-P1d PT11H PT4M PT37S.607S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithZeroDuration() {
 long durationMillis = 0;
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 String format = "InvalidFormat";
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expectedResult = "1 day 11 hours 4 minutes 37 seconds point six zero seven";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1516912000000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 long endMillis = 1516912300000L; // Fri Jan 01 2018 00:10:00 GMT-05:00
 String expectedResult = "P1DT0H5M10S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithNegativeDurations() {
 long startMillis = 1516912000000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 long endMillis = 1516911660000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 String expectedResult = "-P1DT0H4M";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithInvalidFormat() {
 String startFormat = "yyyy-MM-dd HH:mm:ss.SSS";
 String endFormat = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 long startMillis = 1516912000000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 long endMillis = 1516912300000L; // Fri Jan 01 2018 00:10:00 GMT-05:00
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "InvalidFormat");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatPeriodWithZeroDuration() {
 long startMillis = 1516912000000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 long endMillis = 1516912000000L; // Fri Jan 01 2018 00:05:00 GMT-05:00
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "PT0S");
 assertEquals(expectedResult, result);
 }
}