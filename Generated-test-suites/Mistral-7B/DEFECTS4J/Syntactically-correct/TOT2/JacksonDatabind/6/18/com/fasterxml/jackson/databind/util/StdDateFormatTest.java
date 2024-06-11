import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 @Test
 public void testInstance() {
 StdDateFormat stdDateFormat = StdDateFormat.instance;
 assertNotNull(stdDateFormat);
 }

 @Test
 public void testParseISO8601Format() throws ParseException {
 Date date = StdDateFormat.instance.parse("2022-03-31T15:30:23.045-05:00");
 assertEquals("Expected correct date", new Date(1648528623000L), date);
 }

 @Test
 public void testParseISO8601FormatWithTimeZone() throws ParseException {
 DateFormat stdDateFormatWithTimeZone = StdDateFormat.instance.withTimeZone(TimeZone.getTimeZone("America/New_York"));
 Date date = stdDateFormatWithTimeZone.parse("2022-03-31T15:30:23.045-05:00");
 assertEquals("Expected correct date in New York timezone", new Date(1648532023000L), date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidISO8601Format() throws ParseException {
 StdDateFormat.instance.parse("InvalidDateString");
 // ParseException should be thrown
 }

 @Test
 public void testParseRFC1123Format() throws ParseException {
 Date date = StdDateFormat.instance.parse("Thu, 31 Mar 2022 15:30:23 EST");
 assertEquals("Expected correct date", new Date(1648532023000L), date);
 }

 @Test
 public void testParsePlainFormat() throws ParseException {
 Date date = StdDateFormat.instance.parse("2022-03-31");
 assertEquals("Expected correct date", new Date(1648507200000L), date);
 }

 @Test
 public void testToStringFormat() {
 Date date = new Date(1648532023000L);
 String formattedDate = StdDateFormat.instance.format(date, new StringBuffer(), null);
 assertEquals("Expected correct formatted date", "Thu Mar 31 15:30:23 EDT 2022", formattedDate.toString());
 }

}