package org.junit.tests;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "1:46:11";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 123456789;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P1Y1M1W1D2H3M4.689S";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890;
        String format = "dd-MMM-yy HH:mm:ss.SSS z";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "03-Jan-18 13:46:11.689 PDT";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNegativeDuration() {
        long durationMillis = -1234567890L;
        String format = "HH:mm:ss";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testZeroDuration() {
        long durationMillis = 0;
        String format = "HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "00:00:00.000";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatWithInvalidFormat() {
        long durationMillis = 1234567890L;
        String format = "X";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatWithUnknownToken() {
        long durationMillis = 1234567890L;
        String format = "dd-XXX-yy HH:mm:ss.SSS z";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatWithIncorrectTokenCount() {
        long durationMillis = 1234567890L;
        String format = "dd-MMM-yy HH:mi:ss.SSS z";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatWithUnmatchedQuote() {
        long durationMillis = 1234567890L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}