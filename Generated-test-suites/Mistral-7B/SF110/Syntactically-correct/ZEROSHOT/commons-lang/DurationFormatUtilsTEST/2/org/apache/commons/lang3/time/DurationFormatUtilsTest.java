import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

 private long createDurationMillis(int years, int months, int days, int hours, int minutes, int seconds, int milliseconds) {
 long duration = 0;
 duration += years * 31536000000L;
 duration += months * 2678400000L;
 duration += days * 86400000L;
 duration += hours * 3600000L;
 duration += minutes * 60000L;
 duration += seconds * 1000L;
 duration += milliseconds;
 return duration;
 }

 @Test
 public void testFormatDurationHMS() {
 long duration = createDurationMillis(0, 0, 2, 3, 4, 5, 678);
 String formatted = DurationFormatUtils.formatDurationHMS(duration);
 assertEquals("02:03:04.678", formatted);
 }

 @Test
 public void testFormatDurationISO() {
 long duration = createDurationMillis(1, 2, 3, 4, 5, 6, 789);
 String formatted = DurationFormatUtils.formatDurationISO(duration);
 assertEquals("P1Y2M3DT4H5M6.789S", formatted);
 }

 @Test
 public void testFormatDurationWithPadZeros() {
 long duration = createDurationMillis(1, 1, 2, 3, 4, 5, 6);
 String formatted = DurationFormatUtils.formatDuration(duration, "d:HH:mm:ss.SSS", true);
 assertEquals("01:01:02:03:04:05.006", formatted);
 }

 @Test
 public void testFormatDurationWithoutPadZeros() {
 long duration = createDurationMillis(1, 1, 2, 3, 4, 5, 6);
 String formatted = DurationFormatUtils.formatDuration(duration, "d:HH:mm:ss.SSS", false);
 assertEquals("1:1:2:3:4:5.6", formatted);
 }

 @Test
 public void testFormatDurationWords() {
 long duration = createDurationMillis(365, 0, 0, 0, 0, 0, 0);
 String formatted = DurationFormatUtils.formatDurationWords(duration, false, false);
 assertEquals("1 year", formatted);
 }

 @Test
 public void testFormatPeriodISO() {
 long start = 1483228800000L; // 2017-01-01 00:00:00.000
 long end = 1483275200000L; // 2017-01-02 00:00:00.000
 String formatted = DurationFormatUtils.formatPeriodISO(start, end);
 assertEquals("PT1D", formatted);
 }

 @Test
 public void testFormatPeriod() {
 long start = 1483228800000L; // 2017-01-01 00:00:00.000
 long end = 1483275200000L; // 2017-01-02 00:00:00.000
 String format = "d 'day' H:mm";
 String formatted = DurationFormatUtils.formatPeriod(start, end, format);
 assertEquals("1 day 23:00", formatted);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 String format = "x:X";
 try {
 DurationFormatUtils.formatDuration(0, format, true);
 } catch (IllegalArgumentException e) {
 return; // Test passed
 }
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatWithLiteral() { 
 String format = "'x':'X'";
 try {
 DurationFormatUtils.formatDuration(0, format, true);
 } catch (IllegalArgumentException e) {
 return; // Test passed
 }
 fail("Expected IllegalArgumentException");
 }
}