package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:14.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "P2DT2H7M14.67S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d.HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        String expected = "2d.02:07:14.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithPadding() {
        long durationMillis = 1234567L;
        String format = "d.HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        String expected = "02d.00:07:14.670";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "d.HH:mm:ss.SSD";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "2 days 2 hours 7 minutes 14 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressLeadingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = false;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "2 days 2 hours 7 minutes 14 seconds";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressTrailingZeroElements() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = false;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "2 days 2 hours 7 minutes 14 sec";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithBothSuppressions() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        String expected = "2 days 2 hours 7 minutes 14";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239033000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "P1DT1H52M11S";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239033000L;
        String format = "d.HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        String expected = "1d.01:52:11.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithPadding() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239033000L;
        String format = "d.HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true);
        String expected = "01d.00:52:11.670";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239033000L;
        String format = "d.HH:mm:ss.SSD";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    }

    @Test
    public void testFormatPeriodWithDifferentYear() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239133000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "2017-01-02T02:52:11.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithSameYear() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239233000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "2017-01-02T03:52:11.670";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodWithUnableToResolveMonth() {
        long startMillis = 1494238982000L;
        long endMillis = 1494239333000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unable to resolve month with value 13 for format yyyy-MM-dd'T'HH:mm:ss.SSS";
            assertEquals(expectedMessage, e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1494239333000L;
        long endMillis = 1494238982000L;
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
        String expected = "-1d-23:52:11.670";
        assertEquals(expected, result);
    }

    @Test
    public void testLexx() {
        String format = "d.HH:mm:ss.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        List<String> expected = new ArrayList<String>();
        expected.add("d");
        expected.add(".");
        expected.add("H");
        expected.add(":");
        expected.add("m");
        expected.add(":");
        expected.add("s");
        expected.add(".");
        expected.add("S");
        expected.add("S");
        for (int i = 0; i < tokens.length; ++i) {
            Token token = tokens[i];
            assertEquals(expected.get(i), token.getValue().toString());
        }
    }
}