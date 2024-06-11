package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy");
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", new TimeZone("UTC"));
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yyyy", new TimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getDateInstance(new TimeZone("UTC"));
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance("dd/MM/yyyy", new TimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance(Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getTimeInstance(new TimeZone("UTC"));
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance("hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance();
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(new TimeZone("UTC"));
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertNotNull(format);
    }

    @Test
    public void testFormat() throws Exception {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        Date date = new GregorianCalendar(2019, 1, 1).getTime();
        assertEquals("01/02/2019 12:00 AM", format.format(date));
    }

    @Test
    public void testParse() throws Exception {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        String dateString = "01/02/2019 12:00 AM";
        Date date = format.parse(dateString);
        assertEquals(new GregorianCalendar(2019, 1, 1).getTime(), date);
    }

    @Test
    public void testFormatWithBuffer() throws Exception {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        Date date = new GregorianCalendar(2019, 1, 1).getTime();
        StringBuffer buffer = new StringBuffer();
        format.format(date, buffer);
        assertEquals("01/02/2019 12:00 AM", buffer.toString());
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertEquals("dd/MM/yyyy hh:mm a", format.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertEquals("UTC", format.getTimeZone().getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertEquals(Locale.FRENCH, format.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertEquals(32, format.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        FastDateFormat format2 = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        FastDateFormat format3 = FastDateFormat.getDateInstance();

        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        FastDateFormat format2 = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        FastDateFormat format3 = FastDateFormat.getDateInstance();

        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        int hashCode3 = format3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertSame(hashCode1, hashCode1);
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        assertEquals("FastDateFormat[dd/MM/yyyy hh:mm a,UTC,UTC]", format.toString());
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = new GregorianCalendar(2019, 1, 1);
        FastDateFormat format = FastDateFormat.getDateTimeInstance("dd/MM/yyyy hh:mm a", new TimeZone("UTC"), Locale.FRENCH);
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        assertEquals("01/02/2019 12:00 AM", buffer.toString());
    }
}