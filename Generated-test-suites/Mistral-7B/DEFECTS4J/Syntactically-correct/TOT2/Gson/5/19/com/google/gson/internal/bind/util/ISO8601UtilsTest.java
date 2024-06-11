import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_Default() {
        Date date = new GregorianCalendar(2022, 11, 28).getTime();
        String formatted = ISO8601Utils.format(date);
        String expected = "2022-12-28T00:00:00Z";
        Assert.assertEquals(expected, formatted);
    }

    @Test
    public void testFormat_WithMillis() {
        Date date = new GregorianCalendar(2022, 11, 28, 10, 30, 45).getTime();
        String formatted = ISO8601Utils.format(date, true);
        String expected = "2022-12-28T10:30:45.045Z";
        Assert.assertEquals(expected, formatted);
    }

    @Test
    public void testFormat_WithTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 28, 10, 30, 45, 12345).getTime();
        String timeZoneId = "America/Los_Angeles";
        String formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone(timeZoneId));
        long offset = TimeZone.getTimeZone(timeZoneId).getOffset(System.currentTimeMillis());
        String expected = "2022-12-28T15:30:45.045-08:00";
        Assert.assertEquals(expected, formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormat_InvalidTimeZone() {
        Date date = new GregorianCalendar(2022, 11, 28, 10, 30, 45, 12345).getTime();
        String timeZoneId = "InvalidTimeZone";
        ISO8601Utils.format(date, true, TimeZone.getTimeZone(timeZoneId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidFormat() {
        String dateString = "2022-12-28T10:30:45.045-08:00a";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
        Assert.fail("Expected ParseException for invalid format");
    }

    @Test
    public void testParse_ValidFormat() {
        String dateString = "2022-12-28T10:30:45.045Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        Long millis = parsedDate.getTime();
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 11, 28, 10, 30, 45, 45);
        Long expectedMillis = expectedCalendar.getTimeInMillis();
        Assert.assertEquals(expectedMillis, millis);
        Assert.assertEquals(0, pos.getIndex());
    }

    @Test
    public void testParse_WithTimeZone() {
        String dateString = "2022-12-28T10:30:45.045-08:00";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("America/Los_Angeles"));
        Long millis = parsedDate.getTime();
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 11, 28, 15, 30, 45, 45);
        Long expectedMillis = expectedCalendar.getTimeInMillis() - TimeZone.getTimeZone("America/Los_Angeles").getOffset(expectedMillis);
        Assert.assertEquals(expectedMillis, millis);
        Assert.assertEquals(dateString.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidTimeZoneOffset() {
        String dateString = "2022-12-28T10:30:45.045InvalidOffset";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos, TimeZone.getTimeZone("UTC"));
        Assert.fail("Expected ParseException for invalid time zone offset");
    }
}