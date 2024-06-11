package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedOutput = "02:04:17.670";
        String actualOutput = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedOutput = "P2D0H4S17.67";
        String actualOutput = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationWithCustomFormat() {
        long durationMillis = 1234567L;
        String expectedOutput = "2 days 4 hours 17 minutes 37.67 seconds";
        String actualOutput = DurationFormatUtils.formatDuration("d ' days 'H ' hours 'm' minutes 's' seconds", durationMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatDurationWithCustomFormatAndZeroSuppression() {
        long durationMillis = 1234567L;
        String expectedOutput = "2d 4h 17m 37.67s";
        String actualOutput = DurationFormatUtils.formatDuration("d ' days 'H ' hours 'm' minutes 's' seconds", durationMillis, true, true);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1577846400000L; // 1st January 2020, 00:00:00 UTC
        long endMillis = 1577847700000L; // 1st January 2020, 03:35:00 UTC
        String expectedOutput = "P1DT2H15M";
        String actualOutput = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatPeriodWithCustomFormat() {
        long startMillis = 1577846400000L; // 1st January 2020, 00:00:00 UTC
        long endMillis = 1577847700000L; // 1st January 2020, 03:35:00 UTC
        String expectedOutput = "1d 2h 15m";
        String actualOutput = DurationFormatUtils.formatPeriod("d ' days 'h ' hours 'm' minutes", startMillis, endMillis);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatPeriodWithCustomFormatAndZeroSuppression() {
        long startMillis = 1577846400000L; // 1st January 2020, 00:00:00 UTC
        long endMillis = 1577847700000L; // 1st January 2020, 03:35:00 UTC
        String expectedOutput = "1d 2h 15m";
        String actualOutput = DurationFormatUtils.formatPeriod("d ' days 'h ' hours 'm' minutes", startMillis, endMillis, true, true);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFormatPeriodWithCustomFormatAndTimeZone() {
        long startMillis = 1577846400000L; // 1st January 2020, 00:00:00 UTC
        long endMillis = 1577847700000L; // 1st January 2020, 03:35:00 UTC
        String expectedOutput = "PT1H35M";
        String actualOutput = DurationFormatUtils.formatPeriod("P", startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getTimeZone("UTC"));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testParseDurationWithInvalidFormat() {
        String invalidFormat = "invalidFormat";
        long durationMillis = DurationFormatUtils.parseDuration(invalidFormat);
        assertEquals(-1, durationMillis);
    }

    @Test
    public void testParseDurationWithValidFormat() {
        long durationMillis = 1234567L;
        String format = "HH:mm:ss.SSS";
        long parsedDurationMillis = DurationFormatUtils.parseDuration(format, "01:02:03.456");
        assertEquals(durationMillis, parsedDurationMillis);
    }

    @Test
    public void testParseDurationWithCustomFormat() {
        long durationMillis = 1234567L;
        String format = "d ' days 'H ' hours 'm' minutes 's' seconds";
        long parsedDurationMillis = DurationFormatUtils.parseDuration(format, "2d 4h 17m 37.67s");
        assertEquals(durationMillis, parsedDurationMillis);
    }
}

class Token {
    // Empty for testing purposes
}