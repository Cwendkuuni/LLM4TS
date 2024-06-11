package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1500L;
        String expected = "00:02:30.000";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3600000L;
        String expected = "PT1H";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = -365 * 24 * 60 * 60 * 1000 + 1000;
        String expected = "P-1Y1DPT24H";
        String actual = DurationFormatUtils.formatDuration(durationMillis, DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN, false);
        assertEquals(expected, actual);

        long durationMillis2 = 12 * 60 * 60 * 1000;
        String pattern = "HH:mm:ss";
        String expected2 = "2 hours";
        String actual2 = DurationFormatUtils.formatDuration(durationMillis2, pattern, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected2, actual2);
    }

    @Test(expected = IllegalStateException.class)
    public void testFormatDurationNegativeDuration() {
        long durationMillis = -365 * 24 * 60 * 60 * 1000;
        DurationFormatUtils.formatDuration(durationMillis, DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 365 * 24 * 60 * 60 * 1000;
        String expected = "1 year";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, actual);

        long durationMillis2 = 12 * 60 * 60 * 1000;
        String expected2 = "2 hours";
        String actual2 = DurationFormatUtils.formatDurationWords(durationMillis2, true, false);
        assertEquals(expected2, actual2);

        long durationMillis3 = 12 * 60 * 60 * 1000;
        String expected3 = "2 hours";
        String actual3 = DurationFormatUtils.formatDurationWords(durationMillis3, false, true);
        assertEquals(expected3, actual3);

        long durationMillis4 = -12 * 60 * 60 * 1000;
        String expected4 = "negative 2 hours";
        String actual4 = DurationFormatUtils.formatDurationWords(durationMillis4, false, false);
        assertEquals(expected4, actual4);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1546300800000L; // Thu Jan 03 00:05:20 CET 2019
        long endMillis = 1546301035000L; // Thu Jan 03 00:17:15 CET 2019
        String expected = "P1DT2H17M15.000S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);

        long startMillis2 = System.currentTimeMillis();
        Calendar endCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        endCal.add(Calendar.DAY_OF_YEAR, 3);
        long endMillis2 = endCal.getTimeInMillis();
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        String expected2 = "2023-03-05T00:00:00.000+02:00";
        String actual2 = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, pattern, true, TimeZone.getTimeZone("Europe/Vienna"));
        assertEquals(expected2, actual2);

        long startMillis3 = 1546300800000L;
        long endMillis3 = 1546301400000L;
        String expected3 = "PT1H20M";
        String actual3 = DurationFormatUtils.formatPeriod(startMillis3, endMillis3, null, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected3, actual3);
    }
}

static class Token {
    private final Object value;
    private int count;

    // ... constructor, getters, setters, equals, hashCode, toString are the same as in the original class
}