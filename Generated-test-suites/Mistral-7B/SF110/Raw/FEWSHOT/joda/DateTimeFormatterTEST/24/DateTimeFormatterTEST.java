package org.joda.time.format;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testNewInstance() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        assertFalse(formatter.isPrinter());
        assertFalse(formatter.isParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChronology());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH);
        assertTrue(formatterWithLocale.isPrinter());
        assertTrue(formatterWithLocale.isParser());
        assertEquals(Locale.ENGLISH, formatterWithLocale.getLocale());
        assertFalse(formatterWithLocale.isOffsetParsed());
        assertNull(formatterWithLocale.getChronology());
        assertNull(formatterWithLocale.getZone());
        assertEquals(2000, formatterWithLocale.getDefaultYear());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, true);
        assertTrue(formatterWithOffsetParsed.isPrinter());
        assertTrue(formatterWithOffsetParsed.isParser());
        assertEquals(Locale.ENGLISH, formatterWithOffsetParsed.getLocale());
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
        assertNull(formatterWithOffsetParsed.getChronology());
        assertNull(formatterWithOffsetParsed.getZone());
        assertEquals(2000, formatterWithOffsetParsed.getDefaultYear());

        DateTimeFormatter formatterWithChronology = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, false, ChronologyUtils.INSTANCE);
        assertTrue(formatterWithChronology.isPrinter());
        assertTrue(formatterWithChronology.isParser());
        assertEquals(Locale.ENGLISH, formatterWithChronology.getLocale());
        assertFalse(formatterWithChronology.isOffsetParsed());
        assertEquals(ChronologyUtils.INSTANCE, formatterWithChronology.getChronology());
        assertNull(formatterWithChronology.getZone());
        assertEquals(2000, formatterWithChronology.getDefaultYear());

        DateTimeFormatter formatterWithZoneUTC = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, false, null, DateTimeZone.UTC);
        assertTrue(formatterWithZoneUTC.isPrinter());
        assertTrue(formatterWithZoneUTC.isParser());
        assertEquals(Locale.ENGLISH, formatterWithZoneUTC.getLocale());
        assertFalse(formatterWithZoneUTC.isOffsetParsed());
        assertNull(formatterWithZoneUTC.getChronology());
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
        assertEquals(2000, formatterWithZoneUTC.getDefaultYear());

        DateTimeFormatter formatterWithZoneCustom = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, false, null, DateTimeZone.forOffsetHours(2));
        assertTrue(formatterWithZoneCustom.isPrinter());
        assertTrue(formatterWithZoneCustom.isParser());
        assertEquals(Locale.ENGLISH, formatterWithZoneCustom.getLocale());
        assertFalse(formatterWithZoneCustom.isOffsetParsed());
        assertNull(formatterWithZoneCustom.getChronology());
        assertEquals(DateTimeZone.forOffsetHours(2), formatterWithZoneCustom.getZone());
        assertEquals(2000, formatterWithZoneCustom.getDefaultYear());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, formatterWithLocale.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        assertTrue(formatterWithOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithChronology = formatter.withChronology(ChronologyUtils.INSTANCE);
        assertEquals(ChronologyUtils.INSTANCE, formatterWithChronology.getChronology());
    }

    @Test
    public void testWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());
    }

    @Test
    public void testWithZoneCustom() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithZoneCustom = formatter.withZone(DateTimeZone.forOffsetHours(2));
        assertEquals(DateTimeZone.forOffsetHours(2), formatterWithZoneCustom.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2022);
        assertEquals(2022, formatterWithPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2023);
        assertEquals(2023, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = LocalDateTime.parse("2022-12-31T23:59:59.999");
        formatter.printTo(sb, localDateTime);
        assertEquals("2022-12-31T23:59:59.999", sb.toString());
    }

    @Test
    public void testPrintToWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, false, null, DateTimeZone.UTC, null, 2000);
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = LocalDateTime.parse("2022-12-31T23:59:59.999");
        formatter.printTo(sb, localDateTime);
        assertEquals("2022-12-31T23:59:59.999Z", sb.toString());
    }

    @Test
    public void testPrintToWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl(), Locale.ENGLISH, false, null, DateTimeZone.forOffsetHours(2), null, 2000);
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = LocalDateTime.parse("2022-12-31T23:59:59.999");
        formatter.printTo(sb, localDateTime);
        assertEquals("2022-12-31T23:59:59.999+02:00", sb.toString());
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        ReadWritableInstant readWritableInstant = new DateTime(1670761959000L, DateTimeUtils.UTC);
        int result = formatter.parseInto(readWritableInstant, "2022-12-31T23:59:59.999");
        assertEquals(result, s -> readWritableInstant.getMillis());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        long result = formatter.parseMillis("2022-12-31T23:59:59.999");
        assertEquals(result, 1670761959000L);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        LocalDate result = formatter.parseLocalDate("2022-12-31");
        assertEquals(result, LocalDate.of(2022, 12, 31));
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        LocalTime result = formatter.parseLocalTime("23:59:59.999");
        assertEquals(result, LocalTime.of(23, 59, 59, 999000000));
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        LocalDateTime result = formatter.parseLocalDateTime("2022-12-31T23:59:59.999");
        assertEquals(result, LocalDateTime.parse("2022-12-31T23:59:59.999"));
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        DateTime result = formatter.parseDateTime("2022-12-31T23:59:59.999");
        assertEquals(result, LocalDateTime.parse("2022-12-31T23:59:59.999").toDateTime());
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinterImpl(), new DateTimeParserImpl());
        MutableDateTime result = formatter.parseMutableDateTime("2022-12-31T23:59:59.999");
        assertEquals(result.getMillis(), LocalDateTime.parse("2022-12-31T23:59:59.999").toInstant().getMillis());
    }
}