package org.apache.commons.lang3.time;
import org.apache.commons.lang3.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("2d 3h 22m 56.7s", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT2DPT3HPT22M25.7S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String format = "dd hh:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("02d 03h 22m 56.7s", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatString() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String format = "dd hh:mm:ss.SS"; // one less 'S' than required
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        long durationMillis = -1234567; // -2 days, -3 hours, -22 minutes, -56.7 seconds
        String format = "dd hh:mm:ss.SSS";
        try {
            DurationFormatUtils.formatDuration(durationMillis, format);
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot format negative duration", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxDuration() {
        long durationMillis = Long.MAX_VALUE; // maximum long value
        String format = "dd hh:mm:ss.SSS";
        try {
            DurationFormatUtils.formatDuration(durationMillis, format);
        } catch (IllegalArgumentException e) {
            assertEquals("Duration is too large to be represented as a String", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenCombination() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String format = "dd HH:mm:ss.SSS"; // 'dd' and 'HH' both present
        try {
            DurationFormatUtils.formatDuration(durationMillis, format);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format string: 'dd' and 'HH' are both present", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567; // 2 days, 3 hours, 22 minutes, 56.7 seconds
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals("2 days, 3 hours, 22 minutes, 56.7 seconds", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1464188800000L; // 2016-05-11 00:00:00.000000Z
        long endMillis = 1464192400000L; // 2016-05-11 23:59:59.999999Z
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("PT1D", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1464188800000L; // 2016-05-11 00:00:00.000000Z
        long endMillis = 1464192400000L; // 2016-05-11 23:59:59.999999Z
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("2016-05-11T00:00:01.000PT1D", result);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1464188800000L; // 2016-05-11 00:00:00.000000Z
        long endMillis = 1464192400000L; // 2016-05-11 23:59:59.999999Z
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        assertEquals("2016-05-11T00:00:01.000ZPT1D", result);
    }
}