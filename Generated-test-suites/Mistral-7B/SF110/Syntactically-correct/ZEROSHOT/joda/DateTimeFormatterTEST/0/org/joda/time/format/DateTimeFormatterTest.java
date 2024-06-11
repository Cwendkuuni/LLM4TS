package org.joda.time.format;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.format.*;

import java.text.ParsePosition;
import java.util.Locale;

import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterConstruction() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertNull(formatter.getLocale());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(null, null, Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithPrinter = new DateTimeFormatter(new SimpleDateTimeFormat(""), null);
        assertTrue(formatterWithPrinter.isPrinter());
        assertFalse(formatterWithPrinter.isParser());

        DateTimeFormatter formatterWithParser = new DateTimeFormatter(null, new SimpleDateTimeFormat(""));
        assertFalse(formatterWithParser.isPrinter());
        assertTrue(formatterWithParser.isParser());

        DateTimeFormatter formatterWithPrinterAndParser = new DateTimeFormatter(new SimpleDateTimeFormat(""), new SimpleDateTimeFormat(""));
        assertTrue(formatterWithPrinterAndParser.isPrinter());
        assertTrue(formatterWithPrinterAndParser.isParser());
    }

    @Test
    public void testDateTimeFormatterMethods() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(""), new SimpleDateTimeFormat(""));

        assertEquals(formatter, formatter.withLocale(Locale.US));
        assertEquals(Locale.US, formatter.withLocale(Locale.US).getLocale());
        assertFalse(formatter.withLocale(Locale.US).isOffsetParsed());
        assertFalse(formatter.withLocale(Locale.US).isChronoSet());

        assertTrue(formatter.withOffsetParsed().isOffsetParsed());
        assertFalse(formatter.withOffsetParsed().isChronoSet());

        assertNull(formatter.getChronology());
        DateTimeZone zone = DateTimeZone.forID("America/Los_Angeles");
        DateTimeFormatter formatterWithZone = formatter.withZone(zone);
        assertEquals(zone, formatterWithZone.getZone());

        assertFalse(formatter.isPivotYearSet());
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2005);
        assertTrue(formatterWithPivotYear.isPivotYearSet());
        assertEquals(Integer.valueOf(2005), formatterWithPivotYear.getPivotYear());

        assertFalse(formatter.isDefaultYearSet());
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2010);
        assertTrue(formatterWithDefaultYear.isDefaultYearSet());
        assertEquals(2010, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrinting() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss zzz"), null);
        DateTime dateTime = new DateTime(2005, 11, 30, 13, 45, 0, 0).withZone(DateTimeZone.forID("America/Los_Angeles"));

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        assertEquals("2005-11-30 13:45:00 PST", sb.toString());

        Writer writer = new StringWriter();
        formatter.printTo(writer, dateTime);
        assertEquals("2005-11-30 13:45:00 PST", writer.toString());

        Appendable appendable = new StringBuffer();
        formatter.printTo(appendable, dateTime);
        assertEquals("2005-11-30 13:45:00 PST", appendable.toString());

        sb.setLength(0);
        formatter.printTo(sb, dateTime.getMillis());
        assertEquals("2005-11-30 13:45:00.000 PST", sb.toString());

        writer.getBuffer().setLength(0);
        formatter.printTo(writer, dateTime.getMillis());
        assertEquals("2005-11-30 13:45:00.000 PST", writer.toString());

        appendable.setLength(0);
        formatter.printTo(appendable, dateTime.getMillis());
        assertEquals("2005-11-30 13:45:00.000 PST", appendable.toString());

        sb.setLength(0);
        formatter.printTo(sb, new LocalDateTime(2005, 11, 30, 13, 45));
        assertEquals("2005-11-30 13:45", sb.toString());

        writer.getBuffer().setLength(0);
        formatter.printTo(writer, new LocalDateTime(2005, 11, 30, 13, 45));
        assertEquals("2005-11-30 13:45", writer.toString());

        appendable.setLength(0);
        formatter.printTo(appendable, new LocalDateTime(2005, 11, 30, 13, 45));
        assertEquals("2005-11-30 13:45", appendable.toString());

        sb.setLength(0);
        formatter.printTo(sb, new LocalDateTime(2005, 11, 30).toLocalDate());
        assertEquals("2005-11-30", sb.toString());

        writer.getBuffer().setLength(0);
        formatter.printTo(writer, new LocalDateTime(2005, 11, 30).toLocalDate());
        assertEquals("2005-11-30", writer.toString());

        appendable.setLength(0);
        formatter.printTo(appendable, new LocalDateTime(2005, 11, 30).toLocalDate());
        assertEquals("2005-11-30", appendable.toString());

        sb.setLength(0);
        formatter.printTo(sb, new LocalTime(13, 45));
        assertEquals("13:45", sb.toString());

        writer.getBuffer().setLength(0);
        formatter.printTo(writer, new LocalTime(13, 45));
        assertEquals("13:45", writer.toString());

        appendable.setLength(0);
        formatter.printTo(appendable, new LocalTime(13, 45));
        assertEquals("13:45", appendable.toString());
    }

    @Test
    public void testParsing() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss zzz"), null);

        LocalDateTime parsedDateTime = formatter.parseLocalDateTime("2005-11-30 13:45:00 PST");
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withZone(DateTimeZone.forID("America/Los_Angeles")), parsedDateTime.toDateTimeAtOffset());

        MutableDateTime mutableDateTime = new MutableDateTime();
        int parsed = formatter.parseInto(mutableDateTime, "2005-11-30 13:45:00 PST", 0);
        assertEquals(15, parsed);
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withZone(DateTimeZone.forID("America/Los_Angeles")), mutableDateTime.toDateTimeAtOffset());

        LocalDateTime parsedLocalDateTime = formatter.parseLocalDateTime("2005-11-30 13:45:00");
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0), parsedLocalDateTime.toDateTimeAtOffset());

        MutableDateTime parsedMutableDateTime = new MutableDateTime();
        int parsedInto = formatter.parseInto(parsedMutableDateTime, "2005-11-30 13:45:00", 0);
        assertEquals(15, parsedInto);
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0), parsedMutableDateTime.toDateTimeAtOffset());

        LocalDateTime parsedLocalDateTimeFromMillis = formatter.parseLocalDateTime("2005-11-30 13:45:00.000");
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withMillisOfSecond(0), parsedLocalDateTimeFromMillis.toDateTimeAtOffset());

        MutableDateTime parsedMutableDateTimeFromMillis = new MutableDateTime();
        int parsedIntoFromMillis = formatter.parseInto(parsedMutableDateTime, "2005-11-30 13:45:00.000", 0);
        assertEquals(15, parsedIntoFromMillis);
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withMillisOfSecond(0), parsedMutableDateTime.toDateTimeAtOffset());

        ParsePosition parsePosition = new ParsePosition(0);
        DateTime parsedDateTimeFromString = formatter.parseDateTime("2005-11-30 13:45:00 PST", parsePosition);
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withZone(DateTimeZone.forID("America/Los_Angeles")), parsedDateTimeFromString);
        assertEquals(parsedDateTimeFromString.toString().length(), parsePosition.getIndex());

        MutableDateTime parsedMutableDateTimeFromString = new MutableDateTime();
        int parsedIntoFromString = formatter.parseInto(parsedMutableDateTime, "2005-11-30 13:45:00 PST", 0);
        assertEquals(15, parsedIntoFromString);
        assertEquals(new DateTime(2005, 11, 30, 13, 45, 0, 0).withZone(DateTimeZone.forID("America/Los_Angeles")), parsedMutableDateTime.toDateTimeAtOffset());
        assertEquals(parsedDateTimeFromString.toString().length(), parsePosition.getIndex());

        LocalDateTime parsedLocalDateTimeFromMillisWithZone = formatter.parseLocalDateTime("2005-11-30 13:45:00.000 +0800");
        assertEquals(new DateTime(2005, 11, 30, 21, 45, 0, 0), parsedLocalDateTimeFromMillisWithZone.toDateTimeAtOffset());

        MutableDateTime parsedMutableDateTimeFromMillisWithZone = new MutableDateTime();
        int parsedIntoFromMillisWithZone = formatter.parseInto(parsedMutableDateTime, "2005-11-30 13:45:00.000 +0800", 0);
        assertEquals(15, parsedIntoFromMillisWithZone);
        assertEquals(new DateTime(2005, 11, 30, 21, 45, 0, 0), parsedMutableDateTime.toDateTimeAtOffset());
    }
}