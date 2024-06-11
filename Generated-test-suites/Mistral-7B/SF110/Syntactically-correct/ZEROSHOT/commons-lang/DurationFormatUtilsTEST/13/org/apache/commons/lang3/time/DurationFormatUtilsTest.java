package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:17.7";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 24 * 60 * 60 * 1000L;
        String expected = "PT24H";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expected = "1:46:17.7";
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithDifferentFormats() {
        long durationMillis = 1234567L;
        String formatHMS = "H:mm:ss.SSS";
        String formatISO = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String formatCustom = "d day(s) h hour(s) m minute(s) s second(s)";

        String expectedHMS = "1:46:17.7";
        String actualHMS = DurationFormatUtils.formatDuration(durationMillis, formatHMS, true);
        Assert.assertEquals(expectedHMS, actualHMS);

        String expectedISO = "PT1H46M17.7S";
        String actualISO = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedISO, actualISO);

        String expectedCustom = "1 day 1 hour 46 minutes 17 seconds 17 seconds";
        String actualCustom = DurationFormatUtils.formatDuration(durationMillis, formatCustom, true, null);
        Assert.assertEquals(expectedCustom, actualCustom);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day 1 hour 46 minutes 17 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L);
        long endMillis = System.currentTimeMillis();
        String expected = "PT2D";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L);
        long endMillis = System.currentTimeMillis();
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        boolean padWithZeros = true;
        String timeZoneId = "UTC";
        String expected = "PT2D";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, TimeZone.getTimeZone(timeZoneId));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithDifferentFormats() {
        long startMillis = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L);
        long endMillis = System.currentTimeMillis();
        String formatHMS = "H:mm:ss.SSS";
        String formatISO = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String formatCustom = "d day(s) h hour(s) m minute(s) s second(s)";

        String expectedHMS = "2 days 0 hours 0 minutes 24 hours 0 minutes 0 seconds 0 seconds";
        String actualHMS = DurationFormatUtils.formatPeriod(startMillis, endMillis, formatHMS, true, null);
        Assert.assertEquals(expectedHMS, actualHMS);

        String expectedISO = "PT2D";
        String actualISO = DurationFormatUtils.formatPeriod(startMillis, endMillis, formatISO, true, null);
        Assert.assertEquals(expectedISO, actualISO);

        String expectedCustom = "2 days 24 hours";
        String actualCustom = DurationFormatUtils.formatPeriod(startMillis, endMillis, formatCustom, true, null);
        Assert.assertEquals(expectedCustom, actualCustom);
    }
}

class Token {
    // Empty.
}