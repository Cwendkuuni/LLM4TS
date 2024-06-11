package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expected = "1:46:17.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
 String expected = "P1D";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567;
 String expected = "-1:46:17.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 String format = "X:X:X.X";
 long durationMillis = 1234567;
 try {
 DurationFormatUtils.formatDuration(durationMillis, format);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expected = "1 day 1 hour 46 minutes 17 seconds 670 milliseconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1516035200000L; // 2018-01-01 00:00:00.000Z
 long endMillis = 1516038800000L; // 2018-01-01 04:00:00.000Z
 String expected = "P1DT3H";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 String format = "X:X:X:X:X:X:X:X";
 long startMillis = 1516035200000L; // 2018-01-01 00:00:00.000Z
 long endMillis = 1516038800000L; // 2018-01-01 04:00:00.000Z
 try {
 DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatPeriodWithDifferentTimeZones() {
 long startMillis = 1516035200000L; // 2018-01-01 00:00:00.000Z
 long endMillis = 1516038800000L; // 2018-01-01 04:00:00.000Z
 String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 TimeZone timezone1 = TimeZone.getTimeZone("UTC");
 TimeZone timezone2 = TimeZone.getTimeZone("America/Los_Angeles");
 String expected = "P1DT3H";
 String result1 = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone1);
 assertEquals(expected, result1);
 String result2 = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone2);
 assertEquals(expected, result2);
 }

 @Test
 public void testParseDurationStringWithHMS() throws ParseException {
 String durationString = "1:46:17.67";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, "H:mm:ss.SSS");
 long expected = 1234567;
 assertEquals(expected, durationMillis);
 }

 @Test
 public void testParseDurationStringWithISO() throws ParseException {
 String durationString = "P1DT3H";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'");
 long expected = 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000;
 assertEquals(expected, durationMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDurationStringWithInvalidFormat() throws ParseException {
 String durationString = "X:X:X.X";
 try {
 DurationFormatUtils.parseDuration(durationString, "H:mm:ss.SSS");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseDurationStringWithMissingComponents() throws ParseException {
 String durationString = "P1D";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, "yyyy-MM-dd'T'HH:mm:ss.SSS");
 long expected = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
 assertEquals(expected, durationMillis);
 }

 @Test
 public void testParseDurationStringWithLeadingZeros() throws ParseException {
 String durationString = "P1DT03H01M02S999MS";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, "yyyy-MM-dd'T'HH:mm:ss.SSS");
 long expected = 24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000 + 1 * 60 * 60 * 1000 + 2 * 60 * 1000 + 999 * 1000;
 assertEquals(expected, durationMillis);
 }

 @Test
 public void testParseDurationStringWithNegativeValues() throws ParseException {
 String durationString = "P-1DT-3H-1M-2S-999MS";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, "yyyy-MM-dd'T'HH:mm:ss.SSS");
 long expected = -(24 * 60 * 60 * 1000 + 3 * 60 * 60 * 1000 + 1 * 60 * 60 * 1000 + 2 * 60 * 1000 + 999 * 1000);
 assertEquals(expected, durationMillis);
 }
}