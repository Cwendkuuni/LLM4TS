import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 28).getTime();
        String expected = "2023-02-28T00:00:00Z";
        String result = ISO8601Utils.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDateWithMilliseconds() {
        Date date = new GregorianCalendar(2023, Calendar.FEBRUARY, 28, 12, 30, 45).getTime();
        String expected = "2023-02-28T12:30:45.045Z";
        String result = ISO8601Utils.format(date, true, TimeZone.getDefault());
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullDate() {
        String result = ISO8601Utils.format(null);
        fail("Should throw IllegalArgumentException for null date");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithInvalidTimeZone() {
        String result = ISO8601Utils.format(new Date(), true, new TimeZone("InvalidTimeZone"));
        fail("Should throw IllegalArgumentException for invalid timezone");
    }

    @Test
    public void testParseDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateString = "2023-02-28T12:30:45.045+01:00";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        Date parsedDate = sdf.parse(dateString);
        assertEquals(parsedDate.getTime(), date.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "2023-02-28abc";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw IllegalArgumentException for invalid format");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidYear() throws ParseException {
        String dateString = "abc-0-31";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for invalid year");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidMonth() throws ParseException {
        String dateString = "2023-13-31";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for invalid month");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidDay() throws ParseException {
        String dateString = "2023-02-33";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for invalid day");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTime() throws ParseException {
        String dateString = "2023-02-28T24:60:60";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for invalid time");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingMilliseconds() throws ParseException {
        String dateString = "2023-02-28T12:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for missing milliseconds");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithMissingTimeZone() throws ParseException {
        String dateString = "2023-02-28T12:30:45";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for missing time zone");
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidTimeZoneOffset() throws ParseException {
        String dateString = "2023-02-28T12:30:45.045InvalidTimeZone";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        fail("Should throw ParseException for invalid time zone offset");
    }
}