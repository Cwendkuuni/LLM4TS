package org.apache.commons.lang3.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Date;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstanceWithValidPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertNotNull(fastDateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithEmptyPattern() {
        FastDateFormat.getInstance("");
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        Date date = new Date();
        assertEquals("06/12/23", fastDateFormat.format(date));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateInstanceWithInvalidStyle() {
        FastDateFormat.getDateInstance(10);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        Date date = new Date();
        assertEquals("Tue Aug 02 14:25:36 PDT 2022", fastDateFormat.format(date));
    }

    @Test
    public void testFormatDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        Date date = new Date();
        assertEquals("06/12/23", fastDateFormat.format(date));
    }

    @Test
    public void testParseDate() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        Date date = fastDateFormat.parse("06/12/23");
        assertEquals(new Date(1668722000000L), date);
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        fastDateFormat.parse("xx/xx/xxxx");
    }

    @Test
    public void testGetPattern() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertEquals("dd-MM-yyyy hh:mm:ss a z", fastDateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertNotNull(fastDateFormat.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertNotNull(fastDateFormat.getLocale());
    }

    @Test
    public void testEquals() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertTrue(fastDateFormat1.equals(fastDateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat fastDateFormat1 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertEquals(fastDateFormat1.hashCode(), fastDateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        assertEquals("FastDateFormat[dd-MM-yyyy hh:mm:ss a z, sun.util.calendar.ZoneInfo[id=America/Los_Angeles, offset=0, len=3600000, rules=null], java.util.Locale[id=en, language=en, country=US]],", fastDateFormat.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("dd-MM-yyyy hh:mm:ss a z");
        Calendar calendar = Calendar.getInstance();
        FastDateFormat formattedDate = fastDateFormat.applyRules(calendar, new StringBuffer());
        assertEquals("Tue Aug 02 14:25:36 PDT 2022", formattedDate.toString());
    }
}