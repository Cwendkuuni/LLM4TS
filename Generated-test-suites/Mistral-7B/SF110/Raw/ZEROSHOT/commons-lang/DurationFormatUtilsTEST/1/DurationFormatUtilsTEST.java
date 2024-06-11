package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Tokens;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.670";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 25200000L;
        String expected = "P1dPT20H";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "d HH:mm:ss.SSS";
        String expected = "1 d 01:04:37.890";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "d HH:mm:ss.SSS";
        String expected = "1d 01:04:37.670";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        String expected = "1 day 1 hour 4 minutes 37 seconds 890 milliseconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expected, actual);

        String expectedNoLeadingZero = "1 day 1 hour 4 minutes 37 seconds 890 milliseconds";
        String actualNoLeadingZero = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        Assert.assertEquals(expectedNoLeadingZero, actualNoLeadingZero);

        String expectedNoTrailingZero = "1 day 1 hour 4 minutes 37 seconds";
        String actualNoTrailingZero = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        Assert.assertEquals(expectedNoTrailingZero, actualNoTrailingZero);

        String expectedNoLeadingOrTrailingZero = "1 day 1 hour 4 minutes 37 seconds";
        String actualNoLeadingOrTrailingZero = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        Assert.assertEquals(expectedNoLeadingOrTrailingZero, actualNoLeadingOrTrailingZero);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1640995200000L;
        long endMillis = 1641001600000L;
        String expected = "2022-03-14T23:30:00.000Z-2022-03-15T02:00:00.000Z";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1640995200000L;
        long endMillis = 1641001600000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        Calendar startCal = Calendar.getInstance(TimeZone.getDefault());
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance(TimeZone.getDefault());
        endCal.setTime(endDate);

        String expected = "2022-03-14T23:30:00.000Z-2022-03-15T02:00:00.000Z";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expected, actual);

        String expectedWithTimeZone = "2022-03-14T23:30:00.000-07:00-2022-03-15T02:00:00.000-07:00";
        String actualWithTimeZone = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getTimeZone("America/Los_Angeles"));
        Assert.assertEquals(expectedWithTimeZone, actualWithTimeZone);
    }

    private static class Token {
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

        boolean equals(Object obj2) {
            if (!(obj2 instanceof Token)) {
                return false;
            }
            Token tok2 = (Token) obj2;
            if (this.value.getClass() != tok2.value.getClass()) {
                return false;
            }
            if (this.count != tok2.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(tok2.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(tok2.value);
            }
            return this.value == tok2.value;
        }

        int hashCode() {
            return this.value.hashCode();
        }
    }

    private static Token[] lexx(String format) {
        ArrayList<Token> list = new ArrayList<>();
        boolean inLiteral = false;
        StringBuilder buffer = null;
        Token previous = null;

        for (int i = 0; i < format.length(); ++i) {
            char ch = format.charAt(i);
            if (inLiteral) {
                if (ch != '\'') {
                    buffer.append(ch);
                } else {
                    buffer = null;
                    inLiteral = false;
                }
            } else {
                Object value = null;
                switch (ch) {
                    case '\'':
                        if (inLiteral) {
                            inLiteral = false;
                        } else {
                            buffer = new StringBuilder();
                            list.add(new Token(buffer));
                            inLiteral = true;
                        }
                        break;
                    case 'y':
                        value = "y";
                        break;
                    case 'M':
                        value = "M";
                        break;
                    case 'd':
                        value = "d";
                        break;
                    case 'H':
                        value = "H";
                        break;
                    case 'm':
                        value = "m";
                        break;
                    case 's':
                        value = "s";
                        break;
                    case 'S':
                        value = "S";
                        break;
                    default:
                        if (buffer == null) {
                            buffer = new StringBuilder();
                            list.add(new Token(buffer));
                        }
                        buffer.append(ch);
                        break;
                }

                if (value != null) {
                    if (previous != null && previous.getValue() == value) {
                        previous.increment();
                    } else {
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
}