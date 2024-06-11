import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.*;

public class DurationFormatUtilsTest {
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 360000L; // 1 hour
        String expected = "01:00:00.000";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400000L; // 1 day
        String expected = "P1D";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = -123456789L; // negative value, 2 hours, 33 minutes, and 56.789 seconds
        String format = "H:mm:ss.SSS";
        String expected = "-02:33:56.789";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
        expected = "-2 days -2 hours 33 minutes 56 seconds";
        result = DurationFormatUtils.formatDuration(durationMillis, format, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 123456789L; // positive value, 1 day, 2 hours, 33 minutes, and 56.789 seconds
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day 2 hours 33 minutes 56.789 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = false;
        expected = "1 day 0 hours 2 hours 0 minutes 33 minutes 56.789 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertNotEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L; // 2015-01-01 00:02:10.987
        long endMillis = 1234567900000L; // 2015-01-01 00:02:20.013
        String expected = "P1D";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890000L; // 2015-01-01 00:02:10.987
        long endMillis = 1234567900000L; // 2015-01-01 00:02:20.013
        String format = "P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expected = "P1D";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
        assertEquals(expected, result);

        format = "d 'days' H 'hours' m 'minutes' s 'seconds'";
        expected = "1 day 0 hours 0 hours 1 hour 1 hour 10 minutes 10 minutes 10 seconds";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertEquals(expected, result);
    }
}

