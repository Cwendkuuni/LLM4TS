package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class StdDateFormatTest {

    private StdDateFormat stdDateFormat;

    @Before
    public void setup() {
        stdDateFormat = new StdDateFormat();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(StdDateFormat.instance);
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        @Deprecated
        DateFormat blueprintFormat = StdDateFormat.getBlueprintISO8601Format();
        assertNotNull(blueprintFormat);
    }

    @Test(expected = Deprecated.class)
    public void testGetBlueprintISO8601FormatDeprecated() {
        StdDateFormat.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testGetISO8601FormatWithSameArguments() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        DateFormat format2 = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.US);
        assertEquals(format, format2);
    }

    @Test
    public void testParseISO8601Date() throws ParseException {
        String dateString = "2022-01-01T12:34:56.123Z";
        Date formatDate = stdDateFormat.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatDate);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH) + 1);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, calendar.get(Calendar.MINUTE));
        assertEquals(56, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601Date() throws ParseException {
        String dateString = "2022-01-01T12:34:56.123AA";
        stdDateFormat.parse(dateString);
    }

    @Test
    public void testFormatDate() {
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertFalse(formattedDate.contains(":"));
        assertFalse(formattedDate.contains("."));
        assertFalse(formattedDate.contains("Z"));
    }

    @Test
    public void testFormatDateWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC+1:00");
        StdDateFormat formatter = new StdDateFormat(timeZone, Locale.US);
        Date date = new Date();
        String formattedDate = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(timeZone);
        int offset = calendar.get(Calendar.EXACT_DST_OFFSET());
        String expectedFormattedDate = String.format("%tc %+TZD", calendar);
        assertTrue(formattedDate.startsWith(expectedFormattedDate));
    }

    @Test
    public void testToString() {
        assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dbcs=false,area=null,dstSavings=0,useDaylight=false] (local),locale: java.util.Locale[language=en,country=US])", stdDateFormat.toString());
    }
}