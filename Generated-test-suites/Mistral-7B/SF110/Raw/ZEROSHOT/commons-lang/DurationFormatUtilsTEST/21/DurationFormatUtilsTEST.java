package org.apache.commons.lang3.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "02:01:11.702";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "PT2H1M11.702S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d H:mm:ss";
        String expected = "1d 02:01:11";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithZeroPaddding() {
        long durationMillis = 1234567L;
        String format = "d H:mm:ss.SSS";
        String expected = "1d 02:01:11.702";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "1 day 2 hours 1 minute 11 seconds 702 milliseconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithLeadingZeroSuppression() {
        long durationMillis = 1234567L;
        String expected = "1 day 2 hours 1 min 11 secs 702 ms";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithTrailingZeroSuppression() {
        long durationMillis = 1234567L;
        String expected = "1 day 2 hours 1 min 11 sec 702 ms";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithBothZeroSuppression() {
        long durationMillis = 1234567L;
        String expected = "1d 2h 1m 11s 702ms";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 1337800000123L;
        String expected = "PT13Y2M2W1D11H31M59.877S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 1337800000123L;
        String format = "y M d H:mm:ss.SSS";
        String expected = "13Y 2M 1D 11H 31M 59.877S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithPadWithZerosAndDefaultTimeZone() {
        long startMillis = 1234567890123L;
        long endMillis = 1337800000123L;
        String format = "y-MM-dd HH:mm:ss.SSS";
        String expected = "13-02-29 11-31-59.877";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithCustomTimeZone() {
        long startMillis = 1234567890123L;
        long endMillis = 1337800000123L;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String format = "y-MM-dd HH:mm:ss.SSS z";
        String expected = "13-02-29 11:31:59.877 UTC";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithUnmatchedQuote() {
        long startMillis = 1234567890123L;
        long endMillis = 1337800000123L;
        String format = "y\'M\'d\' H:mm:ss.SSS";
        String[] expectedExMessage = {"Unmatched quote in format: y\'M\'d\' H:mm:ss.SSS"};
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        } catch (IllegalArgumentException e) {
            assertArrayEquals(expectedExMessage, e.getStackTrace());
            throw e;
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

        public Object getValue() {
            return this.value;
        }

        public int getCount() {
            return this.count;
        }

        public void increment() {
            ++this.count;
        }

        public static boolean containsTokenWithValue(Token[] tokens, Object value) {
            for (int sz = tokens.length, i = 0; i < sz; ++i) {
                if (tokens[i].getValue() == value) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Token)) {
                return false;
            }
            Token tok = (Token)obj;
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

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return Arrays.toString(new Object[]{this.value, this.count});
        }
    }
}