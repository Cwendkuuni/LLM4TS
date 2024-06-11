package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

public class FastDateFormatTest {
    @Test public void testGetInstance() { FastDateFormat fastDateFormat = FastDateFormat.getInstance(); assertNotNull(fastDateFormat); }
    // Add test cases for other methods here

    @Test (expected = ParseException.class) public void testParseEmptyPattern() { FastDateFormat fastDateFormat = new FastDateFormat("", null, null); String input = ""; Date date = fastDateFormat.parse(input); fail(); }
    // Add test cases for other edge cases and exceptions here

    @Test public void testFormatLargeDateTime() { FastDateFormat fastDateFormat = new FastDateFormat("yyyy-MM-dd HH:mm:ss", null, null); Date date = new Date(Long.MAX_VALUE); String expected = "9999-12-31 23:59:59"; String result = fastDateFormat.format(date); assertEquals(expected, result); }
    // Add test cases for other edge cases and exceptions here
}