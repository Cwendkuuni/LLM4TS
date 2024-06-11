package org.joda.time.format.test;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.DateTimeUtils;
import org.joda.time.FormatUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeFormatterParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class DateTimeFormatterTest {

    @Test
    public void testDefaultFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter();

        // Test printing
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("2022-12-11T15:32:13.567Z", sb.toString());

        // Test parsing
        Instant instant = DateTimeUtils.parseDate("2022-12-11T15:32:13.567Z");
        DateTimeFormatter parsedFormatter = new DateTimeFormatter();
        ReadWritableInstant parsedInstant = parsedFormatter.parseLocalDateTime("2022-12-11T15:32:13.567Z").toInstant();
        assertEquals(instant.getMillis(), parsedInstant.getMillis());

        // Test formatter with locale
        DateTimeFormatter localeFormatter = formatter.withLocale(Locale.US);
        sb.setLength(0);
        localeFormatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("Sat, 11 Dec 2022 15:32:13 GMT", sb.toString());

        // Test formatter with offset parsed
        DateTimeFormatter offsetFormatter = formatter.withOffsetParsed();
        sb.setLength(0);
        offsetFormatter.printTo(sb, DateTimeUtils.currentTime().getMillis() + DateTimeZone.UTC.getOffset(DateTimeUtils.currentTime().getMillis()));
        assertEquals("2022-12-11T15:32:13.567+00:00", sb.toString());

        // Test formatter with chronology
        Chronology chronology = DateTimeUtils.getChronology("Europe/London");
        DateTimeFormatter chronologyFormatter = formatter.withChronology(chronology);
        sb.setLength(0);
        chronologyFormatter.printTo(sb, DateTimeUtils.currentTime().getMillis());
        assertEquals("2022-12-11T14:32:13.567+00:00", sb.toString());

        // Test formatter with zone
        DateTimeFormatter zoneFormatter = formatter.withZoneUTC();
        sb.setLength(0);
        zoneFormatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("2022-12-11T15:32:13.567Z", sb.toString());

        // Test error scenarios
        assertThrows(IllegalArgumentException.class, () -> formatter.printTo(new StringBuffer(), null));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseMillis(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalDate(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalDateTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseDateTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseMutableDateTime(""));
    }

    @Test
    public void testCustomFormatter() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFixedPattern("Z");
        DateTimeFormatter formatter = builder.toFormatter();

        // Test printing
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("2022-12-11T15:32:13.567Z", sb.toString());

        // Test parsing
        Instant instant = DateTimeUtils.parseDate("2022-12-11T15:32:13.567Z");
        ReadWritableInstant parsedInstant = formatter.parseLocalDateTime("2022-12-11T15:32:13.567Z").toInstant();
        assertEquals(instant.getMillis(), parsedInstant.getMillis());

        // Test formatter with locale
        DateTimeFormatter localeFormatter = formatter.withLocale(Locale.US);
        sb.setLength(0);
        localeFormatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("Sat, 11 Dec 2022 15:32:13 GMT", sb.toString());

        // Test formatter with offset parsed
        DateTimeFormatter offsetFormatter = formatter.withOffsetParsed();
        sb.setLength(0);
        offsetFormatter.printTo(sb, DateTimeUtils.currentTime().getMillis() + DateTimeZone.UTC.getOffset(DateTimeUtils.currentTime().getMillis()));
        assertEquals("2022-12-11T15:32:13.567+00:00", sb.toString());

        // Test formatter with chronology
        Chronology chronology = DateTimeUtils.getChronology("Europe/London");
        DateTimeFormatter chronologyFormatter = formatter.withChronology(chronology);
        sb.setLength(0);
        chronologyFormatter.printTo(sb, DateTimeUtils.currentTime().getMillis());
        assertEquals("2022-12-11T14:32:13.567+00:00", sb.toString());

        // Test formatter with zone
        DateTimeFormatter zoneFormatter = formatter.withZoneUTC();
        sb.setLength(0);
        zoneFormatter.printTo(sb, DateTimeUtils.currentTime());
        assertEquals("2022-12-11T15:32:13.567Z", sb.toString());

        // Test error scenarios
        assertThrows(IllegalArgumentException.class, () -> formatter.printTo(new StringBuffer(), null));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseMillis(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalDate(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseLocalDateTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseDateTime(""));
        assertThrows(IllegalArgumentException.class, () -> formatter.parseMutableDateTime(""));
    }

    @Test
    public void testParserWithPivotYear() {
        DateTimeFormatterParser parser = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFixedPattern("z")
                .parseDefaults()
                .parseLenient()
                .toParser();

        // Test parsing with pivot year
        assertEquals(2000, parser.parseLocalDateTime("2000-12-31T23:59:59.999z").getYear());

        // Test parsing with custom pivot year
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFixedPattern("z")
                .setPivotYear(2022)
                .toFormatter();
        DateTimeFormatterParser customParser = formatter.getParser();
        assertEquals(2022, customParser.parseLocalDateTime("2022-12-31T23:59:59.999z").getYear());
    }

    @Test
    public void testParserWithDefaultYear() {
        DateTimeFormatterParser parser = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFixedPattern("z")
                .parseDefaults()
                .parseLenient()
                .toParser();

        // Test parsing with default year
        assertEquals(2000, parser.parseLocalDateTime("2000-12-31T23:59:59.999z").getYear());

        // Test parsing with custom default year
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendFixedPattern("z")
                .setDefaultYear(2022)
                .toFormatter();
        DateTimeFormatterParser customParser = formatter.getParser();
        assertEquals(2022, customParser.parseLocalDateTime("2022-12-31T23:59:59.999z").getYear());
    }
}