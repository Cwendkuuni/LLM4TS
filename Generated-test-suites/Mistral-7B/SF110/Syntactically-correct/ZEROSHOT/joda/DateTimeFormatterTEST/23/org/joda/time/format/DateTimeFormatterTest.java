import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Assert.*;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterConstruction() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        Assert.assertNull(formatter.getLocale());
        Assert.assertFalse(formatter.isOffsetParsed());
        Assert.assertNull(formatter.getChronology());
        Assert.assertNull(formatter.getZone());
        Assert.assertNull(formatter.getPivotYear());
        Assert.assertEquals(2000, formatter.getDefaultYear());

        DateTimeFormatter formatterWithLocale = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), Locale.ENGLISH);
        Assert.assertEquals(Locale.ENGLISH, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithOffsetParsed = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, true, null, null, null, 2000);
        Assert.assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithChronology = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, false, GregorianChronology.getInstance(), null, null, 2000);
        Assert.assertEquals(GregorianChronology.getInstance(), formatterWithChronology.getChronology());

        DateTimeFormatter formatterWithZone = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, false, null, DateTimeZone.forOffsetHours(-5), null, 2000);
        Assert.assertEquals(DateTimeZone.forOffsetHours(-5), formatterWithZone.getZone());

        DateTimeFormatter formatterWithPivotYear = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, false, null, null, new Integer(2020), 2000);
        Assert.assertEquals(new Integer(2020), formatterWithPivotYear.getPivotYear());

        DateTimeFormatter formatterWithDefaultYear = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser(), null, false, null, null, null, 2100);
        Assert.assertEquals(2100, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterMethods() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());

        Assert.assertTrue(formatter.isPrinter());
        Assert.assertTrue(formatter.isParser());
        Assert.assertNull(formatter.getPrinter());
        Assert.assertNull(formatter.getParser());

        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.ENGLISH);
        Assert.assertEquals(Locale.ENGLISH, formatterWithLocale.getLocale());

        DateTimeFormatter formatterWithOffsetParsed = formatter.withOffsetParsed();
        Assert.assertTrue(formatterWithOffsetParsed.isOffsetParsed());

        DateTimeFormatter formatterWithChronology = formatter.withChronology(GregorianChronology.getInstance());
        Assert.assertEquals(GregorianChronology.getInstance(), formatterWithChronology.getChronology());

        DateTimeFormatter formatterWithZoneUTC = formatter.withZoneUTC();
        Assert.assertEquals(DateTimeZone.UTC, formatterWithZoneUTC.getZone());

        DateTimeFormatter formatterWithZone = formatter.withZone(DateTimeZone.forOffsetHours(-5));
        Assert.assertEquals(DateTimeZone.forOffsetHours(-5), formatterWithZone.getZone());

        DateTimeFormatter formatterWithPivotYear = formatter.withPivotYear(2020);
        Assert.assertEquals(new Integer(2020), formatterWithPivotYear.getPivotYear());

        DateTimeFormatter formatterWithDefaultYear = formatter.withDefaultYear(2100);
        Assert.assertEquals(2100, formatterWithDefaultYear.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterPrinting() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        StringDateFormat sdf = new StringDateFormat();
        Instant instant = new Instant(System.currentTimeMillis());

        String formattedDateTime = formatter.print(instant);
        String expectedDateTime = sdf.toString(instant);
        Assert.assertEquals(expectedDateTime, formattedDateTime);

        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, instant);
        Assert.assertEquals(expectedDateTime, sb.toString());

        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            formatter.printTo(pw, instant);
        }
        Assert.assertEquals(expectedDateTime, sw.toString());

        StringBuilder sbAppendable = new StringBuilder();
        formatter.printTo(sbAppendable, instant);
        Assert.assertEquals(expectedDateTime, sbAppendable.toString());

        // Test printing with locale
        DateTimeFormatter formatterWithLocale = formatter.withLocale(Locale.FRENCH);
        String formattedDateTimeWithLocale = formatterWithLocale.print(instant);
        Assert.assertNotEquals(expectedDateTime, formattedDateTimeWithLocale);
    }

    @Test
    public void testDateTimeFormatterParsing() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());

        Instant parsedInstant = new Instant(0L);
        long millis = formatter.parseMillis("2021-02-28 14:30:05.123");
        parsedInstant.setMillis(millis);
        Assert.assertEquals(new DateTime(2021, 2, 28, 14, 30, 5, 123, DateTimeZone.UTC), new DateTime(parsedInstant.getMillis()));

        ReadWritableInstant rwi = new Instant();
        int parsedInt = formatter.parseInto(rwi, "2021-02-28 14:30:05.123", 0);
        Assert.assertEquals(parsedInt, 18);
        Assert.assertEquals(new DateTime(2021, 2, 28, 14, 30, 5, 123, DateTimeZone.UTC), rwi.toDateTime());

        LocalDateTime localDateTime = formatter.parseLocalDateTime("2021-02-28 14:30:05.123");
        Assert.assertEquals(new DateTime(2021, 2, 28, 14, 30, 5, 123, DateTimeZone.UTC).toLocalDateTime(), localDateTime);

        LocalDate localDate = formatter.parseLocalDate("2021-02-28");
        Assert.assertEquals(new DateTime(2021, 2, 28, 0, 0, 0, 0, DateTimeZone.UTC).toLocalDate(), localDate);

        LocalTime localTime = formatter.parseLocalTime("14:30:05.123");
        Assert.assertEquals(new DateTime(0, 0, 0, 14, 30, 5, 123, DateTimeZone.UTC).toLocalTime(), localTime);

        DateTime parsedDateTime = formatter.parseDateTime("2021-02-28 14:30:05.123");
        Assert.assertEquals(new DateTime(2021, 2, 28, 14, 30, 5, 123, DateTimeZone.UTC), parsedDateTime);

        MutableDateTime mutableDateTime = formatter.parseMutableDateTime("2021-02-28 14:30:05.123");
        Assert.assertEquals(new DateTime(2021, 2, 28, 14, 30, 5, 123, DateTimeZone.UTC), mutableDateTime.toDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeFormatterParsingException() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormatter(), new BasicDateTimeParser());
        formatter.parseMillis("Not a valid date");
    }
}