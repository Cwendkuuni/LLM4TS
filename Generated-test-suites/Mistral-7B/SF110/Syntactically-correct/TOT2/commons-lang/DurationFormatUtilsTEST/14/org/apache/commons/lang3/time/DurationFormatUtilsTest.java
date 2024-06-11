import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "1:46:37.67";
        String result = formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationHMSNegative() {
        long durationMillis = -1234567L;
        String format = "H:mm:ss.SSS";
        formatDurationHMS(durationMillis, format);
    }

    @Test
    public void testFormatDurationHMSZero() {
        long durationMillis = 0L;
        String expectedResult = "0:00:00.000";
        String result = formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationHMSLarge() {
        long durationMillis = Long.MAX_VALUE;
        String format = "H:mm:ss.SSS";
        formatDurationHMS(durationMillis, format);
    }
}