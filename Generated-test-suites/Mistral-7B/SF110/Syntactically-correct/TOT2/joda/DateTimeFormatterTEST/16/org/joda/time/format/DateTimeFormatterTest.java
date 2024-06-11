import org.junit.Test;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeFormatterTest {
    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        assertTrue(formatter.isPrinter());
    }
}