import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new GregorianCalendar(2022, 11, 15, 14, 30, 45, 123).getTime();
        String formattedDate = ISO8601Utils.format(date);
        String expected = "2022-12-15T14:30:45.123Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormatDateWithoutMilliseconds() {
        Date date = new GregorianCalendar(2022, 11, 15, 14, 30).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String expected = "2022-12-15T14:30:00Z";
        Assert.assertEquals(expected, formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateInvalidDate() {
        Date date = null;
        String formattedDate = ISO8601Utils.format(date);
    }

    @Test
    public void testParseDate() throws ParseException {
        String dateString = "2022-12-15T14:30:45.123Z";
        Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 11, 15, 14, 30, 45, 123);
        Assert.assertEquals(expectedCalendar.getTime(), parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseDateInvalidFormat() throws ParseException {
        String dateString = "2022-12-15-14:30:45Z";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDateNullString() throws ParseException {
        String dateString = null;
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test
    public void testParseDateTimezoneUTC() throws ParseException {
        String dateString = "2022-12-15T14:30:45.123Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(TimeZone.getTimeZone("UTC").getID(), parsedDate.getTimezoneOffset(), 0);
    }

    @Test
    public void testParseDateTimezoneOther() throws ParseException {
        String dateString = "2022-12-15T14:30:45.123-05:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Assert.assertEquals(-18000000, parsedDate.getTimezoneOffset(), 0); // 5 hours * 60 minutes * 60 seconds * 1000 milliseconds
    }
}