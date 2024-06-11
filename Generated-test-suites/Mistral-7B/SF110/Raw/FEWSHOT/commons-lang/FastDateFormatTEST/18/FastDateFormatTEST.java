package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(TimeZone.getDefault(), format.getTimeZone());
        assertEquals("yyyy-MM-dd HH:mm:ss z", format.getPattern());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertEquals(Locale.getDefault(), dateFormat.getLocale());
        assertEquals(TimeZone.getDefault(), dateFormat.getTimeZone());
        assertEquals("MM/dd/yyyy", dateFormat.getPattern());

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertEquals(Locale.getDefault(), dateTimeFormat.getLocale());
        assertEquals(TimeZone.getDefault(), dateTimeFormat.getTimeZone());
        assertEquals("MM/dd/yyyy h:mm a z", dateTimeFormat.getPattern());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG);
        assertEquals(Locale.getDefault(), dateTimeFormat.getLocale());
        assertEquals(TimeZone.getDefault(), dateTimeFormat.getTimeZone());
        assertEquals("MM/dd/yyyy h:mm a zzzz", dateTimeFormat.getPattern());

        FastDateFormat dateTimeFormatWithTimeZone = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new TimeZone("GMT-05:00"), new Locale("es", "ES"));
        assertEquals(Locale.forLanguageTag("es"), dateTimeFormatWithTimeZone.getLocale());
        assertEquals(new TimeZone("GMT-05:00"), dateTimeFormatWithTimeZone.getTimeZone());
        assertEquals("MM/dd/yyyy h:mm a z", dateTimeFormat.getPattern());
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = dateFormat.format(new Date());
        assertEquals("12/31/2022", dateString);

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String dateTimeString = dateTimeFormat.format(new Date());
        assertEquals("12/31/2022 1:23 PM EST", dateTimeString);

        FastDateFormat dateTimeFormatWithTimeZone = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new TimeZone("GMT-05:00"), new Locale("es", "ES"));
        String dateTimeStringWithTimeZone = dateTimeFormatWithTimeZone.format(new Date());
        assertEquals("31/12/2022 05:23:59 PM EST", dateTimeStringWithTimeZone);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date date = dateFormat.parse("12/31/2022");
        assertEquals(new Date(Long.parseLong("1672492800000")), date);

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Date dateTime = dateTimeFormat.parse("12/31/2022 1:23 PM EST");
        assertEquals(new Date(Long.parseLong("1672501830000")), dateTime);

        FastDateFormat dateTimeFormatWithTimeZone = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, new TimeZone("GMT-05:00"), new Locale("es", "ES"));
        Date dateTimeWithTimeZone = dateTimeFormatWithTimeZone.parse("31/12/2022 05:23:59 PM EST");
        assertEquals(new Date(Long.parseLong("1672502339000")), dateTimeWithTimeZone);
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        assertTrue(format1.equals(format2));

        FastDateFormat format3 = new FastDateFormat("yyyy-MM-dd", TimeZone.getTimeZone("UTC"), Locale.US);
        assertFalse(format1.equals(format3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        assertEquals(format1.hashCode(), format2.hashCode());

        FastDateFormat format3 = new FastDateFormat("yyyy-MM-dd", TimeZone.getTimeZone("UTC"), Locale.US);
        assertFalse(format1.hashCode() == format3.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        String formattedString = format.toString();
        String expected = "FastDateFormat[yyyy-MM-dd HH:mm:ss z, null, null]";
        assertEquals(expected, formattedString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = new GregorianCalendar(2022, 11, 31);
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        StringBuffer buffer = new StringBuffer();
        dateFormat.applyRules(calendar, buffer);
        assertEquals("12/31/2022", buffer.toString());

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer buffer2 = new StringBuffer();
        dateTimeFormat.applyRules(calendar, buffer2);
        assertEquals("12/31/2022 1:23 PM EST", buffer2.toString());
    }
}