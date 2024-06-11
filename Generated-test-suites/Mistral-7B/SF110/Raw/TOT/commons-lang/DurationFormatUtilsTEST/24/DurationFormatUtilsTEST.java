package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FormatUtilsDateParser;
import org.apache.commons.lang3.time.ParsePosition;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:11.7";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "P2Y1M3DT8H21M11.7S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567890L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expected = "-P2Y1M3DT8H21M11.7S";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithTokens() {
        long durationMillis = 1234567890L;
        String format = "d H m s";
        String expected = "3 21 11";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithTokensAndPadWithZeros() {
        long durationMillis = 1234567890L;
        String format = "d H m s";
        String expected = "03 21 11";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true, new TimeZone("UTC"));
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithTokensAndPadWithZerosWithNegativeDuration() {
        long durationMillis = -1234567890L;
        String format = "d H m s";
        String expected = "-03 -21 -11";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true, new TimeZone("UTC"));
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        String expected = "2 years, 1 month, 3 days, 8 hours, 21 minutes, 11 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
        long durationMillis = 1234567890L;
        String expected = "2 years, 1 month, 3 days, 8 hours, 21 minutes, 11 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
        long durationMillis = 1234567890L;
        String expected = "2 years, 1 month, 3 days, 8 hours, 21 minutes";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L;
        long endMillis = 1334567890000L;
        String expected = "P1Y1M1DT1H1M1S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithTokens() {
        long startMillis = 1234567890000L;
        long endMillis = 1334567890000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expected = "P2Y1M1DT1H1M1S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithTokensAndPadWithZeros() {
        long startMillis = 1234567890000L;
        long endMillis = 1334567890000L;
        String format = "d H m s";
        String expected = "1 1 1";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testParseDurationHMSString() {
        String durationString = "1:46:11.7";
        long expected = 1234567;
        long result = DurationFormatUtils.parseDurationHMS(durationString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseDurationISOString() {
        String durationString = "P2Y1M3DT8H21M11.7S";
        long expected = 1234567890000L;
        long result = DurationFormatUtils.parseDurationISO(durationString);
        assertEquals(expected, result);
    }

    @Test
    public void testParseDurationISOStringWithParsePosition() {
        String durationString = "P2Y1M3DT8H21M11.7S";
        long expected = 1234567890000L;
        ParsePosition parsePosition = new ParsePosition(0);
        long result = DurationFormatUtils.parseDurationISO(durationString, parsePosition);
        assertEquals(expected, result);
        assertEquals(-1, parsePosition.getIndex());
        assertEquals(-1, parsePosition.getErrorIndex());
    }

    @Test
    public void testParseDurationISOStringWithInvalidFormat() {
        String durationString = "P2Y1M3DT8H21M11.7T";
        long result = DurationFormatUtils.parseDurationISO(durationString);
        assertNull(result);
    }

    @Test
    public void testParseDurationStringWithTokens() {
        String durationString = "3 days, 21 hours, 11 minutes, 1 second";
        long expected = 1234567891L;
        long result = DurationFormatUtils.parseDuration(durationString, new ParsePosition(0));
        assertEquals(expected, result);
        assertEquals(-1, parsePosition.getIndex());
        assertEquals(-1, parsePosition.getErrorIndex());
    }

    @Test
    public void testParseDurationStringWithTokensAndInvalidFormat() {
        String durationString = "3 days, 21 hours, 11 minutes, 1 second, 1 second";
        long result = DurationFormatUtils.parseDuration(durationString, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testParseDurationStringWithTokensAndInvalidFormat2() {
        String durationString = "3 days, 21 hours, 11 minutes, 1 second, 1 hours";
        long result = DurationFormatUtils.parseDuration(durationString, new ParsePosition(0));
        assertNull(result);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1334567890000L;
        long endMillis = 1234567890000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String expected = "P-1Y-1M-1DT-1H-21M-11.7S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithNegativeDurationAndTokens() {
        long startMillis = 1334567890000L;
        long endMillis = 1234567890000L;
        String format = "d H m s";
        String expected = "-1 -21 -11";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890000L;
        long endMillis = 1334567890000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m s", false, TimeZone.getDefault());
        assertNull(result);
    }
}