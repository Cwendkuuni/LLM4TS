import static org.junit.Assert.*;
import org.apache.commons.lang3.time.*;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "1:46:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P1Y1M1W1D1H4H46M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890123L;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        String expected = "2023-03-12 14:23:17.123";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 1234567890123L;
        String format = "yyyy-MM-dd HH:mm:ss.SSSd";
        DurationFormatUtils.formatDuration(durationMillis, format, true);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890123L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "1 year 1 month 1 week 1 day 14 hours 4 hours 46 minutes 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1Y1M1W1DT1H4H46M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        String expected = "2023-03-12 14:23:17.123 - 2023-03-12 15:09:33.456";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodInvalidFormat() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "yyyy-MM-dd HH:mm:ss.SSSd";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
    }
}