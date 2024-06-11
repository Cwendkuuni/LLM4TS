package org.junit.tests;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        // Test normal case
        long durationMillis = 123456;
        String expected = "1:46:06.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);

        // Test edge cases for small durations
        long smallNegativeDurationMillis = -1;
        String smallNegativeExpected = "-0:00:01.000";
        String smallNegativeResult = DurationFormatUtils.formatDurationHMS(smallNegativeDurationMillis);
        assertEquals(smallNegativeExpected, smallNegativeResult);

        long smallDurationMillis = 0;
        String smallDurationExpected = "0:00:00.000";
        String smallDurationResult = DurationFormatUtils.formatDurationHMS(smallDurationMillis);
        assertEquals(smallDurationExpected, smallDurationResult);

        // Test edge cases for large durations
        long largeDurationMillis = Long.MAX_VALUE;
        String largeDurationExpected = "P22YT23H59M59.999S";
        String largeDurationResult = DurationFormatUtils.formatDurationHMS(largeDurationMillis);
        assertEquals(largeDurationExpected, largeDurationResult);

        long veryLargeDurationMillis = Long.MAX_VALUE + 1;
        String veryLargeDurationExpected = "P23Y0M0.001S";
        String veryLargeDurationResult = DurationFormatUtils.formatDurationHMS(veryLargeDurationMillis);
        assertEquals(veryLargeDurationExpected, veryLargeDurationResult);
    }

    @Test
    public void testFormatDurationISO() {
        // Test normal case
        long durationMillis = 123456;
        String expected = "PT1H46M6.000S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);

        // Test edge cases for small durations
        long smallNegativeDurationMillis = -1;
        String smallNegativeExpected = "PT0S";
        String smallNegativeResult = DurationFormatUtils.formatDurationISO(smallNegativeDurationMillis);
        assertEquals(smallNegativeExpected, smallNegativeResult);

        long smallDurationMillis = 0;
        String smallDurationExpected = "PT0S";
        String smallDurationResult = DurationFormatUtils.formatDurationISO(smallDurationMillis);
        assertEquals(smallDurationExpected, smallDurationResult);

        // Test edge cases for large durations
        long largeDurationMillis = Long.MAX_VALUE;
        String largeDurationExpected = "PT22Y11M30DT23H59M59.999S";
        String largeDurationResult = DurationFormatUtils.formatDurationISO(largeDurationMillis);
        assertEquals(largeDurationExpected, largeDurationResult);

        long veryLargeDurationMillis = Long.MAX_VALUE + 1;
        String veryLargeDurationExpected = "PT23Y0M0.001S";
        String veryLargeDurationResult = DurationFormatUtils.formatDurationISO(veryLargeDurationMillis);
        assertEquals(veryLargeDurationExpected, veryLargeDurationResult);
    }

    @Test
    public void testFormatDuration() {
        // Test normal case
        long durationMillis = 123456;
        String format = "d H:mm:ss";
        String expected = "1 1:46:06";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        // Test edge cases for small durations
        long smallNegativeDurationMillis = -1;
        String smallNegativeFormat = "d H:mm:ss";
        String smallNegativeExpected = "0 0:00:01";
        String smallNegativeResult = DurationFormatUtils.formatDuration(smallNegativeDurationMillis, smallNegativeFormat);
        assertEquals(smallNegativeExpected, smallNegativeResult);

        long smallDurationMillis = 0;
        String smallDurationFormat = "d H:mm:ss";
        String smallDurationExpected = "0 0:00:00";
        String smallDurationResult = DurationFormatUtils.formatDuration(smallDurationMillis, smallDurationFormat);
        assertEquals(smallDurationExpected, smallDurationResult);

        // Test edge cases for large durations
        long largeDurationMillis = Long.MAX_VALUE;
        String largeDurationFormat = "P";
        String largeDurationExpected = "P22Y11M30DT23H59M59.999S";
        String largeDurationResult = DurationFormatUtils.formatDuration(largeDurationMillis, largeDurationFormat);
        assertEquals(largeDurationExpected, largeDurationResult);

        long veryLargeDurationMillis = Long.MAX_VALUE + 1;
        String veryLargeDurationFormat = "P";
        String veryLargeDurationExpected = "P23Y0M0.001S";
        String veryLargeDurationResult = DurationFormatUtils.formatDuration(veryLargeDurationMillis, veryLargeDurationFormat);
        assertEquals(veryLargeDurationExpected, veryLargeDurationResult);

        // Test different format scenarios
        long durationMillisForDifferentFormat = 1234567;
        String formatForDifferentFormat = "m:ss.SSS";
        String expectedForDifferentFormat = "20:46.066";
        String resultForDifferentFormat = DurationFormatUtils.formatDuration(durationMillisForDifferentFormat, formatForDifferentFormat);
        assertEquals(expectedForDifferentFormat, resultForDifferentFormat);
    }
}