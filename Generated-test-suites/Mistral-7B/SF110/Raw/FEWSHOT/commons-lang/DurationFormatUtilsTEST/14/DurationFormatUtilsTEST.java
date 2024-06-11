package org.apache.commons.lang3; // assuming the test class is in the same package as the DurationFormatUtils class

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import java.util.ArrayList;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "01:46:37.67";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expectedResult = "P1Y1M1T2H36M37.67S";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String expectedResult = "1 year 1 month 2 days 3 hours 36 minutes 37 seconds";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "d' days H:mm:ss";
        expectedResult = "1y1m2d3h36m37s";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "d' days H:mm:ss.SSS";
        expectedResult = "1y1m2d3h36m37.67s";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 year 1 month 2 days 3 hours 36 minutes 37 seconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, actualResult);

        suppressLeadingZeroElements = false;
        suppressTrailingZeroElements = false;
        expectedResult = "1 year 1 month 2 days 3 hours 36 minutes 37 seconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, actualResult);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = false;
        expectedResult = "1 year 1 month 2 days 3 hours 36 minutes 37 seconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertTrue(actualResult.startsWith(" 1 year"));
        assertTrue(actualResult.endsWith(" 37 seconds"));

        suppressLeadingZeroElements = false;
        suppressTrailingZeroElements = true;
        expectedResult = "1 year 1 month 2 days 3 hours 36 minutes 37 seconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertTrue(actualResult.endsWith(" 36 minutes 37 seconds"));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String expectedResult = "P1Y1M1T1S";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "P'y'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expectedResult = "PT1Y1M1DT1H0M1S";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "P'y'Y'M'M'd'DT'H'H':'m'M':'s'S'";
        expectedResult = "PT1Y1M1DT1H0M1S";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "P'y'Y'M'M'd'DT'H'H':'m'M':'s'S'";
        expectedResult = "PT1Y1M1DT1H0M1.0S";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithTokens() {
        long startMillis = 1234567890123L;
        long endMillis = 123456789012345L;
        String format = "P{y}Y{M}M{d}DT{H}:{m}:{s}";
        String expectedResult = "P1Y1M1DT1H0M1S";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "P{y}y{M}M{d}DT{H}:{m}:{s}.{S}";
        expectedResult = "PT1Y1M1DT1H0M1.0S";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
        assertEquals(expectedResult, actualResult);

        Token[] tokens = DurationFormatUtils.lexx(format);
        long years = 1;
        long months = 1;
        long days = 1;
        long hours = 1;
        long minutes = 1;
        long seconds = 1;
        long milliseconds = 0;
        long totalMillis = endMillis - startMillis;
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.y)) {
            years = (totalMillis / (31536000000L));
            totalMillis %= 31536000000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.M)) {
            months = (int) (totalMillis / 2628000000L);
            totalMillis %= 26280000000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            days = (int) (totalMillis / 86400000L);
            totalMillis %= 86400000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            hours = (int) (totalMillis / 3600000L);
            totalMillis %= 3600000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            minutes = (int) (totalMillis / 60000L);
            totalMillis %= 60000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            seconds = (int) (totalMillis / 1000L);
            totalMillis %= 1000L;
            milliseconds = totalMillis;
        }
        expectedResult = String.format("%d%s%d%s%d%s%d%s%d%s%d%s%d%s", years, DurationFormatUtils.y, months, DurationFormatUtils.M, days, DurationFormatUtils.d, hours, DurationFormatUtils.H, minutes, DurationFormatUtils.m, seconds, DurationFormatUtils.s, milliseconds, DurationFormatUtils.S);
        actualResult = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, true);
        assertEquals(expectedResult, actualResult);

        format = "P{y}y{M}M{d}DT{H}:{m}:{s}.{S}";
        expectedResult = "PT1Y1M1DT1H0M1.0S";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
        assertEquals(expectedResult, actualResult);
        tokens = DurationFormatUtils.lexx(format);
        years = 1;
        months = 1;
        days = 1;
        hours = 1;
        minutes = 1;
        seconds = 1;
        milliseconds = 0;
        totalMillis = endMillis - startMillis;
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.y)) {
            years = (totalMillis / (31536000000L));
            totalMillis %= 31536000000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.M)) {
            months = (int) (totalMillis / 2628000000L);
            totalMillis %= 26280000000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.d)) {
            days = (int) (totalMillis / 86400000L);
            totalMillis %= 86400000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.H)) {
            hours = (int) (totalMillis / 3600000L);
            totalMillis %= 3600000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.m)) {
            minutes = (int) (totalMillis / 60000L);
            totalMillis %= 60000L;
        }
        if (DurationFormatUtils.containsTokenWithValue(tokens, DurationFormatUtils.s)) {
            seconds = (int) (totalMillis / 1000L);
            totalMillis %= 1000L;
            milliseconds = (int) (totalMillis * 1000);
        }
        expectedResult = String.format("%d%s%d%s%d%s%d%s%d%s%d%s%d%s", years, DurationFormatUtils.y, months, DurationFormatUtils.M, days, DurationFormatUtils.d, hours, DurationFormatUtils.H, minutes, DurationFormatUtils.m, seconds, DurationFormatUtils.s, milliseconds, DurationFormatUtils.S);
        actualResult = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, false);
        assertEquals(expectedResult, actualResult);
    }
}