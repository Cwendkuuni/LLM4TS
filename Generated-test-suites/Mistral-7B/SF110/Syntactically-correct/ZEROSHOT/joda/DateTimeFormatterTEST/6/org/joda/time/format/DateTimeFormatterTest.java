import org.joda.time.*;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testDateTimeFormatterInitialization() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testDateTimeFormatterWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, withLocale.getLocale());
    }

    @Test
    public void testDateTimeFormatterWithOffsetParsed() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeFormatter withOffsetParsed = formatter.withOffsetParsed();
        assertTrue(withOffsetParsed.isOffsetParsed());
    }

    @Test
    public void testDateTimeFormatterWithChronology() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeZone zone = DateTimeZone.forID("America/New_York");
        Chronology chronology = GregorianChronology.getInstance(zone);
        DateTimeFormatter withChronology = formatter.withChronology(chronology);
        assertEquals(chronology, withChronology.getChronology());
    }

    @Test
    public void testDateTimeFormatterWithZoneUTC() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeFormatter withZoneUTC = formatter.withZoneUTC();
        assertEquals(DateTimeZone.UTC, withZoneUTC.getZone());
    }

    @Test
    public void testDateTimeFormatterWithPivotYear() {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        DateTimeFormatter withPivotYear = formatter.withPivotYear(2010);
        assertEquals(2010, withPivotYear.getPivotYear().intValue());
    }

    @Test
    public void testDateTimeFormatterPrinting() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        LocalDateTime dateTime = new LocalDateTime(2001, 1, 1, 12, 30, 0);
        String expected = "2001-01-01T12:30:00Z";
        StringBuffer sb = new StringBuffer(100);
        formatter.printTo(sb, dateTime.toInstant().toEpochMilli(), formatter.getChronology());
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testDateTimeFormatterParsing() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2001-01-01T12:30:00Z";
        LocalDateTime dateTime = formatter.parseLocalDateTime(dateString);
        assertEquals("2001-01-01T12:30:00.000Z", dateTime.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeFormatterParsingInvalidFormat() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser());
        String dateString = "2001-01-01T12:30:";
        formatter.parseLocalDateTime(dateString);
    }

    @Test
    public void testDateTimeFormatterParsingWithOffset() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser())
                .withOffsetParsed().withZone(DateTimeZone.forID("America/New_York"));
        String dateString = "2001-01-01T12:30:00-05:00";
        LocalDateTime dateTime = formatter.parseLocalDateTime(dateString);
        assertEquals("2001-01-01T17:30:00.000-04:00", dateTime.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeFormatterParsingWithInvalidOffset() throws Exception {
        DateTimeFormatter formatter = new DateTimeFormatter(new DateTimePrinter(), new DateTimeParser())
                .withOffsetParsed().withZone(DateTimeZone.forID("America/New_York"));
        String dateString = "2001-01-01T12:30:00-06:01";
        formatter.parseLocalDateTime(dateString);
    }
}