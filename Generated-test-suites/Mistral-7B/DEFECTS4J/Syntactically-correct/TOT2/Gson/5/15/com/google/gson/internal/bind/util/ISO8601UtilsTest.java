import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 private static String formatDate(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
 ISO8601Utils.format(new GregorianCalendar(timeZone, Locale.US, year, month, day, hour, minute, second, millisecond).getTime(), false, timeZone);
 }

 @Test
 public void testFormatUTC() {
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
 Date date = calendar.getTime();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2000-01-01T00:00:00Z", formattedDate);
 }

 @Test
 public void testFormatDateWithTimezone() {
 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Kolkata"));
 Date date = calendar.getTime();
 String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("Asia/Kolkata"));
 assertEquals("2000-01-01T05:30:00+05:30", formattedDate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullDate() {
 String formattedDate = ISO8601Utils.format(null);
 fail("Formatting a null date should throw an IllegalArgumentException");
 }

 @Test
 public void testParseDate() throws ParseException {
 String iso8601Date = "2000-01-01T00:00:00Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(iso8601Date, pos);
 assertEquals("Sat Jan 01 00:00:00 UTC 2000", parsedDate.toString());
 }

 @Test(expected = ParseException.class)
 public void testParseDateInvalidFormat() throws ParseException {
 String iso8601Date = "2000-01-01T00:00:00XXX";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(iso8601Date, pos);
 fail("Parsing an ISO-8601 formatted string with invalid character 'X' should throw a ParseException");
 }

 @Test(expected = ParseException.class)
 public void testParseNullDateString() throws ParseException {
 String iso8601Date = null;
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(iso8601Date, pos);
 fail("Parsing a null ISO-8601 formatted string should throw a ParseException");
 }

 @Test
 public void testParseDateUTC() throws ParseException {
 String iso8601Date = "2000-01-01T00:00:00Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(iso8601Date, pos);
 assertEquals("Sat Jan 01 00:00:00 UTC 2000", parsedDate.toString());
 }

 @Test
 public void testParseDateWithTimezone() throws ParseException {
 String iso8601Date = "2000-01-01T05:30:00+05:30";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(iso8601Date, pos);
 assertEquals("Sat Jan 01 05:30:00 IST 2000", parsedDate.toString());
 }

 @Test
 public void testParseDateWithTimezoneName() throws ParseException {
 String iso8601Date = "2000-01-01T05:30:00+05:30";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(iso8601Date, pos, "Asia/Kolkata");
 assertEquals("Sat Jan 01 05:30:00 IST 2000", parsedDate.toString());
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidTimezone() throws ParseException {
 String iso8601Date = "2000-01-01T05:30:00+05:30";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(iso8601Date, pos, "InvalidTimezone");
 fail("Parsing with an invalid timezone name should throw a ParseException");
 }

}