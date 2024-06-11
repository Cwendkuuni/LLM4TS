package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 3500;
        assertEquals("00:00:03.5", DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3500;
        assertEquals("PT3.5S", DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 3500;
        assertEquals("0 days 0 hours 0 minutes 0.5 seconds", DurationFormatUtils.formatDuration(durationMillis, "d H m s.S"));
        assertEquals("0:0:0.5", DurationFormatUtils.formatDuration(durationMillis, "H:m:s.S"));
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 3500;
        assertEquals("0 days 0 hours 0 minutes 0.5 seconds", DurationFormatUtils.formatDuration(durationMillis, "d H m s.S", true));
        assertEquals("00:0:0.5", DurationFormatUtils.formatDuration(durationMillis, "H:m:s.S", true));
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 3500;
        assertEquals("3 seconds", DurationFormatUtils.formatDurationWords(durationMillis));
    }

    @Test
    public void testFormatPeriodISO() throws ParseException {
        long startMillis = 1638470400000L;
        long endMillis = 1638470460000L;
        assertEquals("PT4m30s", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);
        assertEquals("PT4m30s", DurationFormatUtils.formatPeriodISO(start, end));
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1638470400000L;
        long endMillis = 1638470460000L;
        assertEquals("4 minutes 30 seconds", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m s.S"));
        assertEquals("4 minutes 30 seconds", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m m s S"));
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);
        assertEquals("4 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "d H m m s S"));
        assertEquals("4 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "dd hh mm ss ss"));
        assertEquals("4 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "d MMM yyyy H h m m s S"));
    }

    @Test
    public void testFormatPeriodWithPadZeros() throws ParseException {
        long startMillis = 1638470400000L;
        long endMillis = 1638470460000L;
        assertEquals("0 days 0 hours 4 minutes 30 seconds", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m m s S", true));
        assertEquals("0 days 0 hours 04 minutes 30 seconds", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m m s S", true));
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);
        assertEquals("0 days 0 hours 4 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "d H m m s S", true));
        assertEquals("0 days 0 hours 04 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "d H m m s s", true));
    }

    @Test
    public void testFormatPeriodWithTimeZone() throws ParseException {
        long startMillis = 1638470400000L;
        long endMillis = 1638470460000L;
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        assertEquals("PT4m30s", DurationFormatUtils.formatPeriodISO(startMillis, endMillis, timezone));
        Calendar start = Calendar.getInstance(timezone);
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance(timezone);
        end.setTimeInMillis(endMillis);
        assertEquals("PT4m30s", DurationFormatUtils.formatPeriodISO(start, end, timezone));
    }

    @Test
    public void testFormatPeriodWithTimeZoneAndPadZeros() throws ParseException {
        long startMillis = 1638470400000L;
        long endMillis = 1638470460000L;
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        assertEquals("0 days 0 hours 4 minutes 30 seconds", DurationFormatUtils.formatPeriod(startMillis, endMillis, "d H m m s s", true, timezone));
        Calendar start = Calendar.getInstance(timezone);
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance(timezone);
        end.setTimeInMillis(endMillis);
        assertEquals("0 days 0 hours 04 minutes 30 seconds", DurationFormatUtils.formatPeriod(start, end, "d H m m s s", true, timezone));
    }

    @Test
    public void testLexx() {
        String format = "H:m:s.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        assertEquals(11, tokens.length);
        assertArrayEquals(new Object[]{"H", "m", "s", "S", "S", "S"}, Arrays.stream(tokens).map(Token::getValue).toArray());
    }

    @Test
    public void testFormat() {
        long durationMillis = 3500;
        StringBuilder buffer = new StringBuilder();
        DurationFormatUtils.format(new Token[]{}, 0, 0, 0, 0, 0, 0, durationMillis, true, buffer);
        assertEquals("0 days 0 hours 0 minutes 0.5 seconds", buffer.toString());
    }
}