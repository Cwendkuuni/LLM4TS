import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 1 week, 3 days, 4 hours, 56 minutes and 7 seconds
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "1w3d4h56m7s";
        assert result.equals(expected);
    }
}