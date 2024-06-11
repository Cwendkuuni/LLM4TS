package org.apache.commons.lang3.test;

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
 long durationMillis = 567890123L;
 String expected = "P2Y1M1W3DT12H48M56.12S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithNegativeDurations() {
 long durationMillis = -1234567L;
 String expected = "-1:46:11.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 long durationMillis = 1234567L;
 String format = "x";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail();
 }

 @Test
 public void testFormatDurationWithLongFormat() {
 long durationMillis = 1234567890123L;
 String expected = "P2Y3M2W3DT12H48M56.123S";
 String result = DurationFormatUtils.formatDuration(durationMillis, "P%dY%dM%dW%dDT%dH%dM%dS.%dS", true);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String expected = "1 day 1 hour 46 minutes 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
 long durationMillis = 1234567L;
 String expected = "1 day 1 hour 46 minutes 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
 long durationMillis = 1234567L;
 String expected = "1 day 1 hour 46 minutes 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1337123456789L;
 String expected = "P2Y1M3W3DT12H48M56.12S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1234567890123L;
 long endMillis = 1337123456789L;
 String format = "x";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail();
 }

 @Test
 public void testFormatPeriodWithLongFormat() {
 long startMillis = 1234567890123L;
 long endMillis = 1337123456789L;
 String expected = "P2Y1M3W3DT12H48M56.123S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "P%dY%dM%dW%dDT%dH%dM%d.%dS%dS", true, TimeZone.getDefault());
 assertEquals(expected, result);
 }
}