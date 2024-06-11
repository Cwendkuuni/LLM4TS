package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String expected = "10:04:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String expected = "P10D10H4M37.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDuration() {
        long durationMillis = 1234567;
        String expected = "10 days 10 hours 04 minutes 37.67 seconds";
        String format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567;
        String expected = "10d 10h 04m 37.67s";
        String format = "d'd' h'h' m'm' s.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        String expected = "10 days, 10 hours, 4 minutes, 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWordsWithZeroSuppression() {
        long durationMillis = 1234567;
        String expected = "10d 10h 04m 37s";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWordsWithBothZeroSuppression() {
        long durationMillis = 1234567;
        String expected = "10d 10h 4m 37s";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1516060800000L; // 2018-01-01 00:00:00.000
        long endMillis = 1516064400000L; // 2018-01-01 04:04:00.000
        String expected = "P1DT4H";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriod() {
        long startMillis = 1516060800000L; // 2018-01-01 00:00:00.000
        long endMillis = 1516064400000L; // 2018-01-01 04:04:00.000
        String expected = "1 day 4 hours";
        String format = "d 'day' H 'hours'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithZeroPadding() {
        long startMillis = 1516060800000L; // 2018-01-01 00:00:00.000
        long endMillis = 1516064400000L; // 2018-01-01 04:04:00.000
        String expected = "1d 4h";
        String format = "d'd' H'h'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1516060800000L; // 2018-01-01 00:00:00.000
        long endMillis = 1516147200000L; // 2018-01-02 16:00:00.000 (UTC+8)
        String timeZoneId = "Asia/Kuala_Lumpur";
        String expected = "P2DT8H";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis, null, null, TimeZone.getTimeZone(timeZoneId));
        assertEquals(expected, result);
    }
}

class Token {
    // Empty for JUnit test
}