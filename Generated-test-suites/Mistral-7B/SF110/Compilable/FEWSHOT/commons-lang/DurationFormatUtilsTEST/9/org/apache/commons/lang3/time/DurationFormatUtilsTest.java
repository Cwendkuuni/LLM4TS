package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        assertEquals("00:01:02.345", DurationFormatUtils.formatDurationHMS(102345L));
        assertEquals("01:02:03.456", DurationFormatUtils.formatDurationHMS(360203456L));
    }

    @Test
    public void testFormatDurationISO() {
        assertEquals("PT1S", DurationFormatUtils.formatDurationISO(1000L));
        assertEquals("PT2H3M45.678S", DurationFormatUtils.formatDurationISO(72345678L));
    }

    @Test
    public void testFormatDuration() {
        assertEquals("1 day, 2 hours, 3 minutes, 4 seconds, 567 milliseconds", DurationFormatUtils.formatDuration(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 4 * 1000 + 567, "d, H, m, s, S"));
        assertEquals("1 day, 2 hours, 3 minutes, 4 seconds, 567 ms", DurationFormatUtils.formatDuration(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 4 * 1000 + 567, "d, H, m, s, S", true));
    }

    @Test
    public void testFormatDurationWords() {
        assertEquals("1 day, 2 hours, 3 minutes, 4 seconds, 567 ms", DurationFormatUtils.formatDurationWords(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 4 * 1000 + 567, false, false));
        assertEquals("1 day, 2 hours, 3 minutes, 4 seconds", DurationFormatUtils.formatDurationWords(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 4 * 1000, false, false));
        assertEquals("1 day, 2 hours, 3 minutes", DurationFormatUtils.formatDurationWords(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000, false, false));
        assertEquals("1 day, 2 hours", DurationFormatUtils.formatDurationWords(61 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000, false, false));
        assertEquals("1 day", DurationFormatUtils.formatDurationWords(61 * 24 * 60 * 60 * 1000, false, false));
        assertEquals("", DurationFormatUtils.formatDurationWords(0, false, false));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1653341200000L; // Thu Jan 27 00:00:00 CET 2022
        long endMillis = 1653343800000L;   // Thu Jan 27 01:20:00 CET 2022
        assertEquals("PT1H12M", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1653341200000L; // Thu Jan 27 00:00:00 CET 2022
        long endMillis = 1653343800000L;   // Thu Jan 27 01:20:00 CET 2022
        assertEquals("1 hour, 12 minutes", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d, H, m, s, S", true, TimeZone.getDefault()));
    }

    @Test
    public void testFormatPeriodWithGregorianCalendar() {
        Calendar start = new GregorianCalendar(2022, 0, 27);
        Calendar end = new GregorianCalendar(2022, 0, 27);
        end.add(Calendar.HOUR, 1);
        end.add(Calendar.MINUTE, 12);
        assertEquals("1 hour, 12 minutes", DurationFormatUtils.formatPeriod(start.getTimeInMillis(), end.getTimeInMillis(), "d, H, m, s, S", true, TimeZone.getDefault()));
    }
}