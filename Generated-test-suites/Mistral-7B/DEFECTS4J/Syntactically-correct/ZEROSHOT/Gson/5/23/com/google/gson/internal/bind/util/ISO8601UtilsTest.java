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
		Date date = new GregorianCalendar(2019, Calendar.DECEMBER, 25).getTime();

		// Test default format with no time or millis
		String result = ISO8601Utils.format(date);
		assertEquals("2019-12-25", result);

		// Test format with time and no millis
		result = ISO8601Utils.format(date, true, null);
		assertEquals("2019-12-25T12:34:56", result);

		// Test format with time, millis and timezone
		TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		result = ISO8601Utils.format(date, true, timeZone);
		assertEquals("2019-12-25T17:34:56-05:00", result);
	}

	@Test
	public void testParse() throws Exception {
		// Test parsing a date with no time or timezone
		Date date = ISO8601Utils.parse("2019-12-25", new ParsePosition(0));
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(2019, calendar.get(Calendar.YEAR));
		assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, calendar.get(Calendar.MINUTE));
		assertEquals(0, calendar.get(Calendar.SECOND));
		assertEquals(0, calendar.get(Calendar.MILLISECOND));

		// Test parsing a date with time and no timezone
		date = ISO8601Utils.parse("2019-12-25T12:34:56", new ParsePosition(0));
		calendar.setTime(date);
		assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(2019, calendar.get(Calendar.YEAR));
		assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(34, calendar.get(Calendar.MINUTE));
		assertEquals(56, calendar.get(Calendar.SECOND));
		assertEquals(0, calendar.get(Calendar.MILLISECOND));

		// Test parsing a date with time, millis and timezone
		TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
		date = ISO8601Utils.parse("2019-12-25T12:34:56.789-05:00", new ParsePosition(0));
		calendar.setTime(date);
		assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
		assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(2019, calendar.get(Calendar.YEAR));
		assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(34, calendar.get(Calendar.MINUTE));
		assertEquals(56, calendar.get(Calendar.SECOND));
		assertEquals(789, calendar.get(Calendar.MILLISECOND));
		assertEquals("America/New_York", timeZone.getID());
		assertEquals(-1 * (5 * 60 * 60), calendar.get(Calendar.DST_OFFSET));
	}

	@Test(expected = ParseException.class)
	public void testParseInvalidDate() throws Exception {
		ISO8601Utils.parse("2019-12-25T12:34:56ZZ", new ParsePosition(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseInvalidTimeZone() throws Exception {
		ISO8601Utils.parse("2019-12-25T12:34:56XXX", new ParsePosition(0));
	}
}