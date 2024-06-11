package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Tokens;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "PT1Y1M1W2D1H4M33.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L;
        String expected = "-1:46:37.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithLongDuration() {
        long durationMillis = Long.MAX_VALUE;
        String expected = "P2147483647D";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x";
        long durationMillis = 1234567L;
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWithZeroPadded() {
        long durationMillis = 15L;
        String expected = "0:00:15";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expected = "1 day 1 hour 46 minutes 37 seconds 670 milliseconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWordsWithNegativeDuration() {
        long durationMillis = -1234567L;
        String expected = "-1 day -1 hour -46 minutes -37 seconds -670 milliseconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String expected = "PT5S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISOWithNegativeDuration() {
        long startMillis = 1234567895L;
        long endMillis = 1234567890L;
        String expected = "PT-5S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISOWithLongDuration() {
        long startMillis = 1234567890123L;
        long endMillis = 1234567891234L;
        String expected = "PT11S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String expected = "PT5S";
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithInvalidTimeZoneID() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String expected = "PT5S";
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "InvalidTimeZoneID";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid timezone ID: " + timeZoneID, e.getMessage());
        }
    }

    @Test
    public void testFormatDurationWithInvalidFormatAndPadWithZeros() {
        long durationMillis = 1234567L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x";
        boolean padWithZeros = true;
        try {
            DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: 'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x'", e.getMessage());
        }
    }

    @Test
    public void testFormatDurationWithValidFormatAndInvalidMillis() {
        long durationMillis = -1;
        String format = "HH:mm:ss";
        boolean padWithZeros = true;
        try {
            DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        } catch (IllegalArgumentException e) {
            assertEquals("Negative duration millis: -1", e.getMessage());
        }
    }

    @Test
    public void testFormatDurationWithValidFormatAndZeroMillis() {
        long durationMillis = 0;
        String format = "HH:mm:ss";
        boolean padWithZeros = true;
        String expected = "00:00:00";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithValidFormat() {
        long durationMillis = 1234567L;
        String format = "HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "18:41:17.670";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithValidFormatAndNegativeMillis() {
        long durationMillis = -1234567L;
        String format = "HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "-18:41:17.670";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWithValidFormatAndLongMillis() {
        long durationMillis = Long.MAX_VALUE;
        String format = "HH:mm:ss.SSS";
        boolean padWithZeros = true;
        String expected = "99:59:59.999";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithValidFormatAndInvalidStartMillis() {
        long startMillis = -1;
        long endMillis = 1234567895L;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        } catch (IllegalArgumentException e) {
            assertEquals("Negative start duration millis: -1", e.getMessage());
        }
    }

    @Test
    public void testFormatPeriodWithValidFormatAndInvalidEndMillis() {
        long startMillis = 1234567890L;
        long endMillis = -1;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        } catch (IllegalArgumentException e) {
            assertEquals("Negative end duration millis: -1", e.getMessage());
        }
    }

    @Test
    public void testFormatPeriodWithValidFormatAndZeroMillis() {
        long startMillis = 1234567890L;
        long endMillis = 1234567890L;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        String expected = "PT0S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithValidFormat() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        String expected = "PT5S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithValidFormatAndLongDuration() {
        long startMillis = 1234567890123L;
        long endMillis = 1234567891234L;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        String expected = "PT11S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithValidFormatAndNegativeDuration() {
        long startMillis = 1234567895L;
        long endMillis = 1234567890L;
        String format = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;
        String timeZoneID = "UTC";
        String expected = "PT-5S";
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1234567890L;
        long endMillis = 1234567895L;
        String format = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x";
        String timeZoneID = "UTC";
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, format, timeZoneID);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: 'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'x'", e.getMessage());
        }
    }
}

class Token {
    private final Object value;
    private int count;

    // Getters and setters

    static boolean containsTokenWithValue(final Token[] tokens, final Object value) {
        for (Token token : tokens) {
            if (token.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    // Other methods
}

class TestSTART {
}

class TestEND {
}