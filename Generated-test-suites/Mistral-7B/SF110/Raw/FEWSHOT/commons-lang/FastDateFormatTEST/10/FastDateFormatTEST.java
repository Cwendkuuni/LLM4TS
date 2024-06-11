package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertEquals("dd/MM/yy HH:mm zzz", dateFormat.getPattern());
        assertNull(dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MMM-yy HH:mm z");
        assertEquals("dd-MMM-yy HH:mm z", dateFormat.getPattern());
        assertNull(dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MMM-yy HH:mm z", TimeZone.getTimeZone("UTC"));
        assertEquals("dd-MMM-yy HH:mm z", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MMM-yy HH:mm z", Locale.US);
        assertEquals("dd-MMM-yy HH:mm z", dateFormat.getPattern());
        assertNull(dateFormat.getTimeZone());
        assertSame(Locale.US, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MMM-yy HH:mm z", Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("dd-MMM-yy HH:mm z", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertSame(Locale.US, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance();
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertNull(dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(Locale.US);
        assertEquals("MM/dd/yy", dateFormat.getPattern());
        assertSame(Locale.US, dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(TimeZone.getTimeZone("UTC"));
        assertEquals("dd/MM/yy", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("MM/dd/yy", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertSame(Locale.US, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance();
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(Locale.US);
        assertEquals("hh:mm:ss a z", dateFormat.getPattern());
        assertSame(Locale.US, dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(TimeZone.getTimeZone("UTC"));
        assertEquals("HH:mm:ss z", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("hh:mm:ss a z", dateFormat.getPattern());
        assertSame(Locale.US, dateFormat.getLocale());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertNull(dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US);
        assertEquals("MM/dd/yy hh:mm:ss a z", dateFormat.getPattern());
        assertSame(Locale.US, dateFormat.getLocale());
        assertNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(TimeZone.getTimeZone("UTC"));
        assertEquals("dd/MM/yy HH:mm:ss z", dateFormat.getPattern());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
        assertNull(dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals("MM/dd/yy hh:mm:ss a z", dateFormat.getPattern());
        assertSame(Locale.US, dateFormat.getLocale());
        assertSame(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        Date date = new GregorianCalendar(2022, 11, 25, 15, 30, 0).getTime();
        String expected = "11/25/22 3:30:00 PM UTC";
        String actual = dateFormat.format(date);
        assertEquals(expected, actual);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        String dateString = "11/25/22 3:30:00 PM UTC";
        Date expectedDate = new GregorianCalendar(2022, 11, 25, 15, 30, 0).getTime();
        Date actualDate = dateFormat.parse(dateString);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFormatWithBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        Date date = new GregorianCalendar(2022, 11, 25, 15, 30, 0).getTime();
        StringBuffer expectedBuffer = new StringBuffer();
        String expected = "11/25/22 3:30:00 PM UTC";
        dateFormat.format(date, expectedBuffer);
        assertEquals(expected, expectedBuffer.toString());
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        String expected = "FastDateFormat[dd/MM/yy hh:mm:ss a z, null, UTC]";
        String actual = dateFormat.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(Locale.US, TimeZone.getTimeZone("UTC"));
        Calendar calendar = new GregorianCalendar(2022, 11, 25, 15, 30, 0);
        StringBuffer expectedBuffer = new StringBuffer();
        String expected = "Friday, November 25, 2022 3:30:00 PM UTC";
        dateFormat.applyRules(calendar, expectedBuffer);
        assertEquals(expected, expectedBuffer.toString());
    }
}