package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new GregorianCalendar(2022, 11, 25).getTime();
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2022-12-25T00:00:00Z", formattedDate);

        Date dateWithMilliseconds = new GregorianCalendar(2022, 11, 25, 12, 34, 56).getTime();
        String formattedDateWithMilliseconds = ISO8601Utils.format(dateWithMilliseconds, true, TimeZone.getDefault());
        assertEquals("2022-12-25T12:34:56.000Z", formattedDateWithMilliseconds);

        Date dateWithTimeZone = new GregorianCalendar(2022, 11, 25, 12, 34, 56, 0, TimeZone.getTimeZone("America/New_York")).getTime();
        String formattedDateWithTimeZone = ISO8601Utils.format(dateWithTimeZone, true, TimeZone.getTimeZone("America/New_York"));
        assertEquals("2022-12-25T17:34:56.000-05:00", formattedDateWithTimeZone);
    }

    @Test
    public void testParseDate() throws ParseException {
        String isoDate = "2022-12-25T12:34:56.000Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2022, 11, 25, 12, 34, 56, 0).getTime(), parsedDate.getTime());
        assertEquals(0, pos.getIndex());

        isoDate = "2022-12-25T12:34:56.123Z";
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2022, 11, 25, 12, 34, 56, 123).getTime(), parsedDate.getTime());
        assertEquals(11, pos.getIndex());

        isoDate = "2022-12-25T12:34:56+01:00";
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2023, 0, 25, 13, 34, 56, 0).getTime(), parsedDate.getTime());
        assertEquals(14, pos.getIndex());

        isoDate = "2022-12-25T12:34:56.123-01:00";
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2022, 11, 25, 11, 33, 56, 123).getTime(), parsedDate.getTime());
        assertEquals(17, pos.getIndex());

        isoDate = "2022-12-25T12:34:56Z";
        parsedDate = ISO8601Utils.parse(isoDate, pos);
        assertEquals(new GregorianCalendar(2022, 11, 25, 12, 34, 56, 0).getTime(), parsedDate.getTime());
        assertEquals(11, pos.getIndex());

        isoDate = "2022-12-25T12:34:56"; // no timezone specified
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(isoDate, pos));
    }

    @Test
    public void testFormatAndParseDatesWithInvalidInput() {
        String[] invalidInputs = {
                "2022-12-25T12:34:56Zaa",
                "2022-12-25T12:34:56.123Zbbb",
                "2022-12-25T12:34:56+1:00ccc",
                "2022-12-25T12:34:56-60:00ddd",
                "2022-12-25T12:34:56Zeeee",
                "2022-12-25T12:34:56eeeee"};

        for (String input : invalidInputs) {
            assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.parse(input, new ParsePosition(0)));
            assertNull(ISO8601Utils.parse(input, new ParsePosition(0)));
            assertThrows(IllegalArgumentException.class, () -> ISO8601Utils.format(null, false, TimeZone.getDefault()));
            assertNull(ISO8601Utils.format(null, false, TimeZone.getDefault()));
        }
    }
}