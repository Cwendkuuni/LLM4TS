import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

 private static Token[] createTokens(String format) {
 return DurationFormatUtils.lexx(format);
 }

 @Test
 public void testFormatDurationHMS() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationHMS(durationMillis);
 assertEquals("02:01:17", result);
 }

 @Test
 public void testFormatDurationISO() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationISO(durationMillis);
 assertEquals("PT2H1M17S", result);
 }

 @Test
 public void testFormatDuration() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDuration(durationMillis, "d H:m:s");
 assertEquals("2d 02:01:17", result);
 }

 @Test
 public void testFormatDurationWithPaddings() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDuration(durationMillis, "d H:m:s.SSS", true);
 assertEquals("2d 02:01:17.000", result);
 }

 @Test
 public void testFormatDurationWords() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, false, false);
 assertEquals("2 days 2 hours 1 minute 17 seconds", result);
 }

 @Test
 public void testFormatDurationWordsWithSuppression() {
 long durationMillis = 1234567L;
 String result = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
 assertEquals("2 days 2 hours 1 min 17 secs", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDurationInvalidFormat() {
 String format = "d H:m:s:s";
 Token[] tokens = createTokens(format);
 Long durationMillis = 1234567L;
 DurationFormatUtils.formatDuration(durationMillis, tokens, 0L, 0L, 0L, 0L, 0L, 0L, true);
 }

 @Test
 public void testFormatPeriodISO() {
 long startMillis = 1234567890L;
 long endMillis = 1337896543L;
 String result = DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
 assertEquals("PT1Y3M18D19H31M54S", result);
 }

 @Test
 public void testFormatPeriod() {
 long startMillis = 1234567890L;
 long endMillis = 1337896543L;
 String format = "y M d H:m:s.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
 assertEquals("2012 03 19 15:52:34.123", result);
 }

 @Test
 public void testFormatPeriodWithPaddings() {
 long startMillis = 1234567890L;
 long endMillis = 1337896543L;
 String format = "y M d H:m:s.SSS";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
 assertEquals("2012 03 19 15:52:34.123", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatPeriodInvalidFormat() {
 long startMillis = 1234567890L;
 long endMillis = 1337896543L;
 String format = "y M d H:m:s:s";
 String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, format, true, null);
 }
}