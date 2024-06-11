package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 15 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 1000; // 3 hours, 30 minutes, 45 seconds
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("03:30:45.000", result);

        durationMillis = 0; // zero duration
        result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("00:00:00.000", result);

        durationMillis = -1; // negative duration
        try {
            result = DurationFormatUtils.formatDurationHMS(durationMillis);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1 * 24 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 1000; // 1 day, 3 hours, 45 minutes
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P1DPT3H30M45S", result);

        durationMillis = 0; // zero duration
        result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P0D", result);

        durationMillis = -1; // negative duration
        try {
            result = DurationFormatUtils.formatDurationISO(durationMillis);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 15 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 1000; // 3 hours, 30 minutes, 45 seconds
        String format = "d 'day' H 'hour' m 'minute' s 'second'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("3 day 3 hour 0 minute 45 second", result);

        format = "d.d h.m.s.SSS";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("3.0.03:30:45.000", result);

        durationMillis = 0; // zero duration
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("0.0.00:00:00.000", result);

        durationMillis = -1; // negative duration
        try {
            result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 15 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 1000; // 3 hours, 30 minutes, 45 seconds
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals("3 hour, 30 minute, 45 second", result);

        durationMillis = 0; // zero duration
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals("0 second", result);

        durationMillis = -1; // negative duration
        try {
            result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1356998400000L; // 2013-01-01 00:00:00.000 UTC
        long endMillis = 1357001600000L; // 2013-01-01 23:59:59.000 UTC
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P1D", result);

        startMillis = 1234567890000L; // arbitrary start date
        endMillis = 1234567895000L; // start + 5 seconds
        result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("PT0.002S", result);

        startMillis = 1356998400000L;
        endMillis = 1356998400000L; // same date, same time
        result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P0D", result);

        startMillis = -1; // negative start date
        try {
            result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1356998400000L; // 2013-01-01 00:00:00.000 UTC
        long endMillis = 1357001600000L; // 2013-01-01 23:59:59.000 UTC
        String format = "d 'day' H 'hour' m 'minute' s 'second'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("1 day", result);

        startMillis = 1234567890000L;
        endMillis = 1234567895000L;
        format = "d.d h.m.s.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("1.0.00:00:02.002", result);

        startMillis = 1356998400000L;
        endMillis = 1356998400000L;
        format = "d 'day' H 'hour' m 'minute' s 'second'";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("0 day", result);

        startMillis = -1;
        try {
            result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}