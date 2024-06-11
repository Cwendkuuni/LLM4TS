package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expectedResult = "1:46:17.67";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567;
 String expectedResult = "P1Y1M1W1D1H1M46S.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567;
 String expectedResult = "-P1Y1M1W1D1H1M46S.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationWithInvalidFormat() {
 String format = "InvalidFormat";
 long durationMillis = 1234567;
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testFormatDurationWithNonExistentToken() {
 String format = "Px";
 long durationMillis = 1234567;
 String expectedResult = "P1Y1M1W1D1H1M46S.67S";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithZeroDuration() {
 long durationMillis = 0;
 String expectedResult = "0s";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithZeroDurationISO() {
 long durationMillis = 0;
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithLongDuration() {
 long durationMillis = 31536000000L; // 10 years
 String expectedResult = "P10Y";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithLongDurationHMS() {
 long durationMillis = 31536000000L; // 10 years
 String expectedResult = "31536000s";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWithLongDurationWords() {
 long durationMillis = 31536000000L; // 10 years
 String expectedResult = "10 years";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithStartDateBeforeEndDate() {
 long startMillis = 1363353200000L; // 2013-02-15 00:00:00.0Z
 long endMillis = 1363357200000L; // 2013-02-15 02:00:00.0Z
 String expectedResult = "PT2H";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithStartDateAfterEndDate() {
 long startMillis = 1363357200000L; // 2013-02-15 02:00:00.0Z
 long endMillis = 1363353200000L; // 2013-02-15 00:00:00.0Z
 String expectedResult = "PT-2H";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithNullStartDate() {
 long startMillis = 0;
 long endMillis = 1363353200000L; // 2013-02-15 00:00:00.0Z
 String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodWithNullEndDate() {
 long startMillis = 1363353200000L; // 2013-02-15 00:00:00.0Z
 long endMillis = 0;
 String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, new GregorianCalendar());
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1363353200000L; // 2013-02-15 00:00:00.0Z
 long endMillis = 1363357200000L; // 2013-02-15 02:00:00.0Z
 String format = "InvalidFormat";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, new GregorianCalendar());
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testParseDurationWithValidDurationString() {
 String durationString = "P1D";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true);
 assertEquals(86400000, durationMillis);
 }

 @Test
 public void testParseDurationWithValidDurationStringWithTimeZone() {
 String durationString = "PT1H";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC));
 assertEquals(3600000, durationMillis);
 }

 @Test
 public void testParseDurationWithValidDurationStringWithTimeZoneAndFormat() {
 String durationString = "PT1H";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC), "PTxH");
 assertEquals(3600000, durationMillis);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDurationWithInvalidDurationString() {
 String durationString = "InvalidDuration";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true, new GregorianCalendar());
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithInvalidDurationStringWithTimeZone() {
 String durationString = "InvalidDuration";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC));
 fail("Should have thrown a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithInvalidDurationStringWithTimeZoneAndFormat() {
 String durationString = "InvalidDuration";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, true, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC), "PTxH");
 fail("Should have thrown a ParseException");
 }

 @Test
 public void testParseDurationWithValidDurationStringISO() {
 String durationString = "P1Y1M1W1D1H1M46S.67S";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false);
 assertEquals(31536000001L, durationMillis);
 }

 @Test
 public void testParseDurationWithValidDurationStringISOWithTimeZone() {
 String durationString = "P1Y1M1W1D1H1M46S.67S";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC));
 assertEquals(31536000001L, durationMillis);
 }

 @Test
 public void testParseDurationWithValidDurationStringISOWithTimeZoneAndFormat() {
 String durationString = "P1Y1M1W1D1H1M46S.67S";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC), DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN);
 assertEquals(31536000001L, durationMillis);
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithInvalidDurationStringISO() {
 String durationString = "InvalidFormat";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false);
 fail("Should have thrown a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithInvalidDurationStringISOWithTimeZone() {
 String durationString = "InvalidFormat";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC));
 fail("Should have thrown a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseDurationWithInvalidDurationStringISOWithTimeZoneAndFormat() {
 String durationString = "InvalidFormat";
 long durationMillis = DurationFormatUtils.parseDuration(durationString, false, new GregorianCalendar(2013, 0, 1, 0, 0, 0, Calendar.UTC), DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN);
 fail("Should have thrown a ParseException");
 }
}