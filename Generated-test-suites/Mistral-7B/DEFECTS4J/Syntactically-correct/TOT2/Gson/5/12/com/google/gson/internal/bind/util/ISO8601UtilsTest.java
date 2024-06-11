import org.junit.Test;
import org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class ISO8601UtilsTest {

    @Test
    public void testFormat_DateWithMilliseconds_ReturnsCorrectlyFormattedString() {
        Date date = new GregorianCalendar(2022, 11, 25, 14, 30, 45).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        String expected = "2022-12-25T14:30:45.045Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithoutMilliseconds_ReturnsCorrectlyFormattedString() {
        Date date = new GregorianCalendar(2022, 11, 25, 14, 30).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        String expected = "2022-12-25T14:30:00Z";
        assertEquals(expected, formattedDate);
    }

    @Test
    public void testFormat_DateWithTimeZone_ReturnsCorrectlyFormattedString() {
        Date date = new GregorianCalendar(2022, 11, 25, 14, 30, 45, 0, TimeZone.getTimeZone("Europe/London").getRawOffset()).getTime();
        String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Europe/London"));
        String expected = "2022-12-25T13:30:45+00:00";
        assertEquals(expected, formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormat_NullDate_ThrowsIllegalArgumentException() {
        String formattedDate = ISO8601Utils.format(null, false, TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testParse_ValidISO8601String_ReturnsCorrectDate() throws ParseException {
        String dateString = "2022-12-25T14:30:45.045Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(dateString, pos);
        GregorianCalendar expectedCalendar = new GregorianCalendar(2022, 11, 25, 14, 30, 45);
        assertEquals(expectedCalendar.getTime(), parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidFormat_ThrowsParseException() throws ParseException {
        String dateString = "2022-12-25T14:30:45Z"; // missing milliseconds digit
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidTimeZoneIndicator_ThrowsParseException() throws ParseException {
        String dateString = "2022-12-25T14:30:45+00:60"; // invalid number of minutes in timezone offset
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(dateString, pos);
    }
}