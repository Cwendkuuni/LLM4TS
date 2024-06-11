import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StdDateFormatTest {

 @Test
 public void testParseISO8601Date() throws ParseException {
 StdDateFormat sdf = new StdDateFormat();
 Date date = sdf.parse("2022-01-01T12:34:56.789Z");
 assertEquals("Incorrect date parsing", new Date(1641431296000L), date);
 }

 @Test
 public void testParsePlainDate() throws ParseException {
 StdDateFormat sdf = new StdDateFormat();
 Date date = sdf.parse("2022-01-01");
 assertEquals("Incorrect date parsing", new Date(1640995200000L), date);
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidDate() throws ParseException {
 StdDateFormat sdf = new StdDateFormat();
 sdf.parse("InvalidDateFormat");
 }

 @Test
 public void testParseRFC1123Date() throws ParseException {
 StdDateFormat sdf = new StdDateFormat();
 Date date = sdf.parse("Sat, 01 Jan 2022 12:34:56 GMT");
 assertEquals("Incorrect date parsing", new Date(1641431296000L), date);
 }

 @Test
 public void testParseWithCustomTimeZone() throws ParseException {
 StdDateFormat sdf = new StdDateFormat(TimeZone.getTimeZone("Asia/Kolkata"), Locale.ENGLISH);
 Date date = sdf.parse("2022-01-01T12:34:56.789Z");
 assertEquals("Incorrect date parsing", new Date(1641431296000L + 18000 * 60 * 60 * 1000), date); // IST is UTC+5:30
 }

 @Test
 public void testFormatDate() {
 StdDateFormat sdf = new StdDateFormat();
 String dateString = sdf.format(new Date(1641431296000L), new StringBuilder(), null);
 assertEquals("Incorrect date formatting", "2022-01-01T12:34:56.789Z", dateString);
 }

 @Test
 public void testFormatDateCustomLocale() {
 StdDateFormat sdf = new StdDateFormat(Locale.FRENCH);
 String dateString = sdf.format(new Date(1641431296000L), new StringBuilder(), null);
 assertEquals("Incorrect date formatting", "01-jan.-2022\t12:34:56.789", dateString);
 }
}