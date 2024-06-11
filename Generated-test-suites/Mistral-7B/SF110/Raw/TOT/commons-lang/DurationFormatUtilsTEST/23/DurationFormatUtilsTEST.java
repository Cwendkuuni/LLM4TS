package org.apache.commons.lang3.time;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String expected = "11:04:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO_positive() {
        long durationMillis = 1234567;
        String expected = "P1Y1M1DT11H4M37.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO_negative() {
        long durationMillis = -1234567;
        String expected = "P-1Y-1M-1DT-11H-4M-37.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration_invalidFormat() {
        long durationMillis = 1234567;
        DurationFormatUtils.formatDuration(durationMillis, "X");
    }

    @Test
    public void testFormatDuration_padWithZeros() {
        long durationMillis = 123456;
        String expected = "01:04:05";
        String actual = DurationFormatUtils.formatDuration(durationMillis, "M:m:s", true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        String expected = "1 year, 1 month and 1 week, 1 day, 11 hours, 4 minutes and 37 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516099200000L;
        long endMillis = 1516102800000L;
        String expected = "2018-02-22T10:00:00.000Z-2018-02-22T14:00:00.000Z";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod_padWithZeros() {
        long startMillis = 1516099200000L;
        long endMillis = 1516102800000L;
        String expected = "P1DT2H";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, "PdH", true);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriod_invalidFormat() {
        long startMillis = 1516099200000L;
        long endMillis = 1516102800000L;
        DurationFormatUtils.formatPeriod(startMillis, endMillis, "X");
    }
}