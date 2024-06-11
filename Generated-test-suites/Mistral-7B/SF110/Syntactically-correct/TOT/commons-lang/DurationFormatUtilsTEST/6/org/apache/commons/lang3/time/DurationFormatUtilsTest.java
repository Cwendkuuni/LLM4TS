package org.apache.commons.lang3.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Test cases for DurationFormatUtils class.
 */
public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "2:12:35.677";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "PT2H12M35.677S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String format = "invalidToken";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567L; // -2 hours, -12 minutes, -35.677 seconds
        String format = "H:mm:ss.SSS";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected NegativeArraySizeException, but got: " + result);
        } catch (NegativeArraySizeException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "2 hours, 12 minutes, 36 seconds"; // extra second due to rounding
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithNegativeDuration() {
        long durationMillis = -1234567L; // -2 hours, -12 minutes, -35.677 seconds
        String expected = "-2 hours, -12 minutes, -36 seconds"; // extra second due to rounding
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedTrailingZeros() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "2 hours, 12 minutes, 35 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDurationWordsWithSuppressedLeadingZeros() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "2 hours, 12 minutes, 35.677 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1499466400000L; // 2017-01-01 00:00:00
        long endMillis = 1499466760000L; // 2017-01-01 02:12:40
        String expected = "PT2H7M40S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatPeriodISOWithInvalidStartOrEndDate() {
        long startMillis = -1L;
        long endMillis = 1234567L;
        try {
            String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        startMillis = 1234567L;
        endMillis = -1L;
        try {
            String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodISOWithInvalidFormat() {
        long startMillis = 1499466400000L; // 2017-01-01 00:00:00
        long endMillis = 1499466760000L; // 2017-01-01 02:12:40
        String format = "invalidToken";
        try {
            String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis, format);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodWithInvalidStartOrEndDate() {
        long startMillis = -1L;
        long endMillis = 1234567L;
        String format = "'P'yyyy'Y'MM'MM'dd'DT'HH'HH'mm'mm'ss'SSS'";
        try {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        startMillis = 1234567L;
        endMillis = -1L;
        try {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1499466400000L; // 2017-01-01 00:00:00
        long endMillis = 1499466760000L; // 2017-01-01 02:12:40
        String format = "invalidToken";
        try {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodWithInvalidPadWithZeros() {
        long startMillis = 1499466400000L; // 2017-01-01 00:00:00
        long endMillis = 1499466760000L; // 2017-01-01 02:12:40
        String format = "'P'yyyy'Y'MM'MM'dd'DT'HH'HH'mm'mm'ss'SSS'";
        try {
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, null);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatPeriodWithTimezone() {
        long startMillis = 1499466400000L; // 2017-01-01 00:00:00
        long endMillis = 1499466760000L; // 2017-01-01 02:12:40
        String format = "'P'yyyy'Y'MM'MM'dd'DT'HH'HH'mm'mm'ss'SSS'";
        String timezoneID = "Europe/London";
        String expected = "PT1H12M40S";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, java.util.TimeZone.getTimeZone(timezoneID));
        assertEquals(expected, result);
    }

    @Test
    public void testParseDurationHMS() throws ParseException {
        String durationString = "2:12:35.677";
        long durationMillis = DurationFormatUtils.parseDurationHMS(durationString);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDurationISO() throws ParseException {
        String durationString = "PT2H12M35.677S";
        long durationMillis = DurationFormatUtils.parseDurationISO(durationString);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDurationWithInvalidFormat() {
        String durationString = "invalidFormat";
        try {
            long durationMillis = DurationFormatUtils.parseDurationISO(durationString);
            fail("Expected ParseException, but got: " + durationMillis);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseDurationWithNegativeDuration() {
        String durationString = "-PT2H12M35.677S";
        long durationMillis = DurationFormatUtils.parseDurationISO(durationString);
        assertEquals(-1234567L, durationMillis);
    }

    @Test
    public void testParseDurationWords() throws ParseException {
        String durationString = "2 hours, 12 minutes, 35.677 seconds";
        long durationMillis = DurationFormatUtils.parseDurationWords(durationString, false, false);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDurationWordsWithSuppressedLeadingZeros() throws ParseException {
        String durationString = "2 hours, 12 minutes, 35.677 seconds";
        long durationMillis = DurationFormatUtils.parseDurationWords(durationString, false, true);
        assertEquals(1234567L, durationMillis);
    }

    @Test
    public void testParseDurationWordsWithSuppressedTrailingZeros() throws ParseException {
        String durationString = "2 hours, 12 minutes, 35.677 seconds";
        long durationMillis = DurationFormatUtils.parseDurationWords(durationString, true, false);
        assertEquals(1234566L, durationMillis);
    }

    @Test
    public void testParseDurationWordsWithInvalidDurationString() {
        String durationString = "invalidFormat";
        try {
            long durationMillis = DurationFormatUtils.parseDurationWords(durationString, false, false);
            fail("Expected ParseException, but got: " + durationMillis);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParsePeriodISO() throws ParseException {
        String durationString = "PT2H12M40S";
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        long durationMillis = DurationFormatUtils.parsePeriodISO(durationString, startCalendar);
        assertEquals(Calendar.getInstance(startCalendar.getTimeZone()), startCalendar);
        assertEquals(Calendar.getInstance(startCalendar.getTimeZone()).add(durationMillis, Calendar.MILLISECOND), endCalendar);
        assertEquals(DurationFormatUtils.formatDurationISO(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis()), durationString);
    }

    @Test
    public void testParsePeriodWithInvalidFormat() {
        String durationString = "invalidFormat";
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        try {
            long durationMillis = DurationFormatUtils.parsePeriodISO(durationString, startCalendar);
            fail("Expected ParseException, but got: " + durationMillis);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParsePeriodWithDifferentTimezones() throws ParseException {
        String durationString = "PT1H12M40S";
        Calendar startCalendar1 = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        Calendar endCalendar1 = Calendar.getInstance(java.util.TimeZone.getTimeZone("Europe/London"));
        long durationMillis1 = DurationFormatUtils.parsePeriodISO(durationString, startCalendar1);
        assertEquals(Calendar.getInstance(startCalendar1.getTimeZone()).add(durationMillis1, Calendar.MILLISECOND), endCalendar1);

        Calendar startCalendar2 = Calendar.getInstance(java.util.TimeZone.getTimeZone("Europe/London"));
        Calendar endCalendar2 = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        long durationMillis2 = DurationFormatUtils.parsePeriodISO(durationString, startCalendar2, java.util.TimeZone.getTimeZone("Europe/London"));
        assertEquals(Calendar.getInstance(startCalendar2.getTimeZone()).add(durationMillis2, Calendar.MILLISECOND), endCalendar2);

        assertEquals(endCalendar1.getTimeInMillis() - startCalendar1.getTimeInMillis(), endCalendar2.getTimeInMillis() - startCalendar2.getTimeInMillis());
    }

    @Test
    public void testFormatDurationWithISOFormat() {
        long durationMillis = 1234567L; // 2 hours, 12 minutes, 35.677 seconds
        String expected = "PT2H12M35.677S";
        String result = DurationFormatUtils.formatDuration(durationMillis, "'P'yyyy'Y'MM'MM'dd'DT'HH'HH'mm'mm'ss'SSS'", false);
        assertEquals(expected, result);
    }
}