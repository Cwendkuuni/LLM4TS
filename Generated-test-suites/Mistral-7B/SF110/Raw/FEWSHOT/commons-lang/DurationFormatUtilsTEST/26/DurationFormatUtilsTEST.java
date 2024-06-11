package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("00:01:02.345", DurationFormatUtils.formatDurationHMS(102345L));
        assertEquals("00:00:01", DurationFormatUtils.formatDurationHMS(1000L));
        assertEquals("02:15:30.456", DurationFormatUtils.formatDurationHMS(81530456L));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("P2D2H15M30.456S", DurationFormatUtils.formatDurationISO(2021530456L));
        assertEquals("P1W1D1H1M1S1", DurationFormatUtils.formatDurationISO(86400051L));
        assertEquals("P0D0H0M1.005S", DurationFormatUtils.formatDurationISO(1056L));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("2 days, 2 hours, 15 minutes, 30.456 seconds", DurationFormatUtils.formatDuration(2021530456L, "d, H, m, s."));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds", DurationFormatUtils.formatDuration(86400051L, "W, d, H, m, s."));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds", DurationFormatUtils.formatDuration(1056L, "d, H, m, s, S."));
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        assertEquals("02:01:02.034", DurationFormatUtils.formatDuration(102345L, "H:mm:ss.SSS", true));
        assertEquals("01:01:01", DurationFormatUtils.formatDuration(1000L, "H:mm:ss", true));
        assertEquals("02:15:30.456", DurationFormatUtils.formatDuration(81530456L, "H:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("2 days, 2 hours, 15 minutes, 30 seconds", DurationFormatUtils.formatDurationWords(2021530456L, false, false));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds", DurationFormatUtils.formatDurationWords(86400051L, false, false));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds", DurationFormatUtils.formatDurationWords(1056L, false, false));
        assertEquals("2 days, 2 hours, 15 minutes, 30 seconds", DurationFormatUtils.formatDurationWords(2021530456L, true, false));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds", DurationFormatUtils.formatDurationWords(86400051L, true, false));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds", DurationFormatUtils.formatDurationWords(1056L, true, false));
        assertEquals("2 days, 2 hours, 15 minutes, 30 seconds", DurationFormatUtils.formatDurationWords(2021530456L, false, true));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds", DurationFormatUtils.formatDurationWords(86400051L, false, true));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds", DurationFormatUtils.formatDurationWords(1056L, false, true));
        assertEquals("2 days, 2 hours, 15 minutes, 30 seconds", DurationFormatUtils.formatDurationWords(2021530456L, true, true));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds", DurationFormatUtils.formatDurationWords(86400051L, true, true));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds", DurationFormatUtils.formatDurationWords(1056L, true, true));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("P2D2H15M30.456S", DurationFormatUtils.formatPeriodISO(1234567890123L, 1234567890123L));
        assertEquals("P1W1DT1H51M5S", DurationFormatUtils.formatPeriodISO(1234567890123L, 1234567890120L));
        assertEquals("P0D0H0M1.005S", DurationFormatUtils.formatPeriodISO(1234567L, 1234566L));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("2 days, 2 hours, 15 minutes, 30.456 seconds ago", DurationFormatUtils.formatPeriod(1234567890123L, 1234567890123L, "d, H, m, s.", false));
        assertEquals("1 week, 1 day, 1 hour, 51 minutes, 5 seconds ago", DurationFormatUtils.formatPeriod(1234567890123L, 1234567890120L, "W, d, H, m, s.", false));
        assertEquals("0 days, 0 hours, 1 minute, 56 seconds, 5 milliseconds ago", DurationFormatUtils.formatPeriod(1234567L, 1234566L, "d, H, m, s, S.", false));
    }
}