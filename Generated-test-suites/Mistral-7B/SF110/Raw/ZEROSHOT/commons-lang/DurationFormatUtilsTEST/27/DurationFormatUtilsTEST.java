package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Tokenizer;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.67";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "PT10H4M37.67S";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithFormat() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.67";
        String format = "H:mm:ss.SSS";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithFormatAndPadWithZeros() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.670";
        String format = "H:mm:ss.SSS";
        boolean padWithZeros = true;
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expectedResult = "10 days 4 hours 37 minutes 37 seconds";
        String suppressLeadingZeroElements = false;
        String suppressTrailingZeroElements = false;
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);

        suppressLeadingZeroElements = true;
        expectedResult = "10 days 4 hours 37 minutes 37 second";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);

        suppressTrailingZeroElements = true;
        expectedResult = "10 days 4 hours 37 minutes 37";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = true;
        expectedResult = "10 days 4 hours 37 minutes 37";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO() throws ParseException {
        long startMillis = 1516336000000L;
        long endMillis = 1516336552000L;
        String expectedResult = "PT5h52m12s";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithFormat() throws ParseException {
        long startMillis = 1516336000000L;
        long endMillis = 1516336552000L;
        String expectedResult = "5 hours 52 minutes 12 seconds";
        String format = "H:mm:ss";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithFormatAndPadWithZeros() throws ParseException {
        long startMillis = 1516336000000L;
        long endMillis = 1516336552000L;
        String expectedResult = "05:05:12.000";
        String format = "HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithFormatAndTimeZone() throws ParseException {
        long startMillis = 1516336000000L;
        long endMillis = 1516336552000L;
        String expectedResult = "PT5h52m12s";
        String format = "PhhmmssSSS";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timeZone);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLexx() {
        String format = "H:mm:ss.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        int expectedSize = 14;
        Assert.assertArrayEquals(new Token[expectedSize], tokens);
        for (int i = 0; i < expectedSize; ++i) {
            final char expectedChar = (i < 13) ? (char)('H' + i) : '.';
            final Token token = tokens[i];
            Assert.assertEquals(new StringBuilder(Character.toString(expectedChar)).toString(), token.getValue().toString());
        }
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

    boolean equals(Token other) {
        if (!(other instanceof Token)) {
            return false;
        }
        final Token otherToken = (Token)other;
        if (this.value.getClass() != otherToken.value.getClass()) {
            return false;
        }
        if (this.count != otherToken.count) {
            return false;
        }
        if (this.value instanceof StringBuilder) {
            return this.value.toString().equals(otherToken.value.toString());
        }
        if (this.value instanceof Number) {
            return this.value.equals(otherToken.value);
        }
        return this.value == otherToken.value;
    }

    int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }
}

class Tokenizer extends Tokenizer {
    @Override
    protected Token makeToken(String s) {
        return new Token(s);
    }
}