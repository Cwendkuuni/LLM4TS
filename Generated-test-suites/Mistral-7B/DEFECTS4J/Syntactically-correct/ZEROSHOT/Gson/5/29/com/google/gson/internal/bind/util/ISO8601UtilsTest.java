package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        Date date = new Date(1234567890123L); // Thu Mar 18 15:16:17 CDT 2010
        String formatted = ISO8601Utils.format(date);
        assertEquals("2010-03-18T15:16:17-05:00", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2010-03-18T15:16:17.012Z", formatted);

        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2010-03-18T20:16:17.012-04:00", formatted);
    }

    @Test
    public void testParse() throws Exception {
        String dateString = "2010-03-18T15:16:17Z";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertNotNull(date);
        assertEquals("Thu Mar 18 15:16:17 CDT 2010", date.toString());

        dateString = "2010-03-18T15:16:17-05:00";
        date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertNotNull(date);
        assertEquals("Thu Mar 18 20:16:17 CDT 2010", date.toString());

        try {
            date = ISO8601Utils.parse("2010-03-18T15:16:17:XX", new ParsePosition(0));
            assertNull(date);
        } catch (ParseException e) {
            // expected
        }

        try {
            date = ISO8601Utils.parse("2010-03-18T15:16:17Za", new ParsePosition(0));
            assertNull(date);
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFormatParsingEdgeCases() throws Exception {
        Date date = new Date(Long.MIN_VALUE); // Thu Jan 01 00:00:01 GMT 1970
        String formatted = ISO8601Utils.format(date);
        assertEquals("1970-01-01T00:00:01.0000001Z", formatted);

        date = new Date(Long.MAX_VALUE); // Wed Jan 19 03:14:07 GMT 2038
        formatted = ISO8601Utils.format(date);
        assertEquals("2101-01-19T03:14:07.0000000Z", formatted);

        Calendar calendar = new GregorianCalendar(2029, Calendar.DECEMBER, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        date = calendar.getTime();
        formatted = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2029-12-31T23:59:59.999Z", formatted);
    }

    @Test
    public void testParseEdgeCases() throws Exception {
        Date date = ISO8601Utils.parse("1970-01-01T00:00:01.0000001Z");
        assertNotNull(date);
        assertEquals("Thu Jan 01 00:00:01 GMT 1970", date.toString());

        date = ISO8601Utils.parse("2101-01-19T03:14:07.0000000Z");
        assertNotNull(date);
        assertEquals("Wed Jan 19 03:14:07 GMT 2101", date.toString());

        Calendar calendar = new GregorianCalendar(2029, Calendar.DECEMBER, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        String dateString = calendar.getTime().toString();
        date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertNotNull(date);
        assertEquals("2029-12-31T23:59:59.999Z", dateString);
    }

}