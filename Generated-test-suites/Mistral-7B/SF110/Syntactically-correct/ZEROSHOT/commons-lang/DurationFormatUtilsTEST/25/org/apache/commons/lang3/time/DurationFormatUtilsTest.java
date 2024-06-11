package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import org.junit.Test;

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
        long durationMillis = 1234567890L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P1Y1M1W1D2H7M17S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890L;
        String format = "d H:mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "1 01:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567890L;
        String format = "d H:mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "01 01:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration_NegativeDuration() {
        long durationMillis = -1234567890L;
        String format = "d H:mm:ss";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "-01 -01:07:17";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "1 year, 1 month, 1 week, 1 day, 2 hours, 7 minutes, 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppression() {
        long durationMillis = 1234567890L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "1 year, 1 month, 1 week, 1 day, 2 hours, 17 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1556214400000L; // 2019-03-15 00:00:00 Z
        long endMillis = 1556218000000L; // 2019-03-15 02:00:00 Z
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P0Y0M0DT2H0M0S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1556214400000L; // 2019-03-15 00:00:00 Z
        long endMillis = 1556218000000L; // 2019-03-15 02:00:00 Z
        String format = "d H:mm:ss";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1 2:00:00";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1556218000000L; // 2019-03-15 02:00:00 Z
        long endMillis = 1556214400000L; // 2019-03-15 00:00:00 Z
        String format = "d H:mm:ss";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "-1 2:00:00";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithTokenList() throws ParseException {
        long startMillis = 1556214400000L; // 2019-03-15 00:00:00 Z
        long endMillis = 1556218000000L; // 2019-03-15 02:00:00 Z
        String format = "P{'d'}D{'H'}H:{'m'}M:{'s'}S";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startMillis));
        Date startDate = calendar.getTime();
        calendar.setTime(new Date(endMillis));
        Date endDate = calendar.getTime();
        List<Token> tokens = DurationFormatUtils.lexx(format);
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, tokens);
        String expected = "P1D2H";
        assertEquals(expected, result);
    }
}

class Token {
    private Object value;
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
        return value;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Token token = (Token) obj;
        if (value.getClass() != token.value.getClass()) {
            return false;
        }
        if (value instanceof StringBuilder) {
            return ((StringBuilder) value).toString().equals(token.value.toString());
        }
        if (value instanceof Number) {
            return value.equals(token.value);
        }
        return value == token.value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return StringUtils.repeat(value.toString(), count);
    }
}