package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("00:01:02.345", DurationFormatUtils.formatDurationHMS(102345L));
        assertEquals("01:02:03", DurationFormatUtils.formatDurationHMS(3602033L));
        assertEquals("1:2:3", DurationFormatUtils.formatDurationHMS(3602033001L));
        assertEquals("2 hours 3 mins 45 secs 123 ms", DurationFormatUtils.formatDurationHMS(3602033123L));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("PT1S", DurationFormatUtils.formatDurationISO(1000L));
        assertEquals("PT2H3M45S", DurationFormatUtils.formatDurationISO(72045000L));
        assertEquals("PT3H2M", DurationFormatUtils.formatDurationISO(113600000L));
        assertEquals("PT1H2M3S.123MS", DurationFormatUtils.formatDurationISO(3663123L));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("1 day, 2 hours, 3 mins, 4 secs, 123 ms", DurationFormatUtils.formatDuration(102345123L, "d, H, m, s, S"));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs", DurationFormatUtils.formatDuration(102345000L, "d, H, m, s"));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs, 123 ms", DurationFormatUtils.formatDuration(102345123001L, "d, H:mm:ss.SSS"));
    }

    @Test
    public void testFormatDurationWithPad() {
        assertEquals("1 day, 02 hours, 03 mins, 04 secs, 123 ms", DurationFormatUtils.formatDuration(102345123L, "d, H:mm:ss.SSS", true));
        assertEquals("1 day, 02:03:04, 123 ms", DurationFormatUtils.formatDuration(102345123L, "d, H:mm:ss.SSS", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day, 2 hours, 3 mins, 4 secs, 123 ms", DurationFormatUtils.formatDurationWords(102345123L, false, false));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs", DurationFormatUtils.formatDurationWords(102345000L, true, false));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs, 123 ms", DurationFormatUtils.formatDurationWords(102345123001L, true, false));
        assertEquals("1 day, 2 hours, 3 mins", DurationFormatUtils.formatDurationWords(102345000L, true, true));
    }

    @Test
    public void testFormatPeriodISO() {
        assertEquals("PT1H2M3S", DurationFormatUtils.formatPeriodISO(113602033L));
        assertEquals("PT1D2H3M45S", DurationFormatUtils.formatPeriodISO(864027245L));
        assertEquals("PT1D", DurationFormatUtils.formatPeriodISO(86400000L));
    }

    @Test
    public void testFormatPeriod() {
        assertEquals("1 hour, 2 mins, 3 secs", DurationFormatUtils.formatPeriod(86402033L, "H:mm:ss"));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs", DurationFormatUtils.formatPeriod(864027245L, "dd HH:mm:ss"));
        assertEquals("1 day, 2 hours, 3 mins, 4 secs", DurationFormatUtils.formatPeriod(864027245000L, "dd HH:mm:ss.SSS"));
    }

    @Test
    public void testFormatPeriodWithPad() {
        assertEquals("1 hour, 02 mins, 03 secs", DurationFormatUtils.formatPeriod(86402033L, "H:mm:ss", true));
        assertEquals("1 hour, 02:03:04", DurationFormatUtils.formatPeriod(86402033000L, "H:mm:ss", true));
        assertEquals("1 day, 02 hours, 03 mins, 04 secs", DurationFormatUtils.formatPeriod(864027245L, "dd HH:mm:ss", true));
        assertEquals("1 day, 02:03:04:123 ms", DurationFormatUtils.formatPeriod(864027245123L, "dd HH:mm:ss.SSS", true));
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        assertNull(DurationFormatUtils.formatPeriodISO(0L));
        assertNull(DurationFormatUtils.formatPeriod("", 0L, "", false, null));
    }
}