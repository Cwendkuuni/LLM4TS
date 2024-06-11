package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 123456;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "02:04:26.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 123456789;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "P3D1H2H26M46S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithCustomFormat() {
 long durationMillis = 123456;
 String format = "m:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "02:04:26.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithPadWithZeros() {
 long durationMillis = 123456;
 String format = "m:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 String expected = "02:04:26.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 123456;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days 2 hours 4 minutes 26 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
 long durationMillis = 123456;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = false;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days 2 hours 4 minutes 26 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
 long durationMillis = 123456;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = true;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days 2 hours 4 minutes 26 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithBothSuppressed() {
 long durationMillis = 123456;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = true;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 days 2 hours 4 minutes";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 String format = "m:ss'hello'S";
 long durationMillis = 123456;
 try {
 DurationFormatUtils.formatDuration(durationMillis, format);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567890123L + 3600000;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "PT3H";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithCustomFormat() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567890123L + 3600000;
 String format = "ss:mm:HH:dd:MM:yy";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "00:01:03:00:01:2012";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithPadWithZeros() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567890123L + 3600000;
 String format = "ss:mm:HH:dd:MM:yy";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
 String expected = "00:01:03:00:01:2012";
 assertEquals(expected, result);
 }
}