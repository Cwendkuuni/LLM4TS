package org.apache.commons.lang3.time;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {
    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }
}