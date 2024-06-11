package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
        assertEquals("dd/MM/yy HH:mm zzz", format.getPattern());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy h:mm a z");
        assertNotNull(format);
        assertEquals("dd-MM-yyyy h:mm a z", format.getPattern());
    }

    @Test
    public void testGetInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yy HH:mm zzz", new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("dd/MM/yy HH:mm GMT", format.getPattern());
    }

    @Test
    public void testGetInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getInstance("dd/MM/yy HH:mm zzz", Locale.GERMAN);
        assertNotNull(format);
        assertEquals("dd.MM.yyyy HH:mm z", format.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getInstance("dd-MM-yyyy h:mm a z", new SimpleTimeZone(0, "GMT"), Locale.GERMAN);
        assertNotNull(format);
        assertEquals("dd-MM-yyyy h:mm a z", format.getPattern());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
        assertEquals("d/M/yy", format.getPattern());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.GERMAN);
        assertNotNull(format);
        assertEquals("d.M.yy", format.getPattern());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("d/M/yy G", format.getPattern());
    }

    @Test
    public void testGetDateInstanceWithTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.GERMAN, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("d.M.yy", format.getPattern());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(format);
        assertEquals("HH:mm z", format.getPattern());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.GERMAN);
        assertNotNull(format);
        assertEquals("HH:mm", format.getPattern());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("HH:mm G", format.getPattern());
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.GERMAN, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("HH:mm", format.getPattern());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(format);
        assertEquals("d/M/yy h:mm a z", format.getPattern());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.GERMAN);
        assertNotNull(format);
        assertEquals("d.M.yy HH:mm", format.getPattern());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("d/M/yy HH:mm G", format.getPattern());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneLocale() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.GERMAN, new SimpleTimeZone(0, "GMT"));
        assertNotNull(format);
        assertEquals("d.M.yy HH:mm", format.getPattern());
    }

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Date date = new SimpleDate(System.currentTimeMillis());
        String formattedDate = format.format(date);
        assertEquals("expected formatted date", formattedDate);
    }

    @Test
    public void testParse() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String dateString = "25.02.2023 15:30";
        Date parsedDate = format.parse(dateString);
        assertEquals("expected parsed date", parsedDate);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDate() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String dateString = "25.02.2023 33:30";
        format.parse(dateString);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        int maxLength = format.getMaxLengthEstimate();
        assertNotEquals(-1, maxLength);
    }
}