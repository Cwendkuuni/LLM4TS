package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("02:07:11.700", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P2DT2H7M11.7S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDuration(durationMillis, "d h m s.SSS");
        assertEquals("2d 2h 7m 11.7s", result);

        long durationMillis2 = 123000;
        result = DurationFormatUtils.formatDuration(durationMillis2, "h:mm:ss", false);
        assertEquals("00:02:00", result);
    }

    @Test
    public void testFormatDurationWithNegativeValues() {
        long durationMillis = -1234567;
        String result = DurationFormatUtils.formatDuration(durationMillis, "d h m s.SSS");
        assertEquals("-3d -2h -7m -11.7s", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDuration(durationMillis, "x");
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals("2 days 2 hours 7 minutes 11 seconds", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P1DT1H59M", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d h m s.SSS");
        assertEquals("1d 1h 59m 59.0s", result);

        long startMillis2 = 1420070400000L;
        long endMillis2 = 1420070401000L;
        result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, "h:mm:ss");
        assertEquals("00:01:00", result);
    }

    @Test
    public void testFormatPeriodWithNegativeValues() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070359000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d h m s.SSS");
        assertEquals("-1d -1h -59m -59.0s", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070459000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "x");
    }
}