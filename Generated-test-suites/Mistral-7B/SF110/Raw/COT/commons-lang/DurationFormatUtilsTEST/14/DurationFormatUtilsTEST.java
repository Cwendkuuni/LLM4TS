import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Tokenizer;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567;
        String expectedResult = "10:47:31.345";
        String actualResult = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567;
        String expectedResult = "P2DT10H47M31.345S";
        String actualResult = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithDifferentFormats() {
        long durationMillis = 1234567;
        String format = "HH:mm:ss.SSS";
        String expectedResult = "10:47:31.345";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "mm:ss.SSS";
        expectedResult = "47:31.345";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "ss.SSS";
        expectedResult = "31345";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "HH:mm:ss";
        expectedResult = "10:47:31";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "HH:mm";
        expectedResult = "10:47";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "m:ss.SSS";
        expectedResult = "47:31.345";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "s.SSS";
        expectedResult = "31345";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "s";
        expectedResult = "31345";
        actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithNegativeDuration() {
        long durationMillis = -1234567;
        String format = "HH:mm:ss.SSS";
        String expectedResult = "-10:47:31.345";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatDurationWithZeroDuration() {
        long durationMillis = 0;
        String format = "HH:mm:ss.SSS";
        String expectedResult = "00:00:00.000";
        String actualResult = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationWithInvalidFormat() {
        long durationMillis = 1234567;
        String format = "XX:XX:XX.XXXX";
        DurationFormatUtils.formatDuration(durationMillis, format);
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567;
        String expectedResult = "2 days 10 hours 47 minutes 31 seconds 345 milliseconds";
        String actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        assertEquals(expectedResult, actualResult);

        expectedResult = "2 days 10 hours 47 minutes 31 seconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        assertEquals(expectedResult, actualResult);

        expectedResult = "2 days 10 hours 47 minutes 31 seconds 345 milliseconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        assertEquals(expectedResult, actualResult);

        expectedResult = "2 days 10 hours 47 minutes 31 seconds";
        actualResult = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        long endMillis = 1420071000000L; // 2014-11-11 01:00:00.000
        String expectedResult = "P1DT0H0M";
        String actualResult = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithDifferentFormats() {
        long startMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        long endMillis = 1420071000000L; // 2014-11-11 01:00:00.000
        String format = "d HH:mm:ss";
        String expectedResult = "1 01:00:00";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm:ss";
        expectedResult = "1 01:00:00";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "d HH:mm";
        expectedResult = "1 01:00";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm";
        expectedResult = "1 01:00";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm:ss";
        expectedResult = "1 01:00:00";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm:ss.SSS";
        expectedResult = "1 01:00:00.000";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm:ss.SSS z";
        expectedResult = "1 01:00:00.000 EST";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, Locale.US);
        assertEquals(expectedResult, actualResult);

        format = "dd HH:mm:ss.SSS z";
        expectedResult = "1 01:00:00.000 EST";
        actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, Locale.ENGLISH);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithNegativeDuration() {
        long startMillis = 1420071000000L; // 2014-11-11 01:00:00.000
        long endMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        String format = "d HH:mm:ss";
        String expectedResult = "-1 23:59:60";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatPeriodWithZeroDuration() {
        long startMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        long endMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        String format = "d HH:mm:ss";
        String expectedResult = "0 00:00:00";
        String actualResult = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatPeriodWithInvalidFormat() {
        long startMillis = 1420070400000L; // 2014-11-11 00:00:00.000
        long endMillis = 1420071000000L; // 2014-11-11 01:00:00.000
        String format = "XXX:XXX:XXX.XXXX";
        DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
    }
}

class Token {
    private final Object value;
    private int count;

    public Token(Object value) {
        this.value = value;
        this.count = 1;
    }

    public Token(Object value, int count) {
        this.value = value;
        this.count = count;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token token = (Token) obj;
            if (this.value.getClass() != token.value.getClass()) {
                return false;
            }
            if (this.count != token.count) {
                return false;
            }
            if (this.value instanceof StringBuilder) {
                return this.value.toString().equals(token.value.toString());
            }
            if (this.value instanceof Number) {
                return this.value.equals(token.value);
            }
            return this.value == token.value;
        }
        return false;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return StringUtils.repeat(this.value.toString(), this.count);
    }
}