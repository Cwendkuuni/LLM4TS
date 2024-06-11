import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 2 days, 2 hours, 12 minutes, 35 seconds, 670 milliseconds
        String expectedResult = "2d 2h 12m 35s 670ms";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }
}