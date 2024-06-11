package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DurationFormatUtilsTest {

    

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "01:46:36.700";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400001L;
        String expectedResult = "P1dPT1h";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 2533561235L; // 3 years, 6 months, 1 week, 3 days, 11 hours, 58 minutes, 12 seconds, 355 milliseconds
        String expectedResult = "P3Y6M1W3D11H58M12S355MS";
        assertEquals(expectedResult, DurationFormatUtils.formatDuration(durationMillis, DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN));
    }

    @Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 2533561235L; // 3 years, 6 months, 1 week, 3 days, 11 hours, 58 minutes, 12 seconds, 355 milliseconds
        String expectedResult = "P3 years 6 months 1 week 3 days 11 hours 58 minutes 12 seconds 355 milliseconds";
        assertEquals(expectedResult, DurationFormatUtils.formatDuration(durationMillis, "P y yyyy M M M d d d d H H H h h h h m m m m s s s s S S S S S S S S", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        DurationFormatUtils.formatDuration(0L, "invalid_format");
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516092800000L; // 2017-12-25 00:00:00.000
        long endMillis = 1516106360000L; // 2017-12-25 23:56:00.000
        String expectedResult = "P1DT15H56M";
        assertEquals(expectedResult, DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1516092800000L; // 2017-12-25 00:00:00.000
        long endMillis = 1516106360000L; // 2017-12-25 23:56:00.000
        String expectedResult = "P1dPT15h";
        assertEquals(expectedResult, DurationFormatUtils.formatPeriod(startMillis, endMillis, DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodInvalidFormat() {
        DurationFormatUtils.formatPeriod(0L, 0L, "invalid_format", false);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1516092800000L; // 2017-12-25 00:00:00.000
        long endMillis = 1516106360000L; // 2017-12-25 23:56:00.000
        String expectedResult = "P1dPT15h";
        assertEquals(expectedResult, DurationFormatUtils.formatPeriod(startMillis, endMillis, DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN, false, TimeZone.getTimeZone("UTC")));
    }

    @Test
    public void testFormatDurationWordsSuppressLeadingZeros() {
        long durationMillis = 2533561235L; // 3 years, 6 months, 1 week, 3 days, 11 hours, 58 minutes, 12 seconds, 355 milliseconds
        String expectedResult = "3 years 6 months 1 week 3 days 11 hours 58 minutes 12 seconds";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationWords(durationMillis, true, false));
    }

    @Test
    public void testFormatDurationWordsSuppressTrailingZeros() {
        long durationMillis = 2533561235L; // 3 years, 6 months, 1 week, 3 days, 11 hours, 58 minutes, 12 seconds, 355 milliseconds
        String expectedResult = "3 years 6 months 1 week 3 days 11 hours 58 minutes";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationWords(durationMillis, false, true));
    }

    @Test
    public void testFormatDurationWordsSuppressBoth() {
        long durationMillis = 2533561235L; // 3 years, 6 months, 1 week, 3 days, 11 hours, 58 minutes, 12 seconds, 355 milliseconds
        String expectedResult = "3 years 6 months 1 week 3 days 11 hours 58 minutes 12 seconds 355 milliseconds";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationWords(durationMillis, true, true));
    }

    @Test
    public void testFormatDurationWordsNegativeDuration() {
        long durationMillis = -2533561235L; // -3 years, -6 months, -1 week, -3 days, -11 hours, -58 minutes, -12 seconds, -355 milliseconds
        String expectedResult = "-3 years -6 months -1 week -3 days -11 hours -58 minutes -12 seconds -355 milliseconds";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationWords(durationMillis, true, true));
    }

    @Test
    public void testFormatDurationWordsEmpty() {
        long durationMillis = 0L;
        String expectedResult = "";
        assertEquals(expectedResult, DurationFormatUtils.formatDurationWords(durationMillis, true, true));
    }

    @Test
    public void testParseDurationHMS() throws ParseException {
        String durationString = "01:46:36.700";
        long expectedDurationMillis = 1234567L;
        Calendar resultCalendar = DurationFormatUtils.parseDuration(durationString, null);
        long durationMillis = resultCalendar.getTimeInMillis();
        assertEquals(expectedDurationMillis, durationMillis);
    }

    @Test(expected = ParseException.class)
    public void testParseDurationISOInvalidFormat() throws ParseException {
        DurationFormatUtils.parseDuration("Pinvalid_format", null);
    }

    @Test(expected = ParseException.class)
    public void testParseDurationISOInvalidValue() throws ParseException {
        DurationFormatUtils.parseDuration("P-1d", null);
    }

    @Test
    public void testParseDuration() throws ParseException {
        String durationString = "P3Y6M1W3D11H58M12S355MS";
        Calendar resultCalendar = DurationFormatUtils.parseDuration(durationString, null);
        long expectedDurationMillis = 2533561235L;
        long durationMillis = resultCalendar.getTimeInMillis();
        assertEquals(expectedDurationMillis, durationMillis);
    }

    @Test(expected = ParseException.class)
    public void testParseDurationInvalidFormat() throws ParseException {
        DurationFormatUtils.parseDuration("P y yyyy M M M d d d d H H H h h h h m m m m s s s s S S S S S S S S", null);
    }

    
}