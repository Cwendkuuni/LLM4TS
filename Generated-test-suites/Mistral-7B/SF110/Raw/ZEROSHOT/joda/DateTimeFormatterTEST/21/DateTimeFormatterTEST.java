import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ChronologyUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testConstructor() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        assertNull(formatter.getLocale());
        assertFalse(formatter.isOffsetParsed());
        assertNull(formatter.getChrono());
        assertNull(formatter.getZone());
        assertNull(formatter.getPivotYear());
        assertEquals(2000, formatter.getDefaultYear());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        DateTimeFormatter withLocale = formatter.withLocale(Locale.US);
        assertEquals(Locale.US, withLocale.getLocale());
        DateTimeFormatter withLocale2 = formatter.withLocale(null);
        assertNull(withLocale2.getLocale());
    }

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        assertTrue(formatter.isParser());
    }

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, ChronologyUtils.UTC);
        formatter.printTo(sb, localDateTime);
        assertEquals("12-01-2022", sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, localDateTime);
            assertEquals("12-01-2022", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        Appendable appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, localDateTime);
            assertEquals("12-01-2022", appendable.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testPrintToLong() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        StringBuffer sb = new StringBuffer();
        long millis = 1640995200000L; // 2022-01-01 00:00:00.000 UTC
        formatter.printTo(sb, millis);
        assertEquals("01-01-2022", sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, millis);
            assertEquals("01-01-2022", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        Appendable appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, millis);
            assertEquals("01-01-2022", appendable.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testPrintToReadablePartial() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        StringBuffer sb = new StringBuffer();
        LocalDate localDate = new LocalDate(2022, 1, 1);
        formatter.printTo(sb, localDate);
        assertEquals("01-01-2022", sb.toString());

        Writer writer = new StringWriter();
        try {
            formatter.printTo(writer, localDate);
            assertEquals("01-01-2022", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        Appendable appendable = new StringBuffer();
        try {
            formatter.printTo(appendable, localDate);
            assertEquals("01-01-2022", appendable.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        LocalTime localTime = new LocalTime(12, 0);
        sb.setLength(0);
        formatter.printTo(sb, localTime);
        assertEquals("12:00", sb.toString());

        writer.getBuffer().setLength(0);
        try {
            formatter.printTo(writer, localTime);
            assertEquals("12:00", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        appendable.setLength(0);
        try {
            formatter.printTo(appendable, localTime);
            assertEquals("12:00", appendable.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseInto() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        ReadWritableInstant readWritableInstant = new BaseDateTime(0, 0, 0, 0, 0, 0, 0, ChronologyUtils.UTC);
        String s = "12-01-2022 12:00:00.000";
        int into = formatter.parseInto(readWritableInstant, s, 0);
        assertEquals(s.length(), into);
        assertEquals(1640995200000L, readWritableInstant.getMillis());
        assertEquals(ChronologyUtils.UTC, readWritableInstant.getChronology());

        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, ChronologyUtils.UTC);
        into = formatter.parseInto(new MutableDateTime(localDateTime), s, 0);
        assertEquals(s.length(), into);
        assertEquals(localDateTime.getYear(), ((MutableDateTime) readWritableInstant).getYear());
        assertEquals(localDateTime.getMonthOfYear(), ((MutableDateTime) readWritableInstant).getMonthOfYear());
        assertEquals(localDateTime.getDayOfMonth(), ((MutableDateTime) readWritableInstant).getDayOfMonth());
        assertEquals(localDateTime.getHourOfDay(), ((MutableDateTime) readWritableInstant).getHourOfDay());
        assertEquals(localDateTime.getMinuteOfHour(), ((MutableDateTime) readWritableInstant).getMinuteOfHour());
        assertEquals(localDateTime.getSecondOfMinute(), ((MutableDateTime) readWritableInstant).getSecondOfMinute());
        assertEquals(localDateTime.getMillisOfSecond(), ((MutableDateTime) readWritableInstant).getMillisOfSecond());
        assertEquals(ChronologyUtils.UTC, ((MutableDateTime) readWritableInstant).getChronology());
    }

    @Test
    public void testParseMillis() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss.SSS"), new SimpleDateTimeParser());
        String s = "12-01-2022 12:00:00.000";
        long millis = formatter.parseMillis(s);
        assertEquals(1640995200000L, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss"), new SimpleDateTimeParser());
        String s = "12-01-2022 12:00:00.000";
        formatter.parseMillis(s);
    }

    @Test
    public void testParseLocalDate() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy"), new SimpleDateTimeParser());
        String s = "01-01-2022";
        LocalDate localDate = formatter.parseLocalDate(s);
        assertEquals(2022, localDate.getYear());
        assertEquals(1, localDate.getMonthOfYear());
        assertEquals(1, localDate.getDayOfMonth());
    }

    @Test
    public void testParseLocalTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("HH:mm:ss"), new SimpleDateTimeParser());
        String s = "12:00:00";
        LocalTime localTime = formatter.parseLocalTime(s);
        assertEquals(12, localTime.getHourOfDay());
        assertEquals(0, localTime.getMinuteOfHour());
        assertEquals(0, localTime.getSecondOfMinute());
    }

    @Test
    public void testParseLocalDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss"), new SimpleDateTimeParser());
        String s = "01-01-2022 12:00:00";
        LocalDateTime localDateTime = formatter.parseLocalDateTime(s);
        assertEquals(2022, localDateTime.getYear());
        assertEquals(1, localDateTime.getMonthOfYear());
        assertEquals(1, localDateTime.getDayOfMonth());
        assertEquals(12, localDateTime.getHourOfDay());
        assertEquals(0, localDateTime.getMinuteOfHour());
        assertEquals(0, localDateTime.getSecondOfMinute());
        assertEquals(0, localDateTime.getMillisOfSecond());
    }

    @Test
    public void testParseDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss"), new SimpleDateTimeParser());
        String s = "01-01-2022 12:00:00";
        DateTime dateTime = formatter.parseDateTime(s);
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthOfYear());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getHourOfDay());
        assertEquals(0, dateTime.getMinuteOfHour());
        assertEquals(0, dateTime.getSecondOfMinute());
        assertEquals(0, dateTime.getMillisOfSecond());
        assertEquals(ChronologyUtils.UTC, dateTime.getChronology());
    }

    @Test
    public void testParseMutableDateTime() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat("dd-MM-yyyy HH:mm:ss"), new SimpleDateTimeParser());
        String s = "01-01-2022 12:00:00";
        MutableDateTime mutableDateTime = formatter.parseMutableDateTime(s);
        assertEquals(2022, mutableDateTime.getYear());
        assertEquals(1, mutableDateTime.getMonthOfYear());
        assertEquals(1, mutableDateTime.getDayOfMonth());
        assertEquals(12, mutableDateTime.getHourOfDay());
        assertEquals(0, mutableDateTime.getMinuteOfHour());
        assertEquals(0, mutableDateTime.getSecondOfMinute());
        assertEquals(0, mutableDateTime.getMillisOfSecond());
        assertEquals(ChronologyUtils.UTC, mutableDateTime.getChronology());
    }
}