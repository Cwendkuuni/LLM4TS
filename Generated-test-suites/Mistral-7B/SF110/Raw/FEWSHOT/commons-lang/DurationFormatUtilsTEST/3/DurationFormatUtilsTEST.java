package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String formattedDuration = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "10:04:37.67";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String formattedDuration = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT10H4M37.67S";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String formattedDuration = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "3 days 10 hours 4 minutes 37 seconds";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 1234567;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        boolean padWithZeros = true;
        String formattedDuration = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        String expected = "03 days 10 hours 04 minutes 37 seconds";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationTokensWithPadZeros() {
        long durationMillis = 1234567;
        Token[] tokens = DurationFormatUtils.lexx("d' days H' hours m' minutes s' seconds");
        long years = 0;
        long months = 1;
        long days = 3;
        long hours = 10;
        long minutes = 4;
        long seconds = 37;
        long milliseconds = 67;
        String formattedDuration = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
        String expected = "03 days 10 hours 04 minutes 37 seconds";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationTokensWithoutPadZeros() {
        long durationMillis = 1234567;
        Token[] tokens = DurationFormatUtils.lexx("d days H hours m minutes s seconds");
        long years = 0;
        long months = 1;
        long days = 3;
        long hours = 10;
        long minutes = 4;
        long seconds = 37;
        long milliseconds = 67;
        String formattedDuration = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, false);
        String expected = "3 days 10 hours 4 minutes 37 seconds";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodHMS() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        String formattedDuration = DurationFormatUtils.formatPeriodHMS(startMillis, endMillis);
        String expected = "3 days 14 hours 27 minutes 6.35";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        String formattedDuration = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT3DPT14HPT27MPT6.354S";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "3 days 14 hours 27 minutes 6.35";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        boolean padWithZeros = true;
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        String expected = "03 days 14 hours 27 minutes 06.35";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodTokensWithPadZeros() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        Token[] tokens = DurationFormatUtils.lexx("d' days H' hours m' minutes s' seconds");
        long years = 0;
        long months = 1;
        long daysFromStart = 3;
        long hoursFromStart = 10;
        long minutesFromStart = 4;
        long secondsFromStart = 37;
        long millisecondsFromStart = 67;
        long daysFromEnd = 4;
        long hoursFromEnd = 14;
        long minutesFromEnd = 27;
        long secondsFromEnd = 0;
        long millisecondsFromEnd = 354;
        String formattedDuration = DurationFormatUtils.formatPeriod(tokens, startMillis, endMillis, padWithZeros);
        String expected = "03 days 14 hours 27 minutes 06.35";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodTokensWithoutPadZeros() {
        long startMillis = 1234567;
        long endMillis = 8765432;
        Token[] tokens = DurationFormatUtils.lexx("d days H hours m minutes s seconds");
        long years = 0;
        long months = 1;
        long daysFromStart = 3;
        long hoursFromStart = 10;
        long minutesFromStart = 4;
        long secondsFromStart = 37;
        long millisecondsFromStart = 67;
        long daysFromEnd = 4;
        long hoursFromEnd = 14;
        long minutesFromEnd = 27;
        long secondsFromEnd = 0;
        long millisecondsFromEnd = 354;
        String formattedDuration = DurationFormatUtils.formatPeriod(tokens, startMillis, endMillis, false);
        String expected = "3 days 14 hours 27 minutes 6.35";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testLexx() {
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<Object> expectedValues = Arrays.asList(DurationFormatUtils.d, DurationFormatUtils.H, DurationFormatUtils.m, DurationFormatUtils.s);
        List<Token> expectedTokens = Arrays.asList(new Token(DurationFormatUtils.d), new Token(DurationFormatUtils.H), new Token(DurationFormatUtils.m), new Token(DurationFormatUtils.s));
        assertEquals(expectedValues, Arrays.asList(tokens[0].getValue(), tokens[1].getValue(), tokens[2].getValue(), tokens[3].getValue()));
        assertEquals(expectedTokens, Arrays.asList(tokens[0], tokens[1], tokens[2], tokens[3]));
    }
}