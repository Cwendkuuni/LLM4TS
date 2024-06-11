package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expected = "1:46:11.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890L;
 String expected = "P1Y2M3DT5H11M16.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567890L;
 String format = "dd:HH:mm:ss.SSS";
 String expected = "1:23:05:16.673";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567890L;
 String format = "dd:HH:mm:ss.SSSx"; // invalid char 'x'
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567890L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = true;
 String expected = "1 day 2 hours 3 minutes 5 seconds 167 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567890123L;
 String expected = "P1DT1H12M11.671S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567890123L;
 String format = "d' days 'H' hours 'm' minutes 's' seconds'";
 String expected = "1 day 1 hour 12 minutes 11 seconds";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodInvalidFormat() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567890123L;
 String format = "d' days 'H' hours 'm' minutes 's' seconds'x"; // invalid char 'x'
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("IllegalArgumentException expected but not thrown");
 }

}