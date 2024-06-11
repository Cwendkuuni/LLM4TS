package org.apache.commons.lang3.time;

import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "11:04:37.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1Y1M1T11H4M37.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d HH:mm:ss";
        String expectedResult = "1 11:04:37";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);

        format = "d-HH:mm:ss.SSS";
        expectedResult = "1-11:04:37.670";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithPadZeros() {
        long durationMillis = 1234567L;
        String format = "d HH:mm:ss";
        String expectedResult = "1 01:04:37";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expectedResult = "1 day 11 hours 4 minutes and 37 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1d 11h 4m 37s";
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 11 hours 4 minutes";
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1d 11h 4m";
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890000L;
        long endMillis = 1234567901234L;
        String expectedResult = "P1Y1M1T1H1M34.234S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1234567890000L;
        long endMillis = 1234567901234L;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        String expectedResult = "2017-03-28 12:01:34.234";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        format = "yyyy-MM-dd HH:mm:ss";
        expectedResult = "2017-03-28 12:01:34";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithPadZeros() {
        long startMillis = 1234567890000L;
        long endMillis = 1234567901234L;
        String format = "yyyy-MM-dd HH:mm:ss";
        String expectedResult = "2017-03-28 01:01:34";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1234567890000L;
        long endMillis = 1234567901234L;
        String format = "yyyy-MM-dd HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String expectedResult = "2017-03-28 12:01:34";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "2017-03-28 13:01:34";
        timeZone = TimeZone.getTimeZone("Europe/Madrid");
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormat() {
        long years = 1;
        long months = 2;
        long days = 3;
        long hours = 4;
        long minutes = 5;
        long seconds = 6;
        long milliseconds = 7;
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        String expectedResult = "2018-04-03 05:06:07.007";
        String result = DurationFormatUtils.format(new Object[]{years, months, days, hours, minutes, seconds, milliseconds}, format);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLexx() {
        String format = "y-MM-dd HH:mm:ss.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);

        List<Token> list = Arrays.asList(tokens);
        Assert.assertEquals(14, list.size());
        Assert.assertTrue(list.contains(new Token("y")));
        Assert.assertTrue(list.contains(new Token("M")));
        Assert.assertTrue(list.contains(new Token("d")));
        Assert.assertTrue(list.contains(new Token("H")));
        Assert.assertTrue(list.contains(new Token("m")));
        Assert.assertTrue(list.contains(new Token("s")));
        Assert.assertTrue(list.contains(new Token("S")));
        Assert.assertTrue(list.contains(new Token(new StringBuilder(""))));
        Assert.assertTrue(list.contains(new Token(new StringBuilder("d"))));
        Assert.assertTrue(list.contains(new Token(new StringBuilder("HH:"))));
        Assert.assertTrue(list.contains(new Token(new StringBuilder("mm:"))));
        Assert.assertTrue(list.contains(new Token(new StringBuilder("ss.SSS"))));

        for (int i = 0; i < list.size(); ++i) {
            Token token1 = list.get(i);
            for (int j = i + 1; j < list.size(); ++j) {
                Token token2 = list.get(j);
                if (token1.getValue() == token2.getValue()) {
                    Assert.assertEquals(token1.getCount(), token2.getCount());
                } else if (token1.getValue() instanceof StringBuilder && token2.getValue() instanceof StringBuilder) {
                    StringBuilder str1 = (StringBuilder)token1.getValue();
                    StringBuilder str2 = (StringBuilder)token2.getValue();
                    Assert.assertEquals(str1.length(), str2.length());
                    for (int k = 0; k < str1.length(); ++k) {
                        char c1 = str1.charAt(k);
                        char c2 = str2.charAt(k);
                        Assert.assertEquals(c1, c2);
                    }
                }
            }
        }
    }

    private static class Token {
        private final Object value;
        private int count;

        public Token(Object value) {
            this(value, 1);
        }

        public Token(Object value, int count) {
            this.value = value;
            this.count = count;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Token) {
                Token token = (Token) obj;
                if (this.value.getClass() != token.value.getClass()) {
                    return false;
                }
                if (this.count != token.count) {
                    return false;
                }
                if (this.value instanceof StringBuilder) {
                    StringBuilder str1 = (StringBuilder) this.value;
                    StringBuilder str2 = (StringBuilder) token.value;
                    for (int i = 0; i < str1.length(); ++i) {
                        if (str1.charAt(i) != str2.charAt(i)) {
                            return false;
                        }
                    }
                    if (str1.length() != str2.length()) {
                        return false;
                    }
                } else if (this.value instanceof Number) {
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
}