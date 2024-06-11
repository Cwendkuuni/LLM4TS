package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.*;

public class DurationFormatUtilsTest {

    @org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:47:31.345";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "P3Y1M2D10H47M31.345S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithCustomFormat() {
        long durationMillis = 1234567890L;
        String expected = "3 years, 1 month, 2 days, 10 hours, 47 minutes, 31.345 seconds";
        String format = "y years, M months, d days, H hours, m minutes, s seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String expected = "10:04:27.345";
        String result = DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS", true);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        String expected = "3 years, 1 month, 2 days, 10 hours, 47 minutes, 31.345 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L;
        long endMillis = 1305013456000L;
        String expected = "P2Y1M25D1H13M5.016S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithCustomFormat() {
        long startMillis = 1234567890000L;
        long endMillis = 1305013456000L;
        String expected = "2 years, 1 month, 25 days, 1 hour, 13 minutes, 5.016 seconds";
        String format = "y years, M months, d days, H hours, m minutes, s seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 1234567890000L;
        long endMillis = 1305013456000L;
        String expected = "2 years, 1 month, 25 days, 1 hour, 13 minutes, 05.016 seconds";
        String format = "y years, M months, d days, H hours, m minutes, s seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertEquals(expected, result);
    }
}

class Token {
    // Empty for this test
}