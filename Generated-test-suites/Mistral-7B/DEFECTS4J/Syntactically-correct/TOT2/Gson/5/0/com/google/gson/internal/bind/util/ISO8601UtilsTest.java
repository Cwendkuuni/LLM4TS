import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

 // Test for format method with default timezone and no milliseconds precision
 @Test
 public void testFormatDefaultTimezone() {
 Date date = new GregorianCalendar(2022, 11, 25).getTime();
 String expected = "2022-12-25";
 String result = ISO8601Utils.format(date);
 assertEquals(expected, result);
 }

 // Test for format method with milliseconds precision and GMT timezone
 @Test
 public void testFormatGMTWithMillis() {
 Date date = new GregorianCalendar(2022, 11, 25, 12, 30, 45).getTime();
 String expected = "2022-12-25T12:30:45.045Z";
 String result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
 assertEquals(expected, result);
 }

 // Test for parse method with valid ISO-8601 formatted string
 @Test
 public void testParseValidISODate() throws ParseException {
 String dateString = "2022-12-25T12:30:45.045Z";
 ParsePosition position = new ParsePosition(0);
 Date date = ISO8601Utils.parse(dateString, position);
 assertEquals(new GregorianCalendar(2022, 11, 25, 12, 30, 45, 0).getTime(), date.getTime());
 }

 // Test for parse method with valid ISO-8601 formatted string with timezone offset
 @Test
 public void testParseISODateWithOffset() throws ParseException {
 String dateString = "2022-12-25T12:30:45.045-01:00";
 ParsePosition position = new ParsePosition(0);
 Date date = ISO8601Utils.parse(dateString, position);
 assertEquals(new GregorianCalendar(2022, 11, 25, 11, 30, 45, 0).getTime(), date.getTime());
 }

 // Test for parse method with invalid ISO-8601 formatted string
 @Test(expected = ParseException.class)
 public void testParseInvalidISODate() throws ParseException {
 String dateString = "2022-12-25T12:30:45.045X"; // X is invalid timezone indicator
 ParsePosition position = new ParsePosition(0);
 ISO8601Utils.parse(dateString, position);
 }

 // Test for parse method with ISO-8601 formatted string missing time component
 @Test(expected = IllegalArgumentException.class)
 public void testParseISODateMissingTimeComponent() throws ParseException {
 String dateString = "2022-12-25";
 ParsePosition position = new ParsePosition(0);
 ISO8601Utils.parse(dateString, position);
 }

 // Test for parse method with ISO-8601 formatted string missing timezone indicator
 @Test(expected = IllegalArgumentException.class)
 public void testParseISODateMissingTimezoneIndicator() throws ParseException {
 String dateString = "2022-12-25T12:30:45.045";
 ParsePosition position = new ParsePosition(0);
 ISO8601Utils.parse(dateString, position);
 }

 // Test for parse method with ISO-8601 formatted string missing milliseconds precision
 @Test
 public void testParseISODateMissingMillis() throws ParseException {
 String dateString = "2022-12-25T12:30:45Z";
 ParsePosition position = new ParsePosition(0);
 Date date = ISO8601Utils.parse(dateString, position);
 assertEquals(new GregorianCalendar(2022, 11, 25, 12, 30, 0).getTime(), date.getTime());
 }
}