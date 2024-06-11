package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P2DT2H7M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days h' hours m' minutes s' seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567L;
        String format = "d' days h' hours m' minutes s' seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "02 days 02 hours 07 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        String format = "d' days h' hours m' minutes s' seconds x";
        DurationFormatUtils.formatDuration(1234567L, format);
    }

    @Test
    public void testFormatDurationNegative() {
        long durationMillis = -1234567L;
        String format = "d' days h' hours m' minutes s' seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "-02 days -02 hours -07 minutes -17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodHMS() {
        long startMillis = 1000L;
        long endMillis = 1234567L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1DT2H7M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1000L;
        long endMillis = 1234567L;
        String format = "P";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis, format);
        String expected = "PT1D2H7M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1000L;
        long endMillis = 1234567L;
        String format = "d' day h' hour m' minute s' second";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1 day 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodZeroPadding() {
        long startMillis = 1000L;
        long endMillis = 1234567L;
        String format = "d' day h' hour m' minute s' second";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "01 day 02 hours 07 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1000L * 60 * 60 * 24 + 30 * 60; // 30 hours
        long endMillis = 1234567L;
        String format = "P";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "PT31H2DT2H7M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppression() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithLeadingZeroSuppression() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithTrailingZeroSuppression() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithZeroSuppression() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        String expected = "2 days 2 hours 7 minutes 17 seconds";
        assertEquals(expected, result);
    }
}