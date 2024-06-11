package org.joda.time.format;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Chronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeParserBucket;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.text.ParsePosition;
import java.util.Locale;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterCreation() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        Assert.assertNull(formatter.getPrinter());
        Assert.assertNull(formatter.getParser());
        Assert.assertNull(formatter.getLocale());
        Assert.assertFalse(formatter.isParser());
        Assert.assertFalse(formatter.isPrinter());
        Assert.assertNull(formatter.getZone());
        Assert.assertNull(formatter.getChronology());
        Assert.assertNull(formatter.getPivotYear());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(null, null, Locale.US);
        Assert.assertEquals(Locale.US, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithPrinter = new DateTimeFormatter(null, new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        Assert.assertNotNull(formatterWithPrinter.getPrinter());
        Assert.assertNull(formatterWithPrinter.getParser());

        DateTimeFormatter formatterWithParser = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), null);
        Assert.assertNull(formatterWithParser.getPrinter());
        Assert.assertNotNull(formatterWithParser.getParser());

        DateTimeFormatter formatterWithZone = new DateTimeFormatter(null, null, Locale.US, DateTimeZone.UTC);
        Assert.assertEquals(DateTimeZone.UTC, formatterWithZone.getZone());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(null, null, Locale.US, true);
        Assert.assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithChronology = new DateTimeFormatter(null, null, Locale.US, null, DateTimeUtils.UTC_CHRONO);
        Assert.assertEquals(DateTimeUtils.UTC_CHRONO, formatterWithChronology.getChronology());

        DateTimeFormatter formatterWithPivotYear = new DateTimeFormatter(null, null, Locale.US, null, null, null, DateTimeConstants.DEFAULT_YEAR);
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatterWithPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterMethods() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, Locale.US, null, null, null, DateTimeConstants.DEFAULT_YEAR);
        Assert.assertFalse(formatter.isOffsetParsed());
        Assert.assertNull(formatter.getZone());
        Assert.assertNull(formatter.getChronology());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatter.getDefaultYear());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        Assert.assertTrue(formatterWithOffsetParsed.isOffsetParsed());
        Assert.assertNull(formatterWithOffsetParsed.getZone());
        Assert.assertNull(formatterWithOffsetParsed.getChronology());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatterWithOffsetParsed.getDefaultYear());

        DateTimeFormatter formatterWithZone = formatterWithOffsetParsed.withZone(DateTimeZone.forOffsetHours(1));
        Assert.assertNull(formatterWithZone.isOffsetParsed());
        Assert.assertEquals(DateTimeZone.forOffsetHours(1), formatterWithZone.getZone());
        Assert.assertNull(formatterWithZone.getChronology());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatterWithZone.getDefaultYear());

        DateTimeFormatter formatterWithLocale = formatterWithZone.withLocale(Locale.UK);
        Assert.assertEquals(Locale.UK, formatterWithLocale.getLocale());
        Assert.assertEquals(DateTimeZone.forOffsetHours(1), formatterWithLocale.getZone());
        Assert.assertNull(formatterWithLocale.getChronology());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatterWithLocale.getDefaultYear());

        DateTimeFormatter formatterWithChronology = formatterWithLocale.withChronology(DateTimeUtils.GREGORIAN_CHRONO);
        Assert.assertEquals(DateTimeUtils.GREGORIAN_CHRONO, formatterWithChronology.getChronology());
        Assert.assertEquals(Locale.UK, formatterWithChronology.getLocale());
        Assert.assertEquals(DateTimeZone.forOffsetHours(1), formatterWithChronology.getZone());
        Assert.assertEquals(DateTimeConstants.DEFAULT_YEAR, formatterWithChronology.getDefaultYear());

        DateTimeFormatter formatterWithPivotYear = formatterWithChronology.withPivotYear(2022);
        Assert.assertEquals(2022, formatterWithPivotYear.getPivotYear().intValue());
        Assert.assertEquals(DateTimeUtils.GREGORIAN_CHRONO, formatterWithPivotYear.getChronology());
        Assert.assertEquals(Locale.UK, formatterWithPivotYear.getLocale());
        Assert.assertEquals(DateTimeZone.forOffsetHours(1), formatterWithPivotYear.getZone());
        Assert.assertEquals(2022, formatterWithPivotYear.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterPrinting() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, Locale.US, null, null, null, DateTimeConstants.DEFAULT_YEAR);

        StringWriter writer = new StringWriter();
        Instant instant = new Instant(1640995200000L);

        formatter.printTo(writer, instant);
        Assert.assertEquals("2022-02-24T12:00:00.000Z", writer.toString());

        formatter = formatter.withZone(DateTimeZone.forOffsetHours(-5));
        formatter.printTo(writer, instant);
        Assert.assertEquals("2022-02-23T17:00:00.000-05:00", writer.toString());

        formatter = formatter.withLocale(Locale.UK);
        formatter.printTo(writer, instant);
        Assert.assertEquals("24-Feb-2022 12:00:00.000 GMT", writer.toString());

        formatter = formatter.withOffsetParsed();
        formatter.printTo(writer, instant);
        Assert.assertEquals("2022-02-24T12:00:00.000Z", writer.toString());

        formatter = formatter.withChronology(DateTimeUtils.BCT_CHRONO);
        formatter.printTo(writer, instant);
        Assert.assertEquals("-2022-02-24T12:00:00.000-01:20:00", writer.toString());

        formatter = formatter.withPivotYear(2023);
        formatter.printTo(writer, instant);
        Assert.assertEquals("-2023-02-24T12:00:00.000Z", writer.toString());
    }

    @Test
    public void testDateTimeFormatterParsing() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null, Locale.US, null, null, null, DateTimeConstants.DEFAULT_YEAR);

        Instant instant = DateTimeFormat.parseLocalDate("2022-02-24").toInstant();
        ParsePosition position = new ParsePosition(0);

        int result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-24");
        Assert.assertEquals(result, 13);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        LocalDate localDate = DateTimeFormat.parseLocalDate("2022-02-29");
        instant = localDate.toInstant();

        result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-29");
        Assert.assertEquals(result, 13);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        LocalDateTime localDateTime = new LocalDateTime(2022, 2, 24, 12, 30, 0);
        instant = localDateTime.toInstant();

        result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-24T12:30");
        Assert.assertEquals(result, 13);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        LocalDateTime localDateTimeWithOffset = new LocalDateTime(2022, 2, 24, 12, 30, 0).withOffsetHours(1);
        instant = localDateTimeWithOffset.toInstant();

        result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-24T13:30+01:00");
        Assert.assertEquals(result, 14);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        ParsePosition parsePosition = new ParsePosition(0);
        long millis = formatter.parseMillis("2022-02-24T12:30:00");
        Assert.assertEquals(millis, 1640998600000L);

        long millisWithOffset = formatter.parseMillis("2022-02-24T13:30:00+01:00");
        Assert.assertEquals(millisWithOffset, 1640999700000L);

        formatter = formatter.withOffsetParsed();

        instant = DateTimeFormat.parseLocalDate("2022-02-24").toInstant();
        result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-24");
        Assert.assertEquals(result, 13);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        result = formatter.parseInto(new InstantParsedPosition(instant), "2022-02-24T12:30:00+01:00");
        Assert.assertEquals(result, 14);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        formatter = formatter.withChronology(DateTimeUtils.BCT_CHRONO);

        long bctMillis = formatter.parseMillis("2022-02-24T12:30:00");
        Assert.assertEquals(bctMillis, -1640998600000L);

        formatter = formatter.withPivotYear(2023);

        instant = DateTimeFormat.parseLocalDate("2023-02-24").toInstant();
        result = formatter.parseInto(new InstantParsedPosition(instant), "2023-02-24");
        Assert.assertEquals(result, 13);
        Assert.assertEquals(instant, new InstantParsedPosition(instant).getInstant());

        millis = formatter.parseMillis("2023-02-24T12:30:00");
        Assert.assertEquals(millis, -1641024400000L);
    }

    private static class InstantParsedPosition extends ReadWritableInstant {
        private InstantParsedPosition(Instant instant) {
            super(instant, DateTimeUtils.UTC_CHRONO);
        }

        private Instant getInstant() {
            return this.getValue();
        }
    }
}