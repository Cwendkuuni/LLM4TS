import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 String expected = "02:07:17";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 String expected = "PT02H07M17S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithCustomFormat() {
 long durationMillis = 1234567L;
 String format = "HH:mm:ss:SSS";
 String result = DurationFormatUtils.formatDuration(durationMillis, format);
 String expected = "02:07:17:567";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithNegativeDuration() {
 long durationMillis = -1234567L;
 String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss.SSS");
 String expected = "-02:07:17.567";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWithZeroDuration() {
 long durationMillis = 0L;
 String result = DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss.SSS");
 String expected = "00:00:00.000";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 boolean suppressLeadingZeroElements = true;
 boolean suppressTrailingZeroElements = true;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
 String expected = "2 hours, 7 minutes, 17 seconds";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890123L;
 long endMillis = 1434567890123L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 String expected = "PT2H11M57S";
 assertEquals(expected, result);
 }

 @Test
 public void testFormatPeriodWithCustomFormat() {
 long startMillis = 1234567890123L;
 long endMillis = 1434567890123L;
 String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 String expected = "2019-11-06T14:11:57.000";
 assertEquals(expected, result);
 }

}