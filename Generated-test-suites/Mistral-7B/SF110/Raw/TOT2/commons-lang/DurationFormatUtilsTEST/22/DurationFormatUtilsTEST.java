import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 1 week, 3 days, 4 hours, 56 minutes and 7 seconds
        String expected = "1w 3d 4h 5m 7s";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assert(expected.equals(actual));
    }
}