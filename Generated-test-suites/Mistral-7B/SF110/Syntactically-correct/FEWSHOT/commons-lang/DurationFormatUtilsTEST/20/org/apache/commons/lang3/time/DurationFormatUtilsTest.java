package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        assertEquals("20:47:17.67", DurationFormatUtils.formatDurationHMS(durationMillis));
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        assertEquals("PT20H47M17.67S", DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567;
        assertEquals("20 hours, 47 minutes, 17 seconds, 670 milliseconds", DurationFormatUtils.formatDuration(durationMillis, "h hours, m minutes, s seconds, S milliseconds"));
    }

    @Test
    public void testFormatDurationWithPadWithZeros() {
        long durationMillis = 1234567;
        assertEquals("20:04:47:17.670", DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss:SSS", true));
    }

    @Test
    public void testFormatDurationWithNegativeValues() {
        long durationMillis = -1234567;
        assertEquals("-PT20H47M17.67S", DurationFormatUtils.formatDurationISO(durationMillis));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        DurationFormatUtils.formatDuration(1234567, "X");
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        assertEquals("two hundred and twenty hours, forty-seven minutes, seventeen seconds, six hundred and seventy milliseconds", DurationFormatUtils.formatDurationWords(durationMillis, false, false));
        assertEquals("two hundred and twenty hours, forty-seven minutes, seventeen seconds", DurationFormatUtils.formatDurationWords(durationMillis, true, false));
        assertEquals("two hundred and twenty hours, forty-seven minutes, seventeen seconds", DurationFormatUtils.formatDurationWords(durationMillis, false, true));
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1516044520000L;
        long endMillis = 1516044580000L;
        assertEquals("PT8h", DurationFormatUtils.formatPeriodISO(startMillis, endMillis));
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1516044520000L;
        long endMillis = 1516044580000L;
        assertEquals("8 hours", DurationFormatUtils.formatPeriod(startMillis, endMillis, "h hours"));
    }

    @Test
    public void testFormatPeriodWithPadWithZeros() {
        long startMillis = 1516044520000L;
        long endMillis = 1516044580000L;
        assertEquals("08:00:00", DurationFormatUtils.formatPeriod(startMillis, endMillis, "HH:mm:ss", true));
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1516044520000L;
        long endMillis = 1516044580000L;
        Calendar calendar = Calendar.getInstance();
        assertEquals("08:00:00 +0200", DurationFormatUtils.formatPeriod(startMillis, endMillis, "HH:mm:ss z", true, calendar.getTimeZone()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1516044520000L;
        long endMillis = 1516044580000L;
        DurationFormatUtils.formatPeriod(startMillis, endMillis, "X");
    }

    @Test
    public void testParseDurationHMS() {
        String durationStr = "01:02:03.456";
        long durationMillis = DurationFormatUtils.parseDurationHMS(durationStr);
        assertEquals(1234567, durationMillis);
        durationStr = "01:02:03.456789";
        durationMillis = DurationFormatUtils.parseDurationHMS(durationStr);
        assertEquals(1234567897L, durationMillis);
    }

    @Test
    public void testParseDurationISO() {
        String durationStr = "PT20H47M17.67S";
        long durationMillis = DurationFormatUtils.parseDurationISO(durationStr);
        assertEquals(1234567, durationMillis);
    }

    @Test(expected = ParseException.class)
    public void testParseDurationInvalid() {
        String durationStr = "PT20H47M17.67";
        DurationFormatUtils.parseDurationISO(durationStr);
    }

    @Test(expected = NumberFormatException.class)
    public void testLexxInvalidFormat() {
        String format = "X";
        DurationFormatUtils.lexx(format);
    }

    @Test
    public void testLexx() {
        String format = "HH:mm:ss.SSS";
        Token[] tokens = DurationFormatUtils.lexx(format);
        assertNotNull(tokens);
        assertEquals(13, tokens.length);
        for (int i = 0; i < tokens.length; ++i) {
            Token token = tokens[i];
            assertTrue(token instanceof Token);
            if (i < 13) {
                assertEquals(Character.toString(format.charAt(i)), token.getValue().toString());
            }
            else {
                assertNull(token.getValue());
            }
        }
    }

    @Test
    public void testFormat() {
        Token[] tokens = new Token[] {
                new Token('H'), new Token("HH"),
                new Token('m'), new Token("mm"),
                new Token('s'), new Token("ss"),
                new Token('S'), new Token("SSS")
        };
        long years = 2018;
        long months = 10;
        long days = 30;
        long hours = 23;
        long minutes = 59;
        long seconds = 59;
        long milliseconds = 123;
        String formatString = "%d years, %02d months, %02d days, %02d hours, %02d minutes, %02d seconds, %03d milliseconds";
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("20");
        expectedValues.add("10");
        expectedValues.add("30");
        expectedValues.add("23");
        expectedValues.add("59");
        expectedValues.add("59");
        expectedValues.add("59");
        expectedValues.add("123");
        for (String value : expectedValues) {
            StringBuilder valueBuilder = new StringBuilder(value);
            tokens[0].setValue(new StringBuilder(valueBuilder.toString()));
            String duration = DurationFormatUtils.format(tokens, years, months, days, hours, minutes, seconds, milliseconds, true);
            assertEquals("20 years, 10 months, 30 days, 23 hours, 59 minutes, 59 seconds, 123 milliseconds", duration);
        }
    }
}