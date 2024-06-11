package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "1:46:11.7";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expected = "P1Y2M3DT1H46M11.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithNegativeDurationMillis() {
 long durationMillis = -1234567;
 String expected = "-P1Y2M3DT1H46M11.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithZeroDurationMillis() {
 long durationMillis = 0;
 String expected = "P0D";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormatString() {
 String format = "H:mm:ss.SSSx"; // Invalid 'x' character
 String duration = DurationFormatUtils.formatDuration(1234567, format);
 fail("Expected IllegalArgumentException but got " + duration);
 }

 @Test
 public void testFormatDurationWithPadWithZerosTrue() {
 long durationMillis = 1234567;
 String expected = "P1Y2M3DT1H46M11.7S";
 String result = DurationFormatUtils.formatDuration(durationMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", true);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expected = "1 year, 2 months, 3 days, 1 hour, 46 minutes, 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithNegativeDurationMillis() {
 long durationMillis = -1234567;
 String expected = "-1 year, 2 months, 3 days, 1 hour, 46 minutes, 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithPadWithZerosTrueAndSuppressLeadingZeroElementsTrue() {
 long durationMillis = 1234567;
 String expected = "1 year, 2 months, 3 days, 1 hour, 46 minutes, 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithPadWithZerosTrueAndSuppressTrailingZeroElementsTrue() {
 long durationMillis = 1234567;
 String expected = "1 year, 2 months, 3 days, 1 hour, 46 minutes, 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1357946360123L;
 String expected = "P12Y1M1DT2H31M59.878S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithInvalidFormatString() {
 long startMillis = 1234567890123L;
 long endMillis = 1357946360123L;
 String format = "H:mm:ss.SSSx"; // Invalid 'x' character
 String duration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("Expected IllegalArgumentException but got " + duration);
 }
}