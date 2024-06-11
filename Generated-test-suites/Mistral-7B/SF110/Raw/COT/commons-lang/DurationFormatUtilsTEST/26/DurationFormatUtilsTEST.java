package org.junit.tests;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Token;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "01:46:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1DT1H46M37.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expectedResult = "1d 1h 46m 37s";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration_withPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        boolean padWithZeros = true;
        String expectedResult = "1d 01:01:46";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDuration_invalidFormat() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds'";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "1 day, 1 hour, 46 minutes, 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWords_suppressLeadingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "1 day, 1 hour, 46 minutes, 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWords_suppressTrailingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day, 1 hour, 46 minutes, 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWords_suppressLeadingAndTrailingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day, 1 hour, 46 minutes";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516099520000L;
        long endMillis = 1516102320000L;
        String expectedResult = "P1DT1H12M";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1516099520000L;
        long endMillis = 1516102320000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        String expectedResult = "PT1H12M";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod_withPadWithZeros() {
        long startMillis = 1516099520000L;
        long endMillis = 1516102320000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        boolean padWithZeros = true;
        String expectedResult = "PT1H12M";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod_withTimeZone() {
        long startMillis = 1516099520000L;
        long endMillis = 1516102320000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
        boolean padWithZeros = true;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String expectedResult = "PT1H12M";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, timeZone);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod_withInvalidFormat() {
        long startMillis = 1516099520000L;
        long endMillis = 1516102320000L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'T";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertNull(result);
    }

    @Test
    public void testFormatPeriod_withNegativeDuration() {
        long startMillis = 1516102320000L;
        long endMillis = 1516099520000L;
        String expectedResult = "PT1H12M";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testLexx() {
        String format = "d' days 'H' hours 'm' minutes 's' seconds 'S' milliseconds";
        Token[] expectedTokens = new Token[]{
                new Token('d'), new Token('d'),
                new Token('H'), new Token('H'),
                new Token('m'), new Token('m'),
                new Token('s'), new Token('s'),
                new Token('S')
        };
        Token[] tokens = DurationFormatUtils.lexx(format);
        assertArrayEquals(expectedTokens, tokens);
    }
}

class Token {
    private final char value;
    private int count;

    Token(char value) {
        this.value = value;
        this.count = 1;
    }

    Token(char value, int count) {
        this.value = value;
        this.count = count;
    }

    boolean equals(Object obj2) {
        Token tok2 = (Token)obj2;
        return this.value == tok2.value && this.count == tok2.count;
    }

    int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.value);
        for (int i = 1; i < this.count; i++) {
            sb.append(this.value);
        }
        return sb.toString();
    }

    static boolean containsTokenWithValue(Token[] tokens, Object value) {
        Pattern pattern = Pattern.compile("[" + value + "]");
        for (Token token : tokens) {
            if (pattern.matcher(token.toString()).matches()) {
                return true;
            }
        }
        return false;
    }
}