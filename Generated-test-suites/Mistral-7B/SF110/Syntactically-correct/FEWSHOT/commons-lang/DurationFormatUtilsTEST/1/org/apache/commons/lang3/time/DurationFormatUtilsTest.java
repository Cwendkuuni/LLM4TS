package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 5 * 60 * 60 * 1000; // 5 hours
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals("05:00:00", result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 3 * 24 * 60 * 60 * 1000; // 3 days
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals("P3D", result);
    }

    @Test
    public void testFormatDurationWithCustomFormat() {
        long durationMillis = 1 * 60 * 1000; // 1 minute
        String format = "m' minute'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("1 minute", result);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1 * 60 * 1000; // 1 minute
        String format = "m' minute'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals("01:00", result);
    }

    @Test
    public void testFormatDurationWithNegativeValue() {
        long durationMillis = -1 * 60 * 1000; // -1 minute
        String format = "m' minute'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals("-01:00", result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 5 * 60 * 60 * 1000; // 5 hours
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals("5 hours", result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
        long durationMillis = 1 * 60 * 1000; // 1 minute
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals("1 minute", result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
        long durationMillis = 1 * 60 * 1000; // 1 minute
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals("1 minute", result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1623329200000L; // 2021-06-01 00:00:00
        long endMillis = 1623332800000L; // 2021-06-01 02:00:00
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals("P1H", result);
    }

    @Test
    public void testFormatPeriodWithCustomFormat() {
        long startMillis = 1623329200000L; // 2021-06-01 00:00:00
        long endMillis = 1623332800000L; // 2021-06-01 02:00:00
        String format = "'P'y'Y'M'M'd' DT 'H'h' m'm's's'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("P1Y0M1DT2H", result);
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 1623329200000L; // 2021-06-01 00:00:00
        long endMillis = 1623332800000L; // 2021-06-01 02:00:00
        String format = "'P'y'Y'M'M'd' DT 'H'h' m'm's's'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertEquals("P1Y0M1DT02H", result);
    }

    @Test
    public void testFormatPeriodWithNegativeValue() {
        long startMillis = 1623332800000L; // 2021-06-01 02:00:00
        long endMillis = 1623329200000L; // 2021-06-01 00:00:00
        String format = "P'w'W'd' DT 'H'h' m'm's's'";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals("P-1DT2H", result);
    }

    @Test
    public void testFormatPeriodWithCustomFormatAndTimezone() {
        long startMillis = 1623329200000L; // 2021-06-01 00:00:00
        long endMillis = 1623332800000L; // 2021-06-01 02:00:00
        TimeZone timezone = TimeZone.getTimeZone("UTC");
        String format = "E, d MMM y H:mm:ss z";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone);
        assertEquals("Tue, 1 Jun 21 02:00:00 UTC", result);
    }

    private static class TokenTest {
        @Test
        public void testTokenEquals() {
            Token token1 = new Token('H');
            Token token2 = new Token('H');
            Token token3 = new Token('m');
            Token token4 = new Token('H', 2);
            Token token5 = new Token('H', 2, "HH");
            Token token6 = new Token(new StringBuilder("HH"));
            Token token7 = new Token(new StringBuilder("HH"), "HH");
            List<Token> tokens = new ArrayList<>();
            tokens.add(token1);
            tokens.add(token2);
            tokens.add(token3);
            tokens.add(token4);
            tokens.add(token5);
            tokens.add(token6);
            tokens.add(token7);
            for (int i = 0; i < tokens.size(); ++i) {
                for (int j = i + 1; j < tokens.size(); ++j) {
                    boolean equals = tokens.get(i).equals(tokens.get(j));
                    if (i == j) {
                        assertTrue(equals);
                    } else {
                        assertFalse(equals);
                    }
                }
            }
        }
    }
}