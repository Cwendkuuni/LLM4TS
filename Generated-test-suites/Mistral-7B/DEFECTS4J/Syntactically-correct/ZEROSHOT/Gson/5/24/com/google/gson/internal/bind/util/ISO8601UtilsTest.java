package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new GregorianCalendar(2023, 1, 1, 12, 34, 56, 789).getTime();
        String formattedDate = ISO8601Utils.format(date, false, TimeZone.getDefault());
        assertEquals("2023-01-01T12:34:56.789", formattedDate);

        String formattedDateWithTimeZone = ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
        assertEquals("2023-01-01T12:34:56.789Z", formattedDateWithTimeZone);

        Date dateWithMillis = new GregorianCalendar(2023, 1, 1, 12, 34, 56, 789, 123).getTime();
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true, TimeZone.getDefault());
        assertEquals("2023-01-01T12:34:56.789123", formattedDateWithMillis);

        String formattedDateWithTimeZoneAndMillis = ISO8601Utils.format(dateWithMillis, true, TimeZone.getTimeZone("UTC"));
        assertEquals("2023-01-01T12:34:56.789123Z", formattedDateWithTimeZoneAndMillis);
    }

    @Test
    public void testParse() throws ParseException {
        String isoDate = "2023-01-01T12:34:56.789Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2023, 0, 1, 12, 34, 56, 789).getTime(), parsedDate);
        assertEquals(isoDate.length(), pos.getIndex());

        String isoDateWithoutTimeZone = "2023-01-01T12:34:56.789";
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(isoDateWithoutTimeZone, pos);
        assertEquals(new GregorianCalendar(2023, 0, 1, 12, 34, 56, 789).getTime(), parsedDate);
        assertEquals(isoDateWithoutTimeZone.length(), pos.getIndex());

        String isoDateWithOffset = "2023-01-01T12:34:56.789+01:30";
        pos.setIndex(0);
        parsedDate = ISO8601Utils.parse(isoDateWithOffset, pos);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
        calendar.setTime(parsedDate);
        assertEquals(Calendar.ZONE_OFFSET + 60 * 60 * 1000 + 30 * 60 * 1000, calendar.getTimeInMillis() % (24 * 60 * 60 * 1000));

        String isoDateWithOffsetWrongFormat = "2023-01-01T12:34:56.789+01:30:30";
        pos.setIndex(0);
        try {
            parsedDate = ISO8601Utils.parse(isoDateWithOffsetWrongFormat, pos);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }

        String isoDateWithTimeZoneButNoOffset = "2023-01-01T12:34:56.789Z";
        pos.setIndex(0);
        try {
            parsedDate = ISO8601Utils.parse(isoDateWithTimeZoneButNoOffset, pos);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

}