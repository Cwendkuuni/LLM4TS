package org.apache.commons.lang3.time; // Import the class under test and necessary dependencies

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    

    @Test
    public void testFormatDurationHMS() {
        long duration = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        String expected = "1:02:03.456";
        String actual = DurationFormatUtils.formatDurationHMS(duration);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long duration = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        String expected = "P1D PT2H PT3M PT456S";
        String actual = DurationFormatUtils.formatDurationISO(duration);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long duration = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expected = "1 days 2 hours 3 minutes 0.456 seconds";
        String actual = DurationFormatUtils.formatDuration(duration, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        // Testing for zero durations
        long duration1 = 0L;
        String format1 = "d' days 'H' hours 'm' minutes 's' seconds";
        String expected1 = "0 days 0 hours 0 minutes 0.000 seconds";
        String actual1 = DurationFormatUtils.formatDuration(duration1, format1);
        Assert.assertEquals(expected1, actual1);

        // Testing for negative durations
        long duration2 = -1234567L; // -1 day, -2 hours, -3 minutes and -456 ms
        String expected2 = "-1 days -2 hours -3 minutes -0.456 seconds";
        String actual2 = DurationFormatUtils.formatDuration(duration2, format);
        Assert.assertEquals(expected2, actual2);

        // Testing format with custom padding
        long duration3 = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        String format3 = "d' days H' hours m' minutes s' seconds";
        String expected3 = "1 days 2 hours 3 minutes 0.456 seconds";
        String actual3 = DurationFormatUtils.formatDuration(duration3, format3);
        Assert.assertEquals(expected3, actual3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        String format = "d' days H' hours m' minutes s' seconds'";
        long duration = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        DurationFormatUtils.formatDuration(duration, format);
    }

    @Test
    public void testFormatDurationWords() {
        long duration = 1234567L; // 1 day, 2 hours, 3 minutes and 456 ms
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day 2 hours 3 minutes 0.456 seconds";
        String actual = DurationFormatUtils.formatDurationWords(duration, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1611987200000L; // 2021-03-15 00:00:00.000
        long endMillis = 1611987800000L; // 2021-03-15 01:00:00.000
        String expected = "P1DT";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1611987200000L; // 2021-03-15 00:00:00.000
        long endMillis = 1611987800000L; // 2021-03-15 01:00:00.000
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expected = "PT1H";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithEdgeCases() {
        // Testing for zero durations
        long startMillis1 = 1611987200000L; // 2021-03-15 00:00:00.000
        long endMillis1 = 1611987200000L; // 2021-03-15 00:00:00.000
        String expected1 = "P0DT0H";
        String actual1 = DurationFormatUtils.formatPeriod(startMillis1, endMillis1, format);
        Assert.assertEquals(expected1, actual1);

        // Testing for negative durations
        long startMillis2 = 1611987800000L; // 2021-03-15 01:00:00.000
        long endMillis2 = 1611987200000L; // 2021-03-15 00:00:00.000
        String expected2 = "P-1DT-1H";
        String actual2 = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        Assert.assertEquals(expected2, actual2);

        // Testing format with custom padding and timezone
        long startMillis3 = 1611987200000L; // 2021-03-15 00:00:00.000
        long endMillis3 = 1611987800000L; // 2021-03-15 01:00:00.000
        String format3 = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        String expected3 = "PT1H";
        String actual3 = DurationFormatUtils.formatPeriod(startMillis3, endMillis3, format3, false, timezone);
        Assert.assertEquals(expected3, actual3);
    }

    
}