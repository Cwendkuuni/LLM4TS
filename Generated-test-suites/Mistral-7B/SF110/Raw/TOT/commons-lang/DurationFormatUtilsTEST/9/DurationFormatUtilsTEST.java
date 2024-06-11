package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expectedResult = "1:46:11.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expectedResult = "P1Y1M1W1D2H4M11S.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567;
 String format = "d:HH:mm:ss";
 String expectedResult = "1:1:04:11";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);

 long durationMillis2 = 1234567890123L;
 String format2 = "H:mm:ss.SSS";
 String expectedResult2 = "356d:01:05:33.123";
 String result2 = DurationFormatUtils.formatDuration(durationMillis2, format2, false);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testFormatDurationPadWithZeros() {
 long durationMillis = 1234567;
 String format = "d:HH:mm:ss";
 String expectedResult = "01:01:04:11";
 String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567;
 String format = "x:HH:mm:ss";
 DurationFormatUtils.formatDuration(durationMillis, format, true);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 String expectedResult = "1 day 1 hour 4 minutes 11 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, result);

 long durationMillis2 = 1234567890123L;
 String expectedResult2 = "356 days 1 hour 5 minutes 33 seconds";
 String result2 = DurationFormatUtils.formatDurationWords(durationMillis2, false, false);
 assertEquals(expectedResult2, result2);

 long durationMillis3 = 1234567;
 String expectedResult3 = "1 day 1 hour 4 minutes 11 seconds";
 String result3 = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expectedResult3, result3);

 long durationMillis4 = 1234567;
 String expectedResult4 = "1 day 1 hour 4 minutes 11 seconds";
 String result4 = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expectedResult4, result4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWordsInvalidFormat() {
 long durationMillis = 1234567;
 String format = "x:d";
 DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 }

 @Test
 public void testFormatPeriodHMS() {
 long startMillis = 1234567000000L;
 long endMillis = 1234567123456L;
 String expectedResult = "1:23:22.856";
 String result = DurationFormatUtils.formatPeriodHMS(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567000000L;
 long endMillis = 1234567123456L;
 String expectedResult = "P1Y1M1W1DT1H23M22.856S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1234567000000L;
 long endMillis = 1234567123456L;
 String format = "d:HH:mm:ss";
 String expectedResult = "1:1:23:22";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);

 long startMillis2 = 1234567000000L;
 long endMillis2 = 1234567123456L;
 String format2 = "H:mm:ss.SSS";
 String expectedResult2 = "1:23:22.856";
 String result2 = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format2, false);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testFormatPeriodPadWithZeros() {
 long startMillis = 1234567000000L;
 long endMillis = 1234567123456L;
 String format = "d:HH:mm:ss";
 String expectedResult = "01:01:23:22";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodInvalidFormat() {
 long startMillis = 1234567000000L;
 long endMillis = 1234567123456L;
 String format = "x:HH:mm:ss";
 DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
 }

 @Test
 public void testFormatPeriodWithTimeZone() {
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
 calendar.setTime(new Date(1234567000000L));
 Calendar calendar2 = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
 calendar2.setTime(new Date(1234567123456L));
 long startMillis = calendar.getTimeInMillis();
 long endMillis = calendar2.getTimeInMillis();
 String format = "d:HH:mm:ss z";
 String expectedResult = "1:1:23:22 UTC";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getTimeZone("UTC"));
 assertEquals(expectedResult, result);
 }

 @Test(expected = ParseException.class)
 public void testParseDurationInvalidFormat() {
 String durationString = "1d2h3m4s";
 DurationFormatUtils.parseDuration(durationString);
 }

 @Test
 public void testParseDurationHMS() {
 String durationString = "1:23:22.856";
 long expectedResult = 1234567890123L;
 long result = DurationFormatUtils.parseDuration(durationString, null).getTime();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseDurationISO() {
 String durationString = "P1Y1M1W1DT1H23M22.856S";
 long expectedResult = 1234567890123L;
 long result = DurationFormatUtils.parseDuration(durationString, null).getTime();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseDurationFormat() {
 String durationString = "1:1:23:22";
 long expectedResult = 1234567123456L;
 long result = DurationFormatUtils.parseDuration(durationString, null, null, null).getTime();
 assertEquals(expectedResult, result);

 String durationString2 = "1:23:22.856";
 long expectedResult2 = 1234567890123L;
 long result2 = DurationFormatUtils.parseDuration(durationString2, null, null, null).getTime();
 assertEquals(expectedResult2, result2);
 }
}