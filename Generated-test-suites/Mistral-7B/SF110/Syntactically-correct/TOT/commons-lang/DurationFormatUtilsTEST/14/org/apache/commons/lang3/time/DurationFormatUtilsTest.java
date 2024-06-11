package org.apache.commons.lang3.time;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        String expectedResult = "2d 2h 13m 17s.333s";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        String expectedResult = "P2D PT2H PT13M PT17S.333S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithNegativeValues() {
        long durationMillis = -1234567; // -2 days, -2 hours, -13 minutes, -17 seconds, -333 milliseconds
        String expectedResult = "-2d -2h -13m -17s.-333s";
        String result = DurationFormatUtils.formatDuration(durationMillis, "m-d'd'h-H'h's-S'S'", false);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        DurationFormatUtils.formatDuration(durationMillis, "X", true);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        String expectedResult = "2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        String expectedResult = "2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
        long durationMillis = 1234567; // 2 days, 2 hours, 13 minutes, 17 seconds, 333 milliseconds
        String expectedResult = "2 days, 2 hours, 13 minutes, 17 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1597171200000L; // 2020-10-12 00:00:00.000 UTC
        long endMillis = 1600332800000L; // 2021-01-01 00:00:00.000 UTC
        String expectedResult = "PT31D";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithCalendars() {
        long startMillis = new GregorianCalendar(2020, 9, 12, 0, 0, 0, Calendar.UTC).getTimeInMillis();
        long endMillis = new GregorianCalendar(2021, 0, 1, 0, 0, 0, Calendar.UTC).getTimeInMillis();
        String expectedResult = "P31D";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, null, false, TimeZone.getDefault());
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1597171200000L; // 2020-10-12 00:00:00.000 UTC
        long endMillis = 1600332800000L; // 2021-01-01 00:00:00.000 UTC
        DurationFormatUtils.formatPeriod(startMillis, endMillis, "X", true, TimeZone.getDefault());
    }
}