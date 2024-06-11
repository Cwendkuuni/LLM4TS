import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {
    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        assertTrue(formatter.isPrinter());
    }
}