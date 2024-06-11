
package org.junit.tests;

import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String result = formatDurationHMS(durationMillis);
        String expected = "11 days 12 hours 21 mins 35.67 secs";
        assertEquals(expected, result);
    }

    @Test
    public void testNegativeDuration() {
        long durationMillis = -1234567;
        String result = formatDurationHMS(durationMillis);
        String expected = "-11 days -12 hours -21 mins -35.67 secs";
        assertEquals(expected, result);
    }

    @Test
    public void testZeroDuration() {
        long durationMillis = 0;
        String result = formatDurationHMS(durationMillis);
        String expected = "0 secs";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaximumLongValue() {
        long durationMillis = Long.MAX_VALUE;
        formatDurationHMS(durationMillis); // This will throw an IllegalArgumentException
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String result = formatDurationISO(durationMillis);
        String expected = "P11DPT12H31M35.67S";
        assertEquals(expected, result);
    }

    @Test
    public void testNegativeDurationISO() {
        long durationMillis = -1234567;
        String result = formatDurationISO(durationMillis);
        String expected = "P-11DPT-12H-21M-35.67S";
        assertEquals(expected, result);
    }

    @Test
    public void testZeroDurationISO() {
        long durationMillis = 0;
        String result = formatDurationISO(durationMillis);
        String expected = "PT0S";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaximumLongValueISO() {
        long durationMillis = Long.MAX_VALUE;
        formatDurationISO(durationMillis); // This will throw an IllegalArgumentException
    }
}
