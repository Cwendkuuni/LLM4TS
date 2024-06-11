import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 private static final String UTC_ID = "UTC";
 private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

 @Test
 public void testFormat() {
     Date date = new Date();

     // Test default format with UTC timezone
     String formattedDateDefault = ISO8601Utils.format(date);
     assertEquals("Expected default format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' for UTC timezone", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedDateDefault);
     assertEquals("Expected default format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' for UTC timezone (value)", date.toString(), ISO8601Utils.parse(formattedDateDefault, new ParsePosition(0)).getTime());

     // Test format with milliseconds precision and UTC timezone
     String formattedDateWithMillis = ISO8601Utils.format(date, true, TIMEZONE_UTC);
     assertEquals("Expected format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' for UTC timezone with milliseconds precision", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", formattedDateWithMillis);
     assertEquals("Expected format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z' for UTC timezone with milliseconds precision (value)", date.toString(), ISO8601Utils.parse(formattedDateWithMillis, new ParsePosition(0)).getTime());

     // Test format with timezone offset
     Date dateWithOffset = new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000); // 10 hours back
     String formattedDateWithOffset = ISO8601Utils.format(dateWithOffset, false, TimeZone.getTimeZone("Europe/Paris"));
     assertEquals("Expected format: yyyy-MM-dd'T'HH:mm:ss'+'hh:mm' for 'Europe/Paris' timezone", "yyyy-MM-dd'T'HH:mm:ss'+02:00'", formattedDateWithOffset);
     assertEquals("Expected format: yyyy-MM-dd'T'HH:mm:ss'+'hh:mm' for 'Europe/Paris' timezone (value)", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(dateWithOffset), ISO8601Utils.parse(formattedDateWithOffset, new ParsePosition(0)).getTime());
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidFormat() {
     String invalidFormatDate = "InvalidFormat";
     ParsePosition pos = new ParsePosition(0);
     ISO8601Utils.parse(invalidFormatDate, pos);
     fail("Expected ParseException for invalid format: "+invalidFormatDate);
 }

 @Test(expected = ParseException.class)
 public void testParseMissingTimeZone() {
     String missingTimeZoneDate = "2022-02-01T12:34:56";
     ParsePosition pos = new ParsePosition(0);
     ISO8601Utils.parse(missingTimeZoneDate, pos);
     fail("Expected ParseException for missing time zone: "+missingTimeZoneDate);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidTimeZone() {
     String invalidTimeZoneDate = "2022-02-01T12:34:56InvalidTimeZone";
     ParsePosition pos = new ParsePosition(0);
     ISO8601Utils.parse(invalidTimeZoneDate, pos);
     fail("Expected ParseException for invalid time zone: "+invalidTimeZoneDate);
 }

}