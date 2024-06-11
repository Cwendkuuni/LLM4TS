import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance((String) null);
        assertThrows(NullPointerException.class, () -> fastDateFormat2);
        FastDateFormat fastDateFormat3 = FastDateFormat.getInstance((Locale) null);
        assertThrows(NullPointerException.class, () -> fastDateFormat3);
        FastDateFormat fastDateFormat4 = FastDateFormat.getInstance((TimeZone) null);
        assertThrows(NullPointerException.class, () -> fastDateFormat4);
    }

    @Test
    public void testGetInstancePattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("");
        assertNotNull(fastDateFormat);
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("InvalidPattern");
        assertThrows(IllegalArgumentException.class, () -> fastDateFormat2);
        FastDateFormat fastDateFormat3 = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = fastDateFormat3.parse("2023-03-15");
        assertNotNull(date);
        String formattedDate = fastDateFormat3.format(date);
        assertEquals("Thu Mar 16 00:00:00 ICT 2023", formattedDate);
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd/MM/yyyy", Locale.US);
        assertNotNull(fastDateFormat);
        Date date = fastDateFormat.parse("03/15/2023");
        assertNotNull(date);
        String formattedDate = fastDateFormat.format(date);
        assertEquals("Mar 15, 2023", formattedDate);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"));
        assertNotNull(fastDateFormat);
        Date date = fastDateFormat.parse("2023-03-15 15:30:00 UTC");
        assertNotNull(date);
        String formattedDate = fastDateFormat.format(date);
        assertEquals("Thu Mar 15 15:30:00 UTC 2023", formattedDate);
    }

    @Test
    public void testGetInstanceWithPatternAndLocaleAndTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MMM-yy hh:mm a zzzz", new Locale("en", "US"), new SimpleTimeZone(0, "UTC"));
        assertNotNull(fastDateFormat);
        Date date = fastDateFormat.parse("03-Mar-23 03:30 PM UTC");
        assertNotNull(date);
        String formattedDate = fastDateFormat.format(date);
        assertEquals("Mar 03, 2023 03:30:00 PM UTC", formattedDate);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(-1, -1);
        assertThrows(IllegalArgumentException.class, () -> fastDateFormat2);
        FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, (Locale) null);
        assertThrows(NullPointerException.class, () -> fastDateFormat3);
        FastDateFormat fastDateFormat4 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, (TimeZone) null);
        assertThrows(NullPointerException.class, () -> fastDateFormat4);
    }

    @Test
    public void testParse() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Date date = fastDateFormat.parse("Mar 15, 2023 3:15:00 PM");
        assertNotNull(date);
        assertThrows(ParseException.class, () -> fastDateFormat.parse("InvalidDate"));
        assertThrows(ParseException.class, () -> fastDateFormat.parse(""));
    }

    @Test
    public void testParseWithParsePosition() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = fastDateFormat.parse("Mar 15, 2023 3:15:00 PM", parsePosition);
        assertNotNull(date);
        assertEquals(17, parsePosition.getIndex());
    }

    @Test
    public void testParseObject() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String source = "Mar 15, 2023 3:15:00 PM";
        Date date = (Date) fastDateFormat.parseObject(source);
        assertNotNull(date);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertEquals("dd/MM/yy hh:mm a", fastDateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleTimeZone(0, "UTC"));
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
        Locale locale = fastDateFormat.getLocale();
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertEquals(32, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
        assertFalse(fastDateFormat1.equals(fastDateFormat3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat2 = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat fastDateFormat3 = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
        assertNotEquals(fastDateFormat1.hashCode(), fastDateFormat3.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String toString = fastDateFormat.toString();
        assertTrue(toString.startsWith("FastDateFormat["));
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = new GregorianCalendar(2023, 2, 15);
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer stringBuffer = new StringBuffer(100);
        FastDateFormat.this.applyRules(calendar, stringBuffer);
        assertEquals("Mar 15, 2023 3:15:00 PM", stringBuffer.toString());
    }
}