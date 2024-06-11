package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 123456;
        String expected = "1:46:26.123";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 123456789;
        String expected = "P3Y4M6DT12H26M12S.123";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 123456;
        String format = "H:m:s";
        String expected = "1:46:26";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d.H:m:s.S";
        expected = "3.12:46:26.123";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        Calendar start = new GregorianCalendar(2010, 1, 1);
        Calendar end = new GregorianCalendar(2010, 1, 3);
        long startMillis = start.getTimeInMillis();
        long endMillis = end.getTimeInMillis();
        format = "y-MM-dd'T'HH:mm:ss.SSS'Z'";
        expected = "2010-01-03T12:46:26.123+00:00";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 123456;
        String format = "X";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationNegativeDuration() {
        long durationMillis = -123456;
        String format = "H:m:s";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationNullFormat() {
        long durationMillis = 123456;
        String format = null;
        try {
            DurationFormatUtils.formatDuration(durationMillis, format);
        } catch (IllegalArgumentException e) {
            assertEquals("format is marked non-null but is null", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 123456;
        String expected = "2 days 1 hour 46 minutes 26 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);

        expected = "2 days 1 hour 46 minutes";
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, result);

        expected = "2 days 1 hour 46 minutes 26 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1356998400000L; // 2013-01-01 00:00:00 UTC
        long endMillis = 1357001600000L; // 2013-01-01 08:00:00 UTC
        String format = "y-MM-dd'T'HH:mm:ss'Z'";
        String expected = "2013-01-01T08:00:00+00:00";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1356998400000L; // 2013-01-01 00:00:00 UTC
        long endMillis = 1357001600000L; // 2013-01-01 08:00:00 UTC
        String format = "y-MM-dd'T'HH:mm:ss'Z'";
        TimeZone timeZone = TimeZone.getTimeZone("Europe/London");
        String expected = "2013-01-01T07:00:00+00:00"; // GMT is UTC+00:00
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        assertEquals(expected, result);
    }
}