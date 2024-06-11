import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1Y3M10.67D";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expectedFormat = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, expectedFormat);
        String expectedResult = "1y3m10d10h4m37s.67s";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long maxLongValue = Long.MAX_VALUE;
        long minimumLongValue = Long.MIN_VALUE;
        long zeroDuration = 0L;
        
        String result = DurationFormatUtils.formatDuration(minimumLongValue, "HH:mm:ss.SSS");
        assertEquals("Invalid duration value", "", result);
        
        result = DurationFormatUtils.formatDuration(zeroDuration, "HH:mm:ss.SSS");
        assertEquals("Zero duration should return empty string", "", result);
        
        result = DurationFormatUtils.formatDuration(maxLongValue, "HH:mm:ss.SSS");
        assertEquals("Max long value should return P1Ytd-1H-1M-1S", "P1Ytd-1H-1M-1S", result);
        
        result = DurationFormatUtils.formatDuration(minimumLongValue, ISO_EXTENDED_FORMAT_PATTERN);
        assertEquals("Invalid duration value", "", result);
        
        result = DurationFormatUtils.formatDuration(zeroDuration, ISO_EXTENDED_FORMAT_PATTERN);
        assertEquals("Zero duration should return P0D", "P0D", result);
        
        result = DurationFormatUtils.formatDuration(maxLongValue, ISO_EXTENDED_FORMAT_PATTERN);
        assertEquals("Max long value should return P1Ytd-1PT1S", "P1Ytd-1PT1S", result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "1 year, 3 months, 10 days, 10 hours, 4 minutes, 37 seconds.67 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890L;
        long endMillis = 1234567901L;
        String expectedResult = "P1D";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890L;
        long endMillis = 1234567901L;
        String expectedFormat = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, expectedFormat);
        String expectedResult = "1d0h0m0s.0s";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithEdgeCases() {
        long startMillis = 0L;
        long endMillis = Long.MAX_VALUE;
        String expectedFormat = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, expectedFormat);
        String expectedResult = "P1Ytd-1PT1S";
        assertEquals(expectedResult, result);
        
        startMillis = 1234567890L;
        endMillis = 1234567890L;
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, expectedFormat);
        expectedResult = "P0D";
        assertEquals(expectedResult, result);
    }
}