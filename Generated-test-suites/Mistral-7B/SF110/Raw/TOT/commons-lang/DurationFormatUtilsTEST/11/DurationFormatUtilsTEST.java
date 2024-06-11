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
 long durationMillis = 3600001;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals("P1DT1H", result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("-02:-07:-11.67", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 String format = "H:mm:ss.SSSXX";
 try {
 DurationFormatUtils.formatDuration(1234567, format);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format character 'X'", e.getMessage());
 }
 }

 @Test
 public void testFormatDurationWithPadWithZeros() {
 long durationMillis = 123456;
 String result = DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss", true);
 assertEquals("01:04:06", result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 assertEquals("2 hours 7 minutes 11.67 seconds", result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567901234L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("P1DT0H0M1.234S", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithNullStartOrEndMillis() {
 try {
 DurationFormatUtils.formatPeriod(0L, null, "");
 } catch (IllegalArgumentException e) {
 assertEquals("Start time must not be null", e.getMessage());
 }
 try {
 DurationFormatUtils.formatPeriod(null, 1234567, "");
 } catch (IllegalArgumentException e) {
 assertEquals("End time must not be null", e.getMessage());
 }
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567901234L;
 String format = "PdHm";
 try {
 DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid format character 'd'", e.getMessage());
 }
 }

 @Test
 public void testFormatPeriodWithPadWithZeros() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567901234L;
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "PddH'mm'ss.SSSSSS");
 assertEquals("P1D2H01M01S.001", result);
 }
}