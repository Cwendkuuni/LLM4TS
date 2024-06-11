package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Token;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:04:37.670";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 86400000L * 2;
        String expectedResult = "PT2D";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithCustomFormat() {
        long durationMillis = 1234567L;
        String customFormat = "dd':'HH':'mm':'ss.SSS";
        String expectedResult = "01:10:04.670";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, customFormat);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithCustomFormatAndPadWithZeros() {
        long durationMillis = 1234567L;
        String customFormat = "dd':'HH':'mm':'ss.SSS";
        String expectedResult = "01:10:04.670";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, customFormat, true);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "1 day 10 hours 4 minutes 4.67 seconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedLeadingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expectedResult = "1 day 10 hours 4 minutes 4.67 seconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedTrailingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day 10 hours 4 minutes 4.67 sec";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWordsWithBothSuppressed() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "1 day 10 hours 4 minutes 4.67";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1534567890000L;
        long endMillis = 1534567900000L;
        String expectedResult = "PT1H";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithCustomFormat() {
        long startMillis = 1534567890000L;
        long endMillis = 1534567900000L;
        String customFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        String expectedResult = "2018-12-31T23:59:10.000+00:00";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, customFormat);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithCustomFormatAndPadWithZeros() {
        long startMillis = 1534567890000L;
        long endMillis = 1534567900000L;
        String customFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        boolean padWithZeros = true;
        String expectedResult = "2018-12-31T23:59:10.000+00:00";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, customFormat, padWithZeros);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithCustomFormatAndTimeZone() {
        long startMillis = 1534567890000L;
        long endMillis = 1534567900000L;
        String customFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String expectedResult = "2018-12-31T23:59:10.000+00:00";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, customFormat, true, timeZone);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithUnmatchedQuote() {
        long startMillis = 1534567890000L;
        long endMillis = 1534567900000L;
        String customFormat = "yyyy-'MM-dd'T'HH:mm:ss.SSSZ";
        String[] expectedMessages = {"Unmatched quote in format: yyyy-'MM-dd'T'HH:mm:ss.SSSZ"};
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, customFormat, true);
        } catch (IllegalArgumentException e) {
            Assert.assertArrayEquals(expectedMessages, e.getStackTrace());
            throw e;
        }
    }

    @Test
    public void testLexx() {
        String format = "y'yyyy'M'MM'd'dd'HH'HH'mm'mm'ss'sss'S'S'";
        Token[] expectedTokens = new Token[] {
                new Token('y'),
                new Token('y'),
                new Token('M'),
                new Token('M'),
                new Token('d'),
                new Token('d'),
                new Token('H'),
                new Token('H'),
                new Token('m'),
                new Token('m'),
                new Token('s'),
                new Token('s'),
                new Token('s')
        };
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<Token> tokenList = new ArrayList<>();
        for (Token token : tokens) {
            tokenList.add(token);
        }
        Assert.assertArrayEquals(expectedTokens, tokenList.toArray(new Token[0]));
    }
}

class Token {
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
        if (!(obj instanceof Token)) {
            return false;
        }
        final Token tok = (Token)obj;
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

    int hashCode() {
        return this.value.hashCode();
    }

    String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }

    static boolean containsTokenWithValue(final Token[] tokens, final Object value) {
        for (int i = 0; i < tokens.length; ++i) {
            if (tokens[i].getValue() == value) {
                return true;
            }
        }
        return false;
    }
}