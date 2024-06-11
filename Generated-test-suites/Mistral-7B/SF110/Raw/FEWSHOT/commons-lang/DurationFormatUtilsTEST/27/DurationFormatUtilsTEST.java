package org.apache.commons.lang3;

import static org.junit.Assert.*;
import static org.apache.commons.lang3.time.DurationFormatUtils.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = formatDurationHMS(durationMillis);
        String expected = "02:04:37.700";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 25200000L;
        String result = formatDurationISO(durationMillis);
        String expected = "PT3h0m0s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = formatDuration(durationMillis, format);
        String expected = "1d 2h 4m 37s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = formatDuration(durationMillis, format, true);
        String expected = "1d 02h 04m 37s 037";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String result = formatDurationWords(durationMillis, false, false);
        String expected = "1 day 2 hours 4 minutes 37 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516092800000L;
        long endMillis = 1516096400000L;
        String result = formatPeriodISO(startMillis, endMillis);
        String expected = "PT4h0m0s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1516092800000L;
        long endMillis = 1516096400000L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = formatPeriod(startMillis, endMillis, format);
        String expected = "1d 4h 0m 0s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 1516092800000L;
        long endMillis = 1516096400000L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = formatPeriod(startMillis, endMillis, format, true);
        String expected = "1d 04h 00m 00s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithDifferentTimeZones() {
        long startMillis = 1516092800000L; // 2018-01-01 04:00:00 UTC
        long endMillis = 1516096400000L; // 2018-01-01 05:00:00 UTC
        TimeZone timezone1 = TimeZone.getTimeZone("UTC");
        TimeZone timezone2 = TimeZone.getTimeZone("America/Los_Angeles");
        String result = formatPeriod(startMillis, endMillis, format, true, timezone1);
        String expected = "1d 04h 00m 00s";
        assertEquals(expected, result);
        result = formatPeriod(startMillis, endMillis, format, true, timezone2);
        expected = "0d 1h 01m 00s";
        assertEquals(expected, result);
    }
}

class Token {
    // Empty for test
}