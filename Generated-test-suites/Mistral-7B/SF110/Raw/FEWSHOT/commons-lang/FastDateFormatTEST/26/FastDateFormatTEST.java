package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.*;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getTimeStyle());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRENCH, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals("yyyy-MM-dd HH:mm:ss", dateFormat.getPattern());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.SHORT, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.SHORT, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.SHORT, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.SHORT, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH);
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRENCH, TimeZone.getTimeZone("UTC"));
        assertNotNull(dateFormat);
        assertEquals(FastDateFormat.MEDIUM, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.SHORT, dateFormat.getTimeStyle());
        assertEquals(Locale.FRENCH, dateFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), dateFormat.getTimeZone());
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        // Add more assertions based on expected format
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG);
        String dateString = "Sat Aug 12 15:30:51 PDT 2023";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
        assertEquals(new Date(1689697051000L), parsedDate);
    }
}