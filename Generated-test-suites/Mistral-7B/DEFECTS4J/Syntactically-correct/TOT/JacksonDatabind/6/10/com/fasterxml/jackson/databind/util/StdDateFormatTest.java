package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class StdDateFormatTest {

    private static final String ISO8601_DATE = "2022-01-01T12:34:56.789Z";
    private static final String ISO8601_DATE_PLUS = "2022-01-01T12:34:56.789+01:00";
    private static final String ISO8601_DATE_MINUS = "2022-01-01T12:34:56.789-01:00";
    private static final String ISO8601_DATE_MISSING_T = "2022-01-0120:34:56.789Z";
    private static final String ISO8601_DATE_MISSING_Z = "2022-01-01T12:34:56.789";
    private static final String ISO8601_DATE_MISSING_COLON = "2022-01-01T123456.789Z";

    private static final String RFC1123_DATE = "Mon, 01 Jan 2022 12:34:56 GMT";
    private static final String RFC1123_DATE_PLUS = "Mon, 01 Jan 2022 13:34:56 CET";
    private static final String RFC1123_DATE_MINUS = "Mon, 01 Jan 2022 11:34:56 PST";
    private static final String RFC1123_DATE_MISSING_WEEKDAY = "Mon, 01 Jan 2022 12:34:56 GMT";
    private static final String RFC1123_DATE_MISSING_MONTH = "Mon, 32 Jan 2022 12:34:56 GMT";
    private static final String RFC1123_DATE_MISSING_YEAR = "Mon, 01 Jan 20XX 12:34:56 GMT";

    private StdDateFormat format;

    @Before
    public void setup() {
        format = new StdDateFormat();
    }

    @Test
    public void testParseISO8601Date() throws ParseException {
        Date date = format.parse(ISO8601_DATE);
        assertEquals(ISO8601_DATE, format.format(date));
    }

    @Test
    public void testParseISO8601DateWithTimezone() throws ParseException {
        Date date = format.parse(ISO8601_DATE_PLUS);
        assertEquals(ISO8601_DATE_PLUS, format.format(date));
        assertEquals("Europe/Paris", date.getTimezone().getID());

        date = format.parse(ISO8601_DATE_MINUS);
        assertEquals(ISO8601_DATE_MINUS, format.format(date));
        assertEquals("America/Los_Angeles", date.getTimezone().getID());
    }

    @Test
    public void testParseISO8601DateMissingT() throws ParseException {
        assertThrows(ParseException.class, () -> format.parse(ISO8601_DATE_MISSING_T));
    }

    @Test
    public void testParseISO8601DateMissingZ() throws ParseException {
        Date date = format.parse(ISO8601_DATE_MISSING_Z);
        assertEquals(ISO8601_DATE_MISSING_Z, format.format(date));
        assertNull(date.getTimezone());
    }

    @Test
    public void testParseISO8601DateMissingColon() throws ParseException {
        assertThrows(ParseException.class, () -> format.parse(ISO8601_DATE_MISSING_COLON));
    }

    @Test
    public void testParseRFC1123Date() throws ParseException {
        Date date = format.parse(RFC1123_DATE);
        assertEquals(RFC1123_DATE, format.format(date));
    }

    @Test
    public void testParseRFC1123DateWithTimezone() throws ParseException {
        Date date = format.parse(RFC1123_DATE_PLUS);
        assertEquals(RFC1123_DATE_PLUS, format.format(date));
        assertEquals("Europe/Paris", date.getTimezone().getID());

        date = format.parse(RFC1123_DATE_MINUS);
        assertEquals(RFC1123_DATE_MINUS, format.format(date));
        assertEquals("America/Los_Angeles", date.getTimezone().getID());
    }

    @Test
    public void testParseRFC1123DateMissingWeekday() throws ParseException {
        assertThrows(ParseException.class, () -> format.parse(RFC1123_DATE_MISSING_WEEKDAY));
    }

    @Test
    public void testParseRFC1123DateMissingMonth() throws ParseException {
        assertThrows(ParseException.class, () -> format.parse(RFC1123_DATE_MISSING_MONTH));
    }

    @Test
    public void testParseRFC1123DateMissingYear() throws ParseException {
        assertThrows(ParseException.class, () -> format.parse(RFC1123_DATE_MISSING_YEAR));
    }

    @Test
    public void testFormatDate() {
        Date date = new Date(1640995200000L); // 1 January 2022, 12:00:00 GMT
        assertEquals(ISO8601_DATE, format.format(date));
    }

    @Test
    public void testFormatDateWithTimezone() {
        Date date = new Date(1640995200000L); // 1 January 2022, 12:00:00 GMT
        format.setTimeZone(TimeZone.getTimezone("Europe/Paris"));
        assertEquals(ISO8601_DATE_PLUS, format.format(date));

        format.setTimeZone(TimeZone.getTimezone("America/Los_Angeles"));
        assertEquals(ISO8601_DATE_MINUS, format.format(date));
    }

    @Test
    public void testClone() {
        StdDateFormat clone = format.clone();
        assertTrue(clone != format);
        assertEquals(format.toString(), clone.toString());
        assertEquals(format.getTimeZone(), clone.getTimeZone());
        assertEquals(format._locale, clone._locale);

        format.setTimeZone(TimeZone.getTimezone("Europe/Paris"));
        assertFalse(format.equals(clone));

        format.setLocale(Locale.FRANCE);
        assertFalse(format.equals(clone));
    }

    @Test
    public void testGetBlueprintFormat() {
        assertNull(format.getBlueprintISO8601Format());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintFormat_get() {
        format.getBlueprintISO8601Format();
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat format = StdDateFormat.getISO8601Format(TimeZone.getTimezone("Europe/Paris"), Locale.FRANCE);
        assertEquals("Europe/Paris", format.getTimeZone().getID());
        assertEquals(Locale.FRANCE, format.getLocale());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetISO8601Format_getBlueprint() {
        StdDateFormat.getISO8601Format(TimeZone.getTimezone("Europe/Paris"));
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat format = StdDateFormat.getRFC1123Format(TimeZone.getTimezone("Europe/Paris"), Locale.FRANCE);
        assertEquals("Europe/Paris", format.getTimeZone().getID());
        assertEquals(Locale.FRANCE, format.getLocale());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetRFC1123Format_getBlueprint() {
        StdDateFormat.getRFC1123Format(TimeZone.getTimezone("Europe/Paris"));
    }

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals("GMT", StdDateFormat.getDefaultTimeZone().getID());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat withTimeZone = format.withTimeZone(TimeZone.getTimezone("Europe/Paris"));
        assertEquals("Europe/Paris", withTimeZone.getTimeZone().getID());

        StdDateFormat withLocale = format.withLocale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, withLocale.getLocale());

        StdDateFormat withTimeZoneAndLocale = format.withTimeZone(TimeZone.getTimezone("Europe/Paris")).withLocale(Locale.FRANCE);
        assertEquals("Europe/Paris", withTimeZoneAndLocale.getTimeZone().getID());
        assertEquals(Locale.FRANCE, withTimeZoneAndLocale.getLocale());
    }
}