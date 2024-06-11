package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.67";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "PT10H4M37.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expected = "3 days 10 hours 4 minutes 37 seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d' days 'H' hours 'm' minutes";
        expected = "3 days 10 hours 4 minutes";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "H:mm:ss.SSS";
        expected = "10:04:37.670";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d' days 'H' hours 'm' minutes 's' seconds 'S' milliseconds";
        expected = "3 days 10 hours 4 minutes 37 seconds 670";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "x' days 'H' hours 'm' minutes 's' seconds";
        assertNull(DurationFormatUtils.formatDuration(durationMillis, format));
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "3 days 10 hours 4 minutes 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = false;
        expected = "3 days 10 hours 4 minutes 37 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = false;
        expected = "3 days 10 hours 4 minutes 37 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = false;
        suppressTrailingZeroElements = false;
        expected = "3 days 10 hours 4 minutes 37 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = true;
        expected = "3 days 10 hours 4 minutes";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070490000L;
        String expected = "PT59M50S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070490000L;
        String format = "Pd/M/d'T'H:mm:ss.SSS";
        String expected = "P3D/1M/5H59M50S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);

        format = "d' days 'H' hours 'm' minutes 's' seconds 'S' milliseconds";
        expected = "3 days 1 hour 59 minutes 50 seconds 0";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);

        format = "P";
        expected = "P3D";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1420070400000L;
        long endMillis = 1420070490000L;
        String format = "PxD/M/d'T'H:mm:ss.SSS";
        assertNull(DurationFormatUtils.formatPeriod(startMillis, endMillis, format));
    }

    @Test
    public void testLexx() {
        String format = "d' days 'H' hours 'm' minutes 's' seconds 'S' milliseconds";
        Token[] expectedTokens = new Token[] {
            new Token(new StringBuilder("d")),
            new Token(' '),
            new Token(new StringBuilder("days")),
            new Token(' '),
            new Token(new StringBuilder("'")),
            new Token('H'),
            new Token(' '),
            new Token(new StringBuilder("hours")),
            new Token(' '),
            new Token(new StringBuilder("'")),
            new Token('m'),
            new Token(' '),
            new Token(new StringBuilder("minutes")),
            new Token(' '),
            new Token(new StringBuilder("'")),
            new Token('s'),
            new Token(' '),
            new Token(new StringBuilder("seconds")),
            new Token(' '),
            new Token(new StringBuilder("'")),
            new Token('S'),
            new Token(' '),
            new Token(new StringBuilder("milliseconds")),
            new Token(' ')
        };
        Token[] tokens = DurationFormatUtils.lexx(format);
        assertNotNull(tokens);
        assertEquals(expectedTokens.length, tokens.length);
        for (int i = 0; i < tokens.length; ++i) {
            assertEquals(expectedTokens[i].getValue(), tokens[i].getValue());
            assertEquals(expectedTokens[i].getCount(), tokens[i].getCount());
        }
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
            if (!(obj instanceof Token)) {
                return false;
            }
            final Token token = (Token)obj;
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

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }

        public static boolean containsTokenWithValue(Token[] tokens, Object value) {
            for (int sz = tokens.length, i = 0; i < sz; ++i) {
                if (tokens[i].getValue() == value) {
                    return true;
                }
            }
            return false;
        }
    }
}