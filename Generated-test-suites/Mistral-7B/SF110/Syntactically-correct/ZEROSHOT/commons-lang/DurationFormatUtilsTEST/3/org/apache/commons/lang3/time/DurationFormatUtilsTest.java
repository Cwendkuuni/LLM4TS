package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "02:07:11.700";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "P2DT2H7M11.7S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567L;
 String format = "d:HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "2d:02:07:11.700";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithPadZeros() {
 long durationMillis = 1234567L;
 String format = "d:HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 String expected = "02:07:11.700";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 String expected = "2 days 2 hours 7 minutes 11 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 String expected = "2 days 2 hours 7 minutes 11 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 String expected = "2 days 2 hours 7 minutes 11 sec";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567891234L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "P1DT1H";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567891234L;
 String format = "d:HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "1d:01:01:00.101";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithPadZeros() {
 long startMillis = 1234567890123L;
 long endMillis = 1234567891234L;
 String format = "d:HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
 String expected = "1d:01:01:00.101";
 assertEquals(expected, result);
 }
}