package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expectedResult = "20:47:36.700";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String expectedResult = "P2D20H47M36.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567L;
 String expectedResult = "-P2D20H47M36.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithZeroDuration() {
 long durationMillis = 0L;
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatPattern() {
 String format = "XXX";
 try {
 DurationFormatUtils.formatDuration(1234567L, format);
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatDurationWithCustomPadWithZeros() {
 long durationMillis = 1234567L;
 String expectedResult = "02:02:02:020.002";
 String format = "d:H:m:s:S";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithCustomPadWithoutZeros() {
 long durationMillis = 1234567L;
 String expectedResult = "2:2:2:2.2";
 String format = "d:H:m:s:S";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String expectedResult = "2 days 20 hours 47 minutes 36 seconds 700 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsWithLeadingZeros() {
 long durationMillis = 1234567L;
 String expectedResult = "2 days 02 hours 47 minutes 36 seconds 0700 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsWithTrailingZeros() {
 long durationMillis = 1234567L;
 String expectedResult = "2 days 02 hours 47 minutes 36 seconds 700 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsWithLeadingAndTrailingZeros() {
 long durationMillis = 1234567L;
 String expectedResult = "2 days 02 hours 047 minutes 036 seconds 0700 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatPatternWithWords() {
 String format = "XXX";
 try {
 DurationFormatUtils.formatDurationWords(1234567L, false, false);
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567L;
 long endMillis = 135794567L;
 String expectedResult = "P6D14H64M36.7S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISOWithNegativeDuration() {
 long startMillis = 135794567L;
 long endMillis = 1234567L;
 String expectedResult = "-P6D14H64M36.7S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISOWithZeroDuration() {
 long startMillis = 1234567L;
 long endMillis = 1234567L;
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithCustomPadWithZeros() {
 long startMillis = 1234567L;
 long endMillis = 135794567L;
 String expectedResult = "06 days 14 hours 64 minutes 36.7 seconds";
 String format = "d:H:m:s.S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithCustomPadWithoutZeros() {
 long startMillis = 1234567L;
 long endMillis = 135794567L;
 String expectedResult = "6 days 14 hours 64 minutes 36 seconds";
 String format = "d:H:m:s";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithTimeZone() {
 long startMillis = 1234567L;
 long endMillis = 135794567L;
 String expectedResult = "P6D14H64M36.7S";
 String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 String timezoneID = "America/Los_Angeles";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis, format, false, java.util.TimeZone.getTimeZone(timezoneID));
 assertEquals(expectedResult, result);
 }
}