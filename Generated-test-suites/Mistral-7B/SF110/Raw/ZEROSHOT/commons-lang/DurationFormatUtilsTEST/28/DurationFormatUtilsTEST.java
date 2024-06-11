package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DurationFormatUtilsTest {

    @org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "P1Y1M10DT10H4M37.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d H:m:s.SSS";
        boolean padWithZeros = true;
        String expected = "1 1:04:37.670";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithDifferentTokens() {
        long durationMillis = 1234567L;
        String format = "d H:m:s S";
        boolean padWithZeros = true;
        String expected = "1 1:04:37 6";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithNegativeValues() {
        long durationMillis = -1234567L;
        String format = "m:ss";
        boolean padWithZeros = false;
        String expected = "-2:24:37";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day 1 hour 10 minutes 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106360000L; // 2018-01-01 23:56:00 UTC
        String expected = "P1DT15H56M";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriod() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106360000L; // 2018-01-01 23:56:00 UTC
        String format = "Pd HH:mm:ss";
        boolean padWithZeros = true;
        String expected = "P1D15H05M56S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithDifferentTokens() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106360000L; // 2018-01-01 23:56:00 UTC
        String format = "Pd HH:mm D";
        boolean padWithZeros = false;
        String expected = "P1D15H05M 1st Jan 2018";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithNegativeValues() {
        long startMillis = 1516106360000L; // 2018-01-01 23:56:00 UTC
        long endMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        String format = "P-d HH:mm";
        boolean padWithZeros = false;
        String expected = "P-1D15H05M";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testLexx() {
        String format = "d H:m:s S";
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new Token("d"));
        expectedTokens.add(new Token("H"));
        expectedTokens.add(new Token("m"));
        expectedTokens.add(new Token("s"));
        expectedTokens.add(new Token("S"));
        Token[] expected = expectedTokens.toArray(new Token[expectedTokens.size()]);
        assertArrayEquals(expected, tokens);
    }

}

class Token {
    private final Object value;
    private int count;

    Token(Object value) {
        this.value = value;
        this.count = 1;
    }

    Token(Object value, int count) {
        this.value = value;
        this.count = count;
    }

    // Getters and setters omitted for brevity

    @Override
    public boolean equals(Object obj) {
        // Implementation omitted for brevity
    }

    @Override
    public int hashCode() {
        // Implementation omitted for brevity
    }

    @Override
    public String toString() {
        // Implementation omitted for brevity
    }
}