import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate_Basic() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("Unexpected format output for date: " + date, "yyyy-MM-ddThh:mm:ssZ", formattedDate.substring(0, 23));
 }

 @Test
 public void testFormatDate_WithMilliseconds() {
 Date date = new Date();
 String formattedDate = ISO8601Utils.format(date, true);
 assertEquals("Unexpected format output for date with milliseconds: " + date, "yyyy-MM-ddThh:mm:ss.sssZ", formattedDate);
 }

 @Test
 public void testFormatDate_WithCustomTimezone() {
 Date date = new Date();
 String timeZoneID = "America/Los_Angeles";
 TimeZone customTimeZone = TimeZone.getTimeZone(timeZoneID);
 String formattedDate = ISO8601Utils.format(date, false, customTimeZone);
 assertEquals("Unexpected format output for date with custom timezone: " + date, "yyyy-MM-ddThh:mm:ssZ", formattedDate.substring(0, 23));
 assertEquals("Unexpected timezone indicator for date with custom timezone: " + timeZoneID, timeZoneID, formattedDate.substring(23));
 }

 @Test
 public void testParseDate_Basic() throws ParseException {
 String dateString = "2022-12-31T12:00:00Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Unexpected output for parsing a basic date string: " + dateString, "Sat Dec 31 00:00:00 GMT", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz").format(parsedDate));
 assertEquals("Unexpected parse position after parsing a basic date string: " + dateString, dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseDate_WithMilliseconds() throws ParseException {
 String dateString = "2022-12-31T12:00:01.001Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Unexpected output for parsing a date string with milliseconds: " + dateString, "Sat Dec 31 12:00:01 GMT", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz").format(parsedDate));
 assertEquals("Unexpected parse position after parsing a date string with milliseconds: " + dateString, dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseDate_WithCustomTimezone() throws ParseException {
 String dateString = "2022-12-31T12:00:00-08:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals("Unexpected output for parsing a date string with custom timezone: " + dateString, "Sat Dec 31 12:00:00 PST", new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz").format(parsedDate));
 assertEquals("Unexpected parse position after parsing a date string with custom timezone: " + dateString, dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseDate_InvalidFormat() throws ParseException {
 String dateString = "2022-12-31T12:00:00-08";
 ParsePosition pos = new ParsePosition(0);
 try {
 ISO8601Utils.parse(dateString, pos);
 fail("Expected ParseException for invalid format: " + dateString);
 } catch (ParseException e) {
 // Expected
 }
 }
}