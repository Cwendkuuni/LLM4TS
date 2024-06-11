package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.670";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "PT10H4M37.67S";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d H:m:s.SSS";
        String expectedResult = "1d 10:04:37.670";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long maxLongValue = Long.MAX_VALUE;
        long minLongValue = Long.MIN_VALUE;
        String format = "d H:m:s.SSS";
        
        // Test with max long value
        long maxLongDurationMillis = maxLongValue - System.currentTimeMillis();
        String maxLongDurationResult = DurationFormatUtils.formatDuration(maxLongDurationMillis, format);
        Assert.assertEquals("P" + Integer.MAX_VALUE + "D PT" + Integer.MAX_VALUE + "H PT" + Integer.MAX_VALUE + "M PT" + (Integer.MAX_VALUE * 60) + "S.0S", maxLongDurationResult);

        // Test with min long value
        long minLongDurationMillis = minLongValue + System.currentTimeMillis();
        String minLongDurationResult = DurationFormatUtils.formatDuration(minLongDurationMillis, format);
        Assert.assertEquals("P" + Integer.MIN_VALUE + "D PT" + Integer.MIN_VALUE + "H PT" + Integer.MIN_VALUE + "M PT" + (Integer.MIN_VALUE * 60) + "S.0S", minLongDurationResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "d H:m:s.SSSs";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L;
        String format = "d H:m:s.SSS";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day 10 hours 4 minutes 37 seconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String expectedResult = "PT1S";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "y-MM-dd'T'HH:mm:ss.SSS";
        String timeZoneID = "UTC";
        String expectedResult = "2001-12-31T23:59:59.999";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, TimeZone.getTimeZone(timeZoneID));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithEdgeCases() {
        long startMillis = 1323132313231L;
        long endMillis = Long.MAX_VALUE;
        String format = "y-MM-dd'T'HH:mm:ss.SSS";
        String timeZoneID = "UTC";
        
        try {
            String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, TimeZone.getTimeZone(timeZoneID));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        startMillis = Long.MIN_VALUE;
        endMillis = 1234567890123L;
        try {
            String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, TimeZone.getTimeZone(timeZoneID));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        startMillis = 1234567890123L;
        endMillis = Long.MIN_VALUE;
        try {
            String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, TimeZone.getTimeZone(timeZoneID));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "y-MM-dd'T'HH:mm:ss.SSSs";
        String timeZoneID = "UTC";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, TimeZone.getTimeZone(timeZoneID));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDurationFormat() {
        String durationString = "PT1D PT2H PT3M PT4S.PT5";
        long durationMillis = DurationFormatUtils.parseDuration(durationString, null);
    }

    @Test
    public void testParseDuration() {
        String durationString = "PT1H";
        long durationMillis = DurationFormatUtils.parseDuration(durationString, null).getTime();
        long expectedResult = 3600000L;
        Assert.assertEquals(expectedResult, durationMillis);
    }

    @Test
    public void testParseDurationWithTimeZone() {
        String durationString = "PT1H";
        Calendar calendar = GregorianCalendar.getInstance();
        long durationMillis = DurationFormatUtils.parseDuration(durationString, calendar).getTime();
        long expectedResult = calendar.getTimeInMillis() + 3600000L;
        Assert.assertEquals(expectedResult, durationMillis);
    }

    @Test
    public void testParseDurationWithInvalidFormat() {
        String durationString = "PT1H PT1M";
        try {
            DurationFormatUtils.parseDuration(durationString, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseDurationWithNegativeDuration() {
        String durationString = "PT-1H";
        try {
            DurationFormatUtils.parseDuration(durationString, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseDurationWithInvalidTimeZone() {
        String durationString = "PT1H";
        Calendar calendar = new GregorianCalendar("foo", "foo", 1);
        try {
            DurationFormatUtils.parseDuration(durationString, calendar);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}

class Token {
    private final Object value;
    private int count;

    // ... (Keep the existing Token constructor and methods)

    Token(StringBuilder buffer) {
        this.value = buffer;
        this.count = 1;
    }
}