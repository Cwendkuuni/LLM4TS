import org.junit.Test;
import java.text.ParseException;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

 private static final String UTC_ID = "UTC";
 private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

 @Test
 public void testFormatDate() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 assertRegularExpression(expectedFormat, formattedDate);
 }

 @Test
 public void testFormatDateWithMilliseconds() {
 Date date = new Date();
 boolean millis = true;
 String formattedDate = ISO8601Utils.format(date, millis, TIMEZONE_UTC);
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
 assertRegularExpression(expectedFormat, formattedDate);
 }

 @Test
 public void testFormatDateWithTimeZone() {
 Date date = new Date();
 TimeZone tz = TimeZone.getTimeZone("Europe/London");
 String formattedDate = ISO8601Utils.format(date, false, tz);
 String expectedFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
 assertRegularExpression(expectedFormat, formattedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatInvalidDate() {
 Date invalidDate = null;
 String formattedDate = ISO8601Utils.format(invalidDate, false, TIMEZONE_UTC);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testParseValidDate() throws ParseException {
 Date expectedDate = new Date(1516239020000L); // 01-06-2018 10:00:00
 String dateString = "2018-06-01T10:00:00Z";
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(expectedDate.getTime(), parsedDate.getTime());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateFormat() throws ParseException {
 String invalidDateString = "2018-06-01T10:00:00E";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (NumberFormatException) For input string: \"2018-06-01T10:00:00E\"",
         result.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateNoT() throws ParseException {
 String invalidDateString = "2018-06-01-10:00:00Z";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (ParseException) Invalid format: \"2018-06-01-10:00:00Z\"",
         result.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateNoYear() throws ParseException {
 String invalidDateString = "06-01-2018T10:00:00Z";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (ParseException) Invalid format: \"06-01-2018T10:00:00Z\"",
         result.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateNoYearMonth() throws ParseException {
 String invalidDateString = "T10:00:00Z";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (ParseException) Invalid format: \"T10:00:00Z\"",
         result.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateNoColon() throws ParseException {
 String invalidDateString = "2018-06-01T100000Z";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (ParseException) Invalid format: \"2018-06-01T100000Z\"",
         result.getMessage());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDateTimeZone() throws ParseException {
 String invalidDateString = "2018-06-01T10:00:00-05:00";
 TryParseResult result = ISO8601Utils.parse(invalidDateString, new ParsePosition(0));
 assertEquals("Failed to parse date ["+invalidDateString+"]: (ParseException) Invalid format: \"2018-06-01T10:00:00-05:00\"",
         result.getMessage());
 }

 @Test
 public void testParseValidDateWithTimeZone() throws ParseException {
 String dateString = "2018-06-01T10:00:00+01:00";
 Date expectedDate = new Date(1528133200000L); // 01-06-2018 10:00:00 CET
 Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
 assertEquals(expectedDate.getTime(), parsedDate.getTime());
 }

 private static class TryParseResult {
 String message;
 TryParseResult(String message) {
 this.message = message;
 }
 }

 private static void assertRegularExpression(String regex, String value) {
 Pattern pattern = Pattern.compile(regex);
 Matcher matcher = pattern.matcher(value);
 assertTrue("Regular expression matched: " + regex, matcher.matches());
 }

}