package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("02:07:11.67", result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals("P2DT2H7M11S.67S", result);
 }

 @Test
 public void testFormatDurationCustomFormat() {
 long durationMillis = 1234567;
 String format = "d-HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals("2-02:07:11.67", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567;
 String format = "x";
 try {
 DurationFormatUtils.formatDuration(durationMillis, format);
 } catch (IllegalArgumentException e) {
 assertEquals("Unmatched quote in format: x", e.getMessage());
 }
 }

 @Test
 public void testFormatDurationZeroDuration() {
 long durationMillis = 0;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("00:00:00.000", result);
 }

 @Test
 public void testFormatDurationNegativeDuration() {
 long durationMillis = -1234567;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("-02:07:11.67", result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals("2 days 2 hours 7 minutes 11 seconds", result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1451920000000L;
 long endMillis = 1451920360000L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("P2DT1H11M", result);
 }

 @Test
 public void testFormatPeriodCustomFormat() {
 long startMillis = 1451920000000L;
 long endMillis = 1451920360000L;
 String format = "d-MMM-y HH:mm";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals("2-Jan-15 01:11", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodInvalidFormat() {
 long startMillis = 1451920000000L;
 long endMillis = 1451920360000L;
 String format = "x";
 try {
 DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 } catch (IllegalArgumentException e) {
 assertEquals("Unmatched quote in format: x", e.getMessage());
 }
 }

 @Test
 public void testFormatPeriodZeroDuration() {
 long startMillis = 0;
 long endMillis = 0;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("", result);
 }

 @Test
 public void testFormatPeriodNegativeDuration() {
 long startMillis = 1451920360000L;
 long endMillis = 1451920000000L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("", result);
 }
}