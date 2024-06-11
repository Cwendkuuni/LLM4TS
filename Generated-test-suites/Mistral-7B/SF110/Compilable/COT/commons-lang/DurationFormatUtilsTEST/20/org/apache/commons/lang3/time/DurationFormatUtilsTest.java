package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.util.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "1:46:11.7";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT1H46M11.7S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567;
        String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss", true);
        String expected = "-01:46:11.7";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        String invalidFormat = "xyz";
        try {
            DurationFormatUtils.formatDuration(0L, invalidFormat, true);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format character 'x': 'x'", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFormatDurationWithZeroDays() {
        long durationMillis = 0;
        String result = DurationFormatUtils.formatDuration(durationMillis, "d' days 'H' hours 'm' minutes 's' seconds'", true);
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "1 day 1 hour 46 minutes 11 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516041600000L;
        long endMillis = 1516044789000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT2H11M29.89S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1516044789000L;
        long endMillis = 1516041600000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT-3H11M29.89S";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1516041600000L;
        long endMillis = 1516044789000L;
        String invalidFormat = "xyz";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, invalidFormat);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format character 'x': 'x'", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFormatPeriodWithZeroDuration() {
        long startMillis = 1516041600000L;
        long endMillis = 1516041600000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, null);
        String expected = "";
        assertEquals(expected, result);
    }
}