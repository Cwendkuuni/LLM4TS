import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "02:04:35.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P2DT2H3M35.67S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'";
        String expectedResult = "2 days 2 hours 3 minutes 35 seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long[] durations = new long[]{-1234567, 0, 1234567, Long.MAX_VALUE};
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'";
        for (long durationMillis : durations) {
            String expectedResult = getExpectedFormatDurationResult(durationMillis, format);
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            assertEquals(expectedResult, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567L;
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'abc";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithNullFormat() {
        long durationMillis = 1234567L;
        DurationFormatUtils.formatDuration(durationMillis, null);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String expectedResult = "2 days 2 hours 3 minutes 35 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1497368000000L; // Thu, 26 Jan 2017 00:00:00 GMT
        long endMillis = 1497371200000L; // Thu, 26 Jan 2017 04:40:00 GMT
        String expectedResult = "P1DT3H";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1497368000000L; // Thu, 26 Jan 2017 00:00:00 GMT
        long endMillis = 1497371200000L; // Thu, 26 Jan 2017 04:40:00 GMT
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'";
        String expectedResult = "1 day 3 hours 40 minutes 0 seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithEdgeCases() {
        long startMillis = 0L, endMillis = 0L, startMillisOverflow = 1L * Long.MAX_VALUE;
        long durations[] = new long[]{0L, 1L, 2L, Long.MAX_VALUE};
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'";
        TimeZone timezone = TimeZone.getDefault();
        for (long start = startMillis, end = endMillis; start < durations.length * 2L; start += 2L, end += 2L) {
            startMillis = start;
            endMillis = end;
            String expectedResult = getExpectedFormatPeriodResult(startMillis, endMillis, format, timezone);
            String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone);
            assertEquals(expectedResult, result);
        }
        startMillisOverflow = startMillisOverflow;
        startMillis = 0;
        endMillis = startMillisOverflow;
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1497368000000L; // Thu, 26 Jan 2017 00:00:00 GMT
        long endMillis = 1497371200000L; // Thu, 26 Jan 2017 04:40:00 GMT
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'abc";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithNullFormat() {
        long startMillis = 1497368000000L; // Thu, 26 Jan 2017 00:00:00 GMT
        long endMillis = 1497371200000L; // Thu, 26 Jan 2017 04:40:00 GMT
        DurationFormatUtils.formatPeriod(startMillis, endMillis, null, true, TimeZone.getDefault());
    }

    @Test
    public void testFormatPeriodWithTimeZone() {
        long startMillis = 1497368000000L; // Thu, 26 Jan 2017 00:00:00 GMT
        long endMillis = 1497371200000L; // Thu, 26 Jan 2017 04:40:00 GMT
        String format = "dd' days 'HH' hours 'mm' minutes 'ss' seconds'";
        String timeZoneID = "America/New_York";
        String expectedResult = "1 day 3 hours 40 minutes 0 seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, TimeZone.getTimeZone(timeZoneID));
        assertEquals(expectedResult, result);
    }

    private String getExpectedFormatDurationResult(long durationMillis, String format) {
        Token[] tokens = DurationFormatUtils.lexx(format);
        long days = 0, hours = 0, minutes = 0, seconds = 0, milliseconds = durationMillis;
        int tokenIndex = 0;
        while (tokenIndex < tokens.length && days == 0 && hours == 0 && minutes == 0 && seconds >= 0) {
            Token token = tokens[tokenIndex];
            if (token.getValue() == DurationFormatUtils.d) {
                days = milliseconds / 86400000L;
                milliseconds -= days * 86400000L;
                tokenIndex++;
                continue;
            }
            if (token.getValue() == DurationFormatUtils.H) {
                hours = milliseconds / 3600000L;
                milliseconds -= hours * 3600000L;
                tokenIndex++;
                continue;
            }
            if (token.getValue() == DurationFormatUtils.m) {
                minutes = milliseconds / 60000L;
                milliseconds -= minutes * 60000L;
                tokenIndex++;
                continue;
            }
            if (token.getValue() == DurationFormatUtils.s) {
                seconds = milliseconds / 1000L;
                milliseconds -= seconds * 1000L;
                tokenIndex++;
                continue;
            }
            if (token.getValue() == DurationFormatUtils.S) {
                tokenIndex++;
                continue;
            }
            if (tokenIndex < tokens.length - 1 && tokens[tokenIndex + 1].getValue() == DurationFormatUtils.s) {
                seconds += milliseconds / 1000;
                milliseconds %= 1000;
                tokenIndex++;
                continue;
            }
            tokenIndex++;
        }
        StringBuilder result = new StringBuilder();
        for (int i = tokenIndex; i < tokens.length; ++i) {
            Token token = tokens[i];
            result.append(paddedValue(getDurationValue(token.getValue(), days, hours, minutes, seconds, milliseconds), token.getCount()));
        }
        return result.toString();
    }

    private String getExpectedFormatPeriodResult(long startMillis, long endMillis, String format, TimeZone timeZone) {
        Calendar startCalendar = Calendar.getInstance(timeZone);
        startCalendar.setTime(new Date(startMillis));
        Calendar endCalendar = Calendar.getInstance(timeZone);
        endCalendar.setTime(new Date(endMillis));
        int milliseconds = endCalendar.get(Calendar.MILLISECOND) - startCalendar.get(Calendar.MILLISECOND);
        int seconds = endCalendar.get(Calendar.SECOND) - startCalendar.get(Calendar.SECOND);
        int minutes = endCalendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
        int hours = endCalendar.get(Calendar.HOUR_OF_DAY) - startCalendar.get(Calendar.HOUR_OF_DAY);
        int days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH);
        int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        if (months < 0) {
            years--;
            months += 12;
        }
        if (milliseconds < 0) {
            seconds--;
            milliseconds += 1000;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < format.length(); ++i) {
            char ch = format.charAt(i);
            if (ch == 'y') {
                result.append(paddedValue(years, true, 4));
            } else if (ch == 'M') {
                result.append(paddedValue(months, true, 2));
            } else if (ch == 'd') {
                result.append(paddedValue(days, true, 3));
            } else if (ch == 'H') {
                result.append(paddedValue(hours, true, 2));
            } else if (ch == 'm') {
                result.append(paddedValue(minutes, true, 2));
            } else if (ch == 's') {
                result.append(paddedValue(seconds, true, 2));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private long getDurationValue(Object value, long days, long hours, long minutes, long seconds, long milliseconds) {
        if (value == DurationFormatUtils.d) {
            return days;
        } else if (value == DurationFormatUtils.H) {
            return hours;
        } else if (value == DurationFormatUtils.m) {
            return minutes;
        } else if (value == DurationFormatUtils.s) {
            return seconds;
        } else if (value == DurationFormatUtils.S) {
            return milliseconds;
        }
        return 0;
    }

    private String paddedValue(long value, boolean padWithZeros, int width) {
        return String.format("%" + width + "s", String.valueOf(value)).replace(" ", "0");
    }

}