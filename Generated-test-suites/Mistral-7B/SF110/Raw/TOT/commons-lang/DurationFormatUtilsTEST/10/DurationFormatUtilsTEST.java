package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567; // 1 day, 2 hours, 7 minutes, 367 milliseconds
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("1d 2h 7m 367ms", result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567; // 1 day, 2 hours, 7 minutes, 367 milliseconds
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals("P1DT2H7M367S", result);
 }

 @Test
 public void testFormatDurationFormat() {
 long durationMillis = 1234567; // 1 day, 2 hours, 7 minutes, 367 milliseconds
 String format = "d' days 'H' hours 'm' minutes 's' seconds'";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals("1 days 2 hours 7 minutes 367 seconds", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567; // 1 day, 2 hours, 7 minutes, 367 milliseconds
 String format = "d' days 'H' hours 'm' minutes 's' seconds'a";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567; // 1 day, 2 hours, 7 minutes, 367 milliseconds
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals("1 day 2 hours 7 minutes 367 milliseconds", result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1494441357000L; // 2017-03-14 14:29:17 UTC
 long endMillis = 1494441387000L; // 2017-03-14 14:29:47 UTC
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("PT11M10S", result);
 }

 @Test
 public void testFormatPeriodFormat() {
 long startMillis = 1494441357000L; // 2017-03-14 14:29:17 UTC
 long endMillis = 1494441387000L; // 2017-03-14 14:29:47 UTC
 String format = "Pd DT H:mm:ss";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals("PT1 day 11 hours 10 minutes", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodInvalidFormat() {
 long startMillis = 1494441357000L; // 2017-03-14 14:29:17 UTC
 long endMillis = 1494441387000L; // 2017-03-14 14:29:47 UTC
 String format = "Pd DT H:mm:ss'a'";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("Expected IllegalArgumentException");
 }
}