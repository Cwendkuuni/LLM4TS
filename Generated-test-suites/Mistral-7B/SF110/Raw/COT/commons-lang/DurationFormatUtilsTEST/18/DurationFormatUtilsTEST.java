package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Token;
import java.util.concurrent.TimeUnit;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:37.67";
        assertEquals(expected, DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "P1DT1H46M37.67S";
        assertEquals(expected, DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expected = "1:46:37.67";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS"));
        expected = "1:46:37";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss"));
        expected = "1d 1h 46m 37s 670ms";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "d H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long durationMillis = Long.MIN_VALUE;
        String expected = "-9223372036854775808s";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "s"));
        expected = "-P9223372036854775808DT0H0M0.0S";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN));

        durationMillis = Long.MAX_VALUE;
        expected = "P9223372036854775807s";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, "s"));
        expected = "P9223372036854775808DT999999H59M59M.999999999S";
        assertEquals(expected, DurationFormatUtils.formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        DurationFormatUtils.formatDuration(0, "x");
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "1 day 1 hour 46 minutes 37 seconds 670 milliseconds";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, false, false));
        expected = "1 day, 1 hour, 46 minutes, 37 seconds, 670 milliseconds";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, true, false));
        expected = "1 day 1 hour 46 minutes 37 seconds";
        assertEquals(expected, DurationFormatUtils.formatDurationWords(durationMillis, false, true));
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1611892752000L;
        long endMillis = 1611893052000L;
        String expected = "P23H15M";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false));
        expected = "PT23H15M";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, "P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false));

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(new Date(startMillis));
        end.setTime(new Date(endMillis));

        int milliseconds = end.get(Calendar.MILLISECOND) - start.get(Calendar.MILLISECOND);
        int seconds = end.get(Calendar.SECOND) - start.get(Calendar.SECOND);
        int minutes = end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE);
        int hours = end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY);
        int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
        int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);

        expected = "P" + (years > 0 ? years + "Y" : "") + (months > 0 ? months + "M" : "") + (days > 0 ? days + "D" : "")
                  + (hours > 0 ? hours + "H" : "") + (minutes > 0 ? minutes + "M" : "")
                  + (seconds > 0 ? seconds + "S" : "") + (milliseconds > 0 ? milliseconds + "ms" : "");
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, true));
    }

    @Test
    public void testFormatPeriodWithEdgeCases() throws ParseException {
        long startMillis = Long.MIN_VALUE;
        long endMillis = Long.MIN_VALUE - 1000;
        String expected = "P-9223372036854775809TD-23H-59M-59S.999";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false));

        startMillis = Long.MAX_VALUE;
        endMillis = Long.MAX_VALUE + 1000;
        expected = "P9223372036854775808TD1H1M1S.001";
        assertEquals(expected, DurationFormatUtils.formatPeriod(startMillis, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodInvalidStartDate() throws ParseException {
        long endMillis = 1611893052000L;
        DurationFormatUtils.formatPeriod(Long.MIN_VALUE, endMillis, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodInvalidEndDate() throws ParseException {
        long startMillis = 1611892752000L;
        DurationFormatUtils.formatPeriod(1611893052000L, Long.MAX_VALUE, ISO_EXTENDED_FORMAT_PATTERN, false);
    }

    @Test
    public void testDurationConversion() {
        long durationMillis = TimeUnit.Seconds.toMillis(1);
        long expected = 1000;
        assertEquals(expected, durationMillis);
        durationMillis = TimeUnit.NANOSECONDS.toMillis(1_000_000_000L);
        expected = 1000_000_000;
        assertEquals(expected, durationMillis);
        durationMillis = TimeUnit.HOURS.toMillis(5);
        expected = 18_000_000;
        assertEquals(expected, durationMillis);
        durationMillis = TimeUnit.DAYS.toMillis(3);
        expected = 86_400_000 * 3;
        assertEquals(expected, durationMillis);
    }
}

class Token {
    // Same as in the original class
}