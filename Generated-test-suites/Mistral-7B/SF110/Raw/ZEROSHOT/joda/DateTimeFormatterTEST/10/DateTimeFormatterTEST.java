import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterConstruction() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, withLocale.getLocale());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testDateTimeFormatterWithZone() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeZone zone = DateTimeZone.forID("Europe/London");
        DateTimeFormatter withZone = formatter.withZone(zone);
        assertEquals(zone, withZone.getZone());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2010);
        assertEquals(2010, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterWithDefaultYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeFormatter withDefaultYear = formatter.withDefaultYear(2020);
        assertEquals(2020, withDefaultYear.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterPrinting() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        LocalDateTime dateTime = LocalDateTime.parse("2022-01-01T12:34:56");
        StringBuffer sb = new StringBuffer();
        formatter.printTo(sb, dateTime);
        assertEquals("2022-01-01T12:34:56", sb.toString());

        StringWriter writer = new StringWriter();
        formatter.printTo(writer, dateTime);
        assertEquals("2022-01-01T12:34:56", writer.toString());

        StringWriter appendable = new StringWriter();
        formatter.printTo(appendable, dateTime);
        assertEquals("2022-01-01T12:34:56", appendable.toString());
    }

    @Test
    public void testDateTimeFormatterParsing() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        LocalDateTime parsed = formatter.parseLocalDateTime("2022-01-01T12:34:56");
        assertEquals("2022-01-01T12:34:56", formatter.print(parsed));

        long millis = DateTimeUtils.parseISODate("2022-01-01T12:34:56.123").getMillis();
        LocalDateTime parsedMillis = formatter.parseLocalDateTime("2022-01-01T12:34:56.123");
        assertEquals("2022-01-01T12:34:56.123", formatter.print(parsedMillis));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeFormatterParsingInvalidFormat() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        formatter.parseLocalDateTime("InvalidFormat");
    }

    @Test
    public void testDateTimeFormatterParsingWithZone() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        DateTimeZone zone = DateTimeZone.forID("Europe/London");
        formatter = formatter.withZone(zone);
        LocalDateTime parsed = formatter.parseLocalDateTime("2022-01-01T12:34:56");
        assertEquals("2022-01-01T12:34:56+00:00", formatter.print(parsed));

        LocalDateTime parsedWithOffset = formatter.parseLocalDateTime("2022-01-01T11:34:56");
        assertEquals("2022-01-01T12:34:56+01:00", formatter.print(parsedWithOffset));
    }

    @Test
    public void testDateTimeFormatterParsingWithChronology() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        Chronology chronology = ISOChronology.getInstance();
        formatter = formatter.withChronology(chronology);
        Instant instant = new Instant(1640995200000L);
        MutableDateTime parsed = formatter.parseMutableDateTime("2022-01-01T12:34:40Z");
        assertEquals(instant, parsed.toInstant());
    }

    @Test
    public void testDateTimeFormatterParsingWithOffset() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        formatter = formatter.withOffsetParsed();
        Instant instant = new Instant(1640995200000L);
        MutableDateTime parsed = formatter.parseMutableDateTime("2022-01-01T12:34:40+01:00");
        assertEquals(instant.getMillis() + (60 * 60 * 1000), parsed.getMillis());
    }
}