package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1500;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("0:02:30", result);
    }

    @Test
    public void testFormatDurationHMSNegative() {
        long durationMillis = -1500;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("-0:02:30", result);
    }

    @Test
    public void testFormatDurationHMSZero() {
        long durationMillis = 0;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("0:00:00", result);
    }

    @Test
    public void testFormatDurationHMSLong() {
        long durationMillis = Long.MAX_VALUE;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        // Test the handling of large numbers.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationHMSInvalidFormatString() {
        long durationMillis = 1500;
        String format = "INVALID_FORMAT";
        DurationFormatUtils.formatDurationHMS(durationMillis, format);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1500;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT2M30S", result);
    }

    @Test
    public void testFormatDurationISONegative() {
        long durationMillis = -1500;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT-2M30S", result);
    }

    @Test
    public void testFormatDurationISZero() {
        long durationMillis = 0;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT0S", result);
    }

    @Test
    public void testFormatDurationISOLong() {
        long durationMillis = Long.MAX_VALUE;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        // Test the handling of large numbers.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationISOInvalidFormatString() {
        long durationMillis = 1500;
        String format = "INVALID_FORMAT";
        DurationFormatUtils.formatDurationISO(durationMillis, format);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1500;
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("0:02:30.000", result);
    }

    // Add test cases for other methods, such as formatDurationWords, formatPeriodISO, formatPeriod, etc.
}