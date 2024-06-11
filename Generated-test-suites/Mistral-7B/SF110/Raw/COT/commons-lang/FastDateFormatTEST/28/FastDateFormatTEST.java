import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.Formatutils;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", null, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", timeZone, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.FRENCH;
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        Assert.assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Date date = new Date();
        String formattedDate = fastDateFormat.format(date);
        Assert.assertNotNull(formattedDate);
    }

    @Test
    public void testFormatLong() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        long millis = System.currentTimeMillis();
        String formattedLong = fastDateFormat.format(millis);
        Assert.assertNotNull(formattedLong);
    }

    @Test
    public void testFormatCalendar() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        String formattedCalendar = fastDateFormat.format(calendar);
        Assert.assertNotNull(formattedCalendar);
    }

    @Test
    public void testFormatStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(new Date(), stringBuffer, new FieldPosition(0));
        Assert.assertEquals(Formatutils.trimTrailingSpace(fastDateFormat.getPattern()), stringBuffer.toString());
    }

    @Test
    public void testFormatLongStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(System.currentTimeMillis(), stringBuffer);
        Assert.assertEquals(Formatutils.trimTrailingSpace(fastDateFormat.getPattern()), stringBuffer.toString());
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar(2022, Calendar.JANUARY, 1);
        StringBuffer stringBuffer = new StringBuffer();
        fastDateFormat.format(calendar, stringBuffer);
        Assert.assertEquals(Formatutils.trimTrailingSpace(fastDateFormat.getPattern()), stringBuffer.toString());
    }

    @Test
    public void testParseDate() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "24/12/2022";
        Date parsedDate = fastDateFormat.parse(dateString);
        Assert.assertNotNull(parsedDate);
    }

    @Test
    public void testParseStringParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "24/12/2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = fastDateFormat.parse(dateString, parsePosition);
        Assert.assertNotNull(parsedDate);
        Assert.assertEquals(-1, parsePosition.getIndex());
    }

    @Test
    public void testParseObjectParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String dateString = "24/12/2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Object parsedObject = fastDateFormat.parseObject(dateString, parsePosition);
        Assert.assertNotNull(parsedObject);
        Assert.assertEquals(Date.class, parsedObject.getClass());
        Assert.assertEquals(-1, parsePosition.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String invalidDateString = "32/12/2022";
        fastDateFormat.parse(invalidDateString);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String invalidDateString = "32/12/2022";
        ParsePosition parsePosition = new ParsePosition(0);
        fastDateFormat.parse(invalidDateString, parsePosition);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidObjectParsePosition() throws ParseException {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy");
        String invalidDateString = "32/12/2022";
        ParsePosition parsePosition = new ParsePosition(0);
        fastDateFormat.parseObject(invalidDateString, parsePosition);
    }
}