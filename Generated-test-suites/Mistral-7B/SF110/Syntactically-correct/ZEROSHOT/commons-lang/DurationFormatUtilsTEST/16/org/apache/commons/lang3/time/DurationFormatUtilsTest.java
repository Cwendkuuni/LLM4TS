package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Test cases for the {@link DurationFormatUtils} class.
 */
public class DurationFormatUtilsTest {

    /**
     * Test method for {@link DurationFormatUtils#formatDurationHMS(long)}.
     */
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 123456L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:04:26.060";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatDurationISO(long)}.
     */
    @Test
    public void testFormatDurationISO() {
        long durationMillis = 123456789L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P1Y0M1DT2H4M26.06S";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatDuration(long, String)}.
     */
    @Test
    public void testFormatDuration() {
        long durationMillis = 123456789L;
        String format = "mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "20:26";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatDuration(long, String, boolean)}.
     */
    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 123456789L;
        String format = "mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "02:06:26.000";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatDurationWords(long, boolean, boolean)}.
     */
    @Test
    public void testFormatDurationWords() {
        long durationMillis = 123456789L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "1 year, 1 month, 2 days, 2 hours, 6 minutes, 26 seconds";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatPeriodISO(long, long)}.
     */
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1496187200000L; // 2017-01-01 00:00:00 UTC
        long endMillis = 1496273600000L; // 2017-01-02 00:00:00 UTC
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1DT";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatPeriod(long, long, String)}.
     */
    @Test
    public void testFormatPeriod() {
        long startMillis = 1496187200000L; // 2017-01-01 00:00:00 UTC
        long endMillis = 1496273600000L; // 2017-01-02 00:00:00 UTC
        String format = "MMM d, yyyy";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "Jan 2, 2017";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link DurationFormatUtils#formatPeriod(long, long, String, boolean, TimeZone)}.
     */
    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1496187200000L; // 2017-01-01 00:00:00 UTC
        long endMillis = 1496273600000L; // 2017-01-02 00:00:00 UTC
        String format = "h:mm a, MMM d, yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        String expected = "1:00 am, Jan 2, 2017";
        assertEquals(expected, result);
    }

}

class Token {
    private final Object value;
    private int count;

    Token(final Object value) {
        this.value = value;
        this.count = 1;
    }

    Token(final Object value, final int count) {
        this.value = value;
        this.count = count;
    }
}