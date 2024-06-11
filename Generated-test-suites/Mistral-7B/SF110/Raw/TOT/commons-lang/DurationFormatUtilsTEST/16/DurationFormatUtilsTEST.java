package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        String expectedResult = "2w 3d 4h 5m 677ms";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        String expectedResult = "P2W3D4H5M677MS";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        String expectedFormat = "H:mm:ss.SSS";
        String expectedResult = "02:05:677";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, expectedFormat);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        String expectedFormat = "H:mm:ss.SSS";
        String expectedResult = "02:05:677";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, expectedFormat, true);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        String invalidFormat = "H:mm:ss:SSS";
        DurationFormatUtils.formatDuration(durationMillis, invalidFormat);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567; // 2 weeks, 3 days, 4 hours, 5 minutes and 677 milliseconds
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "2 weeks, 3 days, 4 hours, 1 minute and 777 milliseconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1563308800000L; // 2019-09-15 00:00:00.000 UTC
        long endMillis = 1563495200000L; // 2019-09-15 23:59:59.999 UTC
        String expectedResult = "P1D";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1563308800000L; // 2019-09-15 00:00:00.000 UTC
        long endMillis = 1563495200000L; // 2019-09-15 23:59:59.999 UTC
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String expectedResult = "2019-09-15T23:59:59.999Z";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, expectedFormat);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1563308800000L; // 2019-09-15 00:00:00.000 UTC
        long endMillis = 1563495200000L; // 2019-09-15 23:59:59.999 UTC
        String invalidFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"; // missing 'Z' at the end
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1563308800000L; // 2019-09-15 00:00:00.000 UTC
        long endMillis = 1563495200000L; // 2019-09-15 23:59:59.999 UTC
        String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String expectedResult = "2019-09-15T23:59:59.999Z";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, expectedFormat, timeZone);
        assertEquals(expectedResult, actualResult);
    }
}