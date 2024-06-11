package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;

import java.util.*;

public class DurationFormatUtilsTest {

    // Basic test cases for formatDurationHMS method
    @Test
    public void testFormatDurationHMSWithValidDurationMillis() {
        String result = formatDurationHMS(1234567L);
        String expected = "11 days 01:56:51.567";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationHMSWithZeroDurationMillis() {
        String result = formatDurationHMS(0L);
        String expected = "00:00:00.000";
        assertEquals(expected, result);
    }

    // Basic test cases for formatDurationISO method
    @Test
    public void testFormatDurationISOWithValidDurationMillis() {
        String result = formatDurationISO(1234567L);
        String expected = "-P11D";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISOWithZeroDurationMillis() {
        String result = formatDurationISO(0L);
        String expected = "PT0S";
        assertEquals(expected, result);
    }

    // Basic test cases for formatDuration method
    @Test
    public void testFormatDurationWithValidDurationMillisAndFormat() {
        String result = formatDuration(1234567L, "dd hh:mm:ss.SSS");
        String expected = "11d 01:56:51.567";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithValidDurationMillisAndCustomFormatWithoutPadding() {
        String result = formatDuration(1234567L, "ddhhmmssSSS");
        String expected = "1111015651567";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithZeroDurationMillisAndFormat() {
        String result = formatDuration(0L, "dd hh:mm:ss.SSS");
        String expected = "00d 00:00:00.000";
        assertEquals(expected, result);
    }

    // Basic test cases for formatDurationWords method
    @Test
    public void testFormatDurationWordsWithValidDurationMillis() {
        String result = formatDurationWords(1234567L, false, false);
        String expected = "11 days, 1 hour, 56 minutes, 51 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithNegativeDurationMillis() {
        String result = formatDurationWords(-1234567L, false, false);
        String expected = "-11 days, -1 hour, -56 minutes, -51 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithPositiveDurationMillisAndSuppressLeadingZeroElements() {
        String result = formatDurationWords(1234567L, true, false);
        String expected = "11 days 1 hour 56 minutes 51 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithNegativeDurationMillisAndSuppressLeadingZeroElements() {
        String result = formatDurationWords(-1234567L, true, false);
        String expected = "-11 days -1 hour -56 minutes -51 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithPositiveDurationMillisAndSuppressTrailingZeroElements() {
        String result = formatDurationWords(1234567L, false, true);
        String expected = "11 days, 1 hour, 56 minutes";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithNegativeDurationMillisAndSuppressTrailingZeroElements() {
        String result = formatDurationWords(-1234567L, false, true);
        String expected = "-11 days, -1 hour, 56 minutes";
        assertEquals(expected, result);
    }

    // Basic test cases for formatPeriodISO method
    @Test
    public void testFormatPeriodISOWithValidStartMillisAndEndMillis() {
        String result = formatPeriodISO(1234567890123L, 1234567890111L);
        String expected = "-P11Y1M1DPT23H59M59.889S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISOWithValidStartMillisAndZeroEndMillis() {
        String result = formatPeriodISO(1234567890123L, 0L);
        String expected = "PT11Y1M1D";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISOWithZeroStartMillisAndValidEndMillis() {
        String result = formatPeriodISO(0L, 1234567890111L);
        String expected = "-P11Y1M1DPT23H59M59.889S";
        assertEquals(expected, result);
    }

    // Basic test cases for formatPeriod method
    @Test
    public void testFormatPeriodWithValidStartMillisEndMillisAndFormat() {
        String result = formatPeriod(1234567890123L, 1234567890111L, "yyyy-MM-dd'T'HH:mm:ss.SSS");
        String expected = "2030-01-01T23:59:59.889";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithValidStartMillisEndMillisAndCustomFormat() {
        String result = formatPeriod(1234567890123L, 1234567890111L, "yyyy-MM-dd HH:mm:ss.SSS z");
        String expected = "2030-01-01 23:59:59.889 +0000";
        assertEquals(expected, result);
    }

    // Test for edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidDurationMillisAndFormat() {
        formatDuration(-1L, "dd hh:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithValidDurationMillisAndInvalidFormat() {
        formatDuration(1234567L, "dd hh:mm:ss.SSSsss");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithValidDurationMillisAndInvalidFormatWithNullValue() {
        formatDuration(1234567L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithValidDurationMillisAndEmptyFormat() {
        formatDuration(1234567L, "");
    }

    @Test
    public void testFormatDurationWithNegativeDurationMillisAndCustomFormat() {
        String result = formatDuration(-1234567L, "dd hh:mm:ss.SSS");
        String expected = "-11d 01:56:51.567";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithLongerDurationThanMillisAvailable() {
        formatDuration(Long.MAX_VALUE + 1L, "dd hh:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidStartMillis() {
        formatPeriod(-1L, 1234567890111L, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidEndMillis() {
        formatPeriod(1234567890123L, -1L, "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidStartMillisAndCustomFormat() {
        formatPeriod(-1L, 1234567890111L, "dd hh:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidEndMillisAndCustomFormat() {
        formatPeriod(1234567890123L, -1L, "dd hh:mm:ss.SSS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidStartMillisAndEmptyFormat() {
        formatPeriod(-1L, 1234567890111L, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidEndMillisAndEmptyFormat() {
        formatPeriod(1234567890123L, "", "");
    }
}