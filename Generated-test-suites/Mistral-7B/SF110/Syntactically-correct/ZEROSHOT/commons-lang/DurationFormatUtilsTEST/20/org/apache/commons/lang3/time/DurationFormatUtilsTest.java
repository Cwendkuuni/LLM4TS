package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.text.ParseException;
import java.util.*;

public class DurationFormatUtilsTest {

 private static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
 private static final long DURATION_MILLIS_1 = 1479663233231L; // Thu, 24 Mar 2016 16:37:13 GMT
 private static final long DURATION_MILLIS_2 = 1479663243231L; // Thu, 24 Mar 2016 16:37:23 GMT
 private static final long DURATION_MILLIS_3 = 3600000L; // 1 hour
 private static final long DURATION_MILLIS_4 = 60000L; // 1 minute
 private static final long DURATION_MILLIS_5 = 1000L; // 1 second

 @org.junit.Test
 public void testFormatDurationHMS() {
 final String durationHms = DurationFormatUtils.formatDurationHMS(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH);
 assertEquals("01:05:46.854", durationHms);
 }

 @org.junit.Test
 public void testFormatDurationISO() {
 final String durationIso = DurationFormatUtils.formatDurationISO(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH);
 assertEquals("-P1M", durationIso);
 }

 @org.junit.Test
 public void testFormatDuration() {
 final String durationCustom = DurationFormatUtils.formatDuration(DurationFormatUtils.MILLIS_TO_DURATION_2_HOURS, "h:mm:ss:SSS");
 assertEquals("02:02:02.002", durationCustom);
 }

 @org.junit.Test
 public void testFormatDurationWithPadZeros() {
 final String durationCustomPadded = DurationFormatUtils.formatDuration(DurationFormatUtils.MILLIS_TO_DURATION_2_HOURS, "hh:mm:ss:SSS");
 assertEquals("02:02:02.002", durationCustomPadded);
 final String durationCustomPadded3 = DurationFormatUtils.formatDuration(DurationFormatUtils.MILLIS_TO_DURATION_2_HOURS, "hh:mm:ss:SSS", true);
 assertEquals("02:02:02.002", durationCustomPadded3);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 DurationFormatUtils.formatDuration(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH, "invalidFormat");
 }

 @org.junit.Test
 public void testFormatDurationWords() {
 final String durationWords = DurationFormatUtils.formatDurationWords(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH, false, false);
 assertEquals("1 month", durationWords);
 final String durationWordsNoLeadingZero = DurationFormatUtils.formatDurationWords(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH, true, false);
 assertEquals("1 month", durationWordsNoLeadingZero);
 final String durationWordsNoTrailingZero = DurationFormatUtils.formatDurationWords(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH, false, true);
 assertEquals("1 month", durationWordsNoTrailingZero);
 final String durationWordsLeadingZeroAndTrailingZero = DurationFormatUtils.formatDurationWords(DurationFormatUtils.MILLIS_TO_DURATION_1_MONTH, true, true);
 assertEquals("1 month", durationWordsLeadingZeroAndTrailingZero);
 }

 @org.junit.Test
 public void testFormatPeriodISO() {
 final Date startDate = new Date(DURATION_MILLIS_1);
 final Date endDate = new Date(DURATION_MILLIS_2);
 final String durationIso = DurationFormatUtils.formatPeriodISO(DURATION_MILLIS_1, DURATION_MILLIS_2);
 assertEquals("P1DT2H", durationIso);
 }

 @org.junit.Test
 public void testFormatPeriod() {
 final Date startDate = new Date(DURATION_MILLIS_1);
 final Date endDate = new Date(DURATION_MILLIS_2);
 final String durationCustom = DurationFormatUtils.formatPeriod(DURATION_MILLIS_1, DURATION_MILLIS_2, "d:HH:mm:ss.SSS");
 assertEquals("1d 2h", durationCustom);
 }

 @org.junit.Test
 public void testFormatPeriodWithPadZeros() {
 final Date startDate = new Date(DURATION_MILLIS_1);
 final Date endDate = new Date(DURATION_MILLIS_2);
 final String durationCustomPadded = DurationFormatUtils.formatPeriod(DURATION_MILLIS_1, DURATION_MILLIS_2, "dd:HH:mm:ss.SSS");
 assertEquals("1d 02h", durationCustomPadded);
 final String durationCustomPadded3 = DurationFormatUtils.formatPeriod(DURATION_MILLIS_1, DURATION_MILLIS_2, "dd:HH:mm:ss.SSS", true);
 assertEquals("1d 02h", durationCustomPadded3);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodInvalidFormat() {
 DurationFormatUtils.formatPeriod(DURATION_MILLIS_1, DURATION_MILLIS_2, "invalidFormat");
 }

 @org.junit.Test
 public void testLexx() {
 final String format = "d:HH:mm:ss.SSS";
 final Token[] tokens = DurationFormatUtils.lexx(format);
 assertEquals(6, tokens.length);
 for (int i = 0; i < tokens.length; ++i) {
 final Token token = tokens[i];
 if (i == 0) {
 assertEquals(Integer.toString(1), token.getValue().toString());
 assertEquals(Integer.toString(1), token.toString());
 }
 else if (i == 1) {
 assertEquals("d", token.getValue().toString());
 assertEquals("d", token.toString());
 }
 else if (i == 2) {
 assertEquals("H", token.getValue().toString());
 assertEquals("H", token.toString());
 }
 else if (i == 3) {
 assertEquals("mm", token.getValue().toString());
 assertEquals("mm", token.toString());
 }
 else if (i == 4) {
 assertEquals("ss", token.getValue().toString());
 assertEquals("ss", token.toString());
 }
 else if (i == 5) {
 assertEquals("SSS", token.getValue().toString());
 assertEquals("SSS", token.toString());
 }
 }
 }

 class Token {
 private final Object value;
 private int count;

 static boolean containsTokenWithValue(Token[] tokens, Object value) {
 for (Token token : tokens) {
 if (token.getValue().equals(value)) {
 return true;
 }
 }
 return false;
 }

 Token(Object value) {
 this.value = value;
 this.count = 1;
 }

 Token(Object value, int count) {
 this.value = value;
 this.count = count;
 }

 void increment() {
 ++this.count;
 }

 int getCount() {
 return this.count;
 }

 Object getValue() {
 return this.value;
 }

 @Override
 public boolean equals(Object obj) {
 if (obj instanceof Token) {
 Token token = (Token)obj;
 return this.value.equals(token.value) && this.count == token.count;
 }
 return false;
 }

 @Override
 public int hashCode() {
 return this.value.hashCode();
 }

 @Override
 public String toString() {
 StringBuilder buffer = new StringBuilder();
 if (this.value instanceof StringBuilder) {
 buffer.append(this.value.toString());
 }
 else {
 buffer.append(this.value);
 }
 return String.format("%s%d", new Object[] { this.value, this.count });
 }
 }
}