package org.apache.commons.lang3; // assuming the test class is in the same package as the DurationFormatUtils class

import org.apache.commons.lang3.time.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String formattedDuration = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "01:46:37.67";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String formattedDuration = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT1H46M37.67S";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d H:mm:ss.SSS";
        String formattedDuration = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "1d 01:46:37.67";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 1234567L;
        String format = "d H:mm:ss.SSS";
        boolean padWithZeros = true;
        String formattedDuration = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        String expected = "01d 01:46:37.67";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String formattedDuration = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "1 day 1 hour 46 minutes 37 seconds 670 milliseconds";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106359000L; // 2018-01-01 23:56:59 UTC
        String formattedDuration = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT31H56M59.59S";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106359000L; // 2018-01-01 23:56:59 UTC
        String format = "d H:mm:ss.SSS";
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1d 23:56:59.59";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106359000L; // 2018-01-01 23:56:59 UTC
        String format = "d H:mm:ss.SSS";
        boolean padWithZeros = true;
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        String expected = "1d 00:23:56:59.59";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        long endMillis = 1516106359000L; // 2018-01-01 23:56:59 UTC
        String format = "d H:mm:ss.SSS";
        TimeZone timeZone = java.util.TimeZone.getTimeZone("UTC");
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        String expected = "1d 23:56:59.59";
        assertEquals(expected, formattedDuration);
    }

    private static class Token {
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
                Token tok = (Token) obj;
                if (this.value.getClass() != tok.value.getClass()) {
                    return false;
                }
                if (this.count != tok.count) {
                    return false;
                }
                if (this.value instanceof StringBuilder) {
                    return this.value.toString().equals(tok.value.toString());
                }
                if (this.value instanceof Number) {
                    return this.value.equals(tok.value);
                }
                return this.value == tok.value;
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

    @Test
    public void testLexx() {
        String format = "y M d H m s S";
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<Object> expectedValues = new ArrayList<>(Arrays.asList(DurationFormatUtils.y, DurationFormatUtils.M, DurationFormatUtils.d, DurationFormatUtils.H, DurationFormatUtils.m, DurationFormatUtils.s, DurationFormatUtils.S));
        List<Token> expectedTokens = new ArrayList<>();
        for (Object value : expectedValues) {
            expectedTokens.add(new Token(value));
        }
        assertEquals(expectedTokens.toArray(new Token[expectedTokens.size()]), tokens);
    }

    @Test
    public void testFormatWithTokens() {
        long durationMillis = 1234567L;
        Token[] tokens = DurationFormatUtils.lexx("y M d H m s S");
        String formattedDuration = DurationFormatUtils.format(tokens, durationMillis.intValue(), 0, 0, 0, 0, 0, 0, true);
        String expected = "1 1 1 1 1 1 1 670";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatWithNegativeDuration() {
        long startMillis = 1516106359000L; // 2018-01-01 23:56:59 UTC
        long endMillis = 1516092800000L; // 2018-01-01 00:00:00 UTC
        String format = "d H:mm:ss.SSS";
        String formattedDuration = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
        String expected = "-1d 23:59:59.0";
        assertEquals(expected, formattedDuration);
    }

    @Test
    public void testFormatWithWrongFormat() {
        long durationMillis = 1234567L;
        String format = "y M d H m s Ss"; // misspelled "Ss" instead of "S"
        try {
            String formattedDuration = DurationFormatUtils.formatDuration(durationMillis, format, true);
            fail("Expected IllegalArgumentException with message 'Invalid format: y M d H m s Ss'");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: y M d H m s Ss", e.getMessage());
        }
    }
}