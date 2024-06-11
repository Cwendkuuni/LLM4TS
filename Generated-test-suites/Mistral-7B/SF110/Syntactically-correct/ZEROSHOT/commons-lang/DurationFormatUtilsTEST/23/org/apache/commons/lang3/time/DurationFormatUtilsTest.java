package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Token;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 234567890L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P2Y5M3.127S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationCustomFormat() {
        long durationMillis = 34567890L;
        String format = "dd-HH:mm:ss:SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "34-03:11:17:397";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationCustomFormatWithPadZeros() {
        long durationMillis = 4567890L;
        String format = "dd-HH:mm:ss:SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "045-03:06:11:390";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "1 year, 5 months, 3 weeks, 1 day, 7 hours, 11 minutes, 39 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
        long durationMillis = 1234567890L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        String expected = "1 year, 5 months, 3 weeks, 1 day, 7 hours, 11 minutes, 39 seconds";
        assertEquals(expected, result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        expected = "1 year, 5 months, 3 weeks, 1 day, 7 hours, 11 minutes";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
        long durationMillis = 1234567890L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        String expected = "1 year, 5 months, 3 weeks, 1 day, 7 hours, 11 minutes, 39 seconds";
        assertEquals(expected, result);

        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        expected = "1 year, 5 months, 3 weeks, 1 day, 7 hours, 11 minutes";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L;
        long endMillis = 1345678901234L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1Y4M23DT3H5M26.784S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodCustomFormat() {
        long startMillis = 9876543210987654321L;
        long endMillis = 9876543210987654331L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1998-12-31T23:59:59.784";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodCustomFormatWithTimeZone() {
        long startMillis = 1111111111111L;
        long endMillis = 1222222222222L;
        String format = "yyyy-MM-dd HH:mm:ss z";
        String timeZoneID = "America/New_York";
        Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
        startCal.setTimeInMillis(startMillis);
        Calendar endCal = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
        endCal.setTimeInMillis(endMillis);
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getTimeZone(timeZoneID));
        String expected = "2012-05-20 15:22:11 EDT";
        assertEquals(expected, result);
    }

    private static class Token implements Comparable<Token> {
        private final Object value;
        private int count;

        Token(final Object value) {
            this.value = value;
            this.count = 1;
        }

        Token(final Object value, final int count) {
            this.value = value;
            this.count = count;
        }

        boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Token) {
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
            return false;
        }

        int hashCode() {
            return this.value.hashCode();
        }

        String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }

        int compareTo(final Token other) {
            if (this.value instanceof Number && other.value instanceof Number) {
                return Long.compare((Long)this.value, (Long)other.value);
            }
            return 0;
        }
    }
}