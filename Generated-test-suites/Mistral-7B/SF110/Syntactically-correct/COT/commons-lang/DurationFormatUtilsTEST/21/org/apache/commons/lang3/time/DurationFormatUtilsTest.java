package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurationFormatUtilsTest {

    @ org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "PT10H4M37.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "dd:HH:mm:ss.SSS";
        String expected = "02:10:04:37.670";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        long durationMillisNegative = -1234567L;
        String expectedNegative = "-PT02H10M04S.670";
        String actualNegative = DurationFormatUtils.formatDuration(durationMillisNegative, format);
        assertEquals(expectedNegative, actualNegative);
    }

    @ org.junit.Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 1234567L;
        String format = "dd:HH:mm:ss.SSS";
        String expected = "02:10:04:37.670";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String expected = "2 days 10 hours 4 minutes 37 seconds 670 milliseconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actual);

        suppressLeadingZeroElements = true;
        expected = "2 days 10 hours 4 minutes 37 seconds";
        String actualSuppressLeadingZeroElements = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actualSuppressLeadingZeroElements);

        suppressTrailingZeroElements = true;
        expected = "2 days 10 hours 4 minutes 37 seconds";
        String actualSuppressTrailingZeroElements = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actualSuppressTrailingZeroElements);

        suppressLeadingZeroElements = true;
        suppressTrailingZeroElements = true;
        expected = "2 days 10 hours 4 minutes 37 seconds";
        String actualSuppressBoth = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expected, actualSuppressBoth);
    }

    @ org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890123L;
        long endMillis = 1337123456789L;
        String expected = "PT13M17S24.687";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testFormatPeriod() {
        long startMillis = 1234567890123L;
        long endMillis = 1337123456789L;
        String format = "dd.MM.yyyy'T'HH:mm:ss.SSS";
        String expected = "13.12.2022T12:17:24.687";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);

        String formatWithTimeZone = "dd.MM.yyyy'T'HH:mm:ss.SSS'Z'";
        String timeZone = "America/Los_Angeles";
        String expectedWithTimeZone = "13.12.2022T19:17:24.687-08:00";
        String actualWithTimeZone = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getTimeZone(timeZone));
        assertEquals(expectedWithTimeZone, actualWithTimeZone);
    }

    @ org.junit.Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1337123456789L;
        long endMillis = 1234567890123L;
        String format = "dd.MM.yyyy'T'HH:mm:ss.SSS";
        String expected = "PT-13M-17S-24.687";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);
    }

    @ org.junit.Test
    public void testLexxFormat() {
        String format = "dd.MM.yyyy'T'HH:mm:ss.SSS'Z'";
        Token[] tokens = DurationFormatUtils.lexx(format);

        List<Token> tokensList = new ArrayList<Token>(Arrays.asList(tokens));
        assertEquals(12, tokensList.size());
        assertEquals(Token.class, tokensList.get(0).getClass());
        assertEquals(Token.class, tokensList.get(1).getClass());
        assertEquals(Token.class, tokensList.get(2).getClass());
        assertEquals(Token.class, tokensList.get(3).getClass());
        assertEquals(Token.class, tokensList.get(4).getClass());
        assertEquals(Token.class, tokensList.get(5).getClass());
        assertEquals(Token.class, tokensList.get(6).getClass());
        assertEquals(Token.class, tokensList.get(7).getClass());
        assertEquals(Token.class, tokensList.get(8).getClass());
        assertEquals(Token.class, tokensList.get(9).getClass());
        assertEquals(Token.class, tokensList.get(10).getClass());
        assertEquals(Token.class, tokensList.get(11).getClass());

        assertEquals("dd", tokensList.get(0).getValue().toString());
        assertEquals(".", tokensList.get(1).getValue().toString());
        assertEquals("MM", tokensList.get(2).getValue().toString());
        assertEquals(".", tokensList.get(3).getValue().toString());
        assertEquals("yyyy", tokensList.get(4).getValue().toString());
        assertEquals("T", tokensList.get(5).getValue().toString());
        assertEquals("HH", tokensList.get(6).getValue().toString());
        assertEquals(":", tokensList.get(7).getValue().toString());
        assertEquals("mm", tokensList.get(8).getValue().toString());
        assertEquals(":", tokensList.get(9).getValue().toString());
        assertEquals("ss.SSS", tokensList.get(10).getValue().toString());
        assertEquals("Z", tokensList.get(11).getValue().toString());
    }
}