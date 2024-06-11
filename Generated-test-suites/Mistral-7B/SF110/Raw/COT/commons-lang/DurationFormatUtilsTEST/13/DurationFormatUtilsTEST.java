package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DurationFormatUtilsTest {

    

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = TimeUnit.SECONDS.toMillis(3);
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "0:03";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 5 * 60 * 60 * 1000;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT3H";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 2 * 24 * 60 * 60 * 1000;
        String format = "dd'day'HH'hour'mm'minute'ss'second";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "2day24hour";
        assertEquals(expected, result);

        format = "d' days H' hours m' minutes s' seconds S' milliseconds";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        expected = "2 days 24 hours 0 minutes 0 seconds 0 milliseconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long durationMillis1 = Long.MIN_VALUE;
        String format = "H:mm:ss.SSS";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(durationMillis1, format));

        long durationMillis2 = 0;
        long durationMillis3 = 1;
        format = "d' day 'H' hour 'm' minute 's' second 'S' millisecond";
        String result = DurationFormatUtils.formatDuration(durationMillis3 - durationMillis2, format);
        String expected = "--1 millisecond";
        assertEquals(expected, result);

        format = "d' day H' hour m' minute s' second S' millisecond";
        result = DurationFormatUtils.formatDuration(durationMillis3 - durationMillis2, format);
        expected = "0 day 0 hour 0 minute 0 second 1 millisecond";
        assertEquals(expected, result);

        format = "d' day H:mm:ss.SSS";
        result = DurationFormatUtils.formatDuration(durationMillis3 - durationMillis2, format);
        expected = "0 day 0:00:01.000";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 2 * 24 * 60 * 60 * 1000;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        String expected = "2 days 24 hours";
        assertEquals(expected, result);

        durationMillis = -durationMillis;
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        expected = "-2 days -24 hours";
        assertEquals(expected, result);

        durationMillis = 0;
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        expected = "";
        assertEquals(expected, result);

        durationMillis = 1000;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        expected = "1 millisecond";
        assertEquals(expected, result);

        durationMillis = 1000 * 60;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        expected = "1 minute";
        assertEquals(expected, result);

        durationMillis = 1000 * 60 * 60;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        expected = "1 hour";
        assertEquals(expected, result);

        durationMillis = 1000 * 60 * 60 * 24;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        expected = "1 day";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
        long endMillis = System.currentTimeMillis();
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT2D";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
        long endMillis = System.currentTimeMillis();
        String format = "dd'day' HH'hour' mm'minute' ss'second";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "2day";
        assertEquals(expected, result);

        format = "dd'day' HH:mm:ss";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        expected = "2 days 0 hours 0 minutes 0 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithEdgeCases() {
        long startMillis1 = Long.MIN_VALUE;
        long endMillis1 = 0;
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(startMillis1, endMillis1, ""));

        long startMillis2 = 0;
        long endMillis2 = Long.MIN_VALUE;
        String format = "dd'day' HH'hour' mm'minute' ss'second";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format));

        startMillis2 = System.currentTimeMillis();
        endMillis2 = startMillis2 + 1;
        format = "dd'day' HH'hour' mm'minute' ss'second";
        String result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        String expected = "0 day 0 hour 0 minute 0 second";
        assertEquals(expected, result);

        format = "dd'day' HH:mm:ss";
        result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        expected = "0 days 0 hours 0 minutes 0 seconds";
        assertEquals(expected, result);

        startMillis2 = System.currentTimeMillis();
        endMillis2 = startMillis2 + 1000;
        format = "dd'day' HH:mm:ss.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        expected = "0 days 0 hours 0 minutes 1 second";
        assertEquals(expected, result);

        startMillis2 = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
        endMillis2 = System.currentTimeMillis();
        format = "dd'day' HH:mm:ss.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        expected = "2 days 0 hours 0 minutes 0 seconds";
        assertEquals(expected, result);

        startMillis2 = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000;
        endMillis2 = System.currentTimeMillis() - 1000;
        format = "dd'day' HH:mm:ss.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis2, endMillis2, format);
        expected = "2 days 0 hours 0 minutes -1 second";
        assertEquals(expected, result);
    }

    
}

class Token {
    // Same as the original Token class
}