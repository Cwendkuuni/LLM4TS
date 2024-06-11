package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expected = "1:46:17.67";
        String actual = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567890L;
        String expected = "P5Y11M22D18H12M51.67S";
        String actual = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567890123L;
        String format = "d h m s.SSS";
        String expected = "22 18 51.67";
        String actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        format = "d h m s S";
        expected = "22 18 51 675";
        actual = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expected, actual);

        format = "d h m s.SSS";
        expected = "22d 18h 51m 67.67s";
        actual = DurationFormatUtils.formatDuration(durationMillis, format, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567890123L;
        String expected = "22 days 18 hours 51 minutes 67.67 seconds";
        String actual = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expected, actual);

        expected = "22d 18h 51m 67.67s";
        actual = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expected, actual);

        expected = "22 days";
        actual = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1535552000000L;
        long endMillis = 1535553234000L;
        String expected = "P11DT1H22M34S";
        String actual = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriod() throws ParseException {
        long startMillis = 1535552000000L;
        long endMillis = 1535553234000L;
        String format = "d H m s.SSS";
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date startDate = new Date(startMillis);
        Date endDate = new Date(endMillis);
        calendar.setTime(startDate);

        int years = calendar.get(Calendar.YEAR) - 2019;
        int months = calendar.get(Calendar.MONTH) + 1;
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        String expected = String.format("%d years %d months %d days %d hours %d minutes %d seconds.%d milliseconds", years, months,
                days, hours, minutes, seconds, milliseconds);
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expected, actual);

        format = "d h m s S";
        expected = String.format("%d years %d months %d days %d hours %d minutes %d seconds %d milliseconds", years, months, days,
                hours, minutes, seconds, milliseconds);
        actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithWrappedMonths() throws ParseException {
        long startMillis = 1535552000000L;
        long endMillis = 1535553234000L;
        String format = "M d H m s S";
        Calendar startCalendar = Calendar.getInstance(TimeZone.getDefault());
        startCalendar.setTime(new Date(startMillis));
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.setTime(new Date(endMillis));

        int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        int days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
        int hours = endCalendar.get(Calendar.HOUR_OF_DAY) - startCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = endCalendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
        int seconds = endCalendar.get(Calendar.SECOND) - startCalendar.get(Calendar.SECOND);
        int milliseconds = endCalendar.get(Calendar.MILLISECOND) - startCalendar.get(Calendar.MILLISECOND);

        int totalMonths = years * 12 + months;
        List<Integer> monthsList = new ArrayList<>();
        while (totalMonths > 0) {
            int monthsToAdd = Math.min(12, totalMonths);
            totalMonths -= monthsToAdd;
            monthsList.add(monthsToAdd);
        }
        monthsList.add(months);

        String expected = "";
        for (Integer month : monthsList) {
            if (expected.length() > 0) {
                expected += " ";
            }
            expected += String.valueOf(month);
        }
        expected += " days " + days + " hours " + hours + " minutes " + minutes + " seconds " + seconds + "."
                + milliseconds;
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatPeriodWithNegativeDurations() throws ParseException {
        long startMillis = 1535553234000L;
        long endMillis = 1535552000000L;
        String format = "M d H m s S";
        Calendar startCalendar = Calendar.getInstance(TimeZone.getDefault());
        startCalendar.setTime(new Date(startMillis));
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.setTime(new Date(endMillis));

        int years = startCalendar.get(Calendar.YEAR) - endCalendar.get(Calendar.YEAR);
        int months = startCalendar.get(Calendar.MONTH) - endCalendar.get(Calendar.MONTH);
        int days = startCalendar.get(Calendar.DAY_OF_MONTH) - endCalendar.get(Calendar.DAY_OF_MONTH);
        int hours = startCalendar.get(Calendar.HOUR_OF_DAY) - endCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = startCalendar.get(Calendar.MINUTE) - endCalendar.get(Calendar.MINUTE);
        int seconds = startCalendar.get(Calendar.SECOND) - endCalendar.get(Calendar.SECOND);
        int milliseconds = startCalendar.get(Calendar.MILLISECOND) - endCalendar.get(Calendar.MILLISECOND);

        int totalMonths = years * 12 + months;
        List<Integer> monthsList = new ArrayList<>();
        while (totalMonths > 0) {
            int monthsToSubtract = Math.min(12, totalMonths);
            totalMonths -= monthsToSubtract;
            monthsList.add(-monthsToSubtract);
        }
        monthsList.add(-months);

        String expected = "";
        for (Integer month : monthsList) {
            if (expected.length() > 0) {
                expected += " ";
            }
            expected += String.valueOf(month);
        }
        expected += " days " + days + " hours " + hours + " minutes " + minutes + " seconds " + seconds + "."
                + milliseconds;
        String actual = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, TimeZone.getDefault());
        assertEquals(expected, actual);
    }

    private static class Token {
        private final Object value;
        private int count;

        private Token(Object value) {
            this.value = value;
            this.count = 1;
        }

        private Token(Object value, int count) {
            this.value = value;
            this.count = count;
        }

        private boolean equals(Token other) {
            if (this.value.getClass() != other.value.getClass()) {
                return false;
            }
            if (this.count != other.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(other.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(other.value);
            }
            return this.value == other.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Token)) {
                return false;
            }
            Token other = (Token)obj;
            return this.equals(other);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return StringUtils.repeat(this.value.toString(), this.count);
        }
    }
}

class TestDurationFormatUtils {

    static {
        DurationFormatUtils.y = "y";
        DurationFormatUtils.M = "M";
        DurationFormatUtils.d = "d";
        DurationFormatUtils.H = "H";
        DurationFormatUtils.m = "m";
        DurationFormatUtils.s = "s";
        DurationFormatUtils.S = "S";
    }
}