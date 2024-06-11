import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L;
        String expectedResult = "10:43:37.356";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationISO() {
        long durationMillis = 1234567L;
        String expectedResult = "P1Y2M10D10H10M43S.356";
        String result = DurationFormatUtils.formatDurationISO(durationMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDuration() {
        long durationMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expectedResult = "1  days 10  hours 43  minutes 37  seconds";
        String result = DurationFormatUtils.formatDuration(durationMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatDurationWithEdgeCasesAndExceptions() {
        testFormatDurationEdgeCasesAndExceptions("formatDuration");
    }

    private void testFormatDurationEdgeCasesAndExceptions(String methodName) {
        long durationMillis = Long.MIN_VALUE;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";

        try {
            String result = methodName.equals("formatDuration") ? DurationFormatUtils.formatDuration(durationMillis, format) : DurationFormatUtils.formatDuration(durationMillis);
            fail("Expected IllegalArgumentException with the message 'Negative duration value'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Negative duration value", e.getMessage());
        }

        durationMillis = Long.MAX_VALUE;

        String result = methodName.equals("formatDuration") ? DurationFormatUtils.formatDuration(durationMillis, format) : DurationFormatUtils.formatDuration(durationMillis);
        String expectedResult = "P" + Integer.toString(DurationFormatUtils.getYearsFromMillis(durationMillis)) + "Y"
                + (DurationFormatUtils.getMonthsFromMillis(durationMillis) > 0 ? "P" + Integer.toString(DurationFormatUtils.getMonthsFromMillis(durationMillis)) + "M" : "")
                + (DurationFormatUtils.getDaysFromMillis(durationMillis) > 0 ? "P" + Integer.toString(DurationFormatUtils.getDaysFromMillis(durationMillis)) + "D" : "")
                + (DurationFormatUtils.getHoursFromMillis(durationMillis) > 0 ? "PT" + Integer.toString(DurationFormatUtils.getHoursFromMillis(durationMillis)) + "H" : "")
                + (DurationFormatUtils.getMinutesFromMillis(durationMillis) > 0 ? "PT" + Integer.toString(DurationFormatUtils.getMinutesFromMillis(durationMillis)) + "M" : "")
                + (DurationFormatUtils.getSecondsFromMillis(durationMillis) > 0 ? "PT" + Integer.toString(DurationFormatUtils.getSecondsFromMillis(durationMillis)) + "S" : "");
        assertEquals(expectedResult, result);

        format = "P";
        try {
            result = methodName.equals("formatDuration") ? DurationFormatUtils.formatDuration(durationMillis, format) : DurationFormatUtils.formatDuration(durationMillis);
            fail("Expected IllegalArgumentException with the message 'Invalid format pattern: 'P'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid format pattern: 'P'", e.getMessage());
        }

        format = "d' days 'H' hours 'm' minutes 's' seconds'";
        try {
            result = methodName.equals("formatDuration") ? DurationFormatUtils.formatDuration(durationMillis, format) : DurationFormatUtils.formatDuration(durationMillis, format);
            fail("Expected IllegalArgumentException with the message 'Unexpected trailing character: '");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Unexpected trailing character: ''", e.getMessage());
        }
    }

    @Test
    public void testFormatDurationWords() {
        long durationMillis = 1234567L;
        String expectedResult = "1 year 2 months 10 days 10 hours 43 minutes 37 seconds";
        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;
        String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodISO() {
        long startMillis = 0L;
        long endMillis = 1234567L;
        String expectedResult = "P1Y2M10D10H10M43S";
        String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriod() {
        long startMillis = 0L;
        long endMillis = 1234567L;
        String format = "d' days 'H' hours 'm' minutes 's' seconds";
        String expectedResult = "1  days 10  hours 43  minutes 37  seconds";
        String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatPeriodWithEdgeCasesAndExceptions() {
        testFormatPeriodEdgeCasesAndExceptions("formatPeriod");
    }

    private void testFormatPeriodEdgeCasesAndExceptions(String methodName) {
        long startMillis = -1L;
        long endMillis = 1234567L;

        try {
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis, endMillis) : DurationFormatUtils.formatPeriod(startMillis, endMillis);
            fail("Expected IllegalArgumentException with the message 'Invalid start time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid start time", e.getMessage());
        }

        startMillis = 1234567L;

        try {
            long durationMillis = Long.MIN_VALUE;
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis, durationMillis) : DurationFormatUtils.formatPeriod(startMillis, durationMillis);
            fail("Expected IllegalArgumentException with the message 'Invalid end time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid end time", e.getMessage());
        }

        try {
            long startMillis2 = 1234567L;
            long endMillis2 = Long.MIN_VALUE;
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis2, endMillis2) : DurationFormatUtils.formatPeriod(startMillis2, endMillis2);
            fail("Expected IllegalArgumentException with the message 'Invalid end time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid end time", e.getMessage());
        }

        try {
            long startMillis3 = -1L;
            long endMillis3 = 1234567L;
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis3, endMillis3) : DurationFormatUtils.formatPeriod(startMillis3, endMillis3);
            fail("Expected IllegalArgumentException with the message 'Invalid start time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid start time", e.getMessage());
        }

        try {
            long startMillis4 = 1234567L;
            long endMillis4 = Long.MAX_VALUE;
            String format = "d' days 'H' hours 'm' minutes 's' seconds";
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis4, endMillis4, format) : DurationFormatUtils.formatPeriod(startMillis4, endMillis4, format);
            fail("Expected IllegalArgumentException with the message 'Invalid end time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid end time", e.getMessage());
        }

        try {
            long startMillis5 = 1234567L;
            long endMillis5 = -1L;
            String format = "d' days 'H' hours 'm' minutes 's' seconds";
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis5, endMillis5, format) : DurationFormatUtils.formatPeriod(startMillis5, endMillis5, format);
            fail("Expected IllegalArgumentException with the message 'Invalid start time'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Invalid start time", e.getMessage());
        }

        try {
            long startMillis6 = 1234567L;
            long endMillis6 = 1234567L;
            String format = "d' days 'H' hours 'm' minutes 's' seconds";
            String result = methodName.equals("formatPeriod") ? DurationFormatUtils.formatPeriod(startMillis6, endMillis6, format) : DurationFormatUtils.formatPeriod(startMillis6, endMillis6, format);
            fail("Expected IllegalArgumentException with the message 'The start time and end time are the same'");
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("The start time and end time are the same", e.getMessage());
        }
    }
}