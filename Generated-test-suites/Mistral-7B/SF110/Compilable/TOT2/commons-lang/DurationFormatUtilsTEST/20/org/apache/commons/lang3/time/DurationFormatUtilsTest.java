package org.apache.commons.lang3.time;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567; // 1 hour, 42 minutes, 11 seconds, 567 milliseconds
        String expected = "1:42:11.567";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }
}