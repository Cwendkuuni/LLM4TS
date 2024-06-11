package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurationFormatUtilsTest {

 private static final long DURATION_1_HOUR = 3600000L;
 private static final long DURATION_2_HOURS = 7200000L;
 private static final long DURATION_1_DAY = 86400000L;
 private static final long DURATION_2_DAYS = 172800000L;
 private static final long DURATION_1_WEEK = 604800000L;
 private static final long DURATION_2_WEEKS = 1209600000L;
 private static final long DURATION_1_MONTH = 2678400000L;
 private static final long DURATION_2_MONTHS = 5356800000L;
 private static final long DURATION_1_YEAR = 31536000000L;
 private static final long DURATION_2_YEARS = 63072000000L;

 @org.junit.Test
 public void testFormatDurationHMS() {
 String duration = DurationFormatUtils.formatDurationHMS(DURATION_1_HOUR);
 assertEquals("01:01:01", duration);
 }

 @org.junit.Test
 public void testFormatDurationISO() {
 String duration = DurationFormatUtils.formatDurationISO(DURATION_1_DAY);
 assertEquals("P1D", duration);
 }

 @org.junit.Test
 public void testFormatDurationWithCustomFormat() {
 String duration = DurationFormatUtils.formatDuration(DURATION_1_DAY, "dd day");
 assertEquals("1 day", duration);
 }

 @org.junit.Test
 public void testFormatDurationWithPadWithZeros() {
 String duration = DurationFormatUtils.formatDuration(DURATION_1_HOUR, "01:01:01.000");
 assertEquals("01:01:01.000", duration);
 }

 @org.junit.Test
 public void testFormatDurationWithNegativeValue() {
 String duration = DurationFormatUtils.formatDuration(-DURATION_1_HOUR, "HH:mm:ss.SSS");
 assertEquals("-01:01:01.000", duration);
 }

 @org.junit.Test
 public void testFormatDurationWords() {
 String duration = DurationFormatUtils.formatDurationWords(DURATION_1_DAY, false, false);
 assertEquals("1 day", duration);
 }

 @org.junit.Test
 public void testFormatDurationWordsWithLeadingZeroSuppression() {
 String duration = DurationFormatUtils.formatDurationWords(DURATION_1_DAY, true, false);
 assertEquals("1 day", duration);
 }

 @org.junit.Test
 public void testFormatDurationWordsWithTrailingZeroSuppression() {
 String duration = DurationFormatUtils.formatDurationWords(DURATION_1_DAY, false, true);
 assertEquals("1 day", duration);
 }

 @org.junit.Test
 public void testFormatDurationWordsWithLeadingAndTrailingZeroSuppression() {
 String duration = DurationFormatUtils.formatDurationWords(DURATION_1_DAY, true, true);
 assertEquals("1 day", duration);
 }

 @org.junit.Test
 public void testFormatPeriodISO() {
 String period = DurationFormatUtils.formatPeriodISO(0, DURATION_1_DAY);
 assertEquals("P1D", period);
 }

 @org.junit.Test
 public void testFormatPeriodISOWithNegativeStartMillis() {
 String period = DurationFormatUtils.formatPeriodISO(-DURATION_1_DAY, 0);
 assertEquals("P-1D", period);
 }

 @org.junit.Test
 public void testFormatPeriod() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_1_DAY, "dd day");
 assertEquals("1 day", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithCustomFormat() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_1_DAY, "d days h hours m minutes s seconds");
 assertEquals("1 day 0 hours 0 minutes 0 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithNegativeStartMillis() {
 String period = DurationFormatUtils.formatPeriod(-DURATION_1_DAY, 0, "dd day h hours m minutes s seconds");
 assertEquals("-1 day 23 hours 59 minutes 59 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithNegativeEndMillis() {
 String period = DurationFormatUtils.formatPeriod(0, -DURATION_1_DAY, "dd day h hours m minutes s seconds");
 assertEquals("0 day -1 day 23 hours 59 minutes 59 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithLeadingDays() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_2_DAYS, "dd day h hours m minutes s seconds");
 assertEquals("2 days 0 hours 0 minutes 0 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithLeadingWeeks() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_2_WEEKS * 7, "ww weeks d days h hours m minutes s seconds");
 assertEquals("2 weeks 0 days 0 hours 0 minutes 0 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithLeadingMonths() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_2_MONTHS * 30, "MM months d days h hours m minutes s seconds");
 assertEquals("6 months 0 days 0 hours 0 minutes 0 seconds", period);
 }

 @org.junit.Test
 public void testFormatPeriodWithLeadingYears() {
 String period = DurationFormatUtils.formatPeriod(0, DURATION_2_YEARS * 365, "yyyy years w weeks d days h hours m minutes s seconds");
 assertEquals("6 years 0 weeks 0 days 0 hours 0 minutes 0 seconds", period);
 }

 @org.junit.Test
 public void testLexx() {
 List<Token> tokens = DurationFormatUtils.lexx("'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'");
 assertEquals(14, tokens.size());
 List<Token> expectedTokens = Arrays.asList(
 new Token('P'),
 new Token('y', 4),
 new Token('Y'),
 new Token('M'),
 new Token('M'),
 new Token('d'),
 new Token('T'),
 new Token('H'),
 new Token('H'),
 new Token('m'),
 new Token('M'),
 new Token('s'),
 new Token('S')
 );
 assertEquals(expectedTokens, tokens);
 }

 @org.junit.Test
 public void testFormat() {
 Token[] tokens = DurationFormatUtils.lexx("yMMddHHmmssSSS");
 long durationMillis = 1234567890123L;
 long days = durationMillis / 86400000L;
 long hours = (durationMillis / 3600000L) % 24;
 long minutes = (durationMillis / 60000L) % 60;
 long seconds = (durationMillis / 1000L) % 60;
 long milliseconds = durationMillis % 1000;
 StringBuilder result = new StringBuilder();
 DurationFormatUtils.format(tokens, 0, 0, days, hours, minutes, seconds, milliseconds, true);
 assertEquals("1234567890123", result.toString());
 }
}

 class Token {
 private final Object value;
 private int count;

 public Token(final Object value) {
 this.value = value;
 this.count = 1;
 }

 public Token(final Object value, final int count) {
 this.value = value;
 this.count = count;
 }

 public boolean equals(final Object obj) {
 if (obj instanceof Token) {
 Token token = (Token)obj;
 if (this.value.getClass() == token.value.getClass()) {
 if (this.count == token.count) {
 if (this.value instanceof StringBuilder) {
 return this.value.toString().equals(token.value.toString());
 }
 if (this.value instanceof Number) {
 return this.value.equals(token.value);
 }
 return this.value == token.value;
 }
 }
 }
 return false;
 }

 public int hashCode() {
 return this.value.hashCode();
 }

 public String toString() {
 return StringUtils.repeat(this.value.toString(), this.count);
 }
}