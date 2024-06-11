package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.text.ParseException;
import java.util.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "02:07:17";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "PT02H07M17S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567L;
 String format = "d:HH:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "02:07:17";
 assertEquals(expected, result);

 format = "d:HH:mm:ss.SSS";
 result = DurationFormatUtils.formatDuration(durationMillis, format);
 expected = "02:07:17.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithLeadingAndTrailingZeros() {
 long durationMillis = 1234567L;
 String format = "d:HH:mm:ss' 'SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true, true);
 String expected = "02:07:17 000";
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 long durationMillis = 1234567L;
 String format = "x:HH:mm:ss";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1337133713371L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "P3Y1M1W2D3H4M5.566S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1234567890123L;
 long endMillis = 1337133713371L;
 String format = "y:MMM-d'-'HH:mm:ss.SSS z";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "3Y Jan-29 - 04:05:56.566 EDT";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithNegativeValues() {
 long startMillis = 1337133713371L;
 long endMillis = 1234567890123L;
 String format = "y:MMM-d'-'HH:mm:ss.SSS z";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "-3Y Jan-28 - 20:54:05.834 EDT";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "2 hours, 7 minutes, 17 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithZeroValues() {
 long durationMillis = 0L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "0 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithNegativeValues() {
 long durationMillis = -1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "-1 hour, -7 minutes, -17 seconds";
 assertEquals(expected, result);
 }
}