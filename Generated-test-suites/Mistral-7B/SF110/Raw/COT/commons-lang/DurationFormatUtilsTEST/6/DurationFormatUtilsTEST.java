import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DurationFormatUtilsTest {

    

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "01:46:37.700";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1.736833DT1H46M37.7S";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String expectedResult = "1:46:37.700";
        String format = "H:mm:ss.SSS";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 1:46:37.700";
        format = "d H:mm:ss.SSS";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1:46:38";
        format = "H:mm:ss";
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "2:00:00";
        durationMillis = 0L;
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "0:00:01";
        durationMillis = 1000L;
        result = DurationFormatUtils.formatDuration(durationMillis, format);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithEdgeCases() {
        long durationMillis = Long.MAX_VALUE;
        String format = "s";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            Assert.fail("Expected an IllegalArgumentException for the given format and duration.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        durationMillis = Long.MIN_VALUE;
        format = "s";
        try {
            String result = DurationFormatUtils.formatDuration(durationMillis, format);
            Assert.fail("Expected an IllegalArgumentException for the given format and duration.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        long startMillis = 1630227200000L;
        long endMillis = 1630230800000L;
        String expectedResult = "1 hour 26 minutes";
        String format = "H:mm";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        startMillis = 1630227200000L;
        endMillis = 1630227199999L;
        expectedResult = "0 seconds";
        format = "s";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        startMillis = 1630227200000L;
        endMillis = 1630227200001L;
        expectedResult = "1 millisecond";
        format = "S";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        startMillis = 1630227200000L;
        endMillis = 1630227200001L;
        expectedResult = "1 second";
        format = "s";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        startMillis = 1630227200000L;
        endMillis = 1630227200000L;
        expectedResult = "0 days 0 hours 0 minutes 0 seconds";
        format = "d H:mm:ss";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        TimeZone timezone = TimeZone.getTimeZone("Asia/Kolkata");
        startMillis = System.currentTimeMillis();
        endMillis = startMillis + 30 * 60 * 1000;
        expectedResult = "0 days 0 hours 30 minutes";
        format = "d H:mm";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, false, timezone);
        Assert.assertEquals(expectedResult, result);

        startMillis = System.currentTimeMillis() - 30 * 60 * 1000;
        endMillis = startMillis + 30 * 60 * 1000;
        expectedResult = "1 day 0 hours 0 minutes";
        format = "d H:mm";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, timezone);
        Assert.assertEquals(expectedResult, result);

        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "a", true, timezone);
            Assert.fail("Expected an IllegalArgumentException for the given format.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expectedResult = "1 day 1 hour 46 minutes 37.7 seconds";
        String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 1 hour 46 mins 37.7 secs";
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, true);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1d 1h 46m 37.7s";
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, false);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1d 1h 46m 37.7s";
        result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day";
        durationMillis = 86400000L;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "0 days";
        durationMillis = 0L;
        result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 1630227200000L;
        long endMillis = 1630230800000L;
        String expectedResult = "P1H26M";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 1630227200000L;
        long endMillis = 1630230800000L;
        String expectedResult = "1 hour 26 minutes";
        String format = "H:mm";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 hour 26 minutes 0 seconds";
        format = "H:mm:ss";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 hour 26 minutes 0 seconds";
        format = "H:mm:ss.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "2 hours 46 minutes";
        startMillis = 1630227200000L;
        endMillis = 1630232800000L;
        format = "H:mm";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 2 hours 26 minutes 0 seconds";
        Calendar calendar = new GregorianCalendar(2022, 12, 31);
        startMillis = calendar.getTimeInMillis();
        endMillis = calendar.getTimeInMillis() + 31 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000;
        format = "d H:mm";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 1 hour 26 minutes 59 seconds";
        startMillis = calendar.getTimeInMillis();
        endMillis = calendar.getTimeInMillis() + 31 * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 59 * 1000;
        format = "d H:mm:ss";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        expectedResult = "1 day 1 hour 26 minutes 59 seconds 999 milliseconds";
        format = "d H:mm:ss.SSS";
        result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        Assert.assertEquals(expectedResult, result);

        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "a", true, null);
            Assert.fail("Expected an IllegalArgumentException for the given format.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "P1D", false, null);
            Assert.fail("Expected an IllegalArgumentException for the given format.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "P1D", true, null);
            Assert.fail("Expected an IllegalArgumentException for the given format.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            DurationFormatUtils.formatPeriod(startMillis, endMillis, "P1D", true, Calendar.getInstance());
            Assert.fail("Expected an IllegalArgumentException for the given format.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    
}