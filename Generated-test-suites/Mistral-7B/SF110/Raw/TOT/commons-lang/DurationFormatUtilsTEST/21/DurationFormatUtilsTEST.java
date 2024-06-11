package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:11.7";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "P1Y1M2DPT23H46M11.7S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L;
        String expected = "-1:46:11.7";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        DurationFormatUtils.formatDuration(-1, "xyz");
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        String expected = "2022-03-03 23:46:11.7";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567890L;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "2022-03-03 23:46:11.070";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithZeroValues() {
        long durationMillis = 0L;
        String format = "HH:mm:ss.SSS";
        String expected = "00:00:00.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 year and 1 month and 2 days and 23 hours and 46 minutes and 11 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L;
        long endMillis = 1337123456789L;
        String expected = "P3Y2M1W2DT12H32M11.6S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890000L;
        long endMillis = 1337123456789L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String timezoneID = "UTC";
        String expected = "2022-03-03T23:46:11.600Z";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneToString(TimeZone.getDefault()), timezoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1337123456789L;
        long endMillis = 1234567890000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String timezoneID = "UTC";
        String expected = "2022-03-03T23:46:11.600Z";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneToString(TimeZone.getDefault()), timezoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890000L;
        long endMillis = 1337123456789L;
        String format = "xyz";
        String expectedException = "Unsupported format character 'x'";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException, e.getMessage());
        }
    }

    private static String timeZoneToString(TimeZone timezone) {
        return timezone.getID();
    }
}