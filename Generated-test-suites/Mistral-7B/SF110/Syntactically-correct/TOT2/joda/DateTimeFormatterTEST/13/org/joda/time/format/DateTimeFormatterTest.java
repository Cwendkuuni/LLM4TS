import org.joda.time.*;
import org.joda.time.format.*;
import org.junit.*;
import static org.junit.Assert.*;;

public class DateTimeFormatterTest {

    @Test
    public void testPrintTo() {
        DateTimeFormatter formatter = new DateTimeFormatter(new TestDateTimePrinter(), new TestDateTimeParser());
        Instant instant = new DateTime(2022, 1, 1, 12, 0, 0, 0, DateTimeZone.UTC).toInstant();
        StringBuffer sb = new StringBuffer();

        formatter.printTo(sb, instant);
        String expected = "2022-01-01T12:00:00Z";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }
}

class TestDateTimePrinter implements DateTimePrinter {
    @Override
    public void printTo(StringBuffer sb, ReadableInstant readableInstant) {
        sb.append("20xx-MM-dd'T'HH:mm:ss'Z'");
        sb.append(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(readableInstant));
    }

    @Override
    public void printTo(Writer writer, ReadableInstant readableInstant) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(Appendable appendable, ReadableInstant readableInstant) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(StringBuffer sb, long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(Writer writer, long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(Appendable appendable, long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(StringBuffer sb, ReadablePartial readablePartial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(Writer writer, ReadablePartial readablePartial) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTo(Appendable appendable, ReadablePartial readablePartial) throws IOException {
        throw new UnsupportedOperationException();
    }
}

class TestDateTimeParser implements DateTimeParser {
    @Override
    public void parseInto(ReadWritableInstant readWritableInstant, String s, int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long parseMillis(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDate parseLocalDate(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalTime parseLocalTime(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime parseLocalDateTime(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DateTime parseDateTime(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableDateTime parseMutableDateTime(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int parseInto(DateTimeParserBucket dateTimeParserBucket, String s, int n) {
        throw new UnsupportedOperationException();
    }
}