package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "10:04:37";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationHMSNegative() {
 long durationMillis = -1234567;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "-10:04:37";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "P10D10H4M37S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISOWithPadZero() {
 long durationMillis = 123456;
 String result = DurationFormatUtils.formatDurationISO(durationMillis, true);
 String expected = "P10D01H04M06S";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 123456;
 String format = "HM:ms";
 try {
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatDurationISOWithNegativeDuration() {
 long startMillis = 1592466281000L;
 long endMillis = 1592466181000L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "P-1M-1D-1H";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISOWithZeroDuration() {
 long startMillis = 1592466281000L;
 long endMillis = 1592466281000L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "P0D0H0M0S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "1 day 10 hours 4 minutes and 37 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithNegativeDuration() {
 long durationMillis = -1234567;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "1 day 10 hours 4 minutes and 37 seconds ago";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithZeroDuration() {
 long durationMillis = 0;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "";
 assertEquals(expected, result);
 }
}