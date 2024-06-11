package org.joda.time.format.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.FormatUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadWritableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Locale;

public class DateTimeFormatterTest {

    @Test
    public void testDefaultFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter();

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2000-01-01 00:00:00.000Z", writer.toString());

        // Test parsing
        ReadableInstant instants[] = {
                new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC),
                new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC.withOffsetHours(1)),
                new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC.withOffsetHours(-1))};
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }

        // Test parsing with offset and zone
        ReadableInstant instantsWithOffset = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.forOffsetHours(1));
        MutableDateTime mutableDateTimeWithOffset = new MutableDateTime();
        int resultWithOffset = formatter.parseInto(mutableDateTimeWithOffset, formatter.print(instantsWithOffset), 0);
        Assert.assertEquals(instantsWithOffset.getZone(), mutableDateTimeWithOffset.getZone());

        // Test parsing with invalid input
        try {
            formatter.parseInto(new MutableDateTime(), "invalid", 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test parsing with invalid format
        try {
            formatter.parseInto(new MutableDateTime(), "2000-01-02-31", 0);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Unable to parse '2000-01-02-31': expected 'T' but was '-\' at index 10", e.getMessage());
        }
    }

    @Test
    public void testWithLocaleFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withLocale(Locale.FRANCE);

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("01/01/2000 à 0 h 0 min 0 s", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testWithOffsetParsedFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withOffsetParsed();

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2000-01-01T00:00:00Z", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }

        // Test parsing with offset
        MutableDateTime mutableDateTimeWithOffset = new MutableDateTime();
        int resultWithOffset = formatter.parseInto(mutableDateTimeWithOffset, "2000-01-01T01:00:00", 0);
        Assert.assertEquals(new BaseDateTime(0, 2000, 1, 1, 1, 0, 0, DateTimeZone.UTC.withOffsetHours(1)).toInstant(), mutableDateTimeWithOffset.toInstant());
    }

    @Test
    public void testWithChronologyFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withChronology(DateTimeUtils.JULIAN_DATE_CHRONOLOGY);

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("719168.5", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testWithZoneUTCFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withZoneUTC();

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2000-01-01T00:00:00Z", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testWithZoneFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withZone(DateTimeZone.forOffsetHours(1));

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2000-01-01T01:00:00+01:00", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testWithPivotYearFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withPivotYear(2005);

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2005-01-01T00:00:00Z", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testWithDefaultYearFormatter() {
        DateTimeFormatter formatter = new DateTimeFormatter().withDefaultYear(2005);

        // Test printing
        StringWriter writer = new StringWriter();
        formatter.printTo(writer, new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC));
        Assert.assertEquals("2005-01-01T00:00:00Z", writer.toString());

        // Test parsing
        for (ReadableInstant inst : instants) {
            MutableDateTime mutableDateTime = new MutableDateTime();
            int result = formatter.parseInto(mutableDateTime, formatter.print(inst), 0);
            Assert.assertEquals(inst, mutableDateTime.toInstant());
        }
    }

    @Test
    public void testParsePartial() {
        DateTimeFormatter formatter = new DateTimeFormatter();

        // Test parsing LocalDate
        ReadablePartial readablePartial = new BaseDateTime(0, 2000, 1, 1).toLocalDate();
        LocalDateTime localDateTime = formatter.parseLocalDateTime(formatter.print(readablePartial));
        Assert.assertEquals(new LocalDateTime(2000, 1, 1), localDateTime);

        // Test parsing LocalTime
        ReadablePartial readablePartialTime = new BaseDateTime(0, 2000, 1, 1, 1).toLocalTime();
        LocalDateTime localDateTimeTime = formatter.parseLocalDateTime(formatter.print(readablePartialTime));
        Assert.assertEquals(new LocalDateTime(2000, 1, 1, 1), localDateTimeTime);

        // Test parsing DateTime
        ReadablePartial readablePartialDateTime = new BaseDateTime(0, 2000, 1, 1, 1, 1).toDateTime();
        DateTime datetime = formatter.parseDateTime(formatter.print(readablePartialDateTime));
        Assert.assertEquals(new DateTime(DateTimeZone.UTC.getMillis() + 60 * 60 * 1000), datetime);
    }

    @Test
    public void testParseReadWritableInstant() {
        DateTimeFormatter formatter = new DateTimeFormatter();

        // Test parsing with offset and zone
        ReadWritableInstant readWritableInstant = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.forOffsetHours(1)).toMutableInstance();
        ReadableInstant instantsWithOffset = readWritableInstant.toReadableInstant();
        MutableDateTime mutableDateTimeWithOffset = new MutableDateTime();
        int resultWithOffset = formatter.parseInto(mutableDateTimeWithOffset, formatter.print(instantsWithOffset), 0);
        Assert.assertEquals(instantsWithOffset, mutableDateTimeWithOffset.toInstant());

        // Test parsing with zone
        ReadWritableInstant readWritableInstantZone = new BaseDateTime(0, 2000, 1, 1, 0, 0, 0, DateTimeZone.UTC).toMutableInstance();
        ReadableInstant instantsZone = readWritableInstantZone.toReadableInstant();
        MutableDateTime mutableDateTimeZone = new MutableDateTime();
        int resultZone = formatter.parseInto(mutableDateTimeZone, formatter.print(instantsZone), 0);
        Assert.assertEquals(instantsZone, mutableDateTimeZone.toInstant());

        // Test parsing with invalid input
        try {
            formatter.parseInto(new MutableDateTime(), "invalid", 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}