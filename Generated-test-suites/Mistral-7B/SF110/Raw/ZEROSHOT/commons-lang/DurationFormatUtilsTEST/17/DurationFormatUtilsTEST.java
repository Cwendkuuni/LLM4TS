package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "1:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "-01:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithDifferentFormats() {
        long durationMillis = 1234567L;

        String result1 = DurationFormatUtils.formatDuration("H:mm:ss.SSS", durationMillis);
        String expected1 = "01:07:17.67";
        assertEquals(expected1, result1);

        String result2 = DurationFormatUtils.formatDuration("mm:ss.SSS", durationMillis);
        String expected2 = "07:17.67";
        assertEquals(expected2, result2);

        String result3 = DurationFormatUtils.formatDuration("d' day(s)' h m:ss.SSS", durationMillis);
        String expected3 = "1 day 1 hour 7 minutes 17.67 seconds";
        assertEquals(expected3, result3);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L;
        String result = DurationFormatUtils.formatDuration("mm:ss.SSS", durationMillis);
        String expected = "-07:17.67";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithDifferentFormats() throws ParseException {
        long startMillis = 1461433252000L; // 17-Mar-2016 08:30:52 UTC
        long endMillis = 1461433282000L; // 17-Mar-2016 08:31:22 UTC

        String result1 = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected1 = "-PT01M30S";
        assertEquals(expected1, result1);

        String result2 = DurationFormatUtils.formatPeriod("d HH:mm:ss", startMillis, endMillis);
        String expected2 = "0 days 01:30:22";
        assertEquals(expected2, result2);

        String result3 = DurationFormatUtils.formatPeriod("yyyy-MM-dd'T'HH:mm:ss.SSSZ", startMillis, endMillis, null, TimeZone.getDefault());
        String expected3 = "2016-03-17T08:30:52.000Z-2016-03-17T08:31:22.000Z";
        assertEquals(expected3, result3);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() throws ParseException {
        long startMillis = 1461433252000L; // 17-Mar-2016 08:30:52 UTC
        long endMillis = 1461433222000L; // 17-Mar-2016 08:30:22 UTC

        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT20M10S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;

        String result1 = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected1 = "1 day 1 hour 7 minutes 17.67 seconds";
        assertEquals(expected1, result1);

        String result2 = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        String expected2 = "1d 1h 7m 17.67s";
        assertEquals(expected2, result2);

        String result3 = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        String expected3 = "1 day 0 hour(s) 0 minute(s) 7 second(s) 17.67 millisecond(s)";
        assertEquals(expected3, result3);
    }
}

class Token {
    // Empty for test purpose
}