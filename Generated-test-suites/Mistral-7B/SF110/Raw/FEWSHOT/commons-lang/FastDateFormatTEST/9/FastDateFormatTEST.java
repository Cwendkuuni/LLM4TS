package org.apache.commons.lang3.time;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidPattern() {
        FastDateFormat.getInstance("dd-MM-yyy"); // Invalid pattern
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateInstanceWithInvalidStyle() {
        FastDateFormat.getDateInstance(-1); // Invalid style
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(timeFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTimeInstanceWithInvalidStyle() {
        FastDateFormat.getTimeInstance(-1); // Invalid style
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateTimeFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateTimeInstanceWithInvalidDateStyle() {
        FastDateFormat.getDateTimeInstance(-1, FastDateFormat.SHORT); // Invalid date style
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateTimeInstanceWithInvalidTimeStyle() {
        FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, -1); // Invalid time style
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("Sat Aug 27 15:34:12 EDT 2022", formattedDate);
    }

    @Test
    public void testFormatWithLocale() {
        Locale locale = new Locale("fr", "FR");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertTrue(formattedDate.contains("dim")); // "dimanche" is the French word for "Sunday"
    }

    @Test
    public void testFormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertTrue(formattedDate.contains("UTC"));
    }

    @Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Date parsedDate = dateFormat.parse("27 Aug 2022");
        assertEquals(new Date(1659395200000L), parsedDate); // 27 Aug 2022 in medium date format corresponds to 1659395200000 milliseconds
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        dateFormat.parse("27 Aug 2022 23:59:60"); // Invalid format
    }
}