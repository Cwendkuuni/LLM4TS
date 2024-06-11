package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

public class DurationFormatUtilsTest {

    @org.junit.Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        String expected = "02:07:17";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        String expected = "PT2H7M17S";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationWithZeroPadding() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss", true);
        String expected = "02:07:17";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatDurationNegative() {
        long durationMillis = -1234567L;
        String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss", true);
        String expected = "-02:-07:-17";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatDurationInvalidFormat() {
        long durationMillis = 1234567L;
        DurationFormatUtils.formatDuration(durationMillis, "X:mm:ss", true);
    }

    @org.junit.Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        String expected = "2 hours, 7 minutes, 17 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodISO() {
        long startMillis = 1420070400000L;
        long endMillis = 1420071000000L;
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        String expected = "PT56M0S";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriod() {
        long startMillis = 1420070400000L;
        long endMillis = 1420071000000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d 'days' H 'hours' m 'minutes' s 'seconds'");
        String expected = "1 day, 1 hour, 20 minutes, 0 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodInvalidFormat() {
        long startMillis = 1420070400000L;
        long endMillis = 1420071000000L;
        DurationFormatUtils.formatPeriod(startMillis, endMillis, "X:d 'days' H 'hours' m 'minutes' s 'seconds'");
    }

    @org.junit.Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1420070400000L;
        long endMillis = 1420071000000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d 'days' H 'hours' m 'minutes' s 'seconds'", true, java.util.TimeZone.getTimeZone("UTC"));
        String expected = "1 day, 1 hour, 20 minutes, 0 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testFormatPeriodWithInvalidYear() {
        long startMillis = 1234567890000L;
        long endMillis = 1234567899000L;
        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "yyyy 'years' M 'months' d 'days' H 'hours' m 'minutes' s 'seconds'", true, java.util.TimeZone.getDefault());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1420071000000L;
        long endMillis = 1420070400000L;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "d 'days' H 'hours' m 'minutes' s 'seconds'", true, java.util.TimeZone.getDefault());
        String expected = "-1 day, -1 hour, -20 minutes, -0 seconds";
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testLexx() {
        String format = "HH:mm:ss,SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        assertEquals(10, tokens.length);
        for (int i = 0; i < 10; i++) {
            if (i < 2) {
                assertNotNull(tokens[i].getValue());
                assertEquals("H", tokens[i].getValue());
                assertEquals(1, tokens[i].getCount());
            } else if (i == 2) {
                assertNotNull(tokens[i].getValue());
                assertEquals("m", tokens[i].getValue());
                assertEquals(2, tokens[i].getCount());
            } else if (i == 3) {
                assertNotNull(tokens[i].getValue());
                assertEquals("s", tokens[i].getValue());
                assertEquals(2, tokens[i].getCount());
            } else if (i == 4) {
                assertNotNull(tokens[i].getValue());
                assertEquals("s", tokens[i].getValue());
                assertEquals(3, tokens[i].getCount());
            } else {
                assertNull(tokens[i].getValue());
                assertEquals(0, tokens[i].getCount());
            }
        }
    }

}