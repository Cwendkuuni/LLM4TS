package com.google.gson.internal.bind.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1556515200000L); // Wed Oct 02 00:00:00 CEST 2019

        String expected = "2019-10-02T00:00:00+02:00";
        String result = ISO8601Utils.format(date, false, TimeZone.getDefault());
        Assert.assertEquals(expected, result);

        expected = "2019-10-02T00:00:00.000Z";
        result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("UTC"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        String dateString = "2019-10-02T00:00:00+02:00";
        ParsePosition pos = new ParsePosition(0);

        Date date = ISO8601Utils.parse(dateString, pos);
        Assert.assertNotNull(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int expectedYear = calendar.get(Calendar.YEAR);
        int expectedMonth = calendar.get(Calendar.MONTH) + 1;
        int expectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int expectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int expectedMinute = calendar.get(Calendar.MINUTE);
        int expectedSecond = calendar.get(Calendar.SECOND);

        Assert.assertEquals("Year should be 2019", 2019, expectedYear);
        Assert.assertEquals("Month should be October", 10, expectedMonth);
        Assert.assertEquals("Day should be 02", 2, expectedDay);
        Assert.assertEquals("Hour should be 00", 0, expectedHour);
        Assert.assertEquals("Minute should be 00", 0, expectedMinute);
        Assert.assertEquals("Second should be 00", 0, expectedSecond);

        // Test parsing date without time component
        String dateString2 = "2019-10-02";
        Date date2 = ISO8601Utils.parse(dateString2, pos);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date2);
        int expectedYear2 = calendar2.get(Calendar.YEAR);
        int expectedMonth2 = calendar2.get(Calendar.MONTH) + 1;
        int expectedDay2 = calendar2.get(Calendar.DAY_OF_MONTH);

        Assert.assertEquals("Year should be 2019", 2019, expectedYear2);
        Assert.assertEquals("Month should be October", 10, expectedMonth2);
        Assert.assertEquals("Day should be 02", 2, expectedDay2);
        Assert.assertEquals("Hour should be 00", 0, expectedHour);
        Assert.assertEquals("Minute should be 00", 0, expectedMinute);
        Assert.assertEquals("Second should be 00", 0, expectedSecond);

        // Test parsing date with time component
        String dateString3 = "2019-10-02T12:34:56.789+02:00";
        Date date3 = ISO8601Utils.parse(dateString3, pos);
        Calendar calendar3 = new GregorianCalendar();
        calendar3.setTime(date3);

        int expectedHour3 = calendar3.get(Calendar.HOUR_OF_DAY);
        int expectedMinute3 = calendar3.get(Calendar.MINUTE);
        int expectedSecond3 = calendar3.get(Calendar.SECOND);
        int expectedMillisecond3 = calendar3.get(Calendar.MILLISECOND);

        Assert.assertEquals("Hour should be 12", 12, expectedHour3);
        Assert.assertEquals("Minute should be 34", 34, expectedMinute3);
        Assert.assertEquals("Second should be 56", 56, expectedSecond3);
        Assert.assertEquals("Millisecond should be 789", 789, expectedMillisecond3);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String dateString = "2019-10-02T12:34:56abc";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTimeZoneIndicator() throws ParseException {
        String dateString = "2019-10-02T12:34:56ZX";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFormatWithInvalidOffset() {
        Date date = new Date(1556515200000L);
        StringBuilder formatted = new StringBuilder();

        String expected = "2019-10-02T00:00:00+02:00";
        ISO8601Utils.format(date, false, TimeZone.getDefault());
        Assert.assertEquals(expected, formatted.toString());

        // Test formatting with invalid offset
        formatted.setLength(0);
        ISO8601Utils.format(date, false, new TimeZone("InvalidTimeZone"));
        Assert.assertEquals("-1", formatted.length()); // length should be negative because of an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullDate() {
        StringBuilder formatted = new StringBuilder();
        ISO8601Utils.format(null, false, TimeZone.getDefault());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidDate() throws ParseException {
        String dateString = "2019-10-02T12:34:56abc";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullTimeZone() {
        StringBuilder formatted = new StringBuilder();
        ISO8601Utils.format(new Date(), false, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullString() throws ParseException {
        ISO8601Utils.parse(null, new ParsePosition(0));
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullParsePosition() throws ParseException {
        ISO8601Utils.parse(new String(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptyString() throws ParseException {
        ISO8601Utils.parse("", new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidYear() throws ParseException {
        String dateString = "1900-12-31T12:34:56.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidMonth() throws ParseException {
        String dateString = "2000-13-31T12:34:56.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidDay() throws ParseException {
        String dateString = "2019-02-32T12:34:56.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidHour() throws ParseException {
        String dateString = "2019-10-02T24:34:56.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidMinute() throws ParseException {
        String dateString = "2019-10-02T12:60:56.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidSecond() throws ParseException {
        String dateString = "2019-10-02T12:34:60.789+02:00";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }
}