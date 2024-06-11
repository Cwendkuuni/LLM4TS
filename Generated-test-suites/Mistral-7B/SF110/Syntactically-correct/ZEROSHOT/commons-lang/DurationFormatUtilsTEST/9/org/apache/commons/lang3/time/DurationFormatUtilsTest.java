package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.TimeBase;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String expectedResult = "10:04:35.670";
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String expectedResult = "P1Y1M1DT10H4M35.67S";
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567L;
 String expectedResult = "P1Y1M1DT10H4M35.670S";
 String format = "P%dy%dM%dMT%dH%dM%dS%d.%dS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 long durationMillis = 1234567L;
 String format = "P%dy%dM%dMT%dH%dM%dS%d.%d";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = false;
 String expectedResult = "1 year, 1 month and 1 day, 10 hours, 4 minutes and 35.67 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsSuppressLeadingZeroElements() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = false;
 String expectedResult = "1 year, 1 month, 1 day, 10 hours, 4 minutes and 35.67 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsSuppressTrailingZeroElements() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = false;
 boolean suppressTrailingZeroElements = true;
 String expectedResult = "1 year, 1 month, 1 day, 10 hours, 4 minutes and 35 seconds";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatDurationWordsSuppressBothZeroElements() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = true;
 String expectedResult = "1 year, 1 month, 1 day, 10 hours, 4 minutes";
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 String expectedResult = "P1DT23H59M59.999S";
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 String expectedResult = "P1DT23H59M59.999S";
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS'S'";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithTimeZone() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 String expectedResult = "P1DT23H59M59.999S";
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS'S'";
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZone);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithInvalidFormat() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testFormatPeriodWithPastStartDate() {
 long startMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 long endMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS'S'";
 String expectedResult = "P-1DT-23H-59M-59.999S";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithFutureEndDate() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451622400000L; // 2015-11-02 00:00:00.000
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS'S'";
 String expectedResult = "P2DT1D";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatPeriodWithInvalidTokens() {
 long startMillis = 1451606400000L; // 2015-11-01 00:00:00.000
 long endMillis = 1451613600000L; // 2015-11-01 23:59:59.999
 String format = "'P'yyyy'Y'MM'M'd'DT'HH'H'mm'M'ss.SSS'T'"; // extra 'T' token
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 fail("Expected IllegalArgumentException");
 }

 private static class TestToken implements Token {
 private final StringBuilder buffer;

 TestToken() {
 this.buffer = new StringBuilder();
 }

 TestToken(StringBuilder buffer) {
 this.buffer = buffer;
 }

 @Override
 public boolean equals(Object obj) {
 return false; // override for testing
 }

 @Override
 public int hashCode() {
 return 0; // override for testing
 }

 @Override
 public Object getValue() {
 return this.buffer.toString();
 }

 @Override
 public String toString() {
 return this.buffer.toString();
 }
 }

 static {
 DurationFormatUtils.y = "y";
 DurationFormatUtils.M = "M";
 DurationFormatUtils.d = "d";
 DurationFormatUtils.H = "H";
 DurationFormatUtils.m = "m";
 DurationFormatUtils.s = "s";
 DurationFormatUtils.S = "S";
 }
}