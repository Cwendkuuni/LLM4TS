package org.joda.time.format;

import org.junit.Assert;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateTimeParser());
        Assert.assertFalse(formatter.isPrinter());
        Assert.assertFalse(formatter.isParser());
        formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertTrue(formatter.isPrinter());
        Assert.assertTrue(formatter.isParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertNull(formatter.getLocale());
        formatter = formatter.withLocale(Locale.US);
        Assert.assertNotNull(formatter.getLocale());
        Assert.assertEquals(Locale.US, formatter.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertFalse(formatter.isOffsetParsed());
        formatter = formatter.withOffsetParsed();
        Assert.assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Chronology chrono = GregorianChronology.getInstance();
        Assert.assertEquals(chrono, formatter.getChronology());
        formatter = formatter.withChronology(ISOChronology.getInstance());
        Assert.assertNotSame(chrono, formatter.getChronology());
        Assert.assertEquals(ISOChronology.getInstance(), formatter.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertNull(formatter.getZone());
        formatter = formatter.withZoneUTC();
        Assert.assertEquals(DateTimeZone.UTC, formatter.getZone());
    }

    @Test
    public void testWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        DateTimeZone timeZone = DateTimeZone.forID("America/Los_Angeles");
        Assert.assertNull(formatter.getZone());
        formatter = formatter.withZone(timeZone);
        Assert.assertEquals(timeZone, formatter.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertNull(formatter.getPivotYear());
        formatter = formatter.withPivotYear(DateTimeConstants.MIN_YEAR);
        Assert.assertEquals(DateTimeConstants.MIN_YEAR, formatter.getPivotYear().intValue());
        formatter = formatter.withPivotYear(DateTimeConstants.MAX_YEAR);
        Assert.assertEquals(DateTimeConstants.MAX_YEAR, formatter.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        Assert.assertEquals(2000, formatter.getDefaultYear());
        formatter = formatter.withDefaultYear(1999);
        Assert.assertEquals(1999, formatter.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        String expected = "1999-12-31 23:59:59.999";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        StringWriter sw = new StringWriter();
        formatter.printTo(sw, dateTime);
        String expected = "1999-12-31 23:59:59.999";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.printTo(appendable, dateTime);
        String expected = "1999-12-31 23:59:59.999";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999, ISOChronology.getInstance());
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        String expected = "1999-12-31T23:59:59.999Z";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999, ISOChronology.getInstance());
        StringWriter sw = new StringWriter();
        formatter.printTo(sw, dateTime);
        String expected = "1999-12-31T23:59:59.999Z";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendableWithChronology() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999, ISOChronology.getInstance());
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.printTo(appendable, dateTime);
        String expected = "1999-12-31T23:59:59.999Z";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        formatter = formatter.withOffsetParsed();
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        String expected = "1999-12-31T23:59:59.999-05:00";
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterWithOffsetParsed() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        formatter = formatter.withOffsetParsed();
        StringWriter sw = new StringWriter();
        formatter.printTo(sw, dateTime);
        String expected = "1999-12-31T23:59:59.999-05:00";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendableWithOffsetParsed() throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimePrinter(), new SimpleDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(1999, 12, 31, 23, 59, 59, 999);
        formatter = formatter.withOffsetParsed();
        StringWriter sw = new StringWriter();
        Appendable appendable = sw;
        formatter.printTo(appendable, dateTime);
        String expected = "1999-12-31T23:59:59.999-05:00";
        Assert.assertEquals(expected, sw.toString());
    }

    @Test
    public void testParseIntoReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        ReadWritableInstant inst = new GregorianInstant(2000, 12, 31, 23, 59, 59, 999);
        String testString = "2000-12-31 23:59:59.999";
        int result = formatter.parseInto(inst, testString, 0);
        Assert.assertEquals(testString.length(), result);
        Assert.assertEquals(inst.getMillis(), DateTimeUtils.parseLocalDate("2000-12-31").toInstanceInLocal().getMillis());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        long testMillis = DateTimeUtils.parseLocalDate("2000-12-31").toInstanceInLocal().getMillis();
        String testString = "2000-12-31 23:59:59.999";
        long result = formatter.parseMillis(testString);
        Assert.assertEquals(testMillis, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        String testString = "2000-12-31 23:59:59.999ABC";
        formatter.parseMillis(testString);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd"), new SimpleDateTimeParser());
        LocalDate date = LocalDate.parse("2000-12-31");
        Assert.assertEquals(date.getYear(), 2000);
        Assert.assertEquals(date.getMonthOfYear(), 12);
        Assert.assertEquals(date.getDayOfMonth(), 31);
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss.SSS"), new SimpleDateTimeParser());
        LocalTime time = LocalTime.parse("23:59:59.999");
        Assert.assertEquals(time.getHourOfDay(), 23);
        Assert.assertEquals(time.getMinute(), 59);
        Assert.assertEquals(time.getSecondOfMinute(), 59);
        Assert.assertEquals(time.getMillisecondOfSecond(), 999);
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        LocalDateTime dateTime = LocalDateTime.parse("2000-12-31 23:59:59.999");
        Assert.assertEquals(dateTime.getYear(), 2000);
        Assert.assertEquals(dateTime.getMonthOfYear(), 12);
        Assert.assertEquals(dateTime.getDayOfMonth(), 31);
        Assert.assertEquals(dateTime.getHourOfDay(), 23);
        Assert.assertEquals(dateTime.getMinute(), 59);
        Assert.assertEquals(dateTime.getSecondOfMinute(), 59);
        Assert.assertEquals(dateTime.getMillisecondOfSecond(), 999);
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        DateTime dateTime = formatter.parseDateTime("2000-12-31 23:59:59.999");
        Assert.assertEquals(dateTime.getYear(), 2000);
        Assert.assertEquals(dateTime.getMonthOfYear(), 12);
        Assert.assertEquals(dateTime.getDayOfMonth(), 31);
        Assert.assertEquals(dateTime.getHourOfDay(), 23);
        Assert.assertEquals(dateTime.getMinute(), 59);
        Assert.assertEquals(dateTime.getSecondOfMinute(), 59);
        Assert.assertEquals(dateTime.getMillisecondOfSecond(), 999);
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2000-12-31 23:59:59.999");
        Assert.assertEquals(mutableDateTime.getYear(), 2000);
        Assert.assertEquals(mutableDateTime.getMonthOfYear(), 12);
        Assert.assertEquals(mutableDateTime.getDayOfMonth(), 31);
        Assert.assertEquals(mutableDateTime.getHourOfDay(), 23);
        Assert.assertEquals(mutableDateTime.getMinute(), 59);
        Assert.assertEquals(mutableDateTime.getSecondOfMinute(), 59);
        Assert.assertEquals(mutableDateTime.getMillisecondOfSecond(), 999);
    }
}