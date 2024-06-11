package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "1:46:11.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890;
 String expected = "P2Y3M6H11M16.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567;
 String expected = "-1:46:11.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormatString() {
 String format = "X";
 long durationMillis = 1234567;
 String expectedExceptionMessage = "Invalid format character X.";
 try {
 DurationFormatUtils.formatDuration(durationMillis, format);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals(expectedExceptionMessage, e.getMessage());
 }
 }

 @Test
 public void testFormatDurationWithZeroDuration() {
 long durationMillis = 0;
 String expected = "0:00:00.000";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expected = "2 years, 3 months, 6 days, 11 hours, 16 minutes, 11 seconds, 670 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
 long durationMillis = 1234567;
 String expected = "2 years, 3 months, 6 days, 11 hours, 16 minutes, 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
 long durationMillis = 1234567;
 String expected = "2 years, 3 months, 6 days, 11 hours, 16 minutes";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWordsWithInvalidFormatString() {
 String format = "X";
 long durationMillis = 1234567;
 String expectedExceptionMessage = "Invalid format character X.";
 try {
 DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals(expectedExceptionMessage, e.getMessage());
 }
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1420070400000L;
 long endMillis = 1420074000000L;
 String expected = "P2H";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1420070400000L;
 long endMillis = 1420074000000L;
 String expected = "PT2H";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, null);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithNullStartMillis() {
 long endMillis = 1420074000000L;
 String expected = null;
 String result = DurationFormatUtils.formatPeriod(null, endMillis, null);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithNullEndMillis() {
 long startMillis = 1420070400000L;
 String expected = null;
 String result = DurationFormatUtils.formatPeriod(startMillis, null, null);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithInvalidFormatString() {
 String format = "X";
 long startMillis = 1420070400000L;
 long endMillis = 1420074000000L;
 String expectedExceptionMessage = "Invalid format character X.";
 try {
 DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals(expectedExceptionMessage, e.getMessage());
 }
 }
}