package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "1:46:11.7";
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationHMS_Negative() {
 long durationMillis = -1234567;
 String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertNull(actual);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expected = "P1D1H46M11.7S";
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationISO_Negative() {
 long durationMillis = -1234567;
 String actual = DurationFormatUtils.formatDurationISO(durationMillis);
 assertNull(actual);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567;
 String expected = "1d 1h 46m 11.7s";
 String format = "d' days 'h' hours 'm' minutes 's' seconds'";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDuration_Negative() {
 long durationMillis = -1234567;
 String format = "d' days 'h' hours 'm' minutes 's' seconds'";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format);
 assertNull(actual);
 }

 @Test
 public void testFormatDuration_PadWithZeros() {
 long durationMillis = 1234567;
 String format = "d' days 0h 0m 0s.0s'";
 String expected = "1d 0h 0m 12.345.670.000s";
 String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expected = "1 day 1 hour 46 minutes 11.7 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWords_SuppressLeadingZeroElements() {
 long durationMillis = 1234567;
 String expected = "1 day 1 hour 46 minutes 11.7 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDurationWords_SuppressTrailingZeroElements() {
 long durationMillis = 1234567;
 String expected = "1 day 1 hour 46 minutes 11.7 seconds";
 String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1483228800000L;
 long endMillis = 1483229136000L;
 String expected = "P1DT3H9M36S";
 String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1483228800000L;
 long endMillis = 1483229136000L;
 String expected = "1d 3h 9m 36s";
 String format = "d' days 'h' hours 'm' minutes 's' seconds'";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatPeriod_PadWithZeros() {
 long startMillis = 1483228800000L;
 long endMillis = 1483229136000L;
 String format = "d' days 0h 0m 0s.0s'";
 String expected = "1d 0h 0m 0s.0s - 1d 0h 0m 3h 9m 36s.0s";
 String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
 assertEquals(expected, actual);
 }
}