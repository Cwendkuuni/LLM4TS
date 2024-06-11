package org.joda.time.format;

import org.joda.time.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testEmptyConstructor() {
        DateTimeFormatter formatter = new DateTimeFormatter();
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testConstructorWithPrinterAndParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
        assertTrue(formatter.isPrinter());
        assertTrue(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithZone = formatter.withZoneUTC();
        assertNotNull(formatterWithZone.getZone());
        assertEquals(DateTimeZone.UTC, formatterWithZone.getZone());

        DateTimeFormatter formatterWithChronology = formatter.withChronology(ISODateTimeFormat.dateTimeParser());
        assertNotNull(formatterWithChronology.getChronology());
        assertEquals(ISODateTimeFormat.dateTimeParser().getChronology(), formatterWithChronology.getChronology());
    }

    @Test
    public void testPrinting() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30);
        String expected = "2022-01-01T12:30";

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime.toInstant());
        assertEquals(expected, sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, dateTime.toInstant());
            assertEquals(expected, writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        Appendable appendable = new StringWriter();
        try {
            formatter.printTo(appendable, dateTime.toInstant());
            assertEquals(expected, appendable.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        StringBuffer sb2 = new StringBuffer(100);
        formatter.printTo(sb2, 1662332800000L); //Mon Dec 27 15:00:00 CET 2021
        assertEquals("2021-12-27T15:00", sb2.toString());
    }

    @Test
    public void testParsing() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());

        long millis = DateTimeUtils.parseISODate("2022-01-01T12:30:00Z").getMillis();
        LocalDateTime dateTime = formatter.parseLocalDateTime("2022-01-01T12:30");
        assertEquals(new LocalDateTime(2022, 1, 1, 12, 30), dateTime);

        ReadWritableInstant instant = new DateTime();
        formatter.parseInto(instant, "2022-01-01T12:30:00Z", 0);
        assertEquals(millis, instant.getMillis());

        try {
            formatter.parseInto(instant, "2022-01-01T12:60:00Z", 0); //Invalid input
            fail();
        } catch (IllegalArgumentException e) {
            //Expected
        }

        String invalidInput = "InvalidInput";
        try {
            formatter.parseInto(instant, invalidInput, 0); //Invalid input
            fail();
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            formatter.parseMillis("InvalidInput"); //Invalid input
            fail();
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testParsingWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DefaultDateTimeFormatter(), new DefaultDateTimeParser());
        ISODateTimeFormat isoFormatter = ISODateTimeFormat.dateTimeParser();
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 12, 30);

        LocalDateTime parsedDateTime = formatter.parseLocalDateTime("2022-01-01T12:30");
        assertEquals(dateTime, parsedDateTime);

        Chronology chronology = isoFormatter.getChronology();
        LocalDateTime parsedDateTimeWithChronology = formatter.parseLocalDateTime("2022-01-01T12:30", chronology);
        assertEquals(dateTime, parsedDateTimeWithChronology);
    }
}