import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDate() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 12, 13, 45, 60, 999).getTime();
 String expected = "2022-12-12T13:45:60.999Z";
 String actual = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDateWithoutMillis() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 12).getTime();
 String expected = "2022-12-12T00:00:00Z";
 String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatDateWithInvalidTimeZone() throws Exception {
 Date date = new GregorianCalendar(2022, 11, 12).getTime();
 String expected = "2022-12-12T00:00:00+00:00";
 String actual = ISO8601Utils.format(date, false, new TimeZone("InvalidTimeZone"));
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatDateWithNullDate() throws Exception {
 String actual = ISO8601Utils.format(null, false, TimeZone.getTimeZone("UTC"));
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testParseDate() throws Exception {
 String dateString = "2022-12-12T13:45:60.999Z";
 ParsePosition pos = new ParsePosition(0);
 Date date = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 12, 13, 45, 60, 999).getTime(), date.getTime());
 assertEquals(dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseDateWithoutMillis() throws Exception {
 String dateString = "2022-12-12T00:00:00Z";
 ParsePosition pos = new ParsePosition(0);
 Date date = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 12).getTime(), date.getTime());
 assertEquals(dateString.length(), pos.getIndex());
 }

 @Test
 public void testParseDateWithInvalidFormat() throws Exception {
 String dateString = "2022-12-12T13:45:60.999AA";
 ParsePosition pos = new ParsePosition(0);
 try {
 ISO8601Utils.parse(dateString, pos);
 fail("Expected ParseException but got: null");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseDateWithNullString() throws Exception {
 try {
 ISO8601Utils.parse(null, new ParsePosition(0));
 fail("Expected IllegalArgumentException but got: null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}