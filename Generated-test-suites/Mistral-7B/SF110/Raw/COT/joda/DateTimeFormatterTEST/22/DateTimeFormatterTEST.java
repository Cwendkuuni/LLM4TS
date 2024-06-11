import org.joda.time.*;
import org.joda.time.format.*;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatter(new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateTimeParser());

    @Test
    public void testIsPrinter() {
        assertTrue(FORMATTER.isPrinter());
    }

    @Test
    public void testGetPrinter() {
        assertSame(FORMATTER.getPrinter(), new SimpleDateTimeFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    @Test
    public void testIsParser() {
        assertTrue(FORMATTER.isParser());
    }

    @Test
    public void testGetParser() {
        assertSame(FORMATTER.getParser(), new SimpleDateTimeParser());
    }

    @Test
    public void testWithLocale() {
        DateTimeFormatter formatter = FORMATTER.withLocale(Locale.US);
        assertNotSame(FORMATTER, formatter);
        assertEquals(Locale.US, formatter.getLocale());
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.getDefault(), FORMATTER.getLocale());
    }

    @Test
    public void testWithOffsetParsed() {
        DateTimeFormatter formatter = FORMATTER.withOffsetParsed();
        assertNotSame(FORMATTER, formatter);
        assertFalse(FORMATTER.isOffsetParsed());
        assertTrue(formatter.isOffsetParsed());
    }

    @Test
    public void testIsOffsetParsed() {
        assertFalse(FORMATTER.isOffsetParsed());
    }

    @Test
    public void testWithChronology() {
        DateTimeFormatter formatter = FORMATTER.withChronology(ISODate.getInstance().getChronology());
        assertNotSame(FORMATTER, formatter);
        assertEquals(ISODate.getInstance().getChronology(), formatter.getChronology());
    }

    @Test
    public void testGetChronology() {
        assertEquals(DateTimeUtils.getDefault(), FORMATTER.getChronology());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithZoneUTC() {
        FORMATTER.withZoneUTC();
    }

    @Test
    public void testWithZone() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(1);
        DateTimeFormatter formatter = FORMATTER.withZone(zone);
        assertNotSame(FORMATTER, formatter);
        assertEquals(zone, formatter.getZone());
    }

    @Test
    public void testGetZone() {
        assertNull(FORMATTER.getZone());
    }

    @Test
    public void testWithPivotYear() {
        DateTimeFormatter formatter = FORMATTER.withPivotYear(2022);
        assertNotSame(FORMATTER, formatter);
        assertEquals(2022, formatter.getPivotYear().intValue());
    }

    @Test
    public void testGetPivotYear() {
        assertEquals(2000, FORMATTER.getPivotYear().intValue());
    }

    @Test
    public void testWithDefaultYear() {
        DateTimeFormatter formatter = FORMATTER.withDefaultYear(2022);
        assertNotSame(FORMATTER, formatter);
        assertEquals(2022, formatter.getDefaultYear());
    }

    @Test
    public void testGetDefaultYear() {
        assertEquals(2000, FORMATTER.getDefaultYear());
    }

    @Test
    public void testPrintToStringBuffer() {
        StringBuffer sb = new StringBuffer(100);
        DateTime dt = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100, DateTimeZone.UTC).toDateTime();
        FORMATTER.printTo(sb, dt);
        String expected = "2022-01-01T01:01:01.100Z";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriter() throws IOException {
        StringWriter sw = new StringWriter();
        DateTime dt = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100, DateTimeZone.UTC).toDateTime();
        FORMATTER.printTo(sw, dt);
        String expected = "2022-01-01T01:01:01.100Z\n";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendable() throws IOException {
        StringWriter sw = new StringWriter();
        Appendable a = sw;
        DateTime dt = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100, DateTimeZone.UTC).toDateTime();
        FORMATTER.printTo(a, dt);
        String expected = "2022-01-01T01:01:01.100Z";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferLong() {
        StringBuffer sb = new StringBuffer(100);
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        FORMATTER.printTo(sb, millis);
        String expected = "2022-01-01T00:00:00Z";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterLong() throws IOException {
        StringWriter sw = new StringWriter();
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        FORMATTER.printTo(sw, millis);
        String expected = "2022-01-01T00:00:00Z\n";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendableLong() throws IOException {
        StringWriter sw = new StringWriter();
        Appendable a = sw;
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        FORMATTER.printTo(a, millis);
        String expected = "2022-01-01T00:00:00Z";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringBufferReadablePartial() {
        StringBuffer sb = new StringBuffer(100);
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100);
        FORMATTER.printTo(sb, dateTime.toReadablePartial());
        String expected = "2022-01-01 01:01:01.100";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testPrintToWriterReadablePartial() throws IOException {
        StringWriter sw = new StringWriter();
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100);
        FORMATTER.printTo(sw, dateTime.toReadablePartial());
        String expected = "2022-01-01 01:01:01.100\n";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToAppendableReadablePartial() throws IOException {
        StringWriter sw = new StringWriter();
        Appendable a = sw;
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100);
        FORMATTER.printTo(a, dateTime.toReadablePartial());
        String expected = "2022-01-01 01:01:01.100";
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintToStringString() {
        String s = FORMATTER.print(new LocalDateTime(2022, 1, 1, 1, 1, 1, 100));
        String expected = "2022-01-01T01:01:01.100Z";
        assertEquals(expected, s);
    }

    @Test
    public void testPrintToLong() {
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        String s = FORMATTER.print(millis);
        String expected = "2022-01-01T00:00:00Z";
        assertEquals(expected, s);
    }

    @Test
    public void testPrintToReadablePartial() {
        LocalDateTime dateTime = new LocalDateTime(2022, 1, 1, 1, 1, 1, 100);
        String s = FORMATTER.print(dateTime.toReadablePartial());
        String expected = "2022-01-01 01:01:01.100";
        assertEquals(expected, s);
    }

    @Test
    public void testParseIntoReadableInstant() {
        ReadWritableInstant readWritableInstant = new DateTime(1640995200000L, DateTimeUtils.getDefault());
        String s = "2022-01-01T01:01:01.100Z";
        int result = FORMATTER.parseInto(readWritableInstant, s, 0);
        assertEquals(s.length(), result);
        LocalDateTime parsedDateTime = new LocalDateTime(readWritableInstant.getMillis());
        assertEquals("2022-01-01T01:01:01.100", FORMATTER.print(readWritableInstant));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntoReadableInstantInvalidFormat() {
        ReadWritableInstant readWritableInstant = new DateTime(1640995200000L, DateTimeUtils.getDefault());
        String s = "2022-01-01 01:01:01.100Invalid";
        FORMATTER.parseInto(readWritableInstant, s, 0);
    }

    @Test
    public void testParseMillis() {
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        String s = FORMATTER.print(new DateTime(millis, DateTimeUtils.getDefault()));
        LocalDateTime parsedDateTime = FORMATTER.parseLocalDateTime(s);
        assertEquals(millis, parsedDateTime.toDateTime().getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMillisInvalidFormat() {
        long millis = 1640995200000L; // Jan 1, 2022, midnight UTC
        String s = "2022-01-01T01:01:01.100Invalid";
        FORMATTER.parseMillis(s);
    }
}