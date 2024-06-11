import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

 private static final long DURATION_MILLIS_1 = 15 * 60 * 60 * 1000; // 15 hours
 private static final long DURATION_MILLIS_2 = 1 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 60 * 1000; // 1 hour, 30 minutes, 45 seconds
 private static final long DURATION_MILLIS_3 = 1 * 60 * 60 * 1000 * 24; // 1 day
 private static final long DURATION_MILLIS_4 = 2 * 60 * 60 * 1000 * 24; // 2 days
 private static final long DURATION_MILLIS_5 = 3 * 60 * 60 * 1000 * 24; // 3 days
 private static final long DURATION_MILLIS_6 = 4 * 60 * 60 * 1000 * 24; // 4 days

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = DURATION_MILLIS_1;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "15:00:00.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = DURATION_MILLIS_1;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "PT15H";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = DURATION_MILLIS_1;
 String format = "d' days 'H' hours 'm' minutes 's' seconds";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "1 days 15 hours 0 minutes 0 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithEdgeCases() {
 long durationMillisNegative = -(DURATION_MILLIS_1 + 1);
 String formatNegative = "d' days 'H' hours 'm' minutes 's' seconds";
 String resultNegative = DurationFormatUtils.formatDuration(durationMillisNegative, formatNegative);
 String expectedNegative = "-2 days -16 hours -1 hours 0 minutes 0 seconds";
 assertEquals(expectedNegative, resultNegative);

 long durationMillisZero = 0;
 String resultZero = DurationFormatUtils.formatDuration(durationMillisZero, format);
 String expectedZero = "0 days 0 hours 0 minutes 0 seconds";
 assertEquals(expectedZero, resultZero);

 long durationMillisLong = Long.MAX_VALUE;
 String resultLong = DurationFormatUtils.formatDuration(durationMillisLong, format);
 String expectedLong = "YEARS_NUM numerical_value_greater_than_INT_MAX_value days 0 hours 0 minutes 0 seconds";
 assertEquals(expectedLong, resultLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 String format = "d' days 'H' hours 'm' minutes 's' seconds 'S";
 DurationFormatUtils.formatDuration(0, format);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = DURATION_MILLIS_2;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 String expected = "1 hour, 30 minutes, 45 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 15 * 60 * 60 * 1000; // 15 hours
 long endMillis = 2 * 60 * 60 * 1000 * 24 + 15 * 60 * 60 * 1000; // 2 days + 15 hours
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "PT2DPT15H";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 15 * 60 * 60 * 1000; // 15 hours
 long endMillis = 2 * 60 * 60 * 1000 * 24 + 15 * 60 * 60 * 1000; // 2 days + 15 hours
 String format = "d' days 'H' hours 'm' minutes 's' seconds";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "2 days 15 hours 0 minutes 0 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithEdgeCases() {
 long startMillisNegative = -(2 * 60 * 60 * 1000 * 24 + 15 * 60 * 60 * 1000);
 long endMillisNegative = -(10 * 60 * 60 * 1000);
 String formatNegative = "d' days 'H' hours 'm' minutes 's' seconds";
 String resultNegative = DurationFormatUtils.formatPeriod(startMillisNegative, endMillisNegative, formatNegative);
 String expectedNegative = "-3 days -1 hour, -30 minutes, -15 seconds";
 assertEquals(expectedNegative, resultNegative);

 long startMillisZero = 0;
 long endMillisZero = 0;
 String resultZero = DurationFormatUtils.formatPeriod(startMillisZero, endMillisZero, format);
 String expectedZero = "0 days 0 hours 0 minutes 0 seconds";
 assertEquals(expectedZero, resultZero);

 long startMillisLong = Long.MAX_VALUE;
 long endMillisLong = Long.MAX_VALUE;
 String resultLong = DurationFormatUtils.formatPeriod(startMillisLong, endMillisLong, format);
 String expectedLong = "YEARS_NUM1 numerical_value_greater_than_INT_MAX_value YEARS_NUM2 numerical_value_greater_than_INT_MAX_value days 0 hours 0 minutes 0 seconds";
 assertEquals(expectedLong, resultLong);
 }

}