package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.TimeBase;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 // Test case for formatDurationHMS method
 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567;
 String expectedResult = "02:04:23.700";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDurationISO method with positive duration
 @Test
 public void testFormatDurationISOPositive() {
 long durationMillis = 1234567;
 String expectedResult = "P2D2H4M23.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDurationISO method with negative duration
 @Test
 public void testFormatDurationISONegative() {
 long durationMillis = -1234567;
 String expectedResult = "P-2D-2H-4M-23.7S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDuration method with positive duration
 @Test
 public void testFormatDurationPositive() {
 long durationMillis = 1234567;
 String format = "d' days 'H' hours 'm' minutes 's' seconds";
 String expectedResult = "2 days 4 hours 23 minutes 45.7 seconds";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDuration method with negative duration
 @Test
 public void testFormatDurationNegative() {
 long durationMillis = -1234567;
 String format = "d' days 'H' hours 'm' minutes 's' seconds";
 String expectedResult = "-2 days -4 hours -23 minutes -45.7 seconds";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDuration method with zero duration
 @Test
 public void testFormatDurationZero() {
 long durationMillis = 0;
 String format = "d' days 'H' hours 'm' minutes 's' seconds";
 String expectedResult = "0 days 0 hours 0 minutes 0.0 seconds";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for formatDurationWords method
 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String expectedResult = "2 days 4 hours 23 minutes 45.7 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expectedResult, result);
 }

 // Test case for formatPeriodISO method with positive duration
 @Test
 public void testFormatPeriodISOPositive() {
 long startMillis = 1234567890L;
 long endMillis = 131234567890L;
 String expectedResult = "PT2D2H4M23.7S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 // Test case for formatPeriodISO method with negative duration
 @Test
 public void testFormatPeriodISONegative() {
 long startMillis = 131234567890L;
 long endMillis = 1234567890L;
 String expectedResult = "PT-2D-2H-4M-23.7S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 // Test case for formatPeriod method with positive duration
 @Test
 public void testFormatPeriodPositive() {
 long startMillis = 1234567890L;
 long endMillis = 131234567890L;
 String format = "Pd' days Pdh' hours Pm' minutes 's' seconds";
 String expectedResult = "PT2DPT4HPT23M45.7S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for formatPeriod method with negative duration
 @Test
 public void testFormatPeriodNegative() {
 long startMillis = 131234567890L;
 long endMillis = 1234567890L;
 String format = "Pd' days Pdh' hours Pm' minutes 's' seconds";
 String expectedResult = "PT-2D-PT-4H-PT23M-PT45.7S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for formatPeriod method with zero duration
 @Test
 public void testFormatPeriodZero() {
 long startMillis = 1234567890L;
 long endMillis = 1234567890L;
 String format = "Pd' days Pdh' hours Pm' minutes 's' seconds";
 String expectedResult = "PT0S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 // Test case for edge case: long duration greater than Long.MAX_VALUE
 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationLongMaxValue() {
 long durationMillis = Long.MAX_VALUE + 1;
 String format = "H:mm:ss.SSS";
 DurationFormatUtils.formatDuration(durationMillis, format);
 }

 // Test case for edge case: negative duration less than Long.MIN_VALUE
 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationLongMinValue() {
 long durationMillis = Long.MIN_VALUE - 1;
 String format = "H:mm:ss.SSS";
 DurationFormatUtils.formatDuration(durationMillis, format);
 }

 // Test case for edge case: format string contains invalid token
 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidToken() {
 long durationMillis = 1234567;
 String format = "d' days 'H' hours 'm' minutes 'x' seconds";
 DurationFormatUtils.formatDuration(durationMillis, format);
 }
}