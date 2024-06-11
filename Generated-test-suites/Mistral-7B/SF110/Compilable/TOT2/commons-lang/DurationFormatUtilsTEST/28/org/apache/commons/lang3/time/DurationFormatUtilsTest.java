package org.apache.commons.lang3.time;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:23:56.700";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }
}