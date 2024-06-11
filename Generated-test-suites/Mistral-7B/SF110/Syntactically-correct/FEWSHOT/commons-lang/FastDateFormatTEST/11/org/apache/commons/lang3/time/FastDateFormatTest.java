package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat formatter = FastDateFormat.getInstance();
        assertEquals(Locale.getDefault(), formatter.getLocale());
        assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertEquals("dd/MM/yyyy HH:mm:ss z", formatter.getPattern());
        assertEquals(Locale.getDefault(), formatter.getLocale());
        assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", Locale.US);
        assertEquals("dd/MM/yyyy HH:mm:ss z", formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternLocaleTimeZone() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z", Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("dd/MM/yyyy HH:mm:ss z", formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), formatter.getTimeZone());
    }

    @Test
    public void testGetInstanceWithStyle() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertEquals(Locale.getDefault(), formatter.getLocale());
        assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
        assertEquals("dd.MM.yyyy", formatter.getPattern());
    }

    @Test
    public void testGetInstanceWithStyleLocale() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertEquals("dd/MM/yyyy", formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertEquals(TimeZone.getDefault(), formatter.getTimeZone());
    }

    @Test
    public void testGetInstanceWithStyleLocaleTimeZone() {
        FastDateFormat formatter = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("dd/MM/yyyy", formatter.getPattern());
        assertEquals(Locale.US, formatter.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), formatter.getTimeZone());
    }

    @Test
    public void testFormat() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 25).getTime();

        String formattedDate = formatter.format(date);
        assertEquals("25/01/2023 12:00:00 +01:00", formattedDate);
    }

    @Test
    public void testFormatDate() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        Date date = new GregorianCalendar(2023, Calendar.JANUARY, 25, 12, 0).getTime();

        String formattedDate = formatter.format(date);
        assertEquals("25/01/2023 12:00:00 +01:00", formattedDate);
    }

    @Test
    public void testFormatCalendar() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        Calendar calendar = new GregorianCalendar(2023, Calendar.JANUARY, 25, 12, 0);

        String formattedDate = formatter.format(calendar);
        assertEquals("25/01/2023 12:00:00 +01:00", formattedDate);
    }

    @Test
    public void testParse() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        String dateString = "25/01/2023 12:00:00 +01:00";

        Date parsedDate = formatter.parse(dateString);
        assertEquals(new Date(1674652800000L), parsedDate);
    }

    @Test
    public void testParseWithParsePosition() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        String dateString = "25/01/2023 12:00:00 +01:00";

        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = formatter.parse(dateString, parsePosition);
        assertEquals(new Date(1674652800000L), parsedDate);
        assertEquals(dateString.length(), parsePosition.getIndex());
    }

    @Test
    public void testParseObject() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        String dateString = "25/01/2023 12:00:00 +01:00";

        Date parsedDate = (Date) formatter.parseObject(dateString);
        assertEquals(new Date(1674652800000L), parsedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNull() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        formatter.parse(null);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        formatter.parse("Invalid Date");
    }

    @Test
    public void testGetPattern() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertEquals("dd/MM/yyyy HH:mm:ss z", formatter.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        TimeZone timeZone = formatter.getTimeZone();
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        Locale locale = formatter.getLocale();
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        int maxLength = formatter.getMaxLengthEstimate();
        assertEquals(32, maxLength);
    }

    @Test
    public void testEquals() {
        FastDateFormat formatter1 = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        FastDateFormat formatter2 = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertTrue(formatter1.equals(formatter2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat formatter1 = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        FastDateFormat formatter2 = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertEquals(formatter1.hashCode(), formatter2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        assertEquals("FastDateFormat[dd/MM/yyyy HH:mm:ss z, default locale, default timezone]", formatter.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss z");
        Calendar calendar = new GregorianCalendar(2023, Calendar.JANUARY, 25);
        StringBuffer buf = new StringBuffer();

        formatter.applyRules(calendar, buf);
        assertEquals("25/01/2023", buf.toString());
    }
}