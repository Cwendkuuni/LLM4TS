package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:37.67";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 24 * 60 * 60 * 1000 * 30;
        String expected = "P30D";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 24 * 60 * 60 * 1000 * 30;
        String format = "d HH:mm:ss";
        String expected = "30 01:00:00";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "d HH:mm:ss";
        String expected = "1:01:31:067";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "d HH:mm:ssz";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 24 * 60 * 60 * 1000 * 30;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 month 1 day";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1638131200000L;
        long endMillis = 1638137600000L;
        String expected = "P35H";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1638131200000L;
        long endMillis = 1638137600000L;
        String format = "Pd HH:mm:ss";
        String expected = "P35D 01:04:20";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 1638131200000L;
        long endMillis = 1638137600000L;
        String format = "Pd HH:mm:ss";
        String expected = "P35D 01:04:20";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1638131200000L;
        long endMillis = 1638137600000L;
        String format = "Pd HH:mm:ssz";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    }

    @Test
    public void testLexx() {
        String format = "d HH:mm:ssSSS";
        List<Token> expectedTokens = Arrays.asList(
                new Token("d"),
                new Token("HH"),
                new Token("mm"),
                new Token("ss"),
                new Token("SSS")
        );
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<Token> tokensList = new ArrayList<Token>(Arrays.asList(tokens));
        assertEquals(expectedTokens, tokensList);
    }

    @Test
    public void testFormat() {
        Token[] tokens = {
                new Token("d"),
                new Token("HH"),
                new Token("mm"),
                new Token("ss"),
                new Token("SSS")
        };
        long years = 1;
        long months = 2;
        long days = 3;
        long hours = 4;
        long minutes = 5;
        long seconds = 6;
        long milliseconds = 7;
        boolean padWithZeros = true;
        String expected = "01:02:03:04:05:06:007";
        String result = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
        assertEquals(expected, result);
    }

    @Test
    public void testPaddedValue() {
        long value = 123;
        boolean padWithZeros = true;
        int count = 3;
        String expected = "012";
        String result = DurationFormatUtils.paddedValue(value, padWithZeros, count);
        assertEquals(expected, result);
    }

    @Test
    public void testParseDurationWithValidDurationString() {
        String durationString = "P2Y3M4DT2H5M";
        long expectedDurationMillis = 2 * 31536000000L + 4 * 1576800000L + 2 * 3600000L + 5 * 60000L;
        long durationMillis = DurationFormatUtils.parseDuration(durationString);
        assertEquals(expectedDurationMillis, durationMillis);
    }

    @Test(expected = ParseException.class)
    public void testParseDurationWithInvalidDurationString() {
        String durationString = "P2Y3M4DT2H5Mz";
        DurationFormatUtils.parseDuration(durationString);
    }
}

class Token {
    private final Object value;
    private int count;

    public Token(Object value) {
        this.value = value;
        this.count = 1;
    }

    public Token(Object value, int count) {
        this.value = value;
        this.count = count;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token token = (Token)obj;
            if (this.value.getClass() != token.value.getClass()) {
                return false;
            }
            if (this.count != token.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(token.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(token.value);
            }
            return this.value == token.value;
        }
        return false;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }
}