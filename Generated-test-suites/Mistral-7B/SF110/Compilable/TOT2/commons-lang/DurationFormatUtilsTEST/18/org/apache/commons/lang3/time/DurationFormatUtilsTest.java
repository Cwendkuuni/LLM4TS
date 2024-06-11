package org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String expectedOutput = "01:46:13.567";
        String actualOutput = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String expectedOutput = "P1Y1M46DT1H46M13.567S";
        String actualOutput = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationCustomFormat() {
        long durationMillis = 1234567;
        String expectedOutput = "01:46:13.567";
        String format = "HH:mm:ss.SSS";
        String actualOutput = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationNegativeDuration() {
        long durationMillis = -1234567;
        String expectedOutput = "-P1Y1M46DT1H46M13.567S";
        String actualOutput = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationLargeDuration() {
        long durationMillis = Long.MAX_VALUE;
        String expectedOutput = "PT9223372031007031250000S";
        String actualOutput = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testFormatDurationNullFormat() {
        long durationMillis = 1234567;
        String format = null;
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 1234567;
        String format = "XX:XX:XX.XXX";
        String actualOutput = DurationFormatUtils.formatDuration(durationMillis, format);
        String expectedOutput = "Invalid format string: 'XX:XX:XX.XXX'";
        assertEquals(expectedOutput, actualOutput);
    }
}