package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:13.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 234567890L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P5Y10M1W3D18H22M3S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 34567890L;
        String format = "d HH:mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "7 d 04:54:10";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadZeroes() {
        long durationMillis = 4567890L;
        String format = "d HH:mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "07 d 04:54:10";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 567890L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "1 week, 1 day, 7 hours, 4 minutes, 3 seconds";
        assertEquals(expected, result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        expected = "1 week, 1 day, 7 hours, 4 minutes, 3 seconds";
        assertEquals(expected, result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        expected = "1 week 1 day 7 hours 4 minutes 3 seconds";
        assertEquals(expected, result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        expected = "1 week 1 day 7 hours 4 minutes 3 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 2345678901234L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P2Y10M11dPT23H22M3S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 3456789012345L;
        long endMillis = 4567890123456L;
        String format = "'P'y yyyy 'Y' M 'M' M 'd' d 'T' H 'H' h 'm' m 's' s";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "P2Y11M6D PT21H15M35S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadZeroes() {
        long startMillis = 5678901234567L;
        long endMillis = 6789012345678L;
        String format = "'P'y yyyy 'Y' M 'M' M 'd' d 'T' H 'H' h 'm' m 's' s";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "P2Y12M6D PT21H15M35S";
        assertEquals(expected, result);
    }
}