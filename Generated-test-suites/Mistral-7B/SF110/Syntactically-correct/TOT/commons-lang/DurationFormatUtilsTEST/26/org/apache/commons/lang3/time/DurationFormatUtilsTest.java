package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expectedResult = "1:46:11.7";
 String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationHMSWithNegativeDuration() {
 long durationMillis = -1234567L;
 DurationFormatUtils.formatDurationHMS(durationMillis);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567890L;
 String expectedResult = "P1Y1M1DT1H46M11.7S";
 String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDurationWithCustomFormat() {
 long durationMillis = 1234567L;
 String format = "hh:mm:ss:SSS";
 String expectedResult = "01:46:11:700";
 String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDurationWithCustomFormatAndPadWithZeros() {
 long durationMillis = 1234567L;
 String format = "hh:mm:ss:SSS";
 String expectedResult = "01:46:11:070";
 String actualResult = DurationFormatUtils.formatDuration(durationMillis, format, true);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDurationWithCustomFormatAndInvalidFormat() {
 long durationMillis = 1234567L;
 String format = "hh:mm:ss:SSS:SSS";
 String expectedErrorMessage = "Error parsing format: 'hh:mm:ss:SSS:SSS'";
 try {
 DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException with the message: " + expectedErrorMessage);
 }
 catch (IllegalArgumentException e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 86400000L;
 String expectedResult = "1 day";
 String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
 long durationMillis = 25200000L;
 String expectedResult = "1 hour 1 hour 20 minutes";
 String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
 long durationMillis = 25201000L;
 String expectedResult = "1 hour 1 hour 20 minutes 10 seconds";
 String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567900000L;
 String expectedResult = "PT1H";
 String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatPeriodWithCustomFormat() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567900000L;
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 String expectedResult = "2015-01-01T01:46:11.700+00:00";
 String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatPeriodWithCustomFormatAndTimeZone() {
 long startMillis = 1234567890000L;
 long endMillis = 1234567900000L;
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 String expectedResult = "2015-01-01T01:46:11.700+00:00";
 String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseDurationWithCustomFormat() {
 String durationString = "P1Y1M1DT1H46M11.7S";
 long expectedResult = 1234567890000L;
 long actualResult = DurationFormatUtils.parseDuration(durationString, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'");
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithCustomFormatAndInvalidFormat() {
 String durationString = "P1Y1M1DT1H46M11.7SABC";
 String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 String expectedErrorMessage = "Error parsing 'ABC': Unparseable format: 'P1Y1M1DT1H46M11.S'";
 try {
 DurationFormatUtils.parseDuration(durationString, format);
 fail("Expected ParseException with the message: " + expectedErrorMessage);
 }
 catch (ParseException e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test
 public void testParseDurationWithCustomFormatAndTimeZone() {
 String durationString = "PT1H";
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 long expectedResult = 1234567890000L;
 long actualResult = DurationFormatUtils.parseDuration(durationString, format, TimeZone.getTimeZone("UTC"));
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithCustomFormatAndTimeZoneAndInvalidFormat() {
 String durationString = "PT1HABC";
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 String expectedErrorMessage = "Error parsing 'ABC': Unparseable format: 'PT1H'";
 try {
 DurationFormatUtils.parseDuration(durationString, format, TimeZone.getTimeZone("UTC"));
 fail("Expected ParseException with the message: " + expectedErrorMessage);
 }
 catch (ParseException e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test
 public void testFormatPeriodWithWrappedDates() {
 long startMillis = 1000L;
 long endMillis = 16000L;
 Calendar startCalendar = new GregorianCalendar(2001, 1, 1);
 startCalendar.setTimeInMillis(startMillis);
 Calendar endCalendar = new GregorianCalendar(2001, 1, 1);
 endCalendar.setTimeInMillis(endMillis);
 int milliseconds = endCalendar.get(Calendar.MILLISECOND) - startCalendar.get(Calendar.MILLISECOND);
 int seconds = endCalendar.get(Calendar.SECOND) - startCalendar.get(Calendar.SECOND);
 int minutes = endCalendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
 int hours = endCalendar.get(Calendar.HOUR) - startCalendar.get(Calendar.HOUR);
 int days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
 int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
 int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
 int daysInMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
 int daysInPreviousMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
 startCalendar.add(Calendar.DAY_OF_MONTH, daysInMonth + days);
 endCalendar.add(Calendar.DAY_OF_MONTH, daysInMonth + days);
 long wrappedStartMillis = startCalendar.getTimeInMillis();
 long wrappedEndMillis = endCalendar.getTimeInMillis();
 String expectedResult = "P" + (years + 1) + "Y" + (months + 1) + "M" + (days + 1) + "DT" + hours + "H" + minutes + "M" + seconds + "S";
 String actualResult = DurationFormatUtils.formatPeriod(wrappedStartMillis, wrappedEndMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, TimeZone.getDefault()).replaceAll("PT", "").trim();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatPeriodWithWrappedDatesAndTimeZone() {
 long startMillis = 1000L;
 long endMillis = 16000L;
 Calendar startCalendar = new GregorianCalendar(2001, 1, 1);
 startCalendar.setTimeInMillis(startMillis);
 Calendar endCalendar = new GregorianCalendar(2001, 1, 1);
 endCalendar.setTimeInMillis(endMillis);
 int milliseconds = endCalendar.get(Calendar.MILLISECOND) - startCalendar.get(Calendar.MILLISECOND);
 int seconds = endCalendar.get(Calendar.SECOND) - startCalendar.get(Calendar.SECOND);
 int minutes = endCalendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
 int hours = endCalendar.get(Calendar.HOUR) - startCalendar.get(Calendar.HOUR);
 int days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
 int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
 int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
 int daysInMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
 int daysInPreviousMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
 startCalendar.add(Calendar.DAY_OF_MONTH, daysInMonth + days);
 endCalendar.add(Calendar.DAY_OF_MONTH, daysInMonth + days);
 long wrappedStartMillis = startCalendar.getTimeInMillis();
 long wrappedEndMillis = endCalendar.getTimeInMillis();
 String expectedResult = "P" + (years + 1) + "Y" + (months + 1) + "M" + (days + 1) + "DT" + hours + "H" + minutes + "M" + seconds + "S";
 String actualResult = DurationFormatUtils.formatPeriod(wrappedStartMillis, wrappedEndMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", true, TimeZone.getTimeZone("UTC")).replaceAll("PT", "").trim();
 assertEquals(expectedResult, actualResult);
 }

}