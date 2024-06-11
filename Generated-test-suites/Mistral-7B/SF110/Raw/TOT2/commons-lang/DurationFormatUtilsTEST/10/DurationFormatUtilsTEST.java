package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:37";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);

        long negativeDurationMillis = -1234567L;
        expected = "-1:46:37";
        actual = DurationFormatUtils.formatDurationHMS(negativeDurationMillis);
        assertEquals(expected, actual);

        long longDurationMillis = Long.MAX_VALUE;
        expected = "P2147483647D";
        actual = DurationFormatUtils.formatDurationHMS(longDurationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 123456789012L;
        String expected = "-P5Y10M2D1H2M3S.456789012";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);

        long negativeDurationMillis = -123456789012L;
        expected = "P5Y10M2D1H2M3S.456789012";
        actual = DurationFormatUtils.formatDurationISO(negativeDurationMillis);
        assertEquals(expected, actual);

        long zeroDurationMillis = 0L;
        expected = "P0D";
        actual = DurationFormatUtils.formatDurationISO(zeroDurationMillis);
        assertEquals(expected, actual);

        long incorrectFormatMillis = 1234567890L;
        String incorrectFormat = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'Z";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDurationISO(durationMillis, incorrectFormat));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expected = "1 days 0 hours 1 hours 46 minutes 37 seconds";
        String actual = DurationFormatUtils.formatDuration(durationMillis, "d H H m m s s");
        assertEquals(expected, actual);

        long negativeDurationMillis = -1234567L;
        expected = "-1 days 0 hours 1 hours 46 minutes 37 seconds";
        actual = DurationFormatUtils.formatDuration(negativeDurationMillis, "d H H m m s s");
        assertEquals(expected, actual);

        long longDurationMillis = Long.MAX_VALUE;
        expected = "P2147483647D";
        actual = DurationFormatUtils.formatDuration(longDurationMillis, "P");
        assertEquals(expected, actual);

        long zeroDurationMillis = 0L;
        expected = "P0D";
        actual = DurationFormatUtils.formatDuration(zeroDurationMillis, "P");
        assertEquals(expected, actual);

        String incorrectFormat = "d h m s s";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatDuration(durationMillis, incorrectFormat));
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "1 day 1 hour 46 minutes 37 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis);
        assertEquals(expected, actual);

        long negativeDurationMillis = -1234567L;
        expected = "-1 day 1 hour 46 minutes 37 seconds";
        actual = DurationFormatUtils.formatDurationWords(negativeDurationMillis);
        assertEquals(expected, actual);

        long longDurationMillis = Long.MAX_VALUE;
        expected = "P2147483647 days";
        actual = DurationFormatUtils.formatDurationWords(longDurationMillis);
        assertEquals(expected, actual);

        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        long durationMillisWithLeadingAndTrailingZeros = 001234567L;
        String expectedWithLeadingAndTrailingZeros = "1 day 0 hours 0 hours 1 hour 0 minutes 0 minutes 46 seconds 0 seconds 0 milliseconds 0 milliseconds 0";
        String actualWithLeadingAndTrailingZeros = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingAndTrailingZeros, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedWithLeadingAndTrailingZeros, actualWithLeadingAndTrailingZeros);

        suppressLeadingZeroElements = false;
        String expectedWithoutLeadingZeroElements = "1 day 1 hour 46 minutes 37 seconds";
        actual = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingAndTrailingZeros, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedWithoutLeadingZeroElements, actual);

        suppressTrailingZeroElements = false;
        String expectedWithoutTrailingZeroElements = "1 day 1 hour 46 minutes 37 seconds 0 milliseconds";
        actual = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingAndTrailingZeros, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedWithoutTrailingZeroElements, actual);

        suppressLeadingZeroElements = true;
        String expectedWithLeadingZeroElements = "1 day 0 hours 1 hour 0 minutes 0 minutes 46 seconds 0 seconds 0 milliseconds 0 milliseconds";
        actual = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingAndTrailingZeros, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedWithLeadingZeroElements, actual);

        suppressTrailingZeroElements = true;
        String expectedWithoutTrailingZeroElements = "1 day 1 hour 46 minutes 37 seconds";
        actual = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingAndTrailingZeros, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedWithoutTrailingZeroElements, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1631026800000L;
        long endMillis = 1631113200000L;
        String expected = "P1D";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis);
        assertEquals(expected, actual);

        long startMillis2 = 1516092800000L;
        long endMillis2 = 1516179200000L;
        expected = "P7D";
        actual = DurationFormatUtils.formatPeriod(startMillis2, endMillis2);
        assertEquals(expected, actual);

        long startMillis3 = 1234567890123L;
        long endMillis3 = 1234567890124L;
        expected = "PT0S";
        actual = DurationFormatUtils.formatPeriod(startMillis3, endMillis3);
        assertEquals(expected, actual);

        String incorrectFormat = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'Z'";
        assertThrows(IllegalArgumentException.class, () -> DurationFormatUtils.formatPeriod(startMillis, endMillis, incorrectFormat));

        long startMillis4 = 1234567890123L;
        long endMillis4 = 1234567890124L;
        String format = "PT1S";
        expected = "PT1S";
        actual = DurationFormatUtils.formatPeriod(startMillis4, endMillis4, format);
        assertEquals(expected, actual);

        startMillis4 += 1000L;
        actual = DurationFormatUtils.formatPeriod(startMillis4, endMillis4, format);
        expected = "PT2S";
        assertEquals(expected, actual);

        startMillis4 += 1000L;
        actual = DurationFormatUtils.formatPeriod(startMillis4, endMillis4, format);
        expected = "PT3S";
        assertEquals(expected, actual);
    }
}