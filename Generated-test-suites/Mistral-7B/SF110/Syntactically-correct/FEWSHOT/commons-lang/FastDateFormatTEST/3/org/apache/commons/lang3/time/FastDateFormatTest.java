package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yy hh:mm a z");
        assertEquals("dd-MM-yy hh:mm a z", dateFormat.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yy hh:mm a z", timeZone);
        assertEquals("dd-MM-yy hh:mm a z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yy hh:mm a z", null, locale);
        assertEquals("dd-MM-yy hh:mm a z", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yy hh:mm a z", timeZone, locale);
        assertEquals("dd-MM-yy hh:mm a z", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance();
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy", dateFormat.getPattern());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(null, locale);
        assertEquals("dd/MM/yyyy", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(timeZone);
        assertEquals("dd/MM/yyyy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(null, locale, timeZone);
        assertEquals("dd/MM/yyyy", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance();
        assertNotNull(dateFormat);
        assertEquals("HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(null, locale);
        assertEquals("HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(timeZone);
        assertEquals("HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(null, locale, timeZone);
        assertEquals("HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertNotNull(dateFormat);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(timeZone);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithLocaleTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale, timeZone);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testFormat() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        System.out.println("Formatted Date: " + formattedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testFormatWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(timeZone);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        System.out.println("Formatted Date (TimeZone): " + formattedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testFormatWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        System.out.println("Formatted Date (Locale): " + formattedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testFormatWithLocaleTimeZone() {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale, timeZone);
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        System.out.println("Formatted Date (Locale and TimeZone): " + formattedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = dateFormat.parse(dateString);
        System.out.println("Parsed Date: " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testParseWithTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(timeZone);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = dateFormat.parse(dateString);
        System.out.println("Parsed Date (TimeZone): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseWithLocale() throws ParseException {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = dateFormat.parse(dateString);
        System.out.println("Parsed Date (Locale): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testParseWithLocaleTimeZone() throws ParseException {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale, timeZone);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = dateFormat.parse(dateString);
        System.out.println("Parsed Date (Locale and TimeZone): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseObject() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        System.out.println("Parsed Object: " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testParseObjectWithTimeZone() throws ParseException {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(timeZone);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        System.out.println("Parsed Object (TimeZone): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseObjectWithLocale() throws ParseException {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        System.out.println("Parsed Object (Locale): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
    }

    @Test
    public void testParseObjectWithLocaleTimeZone() throws ParseException {
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(null, locale, timeZone);
        String dateString = "25/05/2023 13:24:56 EDT";
        Date parsedDate = (Date) dateFormat.parseObject(dateString);
        System.out.println("Parsed Object (Locale and TimeZone): " + parsedDate);
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
        assertEquals(locale, dateFormat.getLocale());
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testGetPattern() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertEquals("dd/MM/yyyy HH:mm:ss zzz", dateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertNotNull(dateFormat.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertNotNull(dateFormat.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertEquals(32, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance();
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance();
        assertTrue(dateFormat1.equals(dateFormat2));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz");
        FastDateFormat dateFormat3 = new FastDateFormat("dd/MM/yyyy HH:mm:ss zzz", simpleDateFormat.getTimeZone(), Locale.US);
        assertFalse(dateFormat1.equals(dateFormat3));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getDateTimeInstance();
        FastDateFormat dateFormat2 = FastDateFormat.getDateTimeInstance();
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss zzz");
        FastDateFormat dateFormat3 = new FastDateFormat("dd/MM/yyyy HH:mm:ss zzz", simpleDateFormat.getTimeZone(), Locale.US);
        assertNotEquals(dateFormat1.hashCode(), dateFormat3.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance();
        assertEquals("FastDateFormat[dd/MM/yyyy HH:mm:ss zzz, null, null]", dateFormat.toString());
    }
}