package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:11.700";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P2D0H7M11.7S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "2 days 7 hours 11 minutes 11.7 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 1234567;
        String format = "d' days  H:mm:ss";
        boolean padWithZeros = true;
        String result = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        String expected = "2 days 02:07:11.700";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "2 days 7 hours 11 minutes 11.7 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1451606400000L; // 2015-11-30 00:00:00
        long endMillis = 1451610000000L; // 2015-11-30 05:00:00
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1DT5H";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1451606400000L; // 2015-11-30 00:00:00
        long endMillis = 1451610000000L; // 2015-11-30 05:00:00
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1 day 5 hours";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 1451606400000L; // 2015-11-30 00:00:00
        long endMillis = 1451610000000L; // 2015-11-30 05:00:00
        String format = "d' days  H:mm";
        boolean padWithZeros = true;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        String expected = "1 day 05:00";
        assertEquals(expected, result);
    }

    @Test
    public void testFormat() {
        long years = 2;
        long months = 3;
        long days = 4;
        long hours = 5;
        long minutes = 6;
        long seconds = 7;
        long milliseconds = 8;
        long padWithZeros = 1;
        String[] tokens = {"y", "M", "d", "H", "m", "s", "S"};
        String result = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, padWithZeros);
        String expected = "02y 03M 04d 05H 06m 07s 008S";
        assertEquals(expected, result);
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

    static boolean containsTokenWithValue(Token[] tokens, Object value) {
        for (Token token : tokens) {
            if (token.getValue() == value) {
                return true;
            }
        }
        return false;
    }
}

class TestHelper {
    static long addDays(long date, int days) {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTimeInMillis();
    }
}