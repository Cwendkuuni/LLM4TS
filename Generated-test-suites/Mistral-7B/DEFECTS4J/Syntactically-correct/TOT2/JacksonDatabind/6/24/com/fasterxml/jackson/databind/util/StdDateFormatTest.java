import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class StdDateFormatTest {

    @Test
    public void testStdDateFormatInstance() {
        StdDateFormat stdDateFormat = StdDateFormat.instance;
        assertNotNull(stdDateFormat);
    }

    @Test
    public void testParseISO8601DateWithGMT() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        Date date = stdDateFormat.parse("2022-01-01T12:34:56.789Z");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseISO8601DateWithInvalidFormat() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        stdDateFormat.parse("InvalidDate");
    }

    @Test
    public void testParseRFC1123DateWithGMT() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        Date date = stdDateFormat.parse("Sat, 01 Jan 2022 12:34:56 GMT");
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParseRFC1123DateWithInvalidTimezone() throws ParseException {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        stdDateFormat.parse("Sat, 01 Jan 2022 12:34:56 EST");
    }

    @Test
    public void testFormatDateWithGMT() {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        String formattedDate = stdDateFormat.format(new Date(1641312256000L));
        assertEquals("2022-01-01T15:14:16Z", formattedDate);
    }

    @Test
    public void testFormatDateWithCustomTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        String formattedDate = stdDateFormat.format(new Date(1641312256000L));
        assertEquals("2022-01-01T08:14:16-08:00", formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeZoneWithNullTimeZone() {
        StdDateFormat stdDateFormat = new StdDateFormat(null, Locale.US);
        stdDateFormat.setTimeZone(null);
    }
}