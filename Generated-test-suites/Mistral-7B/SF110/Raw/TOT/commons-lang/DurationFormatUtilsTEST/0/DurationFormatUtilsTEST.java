package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:17.700";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        String expected = "P1D";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "H:mm:ss.SSSx"; // Invalid 'x' character
        try {
            DurationFormatUtils.formatDuration(durationMillis, format);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L;
        String format = "H:mm:ss.SSS";
        String expected = "-1:46:17.700";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithZeroDuration() {
        long durationMillis = 0;
        String format = "H:mm:ss.SSS";
        String expected = "0:0:0.000";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "22 minutes, 26 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        long endMillis = 1516044400000L; // 2017-10-01 01:24:00 UTC
        String expected = "P1H24M24S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        long endMillis = 1516044400000L; // 2017-10-01 01:24:00 UTC
        String format = "P1H24M24Sx"; // Invalid 'x' character
        try {
            DurationFormatUtils.formatPeriodISO(startMillis, endMillis, format);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodWithDifferentTimezones() throws ParseException {
        long startMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        long endMillis = 1516127600000L; // 2017-10-02 18:00:00 PST (Pacific Standard Time)
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone pstTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        String expected = "P1DT1H16H0M0S";
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, utcTimeZone);
        Assert.assertEquals(expected, actual);
        actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, pstTimeZone);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        long endMillis = 1516038800000L; // 2017-10-01 23:24:00 UTC (1 hour before the start)
        String format = "P1H24M24S";
        String expected = "P-1H";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithZeroDuration() {
        long startMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        long endMillis = 1516041200000L; // 2017-10-01 00:00:00 UTC
        String format = "P1H24M24S";
        String expected = "P0D0H0M0S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        Assert.assertEquals(expected, actual);
    }
}