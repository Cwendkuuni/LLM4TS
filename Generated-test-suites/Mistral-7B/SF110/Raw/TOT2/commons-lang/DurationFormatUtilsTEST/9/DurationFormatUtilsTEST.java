package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 20 minutes, 45.67 seconds
        String expected = "20:45.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);

        durationMillis = -1234567; // -20 minutes, -45.67 seconds
        expected = "-20:-45.67";
        actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);

        durationMillis = 0;
        expected = "0:00.000";
        actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567; // 20 minutes, 45.67 seconds
        String expected = "PT20M45.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);

        durationMillis = -1234567; // -20 minutes, -45.67 seconds
        expected = "PT-20M-45.67S";
        actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);

        durationMillis = 0;
        expected = "PT0S";
        actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567; // 20 minutes, 45.67 seconds
        String format = "d days, H hours, m minutes, s seconds, S milliseconds";
        String expected = "20 days, 0 hours, 20 minutes, 45 seconds, 670 milliseconds";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        durationMillis = -1234567; // -20 minutes, -45.67 seconds
        format = "d days, H hours, m minutes, s seconds, S milliseconds";
        expected = "-20 days, 0 hours, -20 minutes, -45 seconds, -670 milliseconds";
        actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        durationMillis = 0;
        format = "d days, H hours, m minutes, s seconds, S milliseconds";
        expected = "0 days, 0 hours, 0 minutes, 0 seconds, 0 milliseconds";
        actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        format = "P";
        expected = "P";
        actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        format = "y yyyy yyy yyyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy yyy H";
        try {
            actual = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}