package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FormatUtilsDateParser;
import org.apache.commons.lang3.time.ParsePosition;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expected = "1:46:17.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890L;
 String expected = "P2Y1M1W3D1H4M17S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithInvalidFormat() {
 long durationMillis = 1234567L;
 String format = "HM:ss";
 try {
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Format exception not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567L;
 String format = "H:mm:ss.SSS";
 String expected = "-0:56:17.67";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567890L;
 String expected = "2 years, 1 month, 3 weeks, 1 day, 4 hours, 17 minutes, 1 second";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1324567890000L;
 long endMillis = 1324567899000L;
 String expected = "P2D";
 String format = "'P'd";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1324567890000L;
 long endMillis = 1324567899000L;
 String format = "Hm";
 try {
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
 fail("Format exception not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatPeriodWithNegativeDuration() {
 long startMillis = 1324567899000L;
 long endMillis = 1324567890000L;
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
 String expected = "P-1D";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
 assertEquals(expected, result);
 }

 @Test
 public void testParseDurationWithValidFormat() {
 String input = "P2Y1M1W3DT4H17M1S";
 ParsePosition pos = new ParsePosition(0);
 long durationMillis = DurationFormatUtils.parseDuration(input, pos);
 assertEquals(1234574583000L, durationMillis);
 }

 @Test
 public void testParseDurationWithInvalidFormat() {
 String input = "P2Y1M1W3DT4H17M1S.";
 try {
 ParsePosition pos = new ParsePosition(0);
 long durationMillis = DurationFormatUtils.parseDuration(input, pos);
 fail("Format exception not thrown.");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseDurationWithNegativeDuration() {
 String input = "P-2D";
 ParsePosition pos = new ParsePosition(0);
 long durationMillis = DurationFormatUtils.parseDuration(input, pos);
 assertEquals(-3 * 24 * 60 * 60 * 1000L, durationMillis);
 }

 @Test
 public void testParseDurationWithEmptyInput() {
 String input = "";
 ParsePosition pos = new ParsePosition(0);
 try {
 long durationMillis = DurationFormatUtils.parseDuration(input, pos);
 fail("Format exception not thrown.");
 } catch (ParseException e) {
 // Expected
 }
 }
}