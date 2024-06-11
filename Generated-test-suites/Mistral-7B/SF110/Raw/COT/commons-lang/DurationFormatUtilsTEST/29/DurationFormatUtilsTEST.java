package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.apache.commons.lang3.time.Tokenizer;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:02:11.7";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "P1Y2M3DT1H2M11.7S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "d H m:s.SSS";
        String expected = "1 1 2:11.7";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567890L;
        String format = "d H m:s.SSS";
        String expected = "001 01 02:01.017";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 1234567890L;
        String format = "d H m:s.SSS'abc'";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 year, 2 months, 3 days, 1 hour, 1 minute, 11 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 year, 2 months, 1 hour, 1 minute, 11 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expected = "1 year, 2 months, 1 hour, 1 minute, 11 sec";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() throws ParseException {
        long startMillis = 1451609600000L;
        long endMillis = 1451613200000L;
        String expected = "P1DT5H";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1451609600000L;
        long endMillis = 1451613200000L;
        String format = "P";
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(new Date(startMillis));
        int yearStart = calendar.get(Calendar.YEAR);
        int monthStart = calendar.get(Calendar.MONTH);
        int dayStart = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(new Date(endMillis));
        int yearEnd = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH);
        int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
        String expected = "P1Y0M1DT0H0M5DT0H0M0DT0H0M0";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        expected += " " + yearStart + "T" + monthStart + "T" + dayStart + "T00:00:00Z" + "T" + yearEnd + "T" + monthEnd + "T" + dayEnd + "T00:00:00Z";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() throws ParseException {
        long startMillis = 1451609600000L;
        long endMillis = 1451613200000L;
        String format = "P";
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(new Date(startMillis));
        int yearStart = calendar.get(Calendar.YEAR);
        int monthStart = calendar.get(Calendar.MONTH);
        int dayStart = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(new Date(endMillis));
        int yearEnd = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH);
        int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
        String expected = "P1Y0M1DT0H0M5DT0H0M0DT0H0M0S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        expected += " " + yearStart + "T" + String.format("%02d", monthStart) + "T" + String.format("%02d", dayStart) + "T00:00:00Z" + "T" + yearEnd + "T" + String.format("%02d", monthEnd) + "T" + String.format("%02d", dayEnd) + "T00:00:00Z";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithTimeZone() throws ParseException {
        long startMillis = 1451609600000L;
        long endMillis = 1451613200000L;
        String format = "P";
        String timezoneId = "America/New_York";
        Calendar calendarStart = GregorianCalendar.getInstance(TimeZone.getTimeZone(timezoneId));
        calendarStart.setTime(new Date(startMillis));
        int yearStart = calendarStart.get(Calendar.YEAR);
        int monthStart = calendarStart.get(Calendar.MONTH);
        int dayStart = calendarStart.get(Calendar.DAY_OF_MONTH);
        Calendar calendarEnd = GregorianCalendar.getInstance(TimeZone.getTimeZone(timezoneId));
        calendarEnd.setTime(new Date(endMillis));
        int yearEnd = calendarEnd.get(Calendar.YEAR);
        int monthEnd = calendarEnd.get(Calendar.MONTH);
        int dayEnd = calendarEnd.get(Calendar.DAY_OF_MONTH);
        String expected = "P1Y0M1DT0H0M5DT0H0M0DT0H0M0S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getTimeZone(timezoneId));
        expected += " " + yearStart + "T" + String.format("%02d", monthStart) + "T" + String.format("%02d", dayStart) + "T03:00:00Z" + "T" + yearEnd + "T" + String.format("%02d", monthEnd) + "T" + String.format("%02d", dayEnd) + "T03:00:00Z";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithNegativeDurations() throws ParseException {
        long startMillis = 1451613200000L;
        long endMillis = 1451609600000L;
        String format = "P";
        String expected = "P-1Y0M1DT-5H";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testlexx() {
        String format = "y M d H m s S";
        Tokenizer tokenizer = new Tokenizer();
        Token[] tokens = tokenizer.lexx(format);
        int expectedSize = 7;
        assertEquals(expectedSize, tokens.length);
        for (int i = 0; i < expectedSize; ++i) {
            Token token = tokens[i];
            Object expectedValue = null;
            switch (i) {
                case 0:
                    expectedValue = DurationFormatUtils.y;
                    break;
                case 1:
                    expectedValue = DurationFormatUtils.M;
                    break;
                case 2:
                    expectedValue = DurationFormatUtils.d;
                    break;
                case 3:
                    expectedValue = DurationFormatUtils.H;
                    break;
                case 4:
                    expectedValue = DurationFormatUtils.m;
                    break;
                case 5:
                    expectedValue = DurationFormatUtils.s;
                    break;
                case 6:
                    expectedValue = DurationFormatUtils.S;
                    break;
            }
            assertEquals(expectedValue, token.getValue());
        }
    }
}

class Tokenizer {

    Tokenizer() {
    }

    Token[] lexx(String format) {
        ArrayList<Token> list = new ArrayList<Token>(format.length());
        boolean inLiteral = false;
        StringBuilder buffer = null;
        Token previous = null;
        for (int i = 0; i < format.length(); ++i) {
            char ch = format.charAt(i);
            if (inLiteral && ch != '\'') {
                buffer.append(ch);
            }
            else {
                Object value = null;
                switch (ch) {
                    case '\'': {
                        if (inLiteral) {
                            buffer = null;
                            inLiteral = false;
                        }
                        buffer = new StringBuilder();
                        list.add(new Token(buffer));
                        inLiteral = true;
                        break;
                    }
                    case 'y': {
                        value = DurationFormatUtils.y;
                        break;
                    }
                    case 'M': {
                        value = DurationFormatUtils.M;
                        break;
                    }
                    case 'd': {
                        value = DurationFormatUtils.d;
                        break;
                    }
                    case 'H': {
                        value = DurationFormatUtils.H;
                        break;
                    }
                    case 'm': {
                        value = DurationFormatUtils.m;
                        break;
                    }
                    case 's': {
                        value = DurationFormatUtils.s;
                        break;
                    }
                    case 'S': {
                        value = DurationFormatUtils.S;
                        break;
                    }
                    default: {
                        if (buffer == null) {
                            buffer = new StringBuilder();
                            list.add(new Token(buffer));
                        }
                        buffer.append(ch);
                        break;
                    }
                }
                if (value != null) {
                    if (previous != null && previous.getValue() == value) {
                        previous.increment();
                    }
                    else {
                        Token token = new Token(value);
                        list.add(token);
                        previous = token;
                    }
                    buffer = null;
                }
            }
        }
        if (inLiteral) {
            throw new IllegalArgumentException("Unmatched quote in format: " + format);
        }
        return list.toArray(new Token[list.size()]);
    }

    static class Token {
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

        void increment() {
            ++this.count;
        }

        int getCount() {
            return this.count;
        }

        Object getValue() {
            return this.value;
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
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }
}