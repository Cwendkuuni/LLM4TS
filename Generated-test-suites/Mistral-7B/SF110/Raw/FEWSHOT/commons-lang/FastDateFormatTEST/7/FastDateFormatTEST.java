package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(dateTimeFormat);
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertNotNull(formattedDate);
        assertEquals("Fri Aug 12 15:30:25 EDT 2022", formattedDate);

        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        String formattedTime = timeFormat.format(date);
        assertNotNull(formattedTime);
        assertEquals("03:30:25 PM", formattedTime);

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String formattedDateTime = dateTimeFormat.format(date);
        assertNotNull(formattedDateTime);
        assertEquals("8/12/22, 3:30 pm", formattedDateTime);
    }

    @Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        String dateString = "8/12/22";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);

        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        String timeString = "3:30 pm";
        Date parsedTime = timeFormat.parse(timeString);
        assertNotNull(parsedTime);

        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String dateTimeString = "8/12/22, 3:30 pm";
        Date parsedDateTime = dateTimeFormat.parse(dateTimeString);
        assertNotNull(parsedDateTime);
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        String pattern = dateFormat.getPattern();
        assertNotNull(pattern);
        assertEquals("dd MMM yyyy HH:mm:ss zzz", pattern);
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        int maxLength = dateFormat.getMaxLengthEstimate();
        assertNotNull(maxLength);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        assertTrue(dateFormat1.equals(dateFormat2));

        Object obj = new Object();
        assertFalse(dateFormat1.equals(obj));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        int hashCode = dateFormat.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        String toString = dateFormat.toString();
        assertNotNull(toString);
    }

    @Test
    public void testApplyRules() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        dateFormat.applyRules(calendar, buf);
        assertNotNull(buf.toString());
    }
}