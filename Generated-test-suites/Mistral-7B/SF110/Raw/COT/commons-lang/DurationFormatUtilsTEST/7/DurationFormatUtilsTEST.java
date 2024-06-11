package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.List;

public class DurationFormatUtilsTest {

    @org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:11.700";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT2H7M11.7S";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "H:mm:ss.SSS", true);
        String expected = "02:07:11.700";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationNegative() {
        long durationMillis = -1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss.SSS");
        String expected = "-02:-07:-11.999";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithCustomFormat() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "mm:ss.SSS");
        String expected = "11:11.700";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        DurationFormatUtils.formatDuration(durationMillis, "x:mm:ss.SSS");
    }

    @org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "2 hours, 7 minutes, 11 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWordsWithSuppressedLeadingZero() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        String expected = "2 hours, 7 minutes, 11 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWordsWithSuppressedTrailingZero() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        String expected = "2 hours, 7 minutes, 11 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360489L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT17H13M18.117S";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithCustomFormat() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360489L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d 'day' H 'hour' m 'minute' s 'second'");
        String expected = "17 day, 13 hour, 18 minute, 11 second";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360489L;
        DurationFormatUtils.formatPeriod(startMillis, endMillis, "x:d 'day' H 'hour' m 'minute' s 'second'");
    }

    private static List<Token> createTokens(String format) {
        return new ArrayList<>(DurationFormatUtils.lexx(format));
    }

    @org.junit.Test
    public void testFormatWithCustomTokens() {
        long durationMillis = 1234567L;
        List<Token> tokens = createTokens("H:mm:ss.SSS");
        String result = DurationFormatUtils.format(tokens, 0, 0, 2, 7, 11, 12, 700, true);
        String expected = "02:07:11.700";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatWithNegativeTokens() {
        long durationMillis = -1234567L;
        List<Token> tokens = createTokens("H:mm:ss.SSS");
        String result = DurationFormatUtils.format(tokens, 0, 0, -2, -7, -11, -12, -700, true);
        String expected = "-02:-07:-11.999";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatWithMissingTokens() {
        long durationMillis = 1234567L;
        List<Token> tokens = createTokens("H:mm:ss");
        String result = DurationFormatUtils.format(tokens, 0, 0, 2, 7, 11, 0, 0, true);
        String expected = "02:07:11";
        assertEquals(expected, result);
    }
}

class Token {
    final Object value;
    final int count;

    Token(Object value) {
        this.value = value;
        this.count = 1;
    }

    Token(Object value, int count) {
        this.value = value;
        this.count = count;
    }
}