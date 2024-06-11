import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

 @Test
 public void testFormatDateWithDefaultTimeZone() {
 Date date = new GregorianCalendar(2022, 11, 30).getTime();
 String formattedDate = ISO8601Utils.format(date);
 assertEquals("2022-12-30T00:00:00Z", formattedDate);
 }

 @Test
 public void testFormatDateWithCustomTimeZone() {
 Date date = new GregorianCalendar(2022, 11, 30, 12, 30, 45, 123, TimeZone.getTimeZone("America/New_York")).getTime();
 String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
 assertEquals("2022-12-30T17:30:45.123-05:00", formattedDate);
 }

 @Test
 public void testParseDateWithDefaultTimeZone() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123-05:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 30, 12, 30, 45, 123, TimeZone.getTimeZone("America/New_York")).getTime(), parsedDate);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidFormat() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123Invalid";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithMissingYear() throws ParseException {
 String dateString = "12-30T17:30:45.123-05:00";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithMissingMonth() throws ParseException {
 String dateString = "2022-30T17:30:45.123-05:00";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithMissingDay() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123-05:00";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithMissingTime() throws ParseException {
 String dateString = "2022-12-30";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithMissingTimeZone() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test(expected = ParseException.class)
 public void testParseDateWithInvalidTimeZone() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123InvalidTimeZone";
 ParsePosition pos = new ParsePosition(0);
 ISO8601Utils.parse(dateString, pos);
 }

 @Test
 public void testParseDateWithZeroTimeZoneOffset() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123Z";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 30, 17, 30, 45, 123, TimeZone.getTimeZone("UTC")).getTime(), parsedDate);
 }

 @Test
 public void testParseDateWithPositiveTimeZoneOffset() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123+03:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 30, 20, 30, 45, 123, TimeZone.getTimeZone("Europe/London")).getTime(), parsedDate);
 }

 @Test
 public void testParseDateWithNegativeTimeZoneOffset() throws ParseException {
 String dateString = "2022-12-30T17:30:45.123-05:00";
 ParsePosition pos = new ParsePosition(0);
 Date parsedDate = ISO8601Utils.parse(dateString, pos);
 assertEquals(new GregorianCalendar(2022, 11, 30, 12, 30, 45, 123, TimeZone.getTimeZone("America/New_York")).getTime(), parsedDate);
 }

}