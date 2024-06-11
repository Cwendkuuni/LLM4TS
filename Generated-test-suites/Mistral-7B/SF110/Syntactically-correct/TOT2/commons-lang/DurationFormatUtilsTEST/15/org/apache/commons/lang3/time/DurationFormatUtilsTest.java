import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:47:36.700";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assert (result.equals(expectedResult));
    }
}