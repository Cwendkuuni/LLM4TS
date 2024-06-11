package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

public class FastDateFormatTest {

    @org.junit.Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", null, locale);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy", timeZone, locale);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, locale);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL, timeZone, locale);
        assertNotNull(dateFormat);
    }

    @org.junit.Test
    public void testGetTimeInstance() {
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(timeFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(timeFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(timeFormat);
    }

    @org.junit.Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat timeFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(timeFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM);
        assertNotNull(dateTimeFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, locale);
        assertNotNull(dateTimeFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(dateTimeFormat);
    }

    @org.junit.Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat dateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(dateTimeFormat);
    }

    @org.junit.Test
    public void testFormatObject() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatObjectWithBufferAndPosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer(10);
        FastPosition position = new FastPosition(0, 0, 0, 0);
        dateFormat.format(date, stringBuffer, position);
        String formattedDate = stringBuffer.toString();
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatObjectWithBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer(10);
        dateFormat.format(date, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatObjectWithStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        StringBuffer stringBuffer = new StringBuffer(formattedDate.length());
        dateFormat.format(date, stringBuffer);
        assertEquals("Invalid formatted date", formattedDate, stringBuffer.toString());
    }

    @org.junit.Test
    public void testParse() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        Date parsedDate = dateFormat.parse(dateString);
        assertNotNull(parsedDate);
    }

    @org.junit.Test
    public void testParseWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNotNull(parsedDate);
        assertEquals("Invalid parse position index", dateString.length(), parsePosition.getIndex());
    }

    @org.junit.Test
    public void testParseObject() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        Object parsedObject = dateFormat.parseObject(dateString);
        assertNotNull(parsedObject);
        assertEquals("Invalid parsed object type", Date.class, parsedObject.getClass());
    }

    @org.junit.Test
    public void testParseObjectWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        ParsePosition parsePosition = new ParsePosition(0);
        Object parsedObject = dateFormat.parseObject(dateString, parsePosition);
        assertNotNull(parsedObject);
        assertEquals("Invalid parse position index", dateString.length(), parsePosition.getIndex());
        assertEquals("Invalid parsed object type", Date.class, parsedObject.getClass());
    }

    @org.junit.Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String pattern = dateFormat.getPattern();
        assertNotNull(pattern);
    }

    @org.junit.Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @org.junit.Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
    }

    @org.junit.Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertNotNull(maxLengthEstimate);
    }

    @org.junit.Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals("Objects should be equal", dateFormat1, dateFormat2);
    }

    @org.junit.Test
    public void testHashCode() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        int hashCode = dateFormat.hashCode();
        FastDateFormat dateFormat2 = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertEquals("Hashes should be equal", hashCode, dateFormat2.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String toString = dateFormat.toString();
        assertNotNull(toString);
    }

    @org.junit.Test
    public void testApplyRulesWithCalendarAndBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Calendar calendar = new GregorianCalendar(2022, 11, 25);
        StringBuffer stringBuffer = new StringBuffer(10);
        dateFormat.applyRules(calendar, stringBuffer);
        String formattedDate = stringBuffer.toString();
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatWithNullObject() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertThrows(NullPointerException.class, () -> dateFormat.format((Object)null));
    }

    @org.junit.Test
    public void testFormatWithNullStringBuffer() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertThrows(NullPointerException.class, () -> dateFormat.format((Object)null, (StringBuffer)null));
    }

    @org.junit.Test
    public void testParseWithNullString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertThrows(NullPointerException.class, () -> dateFormat.parse((String)null));
    }

    @org.junit.Test
    public void testParseObjectWithNullString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertThrows(NullPointerException.class, () -> dateFormat.parseObject((String)null));
    }

    @org.junit.Test
    public void testFormatWithLongMilliseconds() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM);
        long millis = ZonedDateTime.now().toInstant().toEpochMilli();
        String formattedDate = dateFormat.format(millis);
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatWithDate() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testFormatWithCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        Calendar calendar = new GregorianCalendar(2022, 11, 25);
        String formattedDate = dateFormat.format(calendar);
        assertEquals("Invalid formatted date", dateFormat.getPattern(), formattedDate.substring(0, dateFormat.getPattern().length()));
    }

    @org.junit.Test
    public void testParseWithParsePositionAndInvalidString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "Invalid";
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = dateFormat.parse(dateString, parsePosition);
        assertNull(parsedDate);
        assertEquals("Invalid parse position index", -1, parsePosition.getIndex());
    }

    @org.junit.Test
    public void testParseObjectWithParsePositionAndInvalidString() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "Invalid";
        ParsePosition parsePosition = new ParsePosition(0);
        Object parsedObject = dateFormat.parseObject(dateString, parsePosition);
        assertNull(parsedObject);
        assertEquals("Invalid parse position index", -1, parsePosition.getIndex());
    }

    @org.junit.Test
    public void testParseWithNullParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        Date parsedDate = dateFormat.parse(dateString, (ParsePosition)null);
        fail("Required ParseException to be thrown");
    }

    @org.junit.Test
    public void testParseObjectWithNullParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        String dateString = "25-12-2022";
        Object parsedObject = dateFormat.parseObject(dateString, (ParsePosition)null);
        fail("Required ParseException to be thrown");
    }
}