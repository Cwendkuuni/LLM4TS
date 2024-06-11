package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "P1y1m2w3d10h4m37.67s";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expected = "1d 10h 4m 37s";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d' days HH:mm:ss.SSS";
        expected = "1d 10:04:37.670";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        format = "d' days H:mm:ss.SSS";
        expected = "1d 10:04:37.67";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "d' days H:mm:ss.SSS";
        String expected = "1d 10:04:37.670";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);

        format = "d' days H:mm:ss.SSS'";
        expected = "1d 10:04:37.670 ";
        result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);

        format = "d' days H:mm:ss.SSS'";
        expected = "1d 10:04:37.67 ";
        result = DurationFormatUtils.formatDuration(durationMillis, format, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 day 10 hours 4 minutes";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = false;
        expected = "1 day 10 hours 4 minutes 37 seconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressTrailingZeroElements = false;
        expected = "1 day 10 hours 4 minutes 37 seconds 670 milliseconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = true;
        expected = "1 day 10 hours 4 minutes 37 seconds 670 milliseconds";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = false;
        expected = "1 day 10 hours 4 minutes 37 seconds 670";
        result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 123456789L;
        long endMillis = 134567890L;
        String expected = "P1y1m1w1d3h10m7.0s";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 123456789L;
        long endMillis = 134567890L;
        String format = "P'yyyy'Y'MM'MM'dd'DT'HH'HH'mm'mm'ss.SSS'";
        String expected = "P1y1m1w1d3h10m7.0s";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);

        format = "P'yyyy-MM-dd'T'HH:mm:ss.SSS'";
        Calendar start = new GregorianCalendar(2019, 9, 1);
        Calendar end = new GregorianCalendar(2019, 9, 5);
        expected = "P1y3d0h0m0s";
        result = DurationFormatUtils.formatPeriod(start.getTimeInMillis(), end.getTimeInMillis(), format);
        assertEquals(expected, result);

        format = "d' days HH:mm:ss.SSS";
        expected = "3 days 00:00:00.0";
        result = DurationFormatUtils.formatPeriod(start.getTimeInMillis(), end.getTimeInMillis(), format);
        assertEquals(expected, result);

        format = "Pd HH:mm:ss.SSS";
        expected = "P3d 00:00:00.0";
        result = DurationFormatUtils.formatPeriod(start.getTimeInMillis(), end.getTimeInMillis(), format);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 123456789L;
        long endMillis = 134567890L;
        String format = "Pd HH:mm:ss.SSS";
        String expected = "P3d 00:00:00.0";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        assertEquals(expected, result);

        format = "P'yyyy-MM-dd'T'HH:mm:ss.SSS'";
        Calendar startCal = new GregorianCalendar(2019, 9, 1);
        Calendar endCal = new GregorianCalendar(2019, 9, 5);
        expected = "P1y2m3d0h0m0s";
        result = DurationFormatUtils.formatPeriod(startCal.getTimeInMillis(), endCal.getTimeInMillis(), format, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormat() {
        Token[] tokens = DurationFormatUtils.lexx("d' days H:mm:ss.SSS");
        long years = 1;
        long months = 2;
        long days = 3;
        long hours = 4;
        long minutes = 5;
        long seconds = 6;
        long milliseconds = 7;
        String expected = "3 days 04:05:06.007";
        String result = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, true);
        assertEquals(expected, result);

        expected = "3 days 04:05:06.007";
        result = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, false);
        assertEquals(expected, result);
    }

    @Test
    public void testLexx() {
        String format = "d' days H:mm:ss.SSS";
        Token[] expected = new Token[] {
            new Token(Integer.toString(3).repeat(1)),
            new Token("d"),
            new Token(Integer.toString(4).repeat(1)),
            new Token("H"),
            new Token(":"),
            new Token(Integer.toString(5).repeat(1)),
            new Token("m"),
            new Token(":"),
            new Token(Integer.toString(6).repeat(1)),
            new Token("s"),
            new Token("."),
            new Token(Integer.toString(7).repeat(1)),
            new Token("S")
        };
        Token[] result = DurationFormatUtils.lexx(format);
        assertArrayEquals(expected, result);
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
        return false;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }
}