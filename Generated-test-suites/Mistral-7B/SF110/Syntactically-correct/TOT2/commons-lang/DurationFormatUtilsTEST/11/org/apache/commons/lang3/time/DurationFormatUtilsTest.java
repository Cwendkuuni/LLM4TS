import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    // Test cases for formatDurationHMS method
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "1:46:37.7";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationHMS_NegativeDuration() {
        long durationMillis = -1234567L;
        DurationFormatUtils.formatDurationHMS(durationMillis);
    }

    // Test cases for formatDurationISO method
    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1D1H46M37.7S";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationISO_NegativeDuration() {
        long durationMillis = -1234567L;
        DurationFormatUtils.formatDurationISO(durationMillis);
    }

    // Test cases for formatDuration method
    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "dd-HH:mm:ss.SSS";
        String expectedResult = "1-01:46:37.700";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDuration_PadWithZeros() {
        long durationMillis = 1234567L;
        String format = "dd-HH:mm:ss.SSS";
        String expectedResult = "01-01:01:46:37.070";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration_InvalidFormat() {
        long durationMillis = 1234567L;
        String format = "ddH:mm:ss.SSS";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration_NullFormat() {
        long durationMillis = 1234567L;
        String format = null;
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    // Test cases for formatDurationWords method
    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expectedResult = "1 day 1 hour 46 minutes 37 seconds 707 milliseconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWords_SuppressLeadingZeroElements() {
        long durationMillis = 1234567L;
        String expectedResult = "1 day 1 hour 46 minutes 37 seconds 707 milliseconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWords_SuppressTrailingZeroElements() {
        long durationMillis = 1234567L;
        String expectedResult = "1 day 1 hour 46 minutes 37 seconds 700 milliseconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expectedResult, actualResult);
    }

    // Test cases for formatPeriodISO method
    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String expectedResult = "P1DT1H9M";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO_NegativeDuration() {
        long startMillis = 1234568490L;
        long endMillis = 1234567890L;
        String expectedResult = "P-1DT-1H-9M";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }

    // Test cases for formatPeriod method
    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String format = "dd-MM-yyyy";
        String expectedResult = "01-12-2022";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod_PadWithZeros() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String format = "dd-MM-yyyy";
        String expectedResult = "01-12-2022";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod_InvalidFormat() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String format = "ddH:mm:ss.SSS";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertNull(actualResult);
    }

    @Test
    public void testFormatPeriod_NullFormat() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, null);
        assertNull(actualResult);
    }

    @Test
    public void testFormatPeriod_TimeZone() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String format = "dd-MM-yyyy HH:mm:ss z";
        String timezone = "America/New_York";
        String expectedResult = "01-12-2022 13:09:50 EDT";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, String.valueOf(TimeZone.getTimeZone(timezone)));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod_NullTimeZone() {
        long startMillis = 1234567890L;
        long endMillis = 1234568490L;
        String format = "dd-MM-yyyy HH:mm:ss z";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertNull(actualResult);
    }
}