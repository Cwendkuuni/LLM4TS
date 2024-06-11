
import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 1 week, 3 days, 4 hours, 56 minutes and 7 seconds
        String result = formatDurationHMS(durationMillis);
        String expected = "1w3d4h56m7s";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567; // same duration as in the previous test case
        String result = formatDurationISO(durationMillis);
        String expected = "P1W3D4H56M7S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationNegativeDuration() {
        long durationMillis = -1234567; // -1 week, -3 days, -4 hours, -56 minutes and -7 seconds
        String result = formatDuration(durationMillis, ISO_EXTENDED_FORMAT_PATTERN, false);
        String expected = "-P1W3D4H56M7S";
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in the class

}