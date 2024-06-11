package org.apache.commons.lang3.time.test;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.67";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expected = "PT10H4M37.672S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.67";
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, result);

        long durationMillisWithNegativeValue = -1234567L;
        String formatNegative = "m:ss.SSS";
        String resultNegative = DurationFormatUtils.formatDuration(durationMillisWithNegativeValue, formatNegative);
        assertEquals("-38:04:37.67", resultNegative);
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567L;
        String expected = "10:04:37.670";
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format, true);
        assertEquals(expected, result);

        long durationMillisWithZeroValue = 0L;
        String formatZero = "H:mm:ss.SSS";
        String resultZero = DurationFormatUtils.formatDuration(durationMillisWithZeroValue, formatZero, true);
        assertEquals("00:00:00.000", resultZero);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "2 days 10 hours 4 minutes 37 seconds 670 milliseconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);

        long durationMillisWithNegativeValue = -1234567L;
        String formatNegative = true;
        String expectedNegative = "2 days 10 hours 4 minutes 37 seconds 670 milliseconds";
        String resultNegative = DurationFormatUtils.formatDurationWords(durationMillisWithNegativeValue, false, formatNegative);
        assertEquals(expectedNegative, resultNegative);

        long durationMillisWithLeadingZeros = 11234567L;
        String formatLeadingZeros = true;
        String formatLeadingZerosWithNegative = true;
        String expectedLeadingZeros = "3 days 11 hours 23 minutes 45 seconds 670 milliseconds";
        String resultLeadingZeros = DurationFormatUtils.formatDurationWords(durationMillisWithLeadingZeros, false, formatLeadingZeros);
        assertEquals(expectedLeadingZeros, resultLeadingZeros);

        String resultLeadingZerosNegative = DurationFormatUtils.formatDurationWords(durationMillisWithNegativeValue, false, formatLeadingZerosWithNegative);
        assertEquals(expectedNegative, resultLeadingZerosNegative);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        String expected = "PT2M2H11D";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        String expected = "2 minutes 2 hours 11 days";
        String format = "m:ss H:mm d";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, result);

        long startMillisNegative = -1497228800000L;
        long endMillisNegative = -1518230400000L;
        String formatNegative = "d h:mm m";
        String resultNegative = DurationFormatUtils.formatPeriod(startMillisNegative, endMillisNegative, formatNegative);
        assertEquals("11 days 2 hours 2 minutes", resultNegative);

        long startMillisZero = 0L;
        long endMillisZero = 0L;
        String formatZero = "d h:mm m";
        String resultZero = DurationFormatUtils.formatPeriod(startMillisZero, endMillisZero, formatZero);
        assertEquals("0 days 0 hours 0 minutes", resultZero);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        List<String> expectedList = Arrays.asList("PT2M2H11D", "2 minutes 2 hours 11 days");
        String timeZone = "UTC";
        String format = "P";
        for (String expected : expectedList) {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getTimeZone(timeZone));
            assertEquals(expected, result);
        }

        List<String> expectedListWithFormat = Arrays.asList("PT2M2H11D", "2 minutes 2 hours 11 days");
        String formatWithTimeZone = "P h:mm a z";
        for (String expected : expectedListWithFormat) {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, formatWithTimeZone, true, getTimeZone(timeZone));
            assertEquals(expected, result);
        }
    }

    @Test
    public void testFormatPeriodWithNegativeValue() {
        long startMillis = -1497228800000L;
        long endMillis = -1518230400000L;
        List<String> expectedList = Arrays.asList("PT2M2H11D", "11 days 2 hours 2 minutes");
        String format = "P";
        for (String expected : expectedList) {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
            assertEquals(expected, result);
        }

        List<String> expectedListWithFormat = Arrays.asList("PT2M2H11D", "11 days 2 hours 2 minutes");
        String formatWithTimeZone = "P h:mm a z";
        for (String expected : expectedListWithFormat) {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, formatWithTimeZone, true, getDefaultTimeZone());
            assertEquals(expected, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        String format = "PXXX";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithNullFormat() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        String format = null;
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithNullTimeZone() {
        long startMillis = 1497228800000L;
        long endMillis = 1518230400000L;
        TimeZone timeZone = null;
        String format = "P";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, timeZone);
    }

    @Test(expected = ParseException.class)
    public void testFormatPeriodWithInvalidDuration() {
        long startMillis = 1497228800000L;
        long endMillis = -1L;
        String format = "P";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidStartDate() {
        long startMillis = -1L;
        long endMillis = 1518230400000L;
        String format = "P";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidEndDate() {
        long startMillis = 1497228800000L;
        long endMillis = -1L;
        String format = "P";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, getDefaultTimeZone());
    }

    @Test
    public void testLexx() {
        String format = "y-M-d'T'H:mm:ss.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        int expectedSize = 8;
        int actualSize = tokens.length;
        assertEquals(expectedSize, actualSize);

        int indexOfY = -1;
        int indexOfM = -1;
        int indexOfD = -1;
        int indexOfTH = -1;
        int indexOfH = -1;
        int indexOfm = -1;
        int indexOfS = -1;
        int indexOfSS = -1;
        for (int i = 0; i < actualSize; ++i) {
            Token token = tokens[i];
            Object value = token.getValue();
            switch (value) {
                case DurationFormatUtils.y:
                    indexOfY = i;
                    break;
                case DurationFormatUtils.M:
                    indexOfM = i;
                    break;
                case DurationFormatUtils.d:
                    indexOfD = i;
                    break;
                case DurationFormatUtils.T:
                    indexOfTH = i;
                    break;
                case DurationFormatUtils.H:
                    indexOfH = i;
                    break;
                case DurationFormatUtils.m:
                    indexOfm = i;
                    break;
                case DurationFormatUtils.s:
                    indexOfS = i;
                    break;
                case DurationFormatUtils.S:
                    indexOfSS = i;
                    break;
            }
        }

        assertEquals(indexOfY, 0);
        assertEquals(indexOfM, 1);
        assertEquals(indexOfD, 2);
        assertEquals(indexOfTH, 3);
        assertEquals(indexOfH, 4);
        assertEquals(indexOfm, 5);
        assertEquals(indexOfS, 6);
        assertEquals(indexOfSS, 7);
    }

    private Calendar getDefaultTimeZone() {
        return Calendar.getInstance();
    }

    private Calendar getTimeZone(String timeZone) {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZoneObj = TimeZone.getTimeZone(timeZone);
        calendar.setTimeZone(timeZoneObj);
        return calendar;
    }
}