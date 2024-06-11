import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StubDateTimePrinter(), new StubDateTimeParser());
        assertTrue(formatter.isPrinter());
    }
}