package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("02:04:27.67", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("PT2H4M27.67S", result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("2 days 4 hours 27 seconds", result);

        format = "d' days H:mm:ss";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("2d 04:27:27", result);

        format = "T'ms'";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("1234567", result);
    }

    @Test
    public void testFormatDurationWithTokens() {
        long durationMillis = 1234567L;

        Token[] tokens = DurationFormatUtils.lexx("d' days H:mm:ss.SSS'");
        String result = DurationFormatUtils.format(tokens, 2, 4, 27, 4, 24, 0, 1234567, true);
        assertEquals("2 days 4 hours 27 seconds.123", result);

        tokens = DurationFormatUtils.lexx("T'ms'");
        result = DurationFormatUtils.format(tokens, 0, 0, 0, 0, 0, 0, 1234567, true);
        assertEquals("1234567", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1499486400000L; // Thu, 24 Nov 2017 00:00:00 GMT
        long endMillis = 1514768000000L; // Mon, 19 Mar 2018 00:00:00 GMT
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("PT138D", result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1499486400000L; // Thu, 24 Nov 2017 00:00:00 GMT
        long endMillis = 1514768000000L; // Mon, 19 Mar 2018 00:00:00 GMT
        String format = "d' days 'H' hours 'm' minutes";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("138 days 17 hours 0 minutes", result);

        format = "d' days H:mm";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("138d 17:00", result);
    }
}