import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1500; // 2 minutes and 30 seconds
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:03:30";
        assert assertEquals(expected, result);
    }
}