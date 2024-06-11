package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expected = "1:46:11.67";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890L;
 String expected = "P3Y1M2D12H4M53S";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationCustomFormat() {
 long durationMillis = 1234567890L;
 String expected = "3 years, 1 month, 2 days, 12 hours, 4 minutes, 53 seconds";
 String actual = DurationFormatUtils.formatDuration(durationMillis, "y years, M months, d days, H hours, m minutes, s seconds", false);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567890L;
 String invalidFormat = "dHm";
 String actual = null;
 try {
 actual = DurationFormatUtils.formatDuration(durationMillis, invalidFormat, true);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(actual);
 }

 @Test
 public void testFormatDurationZeroDuration() {
 long durationMillis = 0L;
 String expected = "0:00:00.000";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationNegativeDuration() {
 long durationMillis = -1234567890L;
 String expected = "-P3Y1M2D12H4M53.67S";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodStartAfterEnd() {
 long startMillis = 1234567890L;
 long endMillis = 1234566890L;
 String expected = "-P1D1H1M1S";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodStartBeforeEnd() {
 long startMillis = 1234566890L;
 long endMillis = 1234567890L;
 String expected = "P1D1H1M1S";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodZeroDuration() {
 long startMillis = 0L;
 long endMillis = 0L;
 String expected = "PT0S";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodNegativeDuration() {
 long startMillis = 1234567890L;
 long endMillis = 1234566890L;
 String expected = "-P1D1H1M1S";
 String actual = DurationFormatUtils.formatPeriod(endMillis, startMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567890L;
 String expected = "3 years, 1 month, 2 days, 12 hours, 4 minutes, 53 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsSuppressLeadingZeroElements() {
 long durationMillis = 1234567890L;
 String expected = "3 years, 1 month, 2 days, 12 hours, 4 minutes, 53 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsSuppressTrailingZeroElements() {
 long durationMillis = 1234567890L;
 String expected = "3 years, 1 month, 2 days, 12 hours, 4 minutes, 53 second";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWordsNegativeDuration() {
 long durationMillis = -1234567890L;
 String expected = "-3 years, -1 month, -2 days, -12 hours, -4 minutes, -53 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

}