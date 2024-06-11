package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("02:01:17.67", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P2DT1H1M17.67S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "dd' days 'hh:mm:ss.SSS");
        assertEquals("2 days 1 hour 1 minute 17.67 seconds", result);

        result = DurationFormatUtils.formatDuration(durationMillis, "dd' days hh:mm:ss.SSS", false);
        assertEquals("2d1h1m17.67s", result);
    }

    @Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "dd' days 'hh:mm:ss.SSS", true);
        assertEquals("02 days 01 hours 01 minutes 17.67 seconds", result);

        result = DurationFormatUtils.formatDuration(durationMillis, "dd'd hh:mm:ss.SSS", true);
        assertEquals("02d01h01m17.67s", result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals("2 days, 1 hour, 1 minute and 17 seconds", result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals("2 days, 1 hour, 1 minute, and 17 seconds", result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals("2 days, 1 hour, 1 minute, and 17 seconds", result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals("2 days, 1 hour, 1 minute, 17 seconds", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P14Y1M2M3DT3H26M12.877S", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "dd' days 'hh:mm:ss.SSS");
        assertEquals("14 days 1 month 2 months 3 days 3 hours 26 minutes 12.877 seconds", result);

        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "dd'd hh:mm:ss.SSS", false);
        assertEquals("14d1m2m3d3h26m12.877s", result);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, timeZone);
        assertEquals("PT14Y1M2M3DT3H26M12.877S", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        String format = "dd' days hh:mm:ss.SSSab";
        DurationFormatUtils.formatDuration(1234567L, format, true);
    }
}