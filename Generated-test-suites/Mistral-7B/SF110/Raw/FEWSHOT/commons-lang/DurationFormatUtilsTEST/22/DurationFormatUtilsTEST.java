package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.cli.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.Token;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "PT1D PT10H PT43M PT3S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "HH:mm:ss.SSS";
        String expected = "1d 10h 43m 3s.67s";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567890L;
        String format = "HH:mm:ss.SSS";
        String expected = "1d 10h 43m 03s.670s";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567890L;
        String format = "HH:mm:ss.SSSXX";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "1 day 10 hours 43 minutes and 3 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String expected = "1d 10h 43m 3s";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String expected = "1d 10h 43m 3s";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithBothSuppressZeroElements() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expected = "1d 10h 43s";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String expected = "PT2D PT23H";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String expected = "2013-01-25T23:01:30.123";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithZeroPadding() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String expected = "2013-01-25T23:01:30.123Z";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithNullTimezone() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String expected = "2013-01-25T23:01:30.123Z";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithWrongTimezoneClass() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String expected = "2013-01-25T23:01:30.123+00:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, new Date());
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithTimezone() throws ParseException {
        long startMillis = 1234567890123L;
        long endMillis = 1357946360123L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS z";
        String expected = "2013-01-25T23:01:30.123-05:00";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, new GregorianCalendar(2013, 0, 25, 23, 1, 30, 123).getTimeZone());
        assertEquals(expected, actual);
    }

    @Test
    public void testLexx() {
        String format = "HH:mm:ss.SSS yyyy-MM-dd'T'HH:mm:ss z";
        Token[] expectedTokens = new Token[] {
                new Token("HH"), new Token(":"), new Token("mm"), new Token(":"), new Token("ss"), new Token("."), new Token("SSS"), new Token("yyyy"), new Token("-"), new Token("MM"), new Token("-"), new Token("dd"), new Token("T"), new Token("HH"), new Token(":"), new Token("mm"), new Token(":"), new Token("ss"), new Token("."), new Token("SSS"), new Token("z")
        };
        Token[] actualTokens = DurationFormatUtils.lexx(format);
        assertArrayEquals(expectedTokens, actualTokens);
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

    void increment() {
        ++this.count;
    }

    int getCount() {
        return this.count;
    }

    Object getValue() {
        return this.value;
    }

    boolean equals(final Object obj2) {
        if (!(obj2 instanceof Token)) {
            return false;
        }
        final Token tok2 = (Token)obj2;
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

    @Override
    public String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }
}